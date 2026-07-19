# 小程序租赁平台增强功能 - The Implementation Plan (Decomposed and Prioritized Task List)

## [x] Task 1: 省市县行政区划管理（后端）
- **Priority**: high
- **Depends On**: None
- **Description**:
  - 创建 sys_region 表及 SQL 初始化数据（山东省聊城市阳谷县示例数据）
  - 创建 SysRegion 实体类、Mapper、Service、Controller
  - 支持树形列表查询、增删改查、小区登记开关设置
  - 提供按父ID查询子级行政区划的接口
- **Acceptance Criteria Addressed**: [AC-1]
- **Test Requirements**:
  - `programmatic` TR-1.1: 树形列表接口返回正确的三级行政区划结构
  - `programmatic` TR-1.2: 新增/编辑/删除行政区划接口正常工作
  - `programmatic` TR-1.3: 小区登记开关可以正常切换
  - `programmatic` TR-1.4: 根据父ID查询子级行政区划接口正确返回

## [x] Task 2: 省市县行政区划管理（前端）
- **Priority**: high
- **Depends On**: Task 1
- **Description**:
  - 创建行政区划管理页面（system/region/index.vue）
  - 树形表格展示省市区三级数据
  - 支持新增、编辑、删除、设置小区登记开关
  - 配置菜单和权限
- **Acceptance Criteria Addressed**: [AC-1]
- **Test Requirements**:
  - `human-judgement` TR-2.1: 页面正确展示树形结构
  - `human-judgement` TR-2.2: 增删改查操作流畅
  - `programmatic` TR-2.3: 小区登记开关切换后数据正确更新

## [x] Task 3: 小区登记地区校验与默认值
- **Priority**: high
- **Depends On**: Task 1
- **Description**:
  - 小区表和小区申请表的 province/city/district 改为存储编码
  - 小区登记页面省市县默认选中 山东省/聊城市/阳谷县
  - 提交时校验小区登记开关，关闭则提示错误
  - 列表和详情展示时编码转换为名称
- **Acceptance Criteria Addressed**: [AC-2, AC-3]
- **Test Requirements**:
  - `programmatic` TR-3.1: 小区登记页面默认省市县正确
  - `programmatic` TR-3.2: 开关关闭时提交返回错误提示
  - `programmatic` TR-3.3: 列表展示时编码正确转换为名称

## [x] Task 4: 字典标签体系扩展
- **Priority**: medium
- **Depends On**: None
- **Description**:
  - 新增四类字典数据初始化 SQL（用户标签、房屋标签、小区标签、商家标签）
  - 房屋、小区、小程序用户实体增加 tags 字段
  - 数据库表增加对应字段
  - 编辑页面增加标签选择器（多选）
  - 列表和详情页展示标签
- **Acceptance Criteria Addressed**: [AC-4]
- **Test Requirements**:
  - `programmatic` TR-4.1: 四类字典数据正确初始化
  - `programmatic` TR-4.2: 标签保存和读取正确
  - `human-judgement` TR-4.3: 列表和详情页标签展示美观

## [x] Task 5: 有效期与次数管理 - 数据模型
- **Priority**: high
- **Depends On**: None
- **Description**:
  - 房屋表增加 valid_until 字段
  - 电话簿表增加 valid_until 字段
  - 小程序用户表增加 publish_count、publish_period_end 字段
  - 对应实体类增加字段
- **Acceptance Criteria Addressed**: [AC-5]
- **Test Requirements**:
  - `programmatic` TR-5.1: 数据库表字段正确添加
  - `programmatic` TR-5.2: 实体类字段正确映射

## [x] Task 6: 有效期与次数管理 - 业务逻辑
- **Priority**: high
- **Depends On**: Task 5
- **Description**:
  - 房屋发布时消耗发布次数，次数不足时提示
  - 房屋/电话簿超过有效期自动下架（查询时过滤）
  - 后台用户管理增加手动调整次数和有效期功能
  - 商城兑换虚拟商品延长次数/有效期（支持叠加）
- **Acceptance Criteria Addressed**: [AC-5, AC-6, AC-7]
- **Test Requirements**:
  - `programmatic` TR-6.1: 发布房屋时正确扣减次数
  - `programmatic` TR-6.2: 过期内容在列表中不显示
  - `programmatic` TR-6.3: 后台手动调整功能正常
  - `programmatic` TR-6.4: 商城兑换后次数/有效期正确增加并叠加

## [x] Task 7: 小程序用户消息推送
- **Priority**: medium
- **Depends On**: None
- **Description**:
  - 创建 biz_user_message 表
  - 创建消息相关的实体、Mapper、Service、Controller
  - 管理后台用户列表增加"推送消息"按钮和弹窗
  - 提供小程序端消息列表、标记已读接口
- **Acceptance Criteria Addressed**: [AC-8]
- **Test Requirements**:
  - `programmatic` TR-7.1: 后台推送消息成功写入数据库
  - `programmatic` TR-7.2: 小程序端消息列表接口正常
  - `programmatic` TR-7.3: 消息已读状态正确更新

## [x] Task 8: 小区审批页面优化（折叠详情 + 时间轴）
- **Priority**: medium
- **Depends On**: None
- **Description**:
  - 小区登记审批列表左侧增加折叠按钮
  - 展开显示详细信息区域
  - 详情区域增加横向时间轴组件
  - 时间轴展示：提交→待审批→通过/驳回 各节点
- **Acceptance Criteria Addressed**: [AC-9]
- **Test Requirements**:
  - `human-judgement` TR-8.1: 折叠展开交互流畅
  - `human-judgement` TR-8.2: 时间轴展示清晰美观
  - `programmatic` TR-8.3: 时间轴节点数据正确

