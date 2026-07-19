# 标签多选与参数配置增强 Spec

## Why
现有小程序租赁增强功能已交付，但存在两类问题：
1. 小区标签、用户标签、房屋标签编辑时仍为单选，无法满足业务"一个对象可同时具备多个标签"的需求
2. 参数键选项编辑器中"添加"按钮点击无反应，无法维护下拉选项；同时缺少参数类型区分，导致数字/文本/下拉等场景混用同一输入框体验差

本变更通过修复多选交互与重构参数选项维护方式，提升后台运营效率。

## What Changes
- 修复小区/用户/房屋编辑表单中标签选择器：由单选改为多选
- 参数设置表单新增"参数类型"字段：数字类型 / 字符串类型 / 文本类型 / 下拉类型
- 根据参数类型动态切换"参数键值"输入控件：
  - 数字类型 → InputNumber
  - 字符串类型 → Input
  - 文本类型 → Input textarea
  - 下拉类型 → Select（数据来自参数键选项）
- 重构"参数键选项"维护方式：由原"动态增删选项行"改为"单文本框格式化维护"
  - 格式：`参数名&参数值#参数名&参数值#参数名&参数值`
  - 示例：`蓝色&skin-blue#绿色&skin-green#紫色&skin-purple#红色&skin-red#黄色&skin-yellow`
- 下拉类型参数的"参数键值"显示参数名（label），但实际存储参数值（value）
- 修复"添加"按钮无反应问题（原动态增删方案被废弃，改为纯文本编辑）
- 对现有 sys_config 数据进行二次清洗，按新格式重新填充 config_options

## Impact
- Affected specs:
  - [miniapp-rental-enhance/spec.md FR-3]（字典标签体系扩展）：标签选择器由单选扩展为多选
  - [miniapp-rental-enhance/spec.md FR-11]（参数设置键选项配置）：维护方式重构
- Affected code:
  - 前端：`ruoyi-ui/src/views/system/config/` 参数编辑表单
  - 前端：`ruoyi-ui/src/views/rental/community/index.vue`（小区编辑标签多选）
  - 前端：`ruoyi-ui/src/views/rental/house/index.vue`（房屋编辑标签多选）
  - 前端：`ruoyi-ui/src/views/miniapp/user/index.vue`（小程序用户编辑标签多选）
  - 后端：`ruoyi-system/src/main/java/com/ruoyi/system/domain/SysConfig.java`（新增 config_type 字段）
  - 数据库：`sys_config` 表新增 `config_type` 字段；清洗 `config_options` 数据

## ADDED Requirements

### Requirement: 参数类型字段
系统 SHALL 在参数设置表单中新增"参数类型"下拉选择，可选值：数字类型（N）、字符串类型（S）、文本类型（T）、下拉类型（D）。

#### Scenario: 数字类型参数
- **WHEN** 用户选择参数类型为"数字类型"
- **THEN** 参数键值输入框渲染为 InputNumber，支持数字步进与最小/最大值约束

#### Scenario: 字符串类型参数
- **WHEN** 用户选择参数类型为"字符串类型"
- **THEN** 参数键值输入框渲染为单行 Input

#### Scenario: 文本类型参数
- **WHEN** 用户选择参数类型为"文本类型"
- **THEN** 参数键值输入框渲染为 textarea 多行文本框

#### Scenario: 下拉类型参数
- **WHEN** 用户选择参数类型为"下拉类型"
- **THEN** 参数键值输入框渲染为 Select，数据来源于"参数键选项"文本框解析后的选项；显示参数名，提交时存储参数值

## MODIFIED Requirements

### Requirement: 标签选择器交互
房屋、小区、小程序用户编辑表单中的标签字段 SHALL 支持多选；选中的多个标签以逗号分隔存储到 tags 字段。

#### Scenario: 多选标签保存
- **WHEN** 用户在小区编辑表单选择多个标签（如"绿化面积大,物业服务好"）
- **THEN** 提交时 tags 字段值为 "绿化面积大,物业服务好"
- **AND** 列表/详情页正确以多个 dict-tag 标签展示

### Requirement: 参数键选项维护方式
参数键选项 SHALL 通过单个 textarea 文本框以格式化字符串维护，格式为 `参数名&参数值#参数名&参数值#...`；不再使用动态增删选项行的方式。

#### Scenario: 维护下拉选项
- **WHEN** 用户在参数编辑表单的"参数键选项"textarea 中输入 `蓝色&skin-blue#绿色&skin-green#紫色&skin-purple`
- **THEN** 保存时 config_options 字段值完整保存该字符串
- **AND** 当参数类型为"下拉类型"时，参数键值下拉框解析该字符串并显示三个选项：蓝色/绿色/紫色

#### Scenario: 显示与存储值分离
- **WHEN** 用户在下拉类型参数的"参数键值"下拉框中选择"绿色"
- **THEN** 提交时 config_value 字段存储为 `skin-green`，而非显示文本"绿色"

### Requirement: 现有参数数据清洗
系统 SHALL 对现有 sys_config 表中的参数进行数据清洗，解析参数 remark（备注）字段中的选项信息，按新格式填充 config_options 字段。

#### Scenario: 皮肤样式参数清洗
- **WHEN** 参数 remark 包含"蓝色 skin-blue 绿色 skin-green ..."等枚举信息
- **THEN** config_options 字段填充为 `蓝色&skin-blue#绿色&skin-green#紫色&skin-purple#红色&skin-red#黄色&skin-yellow`
- **AND** 参数类型 config_type 设置为 D（下拉类型）

## REMOVED Requirements

### Requirement: 动态增删选项行
**Reason**: 用户反馈原"添加/删除选项行"按钮点击无反应，且维护多行输入框体验差；改为单文本框格式化字符串维护更简洁高效
**Migration**: 原 config_options 字段若已存储为 JSON 数组结构，需运行清洗 SQL 转换为 `参数名&参数值#...` 格式
