-- ============================================================
-- 菜单配置 SQL
-- 说明：新增行政区划管理、轮播图管理等菜单
-- ============================================================

-- ============================================================
-- A. 系统管理 - 行政区划管理
-- ============================================================

-- 行政区划目录（二级菜单，在系统管理下）
-- menu_id 起始：2200（避开已有ID范围）
insert into sys_menu values(2200, '行政区划', 1, 6, 'region', 'system/region/index', null, null, 1, 0, 'M', '0', '0', 'system:region:list', 'tree-table', 'admin', sysdate(), '', null, '行政区划管理目录');

-- 按钮级权限
insert into sys_menu values(2201, '行政区划查询', 2200, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'system:region:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2202, '行政区划新增', 2200, 2, '', null, null, null, 1, 0, 'F', '0', '0', 'system:region:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2203, '行政区划修改', 2200, 3, '', null, null, null, 1, 0, 'F', '0', '0', 'system:region:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2204, '行政区划删除', 2200, 4, '', null, null, null, 1, 0, 'F', '0', '0', 'system:region:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2205, '行政区划导出', 2200, 5, '', null, null, null, 1, 0, 'F', '0', '0', 'system:region:export', '#', 'admin', sysdate(), '', null, '');

-- ============================================================
-- B. 业务管理 - 轮播图管理
-- ============================================================

-- 顶级菜单：业务管理（menu_id=2000，已存在）
-- 轮播图管理（二级菜单，在业务管理下）
-- menu_id 起始：2150
insert into sys_menu values(2150, '轮播图管理', 2000, 10, 'banner', 'rental/banner/index', null, null, 1, 0, 'C', '0', '0', 'rental:banner:list', 'picture', 'admin', sysdate(), '', null, '轮播图管理');

-- 按钮级权限
insert into sys_menu values(2151, '轮播图查询', 2150, 1, '', null, null, null, 1, 0, 'F', '0', '0', 'rental:banner:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2152, '轮播图新增', 2150, 2, '', null, null, null, 1, 0, 'F', '0', '0', 'rental:banner:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2153, '轮播图修改', 2150, 3, '', null, null, null, 1, 0, 'F', '0', '0', 'rental:banner:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2154, '轮播图删除', 2150, 4, '', null, null, null, 1, 0, 'F', '0', '0', 'rental:banner:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2155, '轮播图导出', 2150, 5, '', null, null, null, 1, 0, 'F', '0', '0', 'rental:banner:export', '#', 'admin', sysdate(), '', null, '');

-- ============================================================
-- C. 小程序用户消息管理（消息推送按钮权限）
-- ============================================================

-- 消息推送按钮（挂在小程序用户菜单下，menu_id=2001）
insert into sys_menu values(2160, '消息推送', 2001, 10, '', null, null, null, 1, 0, 'F', '0', '0', 'biz:miniapp:user:sendMessage', '#', 'admin', sysdate(), '', null, '小程序用户消息推送');

-- ============================================================
-- 将所有新增菜单授权给管理员角色（role_id=1）
-- ============================================================
insert into sys_role_menu (role_id, menu_id)
select 1, menu_id from sys_menu where menu_id >= 2150 and menu_id < 2300;
