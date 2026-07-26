// 维修记录列表页
const app = getApp()
const { repairApi } = require('../../utils/api.js')
const utils = require('../../utils/index.js')

// 状态映射：text 为状态文字，class 为对应样式类
const STATUS_MAP = {
  0: { text: '待处理', class: 'status-pending' },
  1: { text: '房东已确认', class: 'status-processing' },
  2: { text: '维修中', class: 'status-processing' },
  3: { text: '维修完成', class: 'status-processing' },
  4: { text: '已完成', class: 'status-done' },
  5: { text: '已取消', class: 'status-cancelled' }
}

// 顶部 tab 选项
const TABS = [
  { key: 'all', label: '全部' },
  { key: 'pending', label: '待处理', statuses: [0] },
  { key: 'processing', label: '处理中', statuses: [1, 2, 3] },
  { key: 'done', label: '已完成', statuses: [4] },
  { key: 'cancelled', label: '已取消', statuses: [5] }
]

Page({
  data: {
    tabs: TABS,
    activeTab: 'all',
    repairList: [],
    pageNum: 1,
    pageSize: 10,
    total: 0,
    hasMore: true,
    loading: false,
    userId: null
  },

  onLoad() {
    if (!app.checkLogin()) return
    const userInfo = app.getUserInfo() || {}
    if (!userInfo.userId) {
      utils.showToast('请先登录')
      setTimeout(() => {
        wx.reLaunch({ url: '/pages/login/login' })
      }, 1500)
      return
    }
    this.setData({ userId: userInfo.userId })
    this.loadList(true)
  },

  onShow() {
    // 从详情页返回时刷新列表
    if (this.data.userId && this.data.repairList.length > 0) {
      this.loadList(true)
    }
  },

  onPullDownRefresh() {
    this.loadList(true, () => {
      wx.stopPullDownRefresh()
    })
  },

  onReachBottom() {
    if (!this.data.hasMore || this.data.loading) return
    this.setData({ pageNum: this.data.pageNum + 1 })
    this.loadList(false)
  },

  // 切换 tab
  switchTab(e) {
    const tab = e.currentTarget.dataset.tab
    if (tab === this.data.activeTab) return
    this.setData({
      activeTab: tab,
      repairList: [],
      pageNum: 1,
      hasMore: true,
      loading: false
    })
    this.loadList(true)
  },

  /**
   * 加载维修记录列表
   * @param {boolean} reset 是否重置列表
   * @param {Function} done 完成回调
   */
  loadList(reset = false, done) {
    if (this.data.loading) {
      done && done()
      return
    }
    this.setData({ loading: true })
    if (reset) {
      this.setData({ pageNum: 1, hasMore: true })
    }

    const { userId, pageNum, pageSize } = this.data
    repairApi.list({ userId, pageNum, pageSize }).then(res => {
      // 兼容后端返回 { rows, total } 或 { list, total } 或数组
      let list = []
      let total = 0
      if (Array.isArray(res)) {
        list = res
        total = res.length
      } else if (res) {
        list = res.rows || res.list || res.records || []
        total = res.total || list.length
      }

      // 格式化时间 + 状态映射
      const formatted = list.map(item => {
        const status = Number(item.status === undefined ? item.repairStatus : item.status)
        const statusInfo = STATUS_MAP[status] || { text: '未知', class: '' }
        return {
          ...item,
          statusValue: status,
          statusText: statusInfo.text,
          statusClass: statusInfo.class,
          formatTime: utils.formatDateTime(item.createTime || item.createdAt)
        }
      })

      // 按 tab 过滤
      const filtered = this.filterByTab(formatted)
      const newList = reset ? filtered : this.data.repairList.concat(filtered)
      const hasMore = newList.length < total

      this.setData({
        repairList: newList,
        total,
        hasMore,
        loading: false
      })
    }).catch(() => {
      this.setData({ loading: false })
      utils.showToast('维修记录加载失败')
    }).finally(() => {
      done && done()
    })
  },

  /**
   * 根据 tab 过滤列表
   * @param {Array} list 维修记录列表
   * @returns {Array} 过滤后的列表
   */
  filterByTab(list) {
    const tab = this.data.tabs.find(t => t.key === this.data.activeTab)
    if (!tab || !tab.statuses) return list
    return list.filter(item => tab.statuses.indexOf(item.statusValue) > -1)
  },

  // 跳转维修详情
  goDetail(e) {
    const repairId = e.currentTarget.dataset.id
    if (!repairId) return
    wx.navigateTo({
      url: `/pages/repair/detail?repairId=${repairId}`
    })
  }
})
