// 手机号校验正则
const PHONE_REG = /^1[3-9]\d{9}$/

// 校验手机号格式
function isValidPhone(phone) {
  return PHONE_REG.test(String(phone || '').trim())
}

module.exports = {
  PHONE_REG,
  isValidPhone
}
