-- ----------------------------
-- 微信小程序房屋租赁平台业务表
-- 作者：miniapp-rental-platform spec
-- 日期：2026-07-19
-- ----------------------------

-- ----------------------------
-- 1、小程序用户表
-- ----------------------------
drop table if exists biz_miniapp_user;
create table biz_miniapp_user (
  user_id           bigint(20)      not null auto_increment    comment '用户ID',
  phone             varchar(11)     not null                   comment '手机号',
  openid            varchar(64)     default null               comment '微信openid',
  union_id          varchar(64)     default null               comment '微信unionid',
  nickname          varchar(64)     default null               comment '昵称',
  avatar            varchar(255)    default null               comment '头像URL',
  gender            char(1)         default '0'                comment '性别（0未知 1男 2女）',
  user_type         char(1)         default '0'                comment '用户类型（0普通 1房东 2中介）',
  verify_status     char(1)         default '0'                comment '实名认证状态（0未认证 1待审核 2已认证 3已拒绝）',
  real_name         varchar(50)     default null               comment '真实姓名',
  id_card           varchar(18)     default null               comment '身份证号',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  login_ip          varchar(128)    default ''                 comment '最后登录IP',
  login_date        datetime                                   comment '最后登录时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (user_id),
  unique key idx_phone (phone),
  unique key idx_openid (openid)
) engine=innodb auto_increment=1 comment = '小程序用户表';

-- ----------------------------
-- 2、小区表
-- ----------------------------
drop table if exists biz_community;
create table biz_community (
  community_id      bigint(20)      not null auto_increment    comment '小区ID',
  community_name    varchar(100)    not null                   comment '小区名称',
  province          varchar(50)     default null               comment '省',
  city              varchar(50)     default null               comment '市',
  district          varchar(50)     default null               comment '区/县',
  address           varchar(255)    default null               comment '详细地址',
  longitude         decimal(10,7)   default null               comment '经度',
  latitude          decimal(10,7)   default null               comment '纬度',
  property_company  varchar(100)    default null               comment '物业公司',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (community_id),
  key idx_name (community_name)
) engine=innodb auto_increment=1 comment = '小区表';

-- ----------------------------
-- 3、小区申请表
-- ----------------------------
drop table if exists biz_community_apply;
create table biz_community_apply (
  apply_id          bigint(20)      not null auto_increment    comment '申请ID',
  community_name    varchar(100)    not null                   comment '小区名称',
  province          varchar(50)     default null               comment '省',
  city              varchar(50)     default null               comment '市',
  district          varchar(50)     default null               comment '区/县',
  address           varchar(255)    default null               comment '详细地址',
  applicant_id      bigint(20)      not null                   comment '申请人用户ID',
  applicant_name    varchar(50)     default null               comment '申请人姓名',
  applicant_phone   varchar(11)     default null               comment '申请人手机号',
  apply_status      char(1)         default '0'                comment '审批状态（0待审批 1已通过 2已驳回）',
  approve_by        varchar(64)     default null               comment '审批人',
  approve_time      datetime                                   comment '审批时间',
  approve_remark    varchar(500)    default null               comment '审批意见/驳回原因',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (apply_id),
  key idx_status (apply_status),
  key idx_applicant (applicant_id)
) engine=innodb auto_increment=1 comment = '小区申请表';

-- ----------------------------
-- 4、房屋表
-- ----------------------------
drop table if exists biz_house;
create table biz_house (
  house_id          bigint(20)      not null auto_increment    comment '房源ID',
  title             varchar(200)    not null                   comment '房源标题',
  house_type        char(1)         not null                   comment '租赁类型（1出租 2求租 3合租 4出售）',
  community_id      bigint(20)      not null                   comment '小区ID',
  community_name    varchar(100)    default null               comment '小区名称（冗余）',
  room_count        int(2)          default 0                  comment '室',
  hall_count        int(2)          default 0                  comment '厅',
  bath_count        int(2)          default 0                  comment '卫',
  area              decimal(8,2)    default null                comment '面积(平方米)',
  floor             int(4)          default null               comment '楼层',
  total_floor       int(4)          default null               comment '总楼层',
  decoration        varchar(20)     default null               comment '装修（毛坯/简装/精装/豪装）',
  orientation       varchar(20)     default null               comment '朝向',
  price             decimal(12,2)   not null                   comment '价格（出租为月租金，出售为总价）',
  publish_user_id   bigint(20)      not null                   comment '发布者用户ID',
  publish_user_type char(1)         default '0'                comment '发布者类型（1房东 2中介）',
  images            varchar(2000)   default null               comment '图片URL列表，逗号分隔',
  description       text                                       comment '房源描述',
  status            char(1)         default '1'                comment '状态（0下架 1上架 2在租 3已售）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (house_id),
  key idx_type (house_type),
  key idx_community (community_id),
  key idx_status (status),
  key idx_publish_user (publish_user_id)
) engine=innodb auto_increment=1 comment = '房屋表';

