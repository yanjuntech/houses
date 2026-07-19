package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 小程序用户对象 biz_miniapp_user
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizMiniappUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户ID", cellType = ColumnType.NUMERIC)
    @JsonProperty("userId")
    private Long userId;

    /** 手机号 */
    @Excel(name = "手机号")
    @JsonProperty("phone")
    private String phone;

    /** 微信openid */
    @Excel(name = "微信openid")
    @JsonProperty("openid")
    private String openid;

    /** 微信unionId */
    @Excel(name = "微信unionId")
    @JsonProperty("unionId")
    private String unionId;

    /** 昵称 */
    @Excel(name = "昵称")
    @JsonProperty("nickname")
    private String nickname;

    /** 头像 */
    @Excel(name = "头像")
    @JsonProperty("avatar")
    private String avatar;

    /** 性别（0男 1女 2未知） */
    @Excel(name = "性别", readConverterExp = "0=男,1=女,2=未知")
    @JsonProperty("gender")
    private String gender;

    /** 用户类型（0普通 1房东 2中介） */
    @Excel(name = "用户类型", readConverterExp = "0=普通,1=房东,2=中介")
    @JsonProperty("userType")
    private String userType;

    /** 认证状态（0未认证 1待审核 2已认证 3已拒绝） */
    @Excel(name = "认证状态", readConverterExp = "0=未认证,1=待审核,2=已认证,3=已拒绝")
    @JsonProperty("verifyStatus")
    private String verifyStatus;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    @JsonProperty("realName")
    private String realName;

    /** 身份证号 */
    @Excel(name = "身份证号")
    @JsonProperty("idCard")
    private String idCard;

    /** 账号状态（0正常 1停用 2黑名单） */
    @Excel(name = "账号状态", readConverterExp = "0=正常,1=停用,2=黑名单")
    @JsonProperty("status")
    private String status;

    /** 微信昵称 */
    @Excel(name = "微信昵称")
    @JsonProperty("wechatNickname")
    private String wechatNickname;

    /** 微信头像URL */
    @Excel(name = "微信头像URL")
    @JsonProperty("wechatAvatar")
    private String wechatAvatar;

    /** 身份证是否已认证（0否 1是） */
    @Excel(name = "身份证是否已认证", readConverterExp = "0=否,1=是")
    @JsonProperty("idCardVerified")
    private String idCardVerified;

    /** 拉黑原因 */
    @Excel(name = "拉黑原因")
    @JsonProperty("blacklistReason")
    private String blacklistReason;

    /** 拉黑时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "拉黑时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("blacklistTime")
    private Date blacklistTime;

    /** 最后登录IP */
    @Excel(name = "最后登录IP")
    @JsonProperty("loginIp")
    private String loginIp;

    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("loginDate")
    private Date loginDate;

    /** 标签（逗号分隔） */
    @Excel(name = "标签")
    @JsonProperty("tags")
    private String tags;

    /** 剩余发布次数 */
    @Excel(name = "剩余发布次数")
    @JsonProperty("publishCount")
    private Integer publishCount;

    /** 发布周期结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "周期结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("publishPeriodEnd")
    private Date publishPeriodEnd;

    /** 累计发布次数 */
    @Excel(name = "累计发布次数")
    @JsonProperty("totalPublishCount")
    private Integer totalPublishCount;
}
