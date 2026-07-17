# Checklist

## 阶段一：基础设施
- [ ] pom.xml 引入 spring-boot-starter-web、mybatis-plus、security、jwt、mysql、hutool、lombok、knife4j
- [ ] application.yml 包含数据库、JWT、文件上传、MyBatis Plus 配置
- [ ] 主启动类带 @MapperScan 注解
- [ ] 通用 Result 响应类、全局异常处理器、跨域配置就绪
- [ ] BaseEntity 包含 id/createTime/updateTime/isDeleted 字段

## 阶段二：数据库
- [ ] schema.sql 覆盖所有业务表（users、roles、permissions、user_roles、real_name_authentications、communities、buildings、units、houses、house_images、tags、house_tags、lease_contracts、payment_frequencies、payment_records、maintenance_applications、maintenance_records、maintenance_evaluations、rental_requests、flea_market_items、flea_market_images、flea_market_categories、express_orders、express_order_images、courier_profiles、express_evaluations、directory_categories、directory_entries、directory_images、directory_evaluations、notifications、private_conversations、private_messages、notices）
- [ ] lease_contracts 包含 contract_pdf_url、tenant_signature_url、landlord_signature_url、sign_time 字段
- [ ] data.sql 初始化管理员、角色、字典、示例小区/楼栋/单元/房源/标签/支付频次
- [ ] 启动时自动执行 schema + data 初始化

## 阶段三：用户与认证
- [ ] JwtUtil 实现 token 生成、解析、校验、过期判断
- [ ] JwtAuthenticationFilter 拦截请求并注入用户身份
- [ ] SecurityConfig 配置白名单：/api/auth/**、/api/houses/public/**、/api/notices/**、/h2-console/**
- [ ] AuthController 实现小程序登录、刷新 token、退出
- [ ] 实名认证提交接口校验身份证格式与图片必填
- [ ] 实名认证审核通过后 user.is_verified 自动更新

## 阶段四：房屋租赁
- [ ] 小区/楼栋/单元 CRUD 接口可用，级联查询正确
- [ ] 房源分页接口支持按户型/价格/面积/标签筛选
- [ ] 房源详情包含图片列表与标签列表
- [ ] 房源上下架接口正确更新 is_online 字段
- [ ] 房源发布后状态为"待审核"，审核通过后才在前端展示
- [ ] 求租/合租信息发布、列表、详情、我的接口齐全
- [ ] 中介批量导入 Excel 接口校验数据并返回错误明细

## 阶段五：电子合同
- [ ] 合同生成接口根据模板 + 房屋/租赁信息生成 PDF
- [ ] 签名上传接口接收 canvas 签名图片并存储
- [ ] 双方签署完成后合同状态为"已签"，最终 PDF 存档
- [ ] 签署记录留痕：时间、IP、设备信息
- [ ] 租金账单根据支付频次自动生成
- [ ] 合同到期前 7 天自动触发提醒通知

## 阶段六：维修申请
- [ ] 租户可提交维修申请（含图片）
- [ ] 房东/中介可处理维修（指派、填写处理内容、上传照片）
- [ ] 维修完成后通知租户可评价
- [ ] 租户可对维修进行评分与评论

## 阶段七：跳蚤市场
- [ ] 发布二手物品支持图片上传、分类、价格、新旧程度
- [ ] 浏览接口支持按分类/价格/新旧/关键词筛选
- [ ] "标记已售"接口正确更新状态并通知咨询用户
- [ ] "联系卖家"创建或复用私信会话
- [ ] 我的发布列表接口正确返回当前用户物品

## 阶段八：一步快递
- [ ] 下单代取接口强制校验实名认证（未认证拒绝下单）
- [ ] 下单时悬赏金额从用户余额冻结
- [ ] 代取员申请与审核接口可用
- [ ] 附近订单列表按距离排序
- [ ] 接单、上传取件照片、确认送达流程完整
- [ ] 确认收货后悬赏金额转入代取员余额
- [ ] 订单状态变更触发通知

## 阶段九：周边话簿
- [ ] 分类查询接口仅返回审核通过的商家
- [ ] 商家详情包含图片、评价列表、综合评分
- [ ] 用户登记商家接口需登录，提交后状态为待审核
- [ ] 管理员审核通过后商家在前端展示
- [ ] 商家评价接口（评分 + 评论）正确更新综合评分

## 阶段十：消息系统
- [ ] NotificationService 统一入口被各业务模块调用
- [ ] 系统通知列表、未读数、标记已读接口可用
- [ ] 私信会话列表按最近消息时间倒序
- [ ] 私信会话详情支持文本/图片消息
- [ ] 进入会话后未读数清零
- [ ] 关键业务事件触发通知（合同到期、维修状态、订单状态、审核结果）

## 阶段十一：后台管理
- [ ] 用户管理：列表、编辑、禁用、黑名单、实名审核
- [ ] 房源审核：通过/拒绝（拒绝需填写原因）
- [ ] 商家登记审核：通过/拒绝
- [ ] 代取员审核：通过/拒绝
- [ ] 数据统计：房源数、订单数、用户数、交易额，支持时间筛选
- [ ] 公告发布与管理

## 阶段十二：前端 Vue 工程
- [ ] 后台管理前端登录页 + Layout 完整
- [ ] 后台 Axios 拦截器自动附加 JWT
- [ ] 后台用户管理、房源审核、商家审核、维修管理页面可用
- [ ] 后台数据统计看板可视化展示
- [ ] 小程序首页金刚区入口齐全（租房、跳蚤、快递、话簿）
- [ ] 小程序微信登录、个人中心、实名认证流程完整
- [ ] 小程序租房模块（列表/详情/筛选/我的房源/发布）完整
- [ ] 小程序合同在线签署页（PDF 预览 + canvas 签名）
- [ ] 小程序跳蚤市场（列表/详情/发布/私信）
- [ ] 小程序一步快递（下单/我的订单/代取员端列表）
- [ ] 小程序周边话簿（分类/列表/详情/登记商家）
- [ ] 小程序消息中心（系统通知/私信会话）

## 阶段十三：测试与部署
- [ ] 核心接口单元/集成测试通过
- [ ] Knife4j 接口文档可访问
- [ ] 部署 README 包含后端启动、前端打包、小程序发布说明
- [ ] 数据库迁移脚本与初始化说明文档完整
