// 实名认证页
const app = getApp()
const { showToast, showModal, getUserInfo, setUserInfo } = require('../../utils/index.js')

Page({
  data: {
    // 是否已认证
    verified: false,
    // 脱敏显示的真实姓名
    maskedRealName: '',
    // 脱敏显示的身份证号
    maskedIdCard: '',
    // 表单字段
    realName: '',
    idCard: '',
    // 当前昵称（用于校验不能与真实姓名相同）
    nickname: ''
  },

  onLoad() {
    const userInfo = getUserInfo() || {}
    const verified = userInfo.idCardVerified === 1
    this.setData({
      verified,
      nickname: userInfo.nickname || userInfo.wechatNickname || '',
      maskedRealName: verified ? this.maskRealName(userInfo.realName) : '',
      maskedIdCard: verified ? this.maskIdCard(userInfo.idCard) : ''
    })
  },

  // 真实姓名脱敏：张*三
  maskRealName(name) {
    if (!name) return ''
    const str = String(name)
    if (str.length <= 1) return str
    if (str.length === 2) return str[0] + '*'
    return str[0] + '*'.repeat(str.length - 2) + str[str.length - 1]
  },

  // 身份证号脱敏：110***********1234
  maskIdCard(idCard) {
    if (!idCard) return ''
    const str = String(idCard)
    if (str.length < 8) return str
    return str.slice(0, 3) + '*'.repeat(str.length - 7) + str.slice(-4)
  },

  onRealNameInput(e) {
    this.setData({ realName: e.detail.value })
  },

  onIdCardInput(e) {
    this.setData({ idCard: e.detail.value })
  },

  // 校验真实姓名（2-10 位中文）
  validateRealName(name) {
    return /^[\u4e00-\u9fa5]{2,10}$/.test(name)
  },

  // 校验身份证号（18 位）
  validateIdCard(idCard) {
    return /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/.test(idCard)
  },

  // 提交认证
  handleSubmit() {
    const { realName, idCard, nickname } = this.data
    const trimmedName = (realName || '').trim()
    const trimmedId = (idCard || '').trim()

    if (!trimmedName) {
      showToast('请输入真实姓名')
      return
    }
    if (!this.validateRealName(trimmedName)) {
      showToast('姓名需为2-10位中文')
      return
    }
    if (!trimmedId) {
      showToast('请输入身份证号')
      return
    }
    if (!this.validateIdCard(trimmedId)) {
      showToast('身份证号格式不正确')
      return
    }
    // 校验昵称不能与真实姓名相同
    if (nickname && nickname === trimmedName) {
      showModal('提示', '昵称不能与真实姓名相同，请修改昵称后再进行实名认证')
      return
    }

    const userInfo = getUserInfo() || {}
    const newUserInfo = {
      ...userInfo,
      realName: trimmedName,
      idCard: trimmedId,
      idCardVerified: 1
    }
    // TODO: 生产环境应调用后端实名认证接口（如 userApi.realNameVerify），将身份证号加密传输并由后端审核。
    // 本次任务无专用接口，暂时保存到本地存储 userInfo 并标记 idCardVerified=1。
    setUserInfo(newUserInfo)
    app.globalData.userInfo = newUserInfo

    showToast('认证成功', 'success')
    setTimeout(() => {
      wx.navigateBack()
    }, 800)
  }
})
