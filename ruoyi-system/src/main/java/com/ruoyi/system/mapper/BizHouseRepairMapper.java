package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizHouseRepair;

/**
 * 房屋维修 数据层
 *
 * @author ruoyi
 */
public interface BizHouseRepairMapper
{
    /**
     * 查询房屋维修列表
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
     * 更新维修状态
     *
     * @param bizHouseRepair 房屋维修
     * @return 结果
     */
    public int updateStatus(BizHouseRepair bizHouseRepair);
}
