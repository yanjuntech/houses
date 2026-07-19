package com.ruoyi.system.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizExchangeQuota;
import com.ruoyi.system.domain.BizMallExchangeRecord;
import com.ruoyi.system.domain.BizMallProduct;
import com.ruoyi.system.domain.BizMiniappUser;
import com.ruoyi.system.mapper.BizExchangeQuotaMapper;
import com.ruoyi.system.mapper.BizInviteRelationMapper;
import com.ruoyi.system.mapper.BizMallExchangeRecordMapper;
import com.ruoyi.system.mapper.BizMallProductMapper;
import com.ruoyi.system.mapper.BizMiniappUserMapper;
import com.ruoyi.system.service.IBizMallExchangeRecordService;

/**
 * 兑换记录 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizMallExchangeRecordServiceImpl implements IBizMallExchangeRecordService
{
    @Autowired
    private BizMallExchangeRecordMapper bizMallExchangeRecordMapper;

    @Autowired
    private BizMallProductMapper bizMallProductMapper;

    @Autowired
    private BizInviteRelationMapper bizInviteRelationMapper;

    @Autowired
    private BizExchangeQuotaMapper bizExchangeQuotaMapper;

    @Autowired
    private BizMiniappUserMapper bizMiniappUserMapper;

    /**
     * 查询兑换记录集合
     *
     * @param bizMallExchangeRecord 兑换记录
     * @return 兑换记录集合
     */
    @Override
    public List<BizMallExchangeRecord> selectBizMallExchangeRecordList(BizMallExchangeRecord bizMallExchangeRecord)
    {
        return bizMallExchangeRecordMapper.selectBizMallExchangeRecordList(bizMallExchangeRecord);
    }

    /**
     * 通过兑换记录ID查询兑换记录
     *
     * @param recordId 兑换记录ID
     * @return 兑换记录对象信息
     */
    @Override
    public BizMallExchangeRecord selectBizMallExchangeRecordByRecordId(Long recordId)
    {
        return bizMallExchangeRecordMapper.selectBizMallExchangeRecordByRecordId(recordId);
    }

    /**
     * 查询某用户的兑换记录
     *
     * @param userId 用户ID
     * @return 兑换记录集合
     */
    @Override
    public List<BizMallExchangeRecord> selectBizMallExchangeRecordByUserId(Long userId)
    {
        return bizMallExchangeRecordMapper.selectBizMallExchangeRecordByUserId(userId);
    }

    /**
     * 新增兑换记录
     *
     * @param bizMallExchangeRecord 兑换记录
     * @return 结果
     */
    @Override
    public int insertBizMallExchangeRecord(BizMallExchangeRecord bizMallExchangeRecord)
    {
        return bizMallExchangeRecordMapper.insertBizMallExchangeRecord(bizMallExchangeRecord);
    }

    /**
     * 修改兑换记录
     *
     * @param bizMallExchangeRecord 兑换记录
     * @return 结果
     */
    @Override
    public int updateBizMallExchangeRecord(BizMallExchangeRecord bizMallExchangeRecord)
    {
        return bizMallExchangeRecordMapper.updateBizMallExchangeRecord(bizMallExchangeRecord);
    }

    /**
     * 删除兑换记录
     *
     * @param recordId 兑换记录ID
     * @return 结果
     */
    @Override
    public int deleteBizMallExchangeRecordByRecordId(Long recordId)
    {
        return bizMallExchangeRecordMapper.deleteBizMallExchangeRecordByRecordId(recordId);
    }

    /**
     * 批量删除兑换记录
     *
     * @param recordIds 需要删除的兑换记录ID
     * @return 结果
     */
    @Override
    public int deleteBizMallExchangeRecordByRecordIds(Long[] recordIds)
    {
        return bizMallExchangeRecordMapper.deleteBizMallExchangeRecordByRecordIds(recordIds);
    }

    /**
     * 用户兑换商品（核心业务逻辑）
     * 1. 校验商品库存 > 0
     * 2. 校验用户邀请人数 >= required_invites
     * 3. 扣减商品库存
     * 4. 写入兑换记录
     * 5. 根据商品类型自动生效到 biz_exchange_quota
     *
     * @param bizMallExchangeRecord 兑换记录（需包含 userId、productId 等信息）
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int exchange(BizMallExchangeRecord bizMallExchangeRecord)
    {
        Long productId = bizMallExchangeRecord.getProductId();
        Long userId = bizMallExchangeRecord.getUserId();
        if (productId == null)
        {
            throw new ServiceException("商品ID不能为空");
        }
        if (userId == null)
        {
            throw new ServiceException("用户ID不能为空");
        }

        // 1. 查询商品并校验库存
        BizMallProduct product = bizMallProductMapper.selectBizMallProductByProductId(productId);
        if (product == null)
        {
            throw new ServiceException("商品不存在");
        }
        if (!"0".equals(product.getStatus()))
        {
            throw new ServiceException("商品已下架，无法兑换");
        }
        if (product.getStock() == null || product.getStock() <= 0)
        {
            throw new ServiceException("商品库存不足");
        }

        // 2. 校验用户邀请人数
        int requiredInvites = product.getRequiredInvites() == null ? 0 : product.getRequiredInvites();
        int userInviteCount = bizInviteRelationMapper.selectInviteCountByInviter(userId);
        if (userInviteCount < requiredInvites)
        {
            throw new ServiceException("邀请人数不足，需要 " + requiredInvites + " 人，当前 " + userInviteCount + " 人");
        }

        // 3. 扣减商品库存
        product.setStock(product.getStock() - 1);
        bizMallProductMapper.updateBizMallProduct(product);

        // 4. 写入兑换记录（补充冗余字段）
        if (StringUtils.isEmpty(bizMallExchangeRecord.getUserName()) || StringUtils.isEmpty(bizMallExchangeRecord.getUserPhone()))
        {
            BizMiniappUser user = bizMiniappUserMapper.selectBizMiniappUserByUserId(userId);
            if (user != null)
            {
                if (StringUtils.isEmpty(bizMallExchangeRecord.getUserName()))
                {
                    bizMallExchangeRecord.setUserName(user.getNickname());
                }
                if (StringUtils.isEmpty(bizMallExchangeRecord.getUserPhone()))
                {
                    bizMallExchangeRecord.setUserPhone(user.getPhone());
                }
            }
        }
        bizMallExchangeRecord.setProductName(product.getProductName());
        bizMallExchangeRecord.setProductType(product.getProductType());
        bizMallExchangeRecord.setCostInvites(requiredInvites);
        bizMallExchangeRecord.setExchangeTime(new Date());
        bizMallExchangeRecord.setEffectStatus("0");
        bizMallExchangeRecord.setCreateTime(new Date());
        bizMallExchangeRecordMapper.insertBizMallExchangeRecord(bizMallExchangeRecord);

        // 5. 根据商品类型自动生效到 biz_exchange_quota
        applyProductEffect(product, userId);

        return 1;
    }

    /**
     * 根据商品类型自动生效到用户配额表
     * - HOUSE_PUBLISH_10 → 增加房屋发布次数 10
     * - HOUSE_PUBLISH_20 → 增加房屋发布次数 20
     * - PHONEBOOK_DELAY_10 → 增加电话簿延期 10 天
     * - PHONEBOOK_DELAY_30 → 增加电话簿延期 30 天
     * - VIP_MONTH → 增加 VIP 1 个月
     *
     * @param product 商品
     * @param userId 用户ID
     */
    private void applyProductEffect(BizMallProduct product, Long userId)
    {
        String productType = product.getProductType();
        if (StringUtils.isEmpty(productType))
        {
            return;
        }
        String quotaType;
        int effectCount = product.getEffectValue() == null ? 0 : product.getEffectValue();

        if (productType.startsWith("HOUSE_PUBLISH"))
        {
            quotaType = "HOUSE_PUBLISH";
        }
        else if (productType.startsWith("PHONEBOOK_DELAY"))
        {
            quotaType = "PHONEBOOK_DELAY";
        }
        else if ("VIP_MONTH".equals(productType))
        {
            quotaType = "VIP";
            if (effectCount <= 0)
            {
                effectCount = 1;
            }
        }
        else
        {
            return;
        }

        if (effectCount <= 0)
        {
            effectCount = parseEffectCountFromType(productType);
        }
        if (effectCount <= 0)
        {
            return;
        }
        bizExchangeQuotaMapper.addQuota(userId, quotaType, effectCount);
        applyProductEffectToUser(userId, productType, effectCount);
    }

    /**
     * 将商品效果同步到用户表（发布次数、有效期）
     *
     * @param userId 用户ID
     * @param productType 商品类型
     * @param effectCount 生效数量
     */
    private void applyProductEffectToUser(Long userId, String productType, int effectCount)
    {
        BizMiniappUser user = bizMiniappUserMapper.selectBizMiniappUserByUserId(userId);
        if (user == null)
        {
            return;
        }
        BizMiniappUser update = new BizMiniappUser();
        update.setUserId(userId);
        boolean needUpdate = false;

        if (productType.startsWith("HOUSE_PUBLISH"))
        {
            int currentCount = user.getPublishCount() == null ? 0 : user.getPublishCount();
            update.setPublishCount(currentCount + effectCount);
            needUpdate = true;
        }
        else if (productType.startsWith("PHONEBOOK_DELAY"))
        {
            Date now = new Date();
            Date currentEnd = user.getPublishPeriodEnd();
            Date baseDate = (currentEnd == null || currentEnd.before(now)) ? now : currentEnd;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(baseDate);
            calendar.add(Calendar.DAY_OF_MONTH, effectCount);
            update.setPublishPeriodEnd(calendar.getTime());
            needUpdate = true;
        }

        if (needUpdate)
        {
            bizMiniappUserMapper.updateBizMiniappUser(update);
        }
    }

    /**
     * 从商品类型字符串后缀解析生效数值
     * 例如：HOUSE_PUBLISH_10 → 10，PHONEBOOK_DELAY_30 → 30
     *
     * @param productType 商品类型
     * @return 生效数值
     */
    private int parseEffectCountFromType(String productType)
    {
        if (StringUtils.isEmpty(productType))
        {
            return 0;
        }
        int lastUnderline = productType.lastIndexOf('_');
        if (lastUnderline < 0 || lastUnderline >= productType.length() - 1)
        {
            return 0;
        }
        try
        {
            return Integer.parseInt(productType.substring(lastUnderline + 1));
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }
}
