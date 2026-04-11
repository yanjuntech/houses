package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Building;
import com.example.demo.mapper.BuildingMapper;
import com.example.demo.service.BuildingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {

    @Override
    public List<Building> getBuildingsByCommunityId(Long communityId) {
        QueryWrapper<Building> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("community_id", communityId);
        return this.list(queryWrapper);
    }
}
