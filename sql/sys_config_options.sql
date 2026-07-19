-- ============================================================
-- 参数设置键选项配置扩展
-- ============================================================

-- sys_config 表增加 config_options 字段
ALTER TABLE sys_config ADD COLUMN config_options varchar(1000) DEFAULT NULL COMMENT '参数键选项配置（格式：名&值#名&值...）' AFTER config_value;

-- 数据清洗：填充有枚举值的参数的 config_options

-- 主框架页-默认皮肤样式名称
UPDATE sys_config SET config_options = '蓝色&skin-blue#绿色&skin-green#紫色&skin-purple#红色&skin-red#黄色&skin-yellow' WHERE config_key = 'sys.index.skinName';

-- 用户管理-账号初始密码
UPDATE sys_config SET config_options = '123456&123456#888888&888888#admin123&admin123' WHERE config_key = 'sys.user.initPassword';

-- 主框架页-侧边栏主题
UPDATE sys_config SET config_options = '深色主题&theme-dark#浅色主题&theme-light' WHERE config_key = 'sys.index.sideTheme';

-- 账号自助-验证码开关
UPDATE sys_config SET config_options = '开启&true#关闭&false' WHERE config_key = 'sys.account.captchaEnabled';

-- 账号自助-是否开启用户注册功能
UPDATE sys_config SET config_options = '开启&true#关闭&false' WHERE config_key = 'sys.account.registerUser';

-- 用户管理-初始密码修改策略
UPDATE sys_config SET config_options = '关闭&0#提醒&1' WHERE config_key = 'sys.account.initPasswordModify';

-- 用户管理-密码字符范围
UPDATE sys_config SET config_options = '任意字符&0#数字&1#英文字母&2#字母和数字&3#字母数字和特殊字符&4' WHERE config_key = 'sys.account.chrtype';
