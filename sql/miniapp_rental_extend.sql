-- ----------------------------
-- 微信小程序房屋租赁平台功能扩展 SQL 脚本
-- 变更说明：
--   1. 扩展 biz_miniapp_user 表：微信信息、身份证认证、黑名单
--   2. 扩展 biz_community 表：物业联系电话
--   3. 扩展 biz_phonebook 表：电话字段拆分为 phone1、phone2
--   4. 新增 10 张业务表
--   5. 插入业务菜单与字典数据
-- 作者：extend-mp-rental-platform spec
-- 日期：2026-07-19
-- ----------------------------

-- ============================================================
-- 一、表结构扩展（ALTER）
-- ============================================================

-- 1.1 扩展小程序用户表：微信信息、身份证认证、黑名单
ALTER TABLE biz_miniapp_user
  ADD COLUMN wechat_nickname varchar(64)   DEFAULT NULL COMMENT '微信昵称' AFTER nickname,
  ADD COLUMN wechat_avatar    varchar(255)  DEFAULT NULL COMMENT '微信头像URL' AFTER wechat_nickname,
  ADD COLUMN id_card_verified char(1)       DEFAULT '0'  COMMENT '身份证是否已认证（0否 1是）' AFTER id_card,
  ADD COLUMN blacklist_reason varchar(500)  DEFAULT NULL COMMENT '拉黑原因' AFTER status,
  ADD COLUMN blacklist_time    datetime      DEFAULT NULL COMMENT '拉黑时间' AFTER blacklist_reason;

-- 用户状态字典扩展：增加黑名单状态。原 status 字段约定：0正常 1停用 2黑名单
-- 已有字典 sys_normal_disable 只有 0/1，新增字典 biz_user_blacklist_status

-- 1.2 扩展小区表：物业联系电话
ALTER TABLE biz_community
  ADD COLUMN property_phone varchar(20) DEFAULT NULL COMMENT '物业联系电话' AFTER property_company;

-- 1.3 扩展电话簿表：将 phone 拆分为 phone1、phone2
ALTER TABLE biz_phonebook
  ADD COLUMN phone1 varchar(20) DEFAULT NULL COMMENT '联系电话1' AFTER owner_name,
  ADD COLUMN phone2 varchar(20) DEFAULT NULL COMMENT '联系电话2' AFTER phone1;

-- 将原 phone 数据迁移到 phone1
UPDATE biz_phonebook SET phone1 = phone WHERE phone1 IS NULL AND phone IS NOT NULL;

-- 保留 phone 字段以兼容旧代码（不删除），新代码使用 phone1/phone2

-- ============================================================
-- 二、新增业务表
-- ============================================================

-- 2.1 邀请关系表
drop table if exists biz_invite_relation;
create table biz_invite_relation (
  relation_id       bigint(20)      not null auto_increment    comment '关系ID',
  inviter_id        bigint(20)      not null                   comment '邀请人用户ID',
  invitee_id        bigint(20)      not null                   comment '被邀请人用户ID',
  invite_code       varchar(64)     default null               comment '邀请码',
  invite_status     char(1)         default '0'                comment '邀请状态（0已注册 1已认证）',
  invite_time       datetime        not null                   comment '邀请时间',
  certified_time    datetime        default null               comment '被邀请人认证时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (relation_id),
  unique key idx_invitee (invitee_id),
  key idx_inviter (inviter_id)
) engine=innodb auto_increment=1 comment = '邀请关系表';

-- 2.2 商城商品表
drop table if exists biz_mall_product;
create table biz_mall_product (
  product_id        bigint(20)      not null auto_increment    comment '商品ID',
  product_name      varchar(100)    not null                   comment '商品名称',
  product_type     varchar(50)     not null                   comment '商品类型（HOUSE_PUBLISH_10/HOUSE_PUBLISH_20/PHONEBOOK_DELAY_10/PHONEBOOK_DELAY_30/VIP_MONTH）',
  description       varchar(500)    default null               comment '商品描述',
  required_invites  int(11)         not null default 0         comment '所需邀请人数',
  stock             int(11)         not null default 0         comment '库存数量',
  effect_value      int(11)         default 0                  comment '生效数值（如+10天的10）',
  status            char(1)         default '0'                comment '状态（0上架 1下架）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (product_id),
  key idx_type (product_type),
  key idx_status (status)
) engine=innodb auto_increment=1 comment = '商城商品表';

