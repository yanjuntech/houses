package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 敏感词库 biz_sensitive_word
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizSensitiveWord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 敏感词ID */
    @Excel(name = "敏感词ID")
    @JsonProperty("wordId")
    private Long wordId;

    /** 敏感词 */
    @Excel(name = "敏感词")
    @JsonProperty("word")
    private String word;

    /** 分类（POLITICAL/PORNOGRAPHIC/GAMBLING/ADVERTISING/OTHER） */
    @Excel(name = "分类")
    @JsonProperty("category")
    private String category;

    /** 状态（0启用 1禁用） */
    @Excel(name = "状态", readConverterExp = "0=启用,1=禁用")
    @JsonProperty("status")
    private String status;
}
