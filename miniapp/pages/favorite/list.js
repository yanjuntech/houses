const { favoriteApi } = require('../../utils/api.js')
const { showToast, showLoading, hideLoading, showModal, getUserInfo } = require('../../utils/index.js')

Page({
  data: {
    // 收藏列表
    favoriteList: [],
    loading: false,
    userId: ''
  },

  onLoad() {
    // 优先从 globalData 读取，回退到本地存储
    const app = getApp()
    const userInfo = (app && typeof app.getUserInfo === 'function') ? app.getUserInfo() : getUserInfo()
    const userId = userInfo && userInfo.userId ? userInfo.userId : ''
    this.setData({ userId })
    this.loadFavorites()
  },

  // 下拉刷新
  onPullDownRefresh() {
    this.loadFavorites(() => {
      wx.stopPullDownRefresh()
    })
  },

  /**
   * 加载收藏列表
   * @param {Function} done 加载完成回调
   */
  loadFavorites(done) {
    if (this.data.loading) {
      done && done()
      return
    }
    const userId = this.data.userId
    if (!userId) {
      showToast('请先登录')
      done && done()
      return
    }

    this.setData({ loading: true })
    showLoading('加载中')

    favoriteApi.userFavorite(userId).then(res => {
      // 兼容数组与 {rows}/{list} 两种返回结构
      let list = []
      if (Array.isArray(res)) {
        list = res
      } else if (res && Array.isArray(res.rows)) {
        list = res.rows
      } else if (res && Array.isArray(res.list)) {
        list = res.list
      }

      // 规范化封面图与收藏时间
      const normalized = list.map(item => ({
        ...item,
        coverImage: this.parseCoverImage(item),
        favoriteTime: this.formatFavoriteTime(item.createTime)
      }))

      this.setData({
        favoriteList: normalized,
        loading: false
      })
    }).catch(() => {
      this.setData({ loading: false })
      showToast('收藏列表加载失败')
    }).finally(() => {
      hideLoading()
      done && done()
    })
  },

  // 解析房源封面图，支持字符串、逗号分隔、数组三种格式
  parseCoverImage(item) {
    let img = item.houseImage || item.image || item.cover || ''
    if (Array.isArray(img) && img.length > 0) {
      img = img[0]
    } else if (typeof img === 'string' && img) {
      img = img.split(',')[0]
    }
    return img
  },

  // 格式化收藏时间
  formatFavoriteTime(time) {
    if (!time) return ''
    const d = new Date(time)
    if (isNaN(d.getTime())) return String(time)
    const year = d.getFullYear()
    const month = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  },

  // 跳转房源详情
  goDetail(e) {
    const houseId = e.currentTarget.dataset.id
    if (!houseId) return
    wx.navigateTo({
      url: `/pages/house/detail?houseId=${houseId}`
    })
  },

  // 取消收藏（弹窗确认）
  handleCancel(e) {
    const houseId = e.currentTarget.dataset.id
    if (!houseId) return

    showModal('提示', '确定要取消收藏该房源吗？', {
      confirmText: '取消收藏',
      confirmColor: '#f56c6c'
    }).then(confirm => {
      if (!confirm) return
      this.cancelFavorite(houseId)
    })
  },

  // 执行取消收藏请求
  cancelFavorite(houseId) {
    const userId = this.data.userId
    if (!userId || !houseId) return

    showLoading('取消中')
    favoriteApi.cancel(userId, houseId).then(() => {
      const list = this.data.favoriteList.filter(item => item.houseId !== houseId)
      this.setData({ favoriteList: list })
      showToast('已取消收藏', 'success')
    }).catch(() => {
      showToast('取消收藏失败')
    }).finally(() => {
      hideLoading()
    })
  },

  // 空状态：去逛逛（house/list 是 tab 页，需用 switchTab）
  goHouseList() {
    wx.switchTab({
      url: '/pages/house/list'
    })
  }
})