-- ----------------------------
-- 5、电话簿表
-- ----------------------------
drop table if exists biz_phonebook;
create table biz_phonebook (
  phonebook_id      bigint(20)      not null auto_increment    comment '电话簿ID',
  merchant_name     varchar(100)    not null                   comment '商家名称',
  owner_name        varchar(50)     default null               comment '店主姓名',
  phone             varchar(20)     not null                   comment '联系电话',
  category          varchar(50)     default null               comment '商家分类（餐饮/便利店/家政/维修等）',
  address           varchar(255)    default null               comment '商家地址',
  business_license   varchar(255)   default null               comment '营业执照图片URL',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (phonebook_id),
  key idx_category (category),
  key idx_phone (phone)
) engine=innodb auto_increment=1 comment = '电话簿表';

-- ----------------------------
-- 6、电话簿申请表
-- ----------------------------
drop table if exists biz_phonebook_apply;
create table biz_phonebook_apply (
  apply_id          bigint(20)      not null auto_increment    comment '申请ID',
  merchant_name     varchar(100)    not null                   comment '商家名称',
  owner_name        varchar(50)     default null               comment '店主姓名',
  phone             varchar(20)     not null                   comment '联系电话',
  category          varchar(50)     default null               comment '商家分类',
  address           varchar(255)    default null               comment '商家地址',
  business_license   varchar(255)   default null               comment '营业执照图片URL',
  applicant_id      bigint(20)      not null                   comment '申请人用户ID',
  applicant_name    varchar(50)     default null               comment '申请人姓名',
  applicant_phone   varchar(11)     default null               comment '申请人手机号',
  apply_status      char(1)         default '0'                comment '审批状态（0待审批 1已通过 2已驳回）',
  approve_by        varchar(64)     default null               comment '审批人',
  approve_time      datetime                                   comment '审批时间',
  approve_remark    varchar(500)    default null               comment '审批意见/驳回原因',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (apply_id),
  key idx_status (apply_status),
  key idx_applicant (applicant_id)
) engine=innodb auto_increment=1 comment = '电话簿申请表';

-- ----------------------------
-- 7、电子合同表
-- ----------------------------
drop table if exists biz_contract;
create table biz_contract (
  contract_id       bigint(20)      not null auto_increment    comment '合同ID',
  contract_no       varchar(64)     not null                   comment '合同编号',
  contract_title    varchar(200)    not null                   comment '合同标题',
  house_id          bigint(20)      not null                   comment '房源ID',
  house_title       varchar(200)    default null               comment '房源标题（冗余）',
  community_name    varchar(100)    default null               comment '小区名称（冗余）',
  landlord_id       bigint(20)      not null                   comment '房东/出租方用户ID',
  landlord_name    varchar(50)     default null               comment '出租方姓名',
  landlord_phone   varchar(11)     default null               comment '出租方手机号',
  tenant_id         bigint(20)      not null                   comment '租客/承租方用户ID',
  tenant_name       varchar(50)     default null               comment '承租方姓名',
  tenant_phone      varchar(11)     default null               comment '承租方手机号',
  monthly_rent      decimal(12,2)   not null                   comment '月租金',
  deposit           decimal(12,2)   default 0                  comment '押金',
  rent_period       int(4)          default 12                 comment '租期（月）',
  start_date        date            not null                   comment '租赁开始日期',
  end_date          date            not null                   comment '租赁结束日期',
  pay_cycle         varchar(20)     default '月付'             comment '付款周期（月付/季付/半年付/年付）',
  contract_content  text                                       comment '合同条款内容',
  status            char(1)         default '0'                comment '状态（0待签署 1已签署 2已取消 3已过期）',
  sign_time         datetime                                   comment '签署完成时间',
  pdf_path          varchar(255)    default null               comment '生成的PDF文件路径',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (contract_id),
  unique key idx_contract_no (contract_no),
  key idx_house (house_id),
  key idx_landlord (landlord_id),
  key idx_tenant (tenant_id),
  key idx_status (status)
) engine=innodb auto_increment=1 comment = '电子合同表';