-- 2.3 兑换记录表
drop table if exists biz_mall_exchange_record;
create table biz_mall_exchange_record (
  record_id         bigint(20)      not null auto_increment    comment '兑换记录ID',
  user_id           bigint(20)      not null                   comment '兑换用户ID',
  user_name         varchar(50)     default null               comment '用户昵称',
  user_phone        varchar(11)     default null               comment '用户手机号',
  product_id        bigint(20)      not null                   comment '商品ID',
  product_name      varchar(100)    default null               comment '商品名称（冗余）',
  product_type     varchar(50)     default null               comment '商品类型（冗余）',
  cost_invites      int(11)         not null                   comment '消耗邀请人数',
  exchange_time     datetime        not null                   comment '兑换时间',
  effect_status     char(1)         default '0'                comment '生效状态（0已生效 1已失效）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (record_id),
  key idx_user (user_id),
  key idx_product (product_id),
  key idx_exchange_time (exchange_time)
) engine=innodb auto_increment=1 comment = '兑换记录表';

-- 2.4 用户兑换配额表（记录用户可用的配额，如剩余房屋发布次数）
drop table if exists biz_exchange_quota;
create table biz_exchange_quota (
  quota_id          bigint(20)      not null auto_increment    comment '配额ID',
  user_id           bigint(20)      not null                   comment '用户ID',
  quota_type        varchar(50)     not null                   comment '配额类型（HOUSE_PUBLISH/PHONEBOOK_DELAY/VIP）',
  total_count       int(11)         not null default 0         comment '总数量',
  used_count        int(11)         not null default 0         comment '已使用数量',
  remaining_count   int(11)         not null default 0         comment '剩余数量',
  expire_time       datetime        default null               comment '过期时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (quota_id),
  unique key idx_user_type (user_id, quota_type)
) engine=innodb auto_increment=1 comment = '用户兑换配额表';

-- 2.5 在租房屋合同表（关联房源、合同、租客信息）
drop table if exists biz_rental_contract;
create table biz_rental_contract (
  rental_id         bigint(20)      not null auto_increment    comment '在租ID',
  house_id          bigint(20)      not null                   comment '房源ID',
  house_title       varchar(200)    default null               comment '房源标题（冗余）',
  community_name    varchar(100)    default null               comment '小区名称（冗余）',
  contract_id       bigint(20)      default null               comment '关联合同ID',
  contract_no       varchar(64)     default null               comment '合同编号（冗余）',
  landlord_id       bigint(20)      not null                   comment '房东用户ID',
  landlord_name     varchar(50)     default null               comment '房东姓名',
  landlord_phone    varchar(11)     default null               comment '房东手机号',
  tenant_id         bigint(20)      not null                   comment '租客用户ID',
  tenant_name       varchar(50)     default null               comment '租客姓名',
  tenant_phone      varchar(11)     default null               comment '租客手机号',
  start_date        date            not null                   comment '租赁开始日期',
  end_date          date            not null                   comment '租赁结束日期',
  monthly_rent      decimal(12,2)   not null                   comment '月租金',
  deposit           decimal(12,2)   default 0                  comment '押金',
  rent_period       int(4)          default 12                 comment '租期（月）',
  expire_status     char(1)         default '0'                comment '到期状态（0正常 1即将到期 2已过期）',
  status            char(1)         default '0'                comment '状态（0在租 1已退租 2已到期）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (rental_id),
  key idx_house (house_id),
  key idx_contract (contract_id),
  key idx_landlord (landlord_id),
  key idx_tenant (tenant_id),
  key idx_end_date (end_date),
  key idx_status (status)
) engine=innodb auto_increment=1 comment = '在租房屋合同表';

-- 2.6 房屋维修表
drop table if exists biz_house_repair;
create table biz_house_repair (
  repair_id         bigint(20)      not null auto_increment    comment '维修ID',
  house_id          bigint(20)      not null                   comment '房源ID',
  house_title       varchar(200)    default null               comment '房源标题（冗余）',
  tenant_id         bigint(20)      not null                   comment '租客用户ID',
  tenant_name       varchar(50)     default null               comment '租客姓名',
  landlord_id       bigint(20)      not null                   comment '房东用户ID',
  landlord_name     varchar(50)     default null               comment '房东姓名',
  repair_type       char(1)         default '1'                comment '维修类型（1房东维修 2租客自修报销）',
  description       varchar(1000)   not null                   comment '问题描述',
  images            varchar(2000)   default null               comment '问题图片URL列表，逗号分隔',
  status            char(1)         default '0'                comment '状态（0待房东确认 1维修中 2待租客确认 3已完成 4待租客上传凭证 5待房东确认报销 6已报销 7已取消）',
  receipt_images    varchar(2000)   default null               comment '报销凭证图片URL列表',
  receipt_amount    decimal(12,2)   default null               comment '报销金额',
  confirm_time      datetime        default null               comment '房东确认时间',
  complete_time     datetime        default null               comment '维修完成时间',
  tenant_confirm_time datetime      default null               comment '租客确认时间',
  reimburse_time    datetime        default null               comment '报销确认时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (repair_id),
  key idx_house (house_id),
  key idx_tenant (tenant_id),
  key idx_landlord (landlord_id),
  key idx_status (status)
) engine=innodb auto_increment=1 comment = '房屋维修表';

