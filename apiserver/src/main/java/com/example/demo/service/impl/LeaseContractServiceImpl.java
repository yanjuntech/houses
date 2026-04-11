package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.LeaseContract;
import com.example.demo.mapper.LeaseContractMapper;
import com.example.demo.service.LeaseContractService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaseContractServiceImpl extends ServiceImpl<LeaseContractMapper, LeaseContract> implements LeaseContractService {

    @Override
    public List<LeaseContract> getContractsByLandlordId(Long landlordId) {
        QueryWrapper<LeaseContract> wrapper = new QueryWrapper<>();
        wrapper.eq("landlord_id", landlordId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<LeaseContract> getContractsByTenantId(Long tenantId) {
        QueryWrapper<LeaseContract> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_id", tenantId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<LeaseContract> getContractsByHouseId(Long houseId) {
        QueryWrapper<LeaseContract> wrapper = new QueryWrapper<>();
        wrapper.eq("house_id", houseId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean updateContractStatus(Long contractId, Integer status) {
        LeaseContract contract = baseMapper.selectById(contractId);
        if (contract != null) {
            contract.setStatus(status);
            return updateById(contract);
        }
        return false;
    }

    @Override
    public boolean createContract(LeaseContract contract) {
        // 生成合同编号
        String contractNo = "CON" + System.currentTimeMillis();
        contract.setContractNo(contractNo);
        return save(contract);
    }
}
