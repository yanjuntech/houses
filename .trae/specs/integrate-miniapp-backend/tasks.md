# Tasks

- [x] Task 1: 封装全局网络请求与用户状态管理
  - [x] SubTask 1.1: 修改 `utils/index.js`，封装统一的 request 方法，支持 baseUrl 配置、token 自动注入、401 跳转登录、错误统一提示
  - [x] SubTask 1.2: 修改 `app.js`，添加全局用户状态管理（globalData 中存储 userInfo、token），添加 checkLogin 方法
  - [x] SubTask 1.3: 创建 `utils/api.js`，按模块封装所有后端 API 调用（用户、房屋、电话簿、消息、维修、邀请、商城、收藏、浏览、轮播图、小区）

- [x] Task 2: 创建登录页并对接认证接口
  - [x] SubTask 2.1: 创建 `pages/login/login` 页面（wxml/wxss/js/json），提供手机号登录和微信登录两个入口
  - [x] SubTask 2.2: 对接 `POST /miniapp/user/loginByPhone`（手机号登录，自动注册）
  - [x] SubTask 2.3: 对接 `POST /miniapp/user/loginByWechat`（微信登录，获取 openid）
  - [x] SubTask 2.4: 登录成功后保存用户信息到本地存储，跳转回首页
  - [x] SubTask 2.5: 在 `app.json` 中注册登录页路由

- [x] Task 3: 改造首页对接真实数据
  - [x] SubTask 3.1: 对接 `GET /rental/banner/validList`，首页顶部展示轮播图（swiper 组件）
  - [x] SubTask 3.2: 对接房屋列表接口，首页展示热门房源推荐
  - [x] SubTask 3.3: 对接 `GET /rental/phonebook/selectAll`，首页展示商家推荐
  - [x] SubTask 3.4: 快捷入口跳转到对应功能页（房屋租赁→房屋列表、维修服务→维修申请、电话簿→电话簿页、消息→消息中心）
  - [x] SubTask 3.5: 首页未登录时显示登录引导

- [x] Task 4: 创建房屋列表页和详情页
  - [x] SubTask 4.1: 创建 `pages/house/list` 页面，展示房源卡片列表，支持下拉刷新、上拉加载、筛选条件（小区、价格、户型）
  - [x] SubTask 4.2: 对接房屋列表接口 `GET /rental/house/list`，展示真实房源数据
  - [x] SubTask 4.3: 创建 `pages/house/detail` 页面，展示房源完整信息
  - [x] SubTask 4.4: 对接 `GET /rental/house/{houseId}` 获取详情数据
  - [x] SubTask 4.5: 对接 `POST /rental/browse/record` 记录浏览记录
  - [x] SubTask 4.6: 对接 `POST /rental/favorite/add` 和 `DELETE /rental/favorite/cancel` 实现收藏/取消收藏
  - [x] SubTask 4.7: 在 `app.json` 中注册房屋列表和详情页路由

- [x] Task 5: 创建电话簿页
  - [x] SubTask 5.1: 创建 `pages/phonebook/index` 页面，展示商家列表和分类筛选
  - [x] SubTask 5.2: 对接 `GET /rental/phonebook/selectAll` 获取全部商家数据
  - [x] SubTask 5.3: 实现分类筛选（按 category 字段过滤）和搜索功能
  - [x] SubTask 5.4: 点击商家项可直接拨打电话（wx.makePhoneCall）
  - [x] SubTask 5.5: 在 `app.json` 中注册电话簿页路由

- [x] Task 6: 创建消息中心页
  - [x] SubTask 6.1: 创建 `pages/message/list` 页面，展示消息列表
  - [x] SubTask 6.2: 对接 `GET /miniapp/user/message/list` 获取消息列表
  - [x] SubTask 6.3: 对接 `GET /miniapp/user/message/unreadCount` 获取未读数量，在 tabBar 和个人中心显示徽标
  - [x] SubTask 6.4: 创建 `pages/message/detail` 页面，对接 `GET /miniapp/user/message/{messageId}` 获取详情并自动标记已读
  - [x] SubTask 6.5: 在 `app.json` 中注册消息列表和详情页路由

- [x] Task 7: 创建维修申请页和维修记录页
  - [x] SubTask 7.1: 创建 `pages/repair/apply` 页面，填写维修信息（房屋、问题描述、联系方式）
  - [x] SubTask 7.2: 对接 `POST /rental/repair/apply` 提交维修申请
  - [x] SubTask 7.3: 创建 `pages/repair/list` 页面，展示维修记录列表和状态
  - [x] SubTask 7.4: 对接维修详情接口，展示维修进度时间线
  - [x] SubTask 7.5: 对接 `PUT /rental/repair/tenantConfirm/{repairId}`（租客确认维修完成）
  - [x] SubTask 7.6: 对接 `PUT /rental/repair/cancel/{repairId}`（取消维修）
  - [x] SubTask 7.7: 在 `app.json` 中注册维修页路由

