-- 有效期与发布次数字段
-- biz_house 表增加有效期截止时间
ALTER TABLE biz_house ADD COLUMN valid_until datetime NULL COMMENT '有效期截止时间' AFTER status;

-- biz_phonebook 表增加有效期截止时间
ALTER TABLE biz_phonebook ADD COLUMN valid_until datetime NULL COMMENT '有效期截止时间' AFTER status;

-- biz_miniapp_user 表增加发布次数相关字段
ALTER TABLE biz_miniapp_user ADD COLUMN publish_count int(11) DEFAULT 0 COMMENT '剩余发布次数' AFTER status;
ALTER TABLE biz_miniapp_user ADD COLUMN publish_period_end datetime NULL COMMENT '发布周期结束时间' AFTER publish_count;
ALTER TABLE biz_miniapp_user ADD COLUMN total_publish_count int(11) DEFAULT 0 COMMENT '累计发布次数' AFTER publish_period_end;
