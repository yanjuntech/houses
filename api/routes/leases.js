const express = require('express');
const router = express.Router();
const Lease = require('../models/Lease');
const House = require('../models/House');
const { isAuthenticated } = require('../middleware/auth');

// 获取所有租赁
router.get('/', isAuthenticated, async (req, res) => {
  try {
    const leases = await Lease.find().populate('house', 'title address').populate('tenant', 'username').populate('landlord', 'username');
    res.json(leases);
  } catch (error) {
    res.status(500).json({ message: 'Server error.' });
  }
});

// 获取单个租赁
router.get('/:id', isAuthenticated, async (req, res) => {
  try {
    const lease = await Lease.findById(req.params.id).populate('house', 'title address').populate('tenant', 'username').populate('landlord', 'username');
    if (!lease) {
      return res.status(404).json({ message: 'Lease not found.' });
    }
    res.json(lease);
  } catch (error) {
    res.status(500).json({ message: 'Server error.' });
  }
});

// 创建租赁
router.post('/', isAuthenticated, async (req, res) => {
  try {
    const { houseId, startDate, endDate, monthlyRent, deposit } = req.body;
    const house = await House.findById(houseId);
    if (!house) {
      return res.status(404).json({ message: 'House not found.' });
    }
    if (house.status !== 'available') {
      return res.status(400).json({ message: 'House is not available for rent.' });
    }
    const lease = new Lease({
      house: houseId,
      tenant: req.user._id,
      landlord: house.landlord,
      startDate,
      endDate,
      monthlyRent,
      deposit
    });
    await lease.save();
    house.status = 'rented';
    await house.save();
    res.status(201).json(lease);
  } catch (error) {
    res.status(500).json({ message: 'Server error.' });
  }
});

// 更新租赁
router.put('/:id', isAuthenticated, async (req, res) => {
  try {
    const lease = await Lease.findById(req.params.id);
    if (!lease) {
      return res.status(404).json({ message: 'Lease not found.' });
    }
    if (lease.tenant.toString() !== req.user._id.toString() && lease.landlord.toString() !== req.user._id.toString() && req.user.role !== 'admin') {
      return res.status(403).json({ message: 'Access denied.' });
    }
    const { startDate, endDate, monthlyRent, deposit, status } = req.body;
    lease.startDate = startDate || lease.startDate;
    lease.endDate = endDate || lease.endDate;
    lease.monthlyRent = monthlyRent || lease.monthlyRent;
    lease.deposit = deposit || lease.deposit;
    lease.status = status || lease.status;
    lease.updatedAt = Date.now();
    await lease.save();
    if (status === 'ended' || status === 'cancelled') {
      const house = await House.findById(lease.house);
      house.status = 'available';
      await house.save();
    }
    res.json(lease);
  } catch (error) {
    res.status(500).json({ message: 'Server error.' });
  }
});

// 删除租赁
router.delete('/:id', isAuthenticated, async (req, res) => {
  try {
    const lease = await Lease.findById(req.params.id);
    if (!lease) {
      return res.status(404).json({ message: 'Lease not found.' });
    }
    if (lease.tenant.toString() !== req.user._id.toString() && lease.landlord.toString() !== req.user._id.toString() && req.user.role !== 'admin') {
      return res.status(403).json({ message: 'Access denied.' });
    }
    const house = await House.findById(lease.house);
    house.status = 'available';
    await house.save();
    await lease.remove();
    res.json({ message: 'Lease removed.' });
  } catch (error) {
    res.status(500).json({ message: 'Server error.' });
  }
});

// 获取租户的所有租赁
router.get('/tenant/:id', isAuthenticated, async (req, res) => {
  try {
    const leases = await Lease.find({ tenant: req.params.id }).populate('house', 'title address').populate('landlord', 'username');
    res.json(leases);
  } catch (error) {
    res.status(500).json({ message: 'Server error.' });
  }
});

// 获取房东的所有租赁
router.get('/landlord/:id', isAuthenticated, async (req, res) => {
  try {
    const leases = await Lease.find({ landlord: req.params.id }).populate('house', 'title address').populate('tenant', 'username');
    res.json(leases);
  } catch (error) {
    res.status(500).json({ message: 'Server error.' });
  }
});

module.exports = router;