const express = require('express');
const cors = require('cors');
const connectDB = require('./db');
const config = require('./config');

// 导入路由
const authRoutes = require('./routes/auth');
const houseRoutes = require('./routes/houses');
const leaseRoutes = require('./routes/leases');

const app = express();

// 中间件
app.use(cors());
app.use(express.json());

// 路由
app.use('/api/auth', authRoutes);
app.use('/api/houses', houseRoutes);
app.use('/api/leases', leaseRoutes);

// 连接数据库
connectDB();

// 启动服务器
app.listen(config.PORT, () => {
  console.log(`Server running on port ${config.PORT}`);
});

module.exports = app;