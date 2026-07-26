# TabBar 中间发布按钮 - The Implementation Plan

## [x] Task 1: 创建中间发布页面（pages/publish/index）
- **Priority**: high
- **Depends On**: None
- **Description**: 
  - 创建 `pages/publish/index.js`：处理页面加载、权限校验、弹窗显示
  - 创建 `pages/publish/index.wxml`：弹窗 UI（遮罩层 + 底部操作菜单）
  - 创建 `pages/publish/index.wxss`：弹窗样式（圆角、动画、按钮样式）
  - 创建 `pages/publish/index.json`：配置页面标题为"发布"
- **Acceptance Criteria Addressed**: AC-2, AC-3, AC-4, AC-5, AC-6, AC-7, AC-8
- **Test Requirements**:
  - `programmatic` TR-1.1: onLoad 时自动显示弹窗 ✓
  - `programmatic` TR-1.2: 点击遮罩层关闭弹窗 ✓
  - `programmatic` TR-1.3: 选择"发布房屋"未登录时跳转登录页 ✓
  - `programmatic` TR-1.4: 选择"发布房屋"未认证时跳转实名认证页 ✓
  - `programmatic` TR-1.5: 选择"发布房屋"已认证时跳转发布页 ✓
  - `programmatic` TR-1.6: 选择"申请电话簿收录"未登录时跳转登录页 ✓
  - `programmatic` TR-1.7: 选择"申请电话簿收录"已登录时跳转申请页 ✓

## [x] Task 2: 修改 app.json 配置 tabBar
- **Priority**: high
- **Depends On**: Task 1
- **Description**: 
  - 在 `app.json` 的 `pages` 数组中注册 `pages/publish/index`
  - 在 `tabBar.list` 中"房屋"和"电话簿"之间增加发布按钮配置
  - 使用自定义 tabBar 实现中间圆形按钮效果
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-2.1: `app.json` 中已注册 `pages/publish/index` 路由 ✓
  - `human-judgment` TR-2.2: tabBar 中间显示圆形发布按钮，位于"房屋"和"电话簿"之间 ✓

## [x] Task 3: 添加发布按钮图标资源
- **Priority**: medium
- **Depends On**: Task 2
- **Description**: 
  - 创建发布按钮图标（普通状态和选中状态），使用蓝色渐变圆形背景，中心白色"+"号
  - 图标尺寸建议：64px x 64px
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `human-judgment` TR-3.1: 发布按钮图标清晰可见，样式与现有 tabBar 协调 ✓

## [x] Task 4: 验证与测试
- **Priority**: high
- **Depends On**: Task 1, Task 2, Task 3
- **Description**: 
  - 验证 tabBar 中间发布按钮显示正常
  - 验证点击后弹窗弹出正常
  - 验证权限校验逻辑正确
  - 验证跳转目标页面正确
- **Acceptance Criteria Addressed**: AC-1, AC-2, AC-3, AC-4, AC-5, AC-6, AC-7, AC-8
- **Test Requirements**:
  - `human-judgment` TR-4.1: 所有功能验证通过 ✓
