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
 * 在租房屋合同 biz_rental_contract
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizRentalContract extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 在租ID */
    @Excel(name = "在租ID")
    @JsonProperty("rentalId")
    private Long rentalId;

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

    /** 关联合同ID */
    @Excel(name = "关联合同ID")
    @JsonProperty("contractId")
    private Long contractId;

    /** 合同编号（冗余） */
    @Excel(name = "合同编号")
    @JsonProperty("contractNo")
    private String contractNo;

    /** 房东用户ID */
    @Excel(name = "房东用户ID")
    @JsonProperty("landlordId")
    private Long landlordId;

    /** 房东姓名 */
    @Excel(name = "房东姓名")
    @JsonProperty("landlordName")
    private String landlordName;

    /** 房东手机号 */
    @Excel(name = "房东手机号")
    @JsonProperty("landlordPhone")
    private String landlordPhone;

    /** 租客用户ID */
    @Excel(name = "租客用户ID")
    @JsonProperty("tenantId")
    private Long tenantId;

    /** 租客姓名 */
    @Excel(name = "租客姓名")
    @JsonProperty("tenantName")
    private String tenantName;

    /** 租客手机号 */
    @Excel(name = "租客手机号")
    @JsonProperty("tenantPhone")
    private String tenantPhone;

    /** 租赁开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "租赁开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonProperty("startDate")
    private Date startDate;

    /** 租赁结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "租赁结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonProperty("endDate")
    private Date endDate;

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

    /** 到期状态（0正常 1即将到期 2已过期） */
    @Excel(name = "到期状态", readConverterExp = "0=正常,1=即将到期,2=已过期")
    @JsonProperty("expireStatus")
    private String expireStatus;

    /** 状态（0在租 1已退租 2已到期） */
    @Excel(name = "状态", readConverterExp = "0=在租,1=已退租,2=已到期")
    @JsonProperty("status")
    private String status;

    /** 剩余天数（由 end_date 计算得出，非数据库字段） */
    @Excel(name = "剩余天数")
    @JsonProperty("remainingDays")
    private Long remainingDays;
}
