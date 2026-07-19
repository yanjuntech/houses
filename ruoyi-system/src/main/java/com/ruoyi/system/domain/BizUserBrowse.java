package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户浏览记录 biz_user_browse
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizUserBrowse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 浏览记录ID */
    @Excel(name = "浏览记录ID")
    @JsonProperty("browseId")
    private Long browseId;

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

    /** 浏览时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "浏览时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("browseTime")
    private Date browseTime;
}
