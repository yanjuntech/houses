package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizMiniappUser;

/**
 * 小程序用户 服务层
 *
 * @author ruoyi
 */
public interface IBizMiniappUserService
{
    /**
     * 查询小程序用户列表
     *
     * @param bizMiniappUser 小程序用户信息
     * @return 小程序用户集合
     */
    public List<BizMiniappUser> selectBizMiniappUserList(BizMiniappUser bizMiniappUser);

    /**
     * 通过用户ID查询小程序用户信息
     *
     * @param userId 用户ID
     * @return 小程序用户对象信息
     */
    public BizMiniappUser selectBizMiniappUserByUserId(Long userId);

    /**
     * 新增小程序用户
     *
     * @param bizMiniappUser 小程序用户信息
     * @return 结果
     */
    public int insertBizMiniappUser(BizMiniappUser bizMiniappUser);

    /**
     * 修改小程序用户
     *
     * @param bizMiniappUser 小程序用户信息
     * @return 结果
     */
    public int updateBizMiniappUser(BizMiniappUser bizMiniappUser);

    /**
     * 删除小程序用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteBizMiniappUserByUserId(Long userId);

    /**
     * 批量删除小程序用户
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    public int deleteBizMiniappUserByUserIds(Long[] userIds);

    /**
     * 根据手机号查询小程序用户
     *
     * @param phone 手机号
     * @return 小程序用户对象信息
     */
    public BizMiniappUser selectBizMiniappUserByPhone(String phone);

    /**
     * 根据openid查询小程序用户
     *
     * @param openid 微信openid
     * @return 小程序用户对象信息
     */
    public BizMiniappUser selectBizMiniappUserByOpenid(String openid);

    /**
     * 更新用户类型（身份标签）
     *
     * @param bizMiniappUser 小程序用户信息
     * @return 结果
     */
    public int updateUserType(BizMiniappUser bizMiniappUser);

    /**
     * 更新认证状态
     *
     * @param bizMiniappUser 小程序用户信息
     * @return 结果
     */
    public int updateVerifyStatus(BizMiniappUser bizMiniappUser);

    /**
     * 查询黑名单用户列表（status='2'）
     *
     * @param bizMiniappUser 小程序用户查询条件
     * @return 黑名单用户集合
     */
    public List<BizMiniappUser> selectBlacklist(BizMiniappUser bizMiniappUser);

    /**
     * 加入黑名单（更新 status、blacklist_reason、blacklist_time）
     *
     * @param bizMiniappUser 小程序用户信息
     * @return 结果
     */
    public int addToBlacklist(BizMiniappUser bizMiniappUser);

    /**
     * 解除黑名单（恢复 status='0'，清空 blacklist_reason、blacklist_time）
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int removeFromBlacklist(Long userId);

    /**
     * 调整发布次数
     *
     * @param bizMiniappUser 小程序用户信息（包含 userId 和 publishCount）
     * @return 结果
     */
    public int updatePublishCount(BizMiniappUser bizMiniappUser);

    /**
     * 延长发布周期
     *
     * @param bizMiniappUser 小程序用户信息（包含 userId 和 publishPeriodEnd）
     * @return 结果
     */
    public int extendPublishPeriod(BizMiniappUser bizMiniappUser);

    /**
     * 调整发布次数（正数增加，负数减少）
     *
     * @param userId 用户ID
     * @param count 调整数量（正数增加，负数减少）
     * @param reason 调整原因
     * @return 结果
     */
    public int adjustPublishCount(Long userId, Integer count, String reason);

    /**
     * 延长发布周期（支持叠加）
     *
     * @param userId 用户ID
     * @param days 延长天数
     * @return 结果
     */
    public int extendPublishPeriod(Long userId, Integer days);
}
