# 业务管理菜单点击测试报告

## 基本信息

- **测试时间**: 2026-07-26
- **测试环境**: 本地开发环境
- **测试账号**: admin / admin123
- **前端地址**: http://localhost:8081
- **后端地址**: http://localhost:8080
- **测试范围**: 业务管理模块下所有子菜单（共 16 个）
- **测试方式**: 集成浏览器自动化点击测试

## 测试概览

| 指标 | 数量 | 占比 |
|------|------|------|
| 总菜单数 | 16 | 100% |
| 通过 | 13 | 81.25% |
| 失败 | 2 | 12.50% |
| 警告（部分异常） | 1 | 6.25% |

## 测试结果详情

### 1. 小程序用户 ✅ 通过

- **路由**: /biz/user
- **列表 API**: `GET /dev-api/miniapp/user/list` → 200 OK
- **页面状态**: 正常加载，有搜索栏和表格
- **控制台错误**: 无
- **备注**: 页面正常显示用户列表

### 2. 小区管理 ❌ 失败

- **路由**: /biz/community
- **列表 API**: `GET /dev-api/rental/community/list` → 200 OK（但返回数据库错误）
- **页面状态**: 页面结构加载，但数据查询失败
- **控制台错误**: 有
- **错误信息**:
  ```
  ### Error querying database. Cause: java.sql.SQLSyntaxErrorException: Unknown column 'tags' in 'field list'
  ### The error may exist in BizCommunityMapper.xml
  ### SQL: select community_id, community_name, ..., tags, ... from biz_community LIMIT ?
  ```
- **问题分析**: 数据库表 `biz_community` 缺少 `tags` 字段，而 Mapper XML 中查询了该字段
- **严重程度**: 高 - 页面无法正常显示数据

### 3. 小区申请审批 ✅ 通过

- **路由**: /biz/communityApply
- **列表 API**: `GET /dev-api/rental/communityApply/list` → 200 OK
- **页面状态**: 正常加载
- **控制台错误**: 无

### 4. 房屋管理 ❌ 失败

- **路由**: /biz/house
- **列表 API**: `GET /dev-api/rental/house/list` → 200 OK（但返回数据库错误）
- **页面状态**: 页面结构加载，但数据查询失败
- **控制台错误**: 有
- **错误信息**:
  ```
  ### Error querying database. Cause: java.sql.SQLSyntaxErrorException: Unknown column 'tags' in 'field list'
  ### The error may exist in BizHouseMapper.xml
  ```
- **问题分析**: 数据库表 `biz_house` 缺少 `tags` 字段，而 Mapper XML 中查询了该字段
- **严重程度**: 高 - 页面无法正常显示数据

### 5. 电话簿管理 ✅ 通过

- **路由**: /biz/phonebook
- **列表 API**: `GET /dev-api/rental/phonebook/list` → 200 OK
- **页面状态**: 正常加载
- **控制台错误**: 无

### 6. 商家申请审批 ✅ 通过

- **路由**: /biz/phonebookApply
- **列表 API**: `GET /dev-api/rental/phonebookApply/list` → 200 OK
- **页面状态**: 正常加载
- **控制台错误**: 无
- **备注**: 该菜单实际为"电话簿申请审批"，与菜单名称"商家申请审批"可能不一致

### 7. 电子合同 ✅ 通过

- **路由**: /biz/contract
- **列表 API**: `GET /dev-api/rental/contract/list` → 200 OK
- **页面状态**: 正常加载
- **控制台错误**: 无

### 8. 邀请管理 ⚠️ 警告（部分异常）

- **路由**: /biz/invite
- **列表 API**: `GET /dev-api/rental/invite/list` → 200 OK
- **统计 API**: `GET /dev-api/rental/invite/statistics/undefined` → 200 OK（参数异常）
- **页面状态**: 页面正常加载，列表数据正常显示
- **控制台错误**: 有参数类型不匹配警告
- **错误信息**:
  ```
  请求参数类型不匹配，参数[inviterId]要求类型为：'java.lang.Long'，但输入值为：'undefined'
  ```
- **问题分析**: 前端调用统计 API 时，`inviterId` 参数未正确传递，值为 `undefined`
- **严重程度**: 中 - 列表功能正常，但统计功能可能异常
- **影响范围**: 页面顶部统计数据可能无法正确显示

### 9. 轮播图管理 ✅ 通过

