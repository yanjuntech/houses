<template>
  <div>
    <template v-for="(item, index) in options">
      <template v-if="isValueMatch(item.value)">
        <span
          v-if="(item.raw.listClass == 'default' || item.raw.listClass == '') && (item.raw.cssClass == '' || item.raw.cssClass == null)"
          :key="item.value"
          :index="index"
          :class="item.raw.cssClass"
          >{{ item.label + ' ' }}</span
        >
        <el-tag
          v-else
          :disable-transitions="true"
          :key="item.value"
          :index="index"
          :type="item.raw.listClass == 'primary' ? '' : item.raw.listClass"
          :class="item.raw.cssClass"
        >
          {{ item.label + ' ' }}
        </el-tag>
      </template>
    </template>
    <template v-if="unmatch && showValue">
      {{ unmatchArray | handleArray }}
    </template>
  </div>
</template>

<script>
export default {
  name: "DictTag",
  props: {
    options: {
      type: Array,
      default: null,
    },
    value: [Number, String, Array],
    showValue: {
      type: Boolean,
      default: true,
    },
    separator: {
      type: String,
      default: ","
    }
  },
  data() {
    return {
      unmatchArray: [], // 记录未匹配的项
    }
  },
  computed: {
    values() {
      if (this.value === null || typeof this.value === 'undefined' || this.value === '') return []
      if (typeof this.value === 'number' || typeof this.value === 'boolean') return [this.value]
      return Array.isArray(this.value) ? this.value.map(item => '' + item) : String(this.value).split(this.separator)
    },
    unmatch() {
      this.unmatchArray = []
      // 没有value不显示
      if (this.value === null || typeof this.value === 'undefined' || this.value === '' || this.options.length === 0) return false
      // 传入值为数组
      let unmatch = false // 添加一个标志来判断是否有未匹配项
      this.values.forEach(item => {
        if (!this.options.some(v => v.value == item)) {
          this.unmatchArray.push(item)
          unmatch = true // 如果有未匹配项，将标志设置为true
        }
      })
      return unmatch // 返回标志的值
    },
  },
  methods: {
    isValueMatch(itemValue) {
      return this.values.some(val => val == itemValue)
    }
  },
  filters: {
    handleArray(array) {
      if (array.length === 0) return ''
      return array.reduce((pre, cur) => {
        return pre + ' ' + cur
      })
    },
  }
}
</script>
<style scoped>
div {
  display: inline-flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

::v-deep .el-tag {
  border-radius: 12px;
  padding: 0 14px;
  height: 28px;
  line-height: 26px;
  font-size: 13px;
  font-weight: 500;
  border: none;
  background-color: #ecf5ff;
  color: #409eff;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  margin-left: 0 !important;
}

::v-deep .el-tag:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  transform: translateY(-1px) scale(1.03);
  opacity: 0.92;
}

::v-deep .el-tag--primary {
  background-color: #ecf5ff;
  color: #409eff;
}

::v-deep .el-tag--success {
  background-color: #f0f9eb;
  color: #67c23a;
}

::v-deep .el-tag--info {
  background-color: #f4f4f5;
  color: #909399;
}

::v-deep .el-tag--warning {
  background-color: #fdf6ec;
  color: #e6a23c;
}

::v-deep .el-tag--danger {
  background-color: #fef0f0;
  color: #f56c6c;
}

span {
  display: inline-block;
  padding: 4px 14px;
  border-radius: 12px;
  font-size: 13px;
  font-weight: 500;
  background-color: #f5f7fa;
  color: #606266;
  line-height: 20px;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

span:hover {
  background-color: #e9edf2;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transform: translateY(-1px) scale(1.03);
}
</style>
