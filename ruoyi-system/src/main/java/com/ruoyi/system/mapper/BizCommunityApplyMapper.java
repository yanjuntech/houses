package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizCommunityApply;

/**
 * 小区申请信息 数据层
 *
 * @author ruoyi
 */
public interface BizCommunityApplyMapper
{
    /**
     * 查询小区申请信息集合
     *
     * @param bizCommunityApply 小区申请信息
     * @return 小区申请信息集合
     */
    public List<BizCommunityApply> selectBizCommunityApplyList(BizCommunityApply bizCommunityApply);

    /**
     * 通过申请ID查询小区申请信息
     *
     * @param applyId 申请ID
     * @return 小区申请对象信息
     */
    public BizCommunityApply selectBizCommunityApplyByApplyId(Long applyId);

    /**
     * 新增小区申请信息
     *
     * @param bizCommunityApply 小区申请信息
     * @return 结果
     */
    public int insertBizCommunityApply(BizCommunityApply bizCommunityApply);

    /**
     * 修改小区申请信息
     *
     * @param bizCommunityApply 小区申请信息
     * @return 结果
     */
    public int updateBizCommunityApply(BizCommunityApply bizCommunityApply);

    /**
     * 删除小区申请信息
     *
     * @param applyId 申请ID
     * @return 结果
     */
    public int deleteBizCommunityApplyByApplyId(Long applyId);
}
