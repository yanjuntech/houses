package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.BizInviteRelation;
import com.ruoyi.system.mapper.BizInviteRelationMapper;
import com.ruoyi.system.service.IBizInviteRelationService;

/**
 * 邀请关系 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizInviteRelationServiceImpl implements IBizInviteRelationService
{
    @Autowired
    private BizInviteRelationMapper bizInviteRelationMapper;

    /**
     * 查询邀请关系集合
     *
     * @param bizInviteRelation 邀请关系
     * @return 邀请关系集合
     */
    @Override
    public List<BizInviteRelation> selectBizInviteRelationList(BizInviteRelation bizInviteRelation)
    {
        return bizInviteRelationMapper.selectBizInviteRelationList(bizInviteRelation);
    }

    /**
     * 通过关系ID查询邀请关系
     *
     * @param relationId 关系ID
     * @return 邀请关系对象信息
     */
    @Override
    public BizInviteRelation selectBizInviteRelationByRelationId(Long relationId)
    {
        return bizInviteRelationMapper.selectBizInviteRelationByRelationId(relationId);
    }

    /**
     * 新增邀请关系
     *
     * @param bizInviteRelation 邀请关系
     * @return 结果
     */
    @Override
    public int insertBizInviteRelation(BizInviteRelation bizInviteRelation)
    {
        return bizInviteRelationMapper.insertBizInviteRelation(bizInviteRelation);
    }

    /**
     * 修改邀请关系
     *
     * @param bizInviteRelation 邀请关系
     * @return 结果
     */
    @Override
    public int updateBizInviteRelation(BizInviteRelation bizInviteRelation)
    {
        return bizInviteRelationMapper.updateBizInviteRelation(bizInviteRelation);
    }

    /**
     * 删除邀请关系
     *
     * @param relationId 关系ID
     * @return 结果
     */
    @Override
    public int deleteBizInviteRelationByRelationId(Long relationId)
    {
        return bizInviteRelationMapper.deleteBizInviteRelationByRelationId(relationId);
    }

    /**
     * 批量删除邀请关系
     *
     * @param relationIds 需要删除的关系ID
     * @return 结果
     */
    @Override
    public int deleteBizInviteRelationByRelationIds(Long[] relationIds)
    {
        return bizInviteRelationMapper.deleteBizInviteRelationByRelationIds(relationIds);
    }

    /**
     * 根据被邀请人ID查询邀请关系
     *
     * @param inviteeId 被邀请人用户ID
     * @return 邀请关系对象信息
     */
    @Override
    public BizInviteRelation selectByInviteeId(Long inviteeId)
    {
        return bizInviteRelationMapper.selectByInviteeId(inviteeId);
    }

    /**
     * 查询邀请人已认证的邀请人数
     *
     * @param inviterId 邀请人用户ID
     * @return 已认证邀请人数
     */
    @Override
    public int selectInviteCountByInviter(Long inviterId)
    {
        return bizInviteRelationMapper.selectInviteCountByInviter(inviterId);
    }

    /**
     * 查询邀请人邀请的所有人
     *
     * @param inviterId 邀请人用户ID
     * @return 邀请关系集合
     */
    @Override
    public List<BizInviteRelation> selectInviteListByInviter(Long inviterId)
    {
        return bizInviteRelationMapper.selectInviteListByInviter(inviterId);
    }

    /**
     * 邀请关系绑定
     * 校验：不能自己邀请自己、被邀请人不能已被邀请
     * 插入邀请关系记录，状态为"已注册"
     *
     * @param bizInviteRelation 邀请关系（inviterId、inviteeId、inviteCode）
     * @return 结果
     */
    @Override
    public int bindInvite(BizInviteRelation bizInviteRelation)
    {
        Long inviterId = bizInviteRelation.getInviterId();
        Long inviteeId = bizInviteRelation.getInviteeId();
        // 参数校验
        if (inviterId == null || inviteeId == null)
        {
            throw new ServiceException("邀请人ID和被邀请人ID不能为空");
        }
        // 不能自己邀请自己
        if (inviterId.equals(inviteeId))
        {
            throw new ServiceException("不能自己邀请自己");
        }
        // 被邀请人不能已被邀请
        BizInviteRelation existRelation = bizInviteRelationMapper.selectByInviteeId(inviteeId);
        if (existRelation != null)
        {
            throw new ServiceException("该用户已被邀请，不能重复绑定");
        }
        // 插入邀请关系记录，状态为"已注册"
        bizInviteRelation.setInviteStatus("0");
        bizInviteRelation.setInviteTime(new Date());
        return bizInviteRelationMapper.insertBizInviteRelation(bizInviteRelation);
    }

    /**
     * 被邀请人完成实名认证时调用
     * 更新邀请状态为"已认证"，记录 certified_time
     *
     * @param inviteeId 被邀请人用户ID
     * @return 结果
     */
    @Override
    public int updateCertified(Long inviteeId)
    {
        if (inviteeId == null)
        {
            throw new ServiceException("被邀请人ID不能为空");
        }
        BizInviteRelation relation = bizInviteRelationMapper.selectByInviteeId(inviteeId);
        if (relation == null)
        {
            return 0;
        }
        BizInviteRelation update = new BizInviteRelation();
        update.setRelationId(relation.getRelationId());
        update.setInviteStatus("1");
        update.setCertifiedTime(new Date());
        return bizInviteRelationMapper.updateBizInviteRelation(update);
    }
}
