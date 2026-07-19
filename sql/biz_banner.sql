-- ----------------------------
-- 轮播图表
-- ----------------------------
DROP TABLE IF EXISTS `biz_banner`;
CREATE TABLE `biz_banner` (
  `banner_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
  `image` varchar(500) NOT NULL COMMENT '图片URL',
  `title` varchar(200) DEFAULT NULL COMMENT '简短标题',
  `content` text COMMENT '详细内容',
  `valid_start` datetime DEFAULT NULL COMMENT '发布有效期开始',
  `valid_end` datetime DEFAULT NULL COMMENT '发布有效期结束',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `sort` int(4) DEFAULT 0 COMMENT '显示顺序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`banner_id`),
  KEY `idx_status` (`status`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';
