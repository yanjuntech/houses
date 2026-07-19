# 验证检查清单

## 标签多选（FR-1）
- [x] 小区编辑表单标签字段为多选下拉（el-select multiple）
- [x] 房屋编辑表单标签字段为多选下拉
- [x] 小程序用户编辑表单标签字段为多选下拉
- [x] 选择多个标签后提交，数据库 tags 字段存储为逗号分隔的字符串（如 "绿化面积大,物业服务好"）
- [x] 列表展示时多个标签均能渲染为 dict-tag 组件
- [x] 详情页展示时多个标签均能渲染为 dict-tag 组件
- [x] 编辑回显时已选标签能正确高亮显示

## 参数类型字段（FR-2）
- [x] sys_config 表已新增 config_value_type 字段（char(1)）*（注：原 spec 写为 config_type，因与系统内置字段冲突，实际命名为 config_value_type）*
- [x] SysConfig 实体类已增加 configValueType 属性
- [x] SysConfigMapper.xml 的 select/insert/update 均包含 config_value_type 字段
- [x] 参数编辑表单新增"参数值类型"下拉选择，可选：数字类型/字符串类型/文本类型/下拉类型

## 参数键值控件动态切换（FR-3）
- [x] 选择"数字类型"时，参数键值输入框渲染为 InputNumber（spinbutton）
- [x] 选择"字符串类型"时，参数键值输入框渲染为单行 Input
- [x] 选择"文本类型"时，参数键值输入框渲染为 textarea 多行
- [x] 选择"下拉类型"时，参数键值输入框渲染为 Select
- [x] 切换参数类型时，控件状态正确清空，无残留值

## 参数键选项维护（FR-4）
- [x] 原动态增删选项行组件已移除，"添加"按钮已删除
- [x] 替换为单个 textarea 文本框，placeholder 提示格式 `参数名&参数值#参数名&参数值#...`
- [x] 保存时 textarea 内容完整保存到 config_options 字段
- [x] 下拉类型参数的 Select 选项正确解析 config_options 字符串（按 # 分隔项，按 & 分隔名/值）
- [x] 下拉类型参数提交时，config_value 存储 value（参数值）而非 label（参数名）
- [x] 下拉类型参数回显时，根据 config_value 反查 label 正确显示选中项

## 数据清洗（FR-5）
- [x] 清洗 SQL 已编写并执行
- [x] 主框架页-默认皮肤样式名称参数 config_options 清洗为 `蓝色&skin-blue#绿色&skin-green#紫色&skin-purple#红色&skin-red#黄色&skin-yellow`
- [x] 主框架页-默认皮肤样式名称参数 config_value_type 设置为 'D'
- [x] 其他含枚举值的参数已按格式清洗（sys.index.sideTheme、sys.account.captchaEnabled、sys.account.registerUser 等）
- [x] 数字类型参数 config_value_type 设置为 'N'
- [x] 字符串类型参数 config_value_type 设置为 'S'（默认）
- [x] 文本类型参数 config_value_type 设置为 'T'

## 端到端验证
- [x] 后端服务成功启动
- [x] 前端 dev server 成功启动
- [x] 浏览器内置自动化验证：标签多选保存成功
- [x] 浏览器内置自动化验证：参数类型切换控件正确响应
- [x] 浏览器内置自动化验证：下拉选项解析正确，存储值正确
- [x] 数据库直接验证：清洗 SQL 执行后字段值符合预期
