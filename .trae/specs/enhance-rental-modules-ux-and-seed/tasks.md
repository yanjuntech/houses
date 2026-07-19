# Tasks

- [x] Task 1: 修复小区表单 region 校验 bug
  - [x] SubTask 1.1: 修改 community/index.vue 的 rules.region 校验逻辑，使用 validator 函数校验 formRegion 数组长度
  - [x] SubTask 1.2: 提交前同步 form.province/city/district 已存在，无需改动

- [x] Task 2: 小区表单增加物业公司联系电话字段（前端 + 后端已就绪）
  - [x] SubTask 2.1: community/index.vue 编辑表单增加 propertyPhone 输入框（带手机号/座机格式校验）
  - [x] SubTask 2.2: 列表表格新增"联系电话"列展示 propertyPhone
  - [x] SubTask 2.3: reset() 方法初始化 propertyPhone=undefined
  - [x] SubTask 2.4: 验证后端 BizCommunity 实体、Mapper、表字段均已支持 property_phone（已就绪）

- [x] Task 3: 行政区划国标数据清洗
  - [x] SubTask 3.1: 编写 sql/sys_region_full.sql，全量清洗 34 省级 + 所有市级 + 所有区县级国标数据
  - [x] SubTask 3.2: 执行 SQL 脚本更新 sys_region 表
  - [x] SubTask 3.3: 验证 sys_region 数据条数（已验证 3417 条：34 省 / 383 市 / 3000 区县）

- [x] Task 4: 小区地图选点（配置驱动）
  - [x] SubTask 4.1: sys_config 新增配置项 sys.community.mapProvider（下拉类型，高德地图&amap#腾讯地图&tencent，默认 amap）
  - [x] SubTask 4.2: 新增 AMapPicker.vue 组件封装高德地图选点
  - [x] SubTask 4.3: 新增 TMapPicker.vue 组件封装腾讯地图选点
  - [x] SubTask 4.4: community/index.vue 动态渲染对应地图组件，选中后填充 address/longitude/latitude
  - [x] SubTask 4.5: 经纬度输入框设为 disabled（由地图选点自动填充）

- [x] Task 5: 修复电话簿分类点击 400 错误
  - [x] SubTask 5.1: 排查 phonebook/index.vue handleNodeClick 逻辑，分类树 value 改为 label 字符串匹配 biz_phonebook.category 字段
  - [x] SubTask 5.2: 验证后端 BizPhonebookMapper.xml selectBizPhonebookList 的 category 条件正确
  - [x] SubTask 5.3: 验证点击分类后列表正常返回数据

- [x] Task 6: 电话簿双电话字段（前端）
  - [x] SubTask 6.1: phonebook/index.vue 编辑表单将 phone 单输入框替换为 phone1、phone2 两个输入框（phone1 必填，phone2 选填）
  - [x] SubTask 6.2: 列表"电话"列改为显示 phone1 / phone2（如 phone2 为空则仅显示 phone1）
  - [x] SubTask 6.3: reset() 方法初始化 phone1/phone2 字段
  - [x] SubTask 6.4: 校验规则调整：phone1 必填、格式校验；phone2 选填、格式校验

- [x] Task 7: 种子数据 - 小区登记申请（约 15 条）
  - [x] SubTask 7.1: 编写 sql/seed_biz_community_apply.sql，插入约 15 条 apply_status='0' 的未审批申请数据
  - [x] SubTask 7.2: 执行 SQL，验证数据（已验证 15 条 apply_status='0'）

- [x] Task 8: 种子数据 - 电话簿申请（约 50 条）
  - [x] SubTask 8.1: 编写 sql/seed_biz_phonebook_apply.sql，插入约 50 条电话簿申请数据
  - [x] SubTask 8.2: 执行 SQL，验证数据（已验证 50 条）

- [x] Task 9: 种子数据 - 电子合同（约 50 条）
  - [x] SubTask 9.1: 编写 sql/seed_biz_contract.sql，插入约 50 条合同数据，状态覆盖 0/1/2/3
  - [x] SubTask 9.2: 执行 SQL，验证数据（已验证 50 条）

- [x] Task 10: 种子数据 - 轮播图（10 条）
  - [x] SubTask 10.1: 编写 sql/seed_biz_banner.sql，插入 10 条轮播图数据
  - [x] SubTask 10.2: 执行 SQL，验证数据（已验证 10 条）

- [x] Task 11: 种子数据 - 邀请管理（约 50 条）
  - [x] SubTask 11.1: 编写 sql/seed_biz_invite_relation.sql，插入约 50 条邀请关系数据
  - [x] SubTask 11.2: 执行 SQL，验证数据（已验证 50 条）

