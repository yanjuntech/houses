package com.ruoyi.system.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizMiniappUser;
import com.ruoyi.system.mapper.BizMiniappUserMapper;
import com.ruoyi.system.service.IBizMiniappUserService;

/**
 * 小程序用户 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizMiniappUserServiceImpl implements IBizMiniappUserService
{
    @Autowired
    private BizMiniappUserMapper bizMiniappUserMapper;

    /**
     * 查询小程序用户列表
     *
     * @param bizMiniappUser 小程序用户信息
     * @return 小程序用户集合
     */
    @Override
    public List<BizMiniappUser> selectBizMiniappUserList(BizMiniappUser bizMiniappUser)
    {
        return bizMiniappUserMapper.selectBizMiniappUserList(bizMiniappUser);
    }

    /**
     * 通过用户ID查询小程序用户信息
     *
     * @param userId 用户ID
     * @return 小程序用户对象信息
     */
    @Override
    public BizMiniappUser selectBizMiniappUserByUserId(Long userId)
    {
        return bizMiniappUserMapper.selectBizMiniappUserByUserId(userId);
    }

    /**
     * 新增小程序用户
     *
     * @param bizMiniappUser 小程序用户信息
     * @return 结果
     */
    @Override
    public int insertBizMiniappUser(BizMiniappUser bizMiniappUser)
    {
        return bizMiniappUserMapper.insertBizMiniappUser(bizMiniappUser);
    }

    /**
     * 修改小程序用户
     *
     * @param bizMiniappUser 小程序用户信息
     * @return 结果
     */
    @Override
    public int updateBizMiniappUser(BizMiniappUser bizMiniappUser)
    {
        return bizMiniappUserMapper.updateBizMiniappUser(bizMiniappUser);
    }

    /**
     * 删除小程序用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteBizMiniappUserByUserId(Long userId)
    {
        return bizMiniappUserMapper.deleteBizMiniappUserByUserId(userId);
    }

    /**
     * 批量删除小程序用户
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public int deleteBizMiniappUserByUserIds(Long[] userIds)
    {
        return bizMiniappUserMapper.deleteBizMiniappUserByUserIds(userIds);
    }

    /**
     * 根据手机号查询小程序用户
     *
     * @param phone 手机号
     * @return 小程序用户对象信息
     */
    @Override
    public BizMiniappUser selectBizMiniappUserByPhone(String phone)
    {
        return bizMiniappUserMapper.selectBizMiniappUserByPhone(phone);
    }

    /**
     * 根据openid查询小程序用户
     *
     * @param openid 微信openid
     * @return 小程序用户对象信息
     */
    @Override
    public BizMiniappUser selectBizMiniappUserByOpenid(String openid)
    {
        return bizMiniappUserMapper.selectBizMiniappUserByOpenid(openid);
    }

    /**
     * 更新用户类型（身份标签）
     *
     * @param bizMiniappUser 小程序用户信息
     * @return 结果
     */
    @Override
    public int updateUserType(BizMiniappUser bizMiniappUser)
    {
        return bizMiniappUserMapper.updateUserType(bizMiniappUser);
    }

    /**
     * 更新认证状态
     *
     * @param bizMiniappUser 小程序用户信息
     * @return 结果
     */
    @Override
    public int updateVerifyStatus(BizMiniappUser bizMiniappUser)
    {
        return bizMiniappUserMapper.updateVerifyStatus(bizMiniappUser);
    }

    /**
     * 查询黑名单用户列表（status='2'）
     *
     * @param bizMiniappUser 小程序用户查询条件
     * @return 黑名单用户集合
     */
    @Override
    public List<BizMiniappUser> selectBlacklist(BizMiniappUser bizMiniappUser)
    {
        return bizMiniappUserMapper.selectBlacklist(bizMiniappUser);
    }

    /**
     * 加入黑名单（更新 status、blacklist_reason、blacklist_time）
     *
     * @param bizMiniappUser 小程序用户信息
     * @return 结果
     */
    @Override
    public int addToBlacklist(BizMiniappUser bizMiniappUser)
    {
        return bizMiniappUserMapper.addToBlacklist(bizMiniappUser);
    }

    /**
     * 解除黑名单（恢复 status='0'，清空 blacklist_reason、blacklist_time）
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int removeFromBlacklist(Long userId)
    {
        return bizMiniappUserMapper.removeFromBlacklist(userId);
    }

    /**
     * 调整发布次数
     *
     * @param bizMiniappUser 小程序用户信息（包含 userId 和 publishCount）
     * @return 结果
     */
    @Override
    public int updatePublishCount(BizMiniappUser bizMiniappUser)
    {
        return bizMiniappUserMapper.updateBizMiniappUser(bizMiniappUser);
    }

    /**
     * 延长发布周期
     *
     * @param bizMiniappUser 小程序用户信息（包含 userId 和 publishPeriodEnd）
     * @return 结果
     */
    @Override
    public int extendPublishPeriod(BizMiniappUser bizMiniappUser)
    {
        return bizMiniappUserMapper.updateBizMiniappUser(bizMiniappUser);
    }

    /**
     * 调整发布次数（正数增加，负数减少）
     *
     * @param userId 用户ID
     * @param count 调整数量（正数增加，负数减少）
     * @param reason 调整原因
     * @return 结果
     */
    @Override
    public int adjustPublishCount(Long userId, Integer count, String reason)
    {
        if (userId == null)
        {
            throw new ServiceException("用户ID不能为空");
        }
        if (count == null)
        {
            throw new ServiceException("调整数量不能为空");
        }
        BizMiniappUser user = bizMiniappUserMapper.selectBizMiniappUserByUserId(userId);
        if (user == null)
        {
            throw new ServiceException("用户不存在");
        }
        int currentCount = user.getPublishCount() == null ? 0 : user.getPublishCount();
        int newCount = currentCount + count;
        if (newCount < 0)
        {
            throw new ServiceException("调整后发布次数不能为负数");
        }
        BizMiniappUser update = new BizMiniappUser();
        update.setUserId(userId);
        update.setPublishCount(newCount);
        return bizMiniappUserMapper.updateBizMiniappUser(update);
    }

    /**
     * 延长发布周期（支持叠加）
     *
     * @param userId 用户ID
     * @param days 延长天数
     * @return 结果
     */
    @Override
    public int extendPublishPeriod(Long userId, Integer days)
    {
        if (userId == null)
        {
            throw new ServiceException("用户ID不能为空");
        }
        if (days == null || days <= 0)
        {
            throw new ServiceException("延长天数必须大于0");
        }
        BizMiniappUser user = bizMiniappUserMapper.selectBizMiniappUserByUserId(userId);
        if (user == null)
        {
            throw new ServiceException("用户不存在");
        }
        Date now = new Date();
        Date currentEnd = user.getPublishPeriodEnd();
        Date baseDate = (currentEnd == null || currentEnd.before(now)) ? now : currentEnd;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(baseDate);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date newEnd = calendar.getTime();
        BizMiniappUser update = new BizMiniappUser();
        update.setUserId(userId);
        update.setPublishPeriodEnd(newEnd);
        return bizMiniappUserMapper.updateBizMiniappUser(update);
    }
}
