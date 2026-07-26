const app = getApp()

Page({
  data: {
    userInfo: {
      avatar: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=user%20avatar%20icon%20simple%20minimal%20design&image_size=square',
      nickname: '用户昵称',
      phone: '138****8888'
    },
    userStats: {
      orders: 5,
      coupons: 2,
      points: 1280
    },
    menuList: [
      { id: 1, icon: '📋', text: '我的订单', badge: '2' },
      { id: 2, icon: '🎫', text: '优惠券', badge: '' },
      { id: 3, icon: '🏠', text: '我的房屋', badge: '' },
      { id: 4, icon: '❤️', text: '我的收藏', badge: '5' },
      { id: 5, icon: '📝', text: '报修记录', badge: '' },
      { id: 6, icon: '💬', text: '消息通知', badge: '3' }
    ],
    settingsList: [
      { id: 101, icon: '🔔', text: '通知设置' },
      { id: 102, icon: '🔒', text: '账号安全' },
      { id: 103, icon: '❓', text: '帮助中心' },
      { id: 104, icon: '📞', text: '联系客服' },
      { id: 105, icon: 'ℹ️', text: '关于我们' }
    ],
    appVersion: '1.0.0'
  },

  onLoad() {
    this.loadUserInfo()
  },

  loadUserInfo() {
    this.setData({
      appVersion: app.globalData.appVersion
    })
    console.log('加载用户信息')
  },

  handleEditProfile() {
    wx.showToast({
      title: '编辑资料开发中',
      icon: 'none'
    })
  },

  handleMenu(e) {
    const id = e.currentTarget.dataset.id
    console.log('点击菜单:', id)
    const menuMap = {
      1: '我的订单',
      2: '优惠券',
      3: '我的房屋',
      4: '我的收藏',
      5: '报修记录',
      6: '消息通知'
    }
    wx.showToast({
      title: `${menuMap[id]}开发中`,
      icon: 'none'
    })
  },

  handleSetting(e) {
    const id = e.currentTarget.dataset.id
    console.log('点击设置:', id)
    const settingMap = {
      101: '通知设置',
      102: '账号安全',
      103: '帮助中心',
      104: '联系客服',
      105: '关于我们'
    }
    wx.showToast({
      title: `${settingMap[id]}开发中`,
      icon: 'none'
    })
  }
})