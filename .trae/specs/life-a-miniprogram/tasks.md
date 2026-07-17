# Tasks

## 阶段一：基础设施

- [ ] Task 1: 创建后端项目骨架（Spring Boot 3 + MyBatis Plus + Spring Security + JWT）
  - [ ] SubTask 1.1: 创建 pom.xml，引入 spring-boot-starter-web、mybatis-plus、security、jwt、mysql、hutool、lombok、knife4j 依赖
  - [ ] SubTask 1.2: 创建 application.yml（数据库、JWT、文件上传、MyBatis Plus 配置）
  - [ ] SubTask 1.3: 创建主启动类 HouseRentalApplication + @MapperScan
  - [ ] SubTask 1.4: 创建通用 Result 响应类、全局异常处理器、跨域配置
  - [ ] SubTask 1.5: 创建 BaseEntity 基类（id、createTime、updateTime、isDeleted）

- [ ] Task 2: 创建数据库 schema 与种子数据
  - [ ] SubTask 2.1: 编写 schema.sql，覆盖所有业务表（见 spec 数据库设计）
  - [ ] SubTask 2.2: 编写 data.sql，初始化管理员账号、角色、字典数据、示例小区/楼栋/单元/房源/标签/支付频次
  - [ ] SubTask 2.3: 配置 spring.sql.init 在启动时自动执行 schema+data

## 阶段二：用户与认证

- [ ] Task 3: 实现用户认证与权限模块
  - [ ] SubTask 3.1: 创建 User、Role、Permission、UserRole、RolePermission、RealNameAuthentication 实体类
  - [ ] SubTask 3.2: 创建对应 Mapper 接口
  - [ ] SubTask 3.3: 实现 JwtUtil 工具类（生成、解析、校验 token）
  - [ ] SubTask 3.4: 实现 JwtAuthenticationFilter（请求拦截、token 校验、注入用户身份）
  - [ ] SubTask 3.5: 实现 SecurityConfig（白名单路径：登录、公开房源、公告、H2 控制台）
  - [ ] SubTask 3.6: 实现 AuthController（小程序登录、刷新 token、退出）
  - [ ] SubTask 3.7: 实现 UserService（小程序登录、注册、修改密码、修改资料）
  - [ ] SubTask 3.8: 实现实名认证提交与查询接口（RealNameAuthService）

## 阶段三：房屋租赁核心

- [ ] Task 4: 实现小区/楼栋/单元管理
  - [ ] SubTask 4.1: 创建 Community、Building、Unit 实体 + Mapper + Service + Controller
  - [ ] SubTask 4.2: 提供按小区获取楼栋、按楼栋获取单元的级联接口

- [ ] Task 5: 实现房屋管理模块
  - [ ] SubTask 5.1: 创建 House、HouseImage、Tag、HouseTag 实体 + Mapper
  - [ ] SubTask 5.2: 实现 HouseService（分页、详情含图片与标签、新增、编辑、上下架、删除）
  - [ ] SubTask 5.3: 实现 HouseController（公开分页/详情；登录后我的房源 CRUD）
  - [ ] SubTask 5.4: 实现 HouseImageController（图片上传接口，存储到本地或 MinIO）
  - [ ] SubTask 5.5: 实现 TagController（标签列表、按类型筛选）
  - [ ] SubTask 5.6: 实现房源审核接口（管理员审核通过/拒绝）

- [ ] Task 6: 实现求租/合租信息模块
  - [ ] SubTask 6.1: 创建 RentalRequest 实体 + Mapper + Service + Controller
  - [ ] SubTask 6.2: 提供发布、我的求租、列表筛选、详情接口

- [ ] Task 7: 实现租赁合同与电子签署
  - [ ] SubTask 7.1: 创建 LeaseContract 实体 + Mapper + Service + Controller（扩展合同 PDF 与签名字段）
  - [ ] SubTask 7.2: 实现 PaymentFrequency、PaymentRecord 实体 + Mapper + Service
  - [ ] SubTask 7.3: 实现合同生成接口（基于模板 + 房屋/租赁信息生成 PDF，含双方签名占位）
  - [ ] SubTask 7.4: 实现签名上传接口（接收 canvas 签名图片，存储 + 叠加到 PDF）
  - [ ] SubTask 7.5: 实现双方签署完成后状态流转 + 最终 PDF 存档 + 留痕（IP/时间/设备）
  - [ ] SubTask 7.6: 实现租金账单自动生成（根据支付频次）+ 到期提醒通知

- [ ] Task 8: 实现维修申请与评价
  - [ ] SubTask 8.1: 创建 MaintenanceApplication、MaintenanceRecord、MaintenanceEvaluation 实体 + Mapper
  - [ ] SubTask 8.2: 实现租户提交维修申请接口（含图片上传）
  - [ ] SubTask 8.3: 实现房东/中介处理维修接口（指派、处理完成）
  - [ ] SubTask 8.4: 实现维修评价接口

## 阶段四：跳蚤市场

- [ ] Task 9: 实现跳蚤市场模块
  - [ ] SubTask 9.1: 创建 FleaMarketItem、FleaMarketImage、FleaMarketCategory 实体 + Mapper
  - [ ] SubTask 9.2: 实现发布、编辑、上下架、删除接口（需登录）
  - [ ] SubTask 9.3: 实现公开浏览接口（分页、按分类/价格/新旧程度筛选、关键词搜索）
  - [ ] SubTask 9.4: 实现"标记已售"接口 + 通知已有咨询用户
  - [ ] SubTask 9.5: 实现我的发布列表接口

## 阶段五：一步快递

