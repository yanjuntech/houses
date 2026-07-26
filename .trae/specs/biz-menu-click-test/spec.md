# 业务管理菜单点击测试 - Product Requirement Document

## Overview
- **Summary**: 使用集成浏览器对若依管理系统中"业务管理"菜单下的所有子菜单进行自动化鼠标点击冒烟测试，验证每个菜单页面能否正常打开、无控制台错误、页面内容正确加载。
- **Purpose**: 快速验证业务管理模块所有页面的基本可用性，确保页面路由、组件加载、API 请求无异常，为后续功能开发提供基础质量保障。
- **Target Users**: 开发人员、测试人员

## Goals
- 自动遍历业务管理下所有 17 个一级子菜单页面
- 验证每个菜单点击后路由跳转正确
- 验证每个页面无控制台错误（error）
- 验证每个页面内容正常加载（非空白页）
- 验证每个页面的列表/查询 API 请求正常（HTTP 200）
- 生成测试报告，记录成功/失败的菜单

## Non-Goals (Out of Scope)
- 不测试按钮级别的操作（新增、删除、修改等 F 类型菜单）
- 不测试业务逻辑正确性
- 不做性能测试
- 不做跨浏览器兼容性测试
- 不测试二级下拉菜单的展开/收起动画效果

## Background & Context
- 系统基于 RuoYi Vue Admin 框架（Vue 2 + Element UI）
- 后端 Spring Boot 运行在 `http://localhost:8080`
- 前端静态服务器运行在 `http://localhost:8081`，API 通过 `/dev-api` 代理
- 业务管理菜单（menu_id=2000）下共有 17 个 C 类型子菜单（页面级菜单）
- 测试账号：admin / admin123
- 使用集成浏览器（integrated_browser MCP）进行自动化操作

## Functional Requirements
- **FR-1**: 自动登录系统（admin/admin123）
- **FR-2**: 展开"业务管理"菜单
- **FR-3**: 依次点击每个子菜单项
- **FR-4**: 对每个菜单页面进行检查（URL、页面标题、内容、控制台错误、网络请求）
- **FR-5**: 汇总生成测试结果报告
- **FR-6**: 每个失败项记录错误原因和截图

## Non-Functional Requirements
- **NFR-1**: 整个测试流程在 5 分钟内完成
- **NFR-2**: 测试报告包含每个菜单的通过/失败状态
- **NFR-3**: 失败项附带错误信息和截图路径，便于排查
- **NFR-4**: 测试脚本可重复运行，幂等性保证

## Constraints
- **Technical**: 使用集成浏览器 MCP 工具进行浏览器自动化，测试语言为 Python（通过 MCP 工具调用）
- **Business**: 测试环境数据可能为空，不验证数据内容，只验证页面可正常渲染
- **Dependencies**: 后端服务必须正常运行（端口 8080），前端服务必须正常运行（端口 8081），数据库和 Redis 必须可用

## Assumptions
- admin 账号有所有业务管理菜单的访问权限
- 验证码可以临时关闭以便自动化登录（或通过其他方式绕过）
- 所有菜单页面都遵循 RuoYi 标准页面结构（有搜索栏+表格）
- 测试期间服务稳定运行

## Acceptance Criteria

### AC-1: 自动登录成功
- **Given**: 前端和后端服务正常运行
- **When**: 打开登录页面并输入 admin/admin123 点击登录
- **Then**: 成功跳转到首页（/index），页面显示"若依管理系统"标题
- **Verification**: `programmatic`
- **Notes**: 需临时关闭验证码或找到其他方式处理

### AC-2: 业务管理菜单可展开
- **Given**: 已登录，位于首页
- **When**: 点击左侧"业务管理"菜单
- **Then**: 菜单展开，显示所有子菜单项
- **Verification**: `programmatic`

### AC-3: 所有 17 个子菜单均可点击跳转
- **Given**: 业务管理菜单已展开
- **When**: 依次点击每个子菜单项
- **Then**: URL 正确跳转到对应路由，页面标题包含对应菜单名称
- **Verification**: `programmatic`
- **Notes**: 17 个菜单：小程序用户、小区管理、小区申请审批、房屋管理、电话簿管理、商家申请审批、电子合同、轮播图管理、邀请管理、兑换商城、兑换记录、在租房屋、房屋维修、黑名单管理、消息管理、敏感词管理

### AC-4: 页面无控制台错误
- **Given**: 已跳转到某个菜单页面
- **When**: 页面加载完成后
- **Then**: 浏览器控制台无 error 级别的日志
- **Verification**: `programmatic`

### AC-5: 页面内容正常加载（非空白页）
- **Given**: 已跳转到某个菜单页面
- **When**: 页面加载完成后
- **Then**: 页面包含表格或表单元素，非空白页
- **Verification**: `programmatic`

### AC-6: 列表 API 请求返回 200
- **Given**: 进入列表页面
- **When**: 页面自动触发列表查询 API
- **Then**: 对应的列表查询 API 返回 HTTP 200，且响应 code 为 200
- **Verification**: `programmatic`
- **Notes**: 只检查 RuoYi 标准的 /list 接口模式

### AC-7: 测试报告完整
- **Given**: 所有菜单测试完成
- **When**: 汇总测试结果
- **Then**: 报告包含每个菜单的通过/失败状态、失败原因、截图路径
- **Verification**: `programmatic`

## Open Questions
- [ ] 测试完成后是否需要自动恢复验证码设置？
- [ ] 是否需要将失败页面的截图保存到指定目录？
- [ ] 报告格式偏好（Markdown / JSON / 纯文本）？
