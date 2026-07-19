package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizRentalContract;

/**
 * 在租房屋合同 服务层
 *
 * @author ruoyi
 */
public interface IBizRentalContractService
{
    /**
     * 查询在租房屋合同集合
     *
     * @param bizRentalContract 在租房屋合同
     * @return 在租房屋合同集合
     */
    public List<BizRentalContract> selectBizRentalContractList(BizRentalContract bizRentalContract);

    /**
     * 通过在租ID查询在租房屋合同
     *
     * @param rentalId 在租ID
     * @return 在租房屋合同
     */
    public BizRentalContract selectBizRentalContractByRentalId(Long rentalId);

    /**
     * 新增在租房屋合同
     *
     * @param bizRentalContract 在租房屋合同
     * @return 结果
     */
    public int insertBizRentalContract(BizRentalContract bizRentalContract);

    /**
     * 修改在租房屋合同
     *
     * @param bizRentalContract 在租房屋合同
     * @return 结果
     */
    public int updateBizRentalContract(BizRentalContract bizRentalContract);

    /**
     * 删除在租房屋合同
     *
     * @param rentalId 在租ID
     * @return 结果
     */
    public int deleteBizRentalContractByRentalId(Long rentalId);

    /**
     * 批量删除在租房屋合同
     *
     * @param rentalIds 需要删除的在租ID数组
     * @return 结果
     */
    public int deleteBizRentalContractByRentalIds(Long[] rentalIds);

    /**
     * 查询即将到期的在租房屋合同（end_date 在 days 天内）
     *
     * @param days 天数
     * @return 在租房屋合同集合
     */
    public List<BizRentalContract> selectExpiringSoon(int days);

    /**
     * 查询已过期的在租房屋合同
     *
     * @return 在租房屋合同集合
     */
    public List<BizRentalContract> selectExpired();

    /**
     * 刷新到期状态
     * end_date 早于当前时间置为 2（已过期）
     * end_date 在 7 天内置为 1（即将到期）
     *
     * @return 结果
     */
    public int refreshExpireStatus();
}
