package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizPhonebookApply;

/**
 * 电话簿申请信息 服务层
 *
 * @author ruoyi
 */
public interface IBizPhonebookApplyService
{
    /**
     * 查询电话簿申请信息集合
     *
     * @param bizPhonebookApply 电话簿申请信息
     * @return 电话簿申请信息集合
     */
    public List<BizPhonebookApply> selectBizPhonebookApplyList(BizPhonebookApply bizPhonebookApply);

    /**
     * 通过申请ID查询电话簿申请信息
     *
     * @param applyId 申请ID
     * @return 电话簿申请对象信息
     */
    public BizPhonebookApply selectBizPhonebookApplyByApplyId(Long applyId);

    /**
     * 新增电话簿申请信息
     *
     * @param bizPhonebookApply 电话簿申请信息
     * @return 结果
     */
    public int insertBizPhonebookApply(BizPhonebookApply bizPhonebookApply);

    /**
     * 修改电话簿申请信息
     *
     * @param bizPhonebookApply 电话簿申请信息
     * @return 结果
     */
    public int updateBizPhonebookApply(BizPhonebookApply bizPhonebookApply);

    /**
     * 删除电话簿申请信息
     *
     * @param applyId 申请ID
     * @return 结果
     */
    public int deleteBizPhonebookApplyByApplyId(Long applyId);

    /**
     * 审批通过：更新申请记录状态为已通过，同时将申请的商家信息插入到电话簿表
     *
     * @param bizPhonebookApply 电话簿申请信息（包含审批人和审批备注）
     * @return 结果
     */
    public int approve(BizPhonebookApply bizPhonebookApply);

    /**
     * 审批驳回：更新申请记录状态为已驳回（需要填写驳回原因）
     *
     * @param bizPhonebookApply 电话簿申请信息（包含审批人和驳回原因）
     * @return 结果
     */
    public int reject(BizPhonebookApply bizPhonebookApply);
}
