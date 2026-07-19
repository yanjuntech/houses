-- ============================================
-- 邀请管理种子数据（50 条）
-- inviter_id 1-5, invitee_id 6-55
-- invite_status 分布：0(待认证) 10条 / 1(已认证) 30条 / 2(已拒绝) 10条
-- ============================================
DELETE FROM biz_invite_relation;

INSERT INTO biz_invite_relation
(inviter_id, invitee_id, invite_code, invite_status, invite_time, certified_time, create_by, create_time, remark)
VALUES
-- ====== 状态 0 待认证（10 条）======
(1, 6,  'INV2026A001', '0', NOW() - INTERVAL 1 DAY,  NULL,                    'admin', NOW() - INTERVAL 1 DAY,  NULL),
(1, 7,  'INV2026A002', '0', NOW() - INTERVAL 1 DAY,  NULL,                    'admin', NOW() - INTERVAL 1 DAY,  NULL),
(2, 16, 'INV2026B001', '0', NOW() - INTERVAL 2 DAY,  NULL,                    'admin', NOW() - INTERVAL 2 DAY,  NULL),
(2, 17, 'INV2026B002', '0', NOW() - INTERVAL 2 DAY,  NULL,                    'admin', NOW() - INTERVAL 2 DAY,  NULL),
(3, 26, 'INV2026C001', '0', NOW() - INTERVAL 3 DAY,  NULL,                    'admin', NOW() - INTERVAL 3 DAY,  NULL),
(3, 27, 'INV2026C002', '0', NOW() - INTERVAL 3 DAY,  NULL,                    'admin', NOW() - INTERVAL 3 DAY,  NULL),
(4, 36, 'INV2026D001', '0', NOW() - INTERVAL 4 DAY,  NULL,                    'admin', NOW() - INTERVAL 4 DAY,  NULL),
(4, 37, 'INV2026D002', '0', NOW() - INTERVAL 4 DAY,  NULL,                    'admin', NOW() - INTERVAL 4 DAY,  NULL),
(5, 46, 'INV2026E001', '0', NOW() - INTERVAL 5 DAY,  NULL,                    'admin', NOW() - INTERVAL 5 DAY,  NULL),
(5, 47, 'INV2026E002', '0', NOW() - INTERVAL 5 DAY,  NULL,                    'admin', NOW() - INTERVAL 5 DAY,  NULL),

