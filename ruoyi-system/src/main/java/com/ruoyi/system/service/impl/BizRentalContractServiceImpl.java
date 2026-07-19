package com.ruoyi.system.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizRentalContract;
import com.ruoyi.system.mapper.BizRentalContractMapper;
import com.ruoyi.system.service.IBizRentalContractService;

/**
 * 在租房屋合同 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizRentalContractServiceImpl implements IBizRentalContractService
{
    @Autowired
    private BizRentalContractMapper bizRentalContractMapper;

    /**
     * 查询在租房屋合同集合
     * 同时计算 remainingDays（end_date - 当前日期）
     *
     * @param bizRentalContract 在租房屋合同
     * @return 在租房屋合同集合
     */
    @Override
    public List<BizRentalContract> selectBizRentalContractList(BizRentalContract bizRentalContract)
    {
        List<BizRentalContract> list = bizRentalContractMapper.selectBizRentalContractList(bizRentalContract);
        for (BizRentalContract contract : list)
        {
            buildRemainingDays(contract);
        }
        return list;
    }

    /**
     * 通过在租ID查询在租房屋合同
     * 同时计算 remainingDays（end_date - 当前日期）
     *
     * @param rentalId 在租ID
     * @return 在租房屋合同
     */
    @Override
    public BizRentalContract selectBizRentalContractByRentalId(Long rentalId)
    {
        BizRentalContract contract = bizRentalContractMapper.selectBizRentalContractByRentalId(rentalId);
        if (StringUtils.isNotNull(contract))
        {
            buildRemainingDays(contract);
        }
        return contract;
    }

    /**
     * 新增在租房屋合同
     *
     * @param bizRentalContract 在租房屋合同
     * @return 结果
     */
    @Override
    public int insertBizRentalContract(BizRentalContract bizRentalContract)
    {
        return bizRentalContractMapper.insertBizRentalContract(bizRentalContract);
    }

    /**
     * 修改在租房屋合同
     *
     * @param bizRentalContract 在租房屋合同
     * @return 结果
     */
    @Override
    public int updateBizRentalContract(BizRentalContract bizRentalContract)
    {
        return bizRentalContractMapper.updateBizRentalContract(bizRentalContract);
    }

    /**
     * 删除在租房屋合同
     *
     * @param rentalId 在租ID
     * @return 结果
     */
    @Override
    public int deleteBizRentalContractByRentalId(Long rentalId)
    {
        return bizRentalContractMapper.deleteBizRentalContractByRentalId(rentalId);
    }

    /**
     * 批量删除在租房屋合同
     *
     * @param rentalIds 需要删除的在租ID数组
     * @return 结果
     */
    @Override
    public int deleteBizRentalContractByRentalIds(Long[] rentalIds)
    {
        return bizRentalContractMapper.deleteBizRentalContractByRentalIds(rentalIds);
    }

    /**
     * 查询即将到期的在租房屋合同（end_date 在 days 天内）
     * 同时计算 remainingDays
     *
     * @param days 天数
     * @return 在租房屋合同集合
     */
    @Override
    public List<BizRentalContract> selectExpiringSoon(int days)
    {
        List<BizRentalContract> list = bizRentalContractMapper.selectExpiringSoon(days);
        for (BizRentalContract contract : list)
        {
            buildRemainingDays(contract);
        }
        return list;
    }

    /**
     * 查询已过期的在租房屋合同
     * 同时计算 remainingDays
     *
     * @return 在租房屋合同集合
     */
    @Override
    public List<BizRentalContract> selectExpired()
    {
        List<BizRentalContract> list = bizRentalContractMapper.selectExpired();
        for (BizRentalContract contract : list)
        {
            buildRemainingDays(contract);
        }
        return list;
    }

    /**
     * 刷新到期状态
     * end_date 早于当前时间置为 2（已过期）
     * end_date 在 7 天内置为 1（即将到期）
     *
     * @return 结果
     */
    @Override
    public int refreshExpireStatus()
    {
        return bizRentalContractMapper.updateExpireStatus();
    }

    /**
     * 计算 remainingDays（end_date - 当前日期，单位：天）
     * 若 end_date 为空则不计算
     *
     * @param contract 在租房屋合同
     */
    private void buildRemainingDays(BizRentalContract contract)
    {
        if (StringUtils.isNull(contract) || StringUtils.isNull(contract.getEndDate()))
        {
            return;
        }
        Date endDate = contract.getEndDate();
        LocalDate endDateLocal = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();
        long days = ChronoUnit.DAYS.between(today, endDateLocal);
        contract.setRemainingDays(days);
    }
}
