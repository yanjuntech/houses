-- ============================================================
-- 字典标签体系扩展
-- ============================================================

-- ----------------------------
-- 用户标签
-- ----------------------------
insert into sys_dict_type values(120, '用户标签', 'biz_user_tag', '0', 'admin', sysdate(), '', null, '小程序用户标签');
insert into sys_dict_data values(1201, 1, '新用户',   '0', 'biz_user_tag', '', 'info',    'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1202, 2, '活跃用户', '1', 'biz_user_tag', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1203, 3, '优质用户', '2', 'biz_user_tag', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1204, 4, 'VIP用户',  '3', 'biz_user_tag', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1205, 5, '黑名单',   '4', 'biz_user_tag', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 房屋标签
-- ----------------------------
insert into sys_dict_type values(121, '房屋标签', 'biz_house_tag', '0', 'admin', sysdate(), '', null, '房屋标签列表');
insert into sys_dict_data values(1211, 1,  '南北通透',   '0',  'biz_house_tag', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1212, 2,  '干净整洁',   '1',  'biz_house_tag', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1213, 3,  '空调',       '2',  'biz_house_tag', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1214, 4,  'wifi',       '3',  'biz_house_tag', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1215, 5,  '密码锁',     '4',  'biz_house_tag', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1216, 6,  '热水器',     '5',  'biz_house_tag', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1217, 7,  '油烟机',     '6',  'biz_house_tag', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1218, 8,  '可携带宠物', '7',  'biz_house_tag', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1219, 9,  '精装修',     '8',  'biz_house_tag', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(12110, 10, '近地铁',    '9',  'biz_house_tag', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 小区标签
-- ----------------------------
insert into sys_dict_type values(122, '小区标签', 'biz_community_tag', '0', 'admin', sysdate(), '', null, '小区标签列表');
insert into sys_dict_data values(1221, 1, '绿化面积大', '0', 'biz_community_tag', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1222, 2, '物业服务好', '1', 'biz_community_tag', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1223, 3, '小区清洁',   '2', 'biz_community_tag', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1224, 4, '安全性高',   '3', 'biz_community_tag', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1225, 5, '交通便利',   '4', 'biz_community_tag', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1226, 6, '学区房',     '5', 'biz_community_tag', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 商家标签
-- ----------------------------
insert into sys_dict_type values(123, '商家标签', 'biz_merchant_tag', '0', 'admin', sysdate(), '', null, '商家标签列表');
insert into sys_dict_data values(1231, 1, '口碑好',     '0', 'biz_merchant_tag', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1232, 2, '价格实惠',   '1', 'biz_merchant_tag', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1233, 3, '服务好',     '2', 'biz_merchant_tag', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1234, 4, '24小时营业', '3', 'biz_merchant_tag', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1235, 5, '连锁品牌',   '4', 'biz_merchant_tag', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');

-- ============================================================
-- 表字段扩展
-- ============================================================

-- biz_house 表增加 tags 字段
ALTER TABLE biz_house ADD COLUMN tags varchar(500) DEFAULT NULL COMMENT '标签（逗号分隔）' AFTER status;

-- biz_community 表增加 tags 字段
ALTER TABLE biz_community ADD COLUMN tags varchar(500) DEFAULT NULL COMMENT '标签（逗号分隔）' AFTER status;

-- biz_miniapp_user 表增加 tags 字段
ALTER TABLE biz_miniapp_user ADD COLUMN tags varchar(500) DEFAULT NULL COMMENT '标签（逗号分隔）' AFTER status;
