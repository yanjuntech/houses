# 租赁平台合同/地图/标签/小区/邀请/自动化测试优化 - The Implementation Plan (Decomposed and Prioritized Task List)

## [x] Task 1: 电子合同支付周期字典化
- **Priority**: high
- **Depends On**: None
- **Description**:
  - 在 sys_dict_type 和 sys_dict_data 中新增字典类型 `biz_contract_pay_cycle`
  - 字典项：押一付一(0)、押一付二(1)、押一付三(2)、押一付六(3)、季付(4)、半年付(5)、年付(6)
  - contract/index.vue 编辑表单中 payCycle 从 el-input 改为 el-select 下拉选择
  - contract/index.vue 列表支付周期列改用 dict-tag 展示
  - contract/index.vue 详情页支付周期改用 dict-tag 展示
  - dicts 配置新增 'biz_contract_pay_cycle'
- **Acceptance Criteria Addressed**: AC-1, AC-2
- **Test Requirements**:
  - `programmatic` TR-1.1: sys_dict_data 表中存在 biz_contract_pay_cycle 字典类型及 7 个字典项
  - `programmatic` TR-1.2: 合同编辑对话框中支付周期为下拉选择框
  - `programmatic` TR-1.3: 合同列表支付周期列显示为 dict-tag 标签样式
  - `programmatic` TR-1.4: 合同详情页支付周期显示为 dict-tag 标签样式
  - `programmatic` TR-1.5: 选择支付周期后能正确保存到数据库
- **Notes**: 如果已有合同数据 payCycle 存的是文本值，需要考虑数据迁移；本任务暂不做数据迁移

## [x] Task 2: 小区地图显示优化
- **Priority**: high
- **Depends On**: None
- **Description**:
  - 在 sys_config 表中新增两个配置项：
    - `sys.community.mapKeyAmap` - 高德地图 API Key
    - `sys.community.mapKeyTencent` - 腾讯地图 API Key
  - AMapPicker 组件：从 sys_config 读取 API Key，不再硬编码
  - TMapPicker 组件：从 sys_config 读取 API Key，不再硬编码
  - 增加地图加载状态（loading 动画）
  - 地图加载失败时显示友好提示（提示文字 + 手动输入经纬度说明）
  - 地图加载失败不影响表单其他字段的填写和保存
- **Acceptance Criteria Addressed**: AC-3, AC-4
- **Test Requirements**:
  - `programmatic` TR-2.1: sys_config 表中存在 sys.community.mapKeyAmap 和 sys.community.mapKeyTencent 配置项
  - `programmatic` TR-2.2: AMapPicker 组件从配置读取 API Key
  - `programmatic` TR-2.3: TMapPicker 组件从配置读取 API Key
  - `programmatic` TR-2.4: 地图加载失败时有明确的错误提示信息
  - `programmatic` TR-2.5: 地图加载失败时仍可手动填写经纬度和地址
- **Notes**: 使用 getConfigKey 接口读取配置，组件 mounted 时先获取 Key 再加载地图脚本

## [x] Task 3: 小程序用户标签显示修复
- **Priority**: high
- **Depends On**: None
- **Description**:
  - 排查用户标签列表不能正确显示的原因（存储值 vs 字典值不匹配）
  - 确认 biz_miniapp_user.tags 字段存储的是字典值（0,1,2,3）还是标签名称
  - 如果存储的是名称，改为存储字典值（前后端联动修改）
  - 确保列表标签列 dict-tag 正确渲染
  - 确保编辑回显时 tagsList 正确赋值
  - 确保保存时 tagsList 正确 join 为逗号分隔字符串
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `programmatic` TR-3.1: 用户列表标签列正确显示 dict-tag 标签
  - `programmatic` TR-3.2: 编辑用户时已选标签正确回显
  - `programmatic` TR-3.3: 修改标签后保存，数据库 tags 字段正确更新
  - `programmatic` TR-3.4: 行展开详情中用户标签正确显示
- **Notes**: 需要先检查现有数据格式，再决定修复方案

## [x] Task 4: 房屋管理小区选择支持输入匹配
- **Priority**: medium
- **Depends On**: None
- **Description**:
  - house/index.vue 查询条件的小区 el-select 添加 filterable 属性
  - house/index.vue 编辑表单的小区 el-select 添加 filterable 属性
  - 保持原有 @change 事件和功能不变
