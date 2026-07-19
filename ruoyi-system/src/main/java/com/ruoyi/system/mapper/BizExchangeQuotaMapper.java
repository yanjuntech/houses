package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.BizExchangeQuota;

/**
 * 用户兑换配额 数据层
 *
 * @author ruoyi
 */
public interface BizExchangeQuotaMapper
{
    /**
     * 查询用户兑换配额列表
     *
     * @param bizExchangeQuota 用户兑换配额
     * @return 用户兑换配额集合
     */
    public List<BizExchangeQuota> selectBizExchangeQuotaList(BizExchangeQuota bizExchangeQuota);

    /**
     * 根据用户ID和配额类型查询配额
     *
     * @param userId 用户ID
     * @param quotaType 配额类型
     * @return 用户兑换配额对象信息
     */
    public BizExchangeQuota selectByUserIdAndType(@Param("userId") Long userId, @Param("quotaType") String quotaType);

    /**
     * 新增用户兑换配额
     *
     * @param bizExchangeQuota 用户兑换配额
     * @return 结果
     */
    public int insertBizExchangeQuota(BizExchangeQuota bizExchangeQuota);

    /**
     * 修改用户兑换配额
     *
     * @param bizExchangeQuota 用户兑换配额
     * @return 结果
     */
    public int updateBizExchangeQuota(BizExchangeQuota bizExchangeQuota);

    /**
     * 增加配额（不存在则插入，存在则累加）
     *
     * @param userId 用户ID
     * @param quotaType 配额类型
     * @param count 增加数量
     * @return 结果
     */
    public int addQuota(@Param("userId") Long userId, @Param("quotaType") String quotaType, @Param("count") int count);

    /**
     * 扣减配额（used_count + count, remaining_count - count）
     *
     * @param userId 用户ID
     * @param quotaType 配额类型
     * @param count 扣减数量
     * @return 结果
     */
    public int deductQuota(@Param("userId") Long userId, @Param("quotaType") String quotaType, @Param("count") int count);
}
