# Tasks

- [x] Task 1: 创建数据库脚本与菜单权限初始化
  - [x] SubTask 1.1: 创建 `sql/miniapp_rental.sql`，定义业务表（biz_miniapp_user、biz_community、biz_community_apply、biz_house、biz_phonebook、biz_phonebook_apply、biz_contract、biz_contract_signature）
  - [x] SubTask 1.2: 在 SQL 中插入若依菜单记录（小程序用户管理、房屋租赁、小区管理、小区申请审批、房屋管理、电话簿管理、商家申请审批、电子合同），并配置按钮权限
  - [x] SubTask 1.3: 执行 SQL 脚本到 `ry-vue` 数据库，验证表与菜单创建成功

- [x] Task 2: 实现小程序用户管理模块（后端）
  - [x] SubTask 2.1: 在 `ruoyi-system` 创建 domain（BizMiniappUser，使用 @Data）、mapper、service、serviceImpl
  - [x] SubTask 2.2: 在 `ruoyi-admin` 创建 BizMiniappUserController，实现分页查询、详情、修改身份标签、实名认证审核接口，添加 @PreAuthorize 权限
  - [x] SubTask 2.3: 实现小程序端登录认证接口（手机号+验证码登录、微信登录绑定手机号），返回 JWT token

- [x] Task 3: 实现小区管理与申请审批模块（后端）
  - [x] SubTask 3.1: 创建 domain（BizCommunity、BizCommunityApply）+ mapper + service + serviceImpl
  - [x] SubTask 3.2: 创建 BizCommunityController（小区 CRUD、分页查询）
  - [x] SubTask 3.3: 创建 BizCommunityApplyController（用户申请登记、管理员审批通过/驳回接口）

- [x] Task 4: 实现房屋管理模块（后端）
  - [x] SubTask 4.1: 创建 domain（BizHouse）+ mapper + service + serviceImpl，支持多维度筛选查询（租赁类型、状态、小区、户型）
  - [x] SubTask 4.2: 创建 BizHouseController（分页查询、详情、新增、修改、上下架操作、删除），添加 @PreAuthorize 权限
  - [x] SubTask 4.3: 关联发布者用户身份标签（房东直租/房屋中介）返回前端

- [x] Task 5: 实现电话簿管理与申请审批模块（后端）
  - [x] SubTask 5.1: 创建 domain（BizPhonebook、BizPhonebookApply）+ mapper + service + serviceImpl
  - [x] SubTask 5.2: 创建 BizPhonebookController（商家电话 CRUD、按分类分页查询）
  - [x] SubTask 5.3: 创建 BizPhonebookApplyController（用户申请收录含营业执照、管理员审批通过/驳回）

- [x] Task 6: 实现电子合同签署与 PDF 下载模块（后端）
  - [x] SubTask 6.1: 创建 domain（BizContract、BizContractSignature）+ mapper + service + serviceImpl
  - [x] SubTask 6.2: 创建 BizContractController（发起合同、查询详情、上传签名图片、完成签署、下载 PDF）
  - [x] SubTask 6.3: 集成 PDF 生成（iText 7 + font-asian 中文字体），将合同内容 + 签名图片渲染为 PDF

- [x] Task 7: 实现前端业务页面（ruoyi-ui）
  - [x] SubTask 7.1: 创建 `src/api/miniapp/user.js`、`src/api/rental/community.js`、`src/api/rental/house.js`、`src/api/rental/phonebook.js`、`src/api/rental/phonebookApply.js`、`src/api/rental/contract.js` 接口文件
  - [x] SubTask 7.2: 创建小程序用户管理页面 `src/views/miniapp/user/index.vue`（列表、身份标签修改、实名认证审核）
  - [x] SubTask 7.3: 创建小区管理页面 `src/views/rental/community/index.vue` 与小区申请审批页面 `src/views/rental/community/apply.vue`
  - [x] SubTask 7.4: 创建房屋管理页面 `src/views/rental/house/index.vue`（多维度筛选、上下架、详情编辑）
  - [x] SubTask 7.5: 创建电话簿管理页面 `src/views/rental/phonebook/index.vue` 与商家申请审批页面 `src/views/rental/phonebook/apply.vue`
  - [x] SubTask 7.6: 创建电子合同管理页面 `src/views/rental/contract/index.vue`（合同列表、详情、PDF 下载）

- [x] Task 8: 联调与验证
  - [x] SubTask 8.1: 启动后端（java -jar ruoyi-admin.jar）与前端（npm run dev），确认服务正常
  - [x] SubTask 8.2: 验证菜单显示、接口权限控制、业务流程（用户登录→菜单显示→小区列表→房屋列表含身份标签→小程序用户列表）

# Task Dependencies
- [Task 2/3/4/5/6] 依赖 [Task 1] 完成数据库表和菜单初始化
- [Task 7] 依赖 [Task 2-6] 后端接口实现完成
- [Task 8] 依赖 [Task 7] 前端页面实现完成
- [Task 2/3/4/5/6] 之间无依赖，可并行实现
