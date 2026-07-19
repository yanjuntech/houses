import request from '@/utils/request'

// 查询消息列表
export function listMessage(query) {
  return request({
    url: '/rental/message/list',
    method: 'get',
    params: query
  })
}

// 查询消息详细
export function getMessage(messageId) {
  return request({
    url: '/rental/message/' + messageId,
    method: 'get'
  })
}

// 删除消息
export function delMessage(messageId) {
  return request({
    url: '/rental/message/' + messageId,
    method: 'delete'
  })
}

// 发送消息
export function sendMessage(data) {
  return request({
    url: '/rental/message/send',
    method: 'post',
    data: data
  })
}

// 查询聊天记录
export function getHistory(query) {
  return request({
    url: '/rental/message/history',
    method: 'get',
    params: query
  })
}

// 标记已读
export function markAsRead(messageId) {
  return request({
    url: '/rental/message/markAsRead/' + messageId,
    method: 'put'
  })
}
