-- ----------------------------
-- 用户消息表
-- ----------------------------
DROP TABLE IF EXISTS `biz_user_message`;
CREATE TABLE `biz_user_message` (
  `message_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `user_id` bigint(20) NOT NULL COMMENT '接收用户ID',
  `title` varchar(200) NOT NULL COMMENT '消息标题',
  `content` text COMMENT '消息内容',
  `is_read` char(1) DEFAULT '0' COMMENT '是否已读（0未读 1已读）',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `send_by` varchar(64) DEFAULT '' COMMENT '发送人',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`message_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_read` (`is_read`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户消息表';
