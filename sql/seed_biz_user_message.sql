-- ============================================
-- 消息管理种子数据（50 条）
-- is_read 分布：0(未读) 30条 / 1(已读) 20条
-- ============================================
DELETE FROM biz_user_message;

INSERT INTO biz_user_message
(user_id, title, content, is_read, read_time, send_by, send_time, create_by, create_time, remark)
VALUES
-- ====== 未读消息（30 条）======
(1,  '您的小区登记申请已提交',         '您提交的"阳光花园小区"登记申请已成功提交，请耐心等待管理员审批。',                       '0', NULL, 'system',  NOW() - INTERVAL 1 DAY,  'admin', NOW() - INTERVAL 1 DAY,  NULL),
(1,  '您有新的合同待签署',             '合同编号 HT20260719001 等待您签署，请尽快处理。',                                       '0', NULL, 'system',  NOW() - INTERVAL 1 DAY,  'admin', NOW() - INTERVAL 1 DAY,  NULL),
(1,  '租金到期提醒',                   '您有一笔租金将于3天后到期，请及时缴纳。',                                               '0', NULL, 'system',  NOW() - INTERVAL 2 DAY,  'admin', NOW() - INTERVAL 2 DAY,  NULL),
(2,  '您的小区登记申请已提交',         '您提交的"翠湖天地"登记申请已成功提交，请耐心等待管理员审批。',                           '0', NULL, 'system',  NOW() - INTERVAL 1 DAY,  'admin', NOW() - INTERVAL 1 DAY,  NULL),
(2,  '维修申请已受理',                 '您的维修申请已被房东确认，维修人员将在24小时内联系您。',                                 '0', NULL, 'system',  NOW() - INTERVAL 2 DAY,  'admin', NOW() - INTERVAL 2 DAY,  NULL),
(2,  '邀请好友奖励到账',               '您邀请的好友已完成认证，100元奖励已发放到您的账户。',                                   '0', NULL, 'system',  NOW() - INTERVAL 3 DAY,  'admin', NOW() - INTERVAL 3 DAY,  NULL),
(3,  '合同签署成功',                   '您的合同 HT20260719018 已签署成功，合同已生效。',                                       '0', NULL, 'system',  NOW() - INTERVAL 1 DAY,  'admin', NOW() - INTERVAL 1 DAY,  NULL),
(3,  '社区新功能上线',                 '社区便民电话簿功能上线，周边商家一键查询，快去体验吧！',                                 '0', NULL, 'system',  NOW() - INTERVAL 2 DAY,  'admin', NOW() - INTERVAL 2 DAY,  NULL),
(3,  '维修完成通知',                   '您的维修申请已完成，请确认维修结果。',                                                 '0', NULL, 'system',  NOW() - INTERVAL 4 DAY,  'admin', NOW() - INTERVAL 4 DAY,  NULL),
(4,  '您有新的合同待签署',             '合同编号 HT20260719004 等待您签署，请尽快处理。',                                       '0', NULL, 'system',  NOW() - INTERVAL 1 DAY,  'admin', NOW() - INTERVAL 1 DAY,  NULL),
(4,  '电话簿申请审核中',               '您的电话簿申请正在审核中，请耐心等待。',                                               '0', NULL, 'system',  NOW() - INTERVAL 2 DAY,  'admin', NOW() - INTERVAL 2 DAY,  NULL),
(4,  '租金缴纳提醒',                   '本月租金将于5天后到期，请及时缴纳。',                                                  '0', NULL, 'system',  NOW() - INTERVAL 3 DAY,  'admin', NOW() - INTERVAL 3 DAY,  NULL),
(5,  '小区登记申请审批通过',           '恭喜！您的"中海锦城"小区登记申请已审批通过。',                                          '0', NULL, 'system',  NOW() - INTERVAL 1 DAY,  'admin', NOW() - INTERVAL 1 DAY,  NULL),
(5,  '维修凭证上传提醒',               '您的维修已完成，请上传维修凭证以便报销。',                                             '0', NULL, 'system',  NOW() - INTERVAL 2 DAY,  'admin', NOW() - INTERVAL 2 DAY,  NULL),
(5,  '春季租房节活动',                 '春季租房节来啦！精选好房，月租立减500元，快来看看吧！',                                 '0', NULL, 'system',  NOW() - INTERVAL 5 DAY,  'admin', NOW() - INTERVAL 5 DAY,  NULL),
(6,  '您有新的合同待签署',             '合同编号 HT20260719006 等待您签署，请尽快处理。',                                       '0', NULL, 'system',  NOW() - INTERVAL 1 DAY,  'admin', NOW() - INTERVAL 1 DAY,  NULL),
(6,  '电子合同功能上线',               '电子合同在线签署功能上线，省时省力，安全可靠！',                                       '0', NULL, 'system',  NOW() - INTERVAL 3 DAY,  'admin', NOW() - INTERVAL 3 DAY,  NULL),
(6,  '维修申请已提交',                 '您的维修申请已提交，等待房东确认。',                                                   '0', NULL, 'system',  NOW() - INTERVAL 4 DAY,  'admin', NOW() - INTERVAL 4 DAY,  NULL),
(7,  '邀请好友奖励活动',               '邀请好友注册并完成签约，双方均可获得100元现金奖励！',                                   '0', NULL, 'system',  NOW() - INTERVAL 1 DAY,  'admin', NOW() - INTERVAL 1 DAY,  NULL),
(7,  '合同即将到期',                   '您的合同将于30天后到期，如需续约请提前联系房东。',                                     '0', NULL, 'system',  NOW() - INTERVAL 2 DAY,  'admin', NOW() - INTERVAL 2 DAY,  NULL),
(7,  '电话簿申请驳回',                 '您的电话簿申请因资料不全被驳回，请补充材料后重新提交。',                               '0', NULL, 'system',  NOW() - INTERVAL 3 DAY,  'admin', NOW() - INTERVAL 3 DAY,  NULL),
(8,  '您有新的合同待签署',             '合同编号 HT20260719008 等待您签署，请尽快处理。',                                       '0', NULL, 'system',  NOW() - INTERVAL 1 DAY,  'admin', NOW() - INTERVAL 1 DAY,  NULL),
(8,  '新用户专享红包',                 '新用户注册即送200元租房红包，首次签约立抵房租！',                                       '0', NULL, 'system',  NOW() - INTERVAL 4 DAY,  'admin', NOW() - INTERVAL 4 DAY,  NULL),
(8,  '维修进度通知',                   '您的维修申请已派单，维修人员将于明日上门。',                                           '0', NULL, 'system',  NOW() - INTERVAL 5 DAY,  'admin', NOW() - INTERVAL 5 DAY,  NULL),
(9,  '小区登记申请审批通过',           '恭喜！您的"融创文旅城"小区登记申请已审批通过。',                                        '0', NULL, 'system',  NOW() - INTERVAL 1 DAY,  'admin', NOW() - INTERVAL 1 DAY,  NULL),
(9,  '租金到期提醒',                   '您有一笔租金将于7天后到期，请及时缴纳。',                                              '0', NULL, 'system',  NOW() - INTERVAL 2 DAY,  'admin', NOW() - INTERVAL 2 DAY,  NULL),
(9,  '夏日清凉租房季',                 '夏日清凉租房季，空调房、拎包入住，签约即送电风扇一台！',                                 '0', NULL, 'system',  NOW() - INTERVAL 6 DAY,  'admin', NOW() - INTERVAL 6 DAY,  NULL),
(10, '您有新的合同待签署',             '合同编号 HT20260719010 等待您签署，请尽快处理。',                                       '0', NULL, 'system',  NOW() - INTERVAL 1 DAY,  'admin', NOW() - INTERVAL 1 DAY,  NULL),
(10, '邀请好友得现金奖励',             '邀请好友注册并完成签约，双方均可获得100元现金奖励，多邀多得！',                         '0', NULL, 'system',  NOW() - INTERVAL 3 DAY,  'admin', NOW() - INTERVAL 3 DAY,  NULL),
(10, '维修报销到账',                   '您的维修报销已到账，金额320元，请查收。',                                             '0', NULL, 'system',  NOW() - INTERVAL 7 DAY,  'admin', NOW() - INTERVAL 7 DAY,  NULL),

