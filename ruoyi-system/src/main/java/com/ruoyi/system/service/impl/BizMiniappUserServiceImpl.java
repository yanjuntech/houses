package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}
