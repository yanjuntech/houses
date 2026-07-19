import request from '@/utils/request'

// 查询区划列表
export function listRegion(query) {
  return request({
    url: '/system/region/list',
    method: 'get',
    params: query
  })
}

// 查询区划详细
export function getRegion(regionId) {
  return request({
    url: '/system/region/' + regionId,
    method: 'get'
  })
}

// 新增区划
export function addRegion(data) {
  return request({
    url: '/system/region',
    method: 'post',
    data: data
  })
}

// 修改区划
export function updateRegion(data) {
  return request({
    url: '/system/region',
    method: 'put',
    data: data
  })
}

// 删除区划
export function delRegion(regionId) {
  return request({
    url: '/system/region/' + regionId,
    method: 'delete'
  })
}

// 修改小区登记开关
export function changeRegisterSwitch(data) {
  return request({
    url: '/system/region/changeRegisterSwitch',
    method: 'put',
    data: data
  })
}

// 根据父级ID查询区划列表
export function listRegionByParentId(parentId) {
  return request({
    url: '/system/region/listByParentId/' + parentId,
    method: 'get'
  })
}
