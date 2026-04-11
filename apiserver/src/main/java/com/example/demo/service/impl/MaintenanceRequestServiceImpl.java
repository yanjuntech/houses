package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.MaintenanceRequest;
import com.example.demo.mapper.MaintenanceRequestMapper;
import com.example.demo.service.MaintenanceRequestService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MaintenanceRequestServiceImpl extends ServiceImpl<MaintenanceRequestMapper, MaintenanceRequest> implements MaintenanceRequestService {

    @Override
    public List<MaintenanceRequest> getRequestsByHouseId(Long houseId) {
        QueryWrapper<MaintenanceRequest> wrapper = new QueryWrapper<>();
        wrapper.eq("house_id", houseId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<MaintenanceRequest> getRequestsByTenantId(Long tenantId) {
        QueryWrapper<MaintenanceRequest> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_id", tenantId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean updateRequestStatus(Long requestId, Integer status) {
        MaintenanceRequest request = baseMapper.selectById(requestId);
        if (request != null) {
            request.setStatus(status);
            if (status == 2) { // 已完成
                request.setCompleteTime(new Date());
            }
            return updateById(request);
        }
        return false;
    }

    @Override
    public boolean completeAndRate(Long requestId, Integer rating, String comment) {
        MaintenanceRequest request = baseMapper.selectById(requestId);
        if (request != null) {
            request.setStatus(2); // 已完成
            request.setCompleteTime(new Date());
            request.setRating(rating);
            request.setComment(comment);
            return updateById(request);
        }
        return false;
    }
}
