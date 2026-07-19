package com.ruoyi.system.domain;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 行政区划表 sys_region
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRegion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 区划ID */
    @Excel(name = "区划ID", cellType = ColumnType.NUMERIC)
    @JsonProperty("regionId")
    private Long regionId;

    /** 父级ID */
    @Excel(name = "父级ID", cellType = ColumnType.NUMERIC)
    @JsonProperty("parentId")
    private Long parentId;

    /** 区划编码 */
    @Excel(name = "区划编码")
    @JsonProperty("regionCode")
    private String regionCode;

    /** 区划名称 */
    @Excel(name = "区划名称")
    @JsonProperty("regionName")
    private String regionName;

    /** 层级（1省 2市 3区县） */
    @Excel(name = "层级", readConverterExp = "1=省,2=市,3=区县")
    @JsonProperty("regionLevel")
    private Integer regionLevel;

    /** 小区登记开关（0开启 1关闭） */
    @Excel(name = "小区登记开关", readConverterExp = "0=开启,1=关闭")
    @JsonProperty("communityRegisterSwitch")
    private String communityRegisterSwitch;

    /** 显示顺序 */
    @Excel(name = "显示顺序", cellType = ColumnType.NUMERIC)
    @JsonProperty("sort")
    private Integer sort;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    @JsonProperty("status")
    private String status;

    /** 子区划 */
    private List<SysRegion> children;
}
