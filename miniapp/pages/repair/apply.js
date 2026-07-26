// 维修申请页
const app = getApp()
const { repairApi } = require('../../utils/api.js')
const { showToast, showLoading, hideLoading } = require('../../utils/index.js')

Page({
  data: {
    form: {
      contactName: '',
      contactPhone: '',
      houseAddress: '',
      description: '',
      appointmentDate: ''
    },
    submitting: false,
    today: ''
  },

  onLoad() {
    // 设置今天日期作为最小可选日期
    const today = this.formatDate(new Date())
    // 默认从用户信息预填联系人和电话
    const userInfo = app.getUserInfo() || {}
    this.setData({
      today,
      'form.contactName': userInfo.nickname || userInfo.nickName || '',
      'form.contactPhone': userInfo.phone || ''
    })
  },

  // 格式化日期为 YYYY-MM-DD
  formatDate(date) {
    const d = new Date(date)
    const year = d.getFullYear()
    const month = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  },

  onContactNameInput(e) {
    this.setData({ 'form.contactName': e.detail.value })
  },

  onContactPhoneInput(e) {
    this.setData({ 'form.contactPhone': e.detail.value })
  },

  onHouseAddressInput(e) {
    this.setData({ 'form.houseAddress': e.detail.value })
  },

  onDescriptionInput(e) {
    this.setData({ 'form.description': e.detail.value })
  },

  onDateChange(e) {
    this.setData({ 'form.appointmentDate': e.detail.value })
  },

  // 表单校验
  validate() {
    const { contactName, contactPhone, houseAddress, description, appointmentDate } = this.data.form
    if (!contactName || !contactName.trim()) {
      showToast('请输入联系人姓名')
      return false
    }
    if (!contactPhone || !contactPhone.trim()) {
      showToast('请输入联系电话')
      return false
    }
    if (!/^1[3-9]\d{9}$/.test(contactPhone.trim())) {
      showToast('请输入正确的手机号')
      return false
    }
    if (!houseAddress || !houseAddress.trim()) {
      showToast('请输入房屋地址')
      return false
    }
    if (!description || !description.trim()) {
      showToast('请输入问题描述')
      return false
    }
    if (!appointmentDate) {
      showToast('请选择期望上门时间')
      return false
    }
    return true
  },

  // 提交维修申请
  handleSubmit() {
    if (this.data.submitting) return
    if (!this.validate()) return

    const userInfo = app.getUserInfo() || {}
    const { contactName, contactPhone, houseAddress, description, appointmentDate } = this.data.form
    const submitData = {
      userId: userInfo.userId || userInfo.id || '',
      contactName: contactName.trim(),
      contactPhone: contactPhone.trim(),
      houseAddress: houseAddress.trim(),
      description: description.trim(),
      appointmentDate,
      applicantName: userInfo.nickname || userInfo.nickName || ''
    }

    this.setData({ submitting: true })
    showLoading('提交中')
    repairApi.apply(submitData).then(() => {
      hideLoading()
      showToast('提交成功', 'success')
      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    }).catch(() => {
      hideLoading()
      this.setData({ submitting: false })
    })
  }
})
