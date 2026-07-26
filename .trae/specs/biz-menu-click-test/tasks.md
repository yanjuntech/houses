# 业务管理菜单点击测试 - The Implementation Plan (Decomposed and Prioritized Task List)

## [x] Task 1: 准备测试环境（关闭验证码 + 确认服务运行）
- **Priority**: high
- **Depends On**: None
- **Description**:
  - 临时关闭系统验证码（修改数据库 + 清除 Redis 缓存），方便自动化登录
  - 确认后端（8080）和前端（8081）服务正常运行
  - 确认 admin 用户有业务管理所有菜单的权限
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: `curl http://localhost:8080/captchaImage` 返回 JSON 中 `captchaEnabled` 为 `false`
  - `programmatic` TR-1.2: 后端服务返回 200，前端服务返回 200
- **Notes**: 测试完成后需恢复验证码设置

## [x] Task 2: 使用集成浏览器登录系统
- **Priority**: high
- **Depends On**: Task 1
- **Description**:
  - 使用集成浏览器 MCP 的 `browser_lock` 锁定浏览器
  - 访问 `http://localhost:8081/login`
  - 输入用户名 admin、密码 admin123
  - 点击登录按钮
  - 验证跳转到 `/index` 页面
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-2.1: 登录后 URL 包含 `/index`
  - `programmatic` TR-2.2: 页面标题为"若依管理系统"
  - `programmatic` TR-2.3: 页面包含"首页"、"系统管理"等菜单项
- **Notes**: 如遇到验证码问题，回到 Task 1 确认验证码已关闭

## [x] Task 3: 展开业务管理菜单并获取子菜单列表
- **Priority**: high
- **Depends On**: Task 2
- **Description**:
  - 点击左侧"业务管理"菜单使其展开
  - 通过页面快照获取所有子菜单项的引用和名称
  - 记录 17 个子菜单的名称和对应 element ref
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-3.1: 点击后菜单展开，子菜单项可见
  - `programmatic` TR-3.2: 子菜单数量为 17 个
  - `programmatic` TR-3.3: 子菜单名称与数据库中一致（小程序用户、小区管理、小区申请审批、房屋管理、电话簿管理、商家申请审批、电子合同、轮播图管理、邀请管理、兑换商城、兑换记录、在租房屋、房屋维修、黑名单管理、消息管理、敏感词管理）
- **Notes**: 注意轮播图管理在 order_num=10，可能和邀请管理位置相邻

## [x] Task 4: 逐个点击子菜单并验证页面加载
- **Priority**: high
- **Depends On**: Task 3
- **Description**:
  - 对每个子菜单依次执行以下操作：
    1. 点击菜单项
    2. 等待页面加载（networkidle 或固定延时）
    3. 获取当前 URL，验证路由正确
    4. 获取控制台日志，检查是否有 error
    5. 获取网络请求，检查列表 API 是否返回 200
    6. 截图保存
    7. 记录测试结果（通过/失败 + 错误信息）
  - 测试 17 个菜单：小程序用户、小区管理、小区申请审批、房屋管理、电话簿管理、商家申请审批、电子合同、轮播图管理、邀请管理、兑换商城、兑换记录、在租房屋、房屋维修、黑名单管理、消息管理、敏感词管理
- **Acceptance Criteria Addressed**: AC-3, AC-4, AC-5, AC-6
- **Test Requirements**:
  - `programmatic` TR-4.1: 每个菜单点击后 URL 正确变化，不跳转到 404
  - `programmatic` TR-4.2: 每个页面无控制台 error 级别日志
  - `programmatic` TR-4.3: 每个页面包含表格或表单内容（非空白）
  - `programmatic` TR-4.4: 列表页的 /list API 返回 HTTP 200 且响应 code=200
  - `human-judgement` TR-4.5: 截图显示页面布局正常，无明显 UI 错乱
- **Notes**:
  - 某些页面可能是详情页或审批页，不一定有 /list API，需区别对待
  - 小区申请审批、商家申请审批可能是审批列表页
  - 兑换记录、在租房屋、房屋维修等应有列表

## [x] Task 5: 生成测试报告
- **Priority**: high
- **Depends On**: Task 4
- **Description**:
  - 汇总所有 17 个菜单的测试结果
  - 生成 Markdown 格式报告，包含：
    - 测试时间
    - 总菜单数、通过数、失败数
    - 每个菜单的详细结果（状态、URL、错误信息、截图路径）
    - 失败项汇总
  - 保存报告到 `.trae/documents/` 目录
- **Acceptance Criteria Addressed**: AC-7
- **Test Requirements**:
  - `programmatic` TR-5.1: 报告包含所有 17 个菜单的测试结果
  - `programmatic` TR-5.2: 报告有明确的通过/失败统计
  - `human-judgement` TR-5.3: 报告格式清晰易读

## [x] Task 6: 恢复环境（恢复验证码 + 解锁浏览器）
- **Priority**: medium
- **Depends On**: Task 5
- **Description**:
  - 恢复系统验证码设置（数据库 + Redis 缓存）
  - 调用 `browser_unlock` 释放浏览器锁
- **Acceptance Criteria Addressed**: NFR-4
- **Test Requirements**:
  - `programmatic` TR-6.1: 验证码恢复为开启状态
  - `programmatic` TR-6.2: 浏览器锁已释放
- **Notes**: 这是清理任务，确保不影响后续人工使用
