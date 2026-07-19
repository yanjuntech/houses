package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizContract;

/**
 * 电子合同 数据层
 *
 * @author ruoyi
 */
public interface BizContractMapper
{
    /**
     * 查询电子合同列表
     *
     * @param bizContract 电子合同
     * @return 电子合同集合
     */
    public List<BizContract> selectBizContractList(BizContract bizContract);

    /**
     * 通过合同ID查询电子合同
     *
     * @param contractId 合同ID
     * @return 电子合同
     */
    public BizContract selectBizContractByContractId(Long contractId);

    /**
     * 新增电子合同
     *
     * @param bizContract 电子合同
     * @return 结果
     */
    public int insertBizContract(BizContract bizContract);

    /**
     * 修改电子合同
     *
     * @param bizContract 电子合同
     * @return 结果
     */
    public int updateBizContract(BizContract bizContract);

    /**
     * 删除电子合同
     *
     * @param contractId 合同ID
     * @return 结果
     */
    public int deleteBizContractByContractId(Long contractId);

    /**
     * 批量删除电子合同
     *
     * @param contractIds 需要删除的合同ID数组
     * @return 结果
     */
    public int deleteBizContractByContractIds(Long[] contractIds);
}