-- 2.7 用户浏览记录表
drop table if exists biz_user_browse;
create table biz_user_browse (
  browse_id         bigint(20)      not null auto_increment    comment '浏览ID',
  user_id           bigint(20)      not null                   comment '用户ID',
  house_id          bigint(20)      not null                   comment '房源ID',
  house_title       varchar(200)    default null               comment '房源标题（冗余）',
  browse_time       datetime        not null                   comment '浏览时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  primary key (browse_id),
  key idx_user (user_id),
  key idx_house (house_id),
  key idx_browse_time (browse_time)
) engine=innodb auto_increment=1 comment = '用户浏览记录表';

-- 2.8 用户收藏表
drop table if exists biz_user_favorite;
create table biz_user_favorite (
  favorite_id       bigint(20)      not null auto_increment    comment '收藏ID',
  user_id           bigint(20)      not null                   comment '用户ID',
  house_id          bigint(20)      not null                   comment '房源ID',
  house_title       varchar(200)    default null               comment '房源标题（冗余）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (favorite_id),
  unique key idx_user_house (user_id, house_id),
  key idx_user (user_id),
  key idx_house (house_id)
) engine=innodb auto_increment=1 comment = '用户收藏表';

-- 2.9 聊天消息表
drop table if exists biz_chat_message;
create table biz_chat_message (
  message_id        bigint(20)      not null auto_increment    comment '消息ID',
  sender_id         bigint(20)      not null                   comment '发送方用户ID',
  sender_name       varchar(50)     default null               comment '发送方昵称',
  receiver_id       bigint(20)      not null                   comment '接收方用户ID',
  receiver_name     varchar(50)     default null               comment '接收方昵称',
  house_id          bigint(20)      default null               comment '关联房源ID',
  content           text            not null                   comment '消息内容',
  is_filtered       char(1)         default '0'                comment '是否被敏感词过滤（0否 1是）',
  filter_reason     varchar(500)    default null               comment '过滤原因',
  read_status       char(1)         default '0'                comment '已读状态（0未读 1已读）',
  read_time         datetime        default null               comment '已读时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (message_id),
  key idx_sender (sender_id),
  key idx_receiver (receiver_id),
  key idx_house (house_id),
  key idx_create_time (create_time)
) engine=innodb auto_increment=1 comment = '聊天消息表';

-- 2.10 敏感词库表
drop table if exists biz_sensitive_word;
create table biz_sensitive_word (
  word_id           bigint(20)      not null auto_increment    comment '敏感词ID',
  word              varchar(100)    not null                   comment '敏感词',
  category          varchar(50)     default null               comment '分类（POLITICAL/PORNOGRAPHIC/GAMBLING/ADVERTISING/OTHER）',
  status            char(1)         default '0'                comment '状态（0启用 1禁用）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (word_id),
  unique key idx_word (word),
  key idx_category (category),
  key idx_status (status)
) engine=innodb auto_increment=1 comment = '敏感词库表';

-- ============================================================
-- 三、菜单插入（sys_menu，menu_id 从 2070 起）
-- ============================================================

-- 顶级菜单已存在：业务管理（menu_id=2000）

