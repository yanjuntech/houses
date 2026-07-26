// 登录页
const api = require('../../utils/api.js')
const utils = require('../../utils/index.js')
const app = getApp()

Page({
  data: {
    // 手机号
    phone: '',
    // 是否勾选用户协议
    agreed: false,
    // 加载状态
    loading: false,
    // 登录成功后跳转的目标页面
    redirect: ''
  },

  onLoad(options) {
    // 接收 redirect 参数，登录成功后跳转
    if (options && options.redirect) {
      this.setData({ redirect: decodeURIComponent(options.redirect) })
    }
  },

  // 手机号输入
  onPhoneInput(e) {
    this.setData({ phone: e.detail.value })
  },

  // 切换协议勾选状态
  toggleAgreement() {
    this.setData({ agreed: !this.data.agreed })
  },

  // 校验是否勾选用户协议
  checkAgreement() {
    if (!this.data.agreed) {
      utils.showToast('请先阅读并同意用户协议')
      return false
    }
    return true
  },

  // 校验手机号格式（11位数字）
  validatePhone(phone) {
    return /^1\d{10}$/.test(phone)
  },

  // 手机号登录
  handlePhoneLogin() {
    if (this.data.loading) return

    // 校验协议勾选
    if (!this.checkAgreement()) return

    const { phone } = this.data

    // 校验手机号格式
    if (!phone) {
      utils.showToast('请输入手机号')
      return
    }
    if (!this.validatePhone(phone)) {
      utils.showToast('请输入正确的手机号')
      return
    }

    this.setData({ loading: true })

    // 调用手机号登录接口
    api.userApi.loginByPhone(phone)
      .then(data => {
        this.loginSuccess(data)
      })
      .catch(err => {
        console.error('手机号登录失败:', err)
      })
      .finally(() => {
        this.setData({ loading: false })
      })
  },

  // 微信一键登录
  handleWechatLogin() {
    if (this.data.loading) return

    // 校验协议勾选
    if (!this.checkAgreement()) return

    this.setData({ loading: true })

    // 1. 调用 wx.login 获取 code
    wx.login({
      success: loginRes => {
        if (!loginRes.code) {
          utils.showToast('微信登录失败')
          this.setData({ loading: false })
          return
        }

        // 2. 调用 wx.getUserProfile 获取用户信息
        wx.getUserProfile({
          desc: '用于完善用户资料',
          success: profileRes => {
            const userInfo = profileRes.userInfo || {}
            // 3. 调用微信登录接口
            api.userApi.loginByWechat({
              openid: loginRes.code,
              wechatNickname: userInfo.nickName,
              wechatAvatar: userInfo.avatarUrl
            })
              .then(data => {
                this.loginSuccess(data)
              })
              .catch(err => {
                console.error('微信登录失败:', err)
              })
              .finally(() => {
                this.setData({ loading: false })
              })
          },
          fail: () => {
            utils.showToast('已取消授权')
            this.setData({ loading: false })
          }
        })
      },
      fail: () => {
        utils.showToast('微信登录失败')
        this.setData({ loading: false })
      }
    })
  },

  // 登录成功统一处理：保存用户信息、更新 globalData、跳转页面
  loginSuccess(data) {
    if (!data) {
      utils.showToast('登录失败，请重试')
      return
    }

    // 保存用户信息到本地存储
    utils.setStorage('userInfo', data)
    // 保存 token（使用 userId 作为 token）
    wx.setStorageSync('token', data.userId)

    // 更新 app.globalData
    app.globalData.userInfo = data
    app.globalData.token = data.userId

    utils.showToast('登录成功', 'success')

    // 跳转回首页或 redirect 页面
    setTimeout(() => {
      const redirect = this.data.redirect
      if (redirect && redirect.indexOf('pages/index/index') === -1) {
        // 非 tabBar 页面使用 redirectTo
        wx.redirectTo({ url: redirect })
      } else {
        // 首页是 tabBar 页面，使用 switchTab
        wx.switchTab({ url: '/pages/index/index' })
      }
    }, 800)
  },

  // 查看用户协议
  showUserAgreement() {
    wx.showModal({
      title: '用户协议',
      content: '此处为用户协议内容，实际项目中请替换为完整的用户协议文本。',
      showCancel: false,
      confirmText: '我知道了'
    })
  },

  // 查看隐私政策
  showPrivacyPolicy() {
    wx.showModal({
      title: '隐私政策',
      content: '此处为隐私政策内容，实际项目中请替换为完整的隐私政策文本。',
      showCancel: false,
      confirmText: '我知道了'
    })
  }
})
