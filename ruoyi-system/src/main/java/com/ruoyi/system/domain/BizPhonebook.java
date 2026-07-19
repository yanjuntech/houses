package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 电话簿表 biz_phonebook
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizPhonebook extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 电话簿ID */
    @Excel(name = "电话簿ID", cellType = ColumnType.NUMERIC)
    @JsonProperty("phonebookId")
    private Long phonebookId;

    /** 商家名称 */
    @Excel(name = "商家名称")
    @JsonProperty("merchantName")
    private String merchantName;

    /** 负责人姓名 */
    @Excel(name = "负责人姓名")
    @JsonProperty("ownerName")
    private String ownerName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    @JsonProperty("phone")
    private String phone;

    /** 联系电话1 */
    @Excel(name = "联系电话1")
    @JsonProperty("phone1")
    private String phone1;

    /** 联系电话2 */
    @Excel(name = "联系电话2")
    @JsonProperty("phone2")
    private String phone2;

    /** 分类 */
    @Excel(name = "分类")
    @JsonProperty("category")
    private String category;

    /** 地址 */
    @Excel(name = "地址")
    @JsonProperty("address")
    private String address;

    /** 营业执照 */
    @Excel(name = "营业执照")
    @JsonProperty("businessLicense")
    private String businessLicense;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    @JsonProperty("status")
    private String status;

    /** 有效期截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "有效期截止", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("validUntil")
    private Date validUntil;
}
