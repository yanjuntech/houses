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
 * 小区申请表 biz_community_apply
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizCommunityApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 申请ID */
    @Excel(name = "申请ID", cellType = ColumnType.NUMERIC)
    @JsonProperty("applyId")
    private Long applyId;

    /** 小区名称 */
    @Excel(name = "小区名称")
    @JsonProperty("communityName")
    private String communityName;

    /** 省份 */
    @Excel(name = "省份")
    @JsonProperty("province")
    private String province;

    /** 城市 */
    @Excel(name = "城市")
    @JsonProperty("city")
    private String city;

    /** 区/县 */
    @Excel(name = "区/县")
    @JsonProperty("district")
    private String district;

    /** 详细地址 */
    @Excel(name = "详细地址")
    @JsonProperty("address")
    private String address;

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
