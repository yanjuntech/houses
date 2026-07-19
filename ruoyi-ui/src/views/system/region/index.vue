<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="区划名称" prop="regionName">
        <el-input
          v-model="queryParams.regionName"
          placeholder="请输入区划名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="区划状态" clearable>
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
          v-hasPermi="['system:region:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="regionList"
      row-key="regionId"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      :lazy="!isSearchMode"
      :load="loadRegionChildren"
    >
      <el-table-column prop="regionName" label="区划名称" width="260"></el-table-column>
      <el-table-column prop="regionCode" label="区划编码" width="120"></el-table-column>
      <el-table-column prop="regionLevel" label="层级" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_region_level" :value="scope.row.regionLevel"/>
        </template>
      </el-table-column>
      <el-table-column prop="communityRegisterSwitch" label="小区登记开关" width="120">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.communityRegisterSwitch"
            active-value="0"
            inactive-value="1"
            @change="handleRegisterSwitchChange(scope.row)"
            v-hasPermi="['system:region:edit']"
          />
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="显示顺序" width="100"></el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
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
            v-hasPermi="['system:region:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['system:region:add']"
            :disabled="scope.row.regionLevel >= 3"
          >新增</el-button>
          <el-button
            v-if="scope.row.parentId != 0"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:region:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改区划对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24" v-if="form.parentId !== 0">
            <el-form-item label="上级区划" prop="parentId">
              <treeselect v-model="form.parentId" :options="regionOptions" :normalizer="normalizer" placeholder="选择上级区划" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="区划编码" prop="regionCode">
              <el-input v-model="form.regionCode" placeholder="请输入区划编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="区划名称" prop="regionName">
              <el-input v-model="form.regionName" placeholder="请输入区划名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="层级">
              <el-input v-model="form.regionLevel" disabled>
                <template slot="prepend">
                  {{ levelText }}
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="sort">
              <el-input-number v-model="form.sort" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="小区登记开关">
              <el-switch v-model="form.communityRegisterSwitch" active-value="0" inactive-value="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
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
import { listRegion, getRegion, delRegion, addRegion, updateRegion, changeRegisterSwitch, listRegionByParentId } from "@/api/system/region"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"

export default {
  name: "Region",
  dicts: ['sys_normal_disable', 'sys_region_level'],
  components: { Treeselect },
  data() {
    return {
      loading: true,
      showSearch: true,
      regionList: [],
      regionOptions: [],
      title: "",
      open: false,
      isExpandAll: false,
      refreshTable: true,
      isSearchMode: false,
      queryParams: {
        regionName: undefined,
        status: undefined
      },
      form: {},
      rules: {
        parentId: [
          { required: true, message: "上级区划不能为空", trigger: "blur" }
        ],
        regionCode: [
          { required: true, message: "区划编码不能为空", trigger: "blur" }
        ],
        regionName: [
          { required: true, message: "区划名称不能为空", trigger: "blur" }
        ],
        sort: [
          { required: true, message: "显示顺序不能为空", trigger: "blur" }
        ]
      }
    }
  },
  computed: {
    levelText() {
      if (this.form.regionLevel === 1) return '省'
      if (this.form.regionLevel === 2) return '市'
      if (this.form.regionLevel === 3) return '区县'
      return ''
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      this.isSearchMode = !!(this.queryParams.regionName || this.queryParams.status)
      if (this.isSearchMode) {
        listRegion(this.queryParams).then(response => {
          this.regionList = this.handleTree(response.data, "regionId")
          this.setHasChildren(this.regionList)
          this.loading = false
        })
      } else {
        listRegionByParentId(0).then(response => {
          this.regionList = response.data.map(item => ({
            ...item,
            hasChildren: item.regionLevel < 3
          }))
          this.loading = false
        })
      }
    },
    setHasChildren(list) {
      list.forEach(item => {
        if (item.children && item.children.length > 0) {
          item.hasChildren = true
          this.setHasChildren(item.children)
        } else {
          item.hasChildren = item.regionLevel < 3
        }
      })
    },
    loadRegionChildren(row, treeNode, resolve) {
      listRegionByParentId(row.regionId).then(response => {
        const children = response.data.map(item => ({
          ...item,
          hasChildren: item.regionLevel < 3
        }))
        resolve(children)
      })
    },
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.regionId,
        label: node.regionName,
        children: node.children
      }
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        regionId: undefined,
        parentId: undefined,
        regionCode: undefined,
        regionName: undefined,
        regionLevel: undefined,
        communityRegisterSwitch: "0",
        sort: undefined,
        status: "0",
        remark: undefined
      }
      this.resetForm("form")
    },
    handleQuery() {
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleAdd(row) {
      this.reset()
      if (row != undefined) {
        this.form.parentId = row.regionId
        this.form.regionLevel = row.regionLevel + 1
      } else {
        this.form.parentId = 0
        this.form.regionLevel = 1
      }
      this.open = true
      this.title = "添加区划"
      listRegion().then(response => {
        this.regionOptions = this.handleTree(response.data, "regionId")
      })
    },
    toggleExpandAll() {
      if (this.isSearchMode) {
        this.refreshTable = false
        this.isExpandAll = !this.isExpandAll
        this.$nextTick(() => {
          this.refreshTable = true
        })
      } else {
        if (this.isExpandAll) {
          this.isExpandAll = false
          this.getList()
        } else {
          this.loading = true
          listRegion().then(response => {
            this.regionList = this.handleTree(response.data, "regionId")
            this.setHasChildren(this.regionList)
            this.isExpandAll = true
            this.isSearchMode = true
            this.refreshTable = false
            this.$nextTick(() => {
              this.refreshTable = true
              this.loading = false
            })
          })
        }
      }
    },
    handleUpdate(row) {
      this.reset()
      getRegion(row.regionId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改区划"
        listRegion().then(response => {
          let regionData = response.data
          if (this.form.regionLevel < 3) {
            regionData = this.filterRegionByLevel(regionData, this.form.regionLevel)
          }
          this.regionOptions = this.handleTree(regionData, "regionId")
          if (this.form.parentId === 0) {
            this.regionOptions = []
          }
        })
      })
    },
    filterRegionByLevel(list, currentLevel) {
      const result = []
      list.forEach(item => {
        if (item.regionLevel <= currentLevel) {
          const newItem = { ...item }
          if (item.children && item.children.length) {
            newItem.children = this.filterRegionByLevel(item.children, currentLevel)
          }
          result.push(newItem)
        }
      })
      return result
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.regionId != undefined) {
            updateRegion(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRegion(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleRegisterSwitchChange(row) {
      changeRegisterSwitch(row).then(response => {
        this.$modal.msgSuccess("修改成功")
      }).catch(() => {
        this.getList()
      })
    },
    handleDelete(row) {
      this.$modal.confirm('是否确认删除名称为"' + row.regionName + '"的数据项？').then(function() {
        return delRegion(row.regionId)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    }
  }
}
</script>
