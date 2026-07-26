// 编辑资料页
const app = getApp()
const { showToast, getUserInfo, setUserInfo } = require('../../utils/index.js')

Page({
  data: {
    // 默认头像
    defaultAvatar: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=user%20avatar%20icon%20simple%20minimal%20design&image_size=square',
    // 当前头像
    avatar: '',
    // 当前昵称
    nickname: ''
  },

  onLoad() {
    const userInfo = getUserInfo() || {}
    this.setData({
      avatar: userInfo.avatar || userInfo.wechatAvatar || this.data.defaultAvatar,
      nickname: userInfo.nickname || userInfo.wechatNickname || ''
    })
  },

  // 昵称输入
  onNicknameInput(e) {
    this.setData({ nickname: e.detail.value })
  },

  // 选择头像
  chooseAvatar() {
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePath = res.tempFilePaths && res.tempFilePaths[0]
        if (tempFilePath) {
          this.setData({ avatar: tempFilePath })
          // TODO: 实际项目中应调用 wx.uploadFile 上传到服务器，获取返回的 URL 后再保存到 userInfo
        }
      }
    })
  },

  // 保存
  handleSave() {
    const nickname = (this.data.nickname || '').trim()
    if (!nickname) {
      showToast('请输入昵称')
      return
    }
    if (nickname.length > 20) {
      showToast('昵称不能超过20个字')
      return
    }

    const userInfo = getUserInfo() || {}
    const newUserInfo = {
      ...userInfo,
      avatar: this.data.avatar,
      nickname
    }
    setUserInfo(newUserInfo)
    app.globalData.userInfo = newUserInfo

    showToast('保存成功', 'success')
    setTimeout(() => {
      wx.navigateBack()
    }, 800)
  }
})
