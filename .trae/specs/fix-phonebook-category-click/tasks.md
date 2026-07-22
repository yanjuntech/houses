# Tasks

- [x] Task 1: 修复电话簿分类树初始化逻辑
  - [x] SubTask 1.1: 保留 `initCategoryTree()` 中树节点 `value: item.label`（中文标签），因数据库存储中文；添加注释说明设计意图
  - [x] SubTask 1.2: 调整 `handleNodeClick(data)` 逻辑，点击"全部"根节点时将 `queryParams.category` 置为 `undefined`；点击具体分类节点时使用中文标签
  - [x] SubTask 1.3: 搜索表单 `el-select` 的 `:value` 保持 `item.label`（中文），与树节点点击一致

- [x] Task 2: 验证数据库分类字段存储方式
  - [x] SubTask 2.1: 已确认 DB 存储中文标签（"餐饮美食"、"超市便利"）
  - [x] SubTask 2.2: 前端发送中文标签作为 `category` 参数

- [x] Task 3: 重新构建前端生产版本
  - [x] SubTask 3.1: 执行 `npm run build:prod` 重新构建 `ruoyi-ui/dist`
  - [x] SubTask 3.2: 确认 `serve.js` 静态服务器加载最新构建产物

- [x] Task 4: 浏览器模拟点击测试
  - [x] SubTask 4.1: 使用 Playwright/agent-browser 打开登录页并登录系统（admin/admin123，验证码已临时关闭）
  - [x] SubTask 4.2: 导航到电话簿管理页面 `/biz/phonebook`
  - [x] SubTask 4.3: 点击左侧分类树"餐饮美食"节点，验证表格正确显示过滤后的商家记录（应包含"老北京炸酱面馆"）
  - [x] SubTask 4.4: 点击"全部"节点，验证表格显示所有商家记录
  - [x] SubTask 4.5: 通过搜索表单下拉选择"餐饮美食"并点击搜索，验证与树点击行为一致
  - [x] SubTask 4.6: 捕获页面截图，确认无错误提示且控制台无异常

- [x] Task 5: 恢复系统配置
  - [x] SubTask 5.1: 测试完成后恢复 `sys.account.captchaEnabled` 为 `true` 并清除 Redis 缓存

# Task Dependencies
- [Task 2] 应先于 [Task 1] 完成，以确定数据库存储方式
- [Task 3] 依赖 [Task 1] 完成后的代码改动
- [Task 4] 依赖 [Task 3] 完成后的构建产物
- [Task 5] 在 [Task 4] 完成后执行
