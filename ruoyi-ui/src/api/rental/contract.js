import request from '@/utils/request'

// 查询合同列表
export function listContract(query) {
  return request({
    url: '/rental/contract/list',
    method: 'get',
    params: query
  })
}

// 查询合同详细
export function getContract(contractId) {
  return request({
    url: '/rental/contract/' + contractId,
    method: 'get'
  })
}

// 新增合同
export function addContract(data) {
  return request({
    url: '/rental/contract',
    method: 'post',
    data: data
  })
}

// 修改合同
export function updateContract(data) {
  return request({
    url: '/rental/contract',
    method: 'put',
    data: data
  })
}

// 删除合同
export function delContract(contractId) {
  return request({
    url: '/rental/contract/' + contractId,
    method: 'delete'
  })
}

// 上传签名
export function uploadSignature(data) {
  return request({
    url: '/rental/contract/uploadSignature',
    method: 'post',
    data: data
  })
}

// 完成合同
export function completeContract(data) {
  return request({
    url: '/rental/contract/complete',
    method: 'put',
    data: data
  })
}

// 下载合同
export function downloadContract(contractId) {
  return request({
    url: '/rental/contract/download/' + contractId,
    method: 'get',
    responseType: 'blob'
  })
}
