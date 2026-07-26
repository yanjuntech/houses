# 小程序核心功能完善 - Spec

## Why
小程序虽已对接后台基础接口，但部分核心功能存在缺陷或不完整：微信一键登录将 `wx.login` 的 code 错误地当作 openid 传递（后端无法正确识别用户）；维修申请使用自由文本填写房屋地址，无法关联到租客实际租赁的房源；电话簿分类为顶部横向标签，分类较多时不便浏览；房屋发布页已实现但缺少入口；电话簿申请收录仅为弹窗，体验不佳；首页轮播图在后端无数据时不显示，缺少占位与管理能力。

## What Changes
- 完善微信一键登录流程：后端新增 `code2Session` 换取 openid 接口，前端修正 `loginByWechat` 调用，使用真实 openid 登录
- 维修申请改为选择租赁房屋：后端新增小程序端"查询我的在租房屋"公开接口，前端维修申请页用房源选择器替换地址输入框
- 电话簿改造为左右分栏布局：左侧分类列表（纵向），右侧商家列表
- 房屋发布功能增加入口：在房屋列表 tab 页增加"我要发布"浮动按钮，在个人中心菜单保留入口
- 电话簿申请收录改为独立页面：从弹窗升级为 `pages/phonebook/apply` 独立页面，支持完整表单
- 首页轮播图功能增强：无数据时展示默认占位图，支持点击轮播图跳转，后端轮播图管理增加跳转链接字段

## Impact
- Affected specs: integrate-miniapp-backend, extend-mp-rental-platform, miniapp-rental-enhance
- Affected code:
  - 后端：`BizMiniappUserController`（新增 code2Session 接口）、`BizRentalContractController`（新增小程序端查询接口）、`BizBannerController`（跳转链接字段）
  - 后端实体：`BizBanner`（增加 jumpUrl 字段）、`BizMiniappUser`（openid 字段校验）
  - 小程序：`pages/login/login.js`、`pages/repair/apply.*`、`pages/phonebook/index.*`、`pages/phonebook/apply.*`（新增）、`pages/house/list.*`（增加发布入口）、`pages/index/index.*`、`utils/api.js`

## ADDED Requirements

### Requirement: 微信登录 code 换取 openid
系统 SHALL 在后端提供 `POST /miniapp/user/loginByWechatCode` 接口，接收 wx.login 的 code，调用微信 code2Session 接口换取 openid 和 session_key，根据 openid 查询或注册用户，返回用户信息。

#### Scenario: 微信一键登录
- **WHEN** 用户点击微信一键登录
- **THEN** 前端调用 `wx.login` 获取 code，调用 `POST /miniapp/user/loginByWechatCode`（携带 code），后端换取 openid 后返回用户信息，前端保存并跳转首页

#### Scenario: 获取用户头像昵称
- **WHEN** 登录成功后需要完善用户资料
- **THEN** 前端使用 `wx.getUserProfile` 获取头像昵称，调用 `PUT /miniapp/user/updateProfile` 更新用户信息

### Requirement: 查询我的在租房屋
系统 SHALL 在后端提供 `GET /rental/rentalContract/myRentals/{tenantId}` 公开接口，返回当前用户作为租客的在租房屋列表（仅返回生效中的合同）。

#### Scenario: 查询在租房屋
- **WHEN** 租客进入维修申请页
- **THEN** 调用接口获取在租房屋列表，展示为可选择的房源卡片

### Requirement: 维修申请绑定租赁房屋
系统 SHALL 在维修申请页要求用户从在租房屋中选择一处房源，提交时携带 houseId、tenantId、landlordId，不再使用自由文本地址。

#### Scenario: 选择维修房屋
- **WHEN** 用户进入维修申请页
- **THEN** 展示在租房屋列表，用户选择一处后填写问题描述和联系方式

#### Scenario: 无在租房屋
- **WHEN** 用户无在租房屋记录
- **THEN** 提示"您当前无在租房屋，无法提交维修申请"，禁用提交按钮

### Requirement: 电话簿左右分栏布局
系统 SHALL 在电话簿页采用左右分栏布局：左侧为分类列表（纵向滚动），右侧为对应分类的商家列表。

#### Scenario: 切换分类
- **WHEN** 用户点击左侧某个分类
- **THEN** 右侧商家列表刷新为该分类下的商家，左侧当前分类高亮

#### Scenario: 搜索商家
- **WHEN** 用户在搜索框输入关键词
- **THEN** 右侧列表按关键词过滤（忽略当前分类），左侧"全部"高亮

### Requirement: 电话簿申请收录独立页
系统 SHALL 提供独立的电话簿申请收录页 `pages/phonebook/apply`，包含完整表单（商家名称、联系电话1、联系电话2、分类、地址、备注）。

#### Scenario: 提交申请
- **WHEN** 用户填写完表单点击提交
- **THEN** 调用 `POST /rental/phonebookApply/apply` 提交，成功后返回电话簿页

### Requirement: 房屋发布入口
系统 SHALL 在房屋列表 tab 页右下角显示"我要发布"浮动按钮，点击跳转到房屋发布页（需登录并实名认证）。

#### Scenario: 未登录点击发布
- **WHEN** 未登录用户点击"我要发布"
- **THEN** 提示"请先登录"并跳转登录页

#### Scenario: 未实名认证点击发布
- **WHEN** 已登录但未实名认证的用户点击"我要发布"
- **THEN** 提示"请先完成实名认证"并可跳转实名认证页

### Requirement: 首页轮播图占位与跳转
系统 SHALL 在首页轮播图无数据时展示默认占位图，并支持点击轮播图跳转到指定链接（小程序内页面）。

#### Scenario: 无轮播图数据
- **WHEN** 后端返回轮播图列表为空
- **THEN** 首页展示默认占位轮播图（1 张）

#### Scenario: 点击轮播图跳转
- **WHEN** 用户点击某张轮播图
- **THEN** 跳转到该轮播图配置的链接（jumpUrl），无链接时不跳转

## MODIFIED Requirements

### Requirement: 微信登录
[原：前端将 wx.login 的 code 作为 openid 直接传给后端 loginByWechat]
[新：前端将 code 传给后端 loginByWechatCode，后端调用微信 code2Session 换取真实 openid]

### Requirement: 维修申请表单
[原：自由文本填写房屋地址]
[新：从在租房屋列表中选择，提交时携带 houseId/tenantId/landlordId]

### Requirement: 电话簿页面布局
[原：顶部横向分类标签 + 下方商家列表]
[新：左侧纵向分类列表 + 右侧商家列表]

### Requirement: 电话簿申请收录
[原：弹窗形式提交]
[新：独立页面提交，支持更完整表单]

### Requirement: 首页轮播图
[原：无数据时不显示]
[新：无数据时展示默认占位图，支持点击跳转]
