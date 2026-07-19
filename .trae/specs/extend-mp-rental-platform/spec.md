# 小程序房屋租赁平台功能扩展 Spec

## Why
基于已上线的微信小程序房屋租赁平台，用户反馈需要增强社交裂变（邀请奖励）、用户粘性（兑换商城）、租赁闭环（在租管理、维修管理）、风控（黑名单、实名认证）、交互（消息、收藏、浏览记录）以及运营效率（批量导入、创建时间展示）。本次扩展在已有若依框架与业务模块基础上新增能力，形成完整的小程序生态。

## What Changes
- **扩展小程序用户表**：增加微信信息（openid/unionid 头像昵称）、身份证实名认证（强制）、黑名单管理（status + 黑名单原因）
- **新增邀请管理**：记录用户邀请关系、邀请人数统计、邀请奖励触发
- **新增兑换商城**：商品维护（虚拟商品如"房屋发布次数+10天"、"电话簿展示延期10天"）、库存管理、兑换记录
- **新增在租房屋管理**：租客信息、租赁周期、到期提醒、关联合同
- **新增房屋维修管理**：两种流程（房东维修 + 租客自修报销），状态流转，房东确认
- **新增黑名单管理**：拉黑/解除黑名单、拉黑用户禁止登录与发布
- **新增浏览记录管理**：记录用户浏览房源历史
- **新增房屋收藏管理**：用户收藏/取消收藏房源
- **新增微信小程序消息管理**：用户与房东私聊、敏感词过滤、消息审核
- **房屋管理批量导入**：Excel 模板下载、预览确认、消耗兑换数量校验
- **小区管理增加物业联系电话**
- **电话簿单商家最多 2 个电话**
- **所有列表查询增加创建时间与创建人展示**

## Impact
- Affected specs: [miniapp-rental-platform](.trae/specs/miniapp-rental-platform/spec.md)、[build-mp-rental-platform](.trae/specs/build-mp-rental-platform/spec.md)
- Affected code:
  - 数据库：扩展 `biz_miniapp_user`、`biz_community`、`biz_phonebook`；新增 `biz_invite_relation`、`biz_mall_product`、`biz_mall_exchange_record`、`biz_rental_contract`（在租管理）、`biz_house_repair`、`biz_user_browse`、`biz_user_favorite`、`biz_chat_message`、`biz_exchange_quota`（兑换配额）
  - 后端：`ruoyi-system` 新增 9 组 domain/mapper/service；`biz_miniapp_user` 扩展字段
  - 前端：`ruoyi-ui` 新增 8 个业务页面，修改已有列表页增加创建时间/创建人列
  - SQL 脚本：`sql/miniapp_rental_extend.sql`

## ADDED Requirements

### Requirement: 小程序用户微信信息与实名认证
系统 SHALL 在小程序用户表中记录完整的微信信息（openid、unionid、微信昵称、微信头像）用于微信登录与用户身份识别。所有用户 MUST 使用身份证实名登记，昵称非用户真实姓名。

#### Scenario: 微信信息记录
- **WHEN** 用户使用微信授权登录
- **THEN** 系统记录 openid、unionid、微信昵称、微信头像到 `biz_miniapp_user` 表

#### Scenario: 强制实名认证
- **WHEN** 用户首次登录后
- **THEN** 必须填写真实姓名 + 身份证号完成实名认证才能发布房源、收藏、私聊等
- **WHEN** 昵称与真实姓名相同
- **THEN** 系统拒绝，提示"昵称不能与真实姓名相同"

### Requirement: 邀请管理
系统 SHALL 提供新人邀请管理，小程序用户可邀请新人注册，系统记录邀请关系。

#### Scenario: 用户邀请注册
- **WHEN** 用户 A 分享邀请链接/二维码，用户 B 通过该链接注册
- **THEN** 系统在 `biz_invite_relation` 表记录邀请人 user_id、被邀请人 user_id、邀请时间、邀请状态（已注册/已认证）
- **AND WHEN** 被邀请人完成实名认证
- **THEN** 邀请状态置为"已认证"，邀请人邀请人数 +1

