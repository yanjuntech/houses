<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="字典名称" prop="dictName">
        <el-input
          v-model="queryParams.dictName"
          placeholder="请输入字典名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="字典类型" prop="dictType">
        <el-input
          v-model="queryParams.dictType"
          placeholder="请输入字典类型"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="字典状态"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:dict:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:dict:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:dict:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:dict:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-refresh"
          size="mini"
          @click="handleRefreshCache"
          v-hasPermi="['system:dict:remove']"
        >刷新缓存</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="typeList" @selection-change="handleSelectionChange" @expand-change="handleExpandChange">
      <el-table-column type="expand">
        <template slot-scope="props">
          <div class="dict-data-expand">
            <el-row :gutter="10" class="mb8">
              <el-col :span="12">
                <span class="dict-data-title">字典明细（共 {{ expandData[props.row.dictId] ? expandData[props.row.dictId].total : 0 }} 条）</span>
              </el-col>
              <el-col :span="12" style="text-align: right;">
                <el-select
                  v-model="expandData[props.row.dictId].pageSize"
                  size="mini"
                  style="width: 100px; margin-right: 10px;"
                  @change="handlePageSizeChange(props.row.dictId)"
                >
                  <el-option
                    v-for="item in pageSizeOptions"
                    :key="item"
                    :label="item + '条/页'"
                    :value="item"
                  />
                </el-select>
                <el-button
                  type="primary"
                  plain
                  icon="el-icon-plus"
                  size="mini"
                  @click="handleAddData(props.row)"
                  v-hasPermi="['system:dict:add']"
                >添加</el-button>
              </el-col>
            </el-row>
            <el-table
              v-loading="expandData[props.row.dictId] && expandData[props.row.dictId].loading"
              :data="expandData[props.row.dictId] ? expandData[props.row.dictId].list : []"
              size="small"
              style="width: 100%;"
            >
              <el-table-column label="字典标签" align="center" prop="dictLabel" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                  <span v-if="(scope.row.listClass == '' || scope.row.listClass == 'default') && (scope.row.cssClass == '' || scope.row.cssClass == null)">{{ scope.row.dictLabel }}</span>
                  <el-tag v-else :type="scope.row.listClass == 'primary' ? '' : scope.row.listClass" :class="scope.row.cssClass">{{ scope.row.dictLabel }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="字典键值" align="center" prop="dictValue" :show-overflow-tooltip="true" />
              <el-table-column label="显示排序" align="center" prop="dictSort" width="80" />
              <el-table-column label="状态" align="center" prop="status" width="80">
                <template slot-scope="scope">
                  <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="handleEditData(props.row, scope.row)"
                    v-hasPermi="['system:dict:edit']"
                  >编辑</el-button>
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    @click="handleDeleteData(props.row, scope.row)"
                    v-hasPermi="['system:dict:remove']"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="dict-data-pagination" v-if="expandData[props.row.dictId] && expandData[props.row.dictId].total > 0">
              <el-pagination
                background
                layout="prev, pager, next"
                :current-page="expandData[props.row.dictId].pageNum"
                :page-size="expandData[props.row.dictId].pageSize"
                :total="expandData[props.row.dictId].total"
                @current-change="(page) => handlePageChange(props.row.dictId, page)"
              >
              </el-pagination>
              <span class="page-info">第 {{ expandData[props.row.dictId].pageNum }} / {{ Math.ceil(expandData[props.row.dictId].total / expandData[props.row.dictId].pageSize) }} 页</span>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="字典编号" align="center" prop="dictId" />
      <el-table-column label="字典名称" align="center" prop="dictName" :show-overflow-tooltip="true" />
      <el-table-column label="字典类型" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <a class="link-type" style="cursor:pointer" @click="handleViewData(scope.row)">{{ scope.row.dictType }}</a>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:dict:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-operation"
            @click="handleDataList(scope.row)"
            v-hasPermi="['system:dict:edit']"
          >列表</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:dict:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="form.dictName" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item prop="dictType">
          <el-input v-model="form.dictType" placeholder="请输入字典类型" maxlength="100" />
          <span slot="label">
            <el-tooltip content="数据存储中的Key值，如：sys_user_sex" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
            字典类型
          </span>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <dict-data-drawer :visible.sync="drawerVisible" :row="drawerRow" />

    <!-- 添加或修改字典数据对话框 -->
    <el-dialog :title="dataTitle" :visible.sync="dataOpen" width="500px" append-to-body>
      <el-form ref="dataForm" :model="dataForm" :rules="dataRules" label-width="80px">
        <el-form-item label="字典类型">
          <el-input v-model="dataForm.dictType" :disabled="true" />
        </el-form-item>
        <el-form-item label="数据标签" prop="dictLabel">
          <el-input v-model="dataForm.dictLabel" placeholder="请输入数据标签" />
        </el-form-item>
        <el-form-item label="数据键值" prop="dictValue">
          <el-input v-model="dataForm.dictValue" placeholder="请输入数据键值" />
        </el-form-item>
        <el-form-item label="显示排序" prop="dictSort">
          <el-input-number v-model="dataForm.dictSort" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="dataForm.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dataForm.remark" type="textarea" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDataForm">确 定</el-button>
        <el-button @click="cancelData">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import DictDataDrawer from './detail'
import { listType, getType, delType, addType, updateType, refreshCache } from "@/api/system/dict/type"
import { listData, getData, delData, addData, updateData } from "@/api/system/dict/data"

export default {
  name: "Dict",
  components: { DictDataDrawer },
  dicts: ['sys_normal_disable'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 字典表格数据
      typeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 字典数据抽屉状态
      drawerVisible: false,
      // 字典数据信息
      drawerRow: {},
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dictName: undefined,
        dictType: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        dictName: [
          { required: true, message: "字典名称不能为空", trigger: "blur" }
        ],
        dictType: [
          { required: true, message: "字典类型不能为空", trigger: "blur" }
        ]
      },
      // 展开行数据
      expandData: {},
      // 每页条数选项
      pageSizeOptions: [10, 20, 50, 100],
      // 字典数据弹出层标题
      dataTitle: "",
      // 字典数据是否显示弹出层
      dataOpen: false,
      // 当前展开的字典ID
      currentDictId: null,
      // 字典数据表单参数
      dataForm: {},
      // 字典数据表单校验
      dataRules: {
        dictLabel: [
          { required: true, message: "数据标签不能为空", trigger: "blur" }
        ],
        dictValue: [
          { required: true, message: "数据键值不能为空", trigger: "blur" }
        ],
        dictSort: [
          { required: true, message: "数据顺序不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询字典类型列表 */
    getList() {
      this.loading = true
      listType(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.typeList = response.rows
          this.total = response.total
          this.loading = false
        }
      )
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        dictId: undefined,
        dictName: undefined,
        dictType: undefined,
        status: "0",
        remark: undefined
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm("queryForm")
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加字典类型"
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.dictId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 字典数据抽屉显示信息 */
    handleViewData(row) {
      this.drawerRow = row
      this.drawerVisible = true
    },
    /** 字典数据列表页面 */
    handleDataList(row) {
      this.$tab.openPage("字典数据", '/system/dict-data/index/' + row.dictId)
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const dictId = row.dictId || this.ids
      getType(dictId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改字典类型"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.dictId != undefined) {
            updateType(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addType(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const dictIds = row.dictId || this.ids
      this.$modal.confirm('是否确认删除字典编号为"' + dictIds + '"的数据项？').then(function() {
        return delType(dictIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/dict/type/export', {
        ...this.queryParams
      }, `type_${new Date().getTime()}.xlsx`)
    },
    /** 刷新缓存按钮操作 */
    handleRefreshCache() {
      refreshCache().then(() => {
        this.$modal.msgSuccess("刷新成功")
        this.$store.dispatch('dict/cleanDict')
      })
    },
    /** 表格展开行变化 */
    handleExpandChange(row, expandedRows) {
      if (expandedRows.some(r => r.dictId === row.dictId)) {
        this.loadExpandData(row)
      }
    },
    /** 加载展开行数据 */
    loadExpandData(row) {
      const dictId = row.dictId
      if (!this.expandData[dictId]) {
        this.$set(this.expandData, dictId, {
          list: [],
          total: 0,
          pageNum: 1,
          pageSize: 10,
          loading: false
        })
      }
      this.getExpandDataList(row.dictId, row.dictType)
    },
    /** 获取展开行明细列表 */
    getExpandDataList(dictId, dictType) {
      const data = this.expandData[dictId]
      if (!data) return
      data.loading = true
      listData({
        pageNum: data.pageNum,
        pageSize: data.pageSize,
        dictType: dictType
      }).then(response => {
        data.list = response.rows
        data.total = response.total
        data.loading = false
      }).catch(() => {
        data.loading = false
      })
    },
    /** 每页条数变化 */
    handlePageSizeChange(dictId) {
      const data = this.expandData[dictId]
      if (data) {
        data.pageNum = 1
        const row = this.typeList.find(item => item.dictId === dictId)
        if (row) {
          this.getExpandDataList(dictId, row.dictType)
        }
      }
    },
    /** 页码变化 */
    handlePageChange(dictId, page) {
      const data = this.expandData[dictId]
      if (data) {
        data.pageNum = page
        const row = this.typeList.find(item => item.dictId === dictId)
        if (row) {
          this.getExpandDataList(dictId, row.dictType)
        }
      }
    },
    /** 添加字典数据按钮操作 */
    handleAddData(row) {
      this.resetDataForm()
      this.currentDictId = row.dictId
      this.dataForm.dictType = row.dictType
      this.dataOpen = true
      this.dataTitle = "添加字典数据"
    },
    /** 编辑字典数据按钮操作 */
    handleEditData(dictRow, dataRow) {
      this.resetDataForm()
      this.currentDictId = dictRow.dictId
      getData(dataRow.dictCode).then(response => {
        this.dataForm = response.data
        this.dataOpen = true
        this.dataTitle = "修改字典数据"
      })
    },
    /** 删除字典数据按钮操作 */
    handleDeleteData(dictRow, dataRow) {
      this.$modal.confirm('是否确认删除字典编码为"' + dataRow.dictCode + '"的数据项？').then(() => {
        return delData(dataRow.dictCode)
      }).then(() => {
        this.getExpandDataList(dictRow.dictId, dictRow.dictType)
        this.$modal.msgSuccess("删除成功")
        this.$store.dispatch('dict/removeDict', dictRow.dictType)
      }).catch(() => {})
    },
    /** 提交字典数据表单 */
    submitDataForm() {
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          if (this.dataForm.dictCode != undefined) {
            updateData(this.dataForm).then(() => {
              this.$store.dispatch('dict/removeDict', this.dataForm.dictType)
              this.$modal.msgSuccess("修改成功")
              this.dataOpen = false
              const row = this.typeList.find(item => item.dictId === this.currentDictId)
              if (row) {
                this.getExpandDataList(this.currentDictId, row.dictType)
              }
            })
          } else {
            addData(this.dataForm).then(() => {
              this.$store.dispatch('dict/removeDict', this.dataForm.dictType)
              this.$modal.msgSuccess("新增成功")
              this.dataOpen = false
              const row = this.typeList.find(item => item.dictId === this.currentDictId)
              if (row) {
                const data = this.expandData[this.currentDictId]
                if (data) {
                  data.pageNum = 1
                }
                this.getExpandDataList(this.currentDictId, row.dictType)
              }
            })
          }
        }
      })
    },
    /** 取消字典数据操作 */
    cancelData() {
      this.dataOpen = false
      this.resetDataForm()
    },
    /** 重置字典数据表单 */
    resetDataForm() {
      this.dataForm = {
        dictCode: undefined,
        dictLabel: undefined,
        dictValue: undefined,
        cssClass: undefined,
        listClass: 'default',
        dictSort: 0,
        status: "0",
        remark: undefined
      }
      this.resetForm("dataForm")
    }
  }
}
</script>

<style scoped>
.dict-data-expand {
  padding: 10px 30px;
  background-color: #fafafa;
}
.dict-data-expand .dict-data-title {
  font-size: 14px;
  font-weight: bold;
  color: #303133;
  line-height: 32px;
}
.dict-data-expand .dict-data-pagination {
  margin-top: 10px;
  text-align: right;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
.dict-data-expand .dict-data-pagination .page-info {
  margin-left: 10px;
  color: #606266;
  font-size: 13px;
}
.dict-data-expand .mb8 {
  margin-bottom: 8px;
}
</style>