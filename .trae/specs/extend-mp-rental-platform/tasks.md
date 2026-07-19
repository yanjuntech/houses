# Tasks

- [x] Task 1: 数据库扩展脚本
  - [x] SubTask 1.1: 创建 `sql/miniapp_rental_extend.sql`，包含以下变更：
    - 扩展 `biz_miniapp_user` 表：增加 `wechat_nickname`、`wechat_avatar`、`id_card_verified`、`blacklist_reason`、`blacklist_time` 字段
    - 扩展 `biz_community` 表：增加 `property_phone` 字段
    - 扩展 `biz_phonebook` 表：将原 `phone` 字段拆分为 `phone1`、`phone2`
    - 新增表：`biz_invite_relation`（邀请关系）、`biz_mall_product`（商城商品）、`biz_mall_exchange_record`（兑换记录）、`biz_exchange_quota`（用户兑换配额，如房屋发布次数）、`biz_rental_contract`（在租房屋合同关联）、`biz_house_repair`（维修申请）、`biz_user_browse`（浏览记录）、`biz_user_favorite`（收藏）、`biz_chat_message`（聊天消息）、`biz_sensitive_word`（敏感词库）
  - [x] SubTask 1.2: 在 SQL 中插入新菜单（邀请管理、兑换商城、兑换记录、在租房屋、房屋维修、黑名单管理、浏览记录、房屋收藏、消息管理、敏感词管理）+ 按钮权限 + 字典数据
  - [x] SubTask 1.3: 执行 SQL 脚本到 `ry-vue` 数据库，验证表与菜单创建成功

- [x] Task 2: 用户表扩展与黑名单管理（后端）
  - [x] SubTask 2.1: 修改 `BizMiniappUser` 实体，增加微信信息与黑名单字段（使用 @Data，JSON 转实体使用 @JsonProperty，Vo 层使用 @ApiModelProperty）
  - [x] SubTask 2.2: 修改 `BizMiniappUserMapper.xml`，扩展 resultMap 与查询条件
  - [x] SubTask 2.3: 新增黑名单管理接口：拉黑用户（填写原因）、解除黑名单、黑名单列表查询
  - [x] SubTask 2.4: 修改实名认证逻辑：校验昵称不能与真实姓名相同，强制身份证认证后才能发布房源

- [x] Task 3: 邀请管理模块（后端）
  - [x] SubTask 3.1: 创建 `BizInviteRelation` 实体 + mapper + service + serviceImpl
  - [x] SubTask 3.2: 创建 `BizInviteRelationController`：邀请记录分页查询、邀请统计、邀请关系绑定接口（小程序公开接口）
  - [x] SubTask 3.3: 邀请状态自动更新逻辑：被邀请人完成实名认证时，邀请人邀请人数 +1

- [x] Task 4: 兑换商城模块（后端）
  - [x] SubTask 4.1: 创建 `BizMallProduct`、`BizMallExchangeRecord`、`BizExchangeQuota` 实体 + mapper + service + serviceImpl
  - [x] SubTask 4.2: 创建 `BizMallProductController`：商品 CRUD、上下架管理
  - [x] SubTask 4.3: 创建 `BizMallExchangeRecordController`：兑换记录查询、用户兑换接口（校验邀请人数、扣减库存、自动生效到 biz_exchange_quota）
  - [x] SubTask 4.4: 创建 `BizExchangeQuotaController`：查询用户配额、扣减配额接口（供房屋批量导入调用）

- [x] Task 5: 在租房屋管理模块（后端）
  - [x] SubTask 5.1: 创建 `BizRentalContract` 实体（关联合同 + 租客信息 + 租赁周期 + 到期时间）+ mapper + service + serviceImpl
  - [x] SubTask 5.2: 创建 `BizRentalContractController`：在租房屋分页查询（支持按到期时间筛选：7天内/30天内/已过期）、详情、到期提醒标记

- [x] Task 6: 房屋维修管理模块（后端）
  - [x] SubTask 6.1: 创建 `BizHouseRepair` 实体 + mapper + service + serviceImpl
  - [x] SubTask 6.2: 创建 `BizHouseRepairController`：维修申请提交（公开接口）、维修列表查询、状态流转接口（房东确认维修、完成维修、租客确认、租客自修上传凭证、房东确认报销）

- [x] Task 7: 浏览记录与收藏管理模块（后端）
  - [x] SubTask 7.1: 创建 `BizUserBrowse` 实体 + mapper + service + serviceImpl，实现浏览记录接口（去重逻辑：1小时内同一房源只记录一次）
  - [x] SubTask 7.2: 创建 `BizUserFavorite` 实体 + mapper + service + serviceImpl，实现收藏/取消收藏/收藏列表查询接口

