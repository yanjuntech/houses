package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.BizExchangeQuota;
import com.ruoyi.system.mapper.BizExchangeQuotaMapper;
import com.ruoyi.system.service.IBizExchangeQuotaService;

/**
 * 用户兑换配额 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizExchangeQuotaServiceImpl implements IBizExchangeQuotaService
{
    @Autowired
    private BizExchangeQuotaMapper bizExchangeQuotaMapper;

    /**
     * 查询用户兑换配额集合
     *
     * @param bizExchangeQuota 用户兑换配额
     * @return 用户兑换配额集合
     */
    @Override
    public List<BizExchangeQuota> selectBizExchangeQuotaList(BizExchangeQuota bizExchangeQuota)
    {
        return bizExchangeQuotaMapper.selectBizExchangeQuotaList(bizExchangeQuota);
    }

    /**
     * 根据用户ID和配额类型查询配额
     *
     * @param userId 用户ID
     * @param quotaType 配额类型
     * @return 用户兑换配额对象信息
     */
    @Override
    public BizExchangeQuota selectByUserIdAndType(Long userId, String quotaType)
    {
        return bizExchangeQuotaMapper.selectByUserIdAndType(userId, quotaType);
    }

    /**
     * 新增用户兑换配额
     *
     * @param bizExchangeQuota 用户兑换配额
     * @return 结果
     */
    @Override
    public int insertBizExchangeQuota(BizExchangeQuota bizExchangeQuota)
    {
        return bizExchangeQuotaMapper.insertBizExchangeQuota(bizExchangeQuota);
    }

    /**
     * 修改用户兑换配额
     *
     * @param bizExchangeQuota 用户兑换配额
     * @return 结果
     */
    @Override
    public int updateBizExchangeQuota(BizExchangeQuota bizExchangeQuota)
    {
        return bizExchangeQuotaMapper.updateBizExchangeQuota(bizExchangeQuota);
    }

    /**
     * 增加配额（不存在则插入，存在则累加）
     *
     * @param userId 用户ID
     * @param quotaType 配额类型
     * @param count 增加数量
     * @return 结果
     */
    @Override
    public int addQuota(Long userId, String quotaType, int count)
    {
        return bizExchangeQuotaMapper.addQuota(userId, quotaType, count);
    }

    /**
     * 扣减配额（校验剩余配额是否足够，不足抛出业务异常）
     *
     * @param userId 用户ID
     * @param quotaType 配额类型
     * @param count 扣减数量
     * @return 结果
     */
    @Override
    public int deductQuota(Long userId, String quotaType, int count)
    {
        BizExchangeQuota quota = bizExchangeQuotaMapper.selectByUserIdAndType(userId, quotaType);
        if (quota == null || quota.getRemainingCount() == null || quota.getRemainingCount() < count)
        {
            throw new ServiceException("可用配额不足，请先兑换");
        }
        return bizExchangeQuotaMapper.deductQuota(userId, quotaType, count);
    }
}
