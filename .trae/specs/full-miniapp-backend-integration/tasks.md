# 微信小程序后端全业务对接 - The Implementation Plan

## [ ] Task 1: 房屋管理 - 小程序端公开接口
- **Priority**: high
- **Depends On**: None
- **Description**: 
  - 在 `BizHouseController` 新增 3 个 @Anonymous 公开接口
  - `GET /rental/house/list`：小程序房屋列表（仅上架状态，支持分页筛选）
  - `GET /rental/house/{houseId}`：小程序房屋详情
  - `POST /rental/house/publish`：小程序发布房屋（扣减发布配额）
- **Acceptance Criteria Addressed**: AC-1, AC-2, AC-3
- **Test Requirements**:
  - `programmatic` TR-1.1: 列表接口使用 @Anonymous，支持 pageNum/pageSize，默认过滤上架房屋
  - `programmatic` TR-1.2: 详情接口使用 @Anonymous，返回完整房屋信息
  - `programmatic` TR-1.3: 发布接口使用 @Anonymous，校验 userId，扣减发布配额
  - `programmatic` TR-1.4: 发布接口设置发布者信息和初始状态

## [ ] Task 2: 维修管理 - 小程序端公开列表接口
- **Priority**: high
- **Depends On**: None
- **Description**: 
  - 在 `BizHouseRepairController` 新增 1 个 @Anonymous 公开接口
  - `GET /rental/repair/list`：小程序维修列表（按 userId 筛选，支持分页）
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `programmatic` TR-2.1: 列表接口使用 @Anonymous，支持 userId 参数筛选
  - `programmatic` TR-2.2: 支持分页参数 pageNum/pageSize
  - `programmatic` TR-2.3: 按创建时间倒序排列

## [ ] Task 3: 邀请管理 - 小程序端公开接口
- **Priority**: high
- **Depends On**: None
- **Description**: 
  - 在 `BizInviteRelationController` 新增 2 个 @Anonymous 公开接口
  - `GET /rental/invite/statistics/{inviterId}`：邀请统计（总人数、已认证人数）
  - `GET /rental/invite/inviteList/{inviterId}`：邀请列表
- **Acceptance Criteria Addressed**: AC-5, AC-6
- **Test Requirements**:
  - `programmatic` TR-3.1: 统计接口使用 @Anonymous，返回 totalCount 和 certifiedCount
  - `programmatic` TR-3.2: 列表接口使用 @Anonymous，返回邀请人列表

## [ ] Task 4: 兑换商城 - 小程序端用户记录公开接口
- **Priority**: high
- **Depends On**: None
- **Description**: 
  - 在 `BizMallExchangeRecordController` 为现有 userRecord 接口添加 @Anonymous 注解
- **Acceptance Criteria Addressed**: AC-7
- **Test Requirements**:
  - `programmatic` TR-4.1: userRecord 接口使用 @Anonymous，按 userId 返回兑换记录列表

## [ ] Task 5: 兑换配额 - 小程序端用户配额公开接口
- **Priority**: high
- **Depends On**: None
- **Description**: 
  - 在 `BizExchangeQuotaController` 为现有 userQuota 接口添加 @Anonymous 注解
- **Acceptance Criteria Addressed**: AC-8
- **Test Requirements**:
  - `programmatic` TR-5.1: userQuota 接口使用 @Anonymous，按 userId 返回配额列表

## [ ] Task 6: 收藏管理 - 小程序端用户收藏公开接口
- **Priority**: high
- **Depends On**: None
- **Description**: 
  - 在 `BizUserFavoriteController` 为现有 userFavorite 接口添加 @Anonymous 注解
- **Acceptance Criteria Addressed**: AC-9
- **Test Requirements**:
  - `programmatic` TR-6.1: userFavorite 接口使用 @Anonymous，按 userId 返回收藏列表

## [ ] Task 7: 浏览记录 - 小程序端用户浏览公开接口
- **Priority**: high
- **Depends On**: None
- **Description**: 
  - 在 `BizUserBrowseController` 为现有 userBrowse 接口添加 @Anonymous 注解
- **Acceptance Criteria Addressed**: AC-10
- **Test Requirements**:
  - `programmatic` TR-7.1: userBrowse 接口使用 @Anonymous，按 userId 返回浏览记录列表

## [ ] Task 8: 验证全部接口
- **Priority**: high
- **Depends On**: Task 1, Task 2, Task 3, Task 4, Task 5, Task 6, Task 7
- **Description**: 
  - 验证所有 10 个公开接口的代码实现
  - 检查接口是否正确添加 @Anonymous 注解
  - 检查接口返回格式是否正确
- **Acceptance Criteria Addressed**: AC-1 至 AC-10
- **Test Requirements**:
  - `programmatic` TR-8.1: 所有新增公开接口均有 @Anonymous 注解
  - `programmatic` TR-8.2: 接口路径与前端 api.js 定义一致
  - `programmatic` TR-8.3: 接口返回格式正确（AjaxResult 或 TableDataInfo）
