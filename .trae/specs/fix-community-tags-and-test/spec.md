# 修复小区管理数据加载失败并测试所有菜单 Spec

## Why
业务管理菜单点击测试发现小区管理和房屋管理页面因数据库缺少 `tags` 字段导致数据加载失败，邀请管理页面因前端调用 `inviteStatistics()` 未传 `inviterId` 导致统计 API 参数类型不匹配警告。需要修复这些问题并重新测试全部 16 个业务管理菜单，确保所有页面可正常加载数据。

## What Changes
- 执行数据库增量脚本 `biz_dict_tags.sql`，为 `biz_community` 和 `biz_house` 表添加缺失的 `tags` 字段
- 后端 `BizInviteRelationController` 新增全局统计接口 `GET /rental/invite/statistics/total`，不依赖 inviterId 参数
- 前端 `invite.js` 新增 `inviteTotalStatistics()` API 函数，调用新的全局统计接口
- 前端 `invite/index.vue` 的 `loadTotalStatistics()` 改为调用 `inviteTotalStatistics()` 而非 `inviteStatistics()`
- 重新编译后端并重启服务
- 使用集成浏览器重新测试全部 16 个业务管理菜单，验证所有页面正常加载

## Impact
- Affected specs: `biz-menu-click-test`（测试报告需更新）, `fix-tags-region-role-ux`（前端标签选择修复依赖此数据库字段）
- Affected code:
  - 数据库: `biz_community`、`biz_house` 表结构（新增 tags 字段）
  - 后端: `BizInviteRelationController.java`（新增全局统计接口）
  - 前端: `ruoyi-ui/src/api/rental/invite.js`、`ruoyi-ui/src/views/rental/invite/index.vue`

## ADDED Requirements
### Requirement: 数据库 tags 字段补齐
系统数据库的 `biz_community` 和 `biz_house` 表 SHALL 包含 `tags` 字段（varchar(500)），以匹配实体类与 Mapper XML 中的字段定义。

#### Scenario: 小区管理数据加载成功
- **WHEN** 用户访问小区管理页面
- **THEN** 列表 API 返回 200 且响应 code=200，页面正常显示小区数据，控制台无 error

#### Scenario: 房屋管理数据加载成功
- **WHEN** 用户访问房屋管理页面
- **THEN** 列表 API 返回 200 且响应 code=200，页面正常显示房屋数据，控制台无 error

### Requirement: 邀请管理全局统计接口
系统 SHALL 提供一个不依赖 inviterId 的全局邀请统计接口，返回总邀请人数和已认证邀请人数。

#### Scenario: 页面加载时获取全局统计
- **WHEN** 用户打开邀请管理页面
- **THEN** 前端调用全局统计接口，返回 totalCount 和 certifiedCount，统计卡片正确显示，控制台无参数类型不匹配警告

## MODIFIED Requirements
### Requirement: 邀请管理页面统计展示
邀请管理页面 `loadTotalStatistics()` 方法 SHALL 调用全局统计接口 `inviteTotalStatistics()`，而非按单个邀请人统计的 `inviteStatistics(inviterId)` 接口。

## REMOVED Requirements
（无）
