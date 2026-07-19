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
      <el-form-item label="小区名称" prop="communityName">
        <el-input
          v-model="queryParams.communityName"
          placeholder="请输入小区名称"
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
      <el-form-item label="租客姓名" prop="tenantName">
        <el-input
          v-model="queryParams.tenantName"
          placeholder="请输入租客姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="到期状态" prop="expireStatus">
        <el-select v-model="queryParams.expireStatus" placeholder="到期状态" clearable>
          <el-option
            v-for="dict in dict.type.biz_rental_expire_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="到期范围" prop="expireRange">
        <el-select v-model="queryParams.expireRange" placeholder="到期范围" clearable>
          <el-option label="7天内" value="7" />
          <el-option label="30天内" value="30" />
          <el-option label="已过期" value="expired" />
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
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['rental:rentalContract:edit']"
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
          v-hasPermi="['rental:rentalContract:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-refresh"
          size="mini"
          @click="handleRefreshExpire"
          v-hasPermi="['rental:rentalContract:edit']"
        >刷新到期状态</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['rental:rentalContract:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="rentalList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="租赁编号" align="center" prop="rentalId" />
      <el-table-column label="房源标题" align="center" prop="houseTitle" show-overflow-tooltip />
      <el-table-column label="小区名称" align="center" prop="communityName" show-overflow-tooltip />
      <el-table-column label="房东姓名" align="center" prop="landlordName" />
      <el-table-column label="房东手机号" align="center" prop="landlordPhone" />
      <el-table-column label="租客姓名" align="center" prop="tenantName" />
      <el-table-column label="租客手机号" align="center" prop="tenantPhone" />
      <el-table-column label="租期" align="center" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }} ~ {{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="月租金" align="center" prop="monthlyRent" />
      <el-table-column label="租期(月)" align="center" prop="rentPeriod" />
      <el-table-column label="剩余天数" align="center" prop="remainingDays">
        <template slot-scope="scope">
          <span :style="getRemainingDaysStyle(scope.row.remainingDays)">{{ scope.row.remainingDays }}</span>
        </template>
      </el-table-column>
      <el-table-column label="到期状态" align="center" prop="expireStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_rental_expire_status" :value="scope.row.expireStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="合同编号" align="center" prop="contractNo" />
      <el-table-column label="租赁状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_rental_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['rental:rentalContract:query']"
          >查看详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['rental:rentalContract:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['rental:rentalContract:remove']"
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

    <!-- 在租房屋详情对话框 -->
    <el-dialog :title="title" :visible.sync="viewOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="租赁编号">{{ form.rentalId }}</el-descriptions-item>
        <el-descriptions-item label="合同编号">{{ form.contractNo }}</el-descriptions-item>
        <el-descriptions-item label="房源标题" :span="2">{{ form.houseTitle }}</el-descriptions-item>
        <el-descriptions-item label="小区名称">{{ form.communityName }}</el-descriptions-item>
        <el-descriptions-item label="详细地址">{{ form.houseAddress }}</el-descriptions-item>
        <el-descriptions-item label="房东姓名">{{ form.landlordName }}</el-descriptions-item>
        <el-descriptions-item label="房东手机号">{{ form.landlordPhone }}</el-descriptions-item>
        <el-descriptions-item label="租客姓名">{{ form.tenantName }}</el-descriptions-item>
        <el-descriptions-item label="租客手机号">{{ form.tenantPhone }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ parseTime(form.startDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ parseTime(form.endDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="月租金">{{ form.monthlyRent }}</el-descriptions-item>
        <el-descriptions-item label="租期(月)">{{ form.rentPeriod }}</el-descriptions-item>
        <el-descriptions-item label="剩余天数">
          <span :style="getRemainingDaysStyle(form.remainingDays)">{{ form.remainingDays }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="到期状态">
          <dict-tag :options="dict.type.biz_rental_expire_status" :value="form.expireStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="租赁状态">
          <dict-tag :options="dict.type.biz_rental_status" :value="form.status"/>
        </el-descriptions-item>
        <el-descriptions-item label="押金">{{ form.deposit }}</el-descriptions-item>
        <el-descriptions-item label="支付方式">{{ form.payMethod }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(form.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建者">{{ form.createBy }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ form.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 修改在租房屋对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="合同编号" prop="contractNo">
              <el-input v-model="form.contractNo" placeholder="请输入合同编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房源标题" prop="houseTitle">
              <el-input v-model="form.houseTitle" placeholder="请输入房源标题" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="房东姓名" prop="landlordName">
              <el-input v-model="form.landlordName" placeholder="请输入房东姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房东手机号" prop="landlordPhone">
              <el-input v-model="form.landlordPhone" placeholder="请输入房东手机号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="租客姓名" prop="tenantName">
              <el-input v-model="form.tenantName" placeholder="请输入租客姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="租客手机号" prop="tenantPhone">
              <el-input v-model="form.tenantPhone" placeholder="请输入租客手机号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="form.startDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择开始日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker
                v-model="form.endDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择结束日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="月租金" prop="monthlyRent">
              <el-input-number v-model="form.monthlyRent" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="租期(月)" prop="rentPeriod">
              <el-input-number v-model="form.rentPeriod" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="押金" prop="deposit">
              <el-input-number v-model="form.deposit" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="租赁状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择租赁状态">
                <el-option
                  v-for="dict in dict.type.biz_rental_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRentalContract, getRentalContract, delRentalContract, updateRentalContract, refreshExpire } from "@/api/rental/rentalContract"

export default {
  name: "RentalContract",
  dicts: ['biz_rental_expire_status', 'biz_rental_status'],
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
      // 在租房屋表格数据
      rentalList: [],
      // 弹出层标题
      title: "",
      // 是否显示详情弹出层
      viewOpen: false,
      // 是否显示修改弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        houseTitle: undefined,
        communityName: undefined,
        landlordName: undefined,
        tenantName: undefined,
        expireStatus: undefined,
        expireRange: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        contractNo: [
          { required: true, message: "合同编号不能为空", trigger: "blur" }
        ],
        houseTitle: [
          { required: true, message: "房源标题不能为空", trigger: "blur" }
        ],
        landlordName: [
          { required: true, message: "房东姓名不能为空", trigger: "blur" }
        ],
        tenantName: [
          { required: true, message: "租客姓名不能为空", trigger: "blur" }
        ],
        startDate: [
          { required: true, message: "开始日期不能为空", trigger: "change" }
        ],
        endDate: [
          { required: true, message: "结束日期不能为空", trigger: "change" }
        ],
        monthlyRent: [
          { required: true, message: "月租金不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询在租房屋列表 */
    getList() {
      this.loading = true
      listRentalContract(this.queryParams).then(response => {
        this.rentalList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 剩余天数染色样式
    getRemainingDaysStyle(days) {
      if (days == null || days === undefined) {
        return {}
      }
      if (days < 0) {
        return { color: '#F56C6C', fontWeight: 'bold' }
      } else if (days < 7) {
        return { color: '#E6A23C', fontWeight: 'bold' }
      } else if (days < 30) {
        return { color: '#E6A23C', fontWeight: 'bold' }
      } else {
        return { color: '#67C23A', fontWeight: 'bold' }
      }
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        rentalId: undefined,
        contractNo: undefined,
        houseTitle: undefined,
        houseAddress: undefined,
        communityName: undefined,
        landlordName: undefined,
        landlordPhone: undefined,
        tenantName: undefined,
        tenantPhone: undefined,
        startDate: undefined,
        endDate: undefined,
        monthlyRent: undefined,
        rentPeriod: undefined,
        deposit: undefined,
        payMethod: undefined,
        remainingDays: undefined,
        expireStatus: undefined,
        status: undefined,
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
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.rentalId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 查看详情按钮操作 */
    handleView(row) {
      this.reset()
      const rentalId = row.rentalId || this.ids
      getRentalContract(rentalId).then(response => {
        this.form = response.data
        this.viewOpen = true
        this.title = "在租房屋详情"
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const rentalId = row.rentalId || this.ids
      getRentalContract(rentalId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改在租房屋"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.rentalId != undefined) {
            updateRentalContract(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const rentalIds = row.rentalId || this.ids
      this.$modal.confirm('是否确认删除租赁编号为"' + rentalIds + '"的数据项？').then(function() {
        return delRentalContract(rentalIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 刷新到期状态 */
    handleRefreshExpire() {
      this.$modal.confirm('是否确认刷新所有在租房屋的到期状态？').then(function() {
        return refreshExpire()
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("刷新成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('rental/rentalContract/export', {
        ...this.queryParams
      }, `rental_rentalContract_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
