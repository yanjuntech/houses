// 电话簿页面
const api = require('../../utils/api.js')

Page({
  data: {
    // 全部商家数据
    allMerchants: [],
    // 筛选后的商家
    filteredMerchants: [],
    // 当前分类，空字符串表示全部
    currentCategory: '',
    // 搜索关键词
    keyword: '',
    // 分类列表（含"全部"）
    categories: [
      { value: '', label: '全部', count: 0 },
      { value: '1', label: '餐饮美食', count: 0 },
      { value: '2', label: '快递服务', count: 0 },
      { value: '3', label: '超市便利', count: 0 },
      { value: '4', label: '便民服务', count: 0 },
      { value: '5', label: '维修服务', count: 0 },
      { value: '6', label: '医疗健康', count: 0 },
      { value: '7', label: '教育培训', count: 0 },
      { value: '8', label: '建材五金', count: 0 },
      { value: '9', label: '装修服务', count: 0 },
      { value: '10', label: '宠物服务', count: 0 },
      { value: '11', label: '母婴用品', count: 0 },
      { value: '12', label: '法律服务', count: 0 },
      { value: '13', label: '房产服务', count: 0 }
    ]
  },

  onLoad() {
    this.loadMerchants()
  },

  /**
   * 加载全部商家数据
   */
  loadMerchants() {
    wx.showLoading({ title: '加载中', mask: true })
    api.phonebookApi.selectAll()
      .then((res) => {
        const list = Array.isArray(res) ? res : (res && res.list) || []
        this.setData({
          allMerchants: list,
          filteredMerchants: list
        }, () => {
          this.updateCategoryCounts()
        })
      })
      .catch((err) => {
        console.error('加载商家列表失败:', err)
      })
      .finally(() => {
        wx.hideLoading()
      })
  },

  /**
   * 计算每个分类下的商家数量
   */
  updateCategoryCounts() {
    const { allMerchants, categories } = this.data
    const countMap = {}
    allMerchants.forEach((item) => {
      const cat = String(item.category || '')
      if (cat) {
        countMap[cat] = (countMap[cat] || 0) + 1
      }
    })
    const newCategories = categories.map((c) => {
      if (c.value === '') {
        return { ...c, count: allMerchants.length }
      }
      return { ...c, count: countMap[c.value] || 0 }
    })
    this.setData({ categories: newCategories })
  },

  /**
   * 选择分类
   */
  filterByCategory(e) {
    const value = e.currentTarget.dataset.value || ''
    this.setData({ currentCategory: value }, () => {
      this.doFilter()
    })
  },

  /**
   * 搜索输入：清空当前分类并高亮"全部"
   */
  handleSearch(e) {
    const keyword = e.detail.value || ''
    this.setData({ keyword, currentCategory: '' }, () => {
      this.doFilter()
    })
  },

  /**
   * 清空搜索
   */
  clearKeyword() {
    this.setData({ keyword: '' }, () => {
      this.doFilter()
    })
  },

  /**
   * 执行筛选（分类 + 关键词）
   */
  doFilter() {
    const { allMerchants, currentCategory, keyword } = this.data
    let list = allMerchants
    // 分类筛选
    if (currentCategory) {
      list = list.filter((item) => String(item.category) === String(currentCategory))
    }
    // 关键词模糊匹配商家名称
    if (keyword) {
      const kw = keyword.toLowerCase()
      list = list.filter((item) => {
        const name = (item.merchantName || item.merchant_name || item.name || '').toString().toLowerCase()
        return name.indexOf(kw) > -1
      })
    }
    this.setData({ filteredMerchants: list })
  },

  /**
   * 拨打电话
   */
  callPhone(e) {
    const phone = e.currentTarget.dataset.phone
    if (!phone) {
      wx.showToast({ title: '暂无电话', icon: 'none' })
      return
    }
    wx.makePhoneCall({
      phoneNumber: String(phone),
      fail: (err) => {
        console.error('拨打电话失败:', err)
      }
    })
  },

  /**
   * 申请收录：跳转到独立页
   */
  handleApply() {
    wx.navigateTo({ url: '/pages/phonebook/apply' })
  }
})
