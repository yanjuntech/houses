-- ============================================================
-- 小区地图 API Key 配置
-- ============================================================

-- 高德地图 API Key
INSERT INTO sys_config (config_name, config_key, config_value, config_type, config_value_type, create_by, create_time, remark)
VALUES ('小区管理-高德地图API Key', 'sys.community.mapKeyAmap', '', 'N', 'S', 'admin', sysdate(), '高德地图 Web 端 JS API Key，用于小区地图选点功能');

-- 腾讯地图 API Key
INSERT INTO sys_config (config_name, config_key, config_value, config_type, config_value_type, create_by, create_time, remark)
VALUES ('小区管理-腾讯地图API Key', 'sys.community.mapKeyTencent', '', 'N', 'S', 'admin', sysdate(), '腾讯地图 Web 端 JS API Key，用于小区地图选点功能');
