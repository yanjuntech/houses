<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="用户昵称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户手机号" prop="userPhone">
        <el-input
          v-model="queryParams.userPhone"
          placeholder="请输入用户手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品类型" prop="productType">
        <el-select v-model="queryParams.productType" placeholder="商品类型" clearable>
          <el-option
            v-for="dict in dict.type.biz_product_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="生效状态" prop="effectStatus">
        <el-select v-model="queryParams.effectStatus" placeholder="生效状态" clearable>
          <el-option
            v-for="dict in dict.type.biz_exchange_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="兑换时间">
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
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['rental:mallRecord:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['rental:mallRecord:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="记录编号" align="center" prop="recordId" />
      <el-table-column label="用户昵称" align="center" prop="userName" />
      <el-table-column label="用户手机号" align="center" prop="userPhone" />
      <el-table-column label="商品名称" align="center" prop="productName" show-overflow-tooltip />
      <el-table-column label="商品类型" align="center" prop="productType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_product_type" :value="scope.row.productType"/>
        </template>
      </el-table-column>
      <el-table-column label="消耗邀请数" align="center" prop="costInvites" />
      <el-table-column label="兑换时间" align="center" prop="exchangeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.exchangeTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生效状态" align="center" prop="effectStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_exchange_status" :value="scope.row.effectStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['rental:mallRecord:query']"
          >查看详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['rental:mallRecord:remove']"
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

    <!-- 兑换记录详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="记录编号">{{ form.recordId }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ form.productName }}</el-descriptions-item>
        <el-descriptions-item label="用户昵称">{{ form.userName }}</el-descriptions-item>
        <el-descriptions-item label="用户手机号">{{ form.userPhone }}</el-descriptions-item>
        <el-descriptions-item label="商品类型">
          <dict-tag :options="dict.type.biz_product_type" :value="form.productType"/>
        </el-descriptions-item>
        <el-descriptions-item label="消耗邀请数">{{ form.costInvites }}</el-descriptions-item>
        <el-descriptions-item label="生效值">{{ form.effectValue }}</el-descriptions-item>
        <el-descriptions-item label="生效状态">
          <dict-tag :options="dict.type.biz_exchange_status" :value="form.effectStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="兑换时间">{{ parseTime(form.exchangeTime) }}</el-descriptions-item>
        <el-descriptions-item label="生效时间">{{ parseTime(form.effectTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(form.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建者">{{ form.createBy }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ form.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMallRecord, getMallRecord, delMallRecord } from "@/api/rental/mallRecord"

export default {
  name: "RentalMallRecord",
  dicts: ['biz_product_type', 'biz_exchange_status'],
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
      // 兑换记录表格数据
      recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        userPhone: undefined,
        productType: undefined,
        effectStatus: undefined
      },
      // 表单参数
      form: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询兑换记录列表 */
    getList() {
      this.loading = true
      listMallRecord(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.recordList = response.rows
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
        recordId: undefined,
        userName: undefined,
        userPhone: undefined,
        productName: undefined,
        productType: undefined,
        costInvites: undefined,
        effectValue: undefined,
        effectStatus: undefined,
        exchangeTime: undefined,
        effectTime: undefined,
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.recordId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 查看详情按钮操作 */
    handleView(row) {
      this.reset()
      const recordId = row.recordId || this.ids
      getMallRecord(recordId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "兑换记录详情"
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const recordIds = row.recordId || this.ids
      this.$modal.confirm('是否确认删除兑换记录编号为"' + recordIds + '"的数据项？').then(function() {
        return delMallRecord(recordIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('rental/mallRecord/export', {
        ...this.queryParams
      }, `rental_mallRecord_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
