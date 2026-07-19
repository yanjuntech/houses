<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="发送方昵称" prop="senderName">
        <el-input
          v-model="queryParams.senderName"
          placeholder="请输入发送方昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接收方昵称" prop="receiverName">
        <el-input
          v-model="queryParams.receiverName"
          placeholder="请输入接收方昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否过滤" prop="isFiltered">
        <el-select v-model="queryParams.isFiltered" placeholder="请选择" clearable>
          <el-option label="否" value="0" />
          <el-option label="是" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="已读状态" prop="readStatus">
        <el-select v-model="queryParams.readStatus" placeholder="请选择已读状态" clearable>
          <el-option
            v-for="dict in dict.type.biz_message_read_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="发送时间">
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
          v-hasPermi="['rental:message:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="messageList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="消息ID" align="center" prop="messageId" />
      <el-table-column label="发送方" align="center" prop="senderName" />
      <el-table-column label="接收方" align="center" prop="receiverName" />
      <el-table-column label="消息内容" align="center" prop="content" show-overflow-tooltip />
      <el-table-column label="是否过滤" align="center" prop="isFiltered">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isFiltered === '1' ? 'danger' : 'info'">
            {{ scope.row.isFiltered === '1' ? '是' : '否' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="已读状态" align="center" prop="readStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_message_read_status" :value="scope.row.readStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="发送时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['rental:message:query']"
          >查看详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-chat-line-round"
            @click="handleHistory(scope.row)"
            v-hasPermi="['rental:message:query']"
          >聊天记录</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['rental:message:remove']"
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

    <!-- 消息详情对话框 -->
    <el-dialog title="消息详情" :visible.sync="viewOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="消息ID">{{ viewForm.messageId }}</el-descriptions-item>
        <el-descriptions-item label="发送方">{{ viewForm.senderName }}</el-descriptions-item>
        <el-descriptions-item label="接收方">{{ viewForm.receiverName }}</el-descriptions-item>
        <el-descriptions-item label="是否过滤">
          <el-tag :type="viewForm.isFiltered === '1' ? 'danger' : 'info'">
            {{ viewForm.isFiltered === '1' ? '是' : '否' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="已读状态">
          <dict-tag :options="dict.type.biz_message_read_status" :value="viewForm.readStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="发送时间">{{ parseTime(viewForm.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建者">{{ viewForm.createBy }}</el-descriptions-item>
        <el-descriptions-item label="消息内容" :span="2">{{ viewForm.content }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 聊天记录对话框 -->
    <el-dialog title="聊天记录" :visible.sync="historyOpen" width="700px" append-to-body>
      <div v-loading="historyLoading" class="chat-container" style="max-height: 500px; overflow-y: auto;">
        <div v-if="historyList.length === 0" style="text-align: center; color: #999; padding: 20px;">
          暂无聊天记录
        </div>
        <div
          v-for="(item, index) in historyList"
          :key="index"
          class="chat-item"
          style="margin-bottom: 12px;"
        >
          <div style="font-size: 12px; color: #999; margin-bottom: 4px;">
            <span>{{ item.senderName }}</span>
            <span style="margin: 0 8px;">-></span>
            <span>{{ item.receiverName }}</span>
            <span style="margin-left: 12px;">{{ parseTime(item.createTime) }}</span>
          </div>
          <div
            :style="{
              display: 'inline-block',
              padding: '8px 12px',
              borderRadius: '4px',
              background: item.isFiltered === '1' ? '#fef0f0' : '#f4f4f5',
              color: item.isFiltered === '1' ? '#f56c6c' : '#303133',
              maxWidth: '80%'
            }"
          >
            {{ item.content }}
            <el-tag
              v-if="item.isFiltered === '1'"
              size="mini"
              type="danger"
              style="margin-left: 8px;"
            >已过滤</el-tag>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="historyOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMessage, getMessage, delMessage, getHistory } from "@/api/rental/message"

export default {
  name: "RentalMessage",
  dicts: ['biz_message_read_status'],
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
      // 消息表格数据
      messageList: [],
      // 详情弹出层
      viewOpen: false,
      // 详情表单
      viewForm: {},
      // 聊天记录弹出层
      historyOpen: false,
      // 聊天记录加载
      historyLoading: false,
      // 聊天记录列表
      historyList: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        senderName: undefined,
        receiverName: undefined,
        isFiltered: undefined,
        readStatus: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询消息列表 */
    getList() {
      this.loading = true
      listMessage(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.messageList = response.rows
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
      this.dateRange = []
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.messageId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 查看详情 */
    handleView(row) {
      const messageId = row.messageId || this.ids
      getMessage(messageId).then(response => {
        this.viewForm = response.data
        this.viewOpen = true
      })
    },
    /** 查看聊天记录 */
    handleHistory(row) {
      this.historyLoading = true
      this.historyOpen = true
      this.historyList = []
      getHistory({ userId1: row.senderId, userId2: row.receiverId }).then(response => {
        this.historyList = response.rows || response.data || []
        this.historyLoading = false
      }).catch(() => {
        this.historyLoading = false
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const messageIds = row.messageId || this.ids
      this.$modal.confirm('是否确认删除消息编号为"' + messageIds + '"的数据项？').then(function() {
        return delMessage(messageIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    }
  }
}
</script>
