const app = getApp()
const { houseApi, favoriteApi, browseApi } = require('../../utils/api.js')
const { showToast, showLoading, hideLoading, showModal, normalizeList } = require('../../utils/index.js')

Page({
  data: {
    houseId: '',
    houseInfo: null,
    imageList: [],   // swiper 图片列表
    isFavorited: false,
    loading: true
  },

  onLoad(options) {
    const houseId = options && options.houseId ? options.houseId : ''
    if (!houseId) {
      showToast('房源ID缺失')
      this.setData({ loading: false })
      return
    }
    this.setData({ houseId })
    this.loadDetail()
  },

  // 加载房源详情
  loadDetail() {
    const { houseId } = this.data
    showLoading('加载中')
    this.setData({ loading: true })

    houseApi.detail(houseId).then(res => {
      const info = res || {}
      // 规范化图片字段为数组
      const imageList = this.parseImageList(info)
      this.setData({
        houseInfo: info,
        imageList,
        loading: false
      })
      hideLoading()

      // 记录浏览记录（需要登录）
      this.recordBrowse(info)
      // 检查收藏状态（需要登录）
      this.checkFavorite()
    }).catch(() => {
      this.setData({ loading: false })
      hideLoading()
      showToast('房源详情加载失败')
    })
  },

  // 解析房源图片为数组，支持字符串、逗号分隔、数组三种格式
  parseImageList(info) {
    let images = info.houseImage || info.image || info.images || ''
    if (Array.isArray(images)) {
      return images.filter(url => url)
    }
    if (typeof images === 'string' && images) {
      return images.split(',').map(url => url.trim()).filter(url => url)
    }
    return []
  },

  // 记录浏览记录
  recordBrowse(info) {
    const userInfo = app.getUserInfo()
    if (!userInfo || !userInfo.userId) {
      // 未登录时不记录浏览
      return
    }
    browseApi.record({
      userId: userInfo.userId,
      houseId: this.data.houseId,
      houseTitle: info.houseTitle || ''
    }).catch(() => {
      // 浏览记录记录失败不影响主流程
    })
  },

  // 检查是否已收藏
  checkFavorite() {
    const userInfo = app.getUserInfo()
    if (!userInfo || !userInfo.userId) {
      return
    }
    favoriteApi.userFavorite(userInfo.userId).then(res => {
      const list = normalizeList(res)
      const favorited = list.some(item => String(item.houseId) === String(this.data.houseId))
      this.setData({ isFavorited: favorited })
    }).catch(() => {
      // 收藏状态查询失败，默认未收藏
      this.setData({ isFavorited: false })
    })
  },

  // 切换收藏状态
  toggleFavorite() {
    const userInfo = app.getUserInfo()
    if (!userInfo || !userInfo.userId) {
      showModal('提示', '请先登录后再收藏房源').then(confirm => {
        if (confirm) {
          app.checkLogin()
        }
      })
      return
    }

    const userId = userInfo.userId
    const { houseId, isFavorited } = this.data

    if (isFavorited) {
      // 已收藏 -> 取消收藏
      favoriteApi.cancel(userId, houseId).then(() => {
        this.setData({ isFavorited: false })
        showToast('已取消收藏', 'none')
      }).catch(() => {
        showToast('取消收藏失败')
      })
    } else {
      // 未收藏 -> 添加收藏
      favoriteApi.add({ userId, houseId }).then(() => {
        this.setData({ isFavorited: true })
        showToast('收藏成功', 'success')
      }).catch(() => {
        showToast('收藏失败')
      })
    }
  },

  // 拨打房东电话
  callOwner() {
    const phone = this.data.houseInfo && (this.data.houseInfo.ownerPhone || this.data.houseInfo.landlordPhone || this.data.houseInfo.phone)
    if (!phone) {
      showToast('暂无房东联系方式')
      return
    }
    wx.makePhoneCall({
      phoneNumber: String(phone),
      fail: () => {
        showToast('拨号已取消')
      }
    })
  },

  // 预览大图
  previewImage(e) {
    const current = e.currentTarget.dataset.url
    const urls = this.data.imageList
    if (!urls.length) return
    wx.previewImage({
      current,
      urls
    })
  }
})
