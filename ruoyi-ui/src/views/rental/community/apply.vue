<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="98px">
      <el-form-item label="小区名称" prop="communityName">
        <el-input
          v-model="queryParams.communityName"
          placeholder="请输入小区名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请人姓名" prop="applicantName">
        <el-input
          v-model="queryParams.applicantName"
          placeholder="请输入申请人姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请人手机号" prop="applicantPhone">
        <el-input
          v-model="queryParams.applicantPhone"
          placeholder="请输入申请人手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审批状态" prop="applyStatus">
        <el-select v-model="queryParams.applyStatus" placeholder="审批状态" clearable>
          <el-option
            v-for="dict in dict.type.biz_apply_status"
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['rental:communityApply:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="applyList">
      <el-table-column label="申请编号" align="center" prop="applyId" />
      <el-table-column label="小区名称" align="center" prop="communityName" />
      <el-table-column label="所在省" align="center" prop="province" />
      <el-table-column label="所在市" align="center" prop="city" />
      <el-table-column label="所在区" align="center" prop="district" />
      <el-table-column label="详细地址" align="center" prop="address" show-overflow-tooltip />
      <el-table-column label="申请人姓名" align="center" prop="applicantName" />
      <el-table-column label="申请人手机号" align="center" prop="applicantPhone" />
      <el-table-column label="审批状态" align="center" prop="applyStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_apply_status" :value="scope.row.applyStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="审批人" align="center" prop="approveBy" />
      <el-table-column label="审批时间" align="center" prop="approveTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.approveTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['rental:communityApply:query']"
          >查看详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleApprove(scope.row)"
            v-hasPermi="['rental:communityApply:approve']"
          >审批通过</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleReject(scope.row)"
            v-hasPermi="['rental:communityApply:reject']"
          >审批驳回</el-button>
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

    <!-- 查看详情对话框 -->
    <el-dialog :title="viewTitle" :visible.sync="viewOpen" width="600px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="申请编号">{{ viewForm.applyId }}</el-descriptions-item>
        <el-descriptions-item label="小区名称">{{ viewForm.communityName }}</el-descriptions-item>
        <el-descriptions-item label="所在省">{{ viewForm.province }}</el-descriptions-item>
        <el-descriptions-item label="所在市">{{ viewForm.city }}</el-descriptions-item>
        <el-descriptions-item label="所在区">{{ viewForm.district }}</el-descriptions-item>
        <el-descriptions-item label="详细地址">{{ viewForm.address }}</el-descriptions-item>
        <el-descriptions-item label="申请人姓名">{{ viewForm.applicantName }}</el-descriptions-item>
        <el-descriptions-item label="申请人手机号">{{ viewForm.applicantPhone }}</el-descriptions-item>
        <el-descriptions-item label="审批状态">
          <dict-tag :options="dict.type.biz_apply_status" :value="viewForm.applyStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="审批人">{{ viewForm.approveBy }}</el-descriptions-item>
        <el-descriptions-item label="审批时间">{{ parseTime(viewForm.approveTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(viewForm.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ viewForm.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 审批通过确认对话框 -->
    <el-dialog :title="approveTitle" :visible.sync="approveOpen" width="500px" append-to-body>
      <el-form ref="approveForm" :model="approveForm" :rules="approveRules" label-width="100px">
        <el-form-item label="小区名称">
          <span>{{ approveForm.communityName }}</span>
        </el-form-item>
        <el-form-item label="申请人">
          <span>{{ approveForm.applicantName }}（{{ approveForm.applicantPhone }}）</span>
        </el-form-item>
        <el-form-item label="审批备注" prop="remark">
          <el-input v-model="approveForm.remark" type="textarea" placeholder="请输入审批备注（可选）" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitApprove">确 定</el-button>
        <el-button @click="approveOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 审批驳回对话框 -->
    <el-dialog :title="rejectTitle" :visible.sync="rejectOpen" width="500px" append-to-body>
      <el-form ref="rejectForm" :model="rejectForm" :rules="rejectRules" label-width="100px">
        <el-form-item label="小区名称">
          <span>{{ rejectForm.communityName }}</span>
        </el-form-item>
        <el-form-item label="申请人">
          <span>{{ rejectForm.applicantName }}（{{ rejectForm.applicantPhone }}）</span>
        </el-form-item>
        <el-form-item label="驳回原因" prop="rejectReason">
          <el-input v-model="rejectForm.rejectReason" type="textarea" placeholder="请输入驳回原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitReject">确 定</el-button>
        <el-button @click="rejectOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCommunityApply, getCommunityApply, approveCommunityApply, rejectCommunityApply } from "@/api/rental/community"

export default {
  name: "RentalCommunityApply",
  dicts: ['biz_apply_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 申请表格数据
      applyList: [],
      // 查看详情弹出层标题
      viewTitle: "",
      // 是否显示查看详情弹出层
      viewOpen: false,
      // 查看详情表单
      viewForm: {},
      // 审批通过弹出层标题
      approveTitle: "",
      // 是否显示审批通过弹出层
      approveOpen: false,
      // 审批通过表单
      approveForm: {},
      // 审批通过表单校验
      approveRules: {},
      // 审批驳回弹出层标题
      rejectTitle: "",
      // 是否显示审批驳回弹出层
      rejectOpen: false,
      // 审批驳回表单
      rejectForm: {},
      // 审批驳回表单校验
      rejectRules: {
        rejectReason: [
          { required: true, message: "驳回原因不能为空", trigger: "blur" }
        ]
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        communityName: undefined,
        applicantName: undefined,
        applicantPhone: undefined,
        applyStatus: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询申请列表 */
    getList() {
      this.loading = true
      listCommunityApply(this.queryParams).then(response => {
        this.applyList = response.rows
        this.total = response.total
        this.loading = false
      })
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
    /** 查看详情按钮操作 */
    handleView(row) {
      const applyId = row.applyId
      getCommunityApply(applyId).then(response => {
        this.viewForm = response.data
        this.viewOpen = true
        this.viewTitle = "小区申请详情"
      })
    },
    /** 审批通过按钮操作 */
    handleApprove(row) {
      this.approveForm = {
        applyId: row.applyId,
        communityName: row.communityName,
        applicantName: row.applicantName,
        applicantPhone: row.applicantPhone,
        remark: undefined
      }
      this.approveOpen = true
      this.approveTitle = "审批通过确认"
    },
    /** 提交审批通过 */
    submitApprove() {
      this.$refs["approveForm"].validate(valid => {
        if (valid) {
          approveCommunityApply(this.approveForm).then(() => {
            this.$modal.msgSuccess("审批通过成功")
            this.approveOpen = false
            this.getList()
          })
        }
      })
    },
    /** 审批驳回按钮操作 */
    handleReject(row) {
      this.rejectForm = {
        applyId: row.applyId,
        communityName: row.communityName,
        applicantName: row.applicantName,
        applicantPhone: row.applicantPhone,
        rejectReason: undefined
      }
      this.rejectOpen = true
      this.rejectTitle = "审批驳回"
    },
    /** 提交审批驳回 */
    submitReject() {
      this.$refs["rejectForm"].validate(valid => {
        if (valid) {
          rejectCommunityApply(this.rejectForm).then(() => {
            this.$modal.msgSuccess("驳回成功")
            this.rejectOpen = false
            this.getList()
          })
        }
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('rental/communityApply/export', {
        ...this.queryParams
      }, `rental_community_apply_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
