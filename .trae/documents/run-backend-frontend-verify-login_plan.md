# 运行前后端并验证前端登录

## 研究结论

### 当前服务状态
- **无服务运行**: Java、Node、Redis、MySQL 进程均未运行
- **端口状态**: 8080(后端)、8081(前端) 均无响应
- **数据库目录**: `/var/lib/mysql/` 不存在，MySQL 未初始化
- **Redis**: 未安装（`redis-server`/`redis-cli` 不存在）

### 可用工具
- **Java**: OpenJDK 17.0.2（mise 管理）
- **Node.js**: v24.15.0（nvm 管理）
- **Maven**: 可用（mise 管理）
- **npm**: 可用

### 项目配置
- **后端端口**: 8080，context-path: `/`
- **数据库**: MySQL `localhost:3306/ry-vue`，用户 `ry`，密码 `password`
- **Redis**: `localhost:6379`，无密码，database 0
- **前端端口**: 8081（serve.js 静态服务器）
- **前端代理**: `/dev-api` → `http://localhost:8080`
- **SQL 文件**: `/workspace/sql/` 下有 `ry_20260417.sql`（基础表）及多个业务扩展 SQL

### 关键文件
- 后端配置: [application.yml](file:///workspace/ruoyi-admin/src/main/resources/application.yml)
- 数据源配置: [application-druid.yml](file:///workspace/ruoyi-admin/src/main/resources/application-druid.yml)
- 前端静态服务器: [serve.js](file:///workspace/ruoyi-ui/serve.js)
- 前端 package: [package.json](file:///workspace/ruoyi-ui/package.json)

## 需要修改/操作的模块

### 基础设施层
1. **MySQL 8.0**: 安装、初始化、启动、创建数据库和用户
2. **Redis**: 安装并启动

### 数据层
3. **导入基础 SQL**: `ry_20260417.sql`（若依系统基础表+数据）
4. **导入业务扩展 SQL**: `/workspace/sql/` 下的业务表（biz_ 开头的表）

### 后端
5. **构建后端**: `mvn clean package -DskipTests` 构建 Jar
6. **启动后端**: `java -jar ruoyi-admin/target/ruoyi-admin.jar`

### 前端
7. **构建前端**: `npm run build:prod` 生成 dist/
8. **启动前端**: `node serve.js`（端口 8081，API 代理到 8080）

### 验证
9. **验证登录**: 使用 Playwright/浏览器模拟登录 admin/admin123，确认登录成功跳转首页

## 实施步骤

### Step 1: 安装并启动 MySQL
- `apt-get install -y mysql-server`
- 启动 MySQL 服务
- 创建数据库 `ry-vue` 和用户 `ry`（密码 `password`）
- 赋予 `ry` 用户全部权限

### Step 2: 安装并启动 Redis
- `apt-get install -y redis-server`
- 启动 Redis 服务（默认端口 6379，无密码）
- 验证 `redis-cli ping` 返回 PONG

### Step 3: 导入数据库
- 导入基础 SQL: `mysql -u root ry-vue < sql/ry_20260417.sql`
- 按顺序导入业务扩展 SQL（biz_ 相关）
- 验证 `sys_user` 表中存在 admin 用户

### Step 4: 构建并启动后端
- `cd /workspace && mvn clean package -DskipTests`
- 启动后端: `java -jar ruoyi-admin/target/ruoyi-admin.jar &`
- 等待启动完成（约 30-60 秒）
- 验证: `curl http://localhost:8080/captchaImage` 返回 JSON

### Step 5: 构建并启动前端
- `cd /workspace/ruoyi-ui && npm install`（如 node_modules 不存在）
- `npm run build:prod` 生成 dist/
- 启动前端静态服务器: `node serve.js &`
- 验证: `curl http://localhost:8081/` 返回 HTML

### Step 6: 验证前端登录
- 使用 Playwright 打开 `http://localhost:8081/login`
- 填写用户名 admin、密码 admin123、获取并填写验证码
- 点击登录按钮
- 验证跳转至首页（URL 包含 `/index`，页面出现首页元素）
- 截图保存验证结果

## 依赖关系

```
Step 1 (MySQL) ──┐
                 ├── Step 3 (导入数据) ── Step 4 (后端构建启动) ──┐
Step 2 (Redis) ──┘                                                ├── Step 6 (验证登录)
                                                   Step 5 (前端) ──┘
```

- Step 1 和 Step 2 可并行执行
- Step 3 依赖 Step 1
- Step 4 依赖 Step 2 和 Step 3
- Step 5 与 Step 4 可并行执行
- Step 6 依赖 Step 4 和 Step 5

## 潜在风险与应对

| 风险 | 影响 | 应对方案 |
|------|------|----------|
| MySQL 安装失败/版本不兼容 | 后端无法启动 | 使用 apt 默认 MySQL 8.0，与 RuoYi 配置一致 |
| Maven 构建慢/依赖下载失败 | 后端启动延迟 | 使用已有本地 Maven 缓存，必要时重试 |
| npm install 慢 | 前端构建延迟 | 检查是否已有 node_modules |
| Node v24 与 Vue CLI 4 不兼容 | 前端构建失败 | Vue CLI 4.4.6 理论上兼容 Node 24，失败则尝试降级或跳过构建直接用 dev 模式 |
| 验证码登录复杂 | 自动化验证困难 | 临时关闭验证码（设置 `sys.account.captchaEnabled=false` 并清缓存），测试后恢复 |
| 内存不足 | OOM 导致服务崩溃 | 限制 Java 堆内存 `-Xms256m -Xmx512m` |

## 验证标准

1. MySQL 服务运行中，`ry-vue` 库存在且 `sys_user` 有数据
2. Redis 服务运行中，`redis-cli ping` 返回 PONG
3. 后端端口 8080 可访问，`/captchaImage` 返回有效 JSON
4. 前端端口 8081 可访问，返回 HTML 页面
5. Playwright 浏览器测试：登录成功后 URL 跳转至 `/index`，页面显示"若依管理系统"
