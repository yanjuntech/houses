// 电话簿页面
const api = require('../../utils/api.js')
const app = getApp()

Page({
  data: {
    // 全部商家数据
    allMerchants: [],
    // 筛选后的商家
    filteredMerchants: [],
    // 当前分类，空字符串表示全部
    currentCategory: '',
    // 搜索关键词
    keyword: '',
    // 分类列表
    categories: [
      { value: '', label: '全部' },
      { value: '1', label: '餐饮美食' },
      { value: '2', label: '快递服务' },
      { value: '3', label: '超市便利' },
      { value: '4', label: '便民服务' },
      { value: '5', label: '维修服务' },
      { value: '6', label: '医疗健康' },
      { value: '7', label: '教育培训' },
      { value: '8', label: '建材五金' },
      { value: '9', label: '装修服务' },
      { value: '10', label: '宠物服务' },
      { value: '11', label: '母婴用品' },
      { value: '12', label: '法律服务' },
      { value: '13', label: '房产服务' }
    ],
    // 申请收录弹窗显隐
    showApplyPopup: false,
    // 申请表单
    applyForm: {
      merchantName: '',
      phone: '',
      category: '',
      address: ''
    },
    // 申请表单中分类选择索引
    applyCategoryIndex: 0
  },

  onLoad() {
    this.loadMerchants()
  },

  /**
   * 加载全部商家数据
   */
  loadMerchants() {
    wx.showLoading({ title: '加载中', mask: true })
    api.phonebookApi.selectAll()
      .then((res) => {
        const list = Array.isArray(res) ? res : (res && res.list) || []
        this.setData({
          allMerchants: list,
          filteredMerchants: list
        })
      })
      .catch((err) => {
        console.error('加载商家列表失败:', err)
      })
      .finally(() => {
        wx.hideLoading()
      })
  },

  /**
   * 选择分类
   */
  filterByCategory(e) {
    const value = e.currentTarget.dataset.value || ''
    this.setData({ currentCategory: value }, () => {
      this.doFilter()
    })
  },

  /**
   * 搜索输入
   */
  handleSearch(e) {
    const keyword = e.detail.value || ''
    this.setData({ keyword }, () => {
      this.doFilter()
    })
  },

  /**
   * 清空搜索
   */
  clearKeyword() {
    this.setData({ keyword: '' }, () => {
      this.doFilter()
    })
  },

  /**
   * 执行筛选（分类 + 关键词）
   */
  doFilter() {
    const { allMerchants, currentCategory, keyword } = this.data
    let list = allMerchants
    // 分类筛选
    if (currentCategory) {
      list = list.filter((item) => String(item.category) === String(currentCategory))
    }
    // 关键词模糊匹配商家名称
    if (keyword) {
      const kw = keyword.toLowerCase()
      list = list.filter((item) => {
        const name = (item.merchantName || item.merchant_name || item.name || '').toString().toLowerCase()
        return name.indexOf(kw) > -1
      })
    }
    this.setData({ filteredMerchants: list })
  },

  /**
   * 拨打电话
   */
  callPhone(e) {
    const phone = e.currentTarget.dataset.phone
    if (!phone) {
      wx.showToast({ title: '暂无电话', icon: 'none' })
      return
    }
    wx.makePhoneCall({
      phoneNumber: String(phone),
      fail: (err) => {
        console.error('拨打电话失败:', err)
      }
    })
  },

  /**
   * 打开申请收录弹窗
   */
  handleApply() {
    this.setData({
      showApplyPopup: true,
      applyForm: {
        merchantName: '',
        phone: '',
        category: '',
        address: ''
      },
      applyCategoryIndex: 0
    })
  },

  /**
   * 关闭申请收录弹窗
   */
  closeApplyPopup() {
    this.setData({ showApplyPopup: false })
  },

  /**
   * 阻止冒泡
   */
  stopPropagation() {},

  /**
   * 申请表单输入：商家名称
   */
  onMerchantNameInput(e) {
    this.setData({ 'applyForm.merchantName': e.detail.value })
  },

  /**
   * 申请表单输入：电话
   */
  onPhoneInput(e) {
    this.setData({ 'applyForm.phone': e.detail.value })
  },

  /**
   * 申请表单输入：地址
   */
  onAddressInput(e) {
    this.setData({ 'applyForm.address': e.detail.value })
  },

  /**
   * 申请表单选择分类
   */
  onCategoryChange(e) {
    const index = Number(e.detail.value)
    // 分类列表中第 0 项是"全部"，提交申请时不应选"全部"，从第 1 项开始
    const categories = this.data.categories
    const selected = categories[index] || categories[1]
    this.setData({
      applyCategoryIndex: index,
      'applyForm.category': selected ? selected.value : ''
    })
  },

  /**
   * 提交申请收录
   */
  submitApply() {
    const { merchantName, phone, category, address } = this.data.applyForm
    if (!merchantName || !merchantName.trim()) {
      wx.showToast({ title: '请填写商家名称', icon: 'none' })
      return
    }
    if (!phone || !phone.trim()) {
      wx.showToast({ title: '请填写联系电话', icon: 'none' })
      return
    }
    if (!category) {
      wx.showToast({ title: '请选择商家分类', icon: 'none' })
      return
    }
    if (!address || !address.trim()) {
      wx.showToast({ title: '请填写商家地址', icon: 'none' })
      return
    }

    // 获取当前用户信息
    const userInfo = app.getUserInfo() || {}
    const submitData = {
      merchantName: merchantName.trim(),
      phone: phone.trim(),
      category,
      address: address.trim(),
      userId: userInfo.id || userInfo.userId || '',
      applicantName: userInfo.nickName || userInfo.nickname || ''
    }

    wx.showLoading({ title: '提交中', mask: true })
    api.phonebookApi.apply(submitData)
      .then(() => {
        wx.showToast({ title: '提交成功，待审核', icon: 'success' })
        this.setData({ showApplyPopup: false })
      })
      .catch((err) => {
        console.error('申请收录失败:', err)
      })
      .finally(() => {
        wx.hideLoading()
      })
  }
})
