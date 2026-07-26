# 小程序后台服务全对接测试验证 - Product Requirement Document

## Overview
- **Summary**: 验证微信小程序前端与后端服务的完整对接，确保所有页面功能正常调用后台 API，发布房源时小区下拉选择功能正常工作，并生成全面的测试报告。
- **Purpose**: 确保小程序端所有业务功能（登录、房屋管理、维修管理、邀请管理、兑换商城等）均能正确调用后端公开接口，不存在"功能开发中"的缺失状态。
- **Target Users**: 小程序终端用户、测试人员

## Goals
- 验证发布房源页面小区下拉选择功能正常调用后端接口
- 验证所有前端页面与后端 API 的完整对接
- 模拟人工点击测试所有功能模块
- 生成详细的测试报告

## Non-Goals (Out of Scope)
- 不修改后端 API 接口定义
- 不新增前端页面（仅验证现有页面）
- 不进行性能测试或并发测试

## Background & Context
- 前端已实现 21 个页面，13 个业务模块的 API 封装
- 后端已添加 10 个小程序端公开接口（@Anonymous）
- API 路径已同步更新为 `/miniapp/*` 格式
- 当前需要验证的主要功能：
  - 登录（手机号/微信一键登录）
  - 房屋管理（列表、详情、发布、收藏、浏览）
  - 维修管理（申请、记录列表、详情）
  - 电话簿（列表、申请收录）
  - 邀请管理（统计、列表、绑定）
  - 兑换商城（商品列表、兑换、记录、配额）
  - 个人中心（资料编辑、实名认证）

## Functional Requirements
- **FR-1**: 发布房源页面小区下拉选择功能正常加载小区列表
- **FR-2**: 所有页面 API 调用路径与后端一致
- **FR-3**: 登录功能正常调用后端接口
- **FR-4**: 房屋列表/详情/发布功能正常调用后端接口
- **FR-5**: 维修申请/记录列表功能正常调用后端接口
- **FR-6**: 邀请统计/列表功能正常调用后端接口
- **FR-7**: 兑换商城/记录/配额功能正常调用后端接口
- **FR-8**: 收藏/浏览记录功能正常调用后端接口
- **FR-9**: 电话簿列表/申请功能正常调用后端接口
- **FR-10**: 个人中心功能正常调用后端接口

## Non-Functional Requirements
- **NFR-1**: API 调用应返回正确的数据格式（AjaxResult / TableDataInfo）
- **NFR-2**: 错误处理应友好提示用户
- **NFR-3**: 页面加载应有 loading 状态提示

## Constraints
- **Technical**: 微信小程序框架，后端 Spring Boot + MyBatis
- **Dependencies**: 后端服务需正常运行

## Acceptance Criteria

### AC-1: 小区下拉选择正常
- **Given**: 用户进入发布房源页面
- **When**: 页面加载完成
- **Then**: 小区下拉选择器显示小区列表，包含"请选择小区"占位项
- **Verification**: `programmatic`

### AC-2: 登录功能正常
- **Given**: 用户进入登录页面
- **When**: 输入手机号并点击登录
- **Then**: 调用 `/miniapp/user/loginByPhone` 接口，成功后跳转首页
- **Verification**: `programmatic`

### AC-3: 房屋列表功能正常
- **Given**: 用户进入房屋列表页面
- **When**: 页面加载完成
- **Then**: 调用 `/rental/house/miniapp/list` 接口，显示房源列表
- **Verification**: `programmatic`

### AC-4: 房屋详情功能正常
- **Given**: 用户点击某房源
- **When**: 进入房屋详情页面
- **Then**: 调用 `/rental/house/miniapp/{houseId}` 接口，显示房源详情
- **Verification**: `programmatic`

### AC-5: 房屋发布功能正常
- **Given**: 用户填写完整发布表单并提交
- **When**: 点击发布按钮
- **Then**: 调用 `/rental/house/miniapp/publish` 接口，发布成功
- **Verification**: `programmatic`

