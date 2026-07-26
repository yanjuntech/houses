// 维修状态映射：text 为状态文字，class 为对应样式类
const REPAIR_STATUS_MAP = {
  0: { text: '待处理', class: 'status-pending' },
  1: { text: '房东已确认', class: 'status-processing' },
  2: { text: '维修中', class: 'status-processing' },
  3: { text: '维修完成', class: 'status-processing' },
  4: { text: '已完成', class: 'status-done' },
  5: { text: '已取消', class: 'status-cancelled' }
}

module.exports = {
  REPAIR_STATUS_MAP
}
