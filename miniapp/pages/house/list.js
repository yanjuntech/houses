const { houseApi, communityApi } = require('../../utils/api.js')
const { showToast, parseCoverImage } = require('../../utils/index.js')

// 户型筛选项
const HOUSE_TYPE_OPTIONS = ['全部', '整租', '合租', '单间']

// 价格区间筛选项（单位：元）
const PRICE_RANGE_OPTIONS = ['不限', '1000以下', '1000-2000', '2000-3000', '3000-5000', '5000以上']
const PRICE_RANGE_VALUES = [
  { min: undefined, max: undefined },
  { min: undefined, max: 1000 },
  { min: 1000, max: 2000 },
  { min: 2000, max: 3000 },
  { min: 3000, max: 5000 },
  { min: 5000, max: undefined }
]

Page({
  data: {
    // 房源列表数据
    houseList: [],
    pageNum: 1,
    pageSize: 10,
    total: 0,
    hasMore: true,
    loading: false,
    // 筛选条件
    filters: {
      communityId: '',
      houseType: '',
      minPrice: undefined,
      maxPrice: undefined,
      keyword: ''
    },
    // 小区选项
    communityOptions: [],
    communityIndex: 0,
    // 户型、价格选项
    houseTypeOptions: HOUSE_TYPE_OPTIONS,
    houseTypeIndex: 0,
    priceRangeOptions: PRICE_RANGE_OPTIONS,
    priceRangeIndex: 0,
    // 搜索输入临时值
    searchValue: ''
  },

  onLoad() {
    // 加载小区选项
    this.loadCommunityOptions()
    // 加载房源列表
    this.loadHouses(true)
  },

  // 下拉刷新
  onPullDownRefresh() {
    this.setData({ pageNum: 1, hasMore: true })
    this.loadHouses(true, () => {
      wx.stopPullDownRefresh()
    })
  },

  // 上拉加载更多
  onReachBottom() {
    if (this.data.hasMore && !this.data.loading) {
      this.setData({ pageNum: this.data.pageNum + 1 })
      this.loadHouses(false)
    }
  },

  // 加载小区选项列表
  loadCommunityOptions() {
    communityApi.selectAll().then(res => {
      const list = Array.isArray(res) ? res : (res && res.rows) || []
      // 在首部增加"全部小区"选项
      const options = [{ communityId: '', communityName: '全部小区' }, ...list]
      this.setData({ communityOptions: options })
    }).catch(() => {
      // 加载失败时仅保留"全部小区"
      this.setData({ communityOptions: [{ communityId: '', communityName: '全部小区' }] })
    })
  },

  /**
   * 加载房源列表
   * @param {boolean} reset 是否重置列表（首页/刷新）
   * @param {Function} done 加载完成回调
   */
  loadHouses(reset = false, done) {
    if (this.data.loading) {
      done && done()
      return
    }
    this.setData({ loading: true })

    if (reset) {
      this.setData({ pageNum: 1, hasMore: true })
    }

    const { pageNum, pageSize, filters } = this.data
    const params = {
      pageNum,
      pageSize,
      communityId: filters.communityId || undefined,
      houseType: filters.houseType || undefined,
      minPrice: filters.minPrice,
      maxPrice: filters.maxPrice,
      keyword: filters.keyword || undefined
    }

    houseApi.list(params).then(res => {
      // 兼容 {rows, total} 与数组两种返回结构
      let list = []
      let total = 0
      if (Array.isArray(res)) {
        list = res
        total = res.length
      } else if (res && Array.isArray(res.rows)) {
        list = res.rows
        total = res.total || 0
      } else if (res && Array.isArray(res.list)) {
        list = res.list
        total = res.total || 0
      }

      // 为每个房源规范化图片字段
      const normalized = list.map(item => ({
        ...item,
        coverImage: parseCoverImage(item)
      }))

      const newHouseList = reset ? normalized : this.data.houseList.concat(normalized)
      const hasMore = newHouseList.length < total

      this.setData({
        houseList: newHouseList,
        total,
        hasMore,
        loading: false
      })
    }).catch(() => {
      this.setData({ loading: false })
      showToast('房源加载失败')
    }).finally(() => {
      done && done()
    })
  },

  // 搜索输入
  onSearchInput(e) {
    this.setData({ searchValue: e.detail.value })
  },

  // 点击搜索按钮
  handleSearch() {
    this.setData({ 'filters.keyword': this.data.searchValue.trim() })
    this.loadHouses(true)
  },

  // 小区筛选变化
  onCommunityChange(e) {
    const index = Number(e.detail.value)
    const community = this.data.communityOptions[index] || {}
    this.setData({
      communityIndex: index,
      'filters.communityId': community.communityId || ''
    })
    this.loadHouses(true)
  },

  // 户型筛选变化
  onHouseTypeChange(e) {
    const index = Number(e.detail.value)
    const type = index === 0 ? '' : HOUSE_TYPE_OPTIONS[index]
    this.setData({
      houseTypeIndex: index,
      'filters.houseType': type
    })
    this.loadHouses(true)
  },

  // 价格区间筛选变化
  onPriceRangeChange(e) {
    const index = Number(e.detail.value)
    const range = PRICE_RANGE_VALUES[index]
    this.setData({
      priceRangeIndex: index,
      'filters.minPrice': range.min,
      'filters.maxPrice': range.max
    })
    this.loadHouses(true)
  },

  // 重置筛选条件
  handleFilter() {
    this.setData({
      'filters.communityId': '',
      'filters.houseType': '',
      'filters.minPrice': undefined,
      'filters.maxPrice': undefined,
      'filters.keyword': '',
      communityIndex: 0,
      houseTypeIndex: 0,
      priceRangeIndex: 0,
      searchValue: ''
    })
    this.loadHouses(true)
  },

  // 跳转房源详情
  goDetail(e) {
    const houseId = e.currentTarget.dataset.id
    if (!houseId) return
    wx.navigateTo({
      url: `/pages/house/detail?houseId=${houseId}`
    })
  },

  // 我要发布 - 校验登录与实名认证后跳转发布页
  handlePublish() {
    const app = getApp()
    const userInfo = app.getUserInfo()

    // 1. 校验登录状态
    if (!userInfo || Object.keys(userInfo).length === 0) {
      showToast('请先登录')
      wx.navigateTo({ url: '/pages/login/login' })
      return
    }

    // 2. 校验实名认证状态（verifyStatus === '1' 表示已认证）
    if (userInfo.verifyStatus !== '1') {
      showToast('请先完成实名认证')
      wx.navigateTo({ url: '/pages/profile/verify' })
      return
    }

    // 3. 通过校验，跳转发布页
    wx.navigateTo({ url: '/pages/house/publish' })
  }
})
