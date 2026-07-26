Component({
  properties: {
    status: {
      type: String,
      value: ''
    },
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

  data: {
    // 计算后的状态，兼容 status 属性与原有 boolean 属性
    isLoading: false,
    isEmpty: false,
    isError: false
  },

  observers: {
    'status, loading, empty, error': function (status, loading, empty, error) {
      if (status === 'loading') {
        this.setData({ isLoading: true, isEmpty: false, isError: false })
      } else if (status === 'empty') {
        this.setData({ isLoading: false, isEmpty: true, isError: false })
      } else if (status === 'error') {
        this.setData({ isLoading: false, isEmpty: false, isError: true })
      } else if (status === 'normal' || status) {
        this.setData({ isLoading: false, isEmpty: false, isError: false })
      } else {
        // status 为空时回退到原有 boolean 属性
        this.setData({ isLoading: loading, isEmpty: empty, isError: error })
      }
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