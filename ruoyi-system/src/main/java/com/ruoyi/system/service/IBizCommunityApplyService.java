package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizCommunityApply;

/**
 * 小区申请信息 服务层
 *
 * @author ruoyi
 */
public interface IBizCommunityApplyService
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

    /**
     * 审批通过：更新申请记录状态为已通过，同时将申请的小区信息插入到小区表
     *
     * @param bizCommunityApply 小区申请信息（包含审批人和审批备注）
     * @return 结果
     */
    public int approve(BizCommunityApply bizCommunityApply);

    /**
     * 审批驳回：更新申请记录状态为已驳回（需要填写驳回原因）
     *
     * @param bizCommunityApply 小区申请信息（包含审批人和驳回原因）
     * @return 结果
     */
    public int reject(BizCommunityApply bizCommunityApply);

    /**
     * 校验小区登记开关是否开启
     *
     * @param province 省份
     * @param city 城市
     * @param district 区县
     */
    public void checkCommunityRegisterSwitch(String province, String city, String district);
}
