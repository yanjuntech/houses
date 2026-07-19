package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizMallExchangeRecord;

/**
 * 兑换记录 服务层
 *
 * @author ruoyi
 */
public interface IBizMallExchangeRecordService
{
    /**
     * 查询兑换记录集合
     *
     * @param bizMallExchangeRecord 兑换记录
     * @return 兑换记录集合
     */
    public List<BizMallExchangeRecord> selectBizMallExchangeRecordList(BizMallExchangeRecord bizMallExchangeRecord);

    /**
     * 通过兑换记录ID查询兑换记录
     *
     * @param recordId 兑换记录ID
     * @return 兑换记录对象信息
     */
    public BizMallExchangeRecord selectBizMallExchangeRecordByRecordId(Long recordId);

    /**
     * 查询某用户的兑换记录
     *
     * @param userId 用户ID
     * @return 兑换记录集合
     */
    public List<BizMallExchangeRecord> selectBizMallExchangeRecordByUserId(Long userId);

    /**
     * 新增兑换记录
     *
     * @param bizMallExchangeRecord 兑换记录
     * @return 结果
     */
    public int insertBizMallExchangeRecord(BizMallExchangeRecord bizMallExchangeRecord);

    /**
     * 修改兑换记录
     *
     * @param bizMallExchangeRecord 兑换记录
     * @return 结果
     */
    public int updateBizMallExchangeRecord(BizMallExchangeRecord bizMallExchangeRecord);

    /**
     * 删除兑换记录
     *
     * @param recordId 兑换记录ID
     * @return 结果
     */
    public int deleteBizMallExchangeRecordByRecordId(Long recordId);

    /**
     * 批量删除兑换记录
     *
     * @param recordIds 需要删除的兑换记录ID
     * @return 结果
     */
    public int deleteBizMallExchangeRecordByRecordIds(Long[] recordIds);

    /**
     * 用户兑换商品
     * 核心逻辑：
     * 1. 校验商品库存 > 0
     * 2. 校验用户邀请人数 >= required_invites
     * 3. 扣减商品库存
     * 4. 写入兑换记录
     * 5. 根据商品类型自动生效到 biz_exchange_quota
     *
     * @param bizMallExchangeRecord 兑换记录（需包含 userId、productId 等信息）
     * @return 结果
     */
    public int exchange(BizMallExchangeRecord bizMallExchangeRecord);
}