- [x] Task 8: 消息管理与敏感词过滤模块（后端）
  - [x] SubTask 8.1: 创建 `BizChatMessage`、`BizSensitiveWord` 实体 + mapper + service + serviceImpl
  - [x] SubTask 8.2: 创建 `BizChatMessageController`：发送消息（敏感词过滤）、聊天记录查询、消息列表
  - [x] SubTask 8.3: 创建 `BizSensitiveWordController`：敏感词库 CRUD
  - [x] SubTask 8.4: 实现敏感词过滤工具类（DFA 算法），消息发送前过滤（已修复初始化逻辑：5 分钟缓存 + 双重检查锁）

- [x] Task 9: 房屋批量导入模块（后端）
  - [x] SubTask 9.1: 在 `BizHouseController` 增加 `/downloadTemplate` 接口（下载 Excel 模板）
  - [x] SubTask 9.2: 增加 `/previewImport` 接口：解析 Excel 返回预览数据 + 校验结果
  - [x] SubTask 9.3: 增加 `/confirmImport` 接口：校验兑换配额、扣减配额、批量写入房源
  - [x] SubTask 9.4: 使用若依 `ExcelUtil` 工具类实现导入导出

- [x] Task 10: 小区管理与电话簿扩展（后端）
  - [x] SubTask 10.1: 修改 `BizCommunity` 实体与 Mapper XML，增加 `property_phone` 字段
  - [x] SubTask 10.2: 修改 `BizPhonebook` 实体与 Mapper XML，将 `phone` 拆分为 `phone1`、`phone2`，录入时校验最多 2 个电话
  - [x] SubTask 10.3: 修改前端小区管理页面，增加物业联系电话字段
  - [x] SubTask 10.4: 修改前端电话簿管理页面，电话字段拆分为 2 个输入框

- [x] Task 11: 前端业务页面（扩展模块）
  - [x] SubTask 11.1: 创建邀请管理页面 `src/views/rental/invite/index.vue`（邀请记录列表、邀请统计）
  - [x] SubTask 11.2: 创建兑换商城管理页面 `src/views/rental/mall/product.vue`（商品 CRUD）与 `src/views/rental/mall/record.vue`（兑换记录）
  - [x] SubTask 11.3: 创建在租房屋管理页面 `src/views/rental/rental/index.vue`（在租列表、到期筛选、租客信息）
  - [x] SubTask 11.4: 创建房屋维修管理页面 `src/views/rental/repair/index.vue`（维修列表、状态流转操作）
  - [x] SubTask 11.5: 创建黑名单管理页面 `src/views/miniapp/blacklist/index.vue`（黑名单列表、拉黑/解除操作）
  - [x] SubTask 11.6: 创建消息管理页面 `src/views/rental/message/index.vue`（消息列表、聊天记录查看、敏感词过滤标记）
  - [x] SubTask 11.7: 创建敏感词管理页面 `src/views/rental/sensitive/index.vue`（敏感词库 CRUD）
  - [x] SubTask 11.8: 修改房屋管理页面 `src/views/rental/house/index.vue`，增加批量导入按钮（下载模板、预览、确认导入）、增加创建时间/创建人列
  - [x] SubTask 11.9: 修改小区管理、小区申请、电话簿、电话簿申请等已有列表页面，增加创建时间与创建人列

- [x] Task 12: 联调与验证
  - [x] SubTask 12.1: 重新编译后端（mvn clean package -DskipTests），启动后端服务（端口 8080）
  - [x] SubTask 12.2: 启动前端服务（端口 8081），验证所有新菜单显示与页面访问（8 个新页面均返回 200）
  - [x] SubTask 12.3: 验证核心业务流程：邀请注册→兑换商品→批量导入房屋→维修申请→消息过滤→黑名单管理（敏感词过滤已修复并验证：发送"我想赌博"被拦截，发送"您好..."成功保存）

# Task Dependencies
- [Task 2-10] 依赖 [Task 1] 完成数据库表和菜单初始化
- [Task 11] 依赖 [Task 2-10] 后端接口实现完成
- [Task 12] 依赖 [Task 11] 前端页面实现完成
- [Task 2-10] 之间无依赖，可并行实现（Task 4 兑换配额需在 Task 9 批量导入前完成接口定义）
- [Task 3 邀请管理] 与 [Task 4 兑换商城] 有数据依赖（邀请人数用于兑换），建议先实现 Task 3
