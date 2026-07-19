package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 轮播图 biz_banner
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizBanner extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 轮播图ID */
    @Excel(name = "轮播图ID")
    @JsonProperty("bannerId")
    private Long bannerId;

    /** 图片URL */
    @Excel(name = "图片URL")
    @JsonProperty("image")
    private String image;

    /** 简短标题 */
    @Excel(name = "简短标题")
    @JsonProperty("title")
    private String title;

    /** 详细内容 */
    @Excel(name = "详细内容")
    @JsonProperty("content")
    private String content;

    /** 发布有效期开始 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发布有效期开始", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("validStart")
    private Date validStart;

    /** 发布有效期结束 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发布有效期结束", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("validEnd")
    private Date validEnd;

    /** 联系电话 */
    @Excel(name = "联系电话")
    @JsonProperty("contactPhone")
    private String contactPhone;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    @JsonProperty("sort")
    private Integer sort;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    @JsonProperty("status")
    private String status;
}
