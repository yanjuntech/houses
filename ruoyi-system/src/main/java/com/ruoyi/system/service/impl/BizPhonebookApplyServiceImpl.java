package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizPhonebook;
import com.ruoyi.system.domain.BizPhonebookApply;
import com.ruoyi.system.mapper.BizPhonebookApplyMapper;
import com.ruoyi.system.mapper.BizPhonebookMapper;
import com.ruoyi.system.service.IBizPhonebookApplyService;

/**
 * 电话簿申请信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizPhonebookApplyServiceImpl implements IBizPhonebookApplyService
{
    @Autowired
    private BizPhonebookApplyMapper bizPhonebookApplyMapper;

    @Autowired
    private BizPhonebookMapper bizPhonebookMapper;

    /**
     * 查询电话簿申请信息集合
     *
     * @param bizPhonebookApply 电话簿申请信息
     * @return 电话簿申请信息集合
     */
    @Override
    public List<BizPhonebookApply> selectBizPhonebookApplyList(BizPhonebookApply bizPhonebookApply)
    {
        return bizPhonebookApplyMapper.selectBizPhonebookApplyList(bizPhonebookApply);
    }

    /**
     * 通过申请ID查询电话簿申请信息
     *
     * @param applyId 申请ID
     * @return 电话簿申请对象信息
     */
    @Override
    public BizPhonebookApply selectBizPhonebookApplyByApplyId(Long applyId)
    {
        return bizPhonebookApplyMapper.selectBizPhonebookApplyByApplyId(applyId);
    }

    /**
     * 新增电话簿申请信息
     *
     * @param bizPhonebookApply 电话簿申请信息
     * @return 结果
     */
    @Override
    public int insertBizPhonebookApply(BizPhonebookApply bizPhonebookApply)
    {
        // 默认申请状态为待审批
        if (StringUtils.isEmpty(bizPhonebookApply.getApplyStatus()))
        {
            bizPhonebookApply.setApplyStatus("0");
        }
        return bizPhonebookApplyMapper.insertBizPhonebookApply(bizPhonebookApply);
    }

    /**
     * 修改电话簿申请信息
     *
     * @param bizPhonebookApply 电话簿申请信息
     * @return 结果
     */
    @Override
    public int updateBizPhonebookApply(BizPhonebookApply bizPhonebookApply)
    {
        return bizPhonebookApplyMapper.updateBizPhonebookApply(bizPhonebookApply);
    }

    /**
     * 删除电话簿申请信息
     *
     * @param applyId 申请ID
     * @return 结果
     */
    @Override
    public int deleteBizPhonebookApplyByApplyId(Long applyId)
    {
        return bizPhonebookApplyMapper.deleteBizPhonebookApplyByApplyId(applyId);
    }

    /**
     * 审批通过：更新申请记录状态为已通过，同时将申请的商家信息插入到电话簿表
     *
     * @param bizPhonebookApply 电话簿申请信息（包含审批人和审批备注）
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approve(BizPhonebookApply bizPhonebookApply)
    {
        // 查询申请记录详情
        BizPhonebookApply apply = bizPhonebookApplyMapper.selectBizPhonebookApplyByApplyId(bizPhonebookApply.getApplyId());
        if (apply == null)
        {
            return 0;
        }
        // 更新申请记录状态为已通过
        apply.setApplyStatus("1");
        apply.setApproveBy(bizPhonebookApply.getApproveBy());
        apply.setApproveTime(new Date());
        apply.setApproveRemark(bizPhonebookApply.getApproveRemark());
        int rows = bizPhonebookApplyMapper.updateBizPhonebookApply(apply);

        // 将申请的商家信息插入到电话簿表
        if (rows > 0)
        {
            BizPhonebook phonebook = new BizPhonebook();
            phonebook.setMerchantName(apply.getMerchantName());
            phonebook.setOwnerName(apply.getOwnerName());
            phonebook.setPhone(apply.getPhone());
            phonebook.setCategory(apply.getCategory());
            phonebook.setAddress(apply.getAddress());
            phonebook.setBusinessLicense(apply.getBusinessLicense());
            phonebook.setStatus("0");
            phonebook.setCreateBy(apply.getApproveBy());
            bizPhonebookMapper.insertBizPhonebook(phonebook);
        }
        return rows;
    }

    /**
     * 审批驳回：更新申请记录状态为已驳回（需要填写驳回原因）
     *
     * @param bizPhonebookApply 电话簿申请信息（包含审批人和驳回原因）
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reject(BizPhonebookApply bizPhonebookApply)
    {
        // 校验驳回原因
        if (StringUtils.isEmpty(bizPhonebookApply.getApproveRemark()))
        {
            return 0;
        }
        // 查询申请记录详情
        BizPhonebookApply apply = bizPhonebookApplyMapper.selectBizPhonebookApplyByApplyId(bizPhonebookApply.getApplyId());
        if (apply == null)
        {
            return 0;
        }
        // 更新申请记录状态为已驳回
        apply.setApplyStatus("2");
        apply.setApproveBy(bizPhonebookApply.getApproveBy());
        apply.setApproveTime(new Date());
        apply.setApproveRemark(bizPhonebookApply.getApproveRemark());
        return bizPhonebookApplyMapper.updateBizPhonebookApply(apply);
    }
}
