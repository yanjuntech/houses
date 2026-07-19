# 验证检查清单

## FR-1: 修复小区表单 region 校验 bug
- [x] community/index.vue 的 rules.region 改为 validator 函数，校验 formRegion 长度
- [x] 浏览器：选择"北京市 / 市辖区"两级后提交，不再报"所在地区不能为空"（validator 逻辑：formRegion.length === 0 才报错）
- [x] 浏览器：选择三级地区（山东省/聊城市/阳谷县）提交正常
- [x] 浏览器：未选择地区时仍提示"所在地区不能为空"

## FR-2: 小区物业公司联系电话
- [x] community/index.vue 编辑表单新增 propertyPhone 输入框
- [x] 编辑表单的 propertyPhone 字段有手机号/座机格式校验（pattern: 1[3-9]\d{9} 或 0\d{2,3}-?\d{7,8}）
- [x] community/index.vue 列表表格新增"联系电话"列
- [x] reset() 方法初始化 propertyPhone=undefined
- [x] 后端 BizCommunity 实体已含 propertyPhone 字段及 @JsonProperty 注解
- [x] BizCommunityMapper.xml 已支持 property_phone 字段读写

## FR-3: 行政区划国标数据清洗
- [x] sql/sys_region_full.sql 脚本已编写
- [x] 脚本执行后 sys_region 表总记录数 3417 > 3000
- [x] 脚本覆盖 34 省级（含直辖市、自治区、特别行政区）
- [x] 脚本覆盖 383 个地级市
- [x] 脚本覆盖 3000 个区县
- [x] 数据格式符合 sys_region 表结构（parent_id、region_code、region_name、region_level）

## FR-4: 小区地图选点
- [x] sys_config 新增 sys.community.mapProvider 配置项（D 下拉类型）
- [x] sys.community.mapProvider 的 config_options 为 `高德地图&amap#腾讯地图&tencent`
- [x] sys.community.mapProvider 的 config_value 默认 amap
- [x] 新增 AMapPicker.vue 高德地图选点组件
- [x] 新增 TMapPicker.vue 腾讯地图选点组件
- [x] community/index.vue 根据 sys.community.mapProvider 动态渲染地图组件
- [x] 地图组件支持搜索地址（el-autocomplete + PlaceSearch/AutoComplete）
- [x] 地图组件支持点击选点（map.on('click')）
- [x] 选中后 address、longitude、latitude 字段自动填充（handleMapSelect 方法）
- [x] 经纬度输入框 disabled（由地图选点自动填充）
- [注意] 组件中使用占位 API Key（YOUR_AMAP_KEY / YOUR_QQMAP_KEY），实际部署需替换为有效 Key

## FR-5: 修复电话簿分类点击 400
- [x] phonebook/index.vue handleNodeClick 代码逻辑正确（叶子节点 set queryParams.category=label，根节点 set undefined）
- [x] 后端 BizPhonebookMapper.xml selectBizPhonebookList 的 category 条件正确（AND category = #{category}）
- [x] tansParams 工具会跳过 undefined 值，不会传递空参数
- [x] 浏览器：点击电话簿左侧分类树节点不再返回 400

## FR-6: 电话簿双电话字段
- [x] phonebook/index.vue 编辑表单新增 phone1 输入框（必填）
- [x] phonebook/index.vue 编辑表单新增 phone2 输入框（选填）
- [x] 编辑表单的 phone1、phone2 格式校验（11 位手机号或座机）
- [x] 列表"电话"列显示 phone1 / phone2（phone2 为空时仅显示 phone1）
- [x] reset() 方法初始化 phone1/phone2 字段
- [x] 后端 BizPhonebook 实体已含 phone1, phone2 字段
- [x] BizPhonebookMapper.xml 已支持 phone1/phone2 字段读写

## FR-7: 种子数据 - 小区登记申请
- [x] sql/seed_biz_community_apply.sql 脚本已编写
- [x] 脚本执行后 biz_community_apply 表新增 15 条记录
- [x] 所有新增记录的 apply_status='0'（未审批）

## FR-8: 种子数据 - 电话簿申请
- [x] sql/seed_biz_phonebook_apply.sql 脚本已编写
- [x] 脚本执行后 biz_phonebook_apply 表新增 50 条记录

## FR-9: 种子数据 - 电子合同
- [x] sql/seed_biz_contract.sql 脚本已编写
- [x] 脚本执行后 biz_contract 表新增 50 条记录
- [x] 状态覆盖 0/1/2/3 等多种状态

## FR-10: 种子数据 - 轮播图
- [x] sql/seed_biz_banner.sql 脚本已编写
- [x] 脚本执行后 biz_banner 表新增 10 条记录

## FR-11: 种子数据 - 邀请管理
- [x] sql/seed_biz_invite_relation.sql 脚本已编写
- [x] 脚本执行后 biz_invite_relation 表新增 50 条记录

## FR-12: 种子数据 - 房屋维修
- [x] sql/seed_biz_house_repair.sql 脚本已编写
- [x] 脚本执行后 biz_house_repair 表新增 50 条记录
- [x] 状态覆盖 0/1/2/3/4/5/6 等多种状态
- [x] 时间字段按状态递进

## FR-13: 种子数据 - 消息管理
- [x] sql/seed_biz_user_message.sql 脚本已编写
- [x] 脚本执行后 biz_user_message 表新增 50 条记录
- [x] 数据覆盖不同 is_read 状态（0/1）

## FR-14: 列表行展开 - 小区
- [x] community/index.vue el-table 新增 type="expand" 列
- [x] 展开模板显示省/市/区、详细地址、经纬度、物业公司、物业公司电话、标签、备注、创建人/时间
- [x] 展开折叠箭头正常显示

## FR-15: 列表行展开 - 电话簿
- [x] phonebook/index.vue el-table 新增 type="expand" 列
- [x] 展开模板显示商家名称、负责人、电话1/电话2、分类、地址、营业执照、状态、有效期、备注、创建信息

## FR-16: 列表行展开 - 电子合同
- [x] contract/index.vue el-table 新增 type="expand" 列
- [x] 展开模板显示合同号、标题、房屋、社区、房东、租户、月租金、押金、租期、支付周期、合同期间、状态、签署时间、PDF 路径、备注、创建信息

## FR-17: 列表行展开 - 房屋维修 + 横向时间轴
- [x] repair/index.vue el-table 新增 type="expand" 列
- [x] 展开模板显示房屋、租户、房东、维修类型、描述、图片、状态、备注等
- [x] 展开区底部增加 el-timeline 时间轴
- [x] 时间轴节点包含：提交（create_time）→ 确认（confirm_time）→ 完成（complete_time）→ 租客确认（tenant_confirm_time）→ 报销（reimburse_time）
- [x] 时间轴节点按状态动态渲染（done 节点 #67C23A 高亮，未发生 #909399 灰色）

## 端到端验证
- [x] 后端服务成功启动（ruoyi-admin.jar 进程运行中）
- [x] 前端 dev server 成功启动（vue-cli-service serve 进程运行中）
- [x] 数据库种子数据全部执行成功（共 215 条业务种子数据 + 3417 条行政区划数据）
- [x] 所有上述 FR 检查项全部通过