- [x] Task 8: 创建邀请管理页
  - [x] SubTask 8.1: 创建 `pages/invite/index` 页面，展示邀请统计和邀请列表
  - [x] SubTask 8.2: 对接 `GET /rental/invite/statistics/{inviterId}` 获取邀请统计
  - [x] SubTask 8.3: 对接 `GET /rental/invite/inviteList/{inviterId}` 获取邀请列表
  - [x] SubTask 8.4: 对接 `POST /rental/invite/bindInvite` 绑定邀请关系（通过分享链接进入时）
  - [x] SubTask 8.5: 生成邀请海报/分享小程序卡片功能
  - [x] SubTask 8.6: 在 `app.json` 中注册邀请页路由

- [x] Task 9: 创建兑换商城页
  - [x] SubTask 9.1: 创建 `pages/mall/index` 页面，展示商品列表
  - [x] SubTask 9.2: 对接 `GET /rental/mallProduct/selectAll` 获取上架商品
  - [x] SubTask 9.3: 对接 `GET /rental/exchangeQuota/userQuota/{userId}` 查询用户配额
  - [x] SubTask 9.4: 对接 `POST /rental/mallRecord/exchange` 兑换商品
  - [x] SubTask 9.5: 创建 `pages/mall/record` 页面，对接 `GET /rental/mallRecord/userRecord/{userId}` 查看兑换记录
  - [x] SubTask 9.6: 在 `app.json` 中注册商城页路由

- [x] Task 10: 创建房屋发布页（房东功能）
  - [x] SubTask 10.1: 创建 `pages/house/publish` 页面，填写房源信息表单
  - [x] SubTask 10.2: 对接 `GET /rental/community/selectAll` 获取小区下拉列表
  - [x] SubTask 10.3: 对接 `GET /rental/exchangeQuota/userQuota/{userId}` 查询发布配额
  - [x] SubTask 10.4: 对接 `POST /rental/house` 新增房源接口
  - [x] SubTask 10.5: 发布成功后扣减配额并返回列表
  - [x] SubTask 10.6: 在 `app.json` 中注册房屋发布页路由

- [x] Task 11: 改造个人中心页对接真实数据
  - [x] SubTask 11.1: 从本地存储读取用户信息，展示真实头像、昵称、手机号
  - [x] SubTask 11.2: 未登录时显示"请登录"引导
  - [x] SubTask 11.3: 菜单项跳转到对应功能页（我的订单→房屋列表、我的收藏→收藏列表、报修记录→维修列表、消息通知→消息中心、邀请管理→邀请页、兑换商城→商城页）
  - [x] SubTask 11.4: 创建 `pages/profile/edit` 页面，修改昵称、头像等基本信息
  - [x] SubTask 11.5: 创建 `pages/profile/verify` 实名认证页，提交真实姓名、身份证号
  - [x] SubTask 11.6: 在 `app.json` 中注册编辑资料和实名认证页路由

- [x] Task 12: 创建收藏列表页和浏览记录页
  - [x] SubTask 12.1: 创建 `pages/favorite/list` 页面，展示收藏的房源列表
  - [x] SubTask 12.2: 对接 `GET /rental/favorite/userFavorite/{userId}` 获取收藏列表
  - [x] SubTask 12.3: 对接 `DELETE /rental/favorite/cancel` 取消收藏
  - [x] SubTask 12.4: 在 `app.json` 中注册收藏页路由

- [x] Task 13: 更新 app.json tabBar 和全局配置
  - [x] SubTask 13.1: 更新 tabBar，增加房屋和电话簿两个 tab（首页、房屋、电话簿、我的）
  - [x] SubTask 13.2: 生成新增 tab 所需的图标文件
  - [x] SubTask 13.3: 配置全局域名白名单（request 合法域名）
  - [x] SubTask 13.4: 更新 app.wxss 全局样式，确保所有页面风格统一

- [x] Task 14: 更新网页预览版
  - [x] SubTask 14.1: 更新 `preview/index.html`，同步所有新增页面的预览效果
  - [x] SubTask 14.2: 添加页面间导航逻辑，模拟真实小程序跳转

# Task Dependencies
- [Task 2-12] 依赖 [Task 1] 完成（网络请求封装和 API 定义）
- [Task 3] 首页的快捷入口跳转依赖 [Task 4, 5, 6, 7] 对应页面创建完成
- [Task 10] 房屋发布依赖 [Task 2] 登录功能完成（需判断房东身份）
- [Task 11] 个人中心菜单跳转依赖 [Task 4, 6, 7, 8, 9, 12] 对应页面创建完成
- [Task 13] tabBar 更新依赖 [Task 4, 5] 房屋和电话簿页面创建完成
- [Task 14] 预览版更新依赖 [Task 2-13] 全部完成
- [Task 2, 4, 5, 6, 7, 8, 9, 10, 11, 12] 之间无直接依赖，可并行实现
