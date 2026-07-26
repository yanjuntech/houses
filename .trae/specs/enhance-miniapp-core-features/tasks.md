# Tasks

- [x] Task 1: 完善微信一键登录流程（后端）
  - [x] SubTask 1.1: 在 `BizMiniappUserController` 新增 `POST /miniapp/user/loginByWechatCode` 接口，接收 `{code}`，调用微信 code2Session 接口（使用 appId + appSecret）换取 openid 和 session_key
  - [x] SubTask 1.2: 在 `application.yml` 或 `sys_config` 配置微信小程序 appId 与 appSecret，提供配置读取工具
  - [x] SubTask 1.3: 根据 openid 查询 `biz_miniapp_user`，不存在则自动注册（userType=0, verifyStatus=0），存在则返回用户信息
  - [x] SubTask 1.4: 新增 `PUT /miniapp/user/updateProfile` 公开接口，更新 nickname、avatar、wechatNickname、wechatAvatar 字段
  - [x] SubTask 1.5: 保留旧 `loginByWechat` 接口兼容性（标记为 @Deprecated）

- [x] Task 2: 完善微信一键登录流程（前端）
  - [x] SubTask 2.1: 修改 `utils/api.js`，新增 `userApi.loginByWechatCode(code)` 调用 `POST /miniapp/user/loginByWechatCode`，新增 `userApi.updateProfile(data)` 调用 `PUT /miniapp/user/updateProfile`
  - [x] SubTask 2.2: 修改 `pages/login/login.js` 的 `handleWechatLogin`：调用 `wx.login` 获取 code → 调用 `loginByWechatCode(code)` 登录 → 登录成功后调用 `wx.getUserProfile` 获取头像昵称 → 调用 `updateProfile` 更新资料
  - [x] SubTask 2.3: 优化登录页 UI：微信登录按钮使用绿色按钮突出展示，登录失败显示友好错误提示

- [x] Task 3: 查询我的在租房屋接口（后端）
  - [x] SubTask 3.1: 在 `BizRentalContractController` 新增 `GET /rental/rentalContract/myRentals/{tenantId}` 公开接口（@Anonymous），返回 tenantId 对应的在租房屋列表
  - [x] SubTask 3.2: 查询条件：tenantId 匹配、status 为生效中（未过期未取消），按 createTime 倒序
  - [x] SubTask 3.3: 返回字段包含：rentalId、houseId、houseTitle、communityName、houseAddress、landlordId、landlordName、landlordPhone、tenantName、tenantPhone、rentalStart、rentalEnd

- [x] Task 4: 维修申请页改造为选择租赁房屋（前端）
  - [x] SubTask 4.1: 修改 `utils/api.js`，新增 `rentalContractApi.myRentals(tenantId)` 调用 `GET /rental/rentalContract/myRentals/{tenantId}`
  - [x] SubTask 4.2: 修改 `pages/repair/apply.js`：onLoad 时调用 `myRentals` 获取在租房屋列表，无数据时提示并禁用提交按钮
  - [x] SubTask 4.3: 替换原"房屋地址"输入框为在租房屋选择器（picker 或卡片列表），选中后填充 houseId、tenantId、landlordId、houseTitle
  - [x] SubTask 4.4: 修改 `handleSubmit` 提交数据：携带 houseId、tenantId、landlordId、houseTitle、contactName、contactPhone、description、appointmentDate，移除 houseAddress
  - [x] SubTask 4.5: 更新 `pages/repair/apply.wxml` 和 `apply.wxss`：展示在租房屋选择卡片，选中状态高亮

- [x] Task 5: 电话簿左右分栏布局（前端）
  - [x] SubTask 5.1: 修改 `pages/phonebook/index.wxml`：左侧固定宽度（180rpx）纵向分类列表，右侧 flex 自适应商家列表
  - [x] SubTask 5.2: 修改 `pages/phonebook/index.wxss`：使用 flex 布局实现左右分栏，分类项纵向排列、可滚动
  - [x] SubTask 5.3: 修改 `pages/phonebook/index.js`：分类切换逻辑保持，搜索时清空当前分类并高亮"全部"
  - [x] SubTask 5.4: 优化分类项样式：未选中灰字、选中蓝色背景白字、带商家数量徽标
  - [x] SubTask 5.5: 调整底部"申请收录"按钮为右侧浮动按钮或顶部右侧入口