-- 邀请管理（parent=2000）
insert into sys_menu values(2070, '邀请管理', 2000, 10, 'invite', 'rental/invite/index', null, null, 1, 0, 'C', '0', '0', 'biz:rental:invite:list', 'people', 'admin', sysdate(), '', null, '邀请管理');
insert into sys_menu values(2071, '邀请查询', 2070, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:invite:query', '#', 'admin', sysdate(), '', null, '');

-- 兑换商城（parent=2000）
insert into sys_menu values(2080, '兑换商城', 2000, 11, 'mall-product', 'rental/mall/product', null, null, 1, 0, 'C', '0', '0', 'biz:rental:mallProduct:list', 'shopping', 'admin', sysdate(), '', null, '兑换商品管理');
insert into sys_menu values(2081, '商品新增', 2080, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:mallProduct:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2082, '商品修改', 2080, 2, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:mallProduct:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2083, '商品删除', 2080, 3, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:mallProduct:remove', '#', 'admin', sysdate(), '', null, '');

-- 兑换记录（parent=2000）
insert into sys_menu values(2090, '兑换记录', 2000, 12, 'mall-record', 'rental/mall/record', null, null, 1, 0, 'C', '0', '0', 'biz:rental:mallRecord:list', 'list', 'admin', sysdate(), '', null, '兑换记录');
insert into sys_menu values(2091, '记录查询', 2090, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:mallRecord:query', '#', 'admin', sysdate(), '', null, '');

-- 在租房屋管理（parent=2000）
insert into sys_menu values(2100, '在租房屋', 2000, 13, 'rental-contract', 'rental/rental/index', null, null, 1, 0, 'C', '0', '0', 'biz:rental:rentalContract:list', 'documentation', 'admin', sysdate(), '', null, '在租房屋管理');
insert into sys_menu values(2101, '在租查询', 2100, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:rentalContract:query', '#', 'admin', sysdate(), '', null, '');

-- 房屋维修管理（parent=2000）
insert into sys_menu values(2110, '房屋维修', 2000, 14, 'repair', 'rental/repair/index', null, null, 1, 0, 'C', '0', '0', 'biz:rental:repair:list', 'tool', 'admin', sysdate(), '', null, '房屋维修管理');
insert into sys_menu values(2111, '维修查询', 2110, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:repair:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2112, '维修操作', 2110, 2, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:repair:edit', '#', 'admin', sysdate(), '', null, '');

-- 黑名单管理（parent=2000）
insert into sys_menu values(2120, '黑名单管理', 2000, 15, 'blacklist', 'miniapp/blacklist/index', null, null, 1, 0, 'C', '0', '0', 'biz:miniapp:blacklist:list', 'ban', 'admin', sysdate(), '', null, '用户黑名单管理');
insert into sys_menu values(2121, '拉黑用户', 2120, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:miniapp:blacklist:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2122, '解除黑名单', 2120, 2, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:miniapp:blacklist:remove', '#', 'admin', sysdate(), '', null, '');

-- 消息管理（parent=2000）
insert into sys_menu values(2130, '消息管理', 2000, 16, 'message', 'rental/message/index', null, null, 1, 0, 'C', '0', '0', 'biz:rental:message:list', 'message', 'admin', sysdate(), '', null, '消息管理');
insert into sys_menu values(2131, '消息查询', 2130, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:message:query', '#', 'admin', sysdate(), '', null, '');

-- 敏感词管理（parent=2000）
insert into sys_menu values(2140, '敏感词管理', 2000, 17, 'sensitive', 'rental/sensitive/index', null, null, 1, 0, 'C', '0', '0', 'biz:rental:sensitive:list', 'eye', 'admin', sysdate(), '', null, '敏感词管理');
insert into sys_menu values(2141, '敏感词新增', 2140, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:sensitive:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2142, '敏感词修改', 2140, 2, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:sensitive:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2143, '敏感词删除', 2140, 3, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:sensitive:remove', '#', 'admin', sysdate(), '', null, '');

-- 将所有新增业务菜单授权给管理员角色（role_id=1）
insert into sys_role_menu (role_id, menu_id) values
(1, 2070), (1, 2071),
(1, 2080), (1, 2081), (1, 2082), (1, 2083),
(1, 2090), (1, 2091),
(1, 2100), (1, 2101),
(1, 2110), (1, 2111), (1, 2112),
(1, 2120), (1, 2121), (1, 2122),
(1, 2130), (1, 2131),
(1, 2140), (1, 2141), (1, 2142), (1, 2143);

-- ============================================================
-- 四、字典数据
-- ============================================================

-- 用户黑名单状态字典
insert into sys_dict_type values(110, '用户黑名单状态', 'biz_user_blacklist_status', '0', 'admin', sysdate(), '', null, '用户状态（含黑名单）');
insert into sys_dict_data values(1101, 1, '正常',   '0', 'biz_user_blacklist_status', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1102, 2, '停用',   '1', 'biz_user_blacklist_status', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1103, 3, '黑名单', '2', 'biz_user_blacklist_status', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');

-- 邀请状态字典
insert into sys_dict_type values(111, '邀请状态', 'biz_invite_status', '0', 'admin', sysdate(), '', null, '邀请关系状态');
insert into sys_dict_data values(1111, 1, '已注册', '0', 'biz_invite_status', '', 'info',    'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1112, 2, '已认证', '1', 'biz_invite_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');

-- 商品类型字典
insert into sys_dict_type values(112, '商城商品类型', 'biz_product_type', '0', 'admin', sysdate(), '', null, '兑换商城商品类型');
insert into sys_dict_data values(1121, 1, '房屋发布+10天', 'HOUSE_PUBLISH_10',  'biz_product_type', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1122, 2, '房屋发布+20天', 'HOUSE_PUBLISH_20',  'biz_product_type', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1123, 3, '电话簿延期10天', 'PHONEBOOK_DELAY_10', 'biz_product_type', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1124, 4, '电话簿延期30天', 'PHONEBOOK_DELAY_30', 'biz_product_type', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1125, 5, '会员1个月',   'VIP_MONTH',          'biz_product_type', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');

-- 商品状态字典（复用 sys_normal_disable 即可）

-- 兑换生效状态字典
insert into sys_dict_type values(113, '兑换生效状态', 'biz_exchange_status', '0', 'admin', sysdate(), '', null, '兑换记录生效状态');
insert into sys_dict_data values(1131, 1, '已生效', '0', 'biz_exchange_status', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1132, 2, '已失效', '1', 'biz_exchange_status', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');

-- 在租到期状态字典
insert into sys_dict_type values(114, '在租到期状态', 'biz_rental_expire_status', '0', 'admin', sysdate(), '', null, '在租房屋到期状态');
insert into sys_dict_data values(1141, 1, '正常',     '0', 'biz_rental_expire_status', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1142, 2, '即将到期', '1', 'biz_rental_expire_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1143, 3, '已过期',   '2', 'biz_rental_expire_status', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');

-- 在租状态字典
insert into sys_dict_type values(115, '在租状态', 'biz_rental_status', '0', 'admin', sysdate(), '', null, '在租房屋状态');
insert into sys_dict_data values(1151, 1, '在租',   '0', 'biz_rental_status', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1152, 2, '已退租', '1', 'biz_rental_status', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1153, 3, '已到期', '2', 'biz_rental_status', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');

-- 维修类型字典
insert into sys_dict_type values(116, '维修类型', 'biz_repair_type', '0', 'admin', sysdate(), '', null, '房屋维修类型');
insert into sys_dict_data values(1161, 1, '房东维修',     '1', 'biz_repair_type', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1162, 2, '租客自修报销', '2', 'biz_repair_type', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');

-- 维修状态字典
insert into sys_dict_type values(117, '维修状态', 'biz_repair_status', '0', 'admin', sysdate(), '', null, '房屋维修状态');
insert into sys_dict_data values(1171, 1, '待房东确认',       '0', 'biz_repair_status', '', 'warning', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1172, 2, '维修中',           '1', 'biz_repair_status', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1173, 3, '待租客确认',       '2', 'biz_repair_status', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1174, 4, '已完成',           '3', 'biz_repair_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1175, 5, '待租客上传凭证',   '4', 'biz_repair_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1176, 6, '待房东确认报销',   '5', 'biz_repair_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1177, 7, '已报销',           '6', 'biz_repair_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1178, 8, '已取消',           '7', 'biz_repair_status', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');

-- 消息已读状态字典
insert into sys_dict_type values(118, '消息已读状态', 'biz_message_read_status', '0', 'admin', sysdate(), '', null, '消息已读状态');
insert into sys_dict_data values(1181, 1, '未读', '0', 'biz_message_read_status', '', 'warning', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1182, 2, '已读', '1', 'biz_message_read_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');

-- 敏感词分类字典
insert into sys_dict_type values(119, '敏感词分类', 'biz_sensitive_category', '0', 'admin', sysdate(), '', null, '敏感词分类');
insert into sys_dict_data values(1191, 1, '政治',     'POLITICAL',    'biz_sensitive_category', '', 'danger',  'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1192, 2, '色情',     'PORNOGRAPHIC', 'biz_sensitive_category', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1193, 3, '赌博',     'GAMBLING',     'biz_sensitive_category', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1194, 4, '广告',     'ADVERTISING',  'biz_sensitive_category', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1195, 5, '其他',     'OTHER',        'biz_sensitive_category', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');

-- 身份证认证状态字典（复用 biz_verify_status 已有，但补充说明）
-- biz_verify_status 已有：0未认证 1待审核 2已认证 3已拒绝，本次新增 id_card_verified 字段使用 0否 1是

-- 初始化一些默认敏感词
insert into biz_sensitive_word (word, category, status, create_by, create_time) values
('赌博',   'GAMBLING',     '0', 'admin', sysdate()),
('色情',   'PORNOGRAPHIC', '0', 'admin', sysdate()),
('政治敏感', 'POLITICAL',    '0', 'admin', sysdate()),
('代开发票', 'ADVERTISING',  '0', 'admin', sysdate()),
('办证',   'ADVERTISING',  '0', 'admin', sysdate());
