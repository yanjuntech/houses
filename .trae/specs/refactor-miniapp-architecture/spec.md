# 重构设计整个微信小程序 Spec

## Why
当前小程序功能完整度较高（18 个页面均已对接后端），但工程质量存在系统性问题：公共组件从未被复用、工具函数重复定义 14+ 处、多处硬编码外链地址（含字节内网不可达地址）、实名认证/图片上传等关键功能存在 TODO 未完成、登录校验和错误处理不一致。这些问题导致维护成本高、存在安全漏洞和功能缺陷，需要系统性重构。

## What Changes

### 一、基础架构层重构
- 统一配置管理：抽取 `config/index.js` 集中管理 baseUrl、客服电话、默认头像路径等配置，支持 dev/prod 环境切换
- 清理死代码：删除 `app.js` 中未使用的 `globalData.baseUrl`、`utils/api.js` 中未被调用的 `chatApi`
- 排除预览页：在 `project.config.json` 的 `packOptions.ignore` 中排除 `preview/` 目录

### 二、工具层去重
- 提取 `normalizeList(res)` 到 `utils/index.js`，替换 14 处重复的列表数据规范化逻辑
- 删除 `app.js`、`pages/index/index.js` 中重复定义的 `isEmpty` 函数，统一使用 `utils/index.js` 版本
- 提取 `parseCoverImage`、`maskPhone`、`formatDate` 到 `utils/index.js`
- 提取维修状态映射 `STATUS_MAP` 到 `utils/constants.js`
- 提取手机号校验正则到 `utils/validate.js`

### 三、公共组件复用
- 启用 `components/common` 三态组件（loading/empty/error），在所有列表页注册并替换页面内重复的空状态实现
- 提取房源卡片、商家卡片为独立组件，在首页和列表页复用

### 四、功能 TODO 修复
- 房源发布图片上传：对接 `wx.uploadFile` 上传到服务器，替换本地临时路径
- 用户头像上传：对接 `wx.uploadFile` 上传到服务器
- 实名认证对接后端：调用后端实名认证接口，移除纯前端校验

### 五、UI/UX 统一
- 统一全局样式：清理页面 wxss 中与 `app.wxss` 重复的 `.card`、`.tag` 等定义
- 统一 Toast 调用方式：全部使用 `utils.showToast`
- 统一登录拦截策略：提供 `requireLogin()` 统一处理
- 修复 `profile.wxml` 中损坏的 base64 箭头图标，替换为 emoji 或本地图片
- 替换默认头像外链为本地图片资源
- `wx.chooseImage` 升级为 `wx.chooseMedia`

### 六、性能与安全
- 列表页 `<image>` 添加 `lazy-load` 属性
- 身份证号不再明文存储本地存储
- 移除 `repair/detail.js` 通过列表 find 获取详情的方式（如后端支持则用独立接口）

## Impact
- Affected specs: enhance-miniapp-core-features, full-miniapp-backend-integration, miniapp-full-api-test
- Affected code: 
  - 配置层：`app.js`、`app.json`、`app.wxss`、`project.config.json`
  - 工具层：`utils/index.js`、`utils/api.js`、新增 `utils/config.js`、`utils/constants.js`、`utils/validate.js`
  - 组件层：`components/common/*`、新增房源/商家卡片组件
  - 页面层：全部 18 个页面（样式清理、工具函数替换、组件注册）
  - 预览页：`preview/index.html`（同步更新或排除）

## ADDED Requirements

### Requirement: 统一配置管理
系统 SHALL 提供集中的配置文件 `utils/config.js`，管理 baseUrl、客服电话、默认头像路径等可配置项，支持开发/生产环境切换。

#### Scenario: 环境切换
- **WHEN** 开发者需要切换后端地址
- **THEN** 只需修改 `utils/config.js` 中一处配置，无需改动其他文件

### Requirement: 公共工具函数
系统 SHALL 在 `utils/index.js` 中提供 `normalizeList`、`parseCoverImage`、`maskPhone` 等公共工具函数，供所有页面复用。

#### Scenario: 列表数据规范化
- **WHEN** 任意页面接收到后端返回数据
- **THEN** 调用 `normalizeList(res)` 统一处理，返回数组

### Requirement: 公共组件复用
系统 SHALL 启用 `components/common` 三态组件，所有列表页通过 `usingComponents` 注册并使用。

#### Scenario: 空状态展示
- **WHEN** 列表数据为空
- **THEN** 展示统一的空状态组件，而非各页面自行实现

### Requirement: 图片上传功能
系统 SHALL 通过 `wx.uploadFile` 对接后端图片上传接口，将图片上传至服务器并获取 URL。

#### Scenario: 房源发布图片上传
- **WHEN** 用户选择图片并发布房源
- **THEN** 图片先上传至服务器获取 URL，再将 URL 存入房源数据

## MODIFIED Requirements

### Requirement: 登录拦截策略
所有需要登录的页面 SHALL 使用统一的 `requireLogin()` 方法进行登录校验，未登录时统一跳转登录页。

### Requirement: Toast 调用方式
所有用户提示 SHALL 统一使用 `utils.showToast` 封装函数，不直接调用 `wx.showToast`。

### Requirement: 样式管理
全局通用样式（`.card`、`.tag`、`.btn-primary` 等）SHALL 仅在 `app.wxss` 中定义一次，页面 wxss 不重复定义。

## REMOVED Requirements

### Requirement: chatApi 死代码
**Reason**: `utils/api.js` 中的 `chatApi`（send/history/markAsRead）无任何前端页面调用，且无对应聊天页面
**Migration**: 直接删除，如后续需要聊天功能再新增

### Requirement: app.js 中重复的 isEmpty 函数
**Reason**: 与 `utils/index.js` 中完全相同，重复定义
**Migration**: 删除 `app.js` 中的本地定义，使用 `utils/index.js` 导出版本