## [x] Task 9: 房屋审批页面优化（折叠详情 + 时间轴）
- **Priority**: medium
- **Depends On**: Task 8
- **Description**:
  - 房屋登记审批页（如不存在则新增）同样增加折叠详情和时间轴
  - 复用小区审批页面的组件和样式
- **Acceptance Criteria Addressed**: [AC-9]
- **Test Requirements**:
  - `human-judgement` TR-9.1: 折叠展开交互流畅
  - `human-judgement` TR-9.2: 时间轴展示清晰美观
  - `programmatic` TR-9.3: 时间轴节点数据正确

## [x] Task 10: 小程序用户详情折叠
- **Priority**: low
- **Depends On**: None
- **Description**:
  - 用户列表增加折叠/展开按钮
  - 折叠状态：头像、昵称、用户类型、状态
  - 展开状态：显示手机号、认证状态、真实姓名、身份证号、登录IP、登录时间等
- **Acceptance Criteria Addressed**: [AC-10]
- **Test Requirements**:
  - `human-judgement` TR-10.1: 折叠展开交互流畅
  - `human-judgement` TR-10.2: 展开后信息展示完整

## [x] Task 11: 电话簿商家分类树
- **Priority**: medium
- **Depends On**: None
- **Description**:
  - 电话簿管理页面左侧增加商家分类树
  - 使用字典 biz_merchant_category 管理分类（或新增表）
  - 点击分类节点筛选右侧列表
  - 支持"全部"选项
- **Acceptance Criteria Addressed**: [AC-11]
- **Test Requirements**:
  - `programmatic` TR-11.1: 分类树正确展示所有分类
  - `programmatic` TR-11.2: 点击分类后列表正确筛选
  - `human-judgement` TR-11.3: 左右布局美观合理

## [x] Task 12: 轮播图管理（后端）
- **Priority**: medium
- **Depends On**: None
- **Description**:
  - 创建 biz_banner 表
  - 创建 BizBanner 实体、Mapper、Service、Controller
  - 管理后台 CRUD 接口
  - 小程序端查询有效轮播图接口（按有效期和状态过滤）
- **Acceptance Criteria Addressed**: [AC-12]
- **Test Requirements**:
  - `programmatic` TR-12.1: 轮播图 CRUD 接口正常
  - `programmatic` TR-12.2: 小程序端接口只返回有效期内且启用的轮播图

## [x] Task 13: 轮播图管理（前端）
- **Priority**: medium
- **Depends On**: Task 12
- **Description**:
  - 创建轮播图管理页面（rental/banner/index.vue）
  - 支持增删改查、图片上传、有效期设置
  - 配置菜单和权限
- **Acceptance Criteria Addressed**: [AC-12]
- **Test Requirements**:
  - `human-judgement` TR-13.1: 页面布局美观
  - `programmatic` TR-13.2: 图片上传功能正常
  - `programmatic` TR-13.3: 有效期设置正确保存

## [x] Task 14: 字典管理界面优化
- **Priority**: medium
- **Depends On**: None
- **Description**:
  - 字典类型列表每行左侧增加折叠按钮
  - 点击展开显示该字典的明细数据
  - 明细默认显示 10 条
  - 可选择每页显示数量（10/20/50/100）
  - 明细支持上一页、下一页分页
  - 明细区域支持快速添加字典数据
- **Acceptance Criteria Addressed**: [AC-13]
- **Test Requirements**:
  - `human-judgement` TR-14.1: 折叠展开交互流畅
  - `programmatic` TR-14.2: 明细分页功能正常
  - `programmatic` TR-14.3: 每页数量切换功能正常
  - `programmatic` TR-14.4: 快速添加功能正常

## [x] Task 15: 参数设置键选项配置（后端）
- **Priority**: medium
- **Depends On**: None
- **Description**:
  - sys_config 表增加 config_options 字段
  - SysConfig 实体增加对应字段
  - Mapper 和 Service 适配新字段
  - 编写数据清洗 SQL，解析现有参数备注填充 config_options
- **Acceptance Criteria Addressed**: [AC-14, AC-15]
- **Test Requirements**:
  - `programmatic` TR-15.1: config_options 字段正确保存和读取
  - `programmatic` TR-15.2: 数据清洗 SQL 正确填充已有参数的选项

## [x] Task 16: 参数设置键选项配置（前端）
- **Priority**: medium
- **Depends On**: Task 15
- **Description**:
  - 参数编辑表单增加"参数键选项"配置项
  - 参数键值输入框：有选项时显示下拉选择，无选项时显示文本输入
  - 选项配置支持动态增删选项行（参数名 + 参数值）
- **Acceptance Criteria Addressed**: [AC-14]
- **Test Requirements**:
  - `programmatic` TR-16.1: 有选项的参数显示下拉选择
  - `programmatic` TR-16.2: 选项配置可以动态增删
  - `programmatic` TR-16.3: 保存后选项数据正确

## [x] Task 17: 整合测试与菜单配置
- **Priority**: high
- **Depends On**: Task 2, Task 13, Task 14, Task 16
- **Description**:
  - 配置所有新增页面的菜单和权限
  - 端到端测试所有功能
  - 修复发现的问题
- **Acceptance Criteria Addressed**: [AC-1 ~ AC-15]
- **Test Requirements**:
  - `human-judgement` TR-17.1: 所有菜单正确显示且可访问
  - `programmatic` TR-17.2: 所有功能通过冒烟测试