-- ====== 已读消息（20 条）======
(1,  '合同签署成功',                   '您的合同 HT20260719016 已签署成功，合同已生效。',                                       '1', NOW() - INTERVAL 30 DAY, 'system', NOW() - INTERVAL 31 DAY, 'admin', NOW() - INTERVAL 31 DAY, NULL),
(1,  '维修申请已受理',                 '您的维修申请已被房东确认，维修人员将在24小时内联系您。',                               '1', NOW() - INTERVAL 29 DAY, 'system', NOW() - INTERVAL 30 DAY, 'admin', NOW() - INTERVAL 30 DAY, NULL),
(1,  '租金缴纳成功',                   '您本月租金已缴纳成功，金额5500元。',                                                  '1', NOW() - INTERVAL 28 DAY, 'system', NOW() - INTERVAL 29 DAY, 'admin', NOW() - INTERVAL 29 DAY, NULL),
(2,  '小区登记申请审批通过',           '恭喜！您的"翠湖天地"小区登记申请已审批通过。',                                          '1', NOW() - INTERVAL 25 DAY, 'system', NOW() - INTERVAL 26 DAY, 'admin', NOW() - INTERVAL 26 DAY, NULL),
(2,  '合同到期提醒',                   '您的合同将于60天后到期，如需续约请提前联系房东。',                                     '1', NOW() - INTERVAL 22 DAY, 'system', NOW() - INTERVAL 23 DAY, 'admin', NOW() - INTERVAL 23 DAY, NULL),
(3,  '邀请好友奖励到账',               '您邀请的好友已完成认证，100元奖励已发放到您的账户。',                                   '1', NOW() - INTERVAL 20 DAY, 'system', NOW() - INTERVAL 21 DAY, 'admin', NOW() - INTERVAL 21 DAY, NULL),
(3,  '维修完成通知',                   '您的维修申请已完成，请确认维修结果。',                                                 '1', NOW() - INTERVAL 18 DAY, 'system', NOW() - INTERVAL 19 DAY, 'admin', NOW() - INTERVAL 19 DAY, NULL),
(3,  '电子合同功能上线',               '电子合同在线签署功能上线，省时省力，安全可靠！',                                       '1', NOW() - INTERVAL 16 DAY, 'system', NOW() - INTERVAL 17 DAY, 'admin', NOW() - INTERVAL 17 DAY, NULL),
(4,  '租金缴纳成功',                   '您本月租金已缴纳成功，金额7500元。',                                                  '1', NOW() - INTERVAL 15 DAY, 'system', NOW() - INTERVAL 16 DAY, 'admin', NOW() - INTERVAL 16 DAY, NULL),
(4,  '合同签署成功',                   '您的合同 HT20260719019 已签署成功，合同已生效。',                                       '1', NOW() - INTERVAL 14 DAY, 'system', NOW() - INTERVAL 15 DAY, 'admin', NOW() - INTERVAL 15 DAY, NULL),
(5,  '春季租房节活动',                 '春季租房节来啦！精选好房，月租立减500元，快来看看吧！',                                 '1', NOW() - INTERVAL 12 DAY, 'system', NOW() - INTERVAL 13 DAY, 'admin', NOW() - INTERVAL 13 DAY, NULL),
(5,  '维修报销到账',                   '您的维修报销已到账，金额580元，请查收。',                                             '1', NOW() - INTERVAL 11 DAY, 'system', NOW() - INTERVAL 12 DAY, 'admin', NOW() - INTERVAL 12 DAY, NULL),
(6,  '合同到期提醒',                   '您的合同将于90天后到期，如需续约请提前联系房东。',                                     '1', NOW() - INTERVAL 10 DAY, 'system', NOW() - INTERVAL 11 DAY, 'admin', NOW() - INTERVAL 11 DAY, NULL),
(6,  '社区便民电话簿上线',             '社区便民电话簿功能上线，周边商家一键查询，生活更便利！',                               '1', NOW() - INTERVAL 9 DAY,  'system', NOW() - INTERVAL 10 DAY, 'admin', NOW() - INTERVAL 10 DAY, NULL),
(7,  '维修申请已受理',                 '您的维修申请已被房东确认，维修人员将在24小时内联系您。',                               '1', NOW() - INTERVAL 8 DAY,  'system', NOW() - INTERVAL 9 DAY,  'admin', NOW() - INTERVAL 9 DAY,  NULL),
(7,  '邀请好友奖励活动',               '邀请好友注册并完成签约，双方均可获得100元现金奖励！',                                   '1', NOW() - INTERVAL 7 DAY,  'system', NOW() - INTERVAL 8 DAY,  'admin', NOW() - INTERVAL 8 DAY,  NULL),
(8,  '合同签署成功',                   '您的合同 HT20260719023 已签署成功，合同已生效。',                                       '1', NOW() - INTERVAL 6 DAY,  'system', NOW() - INTERVAL 7 DAY,  'admin', NOW() - INTERVAL 7 DAY,  NULL),
(8,  '租金缴纳成功',                   '您本月租金已缴纳成功，金额5200元。',                                                  '1', NOW() - INTERVAL 5 DAY,  'system', NOW() - INTERVAL 6 DAY,  'admin', NOW() - INTERVAL 6 DAY,  NULL),
(9,  '维修凭证上传提醒',               '您的维修已完成，请上传维修凭证以便报销。',                                             '1', NOW() - INTERVAL 4 DAY,  'system', NOW() - INTERVAL 5 DAY,  'admin', NOW() - INTERVAL 5 DAY,  NULL),
(10, '电话簿申请审核通过',             '恭喜！您的电话簿申请已审核通过，商家信息已上线。',                                     '1', NOW() - INTERVAL 3 DAY,  'system', NOW() - INTERVAL 4 DAY,  'admin', NOW() - INTERVAL 4 DAY,  NULL);
