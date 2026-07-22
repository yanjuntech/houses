# 前后端服务健康检查与登录验证 - The Implementation Plan

## [x] Task 1: 基础服务健康检查
- **Priority**: high
- **Depends On**: None
- **Description**:
  - 检查 MySQL 服务是否运行，执行 `SELECT 1` 验证
  - 检查 Redis 服务是否运行，执行 `PING` 验证
- **Acceptance Criteria Addressed**: AC-1, AC-2
- **Test Requirements**:
  - `programmatic` TR-1.1: MySQL `SELECT 1` 返回成功
  - `programmatic` TR-1.2: Redis `PING` 返回 `PONG`
- **Status**: ✅ 已完成

## [x] Task 2: 后端服务健康检查
- **Priority**: high
- **Depends On**: Task 1
- **Description**:
  - 检查后端 Spring Boot 服务是否在 8080 端口运行
  - 请求 `/captchaImage` 接口验证响应
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-2.1: 后端 8080 端口 `/captchaImage` 返回 HTTP 200
  - `programmatic` TR-2.2: 响应包含验证码图片数据或 captchaEnabled 字段
- **Status**: ✅ 已完成

## [x] Task 3: 前端服务健康检查
- **Priority**: high
- **Depends On**: None
- **Description**:
  - 检查前端静态服务器是否在 8081 端口运行
  - 访问根路径验证 HTML 页面返回
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `programmatic` TR-3.1: 前端 8081 端口 `/` 返回 HTTP 200
  - `programmatic` TR-3.2: 返回内容包含 `若依管理系统` 标题
- **Status**: ✅ 已完成

## [x] Task 4: API 代理验证
- **Priority**: high
- **Depends On**: Task 2, Task 3
- **Description**:
  - 通过前端 8081 端口的 `/dev-api/captchaImage` 验证代理转发
  - 确认响应数据与直接访问后端一致
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `programmatic` TR-4.1: `/dev-api/captchaImage` 返回 HTTP 200
  - `programmatic` TR-4.2: 响应结构与后端直接返回一致
- **Status**: ✅ 已完成

## [x] Task 5: 浏览器页面渲染验证
- **Priority**: high
- **Depends On**: Task 3, Task 4
- **Description**:
  - 使用内置浏览器打开登录页面
  - 检查页面元素是否正常渲染
  - 检查控制台是否有错误
- **Acceptance Criteria Addressed**: AC-6
- **Test Requirements**:
  - `human-judgement` TR-5.1: 登录表单包含账号输入框、密码输入框、验证码、登录按钮
  - `human-judgement` TR-5.2: 控制台无 JavaScript 错误
- **Status**: ✅ 已完成

## [x] Task 6: 登录功能验证
- **Priority**: high
- **Depends On**: Task 5
- **Description**:
  - 临时关闭验证码（数据库配置 + Redis 缓存清除）
  - 在浏览器中输入账号 admin / 密码 admin123
  - 点击登录按钮，验证是否跳转到首页
  - 验证成功后恢复验证码配置
- **Acceptance Criteria Addressed**: AC-7
- **Test Requirements**:
  - `programmatic` TR-6.1: 登录后 URL 跳转到 `/index`
  - `programmatic` TR-6.2: 页面显示侧边栏菜单（首页、系统管理、业务管理等）
  - `programmatic` TR-6.3: 验证码配置恢复为开启状态
- **Status**: ✅ 已完成
