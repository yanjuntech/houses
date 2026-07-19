<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="合同标题" prop="contractTitle">
        <el-input
          v-model="queryParams.contractTitle"
          placeholder="请输入合同标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="合同编号" prop="contractNo">
        <el-input
          v-model="queryParams.contractNo"
          placeholder="请输入合同编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="合同状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择合同状态" clearable>
          <el-option
            v-for="dict in dict.type.biz_contract_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="出租方手机号" prop="landlordPhone">
        <el-input
          v-model="queryParams.landlordPhone"
          placeholder="请输入出租方手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="承租方手机号" prop="tenantPhone">
        <el-input
          v-model="queryParams.tenantPhone"
          placeholder="请输入承租方手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['rental:contract:add']"
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
          v-hasPermi="['rental:contract:edit']"
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
          v-hasPermi="['rental:contract:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="contractList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="expand">
        <template slot-scope="scope">
          <el-descriptions :column="3" border size="medium" style="padding: 10px;">
            <el-descriptions-item label="合同编号">{{ scope.row.contractNo || '-' }}</el-descriptions-item>
            <el-descriptions-item label="合同标题" :span="2">{{ scope.row.contractTitle || '-' }}</el-descriptions-item>
            <el-descriptions-item label="房源标题">{{ scope.row.houseTitle || '-' }}</el-descriptions-item>
            <el-descriptions-item label="所属小区">{{ scope.row.communityName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="合同状态">
              <dict-tag :options="dict.type.biz_contract_status" :value="scope.row.status"/>
            </el-descriptions-item>
            <el-descriptions-item label="出租方姓名">{{ scope.row.landlordName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="出租方电话">{{ scope.row.landlordPhone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="承租方姓名">{{ scope.row.tenantName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="承租方电话">{{ scope.row.tenantPhone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="月租金">{{ scope.row.monthlyRent }} 元</el-descriptions-item>
            <el-descriptions-item label="押金">{{ scope.row.deposit || 0 }} 元</el-descriptions-item>
            <el-descriptions-item label="租期">{{ scope.row.rentPeriod || '-' }} 个月</el-descriptions-item>
            <el-descriptions-item label="支付周期">{{ scope.row.payCycle || '-' }}</el-descriptions-item>
            <el-descriptions-item label="合同开始日期">{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</el-descriptions-item>
            <el-descriptions-item label="合同结束日期">{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</el-descriptions-item>
            <el-descriptions-item label="签订时间" :span="2">{{ parseTime(scope.row.signTime) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="PDF路径" :span="3">
              <el-link v-if="scope.row.pdfPath" :href="scope.row.pdfPath" target="_blank" type="primary">下载合同PDF</el-link>
              <span v-else>-</span>
            </el-descriptions-item>
            <el-descriptions-item label="备注" :span="3">{{ scope.row.remark || '-' }}</el-descriptions-item>
            <el-descriptions-item label="创建人">{{ scope.row.createBy || '-' }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ parseTime(scope.row.createTime) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="更新人">{{ scope.row.updateBy || '-' }}</el-descriptions-item>
          </el-descriptions>
        </template>
      </el-table-column>
      <el-table-column label="合同编号ID" align="center" prop="contractId" />
      <el-table-column label="合同编号" align="center" prop="contractNo" :show-overflow-tooltip="true" />
      <el-table-column label="合同标题" align="center" prop="contractTitle" :show-overflow-tooltip="true" />
      <el-table-column label="房源标题" align="center" prop="houseTitle" :show-overflow-tooltip="true" />
      <el-table-column label="所属小区" align="center" prop="communityName" :show-overflow-tooltip="true" />
      <el-table-column label="出租方" align="center" prop="landlordName" />
      <el-table-column label="承租方" align="center" prop="tenantName" />
      <el-table-column label="月租金(元)" align="center" prop="monthlyRent" />
      <el-table-column label="租期(月)" align="center" prop="rentPeriod" />
      <el-table-column label="合同期限" align="center" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }} ~ {{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="合同状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_contract_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="签订时间" align="center" prop="signTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.signTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="240">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
            v-hasPermi="['rental:contract:query']"
          >查看详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['rental:contract:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleDownload(scope.row)"
            v-hasPermi="['rental:contract:export']"
          >下载PDF</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['rental:contract:remove']"
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

    <!-- 添加或修改合同对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="合同标题" prop="contractTitle">
              <el-input v-model="form.contractTitle" placeholder="请输入合同标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出租房源" prop="houseId">
              <el-select v-model="form.houseId" placeholder="请选择房源" style="width: 100%">
                <el-option
                  v-for="item in houseOptions"
                  :key="item.houseId"
                  :label="item.title"
                  :value="item.houseId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="月租金(元)" prop="monthlyRent">
              <el-input v-model="form.monthlyRent" placeholder="请输入月租金" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出租方ID" prop="landlordId">
              <el-input v-model="form.landlordId" placeholder="请输入出租方ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出租方姓名" prop="landlordName">
              <el-input v-model="form.landlordName" placeholder="请输入出租方姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出租方手机" prop="landlordPhone">
              <el-input v-model="form.landlordPhone" placeholder="请输入出租方手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="承租方ID" prop="tenantId">
              <el-input v-model="form.tenantId" placeholder="请输入承租方ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="承租方姓名" prop="tenantName">
              <el-input v-model="form.tenantName" placeholder="请输入承租方姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="承租方手机" prop="tenantPhone">
              <el-input v-model="form.tenantPhone" placeholder="请输入承租方手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="押金(元)" prop="deposit">
              <el-input v-model="form.deposit" placeholder="请输入押金" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="租期(月)" prop="rentPeriod">
              <el-input-number v-model="form.rentPeriod" controls-position="right" :min="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="form.startDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择开始日期"
                style="width: 100%"
              ></el-date-picker>
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
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="支付周期" prop="payCycle">
              <el-input v-model="form.payCycle" placeholder="请输入支付周期（如：押一付三）" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="合同内容" prop="contractContent">
              <el-input v-model="form.contractContent" type="textarea" :rows="6" placeholder="请输入合同内容" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 合同详情对话框 -->
    <el-dialog title="合同详情" :visible.sync="detailOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="合同编号">{{ detailForm.contractNo }}</el-descriptions-item>
        <el-descriptions-item label="合同标题">{{ detailForm.contractTitle }}</el-descriptions-item>
        <el-descriptions-item label="房源标题">{{ detailForm.houseTitle }}</el-descriptions-item>
        <el-descriptions-item label="所属小区">{{ detailForm.communityName }}</el-descriptions-item>
        <el-descriptions-item label="出租方姓名">{{ detailForm.landlordName }}</el-descriptions-item>
        <el-descriptions-item label="出租方手机号">{{ detailForm.landlordPhone }}</el-descriptions-item>
        <el-descriptions-item label="承租方姓名">{{ detailForm.tenantName }}</el-descriptions-item>
        <el-descriptions-item label="承租方手机号">{{ detailForm.tenantPhone }}</el-descriptions-item>
        <el-descriptions-item label="月租金(元)">{{ detailForm.monthlyRent }}</el-descriptions-item>
        <el-descriptions-item label="押金(元)">{{ detailForm.deposit }}</el-descriptions-item>
        <el-descriptions-item label="租期(月)">{{ detailForm.rentPeriod }}</el-descriptions-item>
        <el-descriptions-item label="支付周期">{{ detailForm.payCycle }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ parseTime(detailForm.startDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ parseTime(detailForm.endDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="合同状态">
          <dict-tag :options="dict.type.biz_contract_status" :value="detailForm.status"/>
        </el-descriptions-item>
        <el-descriptions-item label="签订时间">{{ parseTime(detailForm.signTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ parseTime(detailForm.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="合同内容" :span="2">{{ detailForm.contractContent }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">签名列表</el-divider>
      <el-table :data="detailForm.signList || []" border>
        <el-table-column label="签名姓名" align="center" prop="signerName" />
        <el-table-column label="签名角色" align="center" prop="signerRole" />
        <el-table-column label="签名图片" align="center" prop="signatureImage">
          <template slot-scope="scope">
            <el-image
              v-if="scope.row.signatureImage"
              style="width: 100px; height: 50px;"
              :src="scope.row.signatureImage"
              :preview-src-list="[scope.row.signatureImage]"
              fit="contain"
            ></el-image>
          </template>
        </el-table-column>
        <el-table-column label="签名时间" align="center" prop="signTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.signTime) }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listContract, getContract, addContract, updateContract, delContract, downloadContract } from "@/api/rental/contract"
import { listHouse } from "@/api/rental/house"

export default {
  name: "Contract",
  dicts: ['biz_contract_status'],
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
      // 合同表格数据
      contractList: [],
      // 房源下拉数据
      houseOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 详情弹窗
      detailOpen: false,
      // 详情表单
      detailForm: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contractTitle: undefined,
        contractNo: undefined,
        status: undefined,
        landlordPhone: undefined,
        tenantPhone: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        contractTitle: [
          { required: true, message: "合同标题不能为空", trigger: "blur" }
        ],
        houseId: [
          { required: true, message: "出租房源不能为空", trigger: "change" }
        ],
        landlordName: [
          { required: true, message: "出租方姓名不能为空", trigger: "blur" }
        ],
        landlordPhone: [
          { required: true, message: "出租方手机号不能为空", trigger: "blur" }
        ],
        tenantName: [
          { required: true, message: "承租方姓名不能为空", trigger: "blur" }
        ],
        tenantPhone: [
          { required: true, message: "承租方手机号不能为空", trigger: "blur" }
        ],
        monthlyRent: [
          { required: true, message: "月租金不能为空", trigger: "blur" }
        ],
        rentPeriod: [
          { required: true, message: "租期不能为空", trigger: "blur" }
        ],
        startDate: [
          { required: true, message: "开始日期不能为空", trigger: "change" }
        ],
        endDate: [
          { required: true, message: "结束日期不能为空", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getHouseOptions()
  },
  methods: {
    /** 查询合同列表 */
    getList() {
      this.loading = true
      listContract(this.queryParams).then(response => {
        this.contractList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 查询房源下拉数据 */
    getHouseOptions() {
      listHouse({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.houseOptions = response.rows || []
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
        contractId: undefined,
        contractNo: undefined,
        contractTitle: undefined,
        houseId: undefined,
        landlordId: undefined,
        landlordName: undefined,
        landlordPhone: undefined,
        tenantId: undefined,
        tenantName: undefined,
        tenantPhone: undefined,
        monthlyRent: undefined,
        deposit: undefined,
        rentPeriod: 12,
        startDate: undefined,
        endDate: undefined,
        payCycle: undefined,
        contractContent: undefined
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
      this.ids = selection.map(item => item.contractId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加合同"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const contractId = row.contractId || this.ids
      getContract(contractId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改合同"
      })
    },
    /** 查看详情 */
    handleDetail(row) {
      const contractId = row.contractId
      getContract(contractId).then(response => {
        this.detailForm = response.data
        this.detailOpen = true
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.contractId != undefined) {
            updateContract(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addContract(this.form).then(() => {
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
      const contractIds = row.contractId || this.ids
      this.$modal.confirm('是否确认删除合同编号为"' + contractIds + '"的数据项？').then(function() {
        return delContract(contractIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 下载合同PDF */
    handleDownload(row) {
      this.$modal.loading("正在生成PDF，请稍候...")
      downloadContract(row.contractId).then(response => {
        const blob = new Blob([response], { type: 'application/pdf' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '合同_' + (row.contractNo || row.contractId) + '.pdf'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$modal.closeLoading()
        this.$modal.msgSuccess("下载成功")
      }).catch(() => {
        this.$modal.closeLoading()
        this.$modal.msgError("下载失败")
      })
    }
  }
}
</script>
