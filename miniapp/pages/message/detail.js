// 消息中心 - 消息详情页
const { messageApi } = require('../../utils/api.js')
const utils = require('../../utils/index.js')

Page({
  data: {
    // 消息ID
    messageId: null,
    // 消息详情
    detail: null,
    // 格式化后的时间
    formatTime: '',
    // 是否加载中
    loading: true
  },

  onLoad(options) {
    const messageId = options.messageId
    if (!messageId) {
      utils.showToast('消息ID不存在')
      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
      return
    }
    this.setData({ messageId })
    this.loadDetail()
  },

  /**
   * 加载消息详情（接口会自动标记已读）
   */
  loadDetail() {
    this.setData({ loading: true })
    messageApi
      .detail(this.data.messageId)
      .then((res) => {
        if (!res) {
          utils.showToast('消息不存在')
          this.setData({ loading: false })
          return
        }
        const formatTime = utils.formatDateTime(
          res.createTime || res.sendTime || res.createdAt
        )
        this.setData({
          detail: res,
          formatTime,
          loading: false
        })
      })
      .catch((err) => {
        console.error('加载消息详情失败:', err)
        this.setData({ loading: false })
      })
  },

  /**
   * 返回上一页，通知列表页刷新未读数量
   */
  onBack() {
    const pages = getCurrentPages()
    if (pages.length >= 2) {
      const prevPage = pages[pages.length - 2]
      // 通知列表页刷新
      if (prevPage && typeof prevPage.loadUnreadCount === 'function') {
        prevPage.loadUnreadCount()
      }
      if (prevPage && typeof prevPage.loadMessages === 'function') {
        // 重置列表并刷新
        prevPage.setData({
          pageNum: 1,
          hasMore: true
        })
        prevPage.loadMessages()
      }
    }
    wx.navigateBack()
  },

  /**
   * 点击附件或链接
   */
  handleAttachment(e) {
    const url = e.currentTarget.dataset.url
    if (!url) {
      utils.showToast('链接无效')
      return
    }
    // 仅支持小程序内部跳转
    if (url.startsWith('/')) {
      wx.navigateTo({ url })
    } else {
      utils.showToast('暂不支持打开外部链接')
    }
  }
})
