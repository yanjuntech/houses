package com.example.demo.controller;

import com.example.demo.entity.LeaseContract;
import com.example.demo.service.LeaseContractService;
import com.example.demo.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contract")
public class LeaseContractController {

    @Autowired
    private LeaseContractService contractService;

    /**
     * 获取合同列表
     */
    @GetMapping("/list")
    public ResponseVO<List<LeaseContract>> list(
            @RequestParam(required = false) Long landlordId,
            @RequestParam(required = false) Long tenantId,
            @RequestParam(required = false) Long houseId
    ) {
        if (landlordId != null) {
            return ResponseVO.success(contractService.getContractsByLandlordId(landlordId));
        } else if (tenantId != null) {
            return ResponseVO.success(contractService.getContractsByTenantId(tenantId));
        } else if (houseId != null) {
            return ResponseVO.success(contractService.getContractsByHouseId(houseId));
        }
        return ResponseVO.success(contractService.list());
    }

    /**
     * 根据ID获取合同
     */
    @GetMapping("/get/{id}")
    public ResponseVO<LeaseContract> getById(@PathVariable Long id) {
        return ResponseVO.success(contractService.getById(id));
    }

    /**
     * 创建合同
     */
    @PostMapping("/create")
    public ResponseVO<Boolean> create(@RequestBody LeaseContract contract) {
        return ResponseVO.success(contractService.createContract(contract));
    }

    /**
     * 更新合同
     */
    @PutMapping("/update")
    public ResponseVO<Boolean> update(@RequestBody LeaseContract contract) {
        return ResponseVO.success(contractService.updateById(contract));
    }

    /**
     * 删除合同
     */
    @DeleteMapping("/delete/{id}")
    public ResponseVO<Boolean> delete(@PathVariable Long id) {
        return ResponseVO.success(contractService.removeById(id));
    }

    /**
     * 更新合同状态
     */
    @PutMapping("/status")
    public ResponseVO<Boolean> status(@RequestParam Long contractId, @RequestParam Integer status) {
        return ResponseVO.success(contractService.updateContractStatus(contractId, status));
    }
}