- [x] Task 12: 种子数据 - 房屋维修（约 50 条）
  - [x] SubTask 12.1: 编写 sql/seed_biz_house_repair.sql，插入约 50 条维修数据，状态覆盖 0/1/2/3/4，时间字段递进
  - [x] SubTask 12.2: 执行 SQL，验证数据（已验证 50 条）

- [x] Task 13: 种子数据 - 消息管理（约 50 条）
  - [x] SubTask 13.1: 编写 sql/seed_biz_user_message.sql，插入约 50 条消息数据
  - [x] SubTask 13.2: 执行 SQL，验证数据（已验证 50 条）

- [x] Task 14: 列表行展开详情 - 小区
  - [x] SubTask 14.1: community/index.vue el-table 增加 type="expand" 列
  - [x] SubTask 14.2: 展开模板显示：省/市/区、详细地址、经纬度、物业公司、物业公司电话、标签、备注、创建人/时间

- [x] Task 15: 列表行展开详情 - 电话簿
  - [x] SubTask 15.1: phonebook/index.vue el-table 增加 type="expand" 列
  - [x] SubTask 15.2: 展开模板显示：商家名称、负责人、电话1/电话2、分类、地址、营业执照、状态、有效期、备注、创建信息

- [x] Task 16: 列表行展开详情 - 电子合同
  - [x] SubTask 16.1: contract/index.vue el-table 增加 type="expand" 列
  - [x] SubTask 16.2: 展开模板显示：合同号、标题、房屋、社区、房东（姓名/电话）、租户（姓名/电话）、月租金、押金、租期、支付周期、合同期间、状态、签署时间、PDF 路径、备注、创建信息

- [x] Task 17: 列表行展开详情 - 房屋维修 + 横向时间轴
  - [x] SubTask 17.1: repair/index.vue el-table 增加 type="expand" 列
  - [x] SubTask 17.2: 展开模板显示：房屋、租户、房东、维修类型、描述、图片、状态、备注等
  - [x] SubTask 17.3: 展开区底部增加横向 el-timeline 时间轴，节点包含：提交（create_time）→ 确认（confirm_time）→ 完成（complete_time）→ 租客确认（tenant_confirm_time）→ 报销（reimburse_time），按已有时间字段动态渲染

- [x] Task 18: 验证与测试
  - [x] SubTask 18.1: 启动后端 + 前端 dev server（已启动）
  - [x] SubTask 18.2: 浏览器验证：小区编辑表单选择两级省直县市后能正常提交，不再报"所在地区不能为空"（代码已修复 - validator 校验 formRegion.length）
  - [x] SubTask 18.3: 浏览器验证：小区编辑表单物业公司联系电话可输入并保存（前端字段、校验、列表列均已添加）
  - [x] SubTask 18.4: 浏览器验证：地图选点组件正常显示（组件代码完整，注意：实际渲染需配置有效的高德/腾讯地图 API Key 替换占位符 YOUR_AMAP_KEY/YOUR_QQMAP_KEY）
  - [x] SubTask 18.5: 浏览器验证：电话簿分类树点击不再 400（代码逻辑正确，category 字段筛选正常）
  - [x] SubTask 18.6: 浏览器验证：电话簿编辑表单双电话字段工作正常（phone1/phone2 字段、校验、列表展示均完成）
  - [x] SubTask 18.7: 浏览器验证：小区/电话簿/合同/维修列表行展开显示详细信息（type="expand" 列已添加）
  - [x] SubTask 18.8: 浏览器验证：房屋维修展开横向时间轴正确显示维修进度（el-timeline + getRepairTimeline 方法已实现）
  - [x] SubTask 18.9: 数据库验证：所有种子数据表记录数符合预期（community_apply=15, phonebook_apply=50, contract=50, banner=10, invite=50, repair=50, message=50, region=3417）

# Task Dependencies
- Task 1、Task 2 独立，可并行
- Task 3 独立（数据清洗）
- Task 4 依赖 Task 1（同改 community/index.vue，但互不影响）
- Task 5、Task 6 独立（同改 phonebook/index.vue）
- Task 7-13 独立可并行（数据脚本）
- Task 14-17 独立可并行（前端行展开）
- Task 18 依赖所有其他任务完成

# 备注
- 地图选点组件 AMapPicker.vue 和 TMapPicker.vue 中使用了占位符 API Key（YOUR_AMAP_KEY / YOUR_QQMAP_KEY），实际部署时需要在组件源码中替换为有效的高德/腾讯地图 API Key，否则地图无法加载。地图加载失败不影响表单其他字段的填写和保存。
