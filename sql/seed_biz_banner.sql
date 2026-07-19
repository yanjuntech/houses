-- ============================================
-- 轮播图种子数据（10 条，status 全部 '0'，sort 1-10 递增）
-- ============================================
DELETE FROM biz_banner;

INSERT INTO biz_banner
(image, title, content, valid_start, valid_end, contact_phone, sort, status, create_by, create_time, remark)
VALUES
('https://picsum.photos/seed/banner01/750/300', '春季租房节优惠活动',           '春季租房节来啦！精选好房，月租立减500元，签约即送一个月物业费，活动时间有限，先到先得！',       NOW() - INTERVAL 30 DAY, NOW() + INTERVAL 60 DAY, '400-001-0001', 1,  '0', 'admin', NOW() - INTERVAL 30 DAY, '首页主推活动'),
('https://picsum.photos/seed/banner02/750/300', '新用户专享红包',               '新用户注册即送200元租房红包，首次签约立抵房租，租房更省钱！',                                   NOW() - INTERVAL 25 DAY, NOW() + INTERVAL 65 DAY, '400-001-0002', 2,  '0', 'admin', NOW() - INTERVAL 25 DAY, '新用户拉新'),
('https://picsum.photos/seed/banner03/750/300', '品质合租房源推荐',             '精选城市核心区域品质合租房源，拎包入住，月租低至1500元起，立即查看！',                          NOW() - INTERVAL 20 DAY, NOW() + INTERVAL 70 DAY, '400-001-0003', 3,  '0', 'admin', NOW() - INTERVAL 20 DAY, '合租房源推广'),
('https://picsum.photos/seed/banner04/750/300', '整租房源大促',                 '一居室、二居室、三居室整租房源大促，签约立享首月8折优惠，房源有限，先到先得！',                 NOW() - INTERVAL 15 DAY, NOW() + INTERVAL 75 DAY, '400-001-0004', 4,  '0', 'admin', NOW() - INTERVAL 15 DAY, '整租房源推广'),
('https://picsum.photos/seed/banner05/750/300', '房屋维修服务上线',             '专业房屋维修服务上线！水电维修、家具家电、房屋结构维修一站搞定，一键预约，快速上门！',         NOW() - INTERVAL 10 DAY, NOW() + INTERVAL 80 DAY, '400-001-0005', 5,  '0', 'admin', NOW() - INTERVAL 10 DAY, '维修服务推广'),
('https://picsum.photos/seed/banner06/750/300', '电子合同在线签署',             '电子合同在线签署功能上线，省时省力， legally binding，全程留痕，安全可靠！',                     NOW() - INTERVAL 5 DAY,  NOW() + INTERVAL 85 DAY, '400-001-0006', 6,  '0', 'admin', NOW() - INTERVAL 5 DAY,  '电子合同功能推广'),
('https://picsum.photos/seed/banner07/750/300', '业主委托发布房源',             '业主直租！免去中介费，房源真实可靠，租客直连房东，租房更省心！',                                 NOW() - INTERVAL 3 DAY,  NOW() + INTERVAL 87 DAY, '400-001-0007', 7,  '0', 'admin', NOW() - INTERVAL 3 DAY,  '业主委托推广'),
('https://picsum.photos/seed/banner08/750/300', '社区便民电话簿',               '社区便民电话簿上线啦！周边商家一键查询，餐饮、快递、家政、维修一应俱全，生活更便利！',         NOW() - INTERVAL 2 DAY,  NOW() + INTERVAL 88 DAY, '400-001-0008', 8,  '0', 'admin', NOW() - INTERVAL 2 DAY,  '电话簿功能推广'),
('https://picsum.photos/seed/banner09/750/300', '邀请好友得现金奖励',           '邀请好友注册并完成签约，双方均可获得100元现金奖励，多邀多得，上不封顶！',                       NOW() - INTERVAL 1 DAY,  NOW() + INTERVAL 89 DAY, '400-001-0009', 9,  '0', 'admin', NOW() - INTERVAL 1 DAY,  '邀请活动推广'),
('https://picsum.photos/seed/banner10/750/300', '夏日清凉租房季',               '夏日清凉租房季，空调房、拎包入住，签约即送电风扇一台，让您清凉一夏！',                           NOW(),                   NOW() + INTERVAL 90 DAY, '400-001-0010', 10, '0', 'admin', NOW(),                   '夏日活动推广');