#### Scenario: 邀请统计查询
- **WHEN** 管理员或用户查询邀请记录
- **THEN** 返回邀请列表（被邀请人昵称、手机号、注册时间、认证状态），支持分页

### Requirement: 兑换商城管理
系统 SHALL 提供兑换商城，用户通过邀请人数兑换对应商品。商品为虚拟物品（如"房屋发布次数+10天"、"电话簿展示延期10天"），可动态维护商品内容与库存。

#### Scenario: 商品维护
- **WHEN** 管理员维护商品（名称、描述、类型、所需邀请人数、库存数量、生效规则）
- **THEN** 商品实时生效，小程序端同步展示
- **商品类型**：`HOUSE_PUBLISH_10`（房屋发布+10天）、`HOUSE_PUBLISH_20`（房屋发布+20天）、`PHONEBOOK_DELAY_10`（电话簿展示延期10天）、`PHONEBOOK_DELAY_30`（电话簿展示延期30天）、`VIP_MONTH`（会员1个月）等

#### Scenario: 用户兑换商品
- **WHEN** 用户使用邀请人数兑换某商品
- **THEN** 系统校验邀请人数是否充足、库存是否 > 0
- **AND WHEN** 校验通过
- **THEN** 扣减邀请人数、扣减库存、写入兑换记录、自动生效（如增加房屋发布配额、延长电话簿展示时长）
- **AND WHEN** 库存或邀请人数不足
- **THEN** 返回失败提示

#### Scenario: 兑换记录查询
- **WHEN** 管理员或用户查询兑换记录
- **THEN** 返回兑换商品、兑换时间、消耗邀请人数、生效状态

### Requirement: 在租房屋管理
系统 SHALL 提供在租房屋管理，查看租客信息、租赁周期、到期提醒、关联合同。

#### Scenario: 在租房屋列表
- **WHEN** 管理员查询在租房屋
- **THEN** 返回房源信息、租客姓名、租客手机号、租赁起止时间、剩余天数、关联合同编号
- **支持筛选**：按到期时间（7天内/30天内/已过期）、按小区、按房东

#### Scenario: 到期提醒
- **WHEN** 租赁合同到期前 7 天
- **THEN** 系统自动标记为"即将到期"，在列表中高亮显示

### Requirement: 房屋维修管理
系统 SHALL 提供房屋维修管理，支持两种维修流程。

#### Scenario: 流程一 - 房东维修
- **WHEN** 租客提交维修申请（房源、问题描述、图片）
- **THEN** 状态为"待房东确认"
- **WHEN** 房东确认维修
- **THEN** 状态为"维修中"
- **WHEN** 房东完成维修
- **THEN** 状态为"待租客确认"
- **WHEN** 租客确认完成
- **THEN** 状态为"已完成"

#### Scenario: 流程二 - 租客自修报销
- **WHEN** 租客提交维修申请并选择"租客自修报销"
- **THEN** 状态为"待房东选择"
- **WHEN** 房东选择"租客自行维修报销"
- **THEN** 状态为"待租客上传凭证"
- **WHEN** 租客上传维修凭证（发票/收据图片、金额）
- **THEN** 状态为"待房东确认报销"
- **WHEN** 房东确认报销
- **THEN** 状态为"已报销"，记录报销金额

### Requirement: 用户黑名单管理
系统 SHALL 提供用户黑名单管理，将不遵守规则的用户拉入黑名单。

#### Scenario: 拉黑用户
- **WHEN** 管理员将用户加入黑名单（填写拉黑原因）
- **THEN** 用户 status 置为"黑名单"，记录拉黑原因、拉黑时间
- **AND** 该用户无法登录小程序、无法发布房源、无法私聊

#### Scenario: 解除黑名单
- **WHEN** 管理员解除黑名单
- **THEN** 用户 status 恢复为"正常"，清除拉黑原因

