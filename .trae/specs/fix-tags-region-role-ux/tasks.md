# 租赁平台标签/区划/角色/维修优化 - The Implementation Plan (Decomposed and Prioritized Task List)

## [x] Task 1: 修复标签选择无反应 Bug（小区/房屋/用户）
- **Priority**: high
- **Depends On**: None
- **Description**:
  - 排查并修复小区、房屋、用户三个模块编辑对话框中标签 el-select multiple 选择无反应的问题
  - 可能原因：el-dialog append-to-body 导致 popper 层级问题，或 v-model 绑定问题，或事件冒泡
  - 修复方案：为 el-select 添加 :popper-append-to-body="false" 或调整 z-index，或检查 tagsList 绑定
  - 三个页面统一修复：community/index.vue、house/index.vue、miniapp/user/index.vue
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: 小区编辑对话框中，点击标签下拉框能正常展开，选择标签后 tagsList 正确更新
  - `programmatic` TR-1.2: 房屋编辑对话框中，标签多选功能正常
  - `programmatic` TR-1.3: 用户编辑对话框中，标签多选功能正常
  - `programmatic` TR-1.4: 选中标签后提交保存，数据库 tags 字段正确写入逗号分隔字符串
  - `programmatic` TR-1.5: 编辑回显时，已选标签正确显示在多选框中
- **Notes**: 先确认具体原因再修复，优先使用最简单的方案

## [x] Task 2: 标签样式美化（DictTag 组件）
- **Priority**: medium
- **Depends On**: None
- **Description**:
  - 优化 DictTag 组件的视觉样式：增加圆角、渐变/纯色背景、优化间距
  - 调整标签颜色搭配，使不同类型标签有更明显的视觉区分
  - 增加悬停效果（如阴影、缩放）
  - 保持与 Element UI 整体设计风格一致
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `human-judgement` TR-2.1: 小区列表标签列样式美观，圆角适中，颜色协调
  - `human-judgement` TR-2.2: 房屋列表标签列样式美观
  - `human-judgement` TR-2.3: 用户列表标签列样式美观
  - `human-judgement` TR-2.4: 标签间距合适，不拥挤不松散
  - `human-judgement` TR-2.5: 与整体 UI 风格协调一致
- **Notes**: 修改 DictTag 组件即可，所有使用 DictTag 的地方自动生效

## [x] Task 3: 角色管理行展开显示用户列表
- **Priority**: high
- **Depends On**: None
- **Description**:
  - role/index.vue 列表增加 type="expand" 列
  - 展开时调用 authUser/allocatedList 接口加载该角色下的用户列表
  - 展示信息：头像、用户名、昵称、手机号、用户类型、状态
  - 使用 el-table 或 el-descriptions 展示
  - 限制每页展示 10 条，支持简单分页或"查看更多"
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-3.1: 角色列表每行有展开箭头
  - `programmatic` TR-3.2: 点击展开后正确加载该角色的用户列表
  - `programmatic` TR-3.3: 用户列表显示头像、用户名、手机号等关键信息
  - `programmatic` TR-3.4: 展开收起功能正常，无闪烁或错位
- **Notes**: 使用已有的 authUser/allocatedList 接口（参数 roleId、pageNum、pageSize）

## [x] Task 4: 小程序用户推送消息 - 已推送列表与已读状态
- **Priority**: high
- **Depends On**: None
- **Description**:
  - miniapp/user/index.vue 增加"已推送消息"功能入口（行展开区域或独立按钮+弹窗）
  - 调用 listMessage 接口，按 userId 筛选查询该用户的消息列表
  - 列表展示：消息标题、内容（截断+展开）、发送时间、是否已读（0未读/1已读）、阅读时间
  - 已读/未读使用不同样式区分（如未读加粗+圆点标记）
  - 消息 API 接口增加 userId 查询参数支持（如需要）
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `programmatic` TR-4.1: 用户行展开/点击后能看到已推送消息列表
  - `programmatic` TR-4.2: 消息列表正确显示标题、内容、发送时间
  - `programmatic` TR-4.3: 已读/未读状态正确显示，样式有区分
  - `programmatic` TR-4.4: 阅读时间字段正确显示（已读消息）
- **Notes**: 先确认后端 listMessage 接口是否支持 userId 参数查询，如不支持需添加

## [x] Task 5: 行政区划管理懒加载优化
- **Priority**: high
- **Depends On**: None
- **Description**:
  - region/index.vue 树形表格改为懒加载模式（lazy 属性 + load 方法）
  - 初始仅加载省级数据（region_level=1 或 parent_id=0）
  - 点击展开节点时，调用 listRegionByParentId 接口加载子节点
  - 叶子节点（区县 level=3）不显示展开箭头
  - 保留搜索功能：有搜索关键词时全量加载，无搜索时懒加载
  - hasChildren 字段由后端返回或前端根据 level 判断
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `programmatic` TR-5.1: 页面初始加载仅请求省级数据（约 34 条），首屏加载更快
  - `programmatic` TR-5.2: 点击省级行展开，加载该省下的市级数据
  - `programmatic` TR-5.3: 点击市级行展开，加载该市下的区县数据
  - `programmatic` TR-5.4: 区县级节点无展开箭头（无子节点）
  - `programmatic` TR-5.5: 搜索功能正常，输入区划名称能正确筛选
- **Notes**: 后端 listByParentId 接口已存在，直接使用

## [x] Task 6: 房屋维修按钮修复 + 横向时间轴
- **Priority**: high
- **Depends On**: None
- **Description**:
  - 修复 repair.js 中 API 路径与后端不匹配的问题
  - 当前前端调用路径 vs 后端实际路径：
    - confirmByLandlord → landlordConfirm
    - chooseTenantSelfRepair → landlordChooseTenantRepair
    - completeRepair → landlordComplete
    - confirmByTenant → tenantConfirm
    - uploadVoucher → tenantUploadReceipt
    - confirmReimburse → landlordConfirmReimburse
  - 统一前端 API 路径与后端保持一致
  - 维修进度时间轴改为横向布局（el-timeline 横向 + overflow-x: auto）
  - 保持时间节点状态颜色（已完成绿色，待处理灰色）
- **Acceptance Criteria Addressed**: AC-6, AC-7
- **Test Requirements**:
  - `programmatic` TR-6.1: "房东确认维修"按钮点击后请求成功，状态从 0 变 1
  - `programmatic` TR-6.2: "租客自修"按钮点击后请求成功，状态从 0 变 4
  - `programmatic` TR-6.3: "完成维修"、"租客确认完成"、"上传凭证"、"确认报销"按钮均正常工作
  - `programmatic` TR-6.4: 维修展开详情中时间轴横向展示
  - `human-judgement` TR-6.5: 横向时间轴视觉效果良好，节点状态颜色正确
- **Notes**: 修复 API 路径是核心，时间轴横向已在上一轮部分实现，需确认并完善

# Task Dependencies
- Task 1、Task 2、Task 3、Task 4、Task 5、Task 6 相互独立，可并行
- Task 2 依赖 Task 1 的代码结构理解，但实际可独立进行（改不同文件）
