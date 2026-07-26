# TabBar 中间发布按钮 - Product Requirement Document

## Overview
- **Summary**: 在小程序底部 tabBar 的"房屋"和"电话簿"之间增加一个圆形发布按钮，点击后弹出选择菜单，支持快速发布房屋租赁和申请电话簿收录。
- **Purpose**: 提升用户发布房屋和申请电话簿的操作便捷性，减少操作路径。
- **Target Users**: 小程序所有用户（需登录认证）

## Goals
- 在 tabBar 的"房屋"和"电话簿"之间增加一个醒目的圆形发布按钮
- 点击发布按钮弹出操作菜单，包含"发布房屋"和"申请电话簿收录"两个选项
- 选择"发布房屋"跳转至 `/pages/house/publish`（需登录 + 实名认证）
- 选择"申请电话簿收录"跳转至 `/pages/phonebook/apply`（需登录）

## Non-Goals (Out of Scope)
- 不改变现有 tabBar 的四个页面功能
- 不修改发布房屋和电话簿申请的现有业务逻辑
- 不添加其他发布类型

## Background & Context
- 当前 tabBar 结构：首页 → 房屋 → 电话簿 → 我的
- 房屋发布入口已在 `pages/house/list` 右下角实现（Task 7）
- 电话簿申请收录独立页已创建（Task 6）
- 用户希望有更便捷的统一发布入口

## Functional Requirements
- **FR-1**: tabBar 中间增加发布按钮，位于"房屋"和"电话簿"之间
- **FR-2**: 发布按钮采用圆形设计，居中突出显示
- **FR-3**: 点击发布按钮弹出操作菜单（底部弹窗或居中弹窗）
- **FR-4**: 菜单包含"发布房屋"和"申请电话簿收录"两个选项
- **FR-5**: 选择"发布房屋"校验登录态和实名认证，通过后跳转发布页
- **FR-6**: 选择"申请电话簿收录"校验登录态，通过后跳转申请页

## Non-Functional Requirements
- **NFR-1**: 发布按钮样式与现有 tabBar 风格协调，使用蓝色渐变背景
- **NFR-2**: 弹窗动画流畅，点击遮罩层可关闭
- **NFR-3**: 权限校验提示友好（未登录提示登录，未认证提示认证）

## Constraints
- **Technical**: 微信小程序 tabBar 不支持自定义按钮，需通过中间 tab 页模拟实现
- **Dependencies**: 依赖已实现的 `/pages/house/publish` 和 `/pages/phonebook/apply` 页面

## Assumptions
- 用户已了解发布房屋需要实名认证
- 发布按钮点击后进入的中间页面仅用于展示弹窗，不包含其他内容

## Acceptance Criteria

### AC-1: TabBar 中间显示发布按钮
- **Given**: 用户打开小程序首页或任意 tab 页
- **When**: 查看底部 tabBar
- **Then**: 在"房屋"和"电话簿"之间看到一个圆形发布按钮（蓝色渐变，含"+"图标）
- **Verification**: `human-judgment`

### AC-2: 点击发布按钮弹出操作菜单
- **Given**: 用户点击中间发布按钮
- **When**: 页面跳转至中间 tab 页
- **Then**: 弹出底部弹窗，显示"发布房屋"和"申请电话簿收录"两个选项
- **Verification**: `human-judgment`

### AC-3: 点击遮罩关闭菜单
- **Given**: 操作菜单已弹出
- **When**: 用户点击弹窗外部遮罩区域
- **Then**: 菜单关闭，页面保持在当前 tab（中间页）
- **Verification**: `human-judgment`

### AC-4: 选择发布房屋（已登录已认证）
- **Given**: 用户已登录且已实名认证
- **When**: 选择"发布房屋"选项
- **Then**: 跳转至 `/pages/house/publish` 页面
- **Verification**: `programmatic`

### AC-5: 选择发布房屋（未登录）
- **Given**: 用户未登录
- **When**: 选择"发布房屋"选项
- **Then**: 提示"请先登录"并跳转至登录页
- **Verification**: `programmatic`

### AC-6: 选择发布房屋（未实名认证）
- **Given**: 用户已登录但未实名认证
- **When**: 选择"发布房屋"选项
- **Then**: 提示"请先完成实名认证"并跳转至实名认证页
- **Verification**: `programmatic`

### AC-7: 选择申请电话簿收录（已登录）
- **Given**: 用户已登录
- **When**: 选择"申请电话簿收录"选项
- **Then**: 跳转至 `/pages/phonebook/apply` 页面
- **Verification**: `programmatic`

### AC-8: 选择申请电话簿收录（未登录）
- **Given**: 用户未登录
- **When**: 选择"申请电话簿收录"选项
- **Then**: 提示"请先登录"并跳转至登录页
- **Verification**: `programmatic`

## Open Questions
- [ ] 弹窗样式选择：底部弹窗（actionSheet）还是居中弹窗（modal）？