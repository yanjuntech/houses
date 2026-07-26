// 个人中心页
const app = getApp()
const { messageApi, favoriteApi, browseApi, mallApi } = require('../../utils/api.js')
const { showToast, showModal, getUserInfo, maskPhone } = require('../../utils/index.js')

Page({
  data: {
    // 默认头像
    defaultAvatar: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=user%20avatar%20icon%20simple%20minimal%20design&image_size=square',
    // 是否已登录
    isLoggedIn: false,
    // 用户信息
    userInfo: {},
    // 脱敏手机号
    maskedPhone: '',
    // 数据统计
    userStats: {
      favorites: 0,
      browses: 0,
      inviteCount: 0
    },
    // 未读消息数
    unreadCount: 0,
    // 功能菜单
    menuList: [
      { id: 1, icon: '🏠', text: '我的房屋', badge: '' },
      { id: 2, icon: '❤️', text: '我的收藏', badge: '' },
      { id: 3, icon: '👁️', text: '浏览记录', badge: '' },
      { id: 4, icon: '📝', text: '报修记录', badge: '' },
      { id: 5, icon: '💬', text: '消息通知', badge: '' },
      { id: 6, icon: '🎁', text: '邀请管理', badge: '' },
      { id: 7, icon: '🛒', text: '兑换商城', badge: '' },
      { id: 8, icon: '📢', text: '发布房源', badge: '' }
    ],
    // 设置项（不含退出登录，退出登录单独渲染）
    settingsList: [
      { id: 101, icon: '🔐', text: '实名认证', badge: '' },
      { id: 102, icon: '📞', text: '联系客服', badge: '' },
      { id: 103, icon: 'ℹ️', text: '关于我们', badge: '' }
    ],
    appVersion: '1.0.0',
    // 客服电话
    servicePhone: '400-888-8888'
  },

  onShow() {
    // 每次显示都重新检查登录状态和加载统计数据
    this.checkLoginStatus()
    if (this.data.isLoggedIn) {
      this.loadUserInfo()
      this.loadStats()
      this.loadUnreadCount()
    }
  },

  // 检查登录状态
  checkLoginStatus() {
    const userInfo = getUserInfo()
    const token = wx.getStorageSync('token')
    const isLoggedIn = !!(token && userInfo && userInfo.userId)
    this.setData({ isLoggedIn })
    if (!isLoggedIn) {
      this.setData({
        userInfo: {},
        maskedPhone: '',
        unreadCount: 0,
        userStats: { favorites: 0, browses: 0, inviteCount: 0 }
      })
    }
  },

  // 加载用户信息（从本地存储）
  loadUserInfo() {
    const userInfo = getUserInfo() || {}
    this.setData({
      userInfo,
      maskedPhone: maskPhone(userInfo.phone)
    })
    // 更新实名认证状态标签
    const settingsList = this.data.settingsList.map(item => {
      if (item.id === 101) {
        return { ...item, badge: userInfo.idCardVerified === 1 ? '已认证' : '' }
      }
      return item
    })
    this.setData({ settingsList })
  },

  // 加载统计数据：收藏数、浏览数、邀请人数
  loadStats() {
    const userId = this.data.userInfo.userId
    if (!userId) return

    // 收藏数
    favoriteApi.userFavorite(userId).then(res => {
      let count = 0
      if (Array.isArray(res)) count = res.length
      else if (res) count = (res.rows || res.list || res.records || []).length
      this.setData({ 'userStats.favorites': count })
    }).catch(err => console.error('获取收藏数失败:', err))

    // 浏览记录数
    browseApi.userBrowse(userId).then(res => {
      let count = 0
      if (Array.isArray(res)) count = res.length
      else if (res) count = (res.rows || res.list || res.records || []).length
      this.setData({ 'userStats.browses': count })
    }).catch(err => console.error('获取浏览数失败:', err))

    // 邀请人数（从配额接口获取）
    mallApi.userQuota(userId).then(res => {
      const inviteCount = (res && (res.inviteCount || res.invitedCount)) || 0
      this.setData({ 'userStats.inviteCount': inviteCount })
    }).catch(err => console.error('获取邀请人数失败:', err))
  },

  // 加载未读消息数
  loadUnreadCount() {
    const userId = this.data.userInfo.userId
    if (!userId) return
    messageApi.unreadCount(userId).then(res => {
      const count = typeof res === 'number' ? res : ((res && (res.count || res.data)) || 0)
      this.setData({ unreadCount: count })
      // 同步更新消息通知菜单徽标
      const menuList = this.data.menuList.map(item => {
        if (item.id === 5) {
          return { ...item, badge: count > 0 ? String(count) : '' }
        }
        return item
      })
      this.setData({ menuList })
    }).catch(err => console.error('获取未读消息数失败:', err))
  },

  // 跳转登录页
  handleLogin() {
    wx.navigateTo({ url: '/pages/login/login' })
  },

  // 跳转编辑资料页
  handleEditProfile() {
    if (!this.data.isLoggedIn) {
      showToast('请先登录')
      wx.navigateTo({ url: '/pages/login/login' })
      return
    }
    wx.navigateTo({ url: '/pages/profile/edit' })
  },

  // 点击功能菜单
  handleMenu(e) {
    const id = e.currentTarget.dataset.id
    if (!this.data.isLoggedIn) {
      showToast('请先登录')
      wx.navigateTo({ url: '/pages/login/login' })
      return
    }
    // 发布房源：未实名认证时提示
    if (id === 8) {
      const userInfo = this.data.userInfo
      if (userInfo.idCardVerified !== 1) {
        showToast('请先完成实名认证')
        return
      }
    }
    // tab 页必须使用 switchTab 跳转
    const tabRoutes = {
      1: '/pages/house/list'
    }
    if (tabRoutes[id]) {
      wx.switchTab({ url: tabRoutes[id] })
      return
    }
    const menuRoutes = {
      2: '/pages/favorite/list',
      3: '/pages/favorite/list',
      4: '/pages/repair/list',
      5: '/pages/message/list',
      6: '/pages/invite/index',
      7: '/pages/mall/index',
      8: '/pages/house/publish'
    }
    const url = menuRoutes[id]
    if (url) {
      wx.navigateTo({ url })
    }
  },

  // 点击设置项
  handleSetting(e) {
    const id = e.currentTarget.dataset.id
    if (id === 101) {
      // 实名认证
      if (!this.data.isLoggedIn) {
        showToast('请先登录')
        wx.navigateTo({ url: '/pages/login/login' })
        return
      }
      wx.navigateTo({ url: '/pages/profile/verify' })
    } else if (id === 102) {
      // 联系客服
      showModal('联系客服', '客服电话：' + this.data.servicePhone)
    } else if (id === 103) {
      // 关于我们
      showModal('关于我们', '租房小助手 v' + this.data.appVersion + '\n致力于为您提供便捷的租房服务')
    }
  },

  // 退出登录
  handleLogout() {
    showModal('提示', '确定退出登录吗？').then(confirm => {
      if (confirm) {
        app.logout()
      }
    })
  }
})
