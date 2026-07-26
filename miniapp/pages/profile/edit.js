// 编辑资料页
const app = getApp()
const { uploadApi } = require('../../utils/api.js')
const { showToast, showLoading, hideLoading, getUserInfo, setUserInfo } = require('../../utils/index.js')

Page({
  data: {
    // 默认头像
    defaultAvatar: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=user%20avatar%20icon%20simple%20minimal%20design&image_size=square',
    // 当前头像
    avatar: '',
    // 新选择的本地头像路径（需上传到服务器）
    pendingAvatar: '',
    // 当前昵称
    nickname: '',
    // 保存状态
    saving: false
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
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      sizeType: ['compressed'],
      success: (res) => {
        const tempFile = res.tempFiles && res.tempFiles[0]
        const tempFilePath = tempFile && tempFile.tempFilePath
        if (tempFilePath) {
          // 先在界面预览本地路径，待保存时再上传到服务器
          this.setData({
            avatar: tempFilePath,
            pendingAvatar: tempFilePath
          })
        }
      }
    })
  },

  // 保存
  handleSave() {
    if (this.data.saving) return

    const nickname = (this.data.nickname || '').trim()
    if (!nickname) {
      showToast('请输入昵称')
      return
    }
    if (nickname.length > 20) {
      showToast('昵称不能超过20个字')
      return
    }

    this.setData({ saving: true })
    showLoading('保存中')

    // 如果有新选择的本地头像，先上传到服务器获取 URL
    const uploadTask = this.data.pendingAvatar
      ? uploadApi.uploadImage(this.data.pendingAvatar)
      : Promise.resolve(this.data.avatar)

    uploadTask.then(avatarUrl => {
      const userInfo = getUserInfo() || {}
      const newUserInfo = {
        ...userInfo,
        avatar: avatarUrl || this.data.avatar,
        nickname
      }
      setUserInfo(newUserInfo)
      app.globalData.userInfo = newUserInfo

      hideLoading()
      this.setData({ saving: false, pendingAvatar: '' })
      showToast('保存成功', 'success')
      setTimeout(() => {
        wx.navigateBack()
      }, 800)
    }).catch(err => {
      hideLoading()
      this.setData({ saving: false })
      showToast(err && err.message ? err.message : '头像上传失败')
    })
  }
})
