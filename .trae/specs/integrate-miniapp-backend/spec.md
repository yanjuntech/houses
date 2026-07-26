# 小程序对接后台接口 - Spec

## Why
小程序当前只有首页和个人中心的骨架页面，所有数据均为静态 mock 数据，未与后端 API 对接。需要将小程序与已开发完成的后端接口对接，实现完整的业务功能，让小程序可以真正使用。

## What Changes
- 对接用户认证接口（手机号登录、微信登录、绑定手机号、实名认证）
- 首页对接轮播图、房屋列表、电话簿等真实数据
- 新增房屋列表页、房屋详情页（含浏览记录、收藏功能）
- 新增电话簿页（商家分类浏览、搜索）
- 新增消息中心页（消息列表、消息详情、未读数量）
- 新增收藏列表页
- 新增维修申请页（提交维修、查看维修记录）
- 新增邀请管理页（邀请统计、邀请列表、绑定邀请关系）
- 新增兑换商城页（商品列表、兑换记录）
- 新增房屋发布页（房东发布房源、发布配额查询）
- 个人中心对接真实用户信息、实名认证、身份切换
- 新增登录页
- 全局封装网络请求（统一 token 管理、错误处理）

## Impact
- Affected specs: wechat-miniapp, miniapp-rental-enhance, extend-mp-rental-platform
- Affected code: miniapp/app.js, miniapp/app.json, miniapp/utils/index.js, miniapp/pages/*, miniapp/components/*

## ADDED Requirements

### Requirement: 用户登录认证
系统 SHALL 提供手机号登录和微信登录两种方式，登录后保存用户信息和 token 到本地存储。

#### Scenario: 手机号登录
- **WHEN** 用户输入手机号并点击登录
- **THEN** 调用 `/miniapp/user/loginByPhone` 接口，成功后保存用户信息并跳转首页

#### Scenario: 微信登录
- **WHEN** 用户点击微信一键登录
- **THEN** 调用 `wx.login` 获取 code，调用 `/miniapp/user/loginByWechat` 接口，成功后保存用户信息

### Requirement: 首页数据展示
系统 SHALL 在首页展示轮播图、快捷入口、热门房源、商家推荐等真实数据。

#### Scenario: 加载首页数据
- **WHEN** 用户进入首页
- **THEN** 调用 `/rental/banner/validList` 获取轮播图，调用房屋列表接口获取热门房源，调用电话簿接口获取推荐商家

### Requirement: 房屋列表与详情
系统 SHALL 提供房屋列表浏览和详情查看功能，支持筛选和搜索。

#### Scenario: 浏览房屋列表
- **WHEN** 用户进入房屋列表页
- **THEN** 调用房屋列表接口，展示房源卡片列表，支持下拉刷新和上拉加载

#### Scenario: 查看房屋详情
- **WHEN** 用户点击某个房源
- **THEN** 跳转到详情页，调用详情接口获取完整信息，同时记录浏览记录

#### Scenario: 收藏房源
- **WHEN** 用户在详情页点击收藏
- **THEN** 调用收藏接口，收藏状态实时更新

### Requirement: 电话簿浏览
系统 SHALL 提供电话簿浏览功能，支持按分类筛选和搜索商家。

#### Scenario: 浏览电话簿
- **WHEN** 用户进入电话簿页
- **THEN** 调用 `/rental/phonebook/selectAll` 获取商家列表，支持按分类筛选

### Requirement: 消息中心
系统 SHALL 提供消息列表和详情查看功能，展示未读消息数量。

#### Scenario: 查看消息列表
- **WHEN** 用户进入消息中心
- **THEN** 调用 `/miniapp/user/message/list` 获取消息列表，显示未读数量徽标

#### Scenario: 查看消息详情
- **WHEN** 用户点击某条消息
- **THEN** 调用 `/miniapp/user/message/{messageId}` 获取详情，自动标记为已读

### Requirement: 维修申请
系统 SHALL 允许用户提交维修申请并查看维修记录。

#### Scenario: 提交维修申请
- **WHEN** 用户填写维修信息并提交
- **THEN** 调用 `/rental/repair/apply` 接口，提交成功后显示提示

#### Scenario: 查看维修记录
- **WHEN** 用户进入维修记录页
- **THEN** 调用维修列表接口，展示维修状态和时间线

### Requirement: 邀请管理
系统 SHALL 提供邀请统计展示和邀请关系绑定功能。

#### Scenario: 查看邀请统计
- **WHEN** 用户进入邀请页
- **THEN** 调用统计接口，展示总邀请人数和已认证人数

#### Scenario: 绑定邀请关系
- **WHEN** 新用户通过邀请链接注册
- **THEN** 调用 `/rental/invite/bindInvite` 绑定邀请关系

### Requirement: 兑换商城
系统 SHALL 提供商品浏览和兑换功能。

#### Scenario: 浏览商品
- **WHEN** 用户进入商城页
- **THEN** 调用 `/rental/mallProduct/selectAll` 获取上架商品列表

#### Scenario: 兑换商品
- **WHEN** 用户点击兑换
- **THEN** 调用 `/rental/mallRecord/exchange` 接口，扣减配额并提示成功

### Requirement: 房屋发布
系统 SHALL 允许房东用户发布房源，发布时扣减发布配额。

#### Scenario: 查看发布配额
- **WHEN** 房东进入发布页
- **THEN** 调用配额查询接口，显示剩余发布次数

#### Scenario: 发布房源
- **WHEN** 房东填写房源信息并提交
- **THEN** 调用房屋新增接口，成功后扣减配额并返回列表

### Requirement: 个人中心
系统 SHALL 在个人中心展示真实用户信息，支持实名认证、查看收藏、浏览记录等。

#### Scenario: 展示用户信息
- **WHEN** 用户进入个人中心
- **THEN** 从本地存储读取用户信息并展示头像、昵称、手机号

#### Scenario: 实名认证
- **WHEN** 用户提交实名认证信息
- **THEN** 调用认证接口，等待后台审核

#### Scenario: 查看收藏列表
- **WHEN** 用户点击我的收藏
- **THEN** 跳转到收藏列表页，调用收藏查询接口
