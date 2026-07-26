// 后端 API 接口封装
// 引入 utils/index.js 中的 request 相关方法
const { get, post, put, del } = require('./index.js')

/* ===================== 用户认证模块 ===================== */
const userApi = {
  /**
   * 手机号登录
   * @param {string} phone 手机号
   */
  loginByPhone(phone) {
    return post('/miniapp/user/loginByPhone', { phone })
  },

  /**
   * 微信登录
   * @param {Object} data { openid, wechatNickname, wechatAvatar }
   */
  loginByWechat(data) {
    return post('/miniapp/user/loginByWechat', data)
  },

  /**
   * 微信登录（通过 wx.login 获取的 code）
   * @param {string} code 微信登录 code
   */
  loginByWechatCode(code) {
    return post('/miniapp/user/loginByWechatCode', { code })
  },

  /**
   * 更新用户资料
   * @param {Object} data { id, nickname, avatar, wechatNickname, wechatAvatar, ... }
   */
  updateProfile(data) {
    return put('/miniapp/user/updateProfile', data)
  },

  /**
   * 绑定手机号
   * @param {Object} data { userId, phone }
   */
  bindPhone(data) {
    return post('/miniapp/user/bindPhone', data)
  }
}

/* ===================== 用户消息模块 ===================== */
const messageApi = {
  /**
   * 获取消息列表
   * @param {string|number} userId 用户ID
   * @param {number} pageNum 页码
   * @param {number} pageSize 每页数量
   */
  list(userId, pageNum = 1, pageSize = 10) {
    return get('/miniapp/user/message/list', { userId, pageNum, pageSize })
  },

  /**
   * 获取消息详情（自动标记已读）
   * @param {string|number} messageId 消息ID
   */
  detail(messageId) {
    return get(`/miniapp/user/message/${messageId}`)
  },

  /**
   * 获取未读消息数量
   * @param {string|number} userId 用户ID
   */
  unreadCount(userId) {
    return get('/miniapp/user/message/unreadCount', { userId })
  }
}

/* ===================== 轮播图模块 ===================== */
const bannerApi = {
  /** 获取有效轮播图列表 */
  validList() {
    return get('/rental/banner/validList')
  }
}

/* ===================== 房屋管理模块 ===================== */
const houseApi = {
  /**
   * 获取房屋列表
   * @param {Object} params 查询参数，如 { pageNum, pageSize, ... }
   */
  list(params = {}) {
    const { pageNum = 1, pageSize = 10, ...rest } = params
    return get('/rental/house/miniapp/list', { pageNum, pageSize, ...rest })
  },

  /**
   * 获取房屋详情
   * @param {string|number} houseId 房屋ID
   */
  detail(houseId) {
    return get(`/rental/house/miniapp/${houseId}`)
  },

  /**
   * 新增房屋
   * @param {Object} data BizHouse 对象
   */
  add(data) {
    return post('/rental/house/miniapp/publish', data)
  }
}

/* ===================== 电话簿模块 ===================== */
const phonebookApi = {
  /** 获取全部商家列表 */
  selectAll() {
    return get('/rental/phonebook/selectAll')
  },

  /**
   * 申请收录商家
   * @param {Object} data 商家申请信息
   */
  apply(data) {
    return post('/rental/phonebookApply/apply', data)
  }
}

/* ===================== 小区管理模块 ===================== */
const communityApi = {
  /** 获取全部小区列表 */
  selectAll() {
    return get('/rental/community/selectAll')
  },

  /**
   * 申请登记小区
   * @param {Object} data 小区申请信息
   */
  apply(data) {
    return post('/rental/communityApply/apply', data)
  }
}

/* ===================== 租赁合同模块 ===================== */
const rentalContractApi = {
  /**
   * 获取当前用户作为租客的在租房屋列表（仅生效中合同）
   * @param {string|number} tenantId 租客用户ID
   */
  myRentals(tenantId) {
    return get(`/rental/rentalContract/myRentals/${tenantId}`)
  }
}

