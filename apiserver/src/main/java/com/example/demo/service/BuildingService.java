package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Building;

import java.util.List;

public interface BuildingService extends IService<Building> {
    List<Building> getBuildingsByCommunityId(Long communityId);
}
