import request from '@/utils/request'

// 查询电话簿申请列表
export function listPhonebookApply(query) {
  return request({
    url: '/rental/phonebookApply/list',
    method: 'get',
    params: query
  })
}

// 查询电话簿申请详细
export function getPhonebookApply(applyId) {
  return request({
    url: '/rental/phonebookApply/' + applyId,
    method: 'get'
  })
}

// 申请电话簿收录
export function applyPhonebook(data) {
  return request({
    url: '/rental/phonebookApply/apply',
    method: 'post',
    data: data
  })
}

// 审批通过电话簿申请
export function approvePhonebookApply(data) {
  return request({
    url: '/rental/phonebookApply/approve',
    method: 'put',
    data: data
  })
}

// 驳回电话簿申请
export function rejectPhonebookApply(data) {
  return request({
    url: '/rental/phonebookApply/reject',
    method: 'put',
    data: data
  })
}
