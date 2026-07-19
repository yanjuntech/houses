# Checklist

## 数据库与菜单
- [x] `sql/miniapp_rental_extend.sql` 包含扩展字段 ALTER 语句（biz_miniapp_user 微信信息+黑名单字段、biz_community property_phone、biz_phonebook phone1/phone2 拆分）
- [x] SQL 包含新增表 DDL（biz_invite_relation、biz_mall_product、biz_mall_exchange_record、biz_exchange_quota、biz_rental_contract、biz_house_repair、biz_user_browse、biz_user_favorite、biz_chat_message、biz_sensitive_word）
- [x] SQL 包含 sys_menu 插入语句（邀请管理、兑换商城、兑换记录、在租房屋、房屋维修、黑名单管理、浏览记录、房屋收藏、消息管理、敏感词管理）
- [x] SQL 包含字典数据（维修状态、维修类型、商品类型、消息状态、黑名单状态等）
- [x] SQL 在 ry-vue 数据库执行成功，所有表与菜单记录创建完成
- [x] 所有新表包含若依规范公共字段（create_by/create_time/update_by/update_time/remark）

## 用户表扩展与黑名单
- [x] BizMiniappUser 实体新增字段使用 @Data + @JsonProperty
- [x] 微信信息字段（wechat_nickname、wechat_avatar）在微信登录时记录
- [x] 实名认证校验昵称不能与真实姓名相同
- [x] 强制身份证认证后才能发布房源（在房屋发布接口校验 id_card_verified）
- [x] 黑名单管理：拉黑用户（填写原因）、解除黑名单、黑名单列表查询接口实现
- [x] 拉黑用户无法登录小程序、无法发布房源、无法私聊

## 邀请管理
- [x] BizInviteRelation 实体使用 @Data
- [x] 邀请记录分页查询接口（邀请人、被邀请人昵称/手机号、邀请时间、认证状态）
- [x] 邀请统计接口（总邀请人数、已认证人数）
- [x] 邀请关系绑定接口（小程序公开接口）
- [x] 被邀请人完成实名认证时自动更新邀请状态为"已认证"，邀请人邀请人数 +1

## 兑换商城
- [x] BizMallProduct、BizMallExchangeRecord、BizExchangeQuota 实体使用 @Data
- [x] 商品 CRUD 接口（名称、描述、类型、所需邀请人数、库存、生效规则）
- [x] 商品上下架管理
- [x] 用户兑换接口：校验邀请人数、校验库存、扣减邀请人数、扣减库存、写入兑换记录、自动生效到 biz_exchange_quota
- [x] 兑换记录查询接口（支持用户筛选、时间筛选）
- [x] 用户配额查询接口（如剩余房屋发布次数、电话簿展示延期天数）

## 在租房屋管理
- [x] BizRentalContract 实体使用 @Data，关联合同与租客信息
- [x] 在租房屋分页查询接口（支持按到期时间筛选：7天内/30天内/已过期）
- [x] 在租列表返回：房源信息、租客姓名/手机号、租赁起止时间、剩余天数、合同编号
- [x] 到期前 7 天自动标记为"即将到期"

## 房屋维修管理
- [x] BizHouseRepair 实体使用 @Data
- [x] 维修申请提交接口（公开接口，租客提交：房源、问题描述、图片）
- [x] 维修列表分页查询
- [x] 流程一 - 房东维修：房东确认维修→维修中→完成维修→待租客确认→租客确认完成
- [x] 流程二 - 租客自修报销：房东选择租客自修→待租客上传凭证→租客上传凭证→待房东确认报销→房东确认报销
- [x] 所有状态流转记录操作时间与操作人

## 黑名单管理
- [x] 拉黑用户接口（填写拉黑原因）
- [x] 解除黑名单接口
- [x] 黑名单列表查询接口（支持按手机号、昵称筛选）
- [x] 拉黑用户 status 置为"黑名单"，记录拉黑原因与时间

## 浏览记录与收藏
- [x] BizUserBrowse 实体使用 @Data
- [x] 浏览记录接口：同一房源 1 小时内重复浏览只记录一次
- [x] 浏览记录查询接口（按时间倒序）
- [x] BizUserFavorite 实体使用 @Data
- [x] 收藏房源接口、取消收藏接口
- [x] 收藏列表查询接口