-- ====== 状态 1 已认证（30 条）======
(1, 8,  'INV2026A003', '1', NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 8 DAY,  'admin', NOW() - INTERVAL 10 DAY, '认证通过'),
(1, 9,  'INV2026A004', '1', NOW() - INTERVAL 12 DAY, NOW() - INTERVAL 10 DAY, 'admin', NOW() - INTERVAL 12 DAY, NULL),
(1, 10, 'INV2026A005', '1', NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 13 DAY, 'admin', NOW() - INTERVAL 15 DAY, NULL),
(1, 11, 'INV2026A006', '1', NOW() - INTERVAL 18 DAY, NOW() - INTERVAL 16 DAY, 'admin', NOW() - INTERVAL 18 DAY, NULL),
(1, 12, 'INV2026A007', '1', NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 18 DAY, 'admin', NOW() - INTERVAL 20 DAY, NULL),
(1, 13, 'INV2026A008', '1', NOW() - INTERVAL 25 DAY, NOW() - INTERVAL 23 DAY, 'admin', NOW() - INTERVAL 25 DAY, NULL),
(2, 18, 'INV2026B003', '1', NOW() - INTERVAL 11 DAY, NOW() - INTERVAL 9 DAY,  'admin', NOW() - INTERVAL 11 DAY, NULL),
(2, 19, 'INV2026B004', '1', NOW() - INTERVAL 14 DAY, NOW() - INTERVAL 12 DAY, 'admin', NOW() - INTERVAL 14 DAY, NULL),
(2, 20, 'INV2026B005', '1', NOW() - INTERVAL 17 DAY, NOW() - INTERVAL 15 DAY, 'admin', NOW() - INTERVAL 17 DAY, NULL),
(2, 21, 'INV2026B006', '1', NOW() - INTERVAL 22 DAY, NOW() - INTERVAL 20 DAY, 'admin', NOW() - INTERVAL 22 DAY, NULL),
(2, 22, 'INV2026B007', '1', NOW() - INTERVAL 28 DAY, NOW() - INTERVAL 26 DAY, 'admin', NOW() - INTERVAL 28 DAY, NULL),
(2, 23, 'INV2026B008', '1', NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 28 DAY, 'admin', NOW() - INTERVAL 30 DAY, NULL),
(3, 28, 'INV2026C003', '1', NOW() - INTERVAL 13 DAY, NOW() - INTERVAL 11 DAY, 'admin', NOW() - INTERVAL 13 DAY, NULL),
(3, 29, 'INV2026C004', '1', NOW() - INTERVAL 16 DAY, NOW() - INTERVAL 14 DAY, 'admin', NOW() - INTERVAL 16 DAY, NULL),
(3, 30, 'INV2026C005', '1', NOW() - INTERVAL 19 DAY, NOW() - INTERVAL 17 DAY, 'admin', NOW() - INTERVAL 19 DAY, NULL),
(3, 31, 'INV2026C006', '1', NOW() - INTERVAL 24 DAY, NOW() - INTERVAL 22 DAY, 'admin', NOW() - INTERVAL 24 DAY, NULL),
(3, 32, 'INV2026C007', '1', NOW() - INTERVAL 35 DAY, NOW() - INTERVAL 33 DAY, 'admin', NOW() - INTERVAL 35 DAY, NULL),
(3, 33, 'INV2026C008', '1', NOW() - INTERVAL 40 DAY, NOW() - INTERVAL 38 DAY, 'admin', NOW() - INTERVAL 40 DAY, NULL),
(4, 38, 'INV2026D003', '1', NOW() - INTERVAL 14 DAY, NOW() - INTERVAL 12 DAY, 'admin', NOW() - INTERVAL 14 DAY, NULL),
(4, 39, 'INV2026D004', '1', NOW() - INTERVAL 21 DAY, NOW() - INTERVAL 19 DAY, 'admin', NOW() - INTERVAL 21 DAY, NULL),
(4, 40, 'INV2026D005', '1', NOW() - INTERVAL 26 DAY, NOW() - INTERVAL 24 DAY, 'admin', NOW() - INTERVAL 26 DAY, NULL),
(4, 41, 'INV2026D006', '1', NOW() - INTERVAL 32 DAY, NOW() - INTERVAL 30 DAY, 'admin', NOW() - INTERVAL 32 DAY, NULL),
(4, 42, 'INV2026D007', '1', NOW() - INTERVAL 45 DAY, NOW() - INTERVAL 43 DAY, 'admin', NOW() - INTERVAL 45 DAY, NULL),
(4, 43, 'INV2026D008', '1', NOW() - INTERVAL 50 DAY, NOW() - INTERVAL 48 DAY, 'admin', NOW() - INTERVAL 50 DAY, NULL),
(5, 48, 'INV2026E003', '1', NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 13 DAY, 'admin', NOW() - INTERVAL 15 DAY, NULL),
(5, 49, 'INV2026E004', '1', NOW() - INTERVAL 23 DAY, NOW() - INTERVAL 21 DAY, 'admin', NOW() - INTERVAL 23 DAY, NULL),
(5, 50, 'INV2026E005', '1', NOW() - INTERVAL 33 DAY, NOW() - INTERVAL 31 DAY, 'admin', NOW() - INTERVAL 33 DAY, NULL),
(5, 51, 'INV2026E006', '1', NOW() - INTERVAL 38 DAY, NOW() - INTERVAL 36 DAY, 'admin', NOW() - INTERVAL 38 DAY, NULL),
(5, 52, 'INV2026E007', '1', NOW() - INTERVAL 42 DAY, NOW() - INTERVAL 40 DAY, 'admin', NOW() - INTERVAL 42 DAY, NULL),
(5, 53, 'INV2026E008', '1', NOW() - INTERVAL 55 DAY, NOW() - INTERVAL 53 DAY, 'admin', NOW() - INTERVAL 55 DAY, NULL),

-- ====== 状态 2 已拒绝（10 条）======
(1, 14, 'INV2026A009', '2', NOW() - INTERVAL 6 DAY,  NULL,                    'admin', NOW() - INTERVAL 6 DAY,  '信息不完整'),
(1, 15, 'INV2026A010', '2', NOW() - INTERVAL 8 DAY,  NULL,                    'admin', NOW() - INTERVAL 8 DAY,  '认证失败'),
(2, 24, 'INV2026B009', '2', NOW() - INTERVAL 7 DAY,  NULL,                    'admin', NOW() - INTERVAL 7 DAY,  '材料虚假'),
(2, 25, 'INV2026B010', '2', NOW() - INTERVAL 9 DAY,  NULL,                    'admin', NOW() - INTERVAL 9 DAY,  NULL),
(3, 34, 'INV2026C009', '2', NOW() - INTERVAL 6 DAY,  NULL,                    'admin', NOW() - INTERVAL 6 DAY,  '不符合邀请条件'),
(3, 35, 'INV2026C010', '2', NOW() - INTERVAL 11 DAY, NULL,                    'admin', NOW() - INTERVAL 11 DAY, NULL),
(4, 44, 'INV2026D009', '2', NOW() - INTERVAL 7 DAY,  NULL,                    'admin', NOW() - INTERVAL 7 DAY,  '资料过期'),
(4, 45, 'INV2026D010', '2', NOW() - INTERVAL 12 DAY, NULL,                    'admin', NOW() - INTERVAL 12 DAY, NULL),
(5, 54, 'INV2026E009', '2', NOW() - INTERVAL 6 DAY,  NULL,                    'admin', NOW() - INTERVAL 6 DAY,  '拒绝认证'),
(5, 55, 'INV2026E010', '2', NOW() - INTERVAL 13 DAY, NULL,                    'admin', NOW() - INTERVAL 13 DAY, NULL);
