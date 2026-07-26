const app = getApp()
const { mallApi } = require('../../utils/api.js')
const { showToast, showLoading, hideLoading } = require('../../utils/index.js')

// 兑换状态映射：0 已兑换（绿色）、1 已生效（蓝色）、2 已失效（灰色）
const STATUS_MAP = {
  0: { text: '已兑换', cls: 'status-exchanged' },
  1: { text: '已生效', cls: 'status-effective' },
  2: { text: '已失效', cls: 'status-invalid' }
}

Page({
  data: {
    recordList: [],
    loading: false,
    userId: ''
  },

  onLoad() {
    const userInfo = app.getUserInfo()
    if (userInfo && userInfo.userId) {
      this.setData({ userId: userInfo.userId })
    } else {
      showToast('请先登录')
      return
    }
    this.loadRecords()
  },

  // 下拉刷新
  onPullDownRefresh() {
    this.loadRecords(() => {
      wx.stopPullDownRefresh()
    })
  },

  // 加载兑换记录
  loadRecords(done) {
    if (!this.data.userId) {
      done && done()
      return
    }
    this.setData({ loading: true })
    if (!done) showLoading('加载中')

    mallApi.userRecord(this.data.userId).then(res => {
      let list = []
      if (Array.isArray(res)) {
        list = res
      } else if (res && Array.isArray(res.rows)) {
        list = res.rows
      } else if (res && Array.isArray(res.list)) {
        list = res.list
      }

      const recordList = list.map(item => {
        const statusKey = Number(item.status)
        const statusInfo = STATUS_MAP[statusKey] || { text: '未知', cls: 'status-invalid' }
        return {
          ...item,
          exchangeTimeText: this.formatTime(item.exchangeTime),
          statusText: statusInfo.text,
          statusCls: statusInfo.cls
        }
      })

      this.setData({ recordList, loading: false })
      hideLoading()
    }).catch(() => {
      this.setData({ loading: false, recordList: [] })
      hideLoading()
      showToast('记录加载失败')
    }).finally(() => {
      done && done()
    })
  },

  // 格式化时间
  formatTime(time) {
    if (!time) return ''
    let d
    if (typeof time === 'number') {
      d = new Date(time)
    } else if (typeof time === 'string') {
      // 兼容时间戳字符串与日期字符串
      d = isNaN(Number(time)) ? new Date(time) : new Date(Number(time))
    } else {
      d = new Date(time)
    }
    if (isNaN(d.getTime())) return String(time)
    const year = d.getFullYear()
    const month = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    const hours = String(d.getHours()).padStart(2, '0')
    const minutes = String(d.getMinutes()).padStart(2, '0')
    const seconds = String(d.getSeconds()).padStart(2, '0')
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
  }
})
