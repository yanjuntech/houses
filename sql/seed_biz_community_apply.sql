-- ============================================
-- 小区登记申请种子数据（15 条，apply_status 全部 '0' 未审批）
-- ============================================
DELETE FROM biz_community_apply;

INSERT INTO biz_community_apply
(community_name, province, city, district, address, applicant_id, applicant_name, applicant_phone, apply_status, create_by, create_time, update_by, update_time, remark)
VALUES
('阳光花园小区',     '北京市', '北京市', '朝阳区', '北京市朝阳区建国路88号',           1,  '张伟',  '13800138001', '0', 'admin', NOW() - INTERVAL 30 DAY, 'admin', NOW() - INTERVAL 30 DAY, '小区环境优美，配套齐全'),
('翠湖天地',         '上海市', '上海市', '徐汇区', '上海市徐汇区漕溪北路666号',         2,  '王芳',  '13800138002', '0', 'admin', NOW() - INTERVAL 29 DAY, 'admin', NOW() - INTERVAL 29 DAY, '高端住宅社区'),
('万科城市花园',     '广东省', '广州市', '天河区', '广州市天河区珠江新城花城大道33号',   3,  '李强',  '13800138003', '0', 'admin', NOW() - INTERVAL 28 DAY, 'admin', NOW() - INTERVAL 28 DAY, NULL),
('保利香槟花园',     '广东省', '深圳市', '南山区', '深圳市南山区科技园南区',             4,  '刘洋',  '13800138004', '0', 'admin', NOW() - INTERVAL 27 DAY, 'admin', NOW() - INTERVAL 27 DAY, '社区绿化率高'),
('中海锦城',         '四川省', '成都市', '武侯区', '成都市武侯区天府大道北段',           5,  '陈静',  '13800138005', '0', 'admin', NOW() - INTERVAL 26 DAY, 'admin', NOW() - INTERVAL 26 DAY, NULL),
('龙湖时代天街',     '浙江省', '杭州市', '西湖区', '杭州市西湖区文三路555号',            6,  '杨明',  '13800138006', '0', 'admin', NOW() - INTERVAL 25 DAY, 'admin', NOW() - INTERVAL 25 DAY, NULL),
('金地名峰',         '江苏省', '南京市', '鼓楼区', '南京市鼓楼区中山北路200号',          7,  '赵丽',  '13800138007', '0', 'admin', NOW() - INTERVAL 24 DAY, 'admin', NOW() - INTERVAL 24 DAY, '配套设施完善'),
('绿城桂花苑',       '湖北省', '武汉市', '武昌区', '武汉市武昌区中南路99号',             8,  '孙涛',  '13800138008', '0', 'admin', NOW() - INTERVAL 23 DAY, 'admin', NOW() - INTERVAL 23 DAY, NULL),
('融创文旅城',       '陕西省', '西安市', '雁塔区', '西安市雁塔区科技路50号',             9,  '周倩',  '13800138009', '0', 'admin', NOW() - INTERVAL 22 DAY, 'admin', NOW() - INTERVAL 22 DAY, NULL),
('华润橡树湾',       '山东省', '青岛市', '市南区', '青岛市市南区香港中路18号',           10, '吴磊',  '13800138010', '0', 'admin', NOW() - INTERVAL 21 DAY, 'admin', NOW() - INTERVAL 21 DAY, NULL),
('碧桂园天汇',       '福建省', '厦门市', '思明区', '厦门市思明区湖滨南路100号',          1,  '郑华',  '13800138011', '0', 'admin', NOW() - INTERVAL 20 DAY, 'admin', NOW() - INTERVAL 20 DAY, NULL),
('招商海德园',       '天津市', '天津市', '和平区', '天津市和平区南京路189号',            2,  '冯丽',  '13800138012', '0', 'admin', NOW() - INTERVAL 19 DAY, 'admin', NOW() - INTERVAL 19 DAY, '海景房社区'),
('世茂滨江花园',     '重庆市', '重庆市', '渝中区', '重庆市渝中区解放碑民权路60号',       3,  '陈勇',  '13800138013', '0', 'admin', NOW() - INTERVAL 18 DAY, 'admin', NOW() - INTERVAL 18 DAY, NULL),
('恒大名都',         '辽宁省', '大连市', '中山区', '大连市中山区人民路26号',             4,  '褚红',  '13800138014', '0', 'admin', NOW() - INTERVAL 17 DAY, 'admin', NOW() - INTERVAL 17 DAY, NULL),
('富力桃园',         '云南省', '昆明市', '盘龙区', '昆明市盘龙区北京路432号',            5,  '卫国',  '13800138015', '0', 'admin', NOW() - INTERVAL 16 DAY, 'admin', NOW() - INTERVAL 16 DAY, NULL);
