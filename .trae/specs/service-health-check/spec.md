# 前后端服务健康检查与登录验证 - Product Requirement Document

## Overview
- **Summary**: 对房屋租赁管理系统的前后端服务进行健康检查，验证基础服务（MySQL、Redis）、后端接口、前端页面、API代理以及登录功能是否正常可用。
- **Purpose**: 确保系统在部署/修改后能够正常运行，用户可以成功登录并访问主界面。
- **Target Users**: 开发人员、测试人员、运维人员

## Goals
- 验证所有基础服务（MySQL、Redis）正常运行
- 验证后端 Spring Boot 服务正常响应
- 验证前端静态页面正常加载
- 验证 API 代理转发正常工作
- 验证登录功能完整可用

## Non-Goals (Out of Scope)
- 不测试所有业务功能的详细流程
- 不进行性能压测
- 不进行安全渗透测试
- 不修改任何代码

## Background & Context
- 项目采用 RuoYi-Vue 框架：Spring Boot + MyBatis + Vue 2 + Element UI
- 前端通过 Node.js 静态服务器 + API 代理运行在 8081 端口
- 后端 Spring Boot 运行在 8080 端口
- MySQL 运行在 3306 端口，数据库名 `ry-vue`
- Redis 运行在 6379 端口
- 登录需要验证码，测试时临时禁用验证码完成验证后恢复

## Functional Requirements
- **FR-1**: 基础服务健康检查 - MySQL、Redis 服务正常运行
- **FR-2**: 后端服务健康检查 - Spring Boot 接口正常响应
- **FR-3**: 前端服务健康检查 - 静态页面正常加载
- **FR-4**: API 代理检查 - 前端 8081 端口能正确转发 API 请求到后端 8080
- **FR-5**: 登录功能验证 - 用户能成功登录并进入主界面

## Non-Functional Requirements
- **NFR-1**: 所有服务 HTTP 响应状态码应为 200
- **NFR-2**: 页面加载后无控制台错误
- **NFR-3**: 登录流程在 5 秒内完成

## Constraints
- **Technical**: 使用现有基础设施，不新增依赖
- **Business**: 验证完成后恢复验证码配置
- **Dependencies**: MySQL、Redis、后端、前端服务均需启动

## Assumptions
- 服务均已在本地启动
- 使用 admin/admin123 测试账号
- 验证码可通过数据库配置临时关闭

## Acceptance Criteria

### AC-1: MySQL 服务正常
- **Given**: MySQL 进程在运行
- **When**: 执行 `SELECT 1` 查询
- **Then**: 返回成功结果
- **Verification**: `programmatic`

### AC-2: Redis 服务正常
- **Given**: Redis 进程在运行
- **When**: 执行 `PING` 命令
- **Then**: 返回 `PONG`
- **Verification**: `programmatic`

### AC-3: 后端接口正常
- **Given**: 后端服务在 8080 端口运行
- **When**: 请求 `/captchaImage` 接口
- **Then**: 返回 HTTP 200 且包含验证码数据
- **Verification**: `programmatic`

### AC-4: 前端页面正常
- **Given**: 前端服务在 8081 端口运行
- **When**: 访问根路径 `/`
- **Then**: 返回 HTTP 200 且 HTML 包含登录页面内容
- **Verification**: `programmatic`

### AC-5: API 代理正常
- **Given**: 前端服务和后端服务均在运行
- **When**: 通过前端 8081 端口请求 `/dev-api/captchaImage`
- **Then**: 返回 HTTP 200 且数据与直接访问后端一致
- **Verification**: `programmatic`

### AC-6: 浏览器页面正常渲染
- **Given**: 前端服务正常
- **When**: 用浏览器打开登录页面
- **Then**: 页面正确显示登录表单（账号、密码、验证码、登录按钮），无控制台错误
- **Verification**: `human-judgment`

### AC-7: 登录功能正常
- **Given**: 用户在登录页面，输入正确的账号密码
- **When**: 点击登录按钮
- **Then**: 成功登录，跳转到首页，显示侧边栏菜单
- **Verification**: `programmatic`

## Open Questions
- 无
