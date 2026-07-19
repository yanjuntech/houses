package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizInviteRelation;

/**
 * 邀请关系 数据层
 *
 * @author ruoyi
 */
public interface BizInviteRelationMapper
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
}
