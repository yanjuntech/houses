# 微信小程序后端全业务对接 - Product Requirement Document

## Overview
- **Summary**: 将后端所有业务管理模块的功能通过公开接口（@Anonymous）完整对接到微信小程序前端，确保小程序端可以调用所有后端业务能力，不存在"功能开发中"的缺失状态。
- **Purpose**: 解决小程序前端已实现页面但后端缺少对应公开接口的问题，实现全业务闭环，让用户可以在小程序端完成所有操作。
- **Target Users**: 小程序终端用户

## Goals
- 房屋管理模块：小程序端可查询房屋列表、房屋详情、发布房屋
- 维修管理模块：小程序端可查询我的维修列表
- 邀请管理模块：小程序端可查询邀请统计、邀请列表
- 兑换商城模块：小程序端可查询兑换记录
- 兑换配额模块：小程序端可查询用户配额
- 收藏管理模块：小程序端可查询我的收藏列表
- 浏览记录模块：小程序端可查询我的浏览记录

## Non-Goals (Out of Scope)
- 不修改后端管理后台（PC端）的权限控制接口
- 不新增业务模块，仅补充已有模块的小程序端公开接口
- 不修改数据库表结构
- 不修改现有已有的 @Anonymous 接口

## Background & Context
- 后端已实现 15 个业务模块的管理后台接口（房屋、维修、电话簿、小区、合同、邀请、商城、轮播图等）
- 小程序前端已实现 13 个业务模块的 API 调用（api.js）
- 部分模块只有管理后台接口（@PreAuthorize 保护），缺少小程序端公开接口（@Anonymous）
- 已有的公开接口：登录注册、轮播图、电话簿、小区、申请收录、维修申请/确认/取消、邀请绑定、聊天消息、浏览记录、收藏增删、在租房屋、商品兑换
- 缺失的公开接口：房屋列表/详情/新增、维修列表、邀请统计/列表、兑换记录、配额查询、收藏列表、浏览列表

## Functional Requirements
- **FR-1**: 房屋管理 - 小程序端公开房屋列表接口（支持分页、筛选）
- **FR-2**: 房屋管理 - 小程序端公开房屋详情接口
- **FR-3**: 房屋管理 - 小程序端公开房屋发布接口
- **FR-4**: 维修管理 - 小程序端公开维修列表接口（按用户ID筛选）
- **FR-5**: 邀请管理 - 小程序端公开邀请统计接口
- **FR-6**: 邀请管理 - 小程序端公开邀请列表接口
- **FR-7**: 兑换商城 - 小程序端公开用户兑换记录接口
- **FR-8**: 兑换配额 - 小程序端公开用户配额查询接口
- **FR-9**: 收藏管理 - 小程序端公开用户收藏列表接口
- **FR-10**: 浏览记录 - 小程序端公开用户浏览记录接口

## Non-Functional Requirements
- **NFR-1**: 所有公开接口使用 @Anonymous 注解，无需登录即可访问（但接口内部可进行用户ID校验）
- **NFR-2**: 列表接口支持分页参数（pageNum, pageSize）
- **NFR-3**: 接口返回格式与现有接口保持一致（AjaxResult / TableDataInfo）
- **NFR-4**: 数据安全性：用户相关接口必须传入 userId，只能查询自己的数据

## Constraints
- **Technical**: Spring Boot + MyBatis，使用 RuoYi 框架
- **Business**: 保持现有管理后台接口不变，仅新增小程序端公开接口
- **Dependencies**: 依赖现有 Service 层和 Mapper 层

## Assumptions
- 小程序端通过 userId 标识用户，不使用 token 鉴权（公开接口模式）
- 所有用户数据查询接口通过 userId 参数过滤
- 房屋发布需要扣减发布配额

## Acceptance Criteria

### AC-1: 房屋列表公开接口
- **Given**: 用户打开小程序房屋列表页
- **When**: 调用 `GET /rental/house/list` 并传入 pageNum、pageSize 及筛选参数
- **Then**: 返回分页房屋列表（仅返回上架状态的房屋），格式为 TableDataInfo
- **Verification**: `programmatic`

### AC-2: 房屋详情公开接口
- **Given**: 用户点击某条房屋
- **When**: 调用 `GET /rental/house/{houseId}`
- **Then**: 返回房屋详细信息
- **Verification**: `programmatic`

### AC-3: 房屋发布公开接口
- **Given**: 用户在小程序端填写房屋发布信息
- **When**: 调用 `POST /rental/house/publish` 提交
- **Then**: 扣减发布配额，创建房屋记录（状态为待审核/已上架），返回成功
- **Verification**: `programmatic`

### AC-4: 维修列表公开接口
- **Given**: 用户查看我的维修列表
- **When**: 调用 `GET /rental/repair/list?userId={userId}`
- **Then**: 返回该用户的维修申请列表（按时间倒序）
- **Verification**: `programmatic`

### AC-5: 邀请统计公开接口
- **Given**: 用户查看邀请统计
- **When**: 调用 `GET /rental/invite/statistics/{inviterId}`
- **Then**: 返回总邀请人数和已认证人数
- **Verification**: `programmatic`

### AC-6: 邀请列表公开接口
- **Given**: 用户查看邀请列表
- **When**: 调用 `GET /rental/invite/inviteList/{inviterId}`
- **Then**: 返回邀请人列表
- **Verification**: `programmatic`

### AC-7: 用户兑换记录公开接口
- **Given**: 用户查看兑换记录
- **When**: 调用 `GET /rental/mallRecord/userRecord/{userId}`
- **Then**: 返回该用户的兑换记录列表
- **Verification**: `programmatic`

### AC-8: 用户配额查询公开接口
- **Given**: 用户查看兑换配额
- **When**: 调用 `GET /rental/exchangeQuota/userQuota/{userId}`
- **Then**: 返回该用户的所有配额记录
- **Verification**: `programmatic`

### AC-9: 用户收藏列表公开接口
- **Given**: 用户查看我的收藏
- **When**: 调用 `GET /rental/favorite/userFavorite/{userId}`
- **Then**: 返回该用户的收藏列表（含房屋信息）
- **Verification**: `programmatic`

### AC-10: 用户浏览记录公开接口
- **Given**: 用户查看浏览记录
- **When**: 调用 `GET /rental/browse/userBrowse/{userId}`
- **Then**: 返回该用户的浏览记录列表
- **Verification**: `programmatic`
