package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizContract;
import com.ruoyi.system.domain.BizContractSignature;
import com.ruoyi.system.mapper.BizContractMapper;
import com.ruoyi.system.mapper.BizContractSignatureMapper;
import com.ruoyi.system.service.IBizContractService;

/**
 * 电子合同 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizContractServiceImpl implements IBizContractService
{
    @Autowired
    private BizContractMapper bizContractMapper;

    @Autowired
    private BizContractSignatureMapper bizContractSignatureMapper;

    /**
     * 查询电子合同列表
     *
     * @param bizContract 电子合同
     * @return 电子合同集合
     */
    @Override
    public List<BizContract> selectBizContractList(BizContract bizContract)
    {
        return bizContractMapper.selectBizContractList(bizContract);
    }

    /**
     * 通过合同ID查询电子合同详情
     * 同时关联查询签名列表
     *
     * @param contractId 合同ID
     * @return 电子合同
     */
    @Override
    public BizContract selectBizContractByContractId(Long contractId)
    {
        BizContract contract = bizContractMapper.selectBizContractByContractId(contractId);
        if (StringUtils.isNotNull(contract))
        {
            // 关联查询签名列表
            List<BizContractSignature> signatureList = bizContractSignatureMapper.selectSignatureListByContractId(contractId);
            contract.setSignatureList(signatureList);
        }
        return contract;
    }

    /**
     * 发起合同
     * 自动生成合同编号：HT + yyyyMMddHHmmss + 4位随机数
     * 默认状态为待签署
     *
     * @param bizContract 电子合同
     * @return 结果
     */
    @Override
    public int insertBizContract(BizContract bizContract)
    {
        // 生成合同编号
        bizContract.setContractNo(generateContractNo());
        // 默认状态为待签署
        if (StringUtils.isEmpty(bizContract.getStatus()))
        {
            bizContract.setStatus("0");
        }
        bizContract.setCreateTime(DateUtils.getNowDate());
        return bizContractMapper.insertBizContract(bizContract);
    }

    /**
     * 修改电子合同
     *
     * @param bizContract 电子合同
     * @return 结果
     */
    @Override
    public int updateBizContract(BizContract bizContract)
    {
        bizContract.setUpdateTime(DateUtils.getNowDate());
        return bizContractMapper.updateBizContract(bizContract);
    }

    /**
     * 删除电子合同
     *
     * @param contractId 合同ID
     * @return 结果
     */
    @Override
    public int deleteBizContractByContractId(Long contractId)
    {
        return bizContractMapper.deleteBizContractByContractId(contractId);
    }

    /**
     * 批量删除电子合同
     *
     * @param contractIds 需要删除的合同ID数组
     * @return 结果
     */
    @Override
    public int deleteBizContractByContractIds(Long[] contractIds)
    {
        return bizContractMapper.deleteBizContractByContractIds(contractIds);
    }

    /**
     * 上传合同签名
     * 保存签名记录，若出租方和承租方都已签名，则自动将合同状态改为已签署
     *
     * @param signature 合同签名
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int uploadSignature(BizContractSignature signature)
    {
        signature.setSignTime(DateUtils.getNowDate());
        signature.setCreateTime(DateUtils.getNowDate());
        int rows = bizContractSignatureMapper.insertSignature(signature);
        if (rows > 0)
        {
            // 检查出租方和承租方是否都已签名
            List<BizContractSignature> signatures = bizContractSignatureMapper.selectSignatureListByContractId(signature.getContractId());
            boolean hasLandlord = false;
            boolean hasTenant = false;
            for (BizContractSignature item : signatures)
            {
                if ("1".equals(item.getSignerRole()))
                {
                    hasLandlord = true;
                }
                else if ("2".equals(item.getSignerRole()))
                {
                    hasTenant = true;
                }
            }
            // 双方都已签名，更新合同状态为已签署
            if (hasLandlord && hasTenant)
            {
                BizContract updateContract = new BizContract();
                updateContract.setContractId(signature.getContractId());
                updateContract.setStatus("1");
                updateContract.setSignTime(DateUtils.getNowDate());
                updateContract.setUpdateTime(DateUtils.getNowDate());
                bizContractMapper.updateBizContract(updateContract);
            }
        }
        return rows;
    }

    /**
     * 完成签署
     * 将合同状态改为已签署，记录签署时间
     *
     * @param bizContract 电子合同
     * @return 结果
     */
    @Override
    public int completeContract(BizContract bizContract)
    {
        bizContract.setStatus("1");
        bizContract.setSignTime(DateUtils.getNowDate());
        bizContract.setUpdateTime(DateUtils.getNowDate());
        return bizContractMapper.updateBizContract(bizContract);
    }

    /**
     * 生成合同编号
     * 格式：HT + yyyyMMddHHmmss + 4位随机数
     *
     * @return 合同编号
     */
    private String generateContractNo()
    {
        String dateStr = DateUtils.dateTimeNow("yyyyMMddHHmmss");
        int random = ThreadLocalRandom.current().nextInt(1000, 10000);
        return "HT" + dateStr + random;
    }
}
