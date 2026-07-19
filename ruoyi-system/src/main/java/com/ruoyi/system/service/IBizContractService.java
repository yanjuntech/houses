package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizContract;
import com.ruoyi.system.domain.BizContractSignature;

/**
 * 电子合同 服务层
 *
 * @author ruoyi
 */
public interface IBizContractService
{
    /**
     * 查询电子合同列表
     *
     * @param bizContract 电子合同
     * @return 电子合同集合
     */
    public List<BizContract> selectBizContractList(BizContract bizContract);

    /**
     * 通过合同ID查询电子合同详情（关联返回签名列表）
     *
     * @param contractId 合同ID
     * @return 电子合同
     */
    public BizContract selectBizContractByContractId(Long contractId);

    /**
     * 发起合同（生成合同编号）
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

    /**
     * 上传合同签名
     * 如果出租方和承租方都已签名，则将合同状态改为已签署
     *
     * @param signature 合同签名
     * @return 结果
     */
    public int uploadSignature(BizContractSignature signature);

    /**
     * 完成签署
     * 将合同状态改为已签署，记录签署时间
     *
     * @param bizContract 电子合同
     * @return 结果
     */
    public int completeContract(BizContract bizContract);
}
