package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizHouseRepair;
import com.ruoyi.system.mapper.BizHouseRepairMapper;
import com.ruoyi.system.service.IBizHouseRepairService;

/**
 * 房屋维修 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizHouseRepairServiceImpl implements IBizHouseRepairService
{
    @Autowired
    private BizHouseRepairMapper bizHouseRepairMapper;

    /**
     * 查询房屋维修集合
     *
     * @param bizHouseRepair 房屋维修
     * @return 房屋维修集合
     */
    @Override
    public List<BizHouseRepair> selectBizHouseRepairList(BizHouseRepair bizHouseRepair)
    {
        return bizHouseRepairMapper.selectBizHouseRepairList(bizHouseRepair);
    }

    /**
     * 通过维修ID查询房屋维修
     *
     * @param repairId 维修ID
     * @return 房屋维修
     */
    @Override
    public BizHouseRepair selectBizHouseRepairByRepairId(Long repairId)
    {
        return bizHouseRepairMapper.selectBizHouseRepairByRepairId(repairId);
    }

    /**
     * 新增房屋维修
     *
     * @param bizHouseRepair 房屋维修
     * @return 结果
     */
    @Override
    public int insertBizHouseRepair(BizHouseRepair bizHouseRepair)
    {
        return bizHouseRepairMapper.insertBizHouseRepair(bizHouseRepair);
    }

    /**
     * 修改房屋维修
     *
     * @param bizHouseRepair 房屋维修
     * @return 结果
     */
    @Override
    public int updateBizHouseRepair(BizHouseRepair bizHouseRepair)
    {
        return bizHouseRepairMapper.updateBizHouseRepair(bizHouseRepair);
    }

    /**
     * 删除房屋维修
     *
     * @param repairId 维修ID
     * @return 结果
     */
    @Override
    public int deleteBizHouseRepairByRepairId(Long repairId)
    {
        return bizHouseRepairMapper.deleteBizHouseRepairByRepairId(repairId);
    }

    /**
     * 批量删除房屋维修
     *
     * @param repairIds 需要删除的维修ID数组
     * @return 结果
     */
    @Override
    public int deleteBizHouseRepairByRepairIds(Long[] repairIds)
    {
        return bizHouseRepairMapper.deleteBizHouseRepairByRepairIds(repairIds);
    }

    /**
     * 房东确认维修（0待房东确认 → 1维修中）
     *
     * @param repairId 维修ID
     * @return 结果
     */
    @Override
    public int landlordConfirm(Long repairId)
    {
        BizHouseRepair repair = getRepairOrThrow(repairId);
        if (!"0".equals(repair.getStatus()))
        {
            throw new ServiceException("当前状态不允许房东确认维修，仅待房东确认状态可操作");
        }
        BizHouseRepair update = new BizHouseRepair();
        update.setRepairId(repairId);
        update.setStatus("1");
        update.setConfirmTime(new Date());
        return bizHouseRepairMapper.updateStatus(update);
    }

    /**
     * 房东完成维修（1维修中 → 2待租客确认）
     *
     * @param repairId 维修ID
     * @return 结果
     */
    @Override
    public int landlordComplete(Long repairId)
    {
        BizHouseRepair repair = getRepairOrThrow(repairId);
        if (!"1".equals(repair.getStatus()))
        {
            throw new ServiceException("当前状态不允许房东完成维修，仅维修中状态可操作");
        }
        BizHouseRepair update = new BizHouseRepair();
        update.setRepairId(repairId);
        update.setStatus("2");
        update.setCompleteTime(new Date());
        return bizHouseRepairMapper.updateStatus(update);
    }

    /**
     * 租客确认完成（2待租客确认 → 3已完成）
     *
     * @param repairId 维修ID
     * @return 结果
     */
    @Override
    public int tenantConfirm(Long repairId)
    {
        BizHouseRepair repair = getRepairOrThrow(repairId);
        if (!"2".equals(repair.getStatus()))
        {
            throw new ServiceException("当前状态不允许租客确认完成，仅待租客确认状态可操作");
        }
        BizHouseRepair update = new BizHouseRepair();
        update.setRepairId(repairId);
        update.setStatus("3");
        update.setTenantConfirmTime(new Date());
        return bizHouseRepairMapper.updateStatus(update);
    }

    /**
     * 房东选择租客自修（0待房东确认 → 4待租客上传凭证，仅 repair_type=2 可用）
     *
     * @param repairId 维修ID
     * @return 结果
     */
    @Override
    public int landlordChooseTenantRepair(Long repairId)
    {
        BizHouseRepair repair = getRepairOrThrow(repairId);
        if (!"2".equals(repair.getRepairType()))
        {
            throw new ServiceException("仅租客自修报销类型可选择租客自修");
        }
        if (!"0".equals(repair.getStatus()))
        {
            throw new ServiceException("当前状态不允许房东选择租客自修，仅待房东确认状态可操作");
        }
        BizHouseRepair update = new BizHouseRepair();
        update.setRepairId(repairId);
        update.setStatus("4");
        update.setConfirmTime(new Date());
        return bizHouseRepairMapper.updateStatus(update);
    }

    /**
     * 租客上传凭证（4待租客上传凭证 → 5待房东确认报销）
     *
     * @param repairId 维修ID
     * @param receiptImages 报销凭证图片URL列表
     * @param receiptAmount 报销金额
     * @return 结果
     */
    @Override
    public int tenantUploadReceipt(Long repairId, String receiptImages, BigDecimal receiptAmount)
    {
        BizHouseRepair repair = getRepairOrThrow(repairId);
        if (!"4".equals(repair.getStatus()))
        {
            throw new ServiceException("当前状态不允许租客上传凭证，仅待租客上传凭证状态可操作");
        }
        if (StringUtils.isEmpty(receiptImages))
        {
            throw new ServiceException("报销凭证图片不能为空");
        }
        if (receiptAmount == null)
        {
            throw new ServiceException("报销金额不能为空");
        }
        BizHouseRepair update = new BizHouseRepair();
        update.setRepairId(repairId);
        update.setStatus("5");
        update.setReceiptImages(receiptImages);
        update.setReceiptAmount(receiptAmount);
        return bizHouseRepairMapper.updateStatus(update);
    }

    /**
     * 房东确认报销（5待房东确认报销 → 6已报销）
     *
     * @param repairId 维修ID
     * @return 结果
     */
    @Override
    public int landlordConfirmReimburse(Long repairId)
    {
        BizHouseRepair repair = getRepairOrThrow(repairId);
        if (!"5".equals(repair.getStatus()))
        {
            throw new ServiceException("当前状态不允许房东确认报销，仅待房东确认报销状态可操作");
        }
        BizHouseRepair update = new BizHouseRepair();
        update.setRepairId(repairId);
        update.setStatus("6");
        update.setReimburseTime(new Date());
        return bizHouseRepairMapper.updateStatus(update);
    }

    /**
     * 取消维修（0待房东确认 → 7已取消）
     *
     * @param repairId 维修ID
     * @return 结果
     */
    @Override
    public int cancelRepair(Long repairId)
    {
        BizHouseRepair repair = getRepairOrThrow(repairId);
        if (!"0".equals(repair.getStatus()))
        {
            throw new ServiceException("当前状态不允许取消维修，仅待房东确认状态可操作");
        }
        BizHouseRepair update = new BizHouseRepair();
        update.setRepairId(repairId);
        update.setStatus("7");
        return bizHouseRepairMapper.updateStatus(update);
    }

    /**
     * 根据维修ID查询维修单，不存在则抛出异常
     *
     * @param repairId 维修ID
     * @return 维修单
     */
    private BizHouseRepair getRepairOrThrow(Long repairId)
    {
        BizHouseRepair repair = bizHouseRepairMapper.selectBizHouseRepairByRepairId(repairId);
        if (StringUtils.isNull(repair))
        {
            throw new ServiceException("维修单不存在");
        }
        return repair;
    }
}
