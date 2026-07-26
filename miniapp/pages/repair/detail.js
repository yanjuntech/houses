// 维修详情页
const app = getApp()
const { repairApi } = require('../../utils/api.js')
const utils = require('../../utils/index.js')

// 状态映射：text 为状态文字，class 为对应样式类
const STATUS_MAP = {
  0: { text: '待处理', class: 'status-pending' },
  1: { text: '房东已确认', class: 'status-processing' },
  2: { text: '维修中', class: 'status-processing' },
  3: { text: '维修完成', class: 'status-processing' },
  4: { text: '已完成', class: 'status-done' },
  5: { text: '已取消', class: 'status-cancelled' }
}

// 时间线步骤定义
const TIMELINE_STEPS = [
  { key: 'submit', title: '提交申请', desc: '租客提交维修申请' },
  { key: 'confirm', title: '房东确认', desc: '房东已确认维修单' },
  { key: 'repairing', title: '维修中', desc: '维修人员处理中' },
  { key: 'repaired', title: '维修完成', desc: '维修人员完成维修' },
  { key: 'completed', title: '租客确认', desc: '租客确认维修完成' }
]

// 状态到已完成步骤索引的映射（表示该索引及之前的步骤均已完成）
const STATUS_TO_STEP = {
  0: -1,  // 待处理：仅提交申请，未到房东确认
  1: 0,   // 房东已确认：第 0 步（提交）完成
  2: 1,   // 维修中：第 1 步（房东确认）完成
  3: 2,   // 维修完成：第 2 步（维修中）完成
  4: 3,   // 已完成：第 3 步（维修完成）完成，第 4 步（租客确认）也完成
  5: -1   // 已取消
}

Page({
  data: {
    repairId: '',
    repairInfo: null,
    statusText: '',
    statusClass: '',
    statusValue: -1,
    timelineSteps: [],
    currentStep: -1,
    isCancelled: false,
    canCancel: false,
    canConfirm: false,
    loading: true
  },

  onLoad(options) {
    const repairId = options && options.repairId ? options.repairId : ''
    if (!repairId) {
      utils.showToast('维修单ID缺失')
      this.setData({ loading: false })
      return
    }
    this.setData({ repairId })
    this.loadDetail()
  },

  // 加载维修详情（通过 list 接口筛选出对应 repairId 的记录）
  loadDetail() {
    const userInfo = app.getUserInfo() || {}
    const userId = userInfo.userId || userInfo.id
    if (!userId) {
      utils.showToast('请先登录')
      this.setData({ loading: false })
      return
    }

    this.setData({ loading: true })
    repairApi.list({ userId, pageNum: 1, pageSize: 100 }).then(res => {
      let list = []
      if (Array.isArray(res)) {
        list = res
      } else if (res) {
        list = res.rows || res.list || res.records || []
      }
      const info = list.find(item => String(item.repairId) === String(this.data.repairId)) || null
      if (!info) {
        utils.showToast('维修单不存在')
        this.setData({ loading: false })
        return
      }
      this.fillDetail(info)
    }).catch(() => {
      this.setData({ loading: false })
      utils.showToast('维修详情加载失败')
    })
  },

  // 填充详情数据
  fillDetail(info) {
    const status = Number(info.status === undefined ? info.repairStatus : info.status)
    const statusInfo = STATUS_MAP[status] || { text: '未知', class: '' }
    const isCancelled = status === 5
    const currentStep = STATUS_TO_STEP[status] === undefined ? -1 : STATUS_TO_STEP[status]

    // 构建时间线
    const timelineSteps = TIMELINE_STEPS.map((step, index) => {
      let state = 'todo'
      if (isCancelled) {
        // 已取消：仅"提交申请"标记为已完成（如果已提交），其余标记为 cancelled
        state = index === 0 ? 'done' : 'cancelled'
      } else if (status === 4) {
        // 已完成：所有步骤均已完成
        state = 'done'
      } else {
        if (index <= currentStep) {
          state = 'done'
        } else if (index === currentStep + 1) {
          state = 'current'
        } else {
          state = 'todo'
        }
      }
      return { ...step, state, index }
    })

    const formatTime = utils.formatDateTime(info.createTime || info.createdAt)
    const appointmentDate = info.appointmentDate ? utils.formatDate(info.appointmentDate) : ''

    this.setData({
      repairInfo: { ...info, formatTime, appointmentDate, statusValue: status },
      statusText: statusInfo.text,
      statusClass: statusInfo.class,
      statusValue: status,
      timelineSteps,
      currentStep,
      isCancelled,
      canCancel: status === 0,
      canConfirm: status === 3,
      loading: false
    })
  },

  // 取消维修
  handleCancel() {
    const { repairId } = this.data
    utils.showModal('提示', '确定要取消该维修申请吗？', {
      confirmText: '确定取消',
      confirmColor: '#f56c6c'
    }).then(confirm => {
      if (!confirm) return
      utils.showLoading('处理中')
      repairApi.cancel(repairId).then(() => {
        utils.hideLoading()
        utils.showToast('已取消维修', 'success')
        setTimeout(() => {
          this.loadDetail()
        }, 1000)
      }).catch(() => {
        utils.hideLoading()
      })
    })
  },

  // 租客确认维修完成
  handleConfirm() {
    const { repairId } = this.data
    utils.showModal('提示', '确认该维修已完成吗？', {
      confirmText: '确认完成'
    }).then(confirm => {
      if (!confirm) return
      utils.showLoading('处理中')
      repairApi.tenantConfirm(repairId).then(() => {
        utils.hideLoading()
        utils.showToast('确认成功', 'success')
        setTimeout(() => {
          this.loadDetail()
        }, 1000)
      }).catch(() => {
        utils.hideLoading()
      })
    })
  }
})