- [ ] Task 10: 实现代取快递模块
  - [ ] SubTask 10.1: 创建 ExpressOrder、ExpressOrderImage、CourierProfile（代取员资料）、ExpressEvaluation 实体 + Mapper
  - [ ] SubTask 10.2: 实现代取员申请与审核接口（实名 + 信誉评级）
  - [ ] SubTask 10.3: 实现下单接口（强制校验实名认证，冻结悬赏金额）
  - [ ] SubTask 10.4: 实现附近订单列表接口（按距离排序，待接订单）
  - [ ] SubTask 10.5: 实现接单、上传取件照片、确认送达接口
  - [ ] SubTask 10.6: 实现确认收货与评价接口（释放悬赏金额至代取员余额）
  - [ ] SubTask 10.7: 实现订单状态变更通知

## 阶段六：周边话簿

- [ ] Task 11: 实现周边话簿模块
  - [ ] SubTask 11.1: 创建 DirectoryCategory、DirectoryEntry、DirectoryImage、DirectoryEvaluation 实体 + Mapper
  - [ ] SubTask 11.2: 实现分类查询接口（公开，仅返回已审核通过的商家）
  - [ ] SubTask 11.3: 实现商家详情接口（含图片、评价列表、综合评分）
  - [ ] SubTask 11.4: 实现用户登记商家接口（需登录，提交后状态为待审核）
  - [ ] SubTask 11.5: 实现商家评价接口（评分 + 评论）
  - [ ] SubTask 11.6: 实现管理员审核商家登记接口

## 阶段七：消息系统

- [ ] Task 12: 实现消息系统
  - [ ] SubTask 12.1: 创建 Notification、PrivateConversation、PrivateMessage 实体 + Mapper
  - [ ] SubTask 12.2: 实现 NotificationService（统一发送系统通知入口，业务模块调用）
  - [ ] SubTask 12.3: 实现系统通知列表、未读数、标记已读接口
  - [ ] SubTask 12.4: 实现私信会话列表、会话详情、发送消息（文本/图片）接口
  - [ ] SubTask 12.5: 实现进入会话清未读 + 接收未读消息提醒
  - [ ] SubTask 12.6: 关键业务事件接入通知（合同到期、维修状态、订单状态、审核结果）

## 阶段八：后台管理

- [ ] Task 13: 实现后台管理 API
  - [ ] SubTask 13.1: 实现 AdminUserController（用户列表、编辑、禁用、黑名单、实名审核）
  - [ ] SubTask 13.2: 实现 AdminHouseController（房源审核、违规下架）
  - [ ] SubTask 13.3: 实现 AdminDirectoryController（商家登记审核）
  - [ ] SubTask 13.4: 实现 AdminExpressController（代取员审核、订单监控）
  - [ ] SubTask 13.5: 实现 AdminStatsController（数据统计：房源数、订单数、用户数、交易额）
  - [ ] SubTask 13.6: 实现 AdminNoticeController（公告发布）

## 阶段九：前端 Vue 工程

- [ ] Task 14: 创建后台管理前端（Vue3 + Element Plus）
  - [ ] SubTask 14.1: 使用 Vite 创建项目，配置路由、Pinia、Axios 拦截器（自动附加 JWT）
  - [ ] SubTask 14.2: 实现登录页 + Layout（侧边菜单、顶部用户信息）
  - [ ] SubTask 14.3: 实现用户管理、房源审核、商家审核、维修管理页面
  - [ ] SubTask 14.4: 实现数据统计看板
  - [ ] SubTask 14.5: 实现公告管理、系统配置页面

- [ ] Task 15: 创建小程序前端（uni-app + Vue3 + uView）
  - [ ] SubTask 15.1: 初始化 uni-app 项目，配置 manifest.json、pages.json
  - [ ] SubTask 15.2: 实现微信登录、个人中心、实名认证页面
  - [ ] SubTask 15.3: 实现首页（金刚区入口：租房、跳蚤、快递、话簿）+ 公告
  - [ ] SubTask 15.4: 实现租房模块（房源列表/详情/筛选/我的房源/发布）
  - [ ] SubTask 15.5: 实现合同在线签署页（PDF 预览 + canvas 签名）
  - [ ] SubTask 15.6: 实现维修申请与评价
  - [ ] SubTask 15.7: 实现跳蚤市场（列表/详情/发布/私信）
  - [ ] SubTask 15.8: 实现一步快递（下单/我的订单/代取员端列表）
  - [ ] SubTask 15.9: 实现周边话簿（分类/列表/详情/登记商家）
  - [ ] SubTask 15.10: 实现消息中心（系统通知/私信会话）

## 阶段十：测试与部署

- [ ] Task 16: 集成测试与文档
  - [ ] SubTask 16.1: 编写核心接口的单元/集成测试（登录、房源、合同、订单、消息）
  - [ ] SubTask 16.2: 集成 Knife4j 接口文档，导出 OpenAPI
  - [ ] SubTask 16.3: 编写部署 README（后端启动、前端打包、小程序发布）
  - [ ] SubTask 16.4: 编写数据库迁移脚本与初始化说明

# Task Dependencies
- Task 1 → Task 2（先有项目骨架才能放 schema/data）
- Task 3 → Tasks 4-12（所有业务模块依赖认证）
- Task 4 → Task 5（房源依赖小区/楼栋/单元）
- Task 5 → Task 7（合同依赖房源）
- Task 5 → Task 6（求租信息可选参考房源）
- Task 7 → Task 8（维修依赖合同）
- Task 12 → Tasks 7-11（业务事件通知依赖消息系统）
- Task 13 → 依赖所有业务模块完成
- Task 14 → 依赖后端 API 完成（可并行开发 Mock）
- Task 15 → 依赖后端 API 完成（可并行开发 Mock）
- Task 16 → 依赖前后端完成
