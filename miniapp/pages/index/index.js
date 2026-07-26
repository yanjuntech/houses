const api = require('../../utils/api.js')
const app = getApp()

Page({
  data: {
    greeting: '上午好',
    isLoggedIn: false,      // 是否登录
    banners: [],            // 轮播图列表
    houses: [],             // 热门房源列表
    merchants: [],          // 推荐商家列表
    quickActions: [
      { id: 1, icon: '🏠', name: '房屋租赁' },
      { id: 2, icon: '🔧', name: '维修服务' },
      { id: 3, icon: '📞', name: '电话簿' },
      { id: 4, icon: '💬', name: '消息' },
      { id: 5, icon: '🎧', name: '联系客服' },
      { id: 6, icon: '⚙️', name: '设置' }
    ]
  },

  onLoad() {
    // 设置问候语
    this.setGreeting()
    // 检查登录状态
    this.checkLoginStatus()
    // 加载轮播图
    this.loadBanners()
    // 加载热门房源
    this.loadHouses()
    // 加载推荐商家
    this.loadMerchants()
  },

  onShow() {
    // 重新检查登录状态
    this.checkLoginStatus()
  },

  // 根据当前时间设置问候语
  setGreeting() {
    const hour = new Date().getHours()
    let greeting = '上午好'
    if (hour >= 12 && hour < 18) {
      greeting = '下午好'
    } else if (hour >= 18) {
      greeting = '晚上好'
    }
    this.setData({ greeting })
  },

  // 检查登录状态（仅设置标记，不强制跳转）
  checkLoginStatus() {
    try {
      const token = wx.getStorageSync('token')
      const userInfo = app.getUserInfo()
      const isLoggedIn = !!(token && userInfo && !isEmpty(userInfo))
      this.setData({ isLoggedIn })
    } catch (e) {
      console.error('检查登录状态失败:', e)
      this.setData({ isLoggedIn: false })
    }
  },

  // 加载轮播图数据
  loadBanners() {
    api.bannerApi.validList().then(res => {
      const banners = normalizeList(res)
      if (banners && banners.length > 0) {
        this.setData({ banners })
      } else {
        // 后端无数据时使用占位轮播图，保证首页轮播图始终展示
        this.setData({ banners: getDefaultBanners() })
      }
    }).catch(err => {
      console.error('加载轮播图失败:', err)
      // 接口失败时使用占位轮播图，保证首页轮播图始终展示
      this.setData({ banners: getDefaultBanners() })
    })
  },

  // 点击轮播图：根据 jumpUrl 跳转，无跳转链接则忽略
  handleBannerTap(e) {
    const url = e.currentTarget.dataset.url
    if (!url) return
    wx.navigateTo({
      url: url,
      fail: () => {
        // 跳转失败可能是 tabbar 页面
        if (url.startsWith('/pages/index/') || url.startsWith('/pages/house/') || url.startsWith('/pages/phonebook/') || url.startsWith('/pages/profile/')) {
          wx.switchTab({ url: url, fail: () => {} })
        }
      }
    })
  },

  // 加载热门房源（取4条）
  loadHouses() {
    api.houseApi.list({ pageNum: 1, pageSize: 4 }).then(res => {
      const houses = normalizeList(res)
      this.setData({ houses })
    }).catch(err => {
      console.error('加载热门房源失败:', err)
      this.setData({ houses: [] })
    })
  },

  // 加载推荐商家（取前4条）
  loadMerchants() {
    api.phonebookApi.selectAll().then(res => {
      const list = normalizeList(res)
      this.setData({ merchants: list.slice(0, 4) })
    }).catch(err => {
      console.error('加载推荐商家失败:', err)
      this.setData({ merchants: [] })
    })
  },

  // 点击搜索框：跳转房屋列表页
  handleSearch() {
    wx.switchTab({
      url: '/pages/house/list',
      fail: () => {
        wx.navigateTo({
          url: '/pages/house/list',
          fail: () => {
            wx.showToast({ title: '页面跳转失败', icon: 'none' })
          }
        })
      }
    })
  },

  // 点击快捷入口：根据 id 跳转对应页面
  handleAction(e) {
    const id = e.currentTarget.dataset.id
    switch (id) {
      case 1:
        // 房屋租赁 → 房屋列表页（tab 页）
        wx.switchTab({
          url: '/pages/house/list',
          fail: () => {
            wx.showToast({ title: '页面跳转失败', icon: 'none' })
          }
        })
        break
      case 2:
        // 维修服务 → 维修申请页
        wx.navigateTo({
          url: '/pages/repair/apply',
          fail: () => {
            wx.showToast({ title: '页面跳转失败', icon: 'none' })
          }
        })
        break
      case 3:
        // 电话簿 → 电话簿页（tab 页）
        wx.switchTab({
          url: '/pages/phonebook/index',
          fail: () => {
            wx.showToast({ title: '页面跳转失败', icon: 'none' })
          }
        })
        break
      case 4:
        // 消息 → 消息列表页
        wx.navigateTo({
          url: '/pages/message/list',
          fail: () => {
            wx.showToast({ title: '页面跳转失败', icon: 'none' })
          }
        })
        break
      case 5:
        // 联系客服 → 弹窗展示客服信息
        wx.showModal({
          title: '联系客服',
          content: '客服电话：400-888-8888\n服务时间：09:00-18:00',
          showCancel: false,
          confirmText: '我知道了',
          confirmColor: '#4080ff'
        })
        break
      case 6:
        // 设置 → 暂无
        wx.showToast({ title: '暂无', icon: 'none' })
        break
      default:
        break
    }
  },

  // 点击房源：跳转房屋详情页
  handleService(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: '/pages/house/detail?houseId=' + id,
      fail: () => {
        wx.showToast({ title: '页面跳转失败', icon: 'none' })
      }
    })
  },

  // 点击商家：拨打电话
  handleMerchant(e) {
    const phone = e.currentTarget.dataset.phone
    if (!phone) {
      wx.showToast({ title: '电话号码缺失', icon: 'none' })
      return
    }
    wx.makePhoneCall({
      phoneNumber: String(phone),
      fail: () => {
        wx.showToast({ title: '拨号已取消', icon: 'none' })
      }
    })
  },

  // 查看更多房源
  viewMoreHouses() {
    wx.switchTab({
      url: '/pages/house/list',
      fail: () => {
        wx.showToast({ title: '页面跳转失败', icon: 'none' })
      }
    })
  },

  // 查看更多商家
  viewMoreMerchants() {
    wx.switchTab({
      url: '/pages/phonebook/index',
      fail: () => {
        wx.showToast({ title: '页面跳转失败', icon: 'none' })
      }
    })
  }
})

// 兼容后端返回的数组或分页对象 { list, total, ... }，统一取出数组
function normalizeList(res) {
  if (Array.isArray(res)) return res
  if (res && Array.isArray(res.list)) return res.list
  if (res && Array.isArray(res.rows)) return res.rows
  if (res && Array.isArray(res.records)) return res.records
  if (res && Array.isArray(res.data)) return res.data
  return []
}

// 后端无数据或接口失败时的占位轮播图
function getDefaultBanners() {
  return [{
    bannerId: 0,
    image: '',  // 留空，由 wxss 显示默认背景
    title: '欢迎使用租房小助手',
    jumpUrl: ''
  }]
}

// 判空辅助函数
function isEmpty(value) {
  return value === null || value === undefined || value === '' ||
    (Array.isArray(value) && value.length === 0) ||
    (typeof value === 'object' && Object.keys(value).length === 0)
}
