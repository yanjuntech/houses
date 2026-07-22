<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="4">
        <div class="category-tree-container">
          <div class="tree-header">
            <span class="tree-title">商家分类</span>
          </div>
          <el-tree
            ref="categoryTree"
            :data="categoryTreeData"
            :props="defaultProps"
            node-key="id"
            default-expand-all
            :expand-on-click-node="false"
            highlight-current
            @node-click="handleNodeClick"
          >
            <span class="custom-tree-node" slot-scope="{ node, data }">
              <span class="node-label">{{ node.label }}</span>
              <span class="node-count" v-if="data.count !== undefined">({{ data.count }})</span>
            </span>
          </el-tree>
        </div>
      </el-col>
      <el-col :span="20">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="商家名称" prop="merchantName">
            <el-input
              v-model="queryParams.merchantName"
              placeholder="请输入商家名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="商家分类" prop="category">
            <el-select v-model="queryParams.category" placeholder="请选择商家分类" clearable>
              <el-option
                v-for="item in categoryOptions"
                :key="item.value"
                :label="item.label"
                :value="item.label"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="电话" prop="phone">
            <el-input
              v-model="queryParams.phone"
              placeholder="请输入电话"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
              <el-option label="启用" value="0" />
              <el-option label="停用" value="1" />
            </el-select>
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
              v-hasPermi="['rental:phonebook:add']"
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
              v-hasPermi="['rental:phonebook:edit']"
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
              v-hasPermi="['rental:phonebook:remove']"
            >删除</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="phonebookList" @selection-change="handleSelectionChange">
          <el-table-column type="expand">
            <template slot-scope="scope">
              <el-descriptions :column="3" border size="medium" style="padding: 10px;">
                <el-descriptions-item label="商家名称">{{ scope.row.merchantName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="负责人">{{ scope.row.ownerName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="商家分类">{{ scope.row.category || '-' }}</el-descriptions-item>
                <el-descriptions-item label="联系电话1">{{ scope.row.phone1 || '-' }}</el-descriptions-item>
                <el-descriptions-item label="联系电话2">{{ scope.row.phone2 || '-' }}</el-descriptions-item>
                <el-descriptions-item label="状态">
                  <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">{{ scope.row.status === '0' ? '启用' : '停用' }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="地址" :span="3">{{ scope.row.address || '-' }}</el-descriptions-item>
                <el-descriptions-item label="营业执照" :span="3">
                  <el-link v-if="scope.row.businessLicense" :href="scope.row.businessLicense" target="_blank" type="primary">查看营业执照</el-link>
                  <span v-else>-</span>
                </el-descriptions-item>
                <el-descriptions-item label="有效期至">{{ scope.row.validUntil ? parseTime(scope.row.validUntil, '{y}-{m}-{d}') : '-' }}</el-descriptions-item>
                <el-descriptions-item label="创建人">{{ scope.row.createBy || '-' }}</el-descriptions-item>
                <el-descriptions-item label="创建时间">{{ parseTime(scope.row.createTime) || '-' }}</el-descriptions-item>
                <el-descriptions-item label="备注" :span="3">{{ scope.row.remark || '-' }}</el-descriptions-item>
              </el-descriptions>
            </template>
          </el-table-column>
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="编号" align="center" prop="phonebookId" />
          <el-table-column label="商家名称" align="center" prop="merchantName" :show-overflow-tooltip="true" />
          <el-table-column label="负责人" align="center" prop="ownerName" />
          <el-table-column label="电话" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.phone1 || '' }}{{ scope.row.phone2 ? ' / ' + scope.row.phone2 : '' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="分类" align="center" prop="category" />
          <el-table-column label="地址" align="center" prop="address" :show-overflow-tooltip="true" />
          <el-table-column label="状态" align="center" prop="status">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
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
                v-hasPermi="['rental:phonebook:edit']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['rental:phonebook:remove']"
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
      </el-col>
    </el-row>

    <!-- 添加或修改电话簿对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="商家名称" prop="merchantName">
              <el-input v-model="form.merchantName" placeholder="请输入商家名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="ownerName">
              <el-input v-model="form.ownerName" placeholder="请输入负责人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话1" prop="phone1">
              <el-input v-model="form.phone1" placeholder="11位手机号或座机" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话2" prop="phone2">
              <el-input v-model="form.phone2" placeholder="选填，11位手机号或座机" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商家分类" prop="category">
              <el-select v-model="form.category" placeholder="请选择商家分类" style="width: 100%">
                <el-option
                  v-for="item in categoryOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio label="0">启用</el-radio>
                <el-radio label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入地址" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="营业执照" prop="businessLicense">
              <el-input v-model="form.businessLicense" placeholder="请输入营业执照图片URL" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPhonebook, getPhonebook, addPhonebook, updatePhonebook, delPhonebook, changePhonebookStatus } from "@/api/rental/phonebook"

export default {
  name: "Phonebook",
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
      // 电话簿表格数据
      phonebookList: [],
      // 商家分类下拉数据
      categoryOptions: [
        { value: "1", label: "餐饮美食" },
        { value: "2", label: "生活服务" },
        { value: "3", label: "家居建材" },
        { value: "4", label: "教育培训" },
        { value: "5", label: "医疗健康" },
        { value: "6", label: "休闲娱乐" },
        { value: "7", label: "交通出行" },
        { value: "8", label: "购物商场" }
      ],
      // 分类树配置
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      // 当前选中的分类节点
      currentCategoryId: '0',
      // 分类树数据
      categoryTreeData: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        merchantName: undefined,
        category: undefined,
        phone: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        merchantName: [
          { required: true, message: "商家名称不能为空", trigger: "blur" }
        ],
        ownerName: [
          { required: true, message: "负责人不能为空", trigger: "blur" }
        ],
        phone1: [
          { required: true, message: "联系电话1不能为空", trigger: "blur" },
          { pattern: /(^1[3-9]\d{9}$)|(^0\d{2,3}-?\d{7,8}$)/, message: "电话格式不正确（11位手机号或区号-号码）", trigger: "blur" }
        ],
        phone2: [
          { required: false, pattern: /(^1[3-9]\d{9}$)|(^0\d{2,3}-?\d{7,8}$)/, message: "电话格式不正确（11位手机号或区号-号码）", trigger: "blur" }
        ],
        category: [
          { required: true, message: "商家分类不能为空", trigger: "change" }
        ],
        address: [
          { required: true, message: "地址不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.initCategoryTree()
    this.getList()
  },
  mounted() {
    this.$nextTick(() => {
      if (this.$refs.categoryTree) {
        this.$refs.categoryTree.setCurrentKey('0')
      }
    })
  },
  methods: {
    /** 初始化分类树 */
    initCategoryTree() {
      // 注意:数据库 biz_phonebook.category 字段存储的是中文标签(如"餐饮美食"、"超市便利")
      // 因此 value 必须使用 item.label(中文)以匹配后端查询,
      // id 使用 item.value(数字)仅用于树节点标识
      const children = this.categoryOptions.map(item => ({
        id: item.value,
        label: item.label,
        value: item.label
      }))
      this.categoryTreeData = [{
        id: '0',
        label: '全部',
        value: undefined,
        children: children
      }]
    },
    /** 分类树节点点击 */
    handleNodeClick(data) {
      this.currentCategoryId = data.id
      // 安全检查:如果 data.value 为 undefined 或 null(如点击"全部"根节点),则清空查询条件
      if (data.value === undefined || data.value === null) {
        this.queryParams.category = undefined
      } else {
        this.queryParams.category = data.value
      }
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 查询电话簿列表 */
    getList() {
      this.loading = true
      listPhonebook(this.queryParams).then(response => {
        this.phonebookList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        phonebookId: undefined,
        merchantName: undefined,
        ownerName: undefined,
        phone: undefined,
        phone1: undefined,
        phone2: undefined,
        category: undefined,
        address: undefined,
        businessLicense: undefined,
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
      this.resetForm("queryForm")
      this.currentCategoryId = '0'
      this.queryParams.category = undefined
      if (this.$refs.categoryTree) {
        this.$refs.categoryTree.setCurrentKey('0')
      }
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.phonebookId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加电话簿"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const phonebookId = row.phonebookId || this.ids
      getPhonebook(phonebookId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改电话簿"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.phonebookId != undefined) {
            updatePhonebook(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addPhonebook(this.form).then(() => {
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
      const phonebookIds = row.phonebookId || this.ids
      this.$modal.confirm('是否确认删除电话簿编号为"' + phonebookIds + '"的数据项？').then(function() {
        return delPhonebook(phonebookIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 状态切换 */
    handleStatusChange(row) {
      const text = row.status === "0" ? "启用" : "停用"
      this.$modal.confirm('确认要"' + text + '""' + row.merchantName + '"吗？').then(function() {
        return changePhonebookStatus({ phonebookId: row.phonebookId, status: row.status })
      }).then(() => {
        this.$modal.msgSuccess(text + "成功")
      }).catch(function() {
        row.status = row.status === "0" ? "1" : "0"
      })
    }
  }
}
</script>

<style scoped>
.category-tree-container {
  background: #fff;
  border-radius: 4px;
  padding: 0;
  height: calc(100vh - 140px);
  overflow: hidden;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}
.tree-header {
  height: 40px;
  line-height: 40px;
  padding: 0 15px;
  border-bottom: 1px solid #e6e6e6;
  background: #fafafa;
  border-radius: 4px 4px 0 0;
}
.tree-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.node-label {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.node-count {
  color: #909399;
  font-size: 12px;
  margin-left: 5px;
}
</style>
