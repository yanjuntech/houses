package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizInviteRelation;

/**
 * 邀请关系 服务层
 *
 * @author ruoyi
 */
public interface IBizInviteRelationService
{
    /**
     * 查询邀请关系集合
     *
     * @param bizInviteRelation 邀请关系
     * @return 邀请关系集合
     */
    public List<BizInviteRelation> selectBizInviteRelationList(BizInviteRelation bizInviteRelation);

    /**
     * 通过关系ID查询邀请关系
     *
     * @param relationId 关系ID
     * @return 邀请关系对象信息
     */
    public BizInviteRelation selectBizInviteRelationByRelationId(Long relationId);

    /**
     * 新增邀请关系
     *
     * @param bizInviteRelation 邀请关系
     * @return 结果
     */
    public int insertBizInviteRelation(BizInviteRelation bizInviteRelation);

    /**
     * 修改邀请关系
     *
     * @param bizInviteRelation 邀请关系
     * @return 结果
     */
    public int updateBizInviteRelation(BizInviteRelation bizInviteRelation);

    /**
     * 删除邀请关系
     *
     * @param relationId 关系ID
     * @return 结果
     */
    public int deleteBizInviteRelationByRelationId(Long relationId);

    /**
     * 批量删除邀请关系
     *
     * @param relationIds 需要删除的关系ID
     * @return 结果
     */
    public int deleteBizInviteRelationByRelationIds(Long[] relationIds);

    /**
     * 根据被邀请人ID查询邀请关系
     *
     * @param inviteeId 被邀请人用户ID
     * @return 邀请关系对象信息
     */
    public BizInviteRelation selectByInviteeId(Long inviteeId);

    /**
     * 查询邀请人已认证的邀请人数
     *
     * @param inviterId 邀请人用户ID
     * @return 已认证邀请人数
     */
    public int selectInviteCountByInviter(Long inviterId);

    /**
     * 查询邀请人邀请的所有人
     *
     * @param inviterId 邀请人用户ID
     * @return 邀请关系集合
     */
    public List<BizInviteRelation> selectInviteListByInviter(Long inviterId);

    /**
     * 邀请关系绑定
     * 校验：不能自己邀请自己、被邀请人不能已被邀请
     * 插入邀请关系记录，状态为"已注册"
     *
     * @param bizInviteRelation 邀请关系（inviterId、inviteeId、inviteCode）
     * @return 结果
     */
    public int bindInvite(BizInviteRelation bizInviteRelation);

    /**
     * 被邀请人完成实名认证时调用
     * 更新邀请状态为"已认证"，记录 certified_time
     *
     * @param inviteeId 被邀请人用户ID
     * @return 结果
     */
    public int updateCertified(Long inviteeId);
}
