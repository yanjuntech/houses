package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizContractSignature;

/**
 * 合同签名 数据层
 *
 * @author ruoyi
 */
public interface BizContractSignatureMapper
{
    /**
     * 根据合同ID查询签名列表
     *
     * @param contractId 合同ID
     * @return 签名集合
     */
    public List<BizContractSignature> selectSignatureListByContractId(Long contractId);

    /**
     * 根据签名ID查询签名信息
     *
     * @param signatureId 签名ID
     * @return 签名信息
     */
    public BizContractSignature selectSignatureById(Long signatureId);

    /**
     * 新增合同签名
     *
     * @param signature 合同签名
     * @return 结果
     */
    public int insertSignature(BizContractSignature signature);
}
