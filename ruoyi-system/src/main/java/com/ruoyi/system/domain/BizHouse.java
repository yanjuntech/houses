package com.ruoyi.system.domain;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 房屋信息 biz_house
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizHouse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 房屋ID */
    @Excel(name = "房屋ID")
    @JsonProperty("houseId")
    private Long houseId;

    /** 房屋标题 */
    @Excel(name = "房屋标题")
    @JsonProperty("title")
    private String title;

    /** 房屋类型（1出租 2求租 3合租 4出售） */
    @Excel(name = "房屋类型", readConverterExp = "1=出租,2=求租,3=合租,4=出售")
    @JsonProperty("houseType")
    private String houseType;

    /** 小区ID */
    @Excel(name = "小区ID")
    @JsonProperty("communityId")
    private Long communityId;

    /** 小区名称（冗余） */
    @Excel(name = "小区名称")
    @JsonProperty("communityName")
    private String communityName;

    /** 室 */
    @Excel(name = "室")
    @JsonProperty("roomCount")
    private Integer roomCount;

    /** 厅 */
    @Excel(name = "厅")
    @JsonProperty("hallCount")
    private Integer hallCount;

    /** 卫 */
    @Excel(name = "卫")
    @JsonProperty("bathCount")
    private Integer bathCount;

    /** 面积 */
    @Excel(name = "面积")
    @JsonProperty("area")
    private BigDecimal area;

    /** 楼层 */
    @Excel(name = "楼层")
    @JsonProperty("floor")
    private Integer floor;

    /** 总楼层 */
    @Excel(name = "总楼层")
    @JsonProperty("totalFloor")
    private Integer totalFloor;

    /** 装修 */
    @Excel(name = "装修")
    @JsonProperty("decoration")
    private String decoration;

    /** 朝向 */
    @Excel(name = "朝向")
    @JsonProperty("orientation")
    private String orientation;

    /** 价格 */
    @Excel(name = "价格")
    @JsonProperty("price")
    private BigDecimal price;

    /** 发布用户ID */
    @Excel(name = "发布用户ID")
    @JsonProperty("publishUserId")
    private Long publishUserId;

    /** 发布用户类型（1房东 2中介） */
    @Excel(name = "发布用户类型", readConverterExp = "1=房东,2=中介")
    @JsonProperty("publishUserType")
    private String publishUserType;

    /** 房屋图片（逗号分隔） */
    @Excel(name = "房屋图片")
    @JsonProperty("images")
    private String images;

    /** 房屋描述 */
    @Excel(name = "房屋描述")
    @JsonProperty("description")
    private String description;

    /** 状态（0下架 1上架 2在租 3已售） */
    @Excel(name = "状态", readConverterExp = "0=下架,1=上架,2=在租,3=已售")
    @JsonProperty("status")
    private String status;

    /** 发布者身份中文标签（1房东直租 2房屋中介），由服务层填充 */
    @Excel(name = "发布者标签")
    @JsonProperty("publishUserTypeLabel")
    private String publishUserTypeLabel;
}
