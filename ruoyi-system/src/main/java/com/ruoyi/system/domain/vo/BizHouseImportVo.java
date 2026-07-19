package com.ruoyi.system.domain.vo;

import java.math.BigDecimal;
import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 房屋批量导入 Excel 行对象
 *
 * @author ruoyi
 */
@Data
public class BizHouseImportVo
{
    /** 标题 */
    @Excel(name = "标题")
    @ApiModelProperty("标题")
    private String title;

    /** 租赁类型（1出租 2求租 3合租 4出售） */
    @Excel(name = "租赁类型", readConverterExp = "1=出租,2=求租,3=合租,4=出售")
    @ApiModelProperty("租赁类型（1出租 2求租 3合租 4出售）")
    private String houseType;

    /** 小区名称 */
    @Excel(name = "小区名称")
    @ApiModelProperty("小区名称")
    private String communityName;

    /** 室 */
    @Excel(name = "室")
    @ApiModelProperty("室")
    private Integer roomCount;

    /** 厅 */
    @Excel(name = "厅")
    @ApiModelProperty("厅")
    private Integer hallCount;

    /** 卫 */
    @Excel(name = "卫")
    @ApiModelProperty("卫")
    private Integer bathCount;

    /** 面积 */
    @Excel(name = "面积")
    @ApiModelProperty("面积")
    private BigDecimal area;

    /** 楼层 */
    @Excel(name = "楼层")
    @ApiModelProperty("楼层")
    private Integer floor;

    /** 总楼层 */
    @Excel(name = "总楼层")
    @ApiModelProperty("总楼层")
    private Integer totalFloor;

    /** 装修 */
    @Excel(name = "装修")
    @ApiModelProperty("装修")
    private String decoration;

    /** 朝向 */
    @Excel(name = "朝向")
    @ApiModelProperty("朝向")
    private String orientation;

    /** 价格 */
    @Excel(name = "价格")
    @ApiModelProperty("价格")
    private BigDecimal price;

    /** 描述 */
    @Excel(name = "描述")
    @ApiModelProperty("描述")
    private String description;
}
