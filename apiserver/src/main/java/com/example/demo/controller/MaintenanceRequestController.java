package com.example.demo.controller;

import com.example.demo.entity.MaintenanceRequest;
import com.example.demo.service.MaintenanceRequestService;
import com.example.demo.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceRequestController {

    @Autowired
    private MaintenanceRequestService maintenanceService;

    /**
     * 获取维修申请列表
     */
    @GetMapping("/list")
    public ResponseVO<List<MaintenanceRequest>> list(
            @RequestParam(required = false) Long houseId,
            @RequestParam(required = false) Long tenantId
    ) {
        if (houseId != null) {
            return ResponseVO.success(maintenanceService.getRequestsByHouseId(houseId));
        } else if (tenantId != null) {
            return ResponseVO.success(maintenanceService.getRequestsByTenantId(tenantId));
        }
        return ResponseVO.success(maintenanceService.list());
    }

    /**
     * 根据ID获取维修申请
     */
    @GetMapping("/get/{id}")
    public ResponseVO<MaintenanceRequest> getById(@PathVariable Long id) {
        return ResponseVO.success(maintenanceService.getById(id));
    }

    /**
     * 创建维修申请
     */
    @PostMapping("/create")
    public ResponseVO<Boolean> create(@RequestBody MaintenanceRequest request) {
        return ResponseVO.success(maintenanceService.save(request));
    }

    /**
     * 更新维修申请
     */
    @PutMapping("/update")
    public ResponseVO<Boolean> update(@RequestBody MaintenanceRequest request) {
        return ResponseVO.success(maintenanceService.updateById(request));
    }

    /**
     * 删除维修申请
     */
    @DeleteMapping("/delete/{id}")
    public ResponseVO<Boolean> delete(@PathVariable Long id) {
        return ResponseVO.success(maintenanceService.removeById(id));
    }

    /**
     * 更新维修申请状态
     */
    @PutMapping("/status")
    public ResponseVO<Boolean> status(@RequestParam Long requestId, @RequestParam Integer status) {
        return ResponseVO.success(maintenanceService.updateRequestStatus(requestId, status));
    }

    /**
     * 完成维修并评价
     */
    @PutMapping("/complete")
    public ResponseVO<Boolean> complete(
            @RequestParam Long requestId,
            @RequestParam Integer rating,
            @RequestParam String comment
    ) {
        return ResponseVO.success(maintenanceService.completeAndRate(requestId, rating, comment));
    }
}
