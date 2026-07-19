<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="房源标题" prop="houseTitle">
        <el-input
          v-model="queryParams.houseTitle"
          placeholder="请输入房源标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="租客姓名" prop="tenantName">
        <el-input
          v-model="queryParams.tenantName"
          placeholder="请输入租客姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="房东姓名" prop="landlordName">
        <el-input
          v-model="queryParams.landlordName"
          placeholder="请输入房东姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="维修类型" prop="repairType">
        <el-select v-model="queryParams.repairType" placeholder="请选择维修类型" clearable>
          <el-option
            v-for="dict in dict.type.biz_repair_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="维修状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择维修状态" clearable>
          <el-option
            v-for="dict in dict.type.biz_repair_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
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
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['rental:repair:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="repairList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="维修ID" align="center" prop="repairId" />
      <el-table-column label="房源标题" align="center" prop="houseTitle" show-overflow-tooltip />
      <el-table-column label="租客姓名" align="center" prop="tenantName" />
      <el-table-column label="房东姓名" align="center" prop="landlordName" />
      <el-table-column label="维修类型" align="center" prop="repairType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_repair_type" :value="scope.row.repairType"/>
        </template>
      </el-table-column>
      <el-table-column label="维修描述" align="center" prop="description" show-overflow-tooltip />
      <el-table-column label="维修状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_repair_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="260">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >详情</el-button>
          <!-- status=0 待房东确认 -->
          <el-button
            v-if="scope.row.status === '0'"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleConfirmByLandlord(scope.row)"
            v-hasPermi="['rental:repair:edit']"
          >房东确认维修</el-button>
          <el-button
            v-if="scope.row.status === '0'"
            size="mini"
            type="text"
            icon="el-icon-user"
            @click="handleChooseTenantSelfRepair(scope.row)"
            v-hasPermi="['rental:repair:edit']"
          >选择租客自修</el-button>
          <el-button
            v-if="scope.row.status === '0'"
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleCancel(scope.row)"
            v-hasPermi="['rental:repair:edit']"
          >取消</el-button>
          <!-- status=1 维修中 -->
          <el-button
            v-if="scope.row.status === '1'"
            size="mini"
            type="text"
            icon="el-icon-finished"
            @click="handleComplete(scope.row)"
            v-hasPermi="['rental:repair:edit']"
          >完成维修</el-button>
          <!-- status=2 待租客确认 -->
          <el-button
            v-if="scope.row.status === '2'"
            size="mini"
            type="text"
            icon="el-icon-circle-check"
            @click="handleConfirmByTenant(scope.row)"
            v-hasPermi="['rental:repair:edit']"
          >租客确认完成</el-button>
          <!-- status=4 待租客上传凭证 -->
          <el-button
            v-if="scope.row.status === '4'"
            size="mini"
            type="text"
            icon="el-icon-upload"
            @click="handleUploadVoucher(scope.row)"
            v-hasPermi="['rental:repair:edit']"
          >上传凭证</el-button>
          <!-- status=5 待房东确认报销 -->
          <el-button
            v-if="scope.row.status === '5'"
            size="mini"
            type="text"
            icon="el-icon-coin"
            @click="handleConfirmReimburse(scope.row)"
            v-hasPermi="['rental:repair:edit']"
          >确认报销</el-button>
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

    <!-- 维修详情对话框 -->
    <el-dialog title="维修详情" :visible.sync="viewOpen" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="维修ID">{{ viewForm.repairId }}</el-descriptions-item>
        <el-descriptions-item label="房源标题">{{ viewForm.houseTitle }}</el-descriptions-item>
        <el-descriptions-item label="租客姓名">{{ viewForm.tenantName }}</el-descriptions-item>
        <el-descriptions-item label="房东姓名">{{ viewForm.landlordName }}</el-descriptions-item>
        <el-descriptions-item label="维修类型">
          <dict-tag :options="dict.type.biz_repair_type" :value="viewForm.repairType"/>
        </el-descriptions-item>
        <el-descriptions-item label="维修状态">
          <dict-tag :options="dict.type.biz_repair_status" :value="viewForm.status"/>
        </el-descriptions-item>
        <el-descriptions-item label="维修金额" v-if="viewForm.amount !== undefined && viewForm.amount !== null">
          {{ viewForm.amount }} 元
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(viewForm.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建者">{{ viewForm.createBy }}</el-descriptions-item>
        <el-descriptions-item label="维修描述" :span="2">{{ viewForm.description }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">维修图片</el-divider>
      <div v-if="viewForm.imageUrl">
        <el-image
          style="width: 120px; height: 120px; margin-right: 8px;"
          :src="viewForm.imageUrl"
          :preview-src-list="[viewForm.imageUrl]"
          fit="cover"
        />
      </div>
      <div v-else style="color: #999;">暂无维修图片</div>
      <el-divider content-position="left">报销凭证</el-divider>
      <div v-if="viewForm.voucherImage">
        <el-image
          style="width: 120px; height: 120px; margin-right: 8px;"
          :src="viewForm.voucherImage"
          :preview-src-list="[viewForm.voucherImage]"
          fit="cover"
        />
      </div>
      <div v-else style="color: #999;">暂无凭证图片</div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 上传凭证对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="凭证图片URL" prop="voucherImage">
          <el-input v-model="form.voucherImage" placeholder="请输入凭证图片URL" />
        </el-form-item>
        <el-form-item label="维修金额" prop="amount">
          <el-input-number v-model="form.amount" controls-position="right" :min="0" :precision="2" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitVoucher">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listRepair,
  getRepair,
  delRepair,
  confirmByLandlord,
  chooseTenantSelfRepair,
  cancelRepair,
  completeRepair,
  confirmByTenant,
  uploadVoucher,
  confirmReimburse
} from "@/api/rental/repair"

export default {
  name: "RentalRepair",
  dicts: ['biz_repair_type', 'biz_repair_status'],
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
      // 维修表格数据
      repairList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 详情弹出层
      viewOpen: false,
      // 详情表单
      viewForm: {},
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        voucherImage: [
          { required: true, message: "凭证图片URL不能为空", trigger: "blur" }
        ],
        amount: [
          { required: true, message: "维修金额不能为空", trigger: "blur" }
        ]
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        houseTitle: undefined,
        tenantName: undefined,
        landlordName: undefined,
        repairType: undefined,
        status: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询维修列表 */
    getList() {
      this.loading = true
      listRepair(this.queryParams).then(response => {
        this.repairList = response.rows
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
        repairId: undefined,
        voucherImage: undefined,
        amount: 0
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
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.repairId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 查看详情 */
    handleView(row) {
      const repairId = row.repairId || this.ids
      getRepair(repairId).then(response => {
        this.viewForm = response.data
        this.viewOpen = true
      })
    },
    /** 房东确认维修 */
    handleConfirmByLandlord(row) {
      this.$modal.confirm('是否确认维修该工单？').then(function() {
        return confirmByLandlord(row.repairId)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("操作成功")
      }).catch(() => {})
    },
    /** 房东选择租客自修 */
    handleChooseTenantSelfRepair(row) {
      this.$modal.confirm('是否确认选择租客自修？').then(function() {
        return chooseTenantSelfRepair(row.repairId)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("操作成功")
      }).catch(() => {})
    },
    /** 取消维修 */
    handleCancel(row) {
      this.$modal.confirm('是否取消该维修工单？').then(function() {
        return cancelRepair(row.repairId)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("操作成功")
      }).catch(() => {})
    },
    /** 完成维修 */
    handleComplete(row) {
      this.$modal.confirm('是否确认完成维修？').then(function() {
        return completeRepair(row.repairId)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("操作成功")
      }).catch(() => {})
    },
    /** 租客确认完成 */
    handleConfirmByTenant(row) {
      this.$modal.confirm('是否确认维修已完成？').then(function() {
        return confirmByTenant(row.repairId)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("操作成功")
      }).catch(() => {})
    },
    /** 上传凭证 */
    handleUploadVoucher(row) {
      this.reset()
      this.form.repairId = row.repairId
      this.open = true
      this.title = "上传凭证"
    },
    /** 提交凭证 */
    submitVoucher() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          uploadVoucher(this.form).then(() => {
            this.$modal.msgSuccess("上传成功")
            this.open = false
            this.getList()
          })
        }
      })
    },
    /** 确认报销 */
    handleConfirmReimburse(row) {
      this.$modal.confirm('是否确认报销该维修工单？').then(function() {
        return confirmReimburse(row.repairId)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("操作成功")
      }).catch(() => {})
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const repairIds = row.repairId || this.ids
      this.$modal.confirm('是否确认删除维修编号为"' + repairIds + '"的数据项？').then(function() {
        return delRepair(repairIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    }
  }
}
</script>