/* ===================== 维修管理模块 ===================== */
const repairApi = {
  /**
   * 提交维修申请
   * @param {Object} data 维修申请信息
   */
  apply(data) {
    return post('/rental/repair/apply', data)
  },

  /**
   * 租客确认维修完成
   * @param {string|number} repairId 维修单ID
   */
  tenantConfirm(repairId) {
    return put(`/rental/repair/tenantConfirm/${repairId}`)
  },

  /**
   * 租客上传维修凭证
   * @param {Object} data 维修凭证信息
   */
  tenantUploadReceipt(data) {
    return put('/rental/repair/tenantUploadReceipt', data)
  },

  /**
   * 取消维修
   * @param {string|number} repairId 维修单ID
   */
  cancel(repairId) {
    return put(`/rental/repair/cancel/${repairId}`)
  },

  /**
   * 获取维修列表（按 userId 筛选）
   * @param {Object} params { userId, pageNum, pageSize, ... }
   */
  list(params = {}) {
    const { pageNum = 1, pageSize = 10, ...rest } = params
    return get('/rental/repair/miniapp/list', { pageNum, pageSize, ...rest })
  }
}

/* ===================== 邀请管理模块 ===================== */
const inviteApi = {
  /**
   * 绑定邀请关系
   * @param {Object} data 邀请绑定信息
   */
  bindInvite(data) {
    return post('/rental/invite/bindInvite', data)
  },

  /**
   * 获取邀请统计
   * @param {string|number} inviterId 邀请人ID
   */
  statistics(inviterId) {
    return get(`/rental/invite/miniapp/statistics/${inviterId}`)
  },

  /**
   * 获取邀请列表
   * @param {string|number} inviterId 邀请人ID
   */
  inviteList(inviterId) {
    return get(`/rental/invite/miniapp/inviteList/${inviterId}`)
  }
}

/* ===================== 兑换商城模块 ===================== */
const mallApi = {
  /** 获取上架商品列表 */
  productList() {
    return get('/rental/mallProduct/selectAll')
  },

  /**
   * 兑换商品
   * @param {Object} data 兑换信息
   */
  exchange(data) {
    return post('/rental/mallRecord/exchange', data)
  },

  /**
   * 获取用户兑换记录
   * @param {string|number} userId 用户ID
   */
  userRecord(userId) {
    return get(`/rental/mallRecord/userRecord/${userId}`)
  },

  /**
   * 获取用户配额
   * @param {string|number} userId 用户ID
   */
  userQuota(userId) {
    return get(`/rental/exchangeQuota/userQuota/${userId}`)
  }
}

/* ===================== 收藏模块 ===================== */
const favoriteApi = {
  /**
   * 收藏房源
   * @param {Object} data 收藏信息，如 { userId, houseId }
   */
  add(data) {
    return post('/rental/favorite/add', data)
  },

  /**
   * 取消收藏
   * @param {string|number} userId 用户ID
   * @param {string|number} houseId 房屋ID
   */
  cancel(userId, houseId) {
    return del('/rental/favorite/cancel', { userId, houseId })
  },

  /**
   * 获取用户收藏列表
   * @param {string|number} userId 用户ID
   */
  userFavorite(userId) {
    return get(`/rental/favorite/userFavorite/${userId}`)
  }
}

/* ===================== 浏览记录模块 ===================== */
const browseApi = {
  /**
   * 记录浏览
   * @param {Object} data 浏览记录信息
   */
  record(data) {
    return post('/rental/browse/record', data)
  },

  /**
   * 获取用户浏览记录
   * @param {string|number} userId 用户ID
   */
  userBrowse(userId) {
    return get(`/rental/browse/userBrowse/${userId}`)
  }
}

/* ===================== 聊天消息模块 ===================== */
const chatApi = {
  /**
   * 发送消息
   * @param {Object} data 消息内容
   */
  send(data) {
    return post('/rental/message/send', data)
  },

  /**
   * 获取聊天记录
   * @param {string|number} userId1 用户1ID
   * @param {string|number} userId2 用户2ID
   */
  history(userId1, userId2) {
    return get('/rental/message/history', { userId1, userId2 })
  },

  /**
   * 标记消息已读
   * @param {string|number} messageId 消息ID
   */
  markAsRead(messageId) {
    return put(`/rental/message/markAsRead/${messageId}`)
  }
}

module.exports = {
  userApi,
  messageApi,
  bannerApi,
  houseApi,
  phonebookApi,
  communityApi,
  rentalContractApi,
  repairApi,
  inviteApi,
  mallApi,
  favoriteApi,
  browseApi,
  chatApi
}
