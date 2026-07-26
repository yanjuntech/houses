// 消息中心 - 消息列表页
const app = getApp()
const { messageApi } = require('../../utils/api.js')
const utils = require('../../utils/index.js')

Page({
  data: {
    // 消息列表
    messageList: [],
    // 分页参数
    pageNum: 1,
    pageSize: 10,
    // 总条数
    total: 0,
    // 是否还有更多
    hasMore: true,
    // 是否正在加载
    loading: false,
    // 当前选中的标签：all/unread/read
    activeTab: 'all',
    // 未读消息数量
    unreadCount: 0,
    // 当前登录用户ID
    userId: null
  },

  onLoad() {
    // 检查登录状态
    if (!app.checkLogin()) {
      return
    }
    const userInfo = app.getUserInfo()
    if (!userInfo || !userInfo.userId) {
      utils.showToast('请先登录')
      setTimeout(() => {
        wx.reLaunch({ url: '/pages/login/login' })
      }, 1500)
      return
    }
    this.setData({ userId: userInfo.userId })
    // 加载消息列表和未读数量
    this.loadMessages()
    this.loadUnreadCount()
  },

  onShow() {
    // 从详情页返回时刷新未读数量与列表
    if (this.data.userId) {
      this.loadUnreadCount()
    }
  },

  /**
   * 加载消息列表
   * @param {boolean} isLoadMore 是否为加载更多
   */
  loadMessages(isLoadMore = false) {
    if (this.data.loading) return
    if (isLoadMore && !this.data.hasMore) return

    this.setData({ loading: true })

    const { userId, pageNum, pageSize } = this.data
    messageApi
      .list(userId, pageNum, pageSize)
      .then((res) => {
        // 兼容后端返回 { rows, total } 或 { list, total } 或数组
        const list = utils.normalizeList(res)
        const total = (res && res.total) || list.length

        // 格式化时间并按标签过滤
        const formatted = list.map((item) => {
          return {
            ...item,
            formatTime: utils.formatDateTime(item.createTime || item.sendTime || item.createdAt)
          }
        })

        // 根据 activeTab 过滤已读/未读
        const filtered = this.filterByTab(formatted)

        // 加载更多时追加，否则替换
        const newList = isLoadMore ? this.data.messageList.concat(filtered) : filtered
        const currentCount = isLoadMore ? this.data.messageList.length + list.length : list.length

        this.setData({
          messageList: newList,
          total,
          hasMore: currentCount < total,
          loading: false
        })
      })
      .catch((err) => {
        console.error('加载消息列表失败:', err)
        this.setData({ loading: false })
        if (isLoadMore) {
          this.setData({ pageNum: this.data.pageNum - 1 })
        }
      })
  },

  /**
   * 根据 activeTab 过滤消息
   * @param {Array} list 消息列表
   * @returns {Array} 过滤后的列表
   */
  filterByTab(list) {
    const { activeTab } = this.data
    // readStatus: 0-未读 1-已读
    if (activeTab === 'unread') {
      return list.filter((item) => item.readStatus === 0 || item.readStatus === '0' || !item.isRead)
    }
    if (activeTab === 'read') {
      return list.filter((item) => item.readStatus === 1 || item.readStatus === '1' || item.isRead)
    }
    return list
  },

  /**
   * 加载未读消息数量
   */
  loadUnreadCount() {
    const { userId } = this.data
    if (!userId) return
    messageApi
      .unreadCount(userId)
      .then((res) => {
        const count = typeof res === 'number' ? res : (res && res.count) || (res && res.data) || 0
        this.setData({ unreadCount: count })
      })
      .catch((err) => {
        console.error('获取未读数量失败:', err)
      })
  },

  /**
   * 切换标签：全部/未读/已读
   */
  switchTab(e) {
    const tab = e.currentTarget.dataset.tab
    if (tab === this.data.activeTab) return
    this.setData({
      activeTab: tab,
      messageList: [],
      pageNum: 1,
      hasMore: true,
      loading: false
    })
    this.loadMessages()
  },

  /**
   * 跳转到消息详情页
   */
  goDetail(e) {
    const messageId = e.currentTarget.dataset.id
    if (!messageId) return
    wx.navigateTo({
      url: `/pages/message/detail?messageId=${messageId}`
    })
  },

  /**
   * 下拉刷新：重置列表重新加载
   */
  onPullDownRefresh() {
    this.setData({
      messageList: [],
      pageNum: 1,
      hasMore: true,
      loading: false
    })
    this.loadMessages()
    this.loadUnreadCount()
    // 停止下拉刷新动画
    setTimeout(() => {
      wx.stopPullDownRefresh()
    }, 800)
  },

  /**
   * 上拉加载更多
   */
  onReachBottom() {
    if (!this.data.hasMore || this.data.loading) return
    this.setData({ pageNum: this.data.pageNum + 1 })
    this.loadMessages(true)
  }
})
