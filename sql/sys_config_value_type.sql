-- 增加参数值类型字段：N数字 S字符串 T文本 D下拉
ALTER TABLE `sys_config` ADD COLUMN `config_value_type` char(1) DEFAULT 'S' COMMENT '参数值类型（N数字类型 S字符串类型 T文本类型 D下拉类型）' AFTER `config_options`;

-- 初始化所有现有参数为字符串类型
UPDATE `sys_config` SET `config_value_type` = 'S' WHERE `config_value_type` IS NULL OR `config_value_type` = '';
