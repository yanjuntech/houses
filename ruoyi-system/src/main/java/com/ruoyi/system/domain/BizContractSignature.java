package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合同签名 biz_contract_signature
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizContractSignature extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 签名ID */
    @Excel(name = "签名ID")
    @JsonProperty("signatureId")
    private Long signatureId;

    /** 合同ID */
    @Excel(name = "合同ID")
    @JsonProperty("contractId")
    private Long contractId;

    /** 签署人ID */
    @Excel(name = "签署人ID")
    @JsonProperty("signerId")
    private Long signerId;

    /** 签署人姓名 */
    @Excel(name = "签署人姓名")
    @JsonProperty("signerName")
    private String signerName;

    /** 签署人角色（1出租方 2承租方） */
    @Excel(name = "签署人角色", readConverterExp = "1=出租方,2=承租方")
    @JsonProperty("signerRole")
    private String signerRole;

    /** 签名图片（base64或URL） */
    @JsonProperty("signatureImage")
    private String signatureImage;

    /** 签署时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "签署时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("signTime")
    private Date signTime;
}