- **Acceptance Criteria Addressed**: AC-6
- **Test Requirements**:
  - `programmatic` TR-4.1: 查询条件小区下拉框支持输入过滤
  - `programmatic` TR-4.2: 编辑表单小区下拉框支持输入过滤
  - `programmatic` TR-4.3: 输入文字后下拉选项正确过滤匹配
  - `programmatic` TR-4.4: 原有选择功能不受影响
- **Notes**: 简单改动，仅添加 filterable 属性即可

## [x] Task 5: 邀请人管理展示优化
- **Priority**: medium
- **Depends On**: None
- **Description**:
  - 修复邀请状态显示为数字的问题（确认 dict-tag 的 value 类型匹配）
  - 列表增加 type="expand" 行展开，展示详细信息
  - 优化顶部统计卡片样式（美化边框、背景色、图标）
  - 增加认证率展示（已认证数/总邀请数 百分比）
  - 增加最近 7 天邀请趋势（简单柱状图或数字列表）
  - 优化整体页面间距和对齐
- **Acceptance Criteria Addressed**: AC-7, AC-8
- **Test Requirements**:
  - `programmatic` TR-5.1: 邀请状态列显示为 dict-tag 标签（文字而非数字）
  - `programmatic` TR-5.2: 列表有行展开功能，展开后显示详细信息
  - `programmatic` TR-5.3: 顶部统计卡片包含总邀请数、已认证数、认证率
  - `human-judgement` TR-5.4: 页面整体视觉效果美观，间距对齐合理
  - `human-judgement` TR-5.5: 统计卡片样式美观（背景色、圆角、图标）
- **Notes**: 状态为数字的问题可能是因为 invite_status 字段是数字类型而 dict_value 是字符串，需要做类型转换

## [x] Task 6: 业务管理全流程自动化测试
- **Priority**: high
- **Depends On**: Task 1, Task 2, Task 3, Task 4, Task 5
- **Description**:
  - 使用 agent-browser 内置浏览器进行端到端测试
  - 登录系统后，测试业务管理下所有菜单页面
  - 测试页面列表：
    1. 小区管理 - 搜索、展开、新增/修改弹窗打开、删除确认
    2. 房屋管理 - 搜索、展开、新增/修改弹窗打开、小区下拉过滤
    3. 电子合同 - 搜索、展开、新增/修改弹窗打开、支付周期下拉
    4. 房屋维修 - 搜索、展开、各状态按钮点击
    5. 电话簿管理 - 分类点击、搜索、展开
    6. 轮播图管理 - 搜索、新增/修改弹窗打开
    7. 邀请管理 - 搜索、展开、详情弹窗
    8. 消息管理 - 搜索、展开
    9. 小区登记申请 - 搜索、审批操作
    10. 电话簿申请 - 搜索、审批操作
    11. 小程序用户 - 搜索、展开、推送消息弹窗、推送记录弹窗
  - 每个页面至少测试 3 个交互点
  - 记录每个操作的结果（成功/失败/错误信息）
  - 生成测试报告（Markdown 格式）
  - 验证不出现 400/404/500 等错误提示
- **Acceptance Criteria Addressed**: AC-9, AC-10
- **Test Requirements**:
  - `programmatic` TR-6.1: 测试覆盖至少 10 个业务管理页面
  - `programmatic` TR-6.2: 每个页面至少测试 3 个交互点
  - `programmatic` TR-6.3: 生成完整的测试报告（Markdown 格式）
  - `programmatic` TR-6.4: 所有测试操作不出现 400/404/500 错误弹窗
  - `programmatic` TR-6.5: 如发现 Bug，记录在报告中并附截图证据
- **Notes**: 
  - 测试前确保后端和前端服务都在运行
  - 使用 admin/admin123 登录
  - 测试报告保存到 /workspace/dogfood-output/ 目录
  - 发现 Bug 不阻塞测试继续，全部记录在报告中

# Task Dependencies
- Task 1、Task 2、Task 3、Task 4、Task 5 相互独立，可并行
- Task 6 依赖 Task 1-5 全部完成（先修复再测试）