### AC-6: 维修申请功能正常
- **Given**: 用户进入维修申请页面
- **When**: 选择在租房屋并提交申请
- **Then**: 调用 `/rental/repair/apply` 接口，申请成功
- **Verification**: `programmatic`

### AC-7: 维修记录功能正常
- **Given**: 用户进入维修记录页面
- **When**: 页面加载完成
- **Then**: 调用 `/rental/repair/miniapp/list` 接口，显示维修列表
- **Verification**: `programmatic`

### AC-8: 邀请统计功能正常
- **Given**: 用户进入邀请管理页面
- **When**: 页面加载完成
- **Then**: 调用 `/rental/invite/miniapp/statistics/{inviterId}` 接口，显示统计数据
- **Verification**: `programmatic`

### AC-9: 邀请列表功能正常
- **Given**: 用户进入邀请管理页面
- **When**: 页面加载完成
- **Then**: 调用 `/rental/invite/miniapp/inviteList/{inviterId}` 接口，显示邀请列表
- **Verification**: `programmatic`

### AC-10: 兑换商城功能正常
- **Given**: 用户进入兑换商城页面
- **When**: 页面加载完成
- **Then**: 调用 `/rental/mallProduct/selectAll` 和 `/rental/exchangeQuota/userQuota/{userId}` 接口，显示商品和配额
- **Verification**: `programmatic`

### AC-11: 兑换记录功能正常
- **Given**: 用户进入兑换记录页面
- **When**: 页面加载完成
- **Then**: 调用 `/rental/mallRecord/userRecord/{userId}` 接口，显示兑换记录
- **Verification**: `programmatic`

### AC-12: 收藏功能正常
- **Given**: 用户在房源详情页点击收藏
- **When**: 点击收藏按钮
- **Then**: 调用 `/rental/favorite/add` 或 `/rental/favorite/cancel` 接口
- **Verification**: `programmatic`

### AC-13: 收藏列表功能正常
- **Given**: 用户进入我的收藏页面
- **When**: 页面加载完成
- **Then**: 调用 `/rental/favorite/userFavorite/{userId}` 接口，显示收藏列表
- **Verification**: `programmatic`

### AC-14: 浏览记录功能正常
- **Given**: 用户进入浏览记录页面
- **When**: 页面加载完成
- **Then**: 调用 `/rental/browse/userBrowse/{userId}` 接口，显示浏览记录
- **Verification**: `programmatic`

### AC-15: 电话簿功能正常
- **Given**: 用户进入电话簿页面
- **When**: 页面加载完成
- **Then**: 调用 `/rental/phonebook/selectAll` 接口，显示商家列表
- **Verification**: `programmatic`

### AC-16: 电话簿申请功能正常
- **Given**: 用户进入电话簿申请页面并提交
- **When**: 点击提交按钮
- **Then**: 调用 `/rental/phonebookApply/apply` 接口，申请成功
- **Verification**: `programmatic`

### AC-17: 个人中心功能正常
- **Given**: 用户进入个人中心页面
- **When**: 页面加载完成
- **Then**: 显示用户信息、统计数据、未读消息数
- **Verification**: `human-judgment`

### AC-18: 资料编辑功能正常
- **Given**: 用户进入资料编辑页面并保存
- **When**: 点击保存按钮
- **Then**: 调用 `/miniapp/user/updateProfile` 接口，保存成功
- **Verification**: `programmatic`

### AC-19: 实名认证功能正常
- **Given**: 用户进入实名认证页面并提交
- **When**: 点击提交按钮
- **Then**: 调用后端实名认证接口，认证成功
- **Verification**: `programmatic`

### AC-20: 所有 API 路径一致
- **Given**: 检查前端 api.js 和后端 Controller
- **When**: 对比所有接口路径
- **Then**: 前端 API 路径与后端接口路径完全一致
- **Verification**: `programmatic`

## Open Questions
- [ ] 后端服务是否已启动运行？
- [ ] 是否有测试账号可用？
