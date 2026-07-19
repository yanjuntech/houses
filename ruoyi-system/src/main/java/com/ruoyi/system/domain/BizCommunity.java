package com.ruoyi.system.domain;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 小区表 biz_community
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizCommunity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 小区ID */
    @Excel(name = "小区ID", cellType = ColumnType.NUMERIC)
    @JsonProperty("communityId")
    private Long communityId;

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

    /** 经度 */
    @Excel(name = "经度", cellType = ColumnType.NUMERIC)
    @JsonProperty("longitude")
    private BigDecimal longitude;

    /** 纬度 */
    @Excel(name = "纬度", cellType = ColumnType.NUMERIC)
    @JsonProperty("latitude")
    private BigDecimal latitude;

    /** 物业公司 */
    @Excel(name = "物业公司")
    @JsonProperty("propertyCompany")
    private String propertyCompany;

    /** 物业联系电话 */
    @Excel(name = "物业联系电话")
    @JsonProperty("propertyPhone")
    private String propertyPhone;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    @JsonProperty("status")
    private String status;
}
