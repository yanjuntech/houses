package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 电话簿申请表 biz_phonebook_apply
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizPhonebookApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 申请ID */
    @Excel(name = "申请ID", cellType = ColumnType.NUMERIC)
    @JsonProperty("applyId")
    private Long applyId;

    /** 商家名称 */
    @Excel(name = "商家名称")
    @JsonProperty("merchantName")
    private String merchantName;

    /** 负责人姓名 */
    @Excel(name = "负责人姓名")
    @JsonProperty("ownerName")
    private String ownerName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    @JsonProperty("phone")
    private String phone;

    /** 分类 */
    @Excel(name = "分类")
    @JsonProperty("category")
    private String category;

    /** 地址 */
    @Excel(name = "地址")
    @JsonProperty("address")
    private String address;

    /** 营业执照 */
    @Excel(name = "营业执照")
    @JsonProperty("businessLicense")
    private String businessLicense;

    /** 申请人ID */
    @Excel(name = "申请人ID", cellType = ColumnType.NUMERIC)
    @JsonProperty("applicantId")
    private Long applicantId;

    /** 申请人姓名 */
    @Excel(name = "申请人姓名")
    @JsonProperty("applicantName")
    private String applicantName;

    /** 申请人电话 */
    @Excel(name = "申请人电话")
    @JsonProperty("applicantPhone")
    private String applicantPhone;

    /** 申请状态（0待审批 1已通过 2已驳回） */
    @Excel(name = "申请状态", readConverterExp = "0=待审批,1=已通过,2=已驳回")
    @JsonProperty("applyStatus")
    private String applyStatus;

    /** 审批人 */
    @Excel(name = "审批人")
    @JsonProperty("approveBy")
    private String approveBy;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审批时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("approveTime")
    private Date approveTime;

    /** 审批备注 */
    @Excel(name = "审批备注")
    @JsonProperty("approveRemark")
    private String approveRemark;
}
