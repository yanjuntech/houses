Component({
  properties: {
    loading: {
      type: Boolean,
      value: false
    },
    loadingText: {
      type: String,
      value: '加载中...'
    },
    empty: {
      type: Boolean,
      value: false
    },
    emptyIcon: {
      type: String,
      value: '📭'
    },
    emptyText: {
      type: String,
      value: '暂无数据'
    },
    emptyBtnText: {
      type: String,
      value: ''
    },
    error: {
      type: Boolean,
      value: false
    },
    errorIcon: {
      type: String,
      value: '⚠️'
    },
    errorText: {
      type: String,
      value: '加载失败，请重试'
    }
  },

  methods: {
    handleEmptyBtn() {
      this.triggerEvent('emptyBtnTap')
    },

    handleRetry() {
      this.triggerEvent('retry')
    }
  }
})