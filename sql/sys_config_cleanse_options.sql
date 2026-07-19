-- ============================================================
-- sys_config 参数键选项清洗脚本
-- 1. 根据已知参数 remark 推断枚举值，填充 config_options
-- 2. 根据参数特性设置 config_value_type
--   - D = 下拉类型（枚举选项）
--   - N = 数字类型
--   - S = 字符串类型（默认兜底）
-- ============================================================

-- 1. 主框架页-默认皮肤样式名称
-- remark 中包含"蓝色 skin-blue / 绿色 skin-green ..."等枚举信息，填充为下拉类型
UPDATE `sys_config`
SET `config_options`  = '蓝色&skin-blue#绿色&skin-green#紫色&skin-purple#红色&skin-red#黄色&skin-yellow',
    `config_value_type` = 'D'
WHERE `config_key` = 'sys.index.skinName';

-- 2. 账号自助-是否开启用户注册功能
-- 布尔型参数，转换为 是/否 下拉选项
UPDATE `sys_config`
SET `config_options`  = '是&true#否&false',
    `config_value_type` = 'D'
WHERE `config_key` = 'sys.account.registerUser';

-- 3. 账号自助-是否开启验证码功能
-- 布尔型参数，转换为 是/否 下拉选项
UPDATE `sys_config`
SET `config_options`  = '是&true#否&false',
    `config_value_type` = 'D'
WHERE `config_key` = 'sys.account.captchaEnabled';

-- 4. 账号自助-是否开启忘记密码开关
-- 布尔型参数，转换为 是/否 下拉选项
UPDATE `sys_config`
SET `config_options`  = '是&true#否&false',
    `config_value_type` = 'D'
WHERE `config_key` = 'sys.account.forgetPassword';

-- 5. 用户管理-初始密码长度
-- 纯数字参数，不维护 config_options，仅设置类型为 N
UPDATE `sys_config`
SET `config_value_type` = 'N'
WHERE `config_key` = 'sys.user.initPassword.length';

-- 6. 主框架页-侧边栏主题
-- 枚举型参数，填充为 深色/浅色 下拉选项
UPDATE `sys_config`
SET `config_options`  = '深色&theme-dark#浅色&theme-light',
    `config_value_type` = 'D'
WHERE `config_key` = 'sys.index.sideTheme';

-- 7. 主框架页-布局设置
-- 枚举型参数，填充为 左侧/顶部 下拉选项
UPDATE `sys_config`
SET `config_options`  = '左侧&left#顶部&top',
    `config_value_type` = 'D'
WHERE `config_key` = 'sys.index.layoutStyle';

-- 8. 默认未匹配的参数设置为字符串类型
-- 仅对未明确分类（NULL 或空）的参数设置默认类型，避免覆盖上面已设置的值
-- 不修改 config_options 字段，保持原值不变
UPDATE `sys_config`
SET `config_value_type` = 'S'
WHERE (`config_value_type` IS NULL
        OR `config_value_type` = '')
  AND `config_key` NOT IN (
        'sys.index.skinName',
        'sys.account.registerUser',
        'sys.account.captchaEnabled',
        'sys.account.forgetPassword',
        'sys.user.initPassword.length',
        'sys.index.sideTheme',
        'sys.index.layoutStyle'
    );
