package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.MaintenanceRequest;

import java.util.List;

public interface MaintenanceRequestService extends IService<MaintenanceRequest> {

    /**
     * 根据房屋ID获取维修申请列表
     */
    List<MaintenanceRequest> getRequestsByHouseId(Long houseId);

    /**
     * 根据租户ID获取维修申请列表
     */
    List<MaintenanceRequest> getRequestsByTenantId(Long tenantId);

    /**
     * 更新维修申请状态
     */
    boolean updateRequestStatus(Long requestId, Integer status);

    /**
     * 完成维修并评价
     */
    boolean completeAndRate(Long requestId, Integer rating, String comment);
}
