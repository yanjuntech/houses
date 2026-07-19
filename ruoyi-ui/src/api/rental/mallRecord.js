import request from '@/utils/request'

// 查询兑换记录列表
export function listMallRecord(query) {
  return request({
    url: '/rental/mallRecord/list',
    method: 'get',
    params: query
  })
}

// 查询兑换记录详细
export function getMallRecord(recordId) {
  return request({
    url: '/rental/mallRecord/' + recordId,
    method: 'get'
  })
}

// 删除兑换记录
export function delMallRecord(recordId) {
  return request({
    url: '/rental/mallRecord/' + recordId,
    method: 'delete'
  })
}

// 兑换商品
export function exchange(data) {
  return request({
    url: '/rental/mallRecord/exchange',
    method: 'post',
    data: data
  })
}

// 查询用户兑换记录
export function userRecord(userId) {
  return request({
    url: '/rental/mallRecord/userRecord/' + userId,
    method: 'get'
  })
}
