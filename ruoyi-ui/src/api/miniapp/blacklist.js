import request from '@/utils/request'

// 查询黑名单列表
export function listBlacklist(query) {
  return request({
    url: '/miniapp/user/blacklist/list',
    method: 'get',
    params: query
  })
}

// 加入黑名单
export function addBlacklist(data) {
  return request({
    url: '/miniapp/user/blacklist/add',
    method: 'put',
    data: data
  })
}

// 解除黑名单
export function removeFromBlacklist(userId) {
  return request({
    url: '/miniapp/user/blacklist/remove/' + userId,
    method: 'put'
  })
}
