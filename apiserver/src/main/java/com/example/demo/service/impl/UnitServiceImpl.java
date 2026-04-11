package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Unit;
import com.example.demo.mapper.UnitMapper;
import com.example.demo.service.UnitService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements UnitService {

    @Override
    public List<Unit> getUnitsByBuildingId(Long buildingId) {
        QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("building_id", buildingId);
        return this.list(queryWrapper);
    }
}
