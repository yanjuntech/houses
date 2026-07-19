import request from '@/utils/request'

// 查询邀请关系列表
export function listInvite(query) {
  return request({
    url: '/rental/invite/list',
    method: 'get',
    params: query
  })
}

// 查询邀请关系详细
export function getInvite(relationId) {
  return request({
    url: '/rental/invite/' + relationId,
    method: 'get'
  })
}

// 新增邀请关系
export function addInvite(data) {
  return request({
    url: '/rental/invite',
    method: 'post',
    data: data
  })
}

// 修改邀请关系
export function updateInvite(data) {
  return request({
    url: '/rental/invite',
    method: 'put',
    data: data
  })
}

// 删除邀请关系
export function delInvite(relationId) {
  return request({
    url: '/rental/invite/' + relationId,
    method: 'delete'
  })
}

// 查询邀请人统计数据
export function inviteStatistics(inviterId) {
  return request({
    url: '/rental/invite/statistics/' + inviterId,
    method: 'get'
  })
}

// 查询邀请人发出的邀请列表
export function inviteListByInviter(inviterId) {
  return request({
    url: '/rental/invite/inviteList/' + inviterId,
    method: 'get'
  })
}

// 绑定邀请关系
export function bindInvite(data) {
  return request({
    url: '/rental/invite/bindInvite',
    method: 'post',
    data: data
  })
}

// 更新认证状态
export function updateCertified(data) {
  return request({
    url: '/rental/invite/updateCertified',
    method: 'put',
    data: data
  })
}
