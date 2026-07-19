# Tasks

- [x] Task 1: 标签选择器多选修复（前端）
  - [x] SubTask 1.1: 小区编辑表单标签字段改为 el-select multiple
  - [x] SubTask 1.2: 房屋编辑表单标签字段改为 el-select multiple
  - [x] SubTask 1.3: 小程序用户编辑表单标签字段改为 el-select multiple
  - [x] SubTask 1.4: 列表/详情展示使用 dict-tag 渲染多标签（如已实现则跳过）

- [x] Task 2: 参数类型字段（后端）
  - [x] SubTask 2.1: sys_config 表新增 config_value_type 字段（char(1)，默认 'S'）*（注：原 spec 写为 config_type，因与系统内置字段冲突，实际命名为 config_value_type）*
  - [x] SubTask 2.2: SysConfig 实体增加 configValueType 字段
  - [x] SubTask 2.3: SysConfigMapper.xml 适配新字段的 select/insert/update
  - [x] SubTask 2.4: 数据库初始化清洗 SQL：根据 remark 推断 config_value_type（数字→N，有枚举→D，纯文本→T，其他→S）

- [x] Task 3: 参数键选项维护方式重构（前端）
  - [x] SubTask 3.1: 移除原"添加/删除选项行"按钮及表格组件
  - [x] SubTask 3.2: 替换为 el-input type="textarea"，提示符显示格式 `参数名&参数值#参数名&参数值#...`
  - [x] SubTask 3.3: 表单保存时直接将 textarea 内容作为 config_options 提交

- [x] Task 4: 参数键值控件动态切换（前端）
  - [x] SubTask 4.1: 监听 configValueType 变化，动态渲染对应控件（InputNumber/Input/textarea/Select）
  - [x] SubTask 4.2: 下拉类型时解析 config_options 字符串，转换为 options 数组传入 Select
  - [x] SubTask 4.3: 下拉类型时确保 config_value 存储 value（参数值）而非 label（参数名）
  - [x] SubTask 4.4: 表单 reset 时清空动态控件状态

- [x] Task 5: 现有参数数据清洗（SQL）
  - [x] SubTask 5.1: 编写清洗 SQL，解析现有 sys_config.remark 中的枚举信息，按 `参数名&参数值#...` 格式填充 config_options
  - [x] SubTask 5.2: 至少覆盖以下参数：主框架页-默认皮肤样式名称、用户管理-账号初始密码（如有枚举）、其他含枚举值的参数
  - [x] SubTask 5.3: 将含枚举选项的参数 config_value_type 设置为 'D'；数字类型参数设置为 'N'；其他默认 'S'

- [x] Task 6: 验证与测试
  - [x] SubTask 6.1: 启动后端服务（mvn package + java -jar）
  - [x] SubTask 6.2: 启动前端 dev server
  - [x] SubTask 6.3: 浏览器验证：小区/房屋/用户编辑表单标签多选工作正常
  - [x] SubTask 6.4: 浏览器验证：参数编辑表单中切换参数类型时键值控件正确切换
  - [x] SubTask 6.5: 浏览器验证：下拉类型参数的下拉选项正确解析且存储值正确
  - [x] SubTask 6.6: 数据库验证：清洗 SQL 执行后 config_options 和 config_value_type 字段正确填充

# Task Dependencies
- Task 2 必须先完成，Task 4 才能正确读取 configValueType 字段
- Task 5 依赖 Task 2 完成（需要 config_value_type 字段已存在）
- Task 6 依赖 Task 1、Task 3、Task 4、Task 5 全部完成
- Task 1、Task 3 相互独立，可并行执行
