// 邀请管理页
const app = getApp()
const { inviteApi } = require('../../utils/api.js')
const { showToast, formatDateTime } = require('../../utils/index.js')

// 默认头像（被邀请人未提供头像时使用）
const DEFAULT_AVATAR = 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI9FhqRjMicavia04bibPFpicSibrFY2dXDGZj9zSXQAjBc3q3y0nSIc2JqGgVia0TQpkRuBpDz6Q/0'

Page({
  data: {
    // 当前用户ID（作为邀请人）
    userId: null,
    // 邀请码（即用户ID）
    inviteCode: '',
    // 邀请统计数据
    statistics: {
      totalCount: 0,    // 总邀请人数
      verifiedCount: 0, // 已认证人数
      pendingCount: 0  // 待认证人数
    },
    // 邀请列表
    inviteList: [],
    // 是否正在加载
    loading: false
  },

  onLoad() {
    // 检查登录状态
    if (!app.checkLogin()) {
      return
    }
    const userInfo = app.getUserInfo() || {}
    const userId = userInfo.userId || userInfo.id || ''
    if (!userId) {
      showToast('请先登录')
      setTimeout(() => {
        wx.reLaunch({ url: '/pages/login/login' })
      }, 1500)
      return
    }
    this.setData({
      userId,
      inviteCode: String(userId)
    })
    // 加载统计数据和邀请列表
    this.loadStatistics()
    this.loadInviteList()
  },

  /**
   * 加载邀请统计数据
   */
  loadStatistics() {
    const { userId } = this.data
    if (!userId) return
    inviteApi
      .statistics(userId)
      .then((res) => {
        // 兼容后端返回字段：totalCount/total、verifiedCount/verified、pendingCount/pending
        const data = res || {}
        this.setData({
          statistics: {
            totalCount: Number(data.totalCount ?? data.total ?? 0),
            verifiedCount: Number(data.verifiedCount ?? data.verified ?? 0),
            pendingCount: Number(data.pendingCount ?? data.pending ?? 0)
          }
        })
      })
      .catch((err) => {
        console.error('获取邀请统计失败:', err)
      })
  },

  /**
   * 加载邀请列表
   */
  loadInviteList() {
    const { userId } = this.data
    if (!userId) return
    this.setData({ loading: true })
    inviteApi
      .inviteList(userId)
      .then((res) => {
        // 兼容后端返回数组或 { list, rows, records }
        let list = []
        if (Array.isArray(res)) {
          list = res
        } else if (res) {
          list = res.list || res.rows || res.records || []
        }
        // 格式化每一条邀请记录
        const formatted = list.map((item) => {
          return {
            ...item,
            avatar: this.normalizeAvatar(item.avatar || item.avatarUrl),
            nickname: this.normalizeNickname(item.nickname || item.nickName),
            phoneMasked: this.maskPhone(item.phone || item.mobile || ''),
            verifyStatusText: this.isVerified(item) ? '已认证' : '未认证',
            verified: this.isVerified(item),
            formatTime: formatDateTime(item.inviteTime || item.createTime || item.createdAt)
          }
        })
        this.setData({ inviteList: formatted, loading: false })
      })
      .catch((err) => {
        console.error('获取邀请列表失败:', err)
        this.setData({ loading: false })
      })
  },

  /**
   * 判断被邀请人是否已认证
   * 兼容多种字段：verifyStatus / verified / authStatus
   */
  isVerified(item) {
    const status = item.verifyStatus ?? item.verified ?? item.authStatus ?? item.certified
    if (status === true || status === 1 || status === '1' || status === 'Y') return true
    return false
  },

  /**
   * 头像兜底
   */
  normalizeAvatar(url) {
    if (!url) return DEFAULT_AVATAR
    return url
  },

  /**
   * 昵称兜底
   */
  normalizeNickname(name) {
    if (!name) return '微信用户'
    return name
  },

  /**
   * 手机号脱敏：138****8888
   */
  maskPhone(phone) {
    if (!phone) return ''
    const str = String(phone).trim()
    // 仅处理长度大于等于 7 的手机号
    if (str.length < 7) return str
    return str.substring(0, 3) + '****' + str.substring(str.length - 4)
  },

  /**
   * 复制邀请链接（小程序路径）到剪贴板
   */
  handleCopyInviteLink() {
    const { userId } = this.data
    if (!userId) {
      showToast('邀请码获取失败', 'none')
      return
    }
    const path = `/pages/index/index?inviterId=${userId}`
    wx.setClipboardData({
      data: path,
      success: () => {
        showToast('邀请链接已复制', 'success')
      },
      fail: () => {
        showToast('复制失败，请重试', 'none')
      }
    })
  },

  /**
   * 复制邀请码到剪贴板
   */
  handleCopyInviteCode() {
    const { inviteCode } = this.data
    if (!inviteCode) {
      showToast('邀请码获取失败', 'none')
      return
    }
    wx.setClipboardData({
      data: inviteCode,
      success: () => {
        showToast('邀请码已复制', 'success')
      },
      fail: () => {
        showToast('复制失败，请重试', 'none')
      }
    })
  },

  /**
   * 下拉刷新：重新加载统计数据和邀请列表
   */
  onPullDownRefresh() {
    this.loadStatistics()
    this.loadInviteList()
    setTimeout(() => {
      wx.stopPullDownRefresh()
    }, 800)
  },

  /**
   * 分享给微信好友
   */
  onShareAppMessage() {
    const userInfo = app.getUserInfo() || {}
    return {
      title: `${userInfo.nickname || '你的好友'} 邀请你加入`,
      path: `/pages/index/index?inviterId=${userInfo.userId || this.data.userId}`,
      imageUrl: ''
    }
  }
})
