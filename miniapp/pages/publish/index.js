const { showToast } = require('../../utils/index.js')

Page({
  data: {
    showPopup: true,
    submitting: false
  },

  onLoad() {
    this.setData({ showPopup: true })
  },

  closePopup() {
    this.setData({ showPopup: false })
  },

  checkLogin() {
    const app = getApp()
    const userInfo = app.getUserInfo()
    if (!userInfo || Object.keys(userInfo).length === 0) {
      showToast('请先登录')
      wx.navigateTo({ url: '/pages/login/login' })
      return false
    }
    return true
  },

  checkVerify(userInfo) {
    if (userInfo.verifyStatus !== '1') {
      showToast('请先完成实名认证')
      wx.navigateTo({ url: '/pages/profile/verify' })
      return false
    }
    return true
  },

  handlePublishHouse() {
    const app = getApp()
    if (!this.checkLogin()) return
    const userInfo = app.getUserInfo()
    if (!this.checkVerify(userInfo)) return
    wx.navigateTo({ url: '/pages/house/publish' })
  },

  handleApplyPhonebook() {
    const app = getApp()
    if (!this.checkLogin()) return
    wx.navigateTo({ url: '/pages/phonebook/apply' })
  }
})