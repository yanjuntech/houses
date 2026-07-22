# 修复电话簿管理分类点击报错 Spec

## Why
用户在电话簿管理页面点击左侧"商家分类"树形菜单中的"餐饮美食"节点时报错。经代码审查与 API 直连测试发现：后端 `/rental/phonebook/list?category=餐饮美食` 接口本身返回正常（命中 1 条记录），说明问题出在前端代码逻辑：`initCategoryTree()` 在构建树节点时使用了不一致的标识映射（`id` 为数字字符串，`value` 却为中文标签），并且没有为节点加载分类下的商家数量。需要修复前端逻辑并通过浏览器模拟点击验证。

## What Changes
- 修复 `ruoyi-ui/src/views/rental/phonebook/index.vue` 中 `initCategoryTree()` 的 `value` 字段映射不一致问题：将树节点 `value` 统一为分类标识（与 `id` 一致使用 `item.value`），避免使用中文标签作为查询值带来的混乱
- 调整 `handleNodeClick(data)`：当点击"全部"节点时将 `queryParams.category` 置为 `undefined`；当点击具体分类节点时使用分类标识或中文标签（与后端数据库存储方式保持一致）作为查询条件
- 统一搜索表单 `el-select` 与新增/修改表单 `el-select` 的取值逻辑，确保与树节点点击保持一致
- 修复"全部"节点点击时 `data.value` 为 `undefined` 导致的潜在问题
- 通过浏览器模拟点击"餐饮美食"分类节点验证修复效果

## Impact
- Affected specs: 无
- Affected code:
  - `ruoyi-ui/src/views/rental/phonebook/index.vue`（前端电话簿主页面）
  - 后端代码不改动（API 已验证正常）

## ADDED Requirements

### Requirement: 分类树点击查询一致性
系统 SHALL 保证左侧分类树节点点击后，向后端发送的 `category` 查询参数与数据库中 `biz_phonebook.category` 字段存储的值类型一致，能够正确命中数据并返回结果。

#### Scenario: 点击具体分类节点
- **WHEN** 用户点击分类树中的"餐饮美食"节点
- **THEN** 列表查询请求 `category` 参数应与数据库存储的分类值匹配
- **AND** 表格应正确显示过滤后的商家记录（如"老北京炸酱面馆"）
- **AND** 不应弹出错误提示或控制台异常

#### Scenario: 点击"全部"节点
- **WHEN** 用户点击分类树的"全部"根节点
- **THEN** 查询请求不应携带 `category` 参数
- **AND** 表格应显示所有商家记录

#### Scenario: 分类下拉与树点击行为一致
- **WHEN** 用户通过搜索表单的"商家分类"下拉选择"餐饮美食"
- **AND** 用户通过左侧分类树点击"餐饮美食"
- **THEN** 两种方式发送的 `category` 查询参数应完全一致

## MODIFIED Requirements

### Requirement: 商家分类树初始化
`initCategoryTree()` 方法 SHALL 构建结构一致的树节点数据：
- 每个节点的 `id` 与 `value` 字段使用相同的标识类型
- 根节点"全部"的 `value` 为 `undefined`，点击时不发送 `category` 参数
- 子节点使用与 `categoryOptions` 中相同的值作为查询标识

### Requirement: 分类节点点击处理
`handleNodeClick(data)` 方法 SHALL：
- 根据点击的节点正确设置 `queryParams.category`
- 点击"全部"时将 `category` 置为 `undefined`
- 点击具体分类时使用与数据库存储一致的值（当前数据库存储中文标签）
