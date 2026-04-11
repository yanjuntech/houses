package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Unit;

import java.util.List;

public interface UnitService extends IService<Unit> {
    List<Unit> getUnitsByBuildingId(Long buildingId);
}
