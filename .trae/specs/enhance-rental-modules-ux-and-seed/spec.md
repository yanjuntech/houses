# 租赁模块增强与种子数据 Spec

## Why
当前租赁业务模块（小区、电话簿、电子合同、房屋维修、轮播图、邀请管理、消息管理）存在以下问题：
1. 小区编辑表单选择"省直县市"后仍提示"所在地区不能为空"（校验规则 bug）
2. 小区已有 property_phone 字段但前端未展示，且经纬度需手动输入，缺少地图选点
3. 行政区划数据不完整（仅山东、北京、上海、广东部分数据），未覆盖全国省市县区国标
4. 电话簿分类树点击报 400，且前端未使用 phone1/phone2 双电话字段
5. 各业务表数据稀少，不利于功能验证与演示
6. 业务列表页缺少行展开查看详情能力，房屋维修缺少进度时间轴

## What Changes
- 修复小区编辑表单 region 校验规则失效问题
- 小区编辑表单增加"物业公司联系电话"输入框，列表新增"联系电话"列
- 小区编辑表单经纬度/详细地址改为通过地图选点获取，地图类型通过 sys_config 配置（高德/腾讯）
- 完整清洗全国省市县区国标数据到 sys_region 表
- 修复电话簿分类树点击 400 错误，前端使用 phone1/phone2 双电话字段
- 新增种子数据 SQL：
  - 小区登记申请：约 15 条，apply_status='0'（未审批）
  - 电话簿申请：约 50 条
  - 电子合同：约 50 条
  - 轮播图：10 条
  - 邀请管理：约 50 条
  - 房屋维修：约 50 条
  - 消息管理：约 50 条
- 列表页新增行展开（折叠箭头），展开显示详细信息：
  - 小区、电话簿、电子合同、房屋维修均支持
  - 房屋维修展开时同时显示横向时间轴展示维修进度（提交→确认→完成→租户确认→报销）

## Impact
- Affected specs:
  - [extend-mp-rental-platform]（业务模块列表交互增强）
  - [miniapp-rental-enhance FR-3]（小区表单字段扩展）
- Affected code:
  - 前端：[community/index.vue](file:///workspace/ruoyi-ui/src/views/rental/community/index.vue)（修复 region 校验、增加 property_phone 字段、地图选点、行展开）
  - 前端：[phonebook/index.vue](file:///workspace/ruoyi-ui/src/views/rental/phonebook/index.vue)（修复分类点击、双电话字段、行展开）
  - 前端：[contract/index.vue](file:///workspace/ruoyi-ui/src/views/rental/contract/index.vue)（行展开）
  - 前端：[repair/index.vue](file:///workspace/ruoyi-ui/src/views/rental/repair/index.vue)（行展开 + 横向时间轴）
  - 数据库：sys_region 表全量清洗
  - 数据库：sys_config 新增 sys.community.mapProvider 配置项（下拉类型，高德/腾讯）
  - 数据库：biz_community、biz_phonebook_apply、biz_contract、biz_banner、biz_invite_relation、biz_house_repair、biz_user_message 表种子数据

## ADDED Requirements

### Requirement: 地图选点
系统 SHALL 在小区编辑表单中提供地图选点能力，用户在地图上点击或搜索地址后，自动填充详细地址、经度、纬度。

#### Scenario: 高德地图
- **WHEN** sys_config 中 sys.community.mapProvider 配置为"高德地图"
- **THEN** 小区编辑表单渲染高德地图组件，支持搜索与点击选点
- **AND** 选中后 address / longitude / latitude 字段自动填充

#### Scenario: 腾讯地图
- **WHEN** sys_config 中 sys.community.mapProvider 配置为"腾讯地图"
- **THEN** 小区编辑表单渲染腾讯地图组件，支持搜索与点击选点
- **AND** 选中后 address / longitude / latitude 字段自动填充

### Requirement: 行展开详情
系统 SHALL 在小区、电话簿、电子合同、房屋维修列表中支持行展开折叠，展开时展示该记录的详细信息字段。

#### Scenario: 展开小区行
- **WHEN** 用户点击小区列表行的折叠箭头
- **THEN** 展开显示省/市/区、详细地址、经纬度、物业公司、物业公司电话、标签、备注、创建人/时间等

#### Scenario: 展开房屋维修行
- **WHEN** 用户点击房屋维修列表行的折叠箭头
- **THEN** 展开显示维修详细信息（房屋、租户、房东、类型、描述、图片、状态、时间等）
- **AND** 在展开区底部显示横向时间轴，展示维修进度节点（提交→确认→完成→租户确认→报销）

### Requirement: 行政区划国标数据
系统 SHALL 在 sys_region 表中维护全国省市县区国标行政区划数据，覆盖 34 省级、所有市级、所有区县级。

#### Scenario: 省市级联
- **WHEN** 用户在小区编辑表单选择"山东省"
- **THEN** 市级下拉展示山东省下所有地级市
- **WHEN** 用户继续选择"聊城市"
- **THEN** 区县级下拉展示聊城市下所有区县

### Requirement: 种子数据
系统 SHALL 通过 SQL 脚本初始化以下种子数据用于功能验证与演示。

#### Scenario: 小区登记申请
- **WHEN** 种子脚本执行后
- **THEN** biz_community_apply 表存在约 15 条 apply_status='0' 的未审批申请

#### Scenario: 电话簿申请
- **WHEN** 种子脚本执行后
- **THEN** biz_phonebook_apply 表存在约 50 条申请数据

#### Scenario: 电子合同
- **WHEN** 种子脚本执行后
- **THEN** biz_contract 表存在约 50 条合同数据，覆盖不同状态

#### Scenario: 轮播图
- **WHEN** 种子脚本执行后
- **THEN** biz_banner 表存在 10 条轮播图数据

#### Scenario: 邀请管理
- **WHEN** 种子脚本执行后
- **THEN** biz_invite_relation 表存在约 50 条邀请关系数据

#### Scenario: 房屋维修
- **WHEN** 种子脚本执行后
- **THEN** biz_house_repair 表存在约 50 条维修数据，覆盖不同状态

#### Scenario: 消息管理
- **WHEN** 种子脚本执行后
- **THEN** biz_user_message 表存在约 50 条消息数据

## MODIFIED Requirements

### Requirement: 小区表单校验
小区编辑表单的"所在地区"校验规则 SHALL 基于 formRegion 数组长度判断，而非 form.region 字段；当 formRegion 长度大于 0 时校验通过。

#### Scenario: 选择省直县市
- **WHEN** 用户在小区编辑表单级联选择"北京市 / 市辖区"（仅两级，无第三级）
- **THEN** formRegion 长度为 2，校验通过，不再提示"所在地区不能为空"

### Requirement: 电话簿分类查询
电话簿分类树点击 SHALL 不再触发 400 错误，点击节点后按分类筛选列表正常返回数据。

#### Scenario: 点击分类节点
- **WHEN** 用户点击电话簿左侧分类树中的"餐饮美食"
- **THEN** 右侧列表刷新，仅显示 category='餐饮美食' 的商家
- **AND** 后端返回 200，无 400 错误

### Requirement: 电话簿双电话字段
电话簿编辑表单 SHALL 提供两个电话输入框（phone1、phone2），phone1 必填，phone2 选填；列表展示两个电话（用 / 分隔）。

#### Scenario: 维护双电话
- **WHEN** 用户在电话簿编辑表单填写两个手机号
- **THEN** 提交时 phone1、phone2 字段分别保存
- **AND** 列表"电话"列显示为 "13800000001 / 13800000002"

## REMOVED Requirements
（无移除项）
