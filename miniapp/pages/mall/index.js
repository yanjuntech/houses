const app = getApp()
const { mallApi } = require('../../utils/api.js')
const { showToast, showLoading, hideLoading, showModal } = require('../../utils/index.js')

Page({
  data: {
    // 用户配额信息
    quota: {
      inviteCount: 0,
      availableCount: 0,
      usedCount: 0
    },
    // 商品列表
    productList: [],
    // 加载状态
    loading: false,
    exchanging: false,
    // 用户ID
    userId: ''
  },

  onLoad() {
    const userInfo = app.getUserInfo()
    if (userInfo && userInfo.userId) {
      this.setData({ userId: userInfo.userId })
    }
    this.initData()
  },

  onShow() {
    // 页面显示时刷新配额（从兑换记录页返回时同步）
    if (this.data.userId) {
      this.loadQuota()
    }
  },

  // 初始化数据
  initData() {
    if (!this.data.userId) {
      showToast('请先登录')
      return
    }
    this.loadQuota()
    this.loadProducts()
  },

  // 加载用户配额
  loadQuota() {
    if (!this.data.userId) return
    mallApi.userQuota(this.data.userId).then(res => {
      const quota = res || {}
      this.setData({
        quota: {
          inviteCount: quota.inviteCount || 0,
          availableCount: quota.availableCount || 0,
          usedCount: quota.usedCount || 0
        }
      })
      // 配额更新后重新计算商品可兑换状态
      this.refreshExchangeState()
    }).catch(() => {
      // 配额加载失败静默处理
    })
  },

  // 加载商品列表
  loadProducts() {
    this.setData({ loading: true })
    mallApi.productList().then(res => {
      let list = []
      if (Array.isArray(res)) {
        list = res
      } else if (res && Array.isArray(res.rows)) {
        list = res.rows
      } else if (res && Array.isArray(res.list)) {
        list = res.list
      }
      // 仅展示上架商品（status=0）
      const products = list.filter(item => item.status === 0 || item.status === '0')
      this.setData({ productList: products, loading: false })
      // 商品加载后根据当前配额刷新可兑换状态
      this.refreshExchangeState()
    }).catch(() => {
      this.setData({ loading: false })
      showToast('商品加载失败')
    })
  },

  // 根据当前配额刷新商品可兑换状态
  refreshExchangeState() {
    const { availableCount } = this.data.quota
    const productList = this.data.productList.map(item => ({
      ...item,
      canExchange: this.checkCanExchange(item, availableCount)
    }))
    this.setData({ productList })
  },

  // 判断商品是否可兑换：可用次数 >= 所需人数 且 库存 > 0
  checkCanExchange(product, availableCount) {
    const available = availableCount !== undefined ? availableCount : this.data.quota.availableCount
    const required = Number(product.requiredCount) || 0
    const stock = product.stock
    // 库存为 undefined/null 时不限制库存（视为有库存），否则需 > 0
    const hasStock = stock === undefined || stock === null ? true : Number(stock) > 0
    return available >= required && hasStock
  },

  // 跳转兑换记录页
  goRecord() {
    wx.navigateTo({
      url: '/pages/mall/record'
    })
  },

  // 点击兑换按钮
  handleExchange(e) {
    const product = e.currentTarget.dataset.product
    if (!product || this.data.exchanging) return

    if (!product.canExchange) {
      const required = Number(product.requiredCount) || 0
      const available = this.data.quota.availableCount
      const stock = product.stock
      if (stock !== undefined && stock !== null && Number(stock) <= 0) {
        showToast('库存不足')
      } else if (available < required) {
        showToast(`可兑换次数不足，还需邀请${required - available}人`)
      } else {
        showToast('暂不可兑换')
      }
      return
    }

    const userInfo = app.getUserInfo()
    if (!userInfo || !userInfo.userId) {
      showModal('提示', '请先登录后再兑换').then(confirm => {
        if (confirm) {
          app.checkLogin()
        }
      })
      return
    }

    showModal(
      '确认兑换',
      `是否消耗${product.requiredCount}人邀请次数兑换「${product.productName}」？`
    ).then(confirm => {
      if (!confirm) return
      this.doExchange(userInfo.userId, product.productId)
    })
  },

  // 执行兑换
  doExchange(userId, productId) {
    this.setData({ exchanging: true })
    showLoading('兑换中')
    mallApi.exchange({ userId, productId }).then(() => {
      hideLoading()
      this.setData({ exchanging: false })
      showToast('兑换成功', 'success')
      // 兑换成功后刷新配额和商品列表
      this.loadQuota()
      this.loadProducts()
    }).catch(() => {
      hideLoading()
      this.setData({ exchanging: false })
      // 错误提示已在 request 中处理
    })
  }
})
