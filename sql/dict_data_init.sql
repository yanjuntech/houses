-- ============================================================
-- 字典数据初始化 SQL
-- 说明：新增业务相关字典类型和数据
-- 注意：如果字典类型已存在则跳过，仅补充缺失数据
-- ============================================================

-- ============================================================
-- 1. 用户标签（biz_user_tag）
-- ============================================================
-- 先删除已存在的旧数据（如果有），重新插入标准数据
delete from sys_dict_data where dict_type = 'biz_user_tag';
delete from sys_dict_type where dict_type = 'biz_user_tag';

insert into sys_dict_type values(120, '用户标签', 'biz_user_tag', '0', 'admin', sysdate(), '', null, '小程序用户标签');
insert into sys_dict_data values(1201, 1, '新用户',   '0', 'biz_user_tag', '', 'info',    'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1202, 2, '活跃用户', '1', 'biz_user_tag', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1203, 3, 'VIP用户',  '2', 'biz_user_tag', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1204, 4, '黑名单',   '3', 'biz_user_tag', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');

-- ============================================================
-- 2. 房屋标签（biz_house_tag）
-- ============================================================
delete from sys_dict_data where dict_type = 'biz_house_tag';
delete from sys_dict_type where dict_type = 'biz_house_tag';

insert into sys_dict_type values(121, '房屋标签', 'biz_house_tag', '0', 'admin', sysdate(), '', null, '房屋标签列表');
insert into sys_dict_data values(1211, 1,  '南北通透',   '0',  'biz_house_tag', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1212, 2,  '干净整洁',   '1',  'biz_house_tag', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1213, 3,  '空调',       '2',  'biz_house_tag', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1214, 4,  'wifi',       '3',  'biz_house_tag', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1215, 5,  '密码锁',     '4',  'biz_house_tag', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1216, 6,  '热水器',     '5',  'biz_house_tag', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1217, 7,  '油烟机',     '6',  'biz_house_tag', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1218, 8,  '可携带宠物', '7',  'biz_house_tag', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1219, 9,  '独立卫浴',   '8',  'biz_house_tag', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(12110, 10, '阳台',      '9',  'biz_house_tag', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');

-- ============================================================
-- 3. 小区标签（biz_community_tag）
-- ============================================================
delete from sys_dict_data where dict_type = 'biz_community_tag';
delete from sys_dict_type where dict_type = 'biz_community_tag';

insert into sys_dict_type values(122, '小区标签', 'biz_community_tag', '0', 'admin', sysdate(), '', null, '小区标签列表');
insert into sys_dict_data values(1221, 1, '绿化面积大', '0', 'biz_community_tag', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1222, 2, '物业服务好', '1', 'biz_community_tag', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1223, 3, '小区清洁',   '2', 'biz_community_tag', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1224, 4, '交通便利',   '3', 'biz_community_tag', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1225, 5, '近地铁',     '4', 'biz_community_tag', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1226, 6, '学区房',     '5', 'biz_community_tag', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1227, 7, '新小区',     '6', 'biz_community_tag', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1228, 8, '配套齐全',   '7', 'biz_community_tag', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');

-- ============================================================
-- 4. 商家标签（biz_merchant_tag）
-- ============================================================
delete from sys_dict_data where dict_type = 'biz_merchant_tag';
delete from sys_dict_type where dict_type = 'biz_merchant_tag';

insert into sys_dict_type values(123, '商家标签', 'biz_merchant_tag', '0', 'admin', sysdate(), '', null, '商家标签列表');
insert into sys_dict_data values(1231, 1, '老字号',     '0', 'biz_merchant_tag', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1232, 2, '连锁品牌',   '1', 'biz_merchant_tag', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1233, 3, '口碑好',     '2', 'biz_merchant_tag', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1234, 4, '价格实惠',   '3', 'biz_merchant_tag', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1235, 5, '24小时营业', '4', 'biz_merchant_tag', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');

-- ============================================================
-- 5. 商家分类（biz_merchant_category）
-- ============================================================
delete from sys_dict_data where dict_type = 'biz_merchant_category';
delete from sys_dict_type where dict_type = 'biz_merchant_category';

insert into sys_dict_type values(124, '商家分类', 'biz_merchant_category', '0', 'admin', sysdate(), '', null, '商家分类列表');
insert into sys_dict_data values(1241, 1, '餐饮美食',   '0', 'biz_merchant_category', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1242, 2, '生活服务',   '1', 'biz_merchant_category', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1243, 3, '家居建材',   '2', 'biz_merchant_category', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1244, 4, '教育培训',   '3', 'biz_merchant_category', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1245, 5, '医疗健康',   '4', 'biz_merchant_category', '', 'danger',  'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1246, 6, '休闲娱乐',   '5', 'biz_merchant_category', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1247, 7, '交通出行',   '6', 'biz_merchant_category', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1248, 8, '购物商场',   '7', 'biz_merchant_category', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
