package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户消息 biz_user_message
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizUserMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消息ID */
    @Excel(name = "消息ID")
    @JsonProperty("messageId")
    private Long messageId;

    /** 接收用户ID */
    @Excel(name = "接收用户ID")
    @JsonProperty("userId")
    private Long userId;

    /** 消息标题 */
    @Excel(name = "消息标题")
    @JsonProperty("title")
    private String title;

    /** 消息内容 */
    @Excel(name = "消息内容")
    @JsonProperty("content")
    private String content;

    /** 是否已读（0未读 1已读） */
    @Excel(name = "是否已读", readConverterExp = "0=未读,1=已读")
    @JsonProperty("isRead")
    private String isRead;

    /** 阅读时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "阅读时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("readTime")
    private Date readTime;

    /** 发送人 */
    @Excel(name = "发送人")
    @JsonProperty("sendBy")
    private String sendBy;

    /** 发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("sendTime")
    private Date sendTime;
}
