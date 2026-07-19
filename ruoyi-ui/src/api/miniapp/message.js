import request from '@/utils/request'

// 查询消息列表
export function listMessage(query) {
  return request({
    url: '/miniapp/message/list',
    method: 'get',
    params: query
  })
}

// 查询消息详细
export function getMessage(messageId) {
  return request({
    url: '/miniapp/message/' + messageId,
    method: 'get'
  })
}

// 推送消息
export function pushMessage(data) {
  return request({
    url: '/miniapp/message',
    method: 'post',
    data: data
  })
}

// 删除消息
export function delMessage(messageId) {
  return request({
    url: '/miniapp/message/' + messageId,
    method: 'delete'
  })
}
