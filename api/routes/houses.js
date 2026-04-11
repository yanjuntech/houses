const express = require('express');
const router = express.Router();
const House = require('../models/House');
const { isAuthenticated, isLandlord } = require('../middleware/auth');

// 获取所有房屋
router.get('/', async (req, res) => {
  try {
    const houses = await House.find().populate('landlord', 'username');
    res.json(houses);
  } catch (error) {
    res.status(500).json({ message: 'Server error.' });
  }
});

// 获取单个房屋
router.get('/:id', async (req, res) => {
  try {
    const house = await House.findById(req.params.id).populate('landlord', 'username');
    if (!house) {
      return res.status(404).json({ message: 'House not found.' });
    }
    res.json(house);
  } catch (error) {
    res.status(500).json({ message: 'Server error.' });
  }
});

// 创建房屋
router.post('/', isAuthenticated, isLandlord, async (req, res) => {
  try {
    const { title, description, price, address, area, bedrooms, bathrooms, type, images } = req.body;
    const house = new House({
      title,
      description,
      price,
      address,
      area,
      bedrooms,
      bathrooms,
      type,
      landlord: req.user._id,
      images
    });
    await house.save();
    res.status(201).json(house);
  } catch (error) {
    res.status(500).json({ message: 'Server error.' });
  }
});

// 更新房屋
router.put('/:id', isAuthenticated, isLandlord, async (req, res) => {
  try {
    const house = await House.findById(req.params.id);
    if (!house) {
      return res.status(404).json({ message: 'House not found.' });
    }
    if (house.landlord.toString() !== req.user._id.toString() && req.user.role !== 'admin') {
      return res.status(403).json({ message: 'Access denied.' });
    }
    const { title, description, price, address, area, bedrooms, bathrooms, type, status, images } = req.body;
    house.title = title || house.title;
    house.description = description || house.description;
    house.price = price || house.price;
    house.address = address || house.address;
    house.area = area || house.area;
    house.bedrooms = bedrooms || house.bedrooms;
    house.bathrooms = bathrooms || house.bathrooms;
    house.type = type || house.type;
    house.status = status || house.status;
    house.images = images || house.images;
    house.updatedAt = Date.now();
    await house.save();
    res.json(house);
  } catch (error) {
    res.status(500).json({ message: 'Server error.' });
  }
});

// 删除房屋
router.delete('/:id', isAuthenticated, isLandlord, async (req, res) => {
  try {
    const house = await House.findById(req.params.id);
    if (!house) {
      return res.status(404).json({ message: 'House not found.' });
    }
    if (house.landlord.toString() !== req.user._id.toString() && req.user.role !== 'admin') {
      return res.status(403).json({ message: 'Access denied.' });
    }
    await house.remove();
    res.json({ message: 'House removed.' });
  } catch (error) {
    res.status(500).json({ message: 'Server error.' });
  }
});

// 获取房东的所有房屋
router.get('/landlord/:id', async (req, res) => {
  try {
    const houses = await House.find({ landlord: req.params.id }).populate('landlord', 'username');
    res.json(houses);
  } catch (error) {
    res.status(500).json({ message: 'Server error.' });
  }
});

module.exports = router;