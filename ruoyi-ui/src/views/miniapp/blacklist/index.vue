<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="手机号" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input
          v-model="queryParams.nickname"
          placeholder="请输入昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="真实姓名" prop="realName">
        <el-input
          v-model="queryParams.realName"
          placeholder="请输入真实姓名"
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
          v-hasPermi="['miniapp:blacklist:add']"
        >拉黑用户</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleBatchRemove"
          v-hasPermi="['miniapp:blacklist:remove']"
        >批量解除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="blacklistList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="手机号" align="center" prop="phone" />
      <el-table-column label="昵称" align="center" prop="nickname" />
      <el-table-column label="真实姓名" align="center" prop="realName" />
      <el-table-column label="用户类型" align="center" prop="userType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_user_type" :value="scope.row.userType"/>
        </template>
      </el-table-column>
      <el-table-column label="拉黑原因" align="center" prop="blacklistReason" show-overflow-tooltip />
      <el-table-column label="拉黑时间" align="center" prop="blacklistTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.blacklistTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleRemove(scope.row)"
            v-hasPermi="['miniapp:blacklist:remove']"
          >解除黑名单</el-button>
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

    <!-- 拉黑用户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="选择用户" prop="userId">
          <el-select
            v-model="form.userId"
            placeholder="请输入手机号/昵称搜索用户"
            filterable
            remote
            reserve-keyword
            :remote-method="remoteUserSearch"
            :loading="userLoading"
            style="width: 100%"
          >
            <el-option
              v-for="item in userOptions"
              :key="item.userId"
              :label="item.nickname + '(' + (item.phone || '') + ')'"
              :value="item.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="拉黑原因" prop="blacklistReason">
          <el-input
            v-model="form.blacklistReason"
            type="textarea"
            :rows="4"
            placeholder="请输入拉黑原因"
            maxlength="200"
            show-word-limit
          />
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
import { listBlacklist, addBlacklist, removeFromBlacklist } from "@/api/miniapp/blacklist"
import { listUser } from "@/api/miniapp/user"

export default {
  name: "MiniappBlacklist",
  dicts: ['biz_user_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 黑名单表格数据
      blacklistList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 用户下拉加载
      userLoading: false,
      // 用户下拉选项
      userOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        phone: undefined,
        nickname: undefined,
        realName: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "请选择要拉黑的用户", trigger: "change" }
        ],
        blacklistReason: [
          { required: true, message: "拉黑原因不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询黑名单列表 */
    getList() {
      this.loading = true
      listBlacklist(this.queryParams).then(response => {
        this.blacklistList = response.rows
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
        userId: undefined,
        blacklistReason: undefined
      }
      this.userOptions = []
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
      this.ids = selection.map(item => item.userId)
      this.multiple = !selection.length
    },
    /** 拉黑用户按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "拉黑用户"
    },
    /** 远程搜索用户 */
    remoteUserSearch(query) {
      if (query) {
        this.userLoading = true
        listUser({ pageNum: 1, pageSize: 20, keyword: query, status: '0' }).then(response => {
          this.userOptions = response.rows
          this.userLoading = false
        }).catch(() => {
          this.userLoading = false
        })
      } else {
        this.userOptions = []
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addBlacklist(this.form).then(() => {
            this.$modal.msgSuccess("拉黑成功")
            this.open = false
            this.getList()
          })
        }
      })
    },
    /** 解除黑名单按钮操作 */
    handleRemove(row) {
      const userIds = row.userId || this.ids
      this.$modal.confirm('是否确认解除用户编号为"' + userIds + '"的黑名单状态？').then(function() {
        return removeFromBlacklist(userIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("解除成功")
      }).catch(() => {})
    },
    /** 批量解除黑名单 */
    handleBatchRemove() {
      const userIds = this.ids
      this.$modal.confirm('是否确认批量解除选中的"' + userIds.length + '"条黑名单？').then(function() {
        return removeFromBlacklist(userIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("批量解除成功")
      }).catch(() => {})
    }
  }
}
</script>
