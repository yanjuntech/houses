# 小程序后台服务全对接测试验证 - The Implementation Plan

## [ ] Task 1: 检查后端服务状态
- **Priority**: high
- **Depends On**: None
- **Description**: 
  - 检查后端 Spring Boot 服务是否正在运行
  - 确认服务端口（默认 8080）是否可达
- **Acceptance Criteria Addressed**: AC-1 至 AC-20
- **Test Requirements**:
  - `programmatic` TR-1.1: 后端服务端口 8080 可访问
  - `programmatic` TR-1.2: 健康检查接口返回成功

## [ ] Task 2: 验证小区下拉选择功能
- **Priority**: high
- **Depends On**: Task 1
- **Description**: 
  - 检查发布房源页面（house/publish.js）的小区选择功能
  - 验证 `communityApi.selectAll()` 调用 `/rental/community/selectAll` 接口
  - 确认页面加载时小区列表正常显示
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-2.1: 页面加载时调用小区列表接口
  - `programmatic` TR-2.2: 小区下拉选择器包含"请选择小区"占位项
  - `programmatic` TR-2.3: 选择小区后 form.communityId 正确赋值

## [ ] Task 3: 验证登录功能
- **Priority**: high
- **Depends On**: Task 1
- **Description**: 
  - 检查登录页面（login/login.js）的手机号登录和微信登录
  - 验证 API 路径与后端一致
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-3.1: `loginByPhone` 调用 `/miniapp/user/loginByPhone`
  - `programmatic` TR-3.2: `loginByWechatCode` 调用 `/miniapp/user/loginByWechatCode`
  - `programmatic` TR-3.3: `updateProfile` 调用 `/miniapp/user/updateProfile`

## [ ] Task 4: 验证房屋管理功能
- **Priority**: high
- **Depends On**: Task 1
- **Description**: 
  - 检查房屋列表、详情、发布页面
  - 验证 API 路径与后端 `/miniapp/*` 格式一致
- **Acceptance Criteria Addressed**: AC-3, AC-4, AC-5
- **Test Requirements**:
  - `programmatic` TR-4.1: `houseApi.list` 调用 `/rental/house/miniapp/list`
  - `programmatic` TR-4.2: `houseApi.detail` 调用 `/rental/house/miniapp/{houseId}`
  - `programmatic` TR-4.3: `houseApi.add` 调用 `/rental/house/miniapp/publish`
  - `programmatic` TR-4.4: 发布页面配额校验功能正常

## [ ] Task 5: 验证维修管理功能
- **Priority**: high
- **Depends On**: Task 1
- **Description**: 
  - 检查维修申请、维修记录列表页面
  - 验证 API 路径与后端一致
- **Acceptance Criteria Addressed**: AC-6, AC-7
- **Test Requirements**:
  - `programmatic` TR-5.1: `repairApi.apply` 调用 `/rental/repair/apply`
  - `programmatic` TR-5.2: `repairApi.list` 调用 `/rental/repair/miniapp/list`
  - `programmatic` TR-5.3: 维修申请页面在租房屋列表加载正常

## [ ] Task 6: 验证邀请管理功能
- **Priority**: high
- **Depends On**: Task 1
- **Description**: 
  - 检查邀请管理页面
  - 验证 API 路径与后端 `/miniapp/*` 格式一致
- **Acceptance Criteria Addressed**: AC-8, AC-9
- **Test Requirements**:
  - `programmatic` TR-6.1: `inviteApi.statistics` 调用 `/rental/invite/miniapp/statistics/{inviterId}`
  - `programmatic` TR-6.2: `inviteApi.inviteList` 调用 `/rental/invite/miniapp/inviteList/{inviterId}`

## [ ] Task 7: 验证兑换商城功能
- **Priority**: high
- **Depends On**: Task 1
- **Description**: 
  - 检查兑换商城和兑换记录页面
  - 验证 API 路径与后端一致
- **Acceptance Criteria Addressed**: AC-10, AC-11
- **Test Requirements**:
  - `programmatic` TR-7.1: `mallApi.productList` 调用 `/rental/mallProduct/selectAll`
  - `programmatic` TR-7.2: `mallApi.exchange` 调用 `/rental/mallRecord/exchange`
  - `programmatic` TR-7.3: `mallApi.userRecord` 调用 `/rental/mallRecord/userRecord/{userId}`
  - `programmatic` TR-7.4: `mallApi.userQuota` 调用 `/rental/exchangeQuota/userQuota/{userId}`

## [ ] Task 8: 验证收藏和浏览功能
- **Priority**: high
- **Depends On**: Task 1
- **Description**: 
  - 检查收藏列表、浏览记录页面
  - 验证 API 路径与后端一致
- **Acceptance Criteria Addressed**: AC-12, AC-13, AC-14
- **Test Requirements**:
  - `programmatic` TR-8.1: `favoriteApi.add` 调用 `/rental/favorite/add`
  - `programmatic` TR-8.2: `favoriteApi.cancel` 调用 `/rental/favorite/cancel`
  - `programmatic` TR-8.3: `favoriteApi.userFavorite` 调用 `/rental/favorite/userFavorite/{userId}`
  - `programmatic` TR-8.4: `browseApi.record` 调用 `/rental/browse/record`
  - `programmatic` TR-8.5: `browseApi.userBrowse` 调用 `/rental/browse/userBrowse/{userId}`

## [ ] Task 9: 验证电话簿功能
- **Priority**: medium
- **Depends On**: Task 1
- **Description**: 
  - 检查电话簿列表和申请页面
  - 验证 API 路径与后端一致
- **Acceptance Criteria Addressed**: AC-15, AC-16
- **Test Requirements**:
  - `programmatic` TR-9.1: `phonebookApi.selectAll` 调用 `/rental/phonebook/selectAll`
  - `programmatic` TR-9.2: `phonebookApi.apply` 调用 `/rental/phonebookApply/apply`

## [ ] Task 10: 验证个人中心功能
- **Priority**: medium
- **Depends On**: Task 1
- **Description**: 
  - 检查个人中心、资料编辑、实名认证页面
  - 验证 API 路径与后端一致
- **Acceptance Criteria Addressed**: AC-17, AC-18, AC-19
- **Test Requirements**:
  - `programmatic` TR-10.1: 个人中心加载统计数据（收藏、浏览、邀请）
  - `programmatic` TR-10.2: 资料编辑调用 `/miniapp/user/updateProfile`
  - `human-judgment` TR-10.3: 个人中心页面布局完整，功能入口清晰

## [ ] Task 11: 生成测试报告
- **Priority**: high
- **Depends On**: Task 2-10
- **Description**: 
  - 汇总所有测试结果
  - 生成详细的测试报告（Markdown 格式）
  - 包含通过/失败统计、问题列表、建议改进项
- **Acceptance Criteria Addressed**: AC-1 至 AC-20
- **Test Requirements**:
  - `human-judgment` TR-11.1: 测试报告包含所有模块的测试结果
  - `human-judgment` TR-11.2: 测试报告格式清晰，易于阅读
  - `human-judgment` TR-11.3: 问题列表包含详细的问题描述和建议解决方案
