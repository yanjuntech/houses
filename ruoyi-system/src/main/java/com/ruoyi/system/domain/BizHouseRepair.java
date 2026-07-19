package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 房屋维修 biz_house_repair
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizHouseRepair extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 维修ID */
    @Excel(name = "维修ID")
    @JsonProperty("repairId")
    private Long repairId;

    /** 房源ID */
    @Excel(name = "房源ID")
    @JsonProperty("houseId")
    private Long houseId;

    /** 房源标题（冗余） */
    @Excel(name = "房源标题")
    @JsonProperty("houseTitle")
    private String houseTitle;

    /** 租客用户ID */
    @Excel(name = "租客用户ID")
    @JsonProperty("tenantId")
    private Long tenantId;

    /** 租客姓名 */
    @Excel(name = "租客姓名")
    @JsonProperty("tenantName")
    private String tenantName;

    /** 房东用户ID */
    @Excel(name = "房东用户ID")
    @JsonProperty("landlordId")
    private Long landlordId;

    /** 房东姓名 */
    @Excel(name = "房东姓名")
    @JsonProperty("landlordName")
    private String landlordName;

    /** 维修类型（1房东维修 2租客自修报销） */
    @Excel(name = "维修类型", readConverterExp = "1=房东维修,2=租客自修报销")
    @JsonProperty("repairType")
    private String repairType;

    /** 问题描述 */
    @Excel(name = "问题描述")
    @JsonProperty("description")
    private String description;

    /** 问题图片URL列表 */
    @Excel(name = "问题图片")
    @JsonProperty("images")
    private String images;

    /** 状态（0待房东确认 1维修中 2待租客确认 3已完成 4待租客上传凭证 5待房东确认报销 6已报销 7已取消） */
    @Excel(name = "状态", readConverterExp = "0=待房东确认,1=维修中,2=待租客确认,3=已完成,4=待租客上传凭证,5=待房东确认报销,6=已报销,7=已取消")
    @JsonProperty("status")
    private String status;

    /** 报销凭证图片URL列表 */
    @Excel(name = "报销凭证")
    @JsonProperty("receiptImages")
    private String receiptImages;

    /** 报销金额 */
    @Excel(name = "报销金额")
    @JsonProperty("receiptAmount")
    private BigDecimal receiptAmount;

    /** 房东确认时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "房东确认时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("confirmTime")
    private Date confirmTime;

    /** 维修完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "维修完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("completeTime")
    private Date completeTime;

    /** 租客确认时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "租客确认时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("tenantConfirmTime")
    private Date tenantConfirmTime;

    /** 报销确认时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "报销确认时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("reimburseTime")
    private Date reimburseTime;
}
