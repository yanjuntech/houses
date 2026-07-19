package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户兑换配额对象 biz_exchange_quota
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizExchangeQuota extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 配额ID */
    @Excel(name = "配额ID")
    @JsonProperty("quotaId")
    private Long quotaId;

    /** 用户ID */
    @Excel(name = "用户ID")
    @JsonProperty("userId")
    private Long userId;

    /** 配额类型（HOUSE_PUBLISH/PHONEBOOK_DELAY/VIP） */
    @Excel(name = "配额类型")
    @JsonProperty("quotaType")
    private String quotaType;

    /** 总数量 */
    @Excel(name = "总数量")
    @JsonProperty("totalCount")
    private Integer totalCount;

    /** 已使用数量 */
    @Excel(name = "已使用数量")
    @JsonProperty("usedCount")
    private Integer usedCount;

    /** 剩余数量 */
    @Excel(name = "剩余数量")
    @JsonProperty("remainingCount")
    private Integer remainingCount;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("expireTime")
    private Date expireTime;
}
