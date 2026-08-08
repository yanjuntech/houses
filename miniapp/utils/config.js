// 全局配置文件
// 通过修改 ENV 切换环境：development | production
const ENV = 'development'

// 各环境配置
const envConfig = {
  development: {
    baseUrl: 'http://127.0.0.1:8080',
    servicePhone: '400-888-8888',
    defaultAvatar: '/images/default-avatar.png'
  },
  production: {
    baseUrl: 'https://api.example.com',
    servicePhone: '400-888-8888',
    defaultAvatar: '/images/default-avatar.png'
  }
}

// 当前环境配置
const config = envConfig[ENV]

module.exports = {
  // 当前环境标识
  env: ENV,
  // 后端服务基础地址
  baseUrl: config.baseUrl,
  // 客服电话
  servicePhone: config.servicePhone,
  // 默认头像（本地图片路径）
  defaultAvatar: config.defaultAvatar
}