- **路由**: /biz/banner
- **列表 API**: `GET /dev-api/rental/banner/list` → 200 OK
- **页面状态**: 正常加载
- **控制台错误**: 无
- **备注**: 轮播图预览图片使用 picsum.photos 外部资源，当前环境无法访问，不影响功能

### 10. 兑换商城 ✅ 通过

- **路由**: /biz/mallProduct
- **列表 API**: `GET /dev-api/rental/mallProduct/list` → 200 OK
- **页面状态**: 正常加载
- **控制台错误**: 无

### 11. 兑换记录 ✅ 通过

- **路由**: /biz/mallRecord
- **列表 API**: `GET /dev-api/rental/mallRecord/list` → 200 OK
- **页面状态**: 正常加载
- **控制台错误**: 无

### 12. 在租房屋 ✅ 通过

- **路由**: /biz/rentalContract
- **列表 API**: `GET /dev-api/rental/rentalContract/list` → 200 OK
- **页面状态**: 正常加载
- **控制台错误**: 无

### 13. 房屋维修 ✅ 通过

- **路由**: /biz/repair
- **列表 API**: `GET /dev-api/rental/repair/list` → 200 OK
- **页面状态**: 正常加载，有测试数据
- **控制台错误**: 无

### 14. 黑名单管理 ✅ 通过

- **路由**: /biz/blacklist
- **列表 API**: `GET /dev-api/miniapp/user/blacklist/list` → 200 OK
- **页面状态**: 正常加载（暂无数据）
- **控制台错误**: 无

### 15. 消息管理 ✅ 通过

- **路由**: /biz/message
- **列表 API**: `GET /dev-api/rental/message/list` → 200 OK
- **页面状态**: 正常加载（暂无数据）
- **控制台错误**: 无

### 16. 敏感词管理 ✅ 通过

- **路由**: /biz/sensitive
- **列表 API**: `GET /dev-api/rental/sensitive/list` → 200 OK
- **页面状态**: 正常加载，有 5 条测试数据（赌博、色情、政治敏感、代开发票、办证）
- **控制台错误**: 无

## 失败项汇总

### ❌ 失败项 1：小区管理 - 数据库字段缺失

| 项目 | 内容 |
|------|------|
| 菜单名称 | 小区管理 |
| 问题类型 | 数据库 Schema 错误 |
| 错误信息 | Unknown column 'tags' in 'field list' |
| 影响文件 | BizCommunityMapper.xml |
| 影响表 | biz_community |
| 修复建议 | 在 biz_community 表中添加 tags 字段，或从 Mapper 查询中移除 tags 字段 |

### ❌ 失败项 2：房屋管理 - 数据库字段缺失

| 项目 | 内容 |
|------|------|
| 菜单名称 | 房屋管理 |
| 问题类型 | 数据库 Schema 错误 |
| 错误信息 | Unknown column 'tags' in 'field list' |
| 影响文件 | BizHouseMapper.xml |
| 影响表 | biz_house |
| 修复建议 | 在 biz_house 表中添加 tags 字段，或从 Mapper 查询中移除 tags 字段 |

### ⚠️ 警告项：邀请管理 - API 参数异常

| 项目 | 内容 |
|------|------|
| 菜单名称 | 邀请管理 |
| 问题类型 | 前端参数传递错误 |
| 错误信息 | inviterId 参数为 undefined，类型应为 Long |
| 影响 API | /rental/invite/statistics/{inviterId} |
| 修复建议 | 检查前端代码，确保 inviterId 参数正确传递；或在后端对 undefined 参数做容错处理 |

## 测试结论

### 总体评价

业务管理模块 **16 个** 子菜单中，**13 个** 完全正常通过（81.25%），**2 个** 存在数据库字段缺失导致的数据加载失败（12.50%），**1 个** 存在部分 API 参数异常（6.25%）。

### 主要问题

1. **数据库 Schema 不一致**：`biz_community` 和 `biz_house` 表缺少 `tags` 字段，但 Mapper XML 中引用了该字段，导致小区管理和房屋管理页面无法正常加载数据。

2. **前端参数传递问题**：邀请管理页面的统计 API 调用时 `inviterId` 参数为 `undefined`，可能导致统计数据无法正确显示。

### 建议修复优先级

1. **P0（高优先级）**：修复小区管理和房屋管理的数据库字段缺失问题
2. **P1（中优先级）**：修复邀请管理的 inviterId 参数传递问题
3. **P2（低优先级）**：确认"商家申请审批"菜单名称与实际功能是否匹配

---

*报告生成时间：2026-07-26*
*测试工具：集成浏览器自动化测试*
