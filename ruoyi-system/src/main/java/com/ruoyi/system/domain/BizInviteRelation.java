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
 * 邀请关系对象 biz_invite_relation
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizInviteRelation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关系ID */
    @Excel(name = "关系ID", cellType = ColumnType.NUMERIC)
    @JsonProperty("relationId")
    private Long relationId;

    /** 邀请人用户ID */
    @Excel(name = "邀请人用户ID", cellType = ColumnType.NUMERIC)
    @JsonProperty("inviterId")
    private Long inviterId;

    /** 被邀请人用户ID */
    @Excel(name = "被邀请人用户ID", cellType = ColumnType.NUMERIC)
    @JsonProperty("inviteeId")
    private Long inviteeId;

    /** 邀请码 */
    @Excel(name = "邀请码")
    @JsonProperty("inviteCode")
    private String inviteCode;

    /** 邀请状态（0已注册 1已认证） */
    @Excel(name = "邀请状态", readConverterExp = "0=已注册,1=已认证")
    @JsonProperty("inviteStatus")
    private String inviteStatus;

    /** 邀请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "邀请时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("inviteTime")
    private Date inviteTime;

    /** 被邀请人认证时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "认证时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("certifiedTime")
    private Date certifiedTime;

    /** 邀请人昵称（关联查询展示） */
    @Excel(name = "邀请人昵称")
    @JsonProperty("inviterName")
    private String inviterName;

    /** 邀请人手机号（关联查询展示） */
    @Excel(name = "邀请人手机号")
    @JsonProperty("inviterPhone")
    private String inviterPhone;

    /** 被邀请人昵称（关联查询展示） */
    @Excel(name = "被邀请人昵称")
    @JsonProperty("inviteeName")
    private String inviteeName;

    /** 被邀请人手机号（关联查询展示） */
    @Excel(name = "被邀请人手机号")
    @JsonProperty("inviteePhone")
    private String inviteePhone;
}
