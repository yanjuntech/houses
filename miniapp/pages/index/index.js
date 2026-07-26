const app = getApp()

Page({
  data: {
    greeting: '上午好',
    quickActions: [
      { id: 1, icon: '🏠', name: '房屋租赁' },
      { id: 2, icon: '🔧', name: '维修服务' },
      { id: 3, icon: '📋', name: '合同管理' },
      { id: 4, icon: '💬', name: '社区互动' },
      { id: 5, icon: '📞', name: '联系客服' },
      { id: 6, icon: '⚙️', name: '设置' }
    ],
    services: [
      {
        id: 1,
        name: '在线报修',
        desc: '一键提交维修申请',
        image: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=home%20repair%20service%20illustration%20clean%20modern%20style&image_size=landscape_16_9',
        tags: ['快捷', '高效']
      },
      {
        id: 2,
        name: '房屋租售',
        desc: '优质房源推荐',
        image: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=real%20estate%20property%20listing%20modern%20clean%20design&image_size=landscape_16_9',
        tags: ['优质', '推荐']
      },
      {
        id: 3,
        name: '费用缴纳',
        desc: '水电费在线缴纳',
        image: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=online%20payment%20bill%20payment%20clean%20interface&image_size=landscape_16_9',
        tags: ['便捷', '安全']
      },
      {
        id: 4,
        name: '社区公告',
        desc: '最新通知消息',
        image: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=community%20notice%20announcement%20modern%20design&image_size=landscape_16_9',
        tags: ['最新', '官方']
      }
    ],
    news: [
      { id: 1, title: '小区垃圾分类新规定实施通知', time: '2小时前' },
      { id: 2, title: '暑期儿童安全知识讲座报名开始', time: '5小时前' },
      { id: 3, title: '停车场系统升级维护公告', time: '1天前' },
      { id: 4, title: '业主大会会议纪要发布', time: '2天前' }
    ]
  },

  onLoad() {
    this.setGreeting()
    this.loadData()
  },

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

  loadData() {
    console.log('加载首页数据')
  },

  handleSearch() {
    wx.showToast({
      title: '搜索功能开发中',
      icon: 'none'
    })
  },

  handleAction(e) {
    const id = e.currentTarget.dataset.id
    console.log('点击快捷操作:', id)
    wx.showToast({
      title: '功能开发中',
      icon: 'none'
    })
  },

  handleService(e) {
    const id = e.currentTarget.dataset.id
    console.log('点击服务:', id)
    wx.showToast({
      title: '服务开发中',
      icon: 'none'
    })
  },

  viewMore() {
    wx.showToast({
      title: '查看更多',
      icon: 'none'
    })
  },

  handleNews(e) {
    const id = e.currentTarget.dataset.id
    console.log('点击新闻:', id)
    wx.showToast({
      title: '新闻详情开发中',
      icon: 'none'
    })
  }
})