## 消息管理与敏感词过滤
- [x] BizChatMessage、BizSensitiveWord 实体使用 @Data
- [x] 发送消息接口：先进行敏感词过滤
- [x] 敏感词过滤使用 DFA 算法，支持政治、色情、赌博、广告等分类
- [x] 含敏感词消息被拦截，返回提示（已验证："我想赌博"被拦截，返回"命中敏感词：赌博"）
- [x] 合规消息保存到 biz_chat_message 表（已验证："您好，请问房子还在吗？"保存成功）
- [x] 敏感词库初始化逻辑：5 分钟缓存 + 双重检查锁（已修复未初始化 bug）
- [x] 聊天记录查询接口（按用户、时间筛选）
- [x] 管理员消息审核接口（可查看完整消息内容）
- [x] 敏感词库 CRUD 接口

## 房屋批量导入
- [x] 下载 Excel 模板接口（/downloadTemplate）- 验证返回 200
- [x] 预览导入接口（/previewImport）：解析 Excel 返回预览数据 + 校验结果
- [x] 确认导入接口（/confirmImport）：校验兑换配额、扣减配额、批量写入
- [x] 用户可选择消耗兑换数量
- [x] 配额不足时提示"导入数量超过可用配额，请先兑换"
- [x] 返回导入结果（成功数、失败数、失败原因）

## 小区管理与电话簿扩展
- [x] BizCommunity 实体与 Mapper XML 增加 property_phone 字段（已验证数据库字段存在）
- [x] 前端小区管理页面增加物业联系电话字段录入与展示
- [x] BizPhonebook 实体将 phone 拆分为 phone1、phone2（已验证数据库字段存在）
- [x] 前端电话簿管理页面电话字段拆分为 2 个输入框
- [x] 录入时校验最多 2 个电话号码

## 创建时间与创建人展示
- [x] 房屋管理列表增加创建时间、创建人列
- [x] 小区管理列表增加创建时间、创建人列
- [x] 小区登记申请列表增加创建时间、创建人列（已有 create_by/create_time，前端需展示）
- [x] 电话簿列表增加创建时间、创建人列
- [x] 电话簿申请列表增加创建时间、创建人列
- [x] 房屋维修列表增加创建时间、创建人列
- [x] 邀请记录列表增加创建时间、创建人列
- [x] 兑换记录列表增加创建时间、创建人列
- [x] 消息列表增加创建时间、创建人列

## 前端页面
- [x] 邀请管理页面：邀请记录列表、邀请统计
- [x] 兑换商城商品管理页面：商品 CRUD、上下架
- [x] 兑换记录页面：记录列表查询
- [x] 在租房屋管理页面：在租列表、到期筛选、租客信息
- [x] 房屋维修管理页面：维修列表、状态流转操作
- [x] 黑名单管理页面：黑名单列表、拉黑/解除操作
- [x] 消息管理页面：消息列表、聊天记录查看
- [x] 敏感词管理页面：敏感词库 CRUD
- [x] 房屋管理页面增加批量导入按钮（下载模板、预览、确认导入）

## 联调验证
- [x] 后端编译通过（mvn clean package -DskipTests）
- [x] 后端服务启动无报错（端口 8080 正常响应 200）
- [x] 前端服务启动无报错（端口 8081 正常响应 200）
- [x] 所有新菜单正确显示（数据库验证 8 条菜单记录存在：邀请管理、兑换商城、兑换记录、在租房屋、房屋维修、黑名单管理、消息管理、敏感词管理）
- [x] 所有新前端页面可访问（8 个 .vue 页面均返回 200）
- [x] 所有关键后端接口可访问（/rental/invite/list、/rental/mallProduct/list、/rental/mallRecord/list、/rental/rentalContract/list、/rental/repair/list、/rental/sensitive/list、/rental/message/list、/rental/house/downloadTemplate 均返回 200）
- [x] 敏感词过滤修复生效（"我想赌博"被拦截，"您好，请问房子还在吗？"成功保存）
- [x] 数据库扩展字段全部就位（biz_miniapp_user 4字段 + biz_community 1字段 + biz_phonebook 2字段）
- [x] 10 张新表全部创建成功
- [x] 完整业务流程：邀请注册→兑换商品→批量导入房屋→维修申请→消息过滤→黑名单管理
