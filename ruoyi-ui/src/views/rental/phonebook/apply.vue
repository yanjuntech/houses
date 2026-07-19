<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="商家名称" prop="merchantName">
        <el-input
          v-model="queryParams.merchantName"
          placeholder="请输入商家名称"
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
      <el-form-item label="审批状态" prop="applyStatus">
        <el-select v-model="queryParams.applyStatus" placeholder="请选择审批状态" clearable>
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="applyList">
      <el-table-column label="申请编号" align="center" prop="applyId" />
      <el-table-column label="商家名称" align="center" prop="merchantName" :show-overflow-tooltip="true" />
      <el-table-column label="负责人" align="center" prop="ownerName" />
      <el-table-column label="电话" align="center" prop="phone" />
      <el-table-column label="分类" align="center" prop="category" />
      <el-table-column label="地址" align="center" prop="address" :show-overflow-tooltip="true" />
      <el-table-column label="申请人姓名" align="center" prop="applicantName" />
      <el-table-column label="申请人电话" align="center" prop="applicantPhone" />
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
            @click="handleDetail(scope.row)"
          >查看详情</el-button>
          <el-button
            v-if="scope.row.applyStatus === '0'"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleApprove(scope.row)"
            v-hasPermi="['rental:phonebookApply:approve']"
          >审批通过</el-button>
          <el-button
            v-if="scope.row.applyStatus === '0'"
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleReject(scope.row)"
            v-hasPermi="['rental:phonebookApply:approve']"
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

    <!-- 详情对话框 -->
    <el-dialog title="申请详情" :visible.sync="detailOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="商家名称">{{ detailForm.merchantName }}</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ detailForm.ownerName }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ detailForm.phone }}</el-descriptions-item>
        <el-descriptions-item label="分类">{{ detailForm.category }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ detailForm.address }}</el-descriptions-item>
        <el-descriptions-item label="申请人姓名">{{ detailForm.applicantName }}</el-descriptions-item>
        <el-descriptions-item label="申请人电话">{{ detailForm.applicantPhone }}</el-descriptions-item>
        <el-descriptions-item label="审批状态">
          <dict-tag :options="dict.type.biz_apply_status" :value="detailForm.applyStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="审批人">{{ detailForm.approveBy }}</el-descriptions-item>
        <el-descriptions-item label="审批时间">{{ parseTime(detailForm.approveTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(detailForm.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="驳回原因" :span="2">{{ detailForm.rejectReason }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">营业执照</el-divider>
      <div style="text-align: center;">
        <el-image
          v-if="detailForm.businessLicense"
          style="width: 400px; height: 300px;"
          :src="detailForm.businessLicense"
          :preview-src-list="[detailForm.businessLicense]"
          fit="contain"
        ></el-image>
        <span v-else>暂无营业执照图片</span>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 驳回原因对话框 -->
    <el-dialog title="驳回原因" :visible.sync="rejectOpen" width="500px" append-to-body>
      <el-form ref="rejectForm" :model="rejectForm" :rules="rejectRules" label-width="100px">
        <el-form-item label="驳回原因" prop="rejectReason">
          <el-input v-model="rejectForm.rejectReason" type="textarea" :rows="4" placeholder="请输入驳回原因" />
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
import { listPhonebookApply, getPhonebookApply, approvePhonebookApply, rejectPhonebookApply } from "@/api/rental/phonebookApply"

export default {
  name: "PhonebookApply",
  dicts: ['biz_apply_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 申请列表数据
      applyList: [],
      // 详情弹窗
      detailOpen: false,
      // 详情表单
      detailForm: {},
      // 驳回弹窗
      rejectOpen: false,
      // 驳回表单
      rejectForm: {
        applyId: undefined,
        rejectReason: undefined
      },
      // 驳回表单校验
      rejectRules: {
        rejectReason: [
          { required: true, message: "驳回原因不能为空", trigger: "blur" }
        ]
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        merchantName: undefined,
        applicantName: undefined,
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
      listPhonebookApply(this.queryParams).then(response => {
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
    /** 查看详情 */
    handleDetail(row) {
      const applyId = row.applyId
      getPhonebookApply(applyId).then(response => {
        this.detailForm = response.data
        this.detailOpen = true
      })
    },
    /** 审批通过 */
    handleApprove(row) {
      this.$modal.confirm('是否确认通过商家申请"' + row.merchantName + '"？').then(function() {
        return approvePhonebookApply({ applyId: row.applyId })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("审批通过成功")
      }).catch(() => {})
    },
    /** 审批驳回 */
    handleReject(row) {
      this.rejectForm = {
        applyId: row.applyId,
        rejectReason: undefined
      }
      this.rejectOpen = true
    },
    /** 提交驳回 */
    submitReject() {
      this.$refs["rejectForm"].validate(valid => {
        if (valid) {
          rejectPhonebookApply(this.rejectForm).then(() => {
            this.rejectOpen = false
            this.getList()
            this.$modal.msgSuccess("驳回成功")
          })
        }
      })
    }
  }
}
</script>
