-- 轮播图表新增跳转链接字段
ALTER TABLE biz_banner ADD COLUMN jump_url VARCHAR(255) DEFAULT NULL COMMENT '点击跳转链接（小程序内页面路径）' AFTER status;

-- 可选：为现有轮播图配置跳转链接示例
-- UPDATE biz_banner SET jump_url = '/pages/house/list' WHERE title = '热门房源';
