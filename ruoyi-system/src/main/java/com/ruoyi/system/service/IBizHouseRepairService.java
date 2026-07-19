package com.ruoyi.system.service;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.system.domain.BizHouseRepair;

/**
 * 房屋维修 服务层
 *
 * @author ruoyi
 */
public interface IBizHouseRepairService
{
    /**
     * 查询房屋维修集合
     *
     * @param bizHouseRepair 房屋维修
     * @return 房屋维修集合
     */
    public List<BizHouseRepair> selectBizHouseRepairList(BizHouseRepair bizHouseRepair);

    /**
     * 通过维修ID查询房屋维修
     *
     * @param repairId 维修ID
     * @return 房屋维修
     */
    public BizHouseRepair selectBizHouseRepairByRepairId(Long repairId);

    /**
     * 新增房屋维修
     *
     * @param bizHouseRepair 房屋维修
     * @return 结果
     */
    public int insertBizHouseRepair(BizHouseRepair bizHouseRepair);

    /**
     * 修改房屋维修
     *
     * @param bizHouseRepair 房屋维修
     * @return 结果
     */
    public int updateBizHouseRepair(BizHouseRepair bizHouseRepair);

    /**
     * 删除房屋维修
     *
     * @param repairId 维修ID
     * @return 结果
     */
    public int deleteBizHouseRepairByRepairId(Long repairId);

    /**
     * 批量删除房屋维修
     *
     * @param repairIds 需要删除的维修ID数组
     * @return 结果
     */
    public int deleteBizHouseRepairByRepairIds(Long[] repairIds);

    /**
     * 房东确认维修（0待房东确认 → 1维修中）
     *
     * @param repairId 维修ID
     * @return 结果
     */
    public int landlordConfirm(Long repairId);

    /**
     * 房东完成维修（1维修中 → 2待租客确认）
     *
     * @param repairId 维修ID
     * @return 结果
     */
    public int landlordComplete(Long repairId);

    /**
     * 租客确认完成（2待租客确认 → 3已完成）
     *
     * @param repairId 维修ID
     * @return 结果
     */
    public int tenantConfirm(Long repairId);

    /**
     * 房东选择租客自修（0待房东确认 → 4待租客上传凭证，仅 repair_type=2 可用）
     *
     * @param repairId 维修ID
     * @return 结果
     */
    public int landlordChooseTenantRepair(Long repairId);

    /**
     * 租客上传凭证（4待租客上传凭证 → 5待房东确认报销）
     *
     * @param repairId 维修ID
     * @param receiptImages 报销凭证图片URL列表
     * @param receiptAmount 报销金额
     * @return 结果
     */
    public int tenantUploadReceipt(Long repairId, String receiptImages, BigDecimal receiptAmount);

    /**
     * 房东确认报销（5待房东确认报销 → 6已报销）
     *
     * @param repairId 维修ID
     * @return 结果
     */
    public int landlordConfirmReimburse(Long repairId);

    /**
     * 取消维修（0待房东确认 → 7已取消）
     *
     * @param repairId 维修ID
     * @return 结果
     */
    public int cancelRepair(Long repairId);
}
