package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.House;
import com.example.demo.mapper.HouseMapper;
import com.example.demo.service.HouseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {

    @Override
    public List<House> getHousesByType(Integer houseType) {
        QueryWrapper<House> wrapper = new QueryWrapper<>();
        wrapper.eq("house_type", houseType);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<House> getHousesByStatus(Integer status) {
        QueryWrapper<House> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean publishHouse(Long houseId, Integer isPublished) {
        House house = baseMapper.selectById(houseId);
        if (house != null) {
            house.setIsPublished(isPublished);
            return updateById(house);
        }
        return false;
    }

    @Override
    public boolean updateHouseStatus(Long houseId, Integer status) {
        House house = baseMapper.selectById(houseId);
        if (house != null) {
            house.setStatus(status);
            return updateById(house);
        }
        return false;
    }

    @Override
    public List<House> getHousesByCommunityId(Long communityId) {
        QueryWrapper<House> wrapper = new QueryWrapper<>();
        wrapper.eq("community_id", communityId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<House> getHousesByOwnerId(Long ownerId) {
        QueryWrapper<House> wrapper = new QueryWrapper<>();
        wrapper.eq("owner_id", ownerId);
        return baseMapper.selectList(wrapper);
    }
}
