<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="邀请人昵称" prop="inviterName">
        <el-input
          v-model="queryParams.inviterName"
          placeholder="请输入邀请人昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邀请人手机号" prop="inviterPhone">
        <el-input
          v-model="queryParams.inviterPhone"
          placeholder="请输入邀请人手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="被邀请人昵称" prop="inviteeName">
        <el-input
          v-model="queryParams.inviteeName"
          placeholder="请输入被邀请人昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="被邀请人手机号" prop="inviteePhone">
        <el-input
          v-model="queryParams.inviteePhone"
          placeholder="请输入被邀请人手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邀请状态" prop="inviteStatus">
        <el-select v-model="queryParams.inviteStatus" placeholder="邀请状态" clearable>
          <el-option
            v-for="dict in dict.type.biz_invite_status"
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
          v-hasPermi="['rental:invite:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['rental:invite:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inviteList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="关系编号" align="center" prop="relationId" />
      <el-table-column label="邀请人昵称" align="center" prop="inviterName" />
      <el-table-column label="邀请人手机号" align="center" prop="inviterPhone" />
      <el-table-column label="被邀请人昵称" align="center" prop="inviteeName" />
      <el-table-column label="被邀请人手机号" align="center" prop="inviteePhone" />
      <el-table-column label="邀请码" align="center" prop="inviteCode" />
      <el-table-column label="邀请状态" align="center" prop="inviteStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_invite_status" :value="scope.row.inviteStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="邀请时间" align="center" prop="inviteTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.inviteTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="认证时间" align="center" prop="certifiedTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.certifiedTime) }}</span>
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
            v-hasPermi="['rental:invite:query']"
          >查看详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['rental:invite:remove']"
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

    <!-- 邀请详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <!-- 顶部统计卡片 -->
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <el-card shadow="hover">
            <div slot="header">总邀请人数</div>
            <div class="statistics-value">{{ statistics.totalCount || 0 }}</div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover">
            <div slot="header">已认证人数</div>
            <div class="statistics-value">{{ statistics.certifiedCount || 0 }}</div>
          </el-card>
        </el-col>
      </el-row>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="关系编号">{{ form.relationId }}</el-descriptions-item>
        <el-descriptions-item label="邀请码">{{ form.inviteCode }}</el-descriptions-item>
        <el-descriptions-item label="邀请人昵称">{{ form.inviterName }}</el-descriptions-item>
        <el-descriptions-item label="邀请人手机号">{{ form.inviterPhone }}</el-descriptions-item>
        <el-descriptions-item label="被邀请人昵称">{{ form.inviteeName }}</el-descriptions-item>
        <el-descriptions-item label="被邀请人手机号">{{ form.inviteePhone }}</el-descriptions-item>
        <el-descriptions-item label="邀请状态">
          <dict-tag :options="dict.type.biz_invite_status" :value="form.inviteStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="邀请时间">{{ parseTime(form.inviteTime) }}</el-descriptions-item>
        <el-descriptions-item label="认证时间">{{ parseTime(form.certifiedTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(form.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建者">{{ form.createBy }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ form.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listInvite, getInvite, delInvite, inviteStatistics } from "@/api/rental/invite"

export default {
  name: "RentalInvite",
  dicts: ['biz_invite_status'],
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
      // 邀请关系表格数据
      inviteList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 统计数据
      statistics: {
        totalCount: 0,
        certifiedCount: 0
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        inviterName: undefined,
        inviterPhone: undefined,
        inviteeName: undefined,
        inviteePhone: undefined,
        inviteStatus: undefined
      },
      // 表单参数
      form: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询邀请关系列表 */
    getList() {
      this.loading = true
      listInvite(this.queryParams).then(response => {
        this.inviteList = response.rows
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
        relationId: undefined,
        inviterName: undefined,
        inviterPhone: undefined,
        inviteeName: undefined,
        inviteePhone: undefined,
        inviteCode: undefined,
        inviteStatus: undefined,
        inviteTime: undefined,
        certifiedTime: undefined,
        remark: undefined
      }
      this.statistics = {
        totalCount: 0,
        certifiedCount: 0
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
      this.ids = selection.map(item => item.relationId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 查看详情按钮操作 */
    handleView(row) {
      this.reset()
      const relationId = row.relationId || this.ids
      getInvite(relationId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "邀请详情"
        // 加载邀请人统计数据
        if (this.form.inviterId) {
          this.loadStatistics(this.form.inviterId)
        }
      })
    },
    /** 加载邀请人统计数据 */
    loadStatistics(inviterId) {
      inviteStatistics(inviterId).then(response => {
        this.statistics = response.data || { totalCount: 0, certifiedCount: 0 }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const relationIds = row.relationId || this.ids
      this.$modal.confirm('是否确认删除邀请关系编号为"' + relationIds + '"的数据项？').then(function() {
        return delInvite(relationIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('rental/invite/export', {
        ...this.queryParams
      }, `rental_invite_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.statistics-value {
  font-size: 28px;
  font-weight: bold;
  color: #409EFF;
  text-align: center;
  padding: 10px 0;
}
</style>
