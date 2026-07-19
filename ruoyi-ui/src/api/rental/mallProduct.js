import request from '@/utils/request'

// 查询兑换商品列表
export function listMallProduct(query) {
  return request({
    url: '/rental/mallProduct/list',
    method: 'get',
    params: query
  })
}

// 查询兑换商品详细
export function getMallProduct(productId) {
  return request({
    url: '/rental/mallProduct/' + productId,
    method: 'get'
  })
}

// 新增兑换商品
export function addMallProduct(data) {
  return request({
    url: '/rental/mallProduct',
    method: 'post',
    data: data
  })
}

// 修改兑换商品
export function updateMallProduct(data) {
  return request({
    url: '/rental/mallProduct',
    method: 'put',
    data: data
  })
}

// 删除兑换商品
export function delMallProduct(productId) {
  return request({
    url: '/rental/mallProduct/' + productId,
    method: 'delete'
  })
}

// 修改商品上下架状态
export function changeProductStatus(data) {
  return request({
    url: '/rental/mallProduct/changeStatus',
    method: 'put',
    data: data
  })
}

// 查询全部商品（下拉选择用）
export function selectAllProduct() {
  return request({
    url: '/rental/mallProduct/selectAll',
    method: 'get'
  })
}
