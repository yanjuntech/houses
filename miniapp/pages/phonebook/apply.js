// 电话簿申请收录页
const app = getApp()
const { phonebookApi } = require('../../utils/api.js')
const { showToast, showLoading, hideLoading } = require('../../utils/index.js')
const { isValidPhone } = require('../../utils/validate.js')

Page({
  data: {
    // 表单字段
    form: {
      merchantName: '',
      phone1: '',
      phone2: '',
      category: '',
      address: '',
      remark: ''
    },
    // 分类选择索引
    categoryIndex: 0,
    // 分类列表（不含"全部"）
    categories: [
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
    submitting: false
  },

  onLoad() {
    // 初始化分类列表已在 data 中定义
  },

  // 商家名称输入
  onMerchantNameInput(e) {
    this.setData({ 'form.merchantName': e.detail.value })
  },

  // 联系电话1输入
  onPhone1Input(e) {
    this.setData({ 'form.phone1': e.detail.value })
  },

  // 联系电话2输入
  onPhone2Input(e) {
    this.setData({ 'form.phone2': e.detail.value })
  },

  // 商家地址输入
  onAddressInput(e) {
    this.setData({ 'form.address': e.detail.value })
  },

  // 备注输入
  onRemarkInput(e) {
    this.setData({ 'form.remark': e.detail.value })
  },

  // 选择分类
  onCategoryChange(e) {
    const index = Number(e.detail.value)
    const categories = this.data.categories
    const selected = categories[index]
    this.setData({
      categoryIndex: index,
      'form.category': selected ? selected.value : ''
    })
  },

  // 表单校验
  validate() {
    const { merchantName, phone1, phone2, category, address } = this.data.form
    if (!merchantName || !merchantName.trim()) {
      showToast('请填写商家名称')
      return false
    }
    if (!phone1 || !phone1.trim()) {
      showToast('请填写联系电话1')
      return false
    }
    if (!isValidPhone(phone1)) {
      showToast('请输入正确的11位手机号')
      return false
    }
    // 电话2选填，但若填写需校验格式
    if (phone2 && phone2.trim() && !isValidPhone(phone2)) {
      showToast('联系电话2 格式不正确')
      return false
    }
    if (!category) {
      showToast('请选择商家分类')
      return false
    }
    if (!address || !address.trim()) {
      showToast('请填写商家地址')
      return false
    }
    return true
  },

  // 提交申请
  handleSubmit() {
    if (this.data.submitting) return
    if (!this.validate()) return

    const userInfo = app.getUserInfo() || {}
    const { merchantName, phone1, phone2, category, address, remark } = this.data.form
    const submitData = {
      merchantName: merchantName.trim(),
      phone1: phone1.trim(),
      phone2: phone2.trim(),
      category,
      address: address.trim(),
      remark: remark ? remark.trim() : '',
      userId: userInfo.id || userInfo.userId || '',
      applicantName: userInfo.nickName || userInfo.nickname || ''
    }

    this.setData({ submitting: true })
    showLoading('提交中')
    phonebookApi.apply(submitData).then(() => {
      hideLoading()
      showToast('提交成功，待审核', 'success')
      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    }).catch((err) => {
      console.error('申请收录失败:', err)
      hideLoading()
      this.setData({ submitting: false })
    })
  }
})
