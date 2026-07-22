# 修复前端打不开问题

## 问题诊断

### 根因
[serve.js](file:///workspace/ruoyi-ui/serve.js) 的 SPA fallback 逻辑有 bug：当请求不存在的文件（如动态加载的 JS chunk）时，返回 index.html（200 + `text/html`）而不是 404。浏览器把 HTML 当作 JavaScript 执行，导致解析错误，Vue 应用无法启动，页面卡在 loading 动画。

### 验证
```
curl -sI http://localhost:8081/static/js/nonexistent.js
# 返回: 200, Content-Type: text/html  ← 应该返回 404
```

## 修复步骤

### Step 1: 修复 serve.js 的 SPA fallback 逻辑
- 文件: `ruoyi-ui/serve.js`
- 修改: 对有文件扩展名的请求（.js/.css/.png 等），文件不存在时返回 404 而不是 index.html
- 只有当请求路径没有文件扩展名时，才返回 index.html（SPA 路由 fallback）
- 同时处理 URL 查询参数（去掉 `?v=123` 等）

### Step 2: 停止旧的前端服务并重启
- 停止当前 serve.js 进程
- 用修复后的 serve.js 重新启动

### Step 3: 验证
- 检查不存在的文件返回 404
- 检查页面正常加载
- 打开预览确认
