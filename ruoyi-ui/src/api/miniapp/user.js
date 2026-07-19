import request from '@/utils/request'

// 查询小程序用户列表
export function listUser(query) {
  return request({
    url: '/miniapp/user/list',
    method: 'get',
    params: query
  })
}

// 查询用户详细
export function getUser(userId) {
  return request({
    url: '/miniapp/user/' + userId,
    method: 'get'
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/miniapp/user',
    method: 'post',
    data: data
  })
}

// 修改用户
export function updateUser(data) {
  return request({
    url: '/miniapp/user',
    method: 'put',
    data: data
  })
}

// 删除用户
export function delUser(userId) {
  return request({
    url: '/miniapp/user/' + userId,
    method: 'delete'
  })
}

// 修改身份标签
export function changeUserType(data) {
  return request({
    url: '/miniapp/user/changeUserType',
    method: 'put',
    data: data
  })
}

// 实名认证审核
export function verifyUser(data) {
  return request({
    url: '/miniapp/user/verify',
    method: 'put',
    data: data
  })
}

// 调整发布次数
export function adjustPublishCount(data) {
  return request({
    url: '/miniapp/user/adjustPublishCount',
    method: 'put',
    data: data
  })
}

// 延长发布周期
export function extendPublishPeriod(data) {
  return request({
    url: '/miniapp/user/extendPublishPeriod',
    method: 'put',
    data: data
  })
}