### Requirement: 浏览记录管理
系统 SHALL 记录用户浏览房源的历史。

#### Scenario: 记录浏览
- **WHEN** 用户在小程序查看房源详情
- **THEN** 系统记录到 `biz_user_browse` 表（用户ID、房源ID、浏览时间）
- **同一房源 1 小时内重复浏览只记录一次**

#### Scenario: 浏览记录查询
- **WHEN** 用户查询自己的浏览记录
- **THEN** 返回浏览过的房源列表（按时间倒序）

### Requirement: 房屋收藏管理
系统 SHALL 提供用户房源收藏功能。

#### Scenario: 收藏房源
- **WHEN** 用户点击收藏某房源
- **THEN** 系统记录到 `biz_user_favorite` 表，返回成功

#### Scenario: 取消收藏
- **WHEN** 用户取消收藏
- **THEN** 删除收藏记录

#### Scenario: 收藏列表查询
- **WHEN** 用户查询收藏列表
- **THEN** 返回收藏的房源列表

### Requirement: 微信小程序消息管理
系统 SHALL 提供用户与房东私聊功能，消息内容必须经过敏感词过滤。

#### Scenario: 发送消息
- **WHEN** 用户向房东发送消息
- **THEN** 系统先进行敏感词过滤（政治、色情、赌博、广告等）
- **AND WHEN** 消息含敏感词
- **THEN** 拦截消息，返回"消息包含敏感内容，请修改后发送"
- **AND WHEN** 消息合规
- **THEN** 保存消息到 `biz_chat_message` 表，推送给接收方

#### Scenario: 消息查询
- **WHEN** 用户查询与某房东的聊天记录
- **THEN** 返回消息列表（发送方、接收方、内容、时间、已读状态）

#### Scenario: 管理员消息审核
- **WHEN** 管理员查看消息列表
- **THEN** 支持按用户、时间筛选，可查看完整消息内容

### Requirement: 房屋管理批量导入
系统 SHALL 提供房屋批量导入功能，导入数量受兑换配额限制。

#### Scenario: 下载导入模板
- **WHEN** 用户下载 Excel 模板
- **THEN** 返回包含字段（标题、小区、户型、面积、价格、类型等）的标准模板

#### Scenario: 上传并预览
- **WHEN** 用户上传 Excel 文件
- **THEN** 系统解析数据返回预览界面（显示每条记录、校验结果、总数量）
- **AND WHEN** 用户选择消耗兑换数量
- **THEN** 系统校验兑换配额是否足够
- **AND WHEN** 配额不足
- **THEN** 提示"导入数量超过可用配额，请先兑换"

#### Scenario: 确认导入
- **WHEN** 用户确认导入
- **THEN** 扣减兑换配额、写入房源数据、返回导入结果（成功数、失败数、失败原因）

## MODIFIED Requirements

### Requirement: 小区管理增加物业联系电话
在 `biz_community` 表增加 `property_phone` 字段，小区维护页面增加该字段录入与展示。

### Requirement: 电话簿单商家最多 2 个电话
`biz_phonebook` 表电话字段拆分为 `phone1`、`phone2`，录入时最多填写 2 个，展示时合并显示。

### Requirement: 所有业务列表显示创建时间与创建人
以下列表查询结果 MUST 返回 `create_by` 和 `create_time` 字段，前端表格 MUST 增加对应列：
- 房屋管理列表
- 小区管理列表
- 小区登记申请列表
- 电话簿列表
- 电话簿申请列表
- 电子合同列表（已有，无需修改）
- 房屋维修列表
- 邀请记录列表
- 兑换记录列表
- 消息列表

### Requirement: 小程序用户表扩展
`biz_miniapp_user` 表增加字段：
- `wechat_nickname`（微信昵称）
- `wechat_avatar`（微信头像）
- `id_card_verified`（身份证是否已认证，0否1是）
- `blacklist_reason`（拉黑原因）
- `blacklist_time`（拉黑时间）

## REMOVED Requirements
（无移除）
