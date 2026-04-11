const jwt = require('jsonwebtoken');
const User = require('../models/User');
const config = require('../config');

// 验证用户登录状态
exports.isAuthenticated = async (req, res, next) => {
  try {
    const { token } = req.headers;
    if (!token) {
      return res.status(401).json({ message: 'Access denied. No token provided.' });
    }
    const decoded = jwt.verify(token, config.JWT_SECRET);
    req.user = await User.findById(decoded.id);
    if (!req.user) {
      return res.status(401).json({ message: 'User not found.' });
    }
    next();
  } catch (error) {
    res.status(401).json({ message: 'Invalid token.' });
  }
};

// 验证管理员权限
exports.isAdmin = (req, res, next) => {
  if (req.user.role !== 'admin') {
    return res.status(403).json({ message: 'Access denied. Admin permission required.' });
  }
  next();
};

// 验证房东权限
exports.isLandlord = (req, res, next) => {
  if (req.user.role !== 'landlord' && req.user.role !== 'admin') {
    return res.status(403).json({ message: 'Access denied. Landlord permission required.' });
  }
  next();
};

// 验证租户权限
exports.isTenant = (req, res, next) => {
  if (req.user.role !== 'tenant' && req.user.role !== 'admin') {
    return res.status(403).json({ message: 'Access denied. Tenant permission required.' });
  }
  next();
};