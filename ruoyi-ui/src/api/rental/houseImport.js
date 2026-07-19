import request from '@/utils/request'

// 下载导入模板
export function downloadTemplate() {
  return request({
    url: '/rental/house/downloadTemplate',
    method: 'post',
    responseType: 'blob'
  })
}

// 预览导入
export function previewImport(formData) {
  return request({
    url: '/rental/house/previewImport',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// 确认导入
export function confirmImport(data) {
  return request({
    url: '/rental/house/confirmImport',
    method: 'post',
    data: data
  })
}
