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

    <el-row :gutter="16" class="mb8 statistics-cards">
      <el-col :span="8">
        <div class="stat-card stat-card-blue">
          <div class="stat-card-icon">
            <i class="el-icon-user"></i>
          </div>
          <div class="stat-card-content">
            <div class="stat-card-label">总邀请人数</div>
            <div class="stat-card-value">{{ totalStatistics.totalCount || 0 }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card stat-card-green">
          <div class="stat-card-icon">
            <i class="el-icon-circle-check"></i>
          </div>
          <div class="stat-card-content">
            <div class="stat-card-label">已认证人数</div>
            <div class="stat-card-value">{{ totalStatistics.certifiedCount || 0 }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card stat-card-orange">
          <div class="stat-card-icon">
            <i class="el-icon-data-line"></i>
          </div>
          <div class="stat-card-content">
            <div class="stat-card-label">认证率</div>
            <div class="stat-card-value">{{ certificationRate }}%</div>
          </div>
        </div>
      </el-col>
    </el-row>

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
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-descriptions :column="2" border size="small" class="expand-descriptions">
            <el-descriptions-item label="关系编号">{{ props.row.relationId }}</el-descriptions-item>
            <el-descriptions-item label="邀请码">{{ props.row.inviteCode }}</el-descriptions-item>
            <el-descriptions-item label="邀请人">
              <span>{{ props.row.inviterName }}</span>
              <span style="color: #909399; margin-left: 8px;">{{ props.row.inviterPhone }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="被邀请人">
              <span>{{ props.row.inviteeName }}</span>
              <span style="color: #909399; margin-left: 8px;">{{ props.row.inviteePhone }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="邀请状态">
              <dict-tag :options="dict.type.biz_invite_status" :value="String(props.row.inviteStatus)"/>
            </el-descriptions-item>
            <el-descriptions-item label="邀请时间">{{ parseTime(props.row.inviteTime) }}</el-descriptions-item>
            <el-descriptions-item label="认证时间">{{ parseTime(props.row.certifiedTime) }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ parseTime(props.row.createTime) }}</el-descriptions-item>
            <el-descriptions-item label="创建者">{{ props.row.createBy }}</el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ props.row.remark || '-' }}</el-descriptions-item>
          </el-descriptions>
        </template>
      </el-table-column>
      <el-table-column label="关系编号" align="center" prop="relationId" width="80" />
      <el-table-column label="邀请人" align="center" width="180">
        <template slot-scope="scope">
          <div>{{ scope.row.inviterName }}</div>
          <div style="color: #909399; font-size: 12px;">{{ scope.row.inviterPhone }}</div>
        </template>
      </el-table-column>
      <el-table-column label="被邀请人" align="center" width="180">
        <template slot-scope="scope">
          <div>{{ scope.row.inviteeName }}</div>
          <div style="color: #909399; font-size: 12px;">{{ scope.row.inviteePhone }}</div>
        </template>
      </el-table-column>
      <el-table-column label="邀请码" align="center" prop="inviteCode" width="100" />
      <el-table-column label="邀请状态" align="center" prop="inviteStatus" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_invite_status" :value="String(scope.row.inviteStatus)"/>
        </template>
      </el-table-column>
      <el-table-column label="邀请时间" align="center" prop="inviteTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.inviteTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150" fixed="right" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['rental:invite:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            style="color: #F56C6C;"
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

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-row :gutter="16" class="mb8 dialog-statistics">
        <el-col :span="8">
          <div class="stat-card stat-card-blue stat-card-sm">
            <div class="stat-card-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-card-content">
              <div class="stat-card-label">总邀请人数</div>
              <div class="stat-card-value">{{ statistics.totalCount || 0 }}</div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-card stat-card-green stat-card-sm">
            <div class="stat-card-icon">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="stat-card-content">
              <div class="stat-card-label">已认证人数</div>
              <div class="stat-card-value">{{ statistics.certifiedCount || 0 }}</div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-card stat-card-orange stat-card-sm">
            <div class="stat-card-icon">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="stat-card-content">
              <div class="stat-card-label">认证率</div>
              <div class="stat-card-value">{{ dialogCertificationRate }}%</div>
            </div>
          </div>
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
          <dict-tag :options="dict.type.biz_invite_status" :value="String(form.inviteStatus)"/>
        </el-descriptions-item>
        <el-descriptions-item label="邀请时间">{{ parseTime(form.inviteTime) }}</el-descriptions-item>
        <el-descriptions-item label="认证时间">{{ parseTime(form.certifiedTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(form.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建者">{{ form.createBy }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ form.remark || '-' }}</el-descriptions-item>
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
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      inviteList: [],
      title: "",
      open: false,
      statistics: {
        totalCount: 0,
        certifiedCount: 0
      },
      totalStatistics: {
        totalCount: 0,
        certifiedCount: 0
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        inviterName: undefined,
        inviterPhone: undefined,
        inviteeName: undefined,
        inviteePhone: undefined,
        inviteStatus: undefined
      },
      form: {}
    }
  },
  computed: {
    certificationRate() {
      const total = this.totalStatistics.totalCount || 0
      const certified = this.totalStatistics.certifiedCount || 0
      if (total === 0) return '0.0'
      return ((certified / total) * 100).toFixed(1)
    },
    dialogCertificationRate() {
      const total = this.statistics.totalCount || 0
      const certified = this.statistics.certifiedCount || 0
      if (total === 0) return '0.0'
      return ((certified / total) * 100).toFixed(1)
    }
  },
  created() {
    this.getList()
    this.loadTotalStatistics()
  },
  methods: {
    getList() {
      this.loading = true
      listInvite(this.queryParams).then(response => {
        this.inviteList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
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
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.relationId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleView(row) {
      this.reset()
      const relationId = row.relationId || this.ids
      getInvite(relationId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "邀请详情"
        if (this.form.inviterId) {
          this.loadStatistics(this.form.inviterId)
        }
      })
    },
    loadStatistics(inviterId) {
      inviteStatistics(inviterId).then(response => {
        this.statistics = response.data || { totalCount: 0, certifiedCount: 0 }
      })
    },
    loadTotalStatistics() {
      inviteStatistics().then(response => {
        this.totalStatistics = response.data || { totalCount: 0, certifiedCount: 0 }
      }).catch(() => {
        this.totalStatistics = { totalCount: 0, certifiedCount: 0 }
      })
    },
    handleDelete(row) {
      const relationIds = row.relationId || this.ids
      this.$modal.confirm('是否确认删除邀请关系编号为"' + relationIds + '"的数据项？').then(function() {
        return delInvite(relationIds)
      }).then(() => {
        this.getList()
        this.loadTotalStatistics()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('rental/invite/export', {
        ...this.queryParams
      }, `rental_invite_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.statistics-cards {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  border-radius: 12px;
  color: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

.stat-card-sm {
  padding: 14px;
}

.stat-card-icon {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  flex-shrink: 0;
}

.stat-card-sm .stat-card-icon {
  width: 44px;
  height: 44px;
  margin-right: 12px;
}

.stat-card-icon i {
  font-size: 28px;
}

.stat-card-sm .stat-card-icon i {
  font-size: 22px;
}

.stat-card-content {
  flex: 1;
}

.stat-card-label {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 6px;
}

.stat-card-sm .stat-card-label {
  font-size: 13px;
  margin-bottom: 4px;
}

.stat-card-value {
  font-size: 28px;
  font-weight: bold;
  line-height: 1.2;
}

.stat-card-sm .stat-card-value {
  font-size: 22px;
}

.stat-card-blue {
  background: linear-gradient(135deg, #667eea 0%, #409EFF 100%);
}

.stat-card-green {
  background: linear-gradient(135deg, #11998e 0%, #67C23A 100%);
}

.stat-card-orange {
  background: linear-gradient(135deg, #f093fb 0%, #E6A23C 100%);
}

.expand-descriptions {
  margin: 10px 0;
}

.dialog-statistics {
  margin-bottom: 20px;
}
</style>
