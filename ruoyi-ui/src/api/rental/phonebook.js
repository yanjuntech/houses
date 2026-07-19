import request from '@/utils/request'

// 查询电话簿列表
export function listPhonebook(query) {
  return request({
    url: '/rental/phonebook/list',
    method: 'get',
    params: query
  })
}

// 查询电话簿详细
export function getPhonebook(phonebookId) {
  return request({
    url: '/rental/phonebook/' + phonebookId,
    method: 'get'
  })
}

// 新增电话簿
export function addPhonebook(data) {
  return request({
    url: '/rental/phonebook',
    method: 'post',
    data: data
  })
}

// 修改电话簿
export function updatePhonebook(data) {
  return request({
    url: '/rental/phonebook',
    method: 'put',
    data: data
  })
}

// 删除电话簿
export function delPhonebook(phonebookId) {
  return request({
    url: '/rental/phonebook/' + phonebookId,
    method: 'delete'
  })
}

// 修改电话簿状态（上下架）
export function changePhonebookStatus(data) {
  return request({
    url: '/rental/phonebook',
    method: 'put',
    data: data
  })
}

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

// 申请电话簿
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
