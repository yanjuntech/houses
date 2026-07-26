const utils = require('./utils/index.js')

// 登录页路径
const LOGIN_PAGE = '/pages/login/login'

App({
  onLaunch() {
    console.log('小程序启动')
    // 启动时同步本地登录信息到 globalData
    const userInfo = this.getUserInfo()
    const token = wx.getStorageSync('token') || ''
    this.globalData.userInfo = userInfo
    this.globalData.token = token
  },

  onShow() {
    console.log('小程序显示')
  },

  onHide() {
    console.log('小程序隐藏')
  },

  globalData: {
    userInfo: null,
    token: ''
  },

  /**
   * 检查是否已登录，未登录则跳转登录页
   * @returns {boolean} 是否已登录
   */
  checkLogin() {
    const token = wx.getStorageSync('token')
    const userInfo = this.getUserInfo()
    if (!token || !userInfo || utils.isEmpty(userInfo)) {
      wx.reLaunch({ url: LOGIN_PAGE })
      return false
    }
    return true
  },

  /**
   * 从本地存储获取用户信息
   * @returns {Object|null} 用户信息对象
   */
  getUserInfo() {
    return utils.getStorage('userInfo', null)
  },

  /**
   * 退出登录：清除登录信息并跳转登录页
   */
  logout() {
    try {
      wx.removeStorageSync('token')
      wx.removeStorageSync('userInfo')
    } catch (e) {
      console.error('清除登录信息失败:', e)
    }
    this.globalData.userInfo = null
    this.globalData.token = ''
    wx.reLaunch({ url: LOGIN_PAGE })
  }
})
