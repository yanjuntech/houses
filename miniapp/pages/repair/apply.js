// 维修申请页
const app = getApp()
const { repairApi, rentalContractApi } = require('../../utils/api.js')
const { showToast, showLoading, hideLoading } = require('../../utils/index.js')

Page({
  data: {
    form: {
      contactName: '',
      contactPhone: '',
      description: '',
      appointmentDate: ''
    },
    submitting: false,
    today: '',
    // 在租房屋列表
    rentals: [],
    hasRentals: true,
    rentalsLoading: true,
    selectedRentalId: '',
    selectedRental: null,
    // 当前租客信息（来自登录用户）
    tenantId: ''
  },

  onLoad() {
    // 设置今天日期作为最小可选日期
    const today = this.formatDate(new Date())
    // 默认从用户信息预填联系人和电话
    const userInfo = app.getUserInfo() || {}
    this.setData({
      today,
      tenantId: userInfo.userId || '',
      'form.contactName': userInfo.nickname || userInfo.nickName || '',
      'form.contactPhone': userInfo.phone || ''
    })

    // 未登录直接提示
    if (!userInfo.userId) {
      this.setData({ rentalsLoading: false, hasRentals: false })
      showToast('请先登录')
      return
    }
    this.loadMyRentals(userInfo.userId)
  },

  // 加载当前用户在租房屋列表
  loadMyRentals(tenantId) {
    this.setData({ rentalsLoading: true })
    rentalContractApi.myRentals(tenantId).then(res => {
      // 兼容后端返回数组或 { list } 结构
      let list = []
      if (Array.isArray(res)) {
        list = res
      } else if (res && Array.isArray(res.list)) {
        list = res.list
      } else if (res && Array.isArray(res.rows)) {
        list = res.rows
      }
      this.setData({
        rentals: list,
        hasRentals: list.length > 0,
        rentalsLoading: false
      })
    }).catch(() => {
      this.setData({
        rentals: [],
        hasRentals: false,
        rentalsLoading: false
      })
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

  onDescriptionInput(e) {
    this.setData({ 'form.description': e.detail.value })
  },

  onDateChange(e) {
    this.setData({ 'form.appointmentDate': e.detail.value })
  },

  // 选择在租房屋
  onSelectRental(e) {
    const rentalId = e.currentTarget.dataset.id
    const rental = this.data.rentals.find(item => String(item.rentalId) === String(rentalId))
    if (!rental) return
    this.setData({
      selectedRentalId: rentalId,
      selectedRental: rental
    })
  },

  // 表单校验
  validate() {
    const { contactName, contactPhone, description, appointmentDate } = this.data.form
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
    if (!this.data.selectedRental) {
      showToast('请选择需要维修的房屋')
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
    if (!this.data.hasRentals) {
      showToast('您当前无在租房屋，无法提交维修申请')
      return
    }
    if (!this.validate()) return

    const userInfo = app.getUserInfo() || {}
    const { contactName, contactPhone, description, appointmentDate } = this.data.form
    const rental = this.data.selectedRental
    const submitData = {
      userId: userInfo.userId || userInfo.id || '',
      houseId: rental.houseId,
      tenantId: this.data.tenantId,
      landlordId: rental.landlordId,
      houseTitle: rental.houseTitle || '',
      contactName: contactName.trim(),
      contactPhone: contactPhone.trim(),
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
