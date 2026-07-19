package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商城商品对象 biz_mall_product
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizMallProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @Excel(name = "商品ID")
    @JsonProperty("productId")
    private Long productId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    @JsonProperty("productName")
    private String productName;

    /** 商品类型（HOUSE_PUBLISH_10/HOUSE_PUBLISH_20/PHONEBOOK_DELAY_10/PHONEBOOK_DELAY_30/VIP_MONTH） */
    @Excel(name = "商品类型")
    @JsonProperty("productType")
    private String productType;

    /** 商品描述 */
    @Excel(name = "商品描述")
    @JsonProperty("description")
    private String description;

    /** 所需邀请人数 */
    @Excel(name = "所需邀请人数")
    @JsonProperty("requiredInvites")
    private Integer requiredInvites;

    /** 库存数量 */
    @Excel(name = "库存数量")
    @JsonProperty("stock")
    private Integer stock;

    /** 生效数值（如+10天的10） */
    @Excel(name = "生效数值")
    @JsonProperty("effectValue")
    private Integer effectValue;

    /** 状态（0上架 1下架） */
    @Excel(name = "状态", readConverterExp = "0=上架,1=下架")
    @JsonProperty("status")
    private String status;
}
