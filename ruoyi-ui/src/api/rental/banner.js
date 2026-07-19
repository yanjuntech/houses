import request from '@/utils/request'

export function listBanner(query) {
  return request({
    url: '/rental/banner/list',
    method: 'get',
    params: query
  })
}

export function getBanner(bannerId) {
  return request({
    url: '/rental/banner/' + bannerId,
    method: 'get'
  })
}

export function addBanner(data) {
  return request({
    url: '/rental/banner',
    method: 'post',
    data: data
  })
}

export function updateBanner(data) {
  return request({
    url: '/rental/banner',
    method: 'put',
    data: data
  })
}

export function delBanner(bannerId) {
  return request({
    url: '/rental/banner/' + bannerId,
    method: 'delete'
  })
}
