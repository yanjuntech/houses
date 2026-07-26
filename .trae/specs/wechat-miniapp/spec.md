# 微信小程序项目 - 产品需求文档

## Overview
- **Summary**: 创建一个微信小程序项目文件夹，包含完整的小程序基础架构和配置，支持后续业务功能开发。
- **Purpose**: 为团队提供一个标准化的微信小程序开发环境，包含必要的配置文件、基础页面和组件结构。
- **Target Users**: 前端开发工程师、小程序开发者

## Goals
- 创建完整的微信小程序项目目录结构
- 配置必要的小程序核心文件（app.js, app.json, app.wxss）
- 提供基础页面模板（首页、个人中心）
- 配置项目构建和开发工具

## Non-Goals (Out of Scope)
- 实现具体业务功能
- 集成后端 API
- 配置微信小程序上线发布流程

## Background & Context
- 当前工作目录已有一个若依（RuoYi）后端管理系统项目
- 需要在独立文件夹中创建微信小程序，与现有后端系统解耦
- 微信小程序使用原生框架开发，便于快速迭代和维护

## Functional Requirements
- **FR-1**: 创建微信小程序项目根目录及核心配置文件
- **FR-2**: 配置 app.json 全局配置（页面路由、窗口样式、tabBar）
- **FR-3**: 创建基础页面结构（首页、个人中心）
- **FR-4**: 添加必要的工具函数和公共组件

## Non-Functional Requirements
- **NFR-1**: 项目结构清晰，符合微信小程序开发规范
- **NFR-2**: 代码风格统一，便于团队协作
- **NFR-3**: 基础配置完整，可直接在微信开发者工具中打开运行

## Constraints
- **Technical**: 使用微信小程序原生框架，不引入额外前端框架
- **Business**: 项目需与现有若依后端系统保持独立
- **Dependencies**: 依赖微信开发者工具进行开发和调试

## Assumptions
- 开发者已安装微信开发者工具
- 项目后续会接入现有后端 API 接口

## Acceptance Criteria

### AC-1: 项目目录结构完整
- **Given**: 项目尚未创建
- **When**: 执行项目初始化
- **Then**: 创建包含 pages、components、utils、images 等目录的完整结构
- **Verification**: `programmatic`
- **Notes**: 目录结构需符合微信小程序规范

### AC-2: 核心配置文件存在且有效
- **Given**: 项目目录已创建
- **When**: 检查配置文件
- **Then**: app.js、app.json、app.wxss、project.config.json 文件均存在且格式正确
- **Verification**: `programmatic`
- **Notes**: project.config.json 需配置正确的 appid 和编译选项

### AC-3: 基础页面可正常访问
- **Given**: 项目已初始化完成
- **When**: 在微信开发者工具中打开项目
- **Then**: 首页和个人中心页面可正常显示，tabBar 切换功能正常
- **Verification**: `human-judgment`

### AC-4: 工具函数和公共组件可用
- **Given**: 项目已初始化完成
- **When**: 调用工具函数或使用公共组件
- **Then**: 工具函数返回预期结果，公共组件正常渲染
- **Verification**: `programmatic`

## Open Questions
- [ ] 是否需要配置特定的小程序 appid？
- [ ] 是否需要集成第三方 UI 组件库？
- [ ] 是否需要添加构建脚本（如 gulp、webpack）？