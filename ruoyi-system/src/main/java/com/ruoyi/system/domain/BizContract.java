package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 电子合同 biz_contract
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizContract extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 合同ID */
    @Excel(name = "合同ID")
    @JsonProperty("contractId")
    private Long contractId;

    /** 合同编号 */
    @Excel(name = "合同编号")
    @JsonProperty("contractNo")
    private String contractNo;

    /** 合同标题 */
    @Excel(name = "合同标题")
    @JsonProperty("contractTitle")
    private String contractTitle;

    /** 房源ID */
    @Excel(name = "房源ID")
    @JsonProperty("houseId")
    private Long houseId;

    /** 房源标题（冗余） */
    @Excel(name = "房源标题")
    @JsonProperty("houseTitle")
    private String houseTitle;

    /** 小区名称（冗余） */
    @Excel(name = "小区名称")
    @JsonProperty("communityName")
    private String communityName;

    /** 出租方ID */
    @Excel(name = "出租方ID")
    @JsonProperty("landlordId")
    private Long landlordId;

    /** 出租方姓名 */
    @Excel(name = "出租方姓名")
    @JsonProperty("landlordName")
    private String landlordName;

    /** 出租方手机号 */
    @Excel(name = "出租方手机号")
    @JsonProperty("landlordPhone")
    private String landlordPhone;

    /** 承租方ID */
    @Excel(name = "承租方ID")
    @JsonProperty("tenantId")
    private Long tenantId;

    /** 承租方姓名 */
    @Excel(name = "承租方姓名")
    @JsonProperty("tenantName")
    private String tenantName;

    /** 承租方手机号 */
    @Excel(name = "承租方手机号")
    @JsonProperty("tenantPhone")
    private String tenantPhone;

    /** 月租金 */
    @Excel(name = "月租金")
    @JsonProperty("monthlyRent")
    private BigDecimal monthlyRent;

    /** 押金 */
    @Excel(name = "押金")
    @JsonProperty("deposit")
    private BigDecimal deposit;

    /** 租期（月） */
    @Excel(name = "租期")
    @JsonProperty("rentPeriod")
    private Integer rentPeriod;

    /** 起租日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "起租日期", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonProperty("startDate")
    private Date startDate;

    /** 到期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "到期日期", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonProperty("endDate")
    private Date endDate;

    /** 支付周期 */
    @Excel(name = "支付周期")
    @JsonProperty("payCycle")
    private String payCycle;

    /** 合同条款 */
    @JsonProperty("contractContent")
    private String contractContent;

    /** 状态（0待签署 1已签署 2已取消 3已过期） */
    @Excel(name = "状态", readConverterExp = "0=待签署,1=已签署,2=已取消,3=已过期")
    @JsonProperty("status")
    private String status;

    /** 签署时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "签署时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("signTime")
    private Date signTime;

    /** PDF文件路径 */
    @JsonProperty("pdfPath")
    private String pdfPath;

    /** 合同签名列表（关联查询） */
    @JsonProperty("signatureList")
    private List<BizContractSignature> signatureList;
}
