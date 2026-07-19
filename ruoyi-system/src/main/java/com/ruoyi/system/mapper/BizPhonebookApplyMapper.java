package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizPhonebookApply;

/**
 * 电话簿申请信息 数据层
 *
 * @author ruoyi
 */
public interface BizPhonebookApplyMapper
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
}
