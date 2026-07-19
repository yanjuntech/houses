package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 聊天消息 biz_chat_message
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizChatMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消息ID */
    @Excel(name = "消息ID")
    @JsonProperty("messageId")
    private Long messageId;

    /** 发送方用户ID */
    @Excel(name = "发送方用户ID")
    @JsonProperty("senderId")
    private Long senderId;

    /** 发送方昵称 */
    @Excel(name = "发送方昵称")
    @JsonProperty("senderName")
    private String senderName;

    /** 接收方用户ID */
    @Excel(name = "接收方用户ID")
    @JsonProperty("receiverId")
    private Long receiverId;

    /** 接收方昵称 */
    @Excel(name = "接收方昵称")
    @JsonProperty("receiverName")
    private String receiverName;

    /** 关联房源ID */
    @Excel(name = "关联房源ID")
    @JsonProperty("houseId")
    private Long houseId;

    /** 消息内容 */
    @Excel(name = "消息内容")
    @JsonProperty("content")
    private String content;

    /** 是否被敏感词过滤（0否 1是） */
    @Excel(name = "是否被过滤", readConverterExp = "0=否,1=是")
    @JsonProperty("isFiltered")
    private String isFiltered;

    /** 过滤原因 */
    @Excel(name = "过滤原因")
    @JsonProperty("filterReason")
    private String filterReason;

    /** 已读状态（0未读 1已读） */
    @Excel(name = "已读状态", readConverterExp = "0=未读,1=已读")
    @JsonProperty("readStatus")
    private String readStatus;

    /** 已读时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "已读时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("readTime")
    private Date readTime;
}
