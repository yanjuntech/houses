import request from '@/utils/request'

// 查询在租房屋列表
export function listRentalContract(query) {
  return request({
    url: '/rental/rentalContract/list',
    method: 'get',
    params: query
  })
}

// 查询在租房屋详细
export function getRentalContract(rentalId) {
  return request({
    url: '/rental/rentalContract/' + rentalId,
    method: 'get'
  })
}

// 新增在租房屋
export function addRentalContract(data) {
  return request({
    url: '/rental/rentalContract',
    method: 'post',
    data: data
  })
}

// 修改在租房屋
export function updateRentalContract(data) {
  return request({
    url: '/rental/rentalContract',
    method: 'put',
    data: data
  })
}

// 删除在租房屋
export function delRentalContract(rentalId) {
  return request({
    url: '/rental/rentalContract/' + rentalId,
    method: 'delete'
  })
}

// 查询即将到期的租赁合同
export function expiringSoon() {
  return request({
    url: '/rental/rentalContract/expiringSoon',
    method: 'get'
  })
}

// 刷新到期状态
export function refreshExpire() {
  return request({
    url: '/rental/rentalContract/refreshExpire',
    method: 'get'
  })
}
