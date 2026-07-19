<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="小区名称" prop="communityName">
        <el-input
          v-model="queryParams.communityName"
          placeholder="请输入小区名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所在地区" prop="district">
        <el-cascader
          v-model="queryRegion"
          :options="regionOptions"
          :props="regionProps"
          :show-all-levels="true"
          change-on-select
          clearable
          placeholder="请选择所在地区"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="小区状态" clearable>
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
          v-hasPermi="['rental:community:add']"
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
          v-hasPermi="['rental:community:edit']"
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
          v-hasPermi="['rental:community:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['rental:community:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="communityList" @selection-change="handleSelectionChange">
      <el-table-column type="expand">
        <template slot-scope="scope">
          <el-descriptions :column="3" border size="medium" style="padding: 10px;">
            <el-descriptions-item label="省份">{{ scope.row.province || '-' }}</el-descriptions-item>
            <el-descriptions-item label="城市">{{ scope.row.city || '-' }}</el-descriptions-item>
            <el-descriptions-item label="区县">{{ scope.row.district || '-' }}</el-descriptions-item>
            <el-descriptions-item label="详细地址" :span="3">{{ scope.row.address || '-' }}</el-descriptions-item>
            <el-descriptions-item label="经度">{{ scope.row.longitude || '-' }}</el-descriptions-item>
            <el-descriptions-item label="纬度">{{ scope.row.latitude || '-' }}</el-descriptions-item>
            <el-descriptions-item label="物业公司">{{ scope.row.propertyCompany || '-' }}</el-descriptions-item>
            <el-descriptions-item label="物业公司电话">{{ scope.row.propertyPhone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
            </el-descriptions-item>
            <el-descriptions-item label="小区标签" :span="3">
              <template v-if="scope.row.tags">
                <dict-tag
                  v-for="(tag, index) in scope.row.tags.split(',')"
                  :key="index"
                  :options="dict.type.biz_community_tag"
                  :value="tag"
                  style="margin-right: 5px;"
                />
              </template>
              <span v-else>-</span>
            </el-descriptions-item>
            <el-descriptions-item label="备注" :span="3">{{ scope.row.remark || '-' }}</el-descriptions-item>
            <el-descriptions-item label="创建人">{{ scope.row.createBy || '-' }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ parseTime(scope.row.createTime) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="更新人">{{ scope.row.updateBy || '-' }}</el-descriptions-item>
            <el-descriptions-item label="更新时间">{{ parseTime(scope.row.updateTime) || '-' }}</el-descriptions-item>
          </el-descriptions>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="小区编号" align="center" prop="communityId" />
      <el-table-column label="小区名称" align="center" prop="communityName" />
      <el-table-column label="所在省" align="center" prop="province" />
      <el-table-column label="所在市" align="center" prop="city" />
      <el-table-column label="所在区" align="center" prop="district" />
      <el-table-column label="详细地址" align="center" prop="address" show-overflow-tooltip />
      <el-table-column label="物业公司" align="center" prop="propertyCompany" />
      <el-table-column label="联系电话" align="center" prop="propertyPhone" />
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
      <el-table-column label="小区标签" align="center" prop="tags">
        <template slot-scope="scope">
          <template v-if="scope.row.tags">
            <dict-tag
              v-for="(tag, index) in scope.row.tags.split(',')"
              :key="index"
              :options="dict.type.biz_community_tag"
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['rental:community:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['rental:community:remove']"
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

    <!-- 添加或修改小区对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="小区名称" prop="communityName">
          <el-input v-model="form.communityName" placeholder="请输入小区名称" />
        </el-form-item>
        <el-form-item label="所在地区" prop="region">
          <el-cascader
            v-model="formRegion"
            :options="regionOptions"
            :props="regionProps"
            :show-all-levels="true"
            change-on-select
            clearable
            placeholder="请选择所在地区"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="地图选点">
          <div style="width: 100%">
            <el-alert title="点击地图或搜索地址选择小区位置，将自动填充详细地址、经纬度" type="info" :closable="false" style="margin-bottom: 10px;" />
            <amap-picker
              v-if="mapProvider === 'amap'"
              :longitude="form.longitude"
              :latitude="form.latitude"
              :address="form.address"
              @select="handleMapSelect"
            />
            <tmap-picker
              v-else-if="mapProvider === 'tencent'"
              :longitude="form.longitude"
              :latitude="form.latitude"
              :address="form.address"
              @select="handleMapSelect"
            />
          </div>
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" type="textarea" placeholder="请输入详细地址（可手动覆盖地图反查结果）" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input v-model="form.longitude" placeholder="经度（由地图选点自动填充）" disabled />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input v-model="form.latitude" placeholder="纬度（由地图选点自动填充）" disabled />
        </el-form-item>
        <el-form-item label="物业公司" prop="propertyCompany">
          <el-input v-model="form.propertyCompany" placeholder="请输入物业公司" />
        </el-form-item>
        <el-form-item label="联系电话" prop="propertyPhone">
          <el-input v-model="form.propertyPhone" placeholder="请输入物业公司联系电话（手机号或座机，如010-12345678）" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="小区标签" prop="tags">
          <el-select v-model="form.tagsList" multiple placeholder="请选择小区标签" style="width: 100%">
            <el-option
              v-for="dict in dict.type.biz_community_tag"
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
  </div>
</template>

<script>
import { listCommunity, getCommunity, delCommunity, addCommunity, updateCommunity } from "@/api/rental/community"
import { listRegion } from "@/api/system/region"
import { getConfigKey } from "@/api/system/config"
import AMapPicker from "@/components/AMapPicker"
import TMapPicker from "@/components/TMapPicker"

export default {
  name: "RentalCommunity",
  dicts: ['sys_normal_disable', 'biz_community_tag'],
  components: { AMapPicker, TMapPicker },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 地图服务商（amap 高德 / tencent 腾讯）
      mapProvider: 'amap',
      // 地图选点组件可见性
      mapPickerVisible: false,
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
      // 小区表格数据
      communityList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 行政区划树数据
      regionOptions: [],
      // 级联选择器配置
      regionProps: {
        value: 'regionName',
        label: 'regionName',
        children: 'children'
      },
      // 查询用地区
      queryRegion: [],
      // 表单用地区
      formRegion: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        communityName: undefined,
        province: undefined,
        city: undefined,
        district: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        communityName: [
          { required: true, message: "小区名称不能为空", trigger: "blur" }
        ],
        region: [
          { required: true, validator: (rule, value, callback) => { if (!this.formRegion || this.formRegion.length === 0) callback(new Error('所在地区不能为空')); else callback(); }, trigger: 'change' }
        ],
        propertyPhone: [
          { required: false, message: "联系电话不能为空", trigger: "blur" },
          { pattern: /(^1[3-9]\d{9}$)|(^0\d{2,3}-?\d{7,8}$)/, message: "联系电话格式不正确（11位手机号或区号-号码）", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getRegionTree()
    this.loadMapProvider()
  },
  methods: {
    /** 加载地图服务商配置 */
    loadMapProvider() {
      getConfigKey('sys.community.mapProvider').then(response => {
        // response.msg 是 config_value
        if (response.msg) {
          this.mapProvider = response.msg === 'tencent' ? 'tencent' : 'amap'
        }
      }).catch(() => {
        // 默认 amap
        this.mapProvider = 'amap'
      })
    },
    /** 地图选点回调，回填表单 */
    handleMapSelect(data) {
      this.form.address = data.address
      this.form.longitude = data.longitude
      this.form.latitude = data.latitude
    },
    /** 获取行政区划树 */
    getRegionTree() {
      listRegion().then(response => {
        this.regionOptions = response.data
      })
    },
    /** 查询小区列表 */
    getList() {
      this.loading = true
      if (this.queryRegion && this.queryRegion.length > 0) {
        this.queryParams.province = this.queryRegion[0] || undefined
        this.queryParams.city = this.queryRegion[1] || undefined
        this.queryParams.district = this.queryRegion[2] || undefined
      } else {
        this.queryParams.province = undefined
        this.queryParams.city = undefined
        this.queryParams.district = undefined
      }
      listCommunity(this.queryParams).then(response => {
        this.communityList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 小区状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用"
      this.$modal.confirm('确认要"' + text + '""' + row.communityName + '"小区吗？').then(function() {
        return updateCommunity({ communityId: row.communityId, status: row.status })
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
        communityId: undefined,
        communityName: undefined,
        province: undefined,
        city: undefined,
        district: undefined,
        address: undefined,
        longitude: undefined,
        latitude: undefined,
        propertyCompany: undefined,
        propertyPhone: undefined,
        status: "0",
        tags: undefined,
        tagsList: [],
        remark: undefined
      }
      this.formRegion = []
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryRegion = []
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.communityId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.formRegion = ['山东省', '聊城市', '阳谷县']
      this.open = true
      this.title = "添加小区"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const communityId = row.communityId || this.ids
      getCommunity(communityId).then(response => {
        this.form = response.data
        this.form.tagsList = response.data.tags ? response.data.tags.split(',') : []
        if (this.form.province || this.form.city || this.form.district) {
          this.formRegion = []
          if (this.form.province) this.formRegion.push(this.form.province)
          if (this.form.city) this.formRegion.push(this.form.city)
          if (this.form.district) this.formRegion.push(this.form.district)
        }
        this.open = true
        this.title = "修改小区"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.formRegion && this.formRegion.length > 0) {
            this.form.province = this.formRegion[0] || undefined
            this.form.city = this.formRegion[1] || undefined
            this.form.district = this.formRegion[2] || undefined
          }
          const submitData = { ...this.form }
          submitData.tags = this.form.tagsList && this.form.tagsList.length > 0 ? this.form.tagsList.join(',') : ''
          if (this.form.communityId != undefined) {
            updateCommunity(submitData).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addCommunity(submitData).then(() => {
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
      const communityIds = row.communityId || this.ids
      this.$modal.confirm('是否确认删除小区编号为"' + communityIds + '"的数据项？').then(function() {
        return delCommunity(communityIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('rental/community/export', {
        ...this.queryParams
      }, `rental_community_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
