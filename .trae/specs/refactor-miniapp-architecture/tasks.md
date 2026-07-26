# Tasks

- [x] Task 1: 基础架构重构 - 配置与清理
  - [ ] SubTask 1.1: 创建 `utils/config.js` 集中管理配置（baseUrl、客服电话、默认头像路径），支持 dev/prod 环境切换
  - [ ] SubTask 1.2: 修改 `app.js` 移除未使用的 `globalData.baseUrl` 和重复的 `isEmpty` 函数，统一使用 `utils/` 导出
  - [ ] SubTask 1.3: 修改 `utils/index.js` 中的 `baseUrl` 改为从 `utils/config.js` 引入
  - [ ] SubTask 1.4: 修改 `project.config.json` 添加 `packOptions.ignore` 排除 `preview/` 目录
  - [ ] SubTask 1.5: 删除 `utils/api.js` 中未被调用的 `chatApi`

- [x] Task 2: 工具层去重 - 提取公共函数
  - [ ] SubTask 2.1: 在 `utils/index.js` 中新增 `normalizeList(res)` 函数，替换 14 处重复的列表数据规范化逻辑
  - [ ] SubTask 2.2: 在 `utils/index.js` 中新增 `parseCoverImage(item)` 函数，替换 `house/list.js` 和 `favorite/list.js` 中的重复定义
  - [ ] SubTask 2.3: 在 `utils/index.js` 中新增 `maskPhone(phone)` 函数，替换 `profile.js` 和 `invite/index.js` 中的重复定义
  - [ ] SubTask 2.4: 创建 `utils/constants.js` 提取维修状态映射 `STATUS_MAP`，替换 `repair/list.js` 和 `repair/detail.js` 中的重复定义
  - [ ] SubTask 2.5: 创建 `utils/validate.js` 提取手机号校验正则 `PHONE_REG`，替换 4 个文件中的重复定义
  - [ ] SubTask 2.6: 删除 `pages/index/index.js` 中重复的 `isEmpty` 和 `normalizeList` 函数

- [ ] Task 3: 公共组件复用 - 启用三态组件
  - [ ] SubTask 3.1: 检查 `components/common` 组件功能是否完整，必要时增强（支持自定义空状态文案和图标）
  - [ ] SubTask 3.2: 在 `house/list.js` 的 json 中注册 common 组件，替换页面内空状态实现
  - [ ] SubTask 3.3: 在 `repair/list.js` 的 json 中注册 common 组件，替换页面内空状态实现
  - [ ] SubTask 3.4: 在 `favorite/list.js` 的 json 中注册 common 组件，替换页面内空状态实现
  - [ ] SubTask 3.5: 在 `message/list.js` 的 json 中注册 common 组件，替换页面内空状态实现
  - [ ] SubTask 3.6: 在 `mall/index.js` 和 `mall/record.js` 的 json 中注册 common 组件

- [ ] Task 4: 功能 TODO 修复 - 图片上传
  - [ ] SubTask 4.1: 在 `utils/api.js` 中新增 `uploadApi.uploadImage(filePath)` 接口，封装 `wx.uploadFile`
  - [ ] SubTask 4.2: 修改 `house/publish.js` 的 `handleSubmit` 方法，先上传图片获取 URL 再提交房源
  - [ ] SubTask 4.3: 修改 `profile/edit.js` 的头像上传逻辑，调用上传接口获取 URL 后保存
  - [ ] SubTask 4.4: 将 `wx.chooseImage` 升级为 `wx.chooseMedia`（house/publish.js、profile/edit.js、repair/apply.js）

- [ ] Task 5: UI/UX 统一 - 样式与交互
  - [ ] SubTask 5.1: 清理页面 wxss 中与 `app.wxss` 重复的 `.card`、`.tag`、`.btn-primary` 等定义
  - [ ] SubTask 5.2: 统一所有页面的 Toast 调用为 `utils.showToast`，移除直接 `wx.showToast` 调用
  - [ ] SubTask 5.3: 在 `app.js` 或 `utils/index.js` 中新增 `requireLogin()` 统一登录拦截方法，替换各页面不一致的登录校验
  - [ ] SubTask 5.4: 修复 `profile.wxml` 中损坏的 base64 箭头图标，替换为 emoji `›`
  - [ ] SubTask 5.5: 替换 `profile.js` 和 `invite/index.js` 中的默认头像外链为本地图片资源
  - [ ] SubTask 5.6: 为 `house/list.wxml`、`favorite/list.wxml` 等列表页 `<image>` 添加 `lazy-load` 属性

- [x] Task 6: 安全加固 - 实名认证
  - [ ] SubTask 6.1: 在 `utils/api.js` 的 `userApi` 中新增 `realNameVerify(data)` 接口
  - [ ] SubTask 6.2: 修改 `profile/verify.js` 调用后端实名认证接口，移除纯前端校验和本地标记
  - [ ] SubTask 6.3: 移除身份证号明文存储本地存储的逻辑

# Task Dependencies
- [Task 2] depends on [Task 1]（工具函数提取依赖配置清理完成）
- [Task 3] depends on [Task 2]（组件替换依赖工具函数统一）
- [Task 4] depends on [Task 1]（上传接口依赖配置管理）
- [Task 5] depends on [Task 2]（样式统一依赖工具函数提取）
- [Task 6] depends on [Task 1]（实名认证依赖配置和 API 层）
- [Task 3]、[Task 4]、[Task 5]、[Task 6] 之间无依赖，可并行
