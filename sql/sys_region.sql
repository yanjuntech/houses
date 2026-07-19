-- ----------------------------
-- 行政区划表
-- ----------------------------
DROP TABLE IF EXISTS sys_region;
CREATE TABLE sys_region (
  region_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '区划ID',
  parent_id bigint(20) DEFAULT 0 COMMENT '父级ID',
  region_code varchar(20) NOT NULL COMMENT '区划编码',
  region_name varchar(100) NOT NULL COMMENT '区划名称',
  region_level tinyint(1) NOT NULL COMMENT '层级（1省 2市 3区县）',
  community_register_switch char(1) DEFAULT '0' COMMENT '小区登记开关（0开启 1关闭）',
  sort int(4) DEFAULT 0 COMMENT '显示顺序',
  status char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  create_by varchar(64) DEFAULT '' COMMENT '创建者',
  create_time datetime COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (region_id),
  KEY idx_parent_id (parent_id),
  KEY idx_region_code (region_code)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='行政区划表';

-- ----------------------------
-- 初始化数据 - 省级
-- ----------------------------
INSERT INTO sys_region (region_id, parent_id, region_code, region_name, region_level, community_register_switch, sort, status, create_by, create_time, remark) VALUES
(1, 0, '370000', '山东省', 1, '0', 1, '0', 'admin', NOW(), '山东省'),
(2, 0, '110000', '北京市', 1, '0', 2, '0', 'admin', NOW(), '北京市'),
(3, 0, '310000', '上海市', 1, '0', 3, '0', 'admin', NOW(), '上海市'),
(4, 0, '440000', '广东省', 1, '0', 4, '0', 'admin', NOW(), '广东省');

-- ----------------------------
-- 初始化数据 - 市级
-- ----------------------------
INSERT INTO sys_region (region_id, parent_id, region_code, region_name, region_level, community_register_switch, sort, status, create_by, create_time, remark) VALUES
(101, 1, '371500', '聊城市', 2, '0', 1, '0', 'admin', NOW(), '聊城市'),
(102, 1, '370100', '济南市', 2, '0', 2, '0', 'admin', NOW(), '济南市'),
(103, 1, '370200', '青岛市', 2, '0', 3, '0', 'admin', NOW(), '青岛市'),
(201, 2, '110100', '市辖区', 2, '0', 1, '0', 'admin', NOW(), '市辖区'),
(301, 3, '310100', '市辖区', 2, '0', 1, '0', 'admin', NOW(), '市辖区'),
(401, 4, '440100', '广州市', 2, '0', 1, '0', 'admin', NOW(), '广州市'),
(402, 4, '440300', '深圳市', 2, '0', 2, '0', 'admin', NOW(), '深圳市');

-- ----------------------------
-- 初始化数据 - 区县级
-- ----------------------------
INSERT INTO sys_region (region_id, parent_id, region_code, region_name, region_level, community_register_switch, sort, status, create_by, create_time, remark) VALUES
(1001, 101, '371521', '阳谷县', 3, '0', 1, '0', 'admin', NOW(), '阳谷县'),
(1002, 101, '371502', '东昌府区', 3, '0', 2, '0', 'admin', NOW(), '东昌府区'),
(1003, 101, '371522', '莘县', 3, '0', 3, '0', 'admin', NOW(), '莘县'),
(1004, 101, '371523', '茌平区', 3, '0', 4, '0', 'admin', NOW(), '茌平区'),
(1005, 101, '371524', '东阿县', 3, '0', 5, '0', 'admin', NOW(), '东阿县'),
(1006, 101, '371525', '冠县', 3, '0', 6, '0', 'admin', NOW(), '冠县'),
(1007, 101, '371526', '高唐县', 3, '0', 7, '0', 'admin', NOW(), '高唐县'),
(1008, 101, '371571', '聊城经济技术开发区', 3, '0', 8, '0', 'admin', NOW(), '聊城经济技术开发区'),
(1101, 102, '370102', '历下区', 3, '0', 1, '0', 'admin', NOW(), '历下区'),
(1102, 102, '370103', '市中区', 3, '0', 2, '0', 'admin', NOW(), '市中区'),
(1201, 103, '370202', '市南区', 3, '0', 1, '0', 'admin', NOW(), '市南区'),
(1202, 103, '370203', '市北区', 3, '0', 2, '0', 'admin', NOW(), '市北区'),
(2001, 201, '110101', '东城区', 3, '0', 1, '0', 'admin', NOW(), '东城区'),
(2002, 201, '110102', '西城区', 3, '0', 2, '0', 'admin', NOW(), '西城区'),
(2003, 201, '110105', '朝阳区', 3, '0', 3, '0', 'admin', NOW(), '朝阳区'),
(3001, 301, '310101', '黄浦区', 3, '0', 1, '0', 'admin', NOW(), '黄浦区'),
(3002, 301, '310104', '徐汇区', 3, '0', 2, '0', 'admin', NOW(), '徐汇区'),
(4001, 401, '440103', '荔湾区', 3, '0', 1, '0', 'admin', NOW(), '荔湾区'),
(4002, 401, '440104', '越秀区', 3, '0', 2, '0', 'admin', NOW(), '越秀区'),
(4101, 402, '440303', '罗湖区', 3, '0', 1, '0', 'admin', NOW(), '罗湖区'),
(4102, 402, '440304', '福田区', 3, '0', 2, '0', 'admin', NOW(), '福田区');
