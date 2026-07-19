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
      <el-form-item label="用户类型" prop="userType">
        <el-select v-model="queryParams.userType" placeholder="用户类型" clearable>
          <el-option
            v-for="dict in dict.type.biz_user_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="认证状态" prop="verifyStatus">
        <el-select v-model="queryParams.verifyStatus" placeholder="认证状态" clearable>
          <el-option
            v-for="dict in dict.type.biz_verify_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="用户状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['miniapp:user:add']"
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
          v-hasPermi="['miniapp:user:edit']"
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
          v-hasPermi="['miniapp:user:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['miniapp:user:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
      <el-table-column type="expand">
        <template slot-scope="props">
          <div class="expand-content">
            <div class="expand-section">
              <div class="section-title">
                <i class="el-icon-user"></i>
                <span>用户基本信息</span>
              </div>
              <el-descriptions :column="2" border size="small">
                <el-descriptions-item label="用户ID">{{ props.row.userId }}</el-descriptions-item>
                <el-descriptions-item label="昵称">{{ props.row.nickname || '-' }}</el-descriptions-item>
                <el-descriptions-item label="头像">
                  <el-image
                    v-if="props.row.avatar"
                    :src="props.row.avatar"
                    :preview-src-list="[props.row.avatar]"
                    fit="cover"
                    style="width: 60px; height: 60px; border-radius: 4px;"
                  >
                    <div slot="error" class="image-slot">
                      <i class="el-icon-picture-outline"></i>
                    </div>
                  </el-image>
                  <span v-else>-</span>
                </el-descriptions-item>
                <el-descriptions-item label="手机号">{{ props.row.phone || '-' }}</el-descriptions-item>
                <el-descriptions-item label="性别">
                  <dict-tag :options="dict.type.sys_user_sex" :value="props.row.gender"/>
                  <span v-if="!props.row.gender">-</span>
                </el-descriptions-item>
                <el-descriptions-item label="真实姓名">{{ props.row.realName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="用户类型">
                  <dict-tag :options="dict.type.biz_user_type" :value="props.row.userType"/>
                </el-descriptions-item>
                <el-descriptions-item label="认证状态">
                  <dict-tag :options="dict.type.biz_verify_status" :value="props.row.verifyStatus"/>
                </el-descriptions-item>
                <el-descriptions-item label="账号状态">
                  <el-tag :type="props.row.status === '0' ? 'success' : props.row.status === '2' ? 'danger' : 'info'" size="small">
                    {{ props.row.status === '0' ? '正常' : props.row.status === '1' ? '停用' : '黑名单' }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="用户标签">
                  <template v-if="props.row.tags">
                    <dict-tag
                      v-for="(tag, index) in props.row.tags.split(',')"
                      :key="index"
                      :options="dict.type.biz_user_tag"
                      :value="tag"
                      style="margin-right: 5px;"
                    />
                  </template>
                  <span v-else>-</span>
                </el-descriptions-item>
              </el-descriptions>
            </div>
            <div class="expand-section">
              <div class="section-title">
                <i class="el-icon-s-data"></i>
                <span>账号信息</span>
              </div>
              <el-descriptions :column="2" border size="small">
                <el-descriptions-item label="微信openid">{{ props.row.openid || '-' }}</el-descriptions-item>
                <el-descriptions-item label="微信unionId">{{ props.row.unionId || '-' }}</el-descriptions-item>
                <el-descriptions-item label="微信昵称">{{ props.row.wechatNickname || '-' }}</el-descriptions-item>
                <el-descriptions-item label="微信头像">
                  <el-image
                    v-if="props.row.wechatAvatar"
                    :src="props.row.wechatAvatar"
                    :preview-src-list="[props.row.wechatAvatar]"
                    fit="cover"
                    style="width: 60px; height: 60px; border-radius: 4px;"
                  >
                    <div slot="error" class="image-slot">
                      <i class="el-icon-picture-outline"></i>
                    </div>
                  </el-image>
                  <span v-else>-</span>
                </el-descriptions-item>
                <el-descriptions-item label="身份证号">{{ props.row.idCard || '-' }}</el-descriptions-item>
                <el-descriptions-item label="身份证认证">
                  <el-tag :type="props.row.idCardVerified === '1' ? 'success' : 'info'" size="small">
                    {{ props.row.idCardVerified === '1' ? '已认证' : '未认证' }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="剩余发布次数">{{ props.row.publishCount || 0 }}</el-descriptions-item>
                <el-descriptions-item label="累计发布次数">{{ props.row.totalPublishCount || 0 }}</el-descriptions-item>
                <el-descriptions-item label="周期结束时间">{{ parseTime(props.row.publishPeriodEnd) || '-' }}</el-descriptions-item>
                <el-descriptions-item label="最后登录IP">{{ props.row.loginIp || '-' }}</el-descriptions-item>
              </el-descriptions>
            </div>
            <div class="expand-section">
              <div class="section-title">
                <i class="el-icon-time"></i>
                <span>时间信息</span>
              </div>
              <el-descriptions :column="2" border size="small">
                <el-descriptions-item label="注册时间">{{ parseTime(props.row.createTime) || '-' }}</el-descriptions-item>
                <el-descriptions-item label="最后登录时间">{{ parseTime(props.row.loginDate) || '-' }}</el-descriptions-item>
                <el-descriptions-item label="拉黑原因">{{ props.row.blacklistReason || '-' }}</el-descriptions-item>
                <el-descriptions-item label="拉黑时间">{{ parseTime(props.row.blacklistTime) || '-' }}</el-descriptions-item>
                <el-descriptions-item label="备注" :span="2">{{ props.row.remark || '-' }}</el-descriptions-item>
              </el-descriptions>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户编号" align="center" prop="userId" />
      <el-table-column label="手机号" align="center" prop="phone" />
      <el-table-column label="昵称" align="center" prop="nickname" />
      <el-table-column label="用户类型" align="center" prop="userType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_user_type" :value="scope.row.userType"/>
        </template>
      </el-table-column>
      <el-table-column label="认证状态" align="center" prop="verifyStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_verify_status" :value="scope.row.verifyStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="真实姓名" align="center" prop="realName" />
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
      <el-table-column label="用户标签" align="center" prop="tags">
        <template slot-scope="scope">
          <template v-if="scope.row.tags">
            <dict-tag
              v-for="(tag, index) in scope.row.tags.split(',')"
              :key="index"
              :options="dict.type.biz_user_tag"
              :value="tag"
              style="margin-right: 5px;"
            />
          </template>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="剩余发布次数" align="center" prop="publishCount" width="100" />
      <el-table-column label="周期结束时间" align="center" prop="publishPeriodEnd" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.publishPeriodEnd) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="累计发布次数" align="center" prop="totalPublishCount" width="100" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['miniapp:user:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="handleVerify(scope.row)"
            v-hasPermi="['miniapp:user:verify']"
          >审核</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit-outline"
            @click="handleAdjustCount(scope.row)"
            v-hasPermi="['miniapp:user:edit']"
          >调整次数</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-date"
            @click="handleExtendPeriod(scope.row)"
            v-hasPermi="['miniapp:user:edit']"
          >延长有效期</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-message"
            @click="handlePushMessage(scope.row)"
            v-hasPermi="['miniapp:message:add']"
          >推送消息</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['miniapp:user:remove']"
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

    <!-- 添加或修改用户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户类型" prop="userType">
          <el-radio-group v-model="form.userType">
            <el-radio
              v-for="dict in dict.type.biz_user_type"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="用户标签" prop="tags">
          <el-select v-model="form.tagsList" multiple placeholder="请选择用户标签" style="width: 100%">
            <el-option
              v-for="dict in dict.type.biz_user_tag"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 实名认证审核对话框 -->
    <el-dialog :title="verifyTitle" :visible.sync="verifyOpen" width="500px" append-to-body>
      <el-form ref="verifyForm" :model="verifyForm" :rules="verifyRules" label-width="100px">
        <el-form-item label="用户昵称" prop="nickname">
          <span>{{ verifyForm.nickname }}</span>
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <span>{{ verifyForm.realName }}</span>
        </el-form-item>
        <el-form-item label="认证状态" prop="verifyStatus">
          <el-radio-group v-model="verifyForm.verifyStatus">
            <el-radio
              v-for="dict in dict.type.biz_verify_status"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注" prop="remark">
          <el-input v-model="verifyForm.remark" type="textarea" placeholder="请输入审核备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitVerify">确 定</el-button>
        <el-button @click="cancelVerify">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 调整发布次数对话框 -->
    <el-dialog title="调整发布次数" :visible.sync="adjustCountOpen" width="500px" append-to-body>
      <el-form ref="adjustCountForm" :model="adjustCountForm" :rules="adjustCountRules" label-width="100px">
        <el-form-item label="用户昵称">
          <span>{{ adjustCountForm.nickname }}</span>
        </el-form-item>
        <el-form-item label="当前次数">
          <span>{{ adjustCountForm.currentCount }}</span>
        </el-form-item>
        <el-form-item label="调整数量" prop="count">
          <el-input-number v-model="adjustCountForm.count" :min="-1000" :max="1000" />
          <div style="font-size: 12px; color: #909399; margin-top: 5px;">正数增加，负数减少</div>
        </el-form-item>
        <el-form-item label="调整原因" prop="reason">
          <el-input v-model="adjustCountForm.reason" type="textarea" placeholder="请输入调整原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAdjustCount">确 定</el-button>
        <el-button @click="cancelAdjustCount">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 延长有效期对话框 -->
    <el-dialog title="延长有效期" :visible.sync="extendPeriodOpen" width="500px" append-to-body>
      <el-form ref="extendPeriodForm" :model="extendPeriodForm" :rules="extendPeriodRules" label-width="100px">
        <el-form-item label="用户昵称">
          <span>{{ extendPeriodForm.nickname }}</span>
        </el-form-item>
        <el-form-item label="当前到期日">
          <span>{{ extendPeriodForm.currentPeriodEnd || '无' }}</span>
        </el-form-item>
        <el-form-item label="延长天数" prop="days">
          <el-input-number v-model="extendPeriodForm.days" :min="1" :max="3650" />
          <div style="font-size: 12px; color: #909399; margin-top: 5px;">在现有基础上延长，支持叠加</div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitExtendPeriod">确 定</el-button>
        <el-button @click="cancelExtendPeriod">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 推送消息对话框 -->
    <el-dialog title="推送消息" :visible.sync="pushMsgOpen" width="500px" append-to-body>
      <el-form ref="pushMsgForm" :model="pushMsgForm" :rules="pushMsgRules" label-width="80px">
        <el-form-item label="接收用户">
          <span>{{ pushMsgForm.nickname }}</span>
        </el-form-item>
        <el-form-item label="消息标题" prop="title">
          <el-input v-model="pushMsgForm.title" placeholder="请输入消息标题" maxlength="200" />
        </el-form-item>
        <el-form-item label="消息内容" prop="content">
          <el-input v-model="pushMsgForm.content" type="textarea" :rows="5" placeholder="请输入消息内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitPushMessage">确 定</el-button>
        <el-button @click="cancelPushMessage">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUser, getUser, delUser, addUser, updateUser, changeUserType, verifyUser, adjustPublishCount, extendPublishPeriod } from "@/api/miniapp/user"
import { pushMessage } from "@/api/miniapp/message"

export default {
  name: "MiniappUser",
  dicts: ['biz_user_type', 'biz_verify_status', 'sys_normal_disable', 'biz_user_tag', 'sys_user_sex'],
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
      // 用户表格数据
      userList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 审核弹出层标题
      verifyTitle: "",
      // 是否显示审核弹出层
      verifyOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        phone: undefined,
        nickname: undefined,
        userType: undefined,
        verifyStatus: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userType: [
          { required: true, message: "用户类型不能为空", trigger: "change" }
        ]
      },
      // 审核表单参数
      verifyForm: {},
      // 审核表单校验
      verifyRules: {
        verifyStatus: [
          { required: true, message: "认证状态不能为空", trigger: "change" }
        ]
      },
      // 调整发布次数对话框
      adjustCountOpen: false,
      adjustCountForm: {},
      adjustCountRules: {
        count: [
          { required: true, message: "调整数量不能为空", trigger: "change" }
        ]
      },
      // 延长有效期对话框
      extendPeriodOpen: false,
      extendPeriodForm: {},
      extendPeriodRules: {
        days: [
          { required: true, message: "延长天数不能为空", trigger: "change" }
        ]
      },
      // 推送消息对话框
      pushMsgOpen: false,
      pushMsgForm: {},
      pushMsgRules: {
        title: [
          { required: true, message: "消息标题不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "消息内容不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true
      listUser(this.queryParams).then(response => {
        this.userList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 用户状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用"
      this.$modal.confirm('确认要"' + text + '""' + row.nickname + '"用户吗？').then(function() {
        return updateUser({ userId: row.userId, status: row.status })
      }).then(() => {
        this.$modal.msgSuccess(text + "成功")
      }).catch(function() {
        row.status = row.status === "0" ? "1" : "0"
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
        userType: undefined,
        tags: undefined,
        tagsList: [],
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
      this.ids = selection.map(item => item.userId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加用户"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const userId = row.userId || this.ids
      getUser(userId).then(response => {
        this.form = {
          userId: response.data.userId,
          userType: response.data.userType,
          tags: response.data.tags,
          tagsList: response.data.tags ? response.data.tags.split(',') : [],
          remark: response.data.remark
        }
        this.open = true
        this.title = "修改身份标签"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const submitData = { ...this.form }
          submitData.tags = this.form.tagsList && this.form.tagsList.length > 0 ? this.form.tagsList.join(',') : ''
          if (this.form.userId != undefined) {
            changeUserType(submitData).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addUser(submitData).then(() => {
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
      const userIds = row.userId || this.ids
      this.$modal.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？').then(function() {
        return delUser(userIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('miniapp/user/export', {
        ...this.queryParams
      }, `miniapp_user_${new Date().getTime()}.xlsx`)
    },
    /** 审核按钮操作 */
    handleVerify(row) {
      this.verifyForm = {
        userId: row.userId,
        nickname: row.nickname,
        realName: row.realName,
        verifyStatus: row.verifyStatus,
        remark: undefined
      }
      this.verifyOpen = true
      this.verifyTitle = "实名认证审核"
    },
    /** 取消审核 */
    cancelVerify() {
      this.verifyOpen = false
      this.verifyForm = {}
      this.resetForm("verifyForm")
    },
    /** 提交审核 */
    submitVerify() {
      this.$refs["verifyForm"].validate(valid => {
        if (valid) {
          verifyUser(this.verifyForm).then(() => {
            this.$modal.msgSuccess("审核成功")
            this.verifyOpen = false
            this.getList()
          })
        }
      })
    },
    /** 调整发布次数按钮操作 */
    handleAdjustCount(row) {
      this.adjustCountForm = {
        userId: row.userId,
        nickname: row.nickname,
        currentCount: row.publishCount || 0,
        count: 0,
        reason: undefined
      }
      this.adjustCountOpen = true
    },
    /** 取消调整发布次数 */
    cancelAdjustCount() {
      this.adjustCountOpen = false
      this.adjustCountForm = {}
      this.resetForm("adjustCountForm")
    },
    /** 提交调整发布次数 */
    submitAdjustCount() {
      this.$refs["adjustCountForm"].validate(valid => {
        if (valid) {
          adjustPublishCount(this.adjustCountForm).then(() => {
            this.$modal.msgSuccess("调整成功")
            this.adjustCountOpen = false
            this.getList()
          })
        }
      })
    },
    /** 延长有效期按钮操作 */
    handleExtendPeriod(row) {
      this.extendPeriodForm = {
        userId: row.userId,
        nickname: row.nickname,
        currentPeriodEnd: row.publishPeriodEnd ? this.parseTime(row.publishPeriodEnd) : '',
        days: 30
      }
      this.extendPeriodOpen = true
    },
    /** 取消延长有效期 */
    cancelExtendPeriod() {
      this.extendPeriodOpen = false
      this.extendPeriodForm = {}
      this.resetForm("extendPeriodForm")
    },
    /** 提交延长有效期 */
    submitExtendPeriod() {
      this.$refs["extendPeriodForm"].validate(valid => {
        if (valid) {
          extendPublishPeriod(this.extendPeriodForm).then(() => {
            this.$modal.msgSuccess("延长成功")
            this.extendPeriodOpen = false
            this.getList()
          })
        }
      })
    },
    /** 推送消息按钮操作 */
    handlePushMessage(row) {
      this.pushMsgForm = {
        userId: row.userId,
        nickname: row.nickname,
        title: undefined,
        content: undefined
      }
      this.pushMsgOpen = true
    },
    /** 取消推送消息 */
    cancelPushMessage() {
      this.pushMsgOpen = false
      this.pushMsgForm = {}
      this.resetForm("pushMsgForm")
    },
    /** 提交推送消息 */
    submitPushMessage() {
      this.$refs["pushMsgForm"].validate(valid => {
        if (valid) {
          pushMessage(this.pushMsgForm).then(() => {
            this.$modal.msgSuccess("推送成功")
            this.pushMsgOpen = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.expand-content {
  padding: 10px 20px;
  background-color: #fafafa;
}

.expand-section {
  margin-bottom: 20px;
}

.expand-section:last-child {
  margin-bottom: 0;
}

.section-title {
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #ebeef5;
}

.section-title i {
  color: #409eff;
  margin-right: 6px;
  font-size: 16px;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 20px;
}
</style>
