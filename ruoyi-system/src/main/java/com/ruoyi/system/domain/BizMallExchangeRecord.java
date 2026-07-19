package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 兑换记录对象 biz_mall_exchange_record
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizMallExchangeRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 兑换记录ID */
    @Excel(name = "兑换记录ID")
    @JsonProperty("recordId")
    private Long recordId;

    /** 兑换用户ID */
    @Excel(name = "兑换用户ID")
    @JsonProperty("userId")
    private Long userId;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    @JsonProperty("userName")
    private String userName;

    /** 用户手机号 */
    @Excel(name = "用户手机号")
    @JsonProperty("userPhone")
    private String userPhone;

    /** 商品ID */
    @Excel(name = "商品ID")
    @JsonProperty("productId")
    private Long productId;

    /** 商品名称（冗余） */
    @Excel(name = "商品名称")
    @JsonProperty("productName")
    private String productName;

    /** 商品类型（冗余） */
    @Excel(name = "商品类型")
    @JsonProperty("productType")
    private String productType;

    /** 消耗邀请人数 */
    @Excel(name = "消耗邀请人数")
    @JsonProperty("costInvites")
    private Integer costInvites;

    /** 兑换时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "兑换时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("exchangeTime")
    private Date exchangeTime;

    /** 生效状态（0已生效 1已失效） */
    @Excel(name = "生效状态", readConverterExp = "0=已生效,1=已失效")
    @JsonProperty("effectStatus")
    private String effectStatus;
}
