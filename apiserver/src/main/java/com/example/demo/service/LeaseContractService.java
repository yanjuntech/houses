package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.LeaseContract;

import java.util.List;

public interface LeaseContractService extends IService<LeaseContract> {

    /**
     * 根据房东ID获取合同列表
     */
    List<LeaseContract> getContractsByLandlordId(Long landlordId);

    /**
     * 根据租户ID获取合同列表
     */
    List<LeaseContract> getContractsByTenantId(Long tenantId);

    /**
     * 根据房屋ID获取合同列表
     */
    List<LeaseContract> getContractsByHouseId(Long houseId);

    /**
     * 更新合同状态
     */
    boolean updateContractStatus(Long contractId, Integer status);

    /**
     * 创建合同
     */
    boolean createContract(LeaseContract contract);
}
