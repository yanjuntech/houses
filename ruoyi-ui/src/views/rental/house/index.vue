<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="房源标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入房源标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="租赁类型" prop="houseType">
        <el-select v-model="queryParams.houseType" placeholder="请选择租赁类型" clearable>
          <el-option
            v-for="dict in dict.type.biz_house_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="小区" prop="communityId">
        <el-select v-model="queryParams.communityId" placeholder="请选择小区" clearable>
          <el-option
            v-for="item in communityOptions"
            :key="item.communityId"
            :label="item.communityName"
            :value="item.communityId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="房屋状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择房屋状态" clearable>
          <el-option
            v-for="dict in dict.type.biz_house_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="几室" prop="roomCount">
        <el-select v-model="queryParams.roomCount" placeholder="请选择几室" clearable>
          <el-option
            v-for="item in roomCountOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
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
          v-hasPermi="['rental:house:add']"
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
          v-hasPermi="['rental:house:edit']"
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
          v-hasPermi="['rental:house:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="houseList" @selection-change="handleSelectionChange">
      <el-table-column type="expand">
        <template slot-scope="props">
          <div class="expand-content">
            <div class="expand-section">
              <div class="section-title">
                <i class="el-icon-document"></i>
                <span>房屋详情</span>
              </div>
              <el-descriptions :column="3" border size="small">
                <el-descriptions-item label="房屋编号">{{ props.row.houseId }}</el-descriptions-item>
                <el-descriptions-item label="房源标题">{{ props.row.title }}</el-descriptions-item>
                <el-descriptions-item label="房屋状态">
                  <dict-tag :options="dict.type.biz_house_status" :value="props.row.status"/>
                </el-descriptions-item>
                <el-descriptions-item label="租赁类型">
                  <dict-tag :options="dict.type.biz_house_type" :value="props.row.houseType"/>
                </el-descriptions-item>
                <el-descriptions-item label="所属小区">{{ props.row.communityName }}</el-descriptions-item>
                <el-descriptions-item label="户型">{{ props.row.roomCount }}室{{ props.row.hallCount }}厅{{ props.row.bathCount }}卫</el-descriptions-item>
                <el-descriptions-item label="面积(㎡)">{{ props.row.area }}</el-descriptions-item>
                <el-descriptions-item label="租金(元/月)">{{ props.row.price }}</el-descriptions-item>
                <el-descriptions-item label="发布方">
                  <span v-if="props.row.publishUserType === '0'">房东直租</span>
                  <span v-else-if="props.row.publishUserType === '1'">房屋中介</span>
                  <span v-else>-</span>
                </el-descriptions-item>
                <el-descriptions-item label="所在楼层">{{ props.row.floor ? props.row.floor + '层' : '-' }}</el-descriptions-item>
                <el-descriptions-item label="总楼层">{{ props.row.totalFloor ? props.row.totalFloor + '层' : '-' }}</el-descriptions-item>
                <el-descriptions-item label="朝向">{{ props.row.orientation || '-' }}</el-descriptions-item>
                <el-descriptions-item label="装修情况">{{ props.row.decoration || '-' }}</el-descriptions-item>
                <el-descriptions-item label="创建时间">{{ parseTime(props.row.createTime) }}</el-descriptions-item>
                <el-descriptions-item label="更新时间">{{ parseTime(props.row.updateTime) || '-' }}</el-descriptions-item>
                <el-descriptions-item label="创建人">{{ props.row.createBy || '-' }}</el-descriptions-item>
                <el-descriptions-item label="更新人">{{ props.row.updateBy || '-' }}</el-descriptions-item>
                <el-descriptions-item label="房屋标签">
                  <template v-if="props.row.tags">
                    <dict-tag
                      v-for="(tag, index) in props.row.tags.split(',')"
                      :key="index"
                      :options="dict.type.biz_house_tag"
                      :value="tag"
                      style="margin-right: 5px;"
                    />
                  </template>
                  <span v-else>-</span>
                </el-descriptions-item>
                <el-descriptions-item label="房屋描述" :span="3">{{ props.row.description || '-' }}</el-descriptions-item>
                <el-descriptions-item label="备注" :span="3">{{ props.row.remark || '-' }}</el-descriptions-item>
              </el-descriptions>
            </div>
            <div class="expand-section">
              <div class="section-title">
                <i class="el-icon-time"></i>
                <span>审批流程</span>
              </div>
              <div class="timeline-horizontal">
                <div class="timeline-item" v-for="(step, index) in getTimelineSteps(props.row)" :key="index">
                  <div class="timeline-node" :class="step.status">
                    <i :class="step.icon"></i>
                  </div>
                  <div class="timeline-line" v-if="index < getTimelineSteps(props.row).length - 1" :class="step.status"></div>
                  <div class="timeline-content">
                    <div class="timeline-title">{{ step.title }}</div>
                    <div class="timeline-time">{{ step.time }}</div>
                    <div class="timeline-operator">{{ step.operator }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="房屋编号" align="center" prop="houseId" />
      <el-table-column label="房源标题" align="center" prop="title" :show-overflow-tooltip="true" />
      <el-table-column label="租赁类型" align="center" prop="houseType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_house_type" :value="scope.row.houseType"/>
        </template>
      </el-table-column>
      <el-table-column label="所属小区" align="center" prop="communityName" :show-overflow-tooltip="true" />
      <el-table-column label="户型" align="center" prop="roomCount">
        <template slot-scope="scope">
          <span>{{ scope.row.roomCount }}室{{ scope.row.hallCount }}厅{{ scope.row.bathCount }}卫</span>
        </template>
      </el-table-column>
      <el-table-column label="面积(㎡)" align="center" prop="area" />
      <el-table-column label="租金(元/月)" align="center" prop="price" />
      <el-table-column label="发布方" align="center" prop="publishUserType">
        <template slot-scope="scope">
          <span v-if="scope.row.publishUserType === '0'">房东直租</span>
          <span v-else-if="scope.row.publishUserType === '1'">房屋中介</span>
        </template>
      </el-table-column>
      <el-table-column label="房屋状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.biz_house_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="房屋标签" align="center" prop="tags">
        <template slot-scope="scope">
          <template v-if="scope.row.tags">
            <dict-tag
              v-for="(tag, index) in scope.row.tags.split(',')"
              :key="index"
              :options="dict.type.biz_house_tag"
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['rental:house:edit']"
          >修改</el-button>
          <el-button
            v-if="scope.row.status === '0'"
            size="mini"
            type="text"
            icon="el-icon-top"
            @click="handleStatus(scope.row, '1')"
            v-hasPermi="['rental:house:edit']"
          >上架</el-button>
          <el-button
            v-if="scope.row.status === '1'"
            size="mini"
            type="text"
            icon="el-icon-bottom"
            @click="handleStatus(scope.row, '0')"
            v-hasPermi="['rental:house:edit']"
          >下架</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['rental:house:remove']"
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

    <!-- 添加或修改房屋对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="房源标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入房源标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="租赁类型" prop="houseType">
              <el-radio-group v-model="form.houseType">
                <el-radio
                  v-for="dict in dict.type.biz_house_type"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属小区" prop="communityId">
              <el-select v-model="form.communityId" placeholder="请选择小区" @change="handleCommunityChange" style="width: 100%">
                <el-option
                  v-for="item in communityOptions"
                  :key="item.communityId"
                  :label="item.communityName"
                  :value="item.communityId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="几室" prop="roomCount">
              <el-input-number v-model="form.roomCount" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="几厅" prop="hallCount">
              <el-input-number v-model="form.hallCount" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="几卫" prop="bathCount">
              <el-input-number v-model="form.bathCount" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="面积(㎡)" prop="area">
              <el-input v-model="form.area" placeholder="请输入面积" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="租金(元/月)" prop="price">
              <el-input v-model="form.price" placeholder="请输入租金" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在楼层" prop="floor">
              <el-input v-model="form.floor" placeholder="请输入所在楼层" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总楼层" prop="totalFloor">
              <el-input v-model="form.totalFloor" placeholder="请输入总楼层" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="装修情况" prop="decoration">
              <el-input v-model="form.decoration" placeholder="请输入装修情况" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="朝向" prop="orientation">
              <el-input v-model="form.orientation" placeholder="请输入朝向" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="图片地址" prop="images">
              <el-input v-model="form.images" placeholder="请输入图片URL，多个用逗号分隔" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="房屋描述" prop="description">
              <el-input v-model="form.description" type="textarea" placeholder="请输入房屋描述" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房屋状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.biz_house_status"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="房屋标签" prop="tags">
              <el-select v-model="form.tagsList" multiple placeholder="请选择房屋标签" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.biz_house_tag"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listHouse, getHouse, addHouse, updateHouse, delHouse, changeHouseStatus } from "@/api/rental/house"
import { selectAllCommunity } from "@/api/rental/community"

export default {
  name: "House",
  dicts: ['biz_house_type', 'biz_house_status', 'biz_house_tag'],
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
      // 房屋表格数据
      houseList: [],
      // 小区下拉数据
      communityOptions: [],
      // 几室下拉数据
      roomCountOptions: [
        { value: 1, label: "1室" },
        { value: 2, label: "2室" },
        { value: 3, label: "3室" },
        { value: 4, label: "4室" },
        { value: 5, label: "5室" }
      ],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        houseType: undefined,
        communityId: undefined,
        status: undefined,
        roomCount: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "房源标题不能为空", trigger: "blur" }
        ],
        houseType: [
          { required: true, message: "租赁类型不能为空", trigger: "change" }
        ],
        communityId: [
          { required: true, message: "所属小区不能为空", trigger: "change" }
        ],
        area: [
          { required: true, message: "面积不能为空", trigger: "blur" }
        ],
        price: [
          { required: true, message: "租金不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getCommunityOptions()
  },
  methods: {
    /** 获取时间轴步骤数据 */
    getTimelineSteps(row) {
      const steps = []
      steps.push({
        title: '提交申请',
        time: this.parseTime(row.createTime) || '-',
        operator: row.createBy ? '发布人：' + row.createBy : '-',
        icon: 'el-icon-edit',
        status: 'finished'
      })
      steps.push({
        title: '待审批',
        time: this.parseTime(row.createTime) || '-',
        operator: '等待审批',
        icon: 'el-icon-time',
        status: row.status === '0' ? 'process' : 'finished'
      })
      if (row.status === '1' || row.status === '2' || row.status === '3') {
        steps.push({
          title: '审批通过',
          time: this.parseTime(row.updateTime) || '-',
          operator: row.updateBy ? '审核人：' + row.updateBy : '-',
          icon: 'el-icon-circle-check',
          status: 'finished success'
        })
      } else {
        steps.push({
          title: '等待审批',
          time: '-',
          operator: '-',
          icon: 'el-icon-loading',
          status: 'wait'
        })
      }
      return steps
    },
    /** 查询房屋列表 */
    getList() {
      this.loading = true
      listHouse(this.queryParams).then(response => {
        this.houseList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 查询小区下拉数据 */
    getCommunityOptions() {
      selectAllCommunity().then(response => {
        this.communityOptions = response.data || response.rows || []
      })
    },
    // 小区选择变化时联动显示小区名称
    handleCommunityChange(communityId) {
      const community = this.communityOptions.find(item => item.communityId === communityId)
      if (community) {
        this.form.communityName = community.communityName
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
        houseId: undefined,
        title: undefined,
        houseType: undefined,
        communityId: undefined,
        communityName: undefined,
        roomCount: 0,
        hallCount: 0,
        bathCount: 0,
        area: undefined,
        floor: undefined,
        totalFloor: undefined,
        decoration: undefined,
        orientation: undefined,
        price: undefined,
        images: undefined,
        description: undefined,
        status: "0",
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
      this.ids = selection.map(item => item.houseId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加房屋"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const houseId = row.houseId || this.ids
      getHouse(houseId).then(response => {
        this.form = response.data
        this.form.tagsList = response.data.tags ? response.data.tags.split(',') : []
        this.open = true
        this.title = "修改房屋"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const submitData = { ...this.form }
          submitData.tags = this.form.tagsList && this.form.tagsList.length > 0 ? this.form.tagsList.join(',') : ''
          if (this.form.houseId != undefined) {
            updateHouse(submitData).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addHouse(submitData).then(() => {
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
      const houseIds = row.houseId || this.ids
      this.$modal.confirm('是否确认删除房屋编号为"' + houseIds + '"的数据项？').then(function() {
        return delHouse(houseIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 上架/下架 */
    handleStatus(row, status) {
      const tipText = status === '1' ? '上架' : '下架'
      this.$modal.confirm('是否确认' + tipText + '房屋编号为"' + row.houseId + '"的房屋？').then(function() {
        return changeHouseStatus({ houseId: row.houseId, status: status })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(tipText + "成功")
      }).catch(() => {})
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

.timeline-horizontal {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  padding: 20px 40px;
  position: relative;
}

.timeline-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  position: relative;
}

.timeline-node {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  z-index: 1;
  background-color: #fff;
  border: 2px solid #dcdfe6;
  color: #dcdfe6;
}

.timeline-node.finished {
  background-color: #409eff;
  border-color: #409eff;
  color: #fff;
}

.timeline-node.finished.success {
  background-color: #67c23a;
  border-color: #67c23a;
  color: #fff;
}

.timeline-node.finished.danger {
  background-color: #f56c6c;
  border-color: #f56c6c;
  color: #fff;
}

.timeline-node.process {
  background-color: #e6a23c;
  border-color: #e6a23c;
  color: #fff;
  animation: pulse 2s infinite;
}

.timeline-node.wait {
  background-color: #fff;
  border-color: #dcdfe6;
  color: #dcdfe6;
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(230, 162, 60, 0.4);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(230, 162, 60, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(230, 162, 60, 0);
  }
}

.timeline-line {
  position: absolute;
  top: 20px;
  left: 50%;
  width: 100%;
  height: 2px;
  background-color: #dcdfe6;
  z-index: 0;
}

.timeline-line.finished {
  background-color: #409eff;
}

.timeline-line.finished.success {
  background-color: #67c23a;
}

.timeline-line.finished.danger {
  background-color: #f56c6c;
}

.timeline-content {
  margin-top: 12px;
  text-align: center;
}

.timeline-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.timeline-time {
  font-size: 12px;
  color: #909399;
  margin-bottom: 2px;
}

.timeline-operator {
  font-size: 12px;
  color: #606266;
}

.timeline-item:last-child .timeline-line {
  display: none;
}
</style>
