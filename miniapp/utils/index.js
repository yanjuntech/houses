const baseUrl = ''

function request(options) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        ...options.header
      },
      success(res) {
        if (res.statusCode === 200) {
          resolve(res.data)
        } else {
          reject(new Error(`请求失败，状态码：${res.statusCode}`))
        }
      },
      fail(err) {
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