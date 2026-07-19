import request from '@/utils/request'

// 查询维修列表
export function listRepair(query) {
  return request({
    url: '/rental/repair/list',
    method: 'get',
    params: query
  })
}

// 查询维修详细
export function getRepair(repairId) {
  return request({
    url: '/rental/repair/' + repairId,
    method: 'get'
  })
}

// 删除维修
export function delRepair(repairId) {
  return request({
    url: '/rental/repair/' + repairId,
    method: 'delete'
  })
}

// 房东确认维修
export function confirmByLandlord(repairId) {
  return request({
    url: '/rental/repair/confirmByLandlord/' + repairId,
    method: 'put'
  })
}

// 房东选择租客自修
export function chooseTenantSelfRepair(repairId) {
  return request({
    url: '/rental/repair/chooseTenantSelfRepair/' + repairId,
    method: 'put'
  })
}

// 取消维修
export function cancelRepair(repairId) {
  return request({
    url: '/rental/repair/cancel/' + repairId,
    method: 'put'
  })
}

// 完成维修
export function completeRepair(repairId) {
  return request({
    url: '/rental/repair/complete/' + repairId,
    method: 'put'
  })
}

// 租客确认完成
export function confirmByTenant(repairId) {
  return request({
    url: '/rental/repair/confirmByTenant/' + repairId,
    method: 'put'
  })
}

// 上传凭证
export function uploadVoucher(data) {
  return request({
    url: '/rental/repair/uploadVoucher',
    method: 'put',
    data: data
  })
}

// 确认报销
export function confirmReimburse(repairId) {
  return request({
    url: '/rental/repair/confirmReimburse/' + repairId,
    method: 'put'
  })
}