- [x] Task 6: 电话簿申请收录独立页（前端）
  - [x] SubTask 6.1: 创建 `pages/phonebook/apply` 页面（wxml/wxss/js/json），导航栏标题"申请收录商家"
  - [x] SubTask 6.2: 表单字段：商家名称、联系电话1、联系电话2（选填）、商家分类（picker）、商家地址、备注
  - [x] SubTask 6.3: 表单校验：商家名称必填、电话1必填且格式校验、分类必选、地址必填
  - [x] SubTask 6.4: 提交调用 `phonebookApi.apply(data)`（`POST /rental/phonebookApply/apply`），成功后返回上一页
  - [x] SubTask 6.5: 修改 `pages/phonebook/index.js` 的 `handleApply`：从弹窗改为 `wx.navigateTo({ url: '/pages/phonebook/apply' })`
  - [x] SubTask 6.6: 删除原弹窗相关代码（showApplyPopup、applyForm 等），保留入口按钮
  - [x] SubTask 6.7: 在 `app.json` 中注册 `pages/phonebook/apply` 路由

- [x] Task 7: 房屋发布入口（前端）
  - [x] SubTask 7.1: 修改 `pages/house/list.wxml`：在页面右下角添加"我要发布"浮动按钮（fixed 定位）
  - [x] SubTask 7.2: 修改 `pages/house/list.wxss`：浮动按钮样式（圆形或圆角矩形、蓝色背景、阴影）
  - [x] SubTask 7.3: 修改 `pages/house/list.js`：点击按钮校验登录态（未登录跳登录页）和实名认证（未认证提示并跳转 verify 页），通过后 `wx.navigateTo` 到 `/pages/house/publish`

- [x] Task 8: 首页轮播图占位与跳转（前端）
  - [x] SubTask 8.1: 修改 `pages/index/index.js`：`loadBanners` 接口失败或返回空时，设置默认占位轮播图（1 张，使用默认图片 URL）
  - [x] SubTask 8.2: 修改 `pages/index/index.wxml`：移除 `wx:if="{{banners.length > 0}}"` 条件，轮播图始终显示；为 swiper-item 添加 `bindtap="handleBannerTap"` 携带 `data-url`
  - [x] SubTask 8.3: 修改 `pages/index/index.js`：新增 `handleBannerTap` 方法，有 jumpUrl 时 `wx.navigateTo` 跳转，无则忽略
  - [x] SubTask 8.4: 修改 `pages/index/index.wxss`：轮播图占位样式（默认背景色 + 提示文案）

- [x] Task 9: 后端轮播图跳转链接字段
  - [x] SubTask 9.1: 修改 `BizBanner` 实体，增加 `jumpUrl` 字段（String，小程序内跳转路径）
  - [x] SubTask 9.2: 修改 `BizBannerMapper.xml` 的 resultMap 和 SQL，包含 jumpUrl 字段
  - [ ] SubTask 9.3: 执行 SQL：`ALTER TABLE biz_banner ADD COLUMN jump_url VARCHAR(255) COMMENT '点击跳转链接'`（需手动执行，迁移脚本位于 /workspace/sql/banner_add_jumpurl.sql）

- [ ] Task 10: 联调与验证
  - [ ] SubTask 10.1: 重新编译后端（`mvn clean package -DskipTests`），启动后端服务
  - [ ] SubTask 10.2: 验证微信登录流程：前端调用 loginByWechatCode 获取真实 openid 并登录成功
  - [ ] SubTask 10.3: 验证维修申请：选择在租房屋后提交维修申请，数据正确携带 houseId/tenantId/landlordId
  - [ ] SubTask 10.4: 验证电话簿左右分栏布局：分类切换、搜索过滤正常
  - [ ] SubTask 10.5: 验证电话簿申请收录独立页：表单提交成功
  - [ ] SubTask 10.6: 验证房屋发布入口：未登录/未认证拦截正常，已认证可跳转发布页
  - [ ] SubTask 10.7: 验证首页轮播图：无数据时展示占位图，点击有 jumpUrl 的轮播图可跳转

# Task Dependencies
- [Task 2] 依赖 [Task 1] 完成后端 code2Session 接口
- [Task 4] 依赖 [Task 3] 完成后端在租房屋查询接口
- [Task 8] 依赖 [Task 9] 完成后端 jumpUrl 字段（仅点击跳转需要）
- [Task 10] 依赖所有任务完成
- [Task 1, 3, 9] 后端任务之间无依赖，可并行实现
- [Task 2, 4, 5, 6, 7, 8] 前端任务之间无依赖，可并行实现（前端可先用 mock 数据开发，再与后端联调）
