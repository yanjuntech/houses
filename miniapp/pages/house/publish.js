const app = getApp()
const { communityApi, houseApi, mallApi, uploadApi } = require('../../utils/api.js')
const { showToast, showLoading, hideLoading, showModal, formatDate, requireLogin } = require('../../utils/index.js')
const { PHONE_REG } = require('../../utils/validate.js')

// 房屋类型选项
const HOUSE_TYPE_OPTIONS = ['整租', '合租', '单间']
// 朝向选项
const DIRECTION_OPTIONS = ['东', '南', '西', '北', '南北']
// 装修选项
const DECORATION_OPTIONS = ['毛坯', '简装', '精装', '豪装']

Page({
  data: {
    // 用户信息
    userInfo: null,
    // 配额信息
    quotaCount: 0,            // 剩余发布次数
    quotaExpireText: '',      // 有效期截止时间展示文本
    quotaLoaded: false,       // 配额是否加载完成
    quotaInsufficient: false, // 配额是否不足
    // 小区选项
    communityOptions: [{ communityId: '', communityName: '请选择小区' }],
    communityIndex: 0,
    // 房屋类型
    houseTypeOptions: HOUSE_TYPE_OPTIONS,
    houseTypeIndex: -1,
    // 朝向
    directionOptions: DIRECTION_OPTIONS,
    directionIndex: -1,
    // 装修
    decorationOptions: DECORATION_OPTIONS,
    decorationIndex: -1,
    // 图片列表（本地临时路径）
    imageList: [],
    // 表单数据
    form: {
      communityId: '',
      houseTitle: '',
      houseType: '',
      houseLayout: '',
      houseArea: '',
      housePrice: '',
      houseFloor: '',
      houseDirection: '',
      houseDecoration: '',
      houseAddress: '',
      houseImage: '',
      houseDesc: '',
      contactName: '',
      contactPhone: ''
    },
    // 提交状态
    submitting: false
  },

  onLoad() {
    if (!requireLogin()) return
    const userInfo = app.getUserInfo()
    this.setData({ userInfo })
    this.loadCommunityOptions()
    this.loadQuota(userInfo.userId)
  },

  // ===================== 数据加载 =====================

  // 加载小区列表
  loadCommunityOptions() {
    communityApi.selectAll().then(res => {
      const list = Array.isArray(res) ? res : (res && res.rows) || []
      const options = [{ communityId: '', communityName: '请选择小区' }, ...list]
      this.setData({ communityOptions: options })
    }).catch(() => {
      // 加载失败仅保留占位项
      this.setData({ communityOptions: [{ communityId: '', communityName: '请选择小区' }] })
    })
  },

  // 加载用户配额
  loadQuota(userId) {
    mallApi.userQuota(userId).then(res => {
      const data = res || {}
      // 兼容多种返回字段命名
      const remain = data.remainCount !== undefined ? data.remainCount
        : (data.remainingCount !== undefined ? data.remainingCount
          : (data.publishCount !== undefined ? data.publishCount : 0))
      const expire = data.expireTime || data.endTime || data.validityTime || data.expireDate || ''
      const expireText = expire ? formatDate(new Date(expire), 'YYYY-MM-DD') : '长期有效'

      this.setData({
        quotaCount: remain,
        quotaExpireText: expireText,
        quotaLoaded: true,
        quotaInsufficient: remain <= 0
      })
    }).catch(() => {
      // 配额加载失败，默认允许提交（避免阻断流程）
      this.setData({
        quotaCount: 0,
        quotaExpireText: '获取失败',
        quotaLoaded: true,
        quotaInsufficient: false
      })
    })
  },

  // ===================== 表单输入处理 =====================

  // 通用文本输入
  onInput(e) {
    const field = e.currentTarget.dataset.field
    if (!field) return
    this.setData({ [`form.${field}`]: e.detail.value })
  },

  // 小区选择
  onCommunityChange(e) {
    const index = Number(e.detail.value)
    const community = this.data.communityOptions[index] || {}
    this.setData({
      communityIndex: index,
      'form.communityId': community.communityId || ''
    })
  },

  // 房屋类型选择
  onHouseTypeChange(e) {
    const index = Number(e.detail.value)
    this.setData({
      houseTypeIndex: index,
      'form.houseType': HOUSE_TYPE_OPTIONS[index] || ''
    })
  },

  // 朝向选择
  onDirectionChange(e) {
    const index = Number(e.detail.value)
    this.setData({
      directionIndex: index,
      'form.houseDirection': DIRECTION_OPTIONS[index] || ''
    })
  },

  // 装修选择
  onDecorationChange(e) {
    const index = Number(e.detail.value)
    this.setData({
      decorationIndex: index,
      'form.houseDecoration': DECORATION_OPTIONS[index] || ''
    })
  },

  // ===================== 图片处理 =====================

  // 选择图片
  chooseImage() {
    const remain = 9 - this.data.imageList.length
    if (remain <= 0) {
      showToast('最多上传9张图片')
      return
    }
    wx.chooseMedia({
      count: remain,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      sizeType: ['compressed'],
      success: (res) => {
        const newPaths = (res.tempFiles || []).map(f => f.tempFilePath)
        const imageList = this.data.imageList.concat(newPaths).slice(0, 9)
        this.setData({ imageList })
      }
    })
  },

  // 删除图片
  deleteImage(e) {
    const index = Number(e.currentTarget.dataset.index)
    if (isNaN(index)) return
    const imageList = this.data.imageList.slice()
    imageList.splice(index, 1)
    this.setData({ imageList })
  },

  // 预览图片
  previewImage(e) {
    const current = e.currentTarget.dataset.url
    const urls = this.data.imageList
    if (!urls.length) return
    wx.previewImage({ current, urls })
  },

  // ===================== 跳转商城 =====================

  goMall() {
    wx.navigateTo({
      url: '/pages/mall/index',
      fail: () => {
        // 商城页可能尚未注册，提示用户
        showToast('商城页暂未开放')
      }
    })
  },

  // ===================== 表单校验 =====================

  // 校验表单，返回 { valid, message }
  validate() {
    const { form } = this.data

    if (!form.communityId) {
      return { valid: false, message: '请选择小区' }
    }
    if (!form.houseTitle || !form.houseTitle.trim()) {
      return { valid: false, message: '请输入房屋标题' }
    }
    if (!form.houseType) {
      return { valid: false, message: '请选择房屋类型' }
    }
    if (!form.houseArea || isNaN(Number(form.houseArea)) || Number(form.houseArea) <= 0) {
      return { valid: false, message: '请输入有效的房屋面积' }
    }
    if (!form.housePrice || isNaN(Number(form.housePrice)) || Number(form.housePrice) <= 0) {
      return { valid: false, message: '请输入有效的月租金' }
    }
    if (!form.houseAddress || !form.houseAddress.trim()) {
      return { valid: false, message: '请输入房屋地址' }
    }
    if (!form.contactName || !form.contactName.trim()) {
      return { valid: false, message: '请输入联系人姓名' }
    }
    if (!form.contactPhone) {
      return { valid: false, message: '请输入联系电话' }
    }
    if (!PHONE_REG.test(form.contactPhone)) {
      return { valid: false, message: '请输入正确的手机号' }
    }

    return { valid: true }
  },

  // ===================== 提交发布 =====================

  handleSubmit() {
    if (this.data.submitting) return

    // 配额校验
    if (this.data.quotaLoaded && this.data.quotaInsufficient) {
      showModal('提示', '发布次数不足，请前往商城兑换', {
        confirmText: '去兑换',
        cancelText: '取消'
      }).then(confirm => {
        if (confirm) this.goMall()
      })
      return
    }

    // 表单校验
    const result = this.validate()
    if (!result.valid) {
      showToast(result.message)
      return
    }

    const userInfo = this.data.userInfo
    this.setData({ submitting: true })
    showLoading('发布中')

    // 先上传图片到服务器，再将返回的 URL 拼接为 houseImage 字段
    const localImages = this.data.imageList || []
    const uploadTask = localImages.length
      ? Promise.all(localImages.map(filePath => uploadApi.uploadImage(filePath)))
      : Promise.resolve([])

    uploadTask.then(uploadedUrls => {
      const houseImage = uploadedUrls.filter(Boolean).join(',')

      const data = {
        userId: userInfo.userId,
        communityId: this.data.form.communityId,
        houseTitle: this.data.form.houseTitle.trim(),
        houseType: this.data.form.houseType,
        houseLayout: this.data.form.houseLayout || '',
        houseArea: Number(this.data.form.houseArea),
        housePrice: Number(this.data.form.housePrice),
        houseFloor: this.data.form.houseFloor || '',
        houseDirection: this.data.form.houseDirection || '',
        houseDecoration: this.data.form.houseDecoration || '',
        houseAddress: this.data.form.houseAddress.trim(),
        houseImage,
        houseDesc: this.data.form.houseDesc || '',
        contactName: this.data.form.contactName.trim(),
        contactPhone: this.data.form.contactPhone,
        status: 0
      }

      return houseApi.add(data)
    }).then(() => {
      hideLoading()
      showToast('发布成功', 'success')
      // 延迟返回上一页，给提示留出展示时间
      setTimeout(() => {
        wx.navigateBack({
          fail: () => {
            // 无法返回上一页时，跳转房源列表（tab 页需用 switchTab）
            wx.switchTab({ url: '/pages/house/list' })
          }
        })
      }, 1000)
    }).catch(err => {
      hideLoading()
      this.setData({ submitting: false })
      showToast(err && err.message ? err.message : '发布失败')
    })
  }
})
