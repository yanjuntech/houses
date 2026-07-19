package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户收藏 biz_user_favorite
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizUserFavorite extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 收藏ID */
    @Excel(name = "收藏ID")
    @JsonProperty("favoriteId")
    private Long favoriteId;

    /** 用户ID */
    @Excel(name = "用户ID")
    @JsonProperty("userId")
    private Long userId;

    /** 房源ID */
    @Excel(name = "房源ID")
    @JsonProperty("houseId")
    private Long houseId;

    /** 房源标题（冗余） */
    @Excel(name = "房源标题")
    @JsonProperty("houseTitle")
    private String houseTitle;
}
