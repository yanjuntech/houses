# Checklist

## 数据库与菜单
- [x] `sql/miniapp_rental.sql` 包含所有业务表 DDL（biz_miniapp_user、biz_community、biz_community_apply、biz_house、biz_phonebook、biz_phonebook_apply、biz_contract、biz_contract_signature）
- [x] SQL 脚本包含 `sys_menu` 插入语句，覆盖业务菜单与按钮权限
- [x] SQL 脚本在 `ry-vue` 数据库中执行成功，所有表与菜单记录创建完成
- [x] 字段命名遵循若依规范（create_by/create_time/update_by/update_time/remark 等公共字段齐全）

## 小程序用户管理
- [ ] BizMiniappUser 实体使用 @Data + @ApiModelProperty 注解
- [x] 实现手机号 + 验证码登录接口，返回 JWT token
- [x] 实现微信登录（openid）+ 绑定手机号接口，未绑定时返回需要绑定状态
- [x] 实现用户分页查询、详情、修改身份标签（房东/中介）接口
- [x] 实现实名认证审核接口（状态：未认证/待审核/已认证/已拒绝）
- [x] 所有后台接口添加 @PreAuthorize 权限注解

## 小区管理与申请审批
- [ ] BizCommunity、BizCommunityApply 实体使用 @Data + @ApiModelProperty
- [x] 实现小区 CRUD + 分页查询接口
- [x] 实现用户申请登记小区接口（状态：待审批）
- [x] 实现管理员审批通过接口（通过后写入 biz_community 表）
- [x] 实现管理员驳回接口（填写驳回原因）

## 房屋管理
- [ ] BizHouse 实体使用 @Data + @ApiModelProperty
- [x] 实现多维度筛选分页查询（租赁类型/状态/小区/户型组合）
- [x] 实现房源详情、新增、修改、删除接口
- [x] 实现上下架操作接口（更新 status 字段）
- [x] 查询结果返回发布者身份标签（房东直租/房屋中介）
- [x] 所有接口添加 @PreAuthorize 权限注解

## 电话簿管理与申请审批
- [ ] BizPhonebook、BizPhonebookApply 实体使用 @Data + @ApiModelProperty
- [x] 实现商家电话 CRUD + 按分类分页查询
- [x] 实现用户申请收录接口（含营业执照图片、店主信息）
- [x] 实现管理员审批通过/驳回接口

## 电子合同
- [ ] BizContract、BizContractSignature 实体使用 @Data + @ApiModelProperty
- [x] 实现发起合同接口（关联房源、双方用户、租金、租期）
- [x] 实现签名图片上传接口
- [x] 实现完成签署接口（状态变更 + 记录签署时间）
- [x] 实现 PDF 下载接口（含合同内容 + 签名图片渲染）
- [ ] PDF 文件能正常下载并打开

## 前端页面
- [x] 创建所有 API 接口文件（miniapp/user、rental/community、rental/house、rental/phonebook、rental/contract）
- [x] 小程序用户管理页面：列表、身份标签修改、实名认证审核
- [x] 小区管理页面：列表、新增、编辑、删除
- [x] 小区申请审批页面：待审批列表、通过/驳回操作
- [x] 房屋管理页面：多维度筛选、上下架、详情编辑
- [x] 电话簿管理页面：列表、分类筛选、新增、编辑
- [x] 商家申请审批页面：待审批列表、营业执照查看、通过/驳回
- [x] 电子合同管理页面：合同列表、详情查看、PDF 下载按钮

## 联调验证
- [x] 后端服务启动无报错（端口 8080）
- [x] 前端服务启动无报错（端口 8081）
- [x] 若依后台菜单正确显示新增的业务菜单
- [x] 权限控制生效（无权限用户无法访问对应菜单和接口）
- [ ] 完整业务流程跑通：用户登录→申请小区→审批→发布房源→发起合同→签署→下载 PDF