-- ----------------------------
-- 8、合同签名表
-- ----------------------------
drop table if exists biz_contract_signature;
create table biz_contract_signature (
  signature_id      bigint(20)      not null auto_increment    comment '签名ID',
  contract_id       bigint(20)      not null                   comment '合同ID',
  signer_id         bigint(20)      not null                   comment '签署人用户ID',
  signer_name       varchar(50)     default null               comment '签署人姓名',
  signer_role       char(1)         not null                   comment '签署角色（1出租方 2承租方）',
  signature_image   varchar(255)   not null                   comment '签名图片URL',
  sign_time         datetime        not null                   comment '签署时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  primary key (signature_id),
  key idx_contract (contract_id),
  key idx_signer (signer_id)
) engine=innodb auto_increment=1 comment = '合同签名表';

-- ----------------------------
-- 业务菜单插入（sys_menu）
-- 菜单ID约定：2000起为业务菜单
-- ----------------------------

-- 顶级菜单：业务管理（parent_id=0）
insert into sys_menu values(2000, '业务管理', 0, 5, 'biz', null, null, null, 1, 0, 'M', '0', '0', '', 'business', 'admin', sysdate(), '', null, '业务管理目录');

-- 小程序用户管理（parent=2000）
insert into sys_menu values(2001, '小程序用户', 2000, 1, 'miniapp-user', 'miniapp/user/index', null, null, 1, 0, 'C', '0', '0', 'biz:miniapp:user:list', 'user', 'admin', sysdate(), '', null, '小程序用户管理');
insert into sys_menu values(2002, '小程序用户新增', 2001, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:miniapp:user:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2003, '小程序用户修改', 2001, 2, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:miniapp:user:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2004, '小程序用户删除', 2001, 3, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:miniapp:user:remove', '#', 'admin', sysdate(), '', null, '');

-- 小区管理（parent=2000）
insert into sys_menu values(2010, '小区管理', 2000, 2, 'community', 'rental/community/index', null, null, 1, 0, 'C', '0', '0', 'biz:rental:community:list', 'tree', 'admin', sysdate(), '', null, '小区管理');
insert into sys_menu values(2011, '小区新增', 2010, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:community:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2012, '小区修改', 2010, 2, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:community:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2013, '小区删除', 2010, 3, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:community:remove', '#', 'admin', sysdate(), '', null, '');

-- 小区申请审批（parent=2000）
insert into sys_menu values(2020, '小区申请审批', 2000, 3, 'community-apply', 'rental/community/apply', null, null, 1, 0, 'C', '0', '0', 'biz:rental:communityApply:list', 'audit', 'admin', sysdate(), '', null, '小区申请审批');
insert into sys_menu values(2021, '审批通过', 2020, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:communityApply:approve', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2022, '审批驳回', 2020, 2, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:communityApply:reject', '#', 'admin', sysdate(), '', null, '');

-- 房屋管理（parent=2000）
insert into sys_menu values(2030, '房屋管理', 2000, 4, 'house', 'rental/house/index', null, null, 1, 0, 'C', '0', '0', 'biz:rental:house:list', 'build', 'admin', sysdate(), '', null, '房屋管理');
insert into sys_menu values(2031, '房屋新增', 2030, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:house:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2032, '房屋修改', 2030, 2, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:house:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2033, '房屋删除', 2030, 3, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:house:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2034, '房屋上下架', 2030, 4, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:house:changeStatus', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2035, '房屋导入', 2030, 5, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:house:import', '#', 'admin', sysdate(), '', null, '房屋批量导入（含下载模板、预览、确认导入）');

-- 电话簿管理（parent=2000）
insert into sys_menu values(2040, '电话簿管理', 2000, 5, 'phonebook', 'rental/phonebook/index', null, null, 1, 0, 'C', '0', '0', 'biz:rental:phonebook:list', 'phone', 'admin', sysdate(), '', null, '电话簿管理');
insert into sys_menu values(2041, '电话簿新增', 2040, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:phonebook:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2042, '电话簿修改', 2040, 2, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:phonebook:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2043, '电话簿删除', 2040, 3, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:phonebook:remove', '#', 'admin', sysdate(), '', null, '');

-- 电话簿申请审批（parent=2000）
insert into sys_menu values(2050, '商家申请审批', 2000, 6, 'phonebook-apply', 'rental/phonebook/apply', null, null, 1, 0, 'C', '0', '0', 'biz:rental:phonebookApply:list', 'audit', 'admin', sysdate(), '', null, '商家申请审批');
insert into sys_menu values(2051, '审批通过', 2050, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:phonebookApply:approve', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2052, '审批驳回', 2050, 2, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:phonebookApply:reject', '#', 'admin', sysdate(), '', null, '');

-- 电子合同管理（parent=2000）
insert into sys_menu values(2060, '电子合同', 2000, 7, 'contract', 'rental/contract/index', null, null, 1, 0, 'C', '0', '0', 'biz:rental:contract:list', 'documentation', 'admin', sysdate(), '', null, '电子合同管理');
insert into sys_menu values(2061, '合同新增', 2060, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:contract:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2062, '合同修改', 2060, 2, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:contract:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2063, '合同删除', 2060, 3, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:contract:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2064, '合同下载', 2060, 4, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:rental:contract:download', '#', 'admin', sysdate(), '', null, '');

-- 将所有业务菜单授权给管理员角色（role_id=1）
insert into sys_role_menu (role_id, menu_id) values
(1, 2000), (1, 2001), (1, 2002), (1, 2003), (1, 2004),
(1, 2010), (1, 2011), (1, 2012), (1, 2013),
(1, 2020), (1, 2021), (1, 2022),
(1, 2030), (1, 2031), (1, 2032), (1, 2033), (1, 2034),
(1, 2040), (1, 2041), (1, 2042), (1, 2043),
(1, 2050), (1, 2051), (1, 2052),
(1, 2060), (1, 2061), (1, 2062), (1, 2063), (1, 2064);

-- ----------------------------
-- 业务字典数据
-- ----------------------------
-- 用户类型字典
insert into sys_dict_type values(100, '小程序用户类型', 'biz_user_type', '0', 'admin', sysdate(), '', null, '小程序用户身份类型');
insert into sys_dict_data values(1001, 1, '普通用户', '0', 'biz_user_type', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1002, 2, '房东',     '1', 'biz_user_type', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1003, 3, '中介',     '2', 'biz_user_type', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');

-- 实名认证状态字典
insert into sys_dict_type values(101, '实名认证状态', 'biz_verify_status', '0', 'admin', sysdate(), '', null, '实名认证状态');
insert into sys_dict_data values(1011, 1, '未认证', '0', 'biz_verify_status', '', 'info',    'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1012, 2, '待审核', '1', 'biz_verify_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1013, 3, '已认证', '2', 'biz_verify_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1014, 4, '已拒绝', '3', 'biz_verify_status', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');

-- 房屋租赁类型字典
insert into sys_dict_type values(102, '房屋租赁类型', 'biz_house_type', '0', 'admin', sysdate(), '', null, '房屋租赁类型');
insert into sys_dict_data values(1021, 1, '出租', '1', 'biz_house_type', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1022, 2, '求租', '2', 'biz_house_type', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1023, 3, '合租', '3', 'biz_house_type', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1024, 4, '出售', '4', 'biz_house_type', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');

-- 房屋状态字典
insert into sys_dict_type values(103, '房屋状态', 'biz_house_status', '0', 'admin', sysdate(), '', null, '房屋上下架状态');
insert into sys_dict_data values(1031, 1, '下架', '0', 'biz_house_status', '', 'info',    'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1032, 2, '上架', '1', 'biz_house_status', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1033, 3, '在租', '2', 'biz_house_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1034, 4, '已售', '3', 'biz_house_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');

-- 审批状态字典
insert into sys_dict_type values(104, '审批状态', 'biz_apply_status', '0', 'admin', sysdate(), '', null, '申请审批状态');
insert into sys_dict_data values(1041, 1, '待审批', '0', 'biz_apply_status', '', 'warning', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1042, 2, '已通过', '1', 'biz_apply_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1043, 3, '已驳回', '2', 'biz_apply_status', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');

-- 合同状态字典
insert into sys_dict_type values(105, '合同状态', 'biz_contract_status', '0', 'admin', sysdate(), '', null, '电子合同状态');
insert into sys_dict_data values(1051, 1, '待签署', '0', 'biz_contract_status', '', 'warning', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1052, 2, '已签署', '1', 'biz_contract_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1053, 3, '已取消', '2', 'biz_contract_status', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1054, 4, '已过期', '3', 'biz_contract_status', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');
