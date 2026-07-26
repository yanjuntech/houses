// 后端服务基础地址（开发环境）
const baseUrl = 'http://localhost:8080'

// 登录页路径，用于 401 自动跳转
const LOGIN_PAGE = '/pages/login/login'

// 处理登录失效：清除登录信息并跳转登录页
function handleUnauthorized() {
  try {
    wx.removeStorageSync('token')
    wx.removeStorageSync('userInfo')
  } catch (e) {
    console.error('清除登录信息失败:', e)
  }
  // 避免重复跳转
  const pages = getCurrentPages()
  const currentRoute = pages.length ? '/' + pages[pages.length - 1].route : ''
  if (currentRoute !== LOGIN_PAGE) {
    wx.reLaunch({ url: LOGIN_PAGE })
  }
}

// 统一错误提示
function showErrorToast(message) {
  if (!message) return
  wx.showToast({
    title: message,
    icon: 'none',
    duration: 2000
  })
}

function request(options) {
  return new Promise((resolve, reject) => {
    // 自动注入 token（从本地存储读取）
    const token = wx.getStorageSync('token') || ''
    const header = {
      'Content-Type': 'application/json',
      ...options.header
    }
    if (token) {
      header['Authorization'] = 'Bearer ' + token
    }

    wx.request({
      url: baseUrl + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header,
      success(res) {
        // 401 未授权：清除登录信息并跳转登录页
        if (res.statusCode === 401) {
          handleUnauthorized()
          showErrorToast('登录已失效，请重新登录')
          reject(new Error('登录已失效，请重新登录'))
          return
        }

        if (res.statusCode === 200) {
          // 兼容后端统一返回结构 {code, message, data}
          const resData = res.data
          if (resData && typeof resData === 'object' && 'code' in resData) {
            // code 为 200 视为业务成功
            if (resData.code === 200) {
              resolve(resData.data !== undefined ? resData.data : resData)
            } else {
              showErrorToast(resData.message || '请求失败')
              reject(new Error(resData.message || '请求失败'))
            }
          } else {
            resolve(resData)
          }
        } else {
          const errMsg = `请求失败，状态码：${res.statusCode}`
          showErrorToast(errMsg)
          reject(new Error(errMsg))
        }
      },
      fail(err) {
        showErrorToast('网络异常，请稍后重试')
        reject(err)
      }
    })
  })
}

function get(url, data = {}, options = {}) {
  return request({
    url,
    method: 'GET',
    data,
    ...options
  })
}

function post(url, data = {}, options = {}) {
  return request({
    url,
    method: 'POST',
    data,
    ...options
  })
}

function put(url, data = {}, options = {}) {
  return request({
    url,
    method: 'PUT',
    data,
    ...options
  })
}

function del(url, data = {}, options = {}) {
  return request({
    url,
    method: 'DELETE',
    data,
    ...options
  })
}

function formatDate(date, format = 'YYYY-MM-DD') {
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')

  return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

function formatTime(date) {
  return formatDate(date, 'HH:mm:ss')
}

function formatDateTime(date) {
  return formatDate(date, 'YYYY-MM-DD HH:mm:ss')
}

function debounce(fn, delay) {
  let timer = null
  return function (...args) {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => {
      fn.apply(this, args)
    }, delay)
  }
}

function throttle(fn, interval) {
  let lastTime = 0
  return function (...args) {
    const now = Date.now()
    if (now - lastTime >= interval) {
      lastTime = now
      fn.apply(this, args)
    }
  }
}

function getStorage(key, defaultValue = null) {
  try {
    const value = wx.getStorageSync(key)
    return value ? JSON.parse(value) : defaultValue
  } catch (e) {
    console.error('读取存储失败:', e)
    return defaultValue
  }
}

function setStorage(key, value) {
  try {
    wx.setStorageSync(key, JSON.stringify(value))
    return true
  } catch (e) {
    console.error('写入存储失败:', e)
    return false
  }
}

function removeStorage(key) {
  try {
    wx.removeStorageSync(key)
    return true
  } catch (e) {
    console.error('删除存储失败:', e)
    return false
  }
}

function showToast(title, icon = 'none', duration = 2000) {
  wx.showToast({
    title,
    icon,
    duration
  })
}

function showLoading(title = '加载中') {
  wx.showLoading({
    title,
    mask: true
  })
}

function hideLoading() {
  wx.hideLoading()
}

function showModal(title, content, options = {}) {
  return new Promise((resolve) => {
    wx.showModal({
      title,
      content,
      confirmText: options.confirmText || '确定',
      cancelText: options.cancelText || '取消',
      confirmColor: options.confirmColor || '#4080ff',
      success(res) {
        resolve(res.confirm)
      }
    })
  })
}

function getUserInfo() {
  return getStorage('userInfo', {})
}

function setUserInfo(userInfo) {
  return setStorage('userInfo', userInfo)
}

function clearUserInfo() {
  return removeStorage('userInfo')
}

function generateId() {
  return Date.now().toString(36) + Math.random().toString(36).substr(2)
}

function isEmpty(value) {
  return value === null || value === undefined || value === '' || (Array.isArray(value) && value.length === 0) || (typeof value === 'object' && Object.keys(value).length === 0)
}

function cloneDeep(obj) {
  return JSON.parse(JSON.stringify(obj))
}

function getUrlParams(url) {
  const params = {}
  const urlObj = new URL(url)
  urlObj.searchParams.forEach((value, key) => {
    params[key] = value
  })
  return params
}

function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms))
}

module.exports = {
  baseUrl,
  request,
  get,
  post,
  put,
  del,
  formatDate,
  formatTime,
  formatDateTime,
  debounce,
  throttle,
  getStorage,
  setStorage,
  removeStorage,
  showToast,
  showLoading,
  hideLoading,
  showModal,
  getUserInfo,
  setUserInfo,
  clearUserInfo,
  generateId,
  isEmpty,
  cloneDeep,
  getUrlParams,
  delay
}