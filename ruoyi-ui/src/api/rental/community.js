import request from '@/utils/request'

// 查询小区列表
export function listCommunity(query) {
  return request({
    url: '/rental/community/list',
    method: 'get',
    params: query
  })
}

// 查询小区详细
export function getCommunity(communityId) {
  return request({
    url: '/rental/community/' + communityId,
    method: 'get'
  })
}

// 新增小区
export function addCommunity(data) {
  return request({
    url: '/rental/community',
    method: 'post',
    data: data
  })
}

// 修改小区
export function updateCommunity(data) {
  return request({
    url: '/rental/community',
    method: 'put',
    data: data
  })
}

// 删除小区
export function delCommunity(communityId) {
  return request({
    url: '/rental/community/' + communityId,
    method: 'delete'
  })
}

// 查询全部小区（下拉选择用）
export function selectAllCommunity() {
  return request({
    url: '/rental/community/selectAll',
    method: 'get'
  })
}

// 查询小区申请列表
export function listCommunityApply(query) {
  return request({
    url: '/rental/communityApply/list',
    method: 'get',
    params: query
  })
}

// 查询小区申请详细
export function getCommunityApply(applyId) {
  return request({
    url: '/rental/communityApply/' + applyId,
    method: 'get'
  })
}

// 申请小区
export function applyCommunity(data) {
  return request({
    url: '/rental/communityApply/apply',
    method: 'post',
    data: data
  })
}

// 审批通过小区申请
export function approveCommunityApply(data) {
  return request({
    url: '/rental/communityApply/approve',
    method: 'put',
    data: data
  })
}

// 驳回小区申请
export function rejectCommunityApply(data) {
  return request({
    url: '/rental/communityApply/reject',
    method: 'put',
    data: data
  })
}
