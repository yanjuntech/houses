package com.ruoyi.system.domain.vo;

import java.util.List;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 房屋批量导入预览结果
 *
 * @author ruoyi
 */
@Data
public class BizHouseImportPreviewVo
{
    /** 总数量 */
    @ApiModelProperty("总数量")
    private Integer totalCount;

    /** 有效数量 */
    @ApiModelProperty("有效数量")
    private Integer validCount;

    /** 无效数量 */
    @ApiModelProperty("无效数量")
    private Integer invalidCount;

    /** 用户剩余配额 */
    @ApiModelProperty("用户剩余配额")
    private Integer remainingQuota;

    /** 配额是否足够 */
    @ApiModelProperty("配额是否足够")
    private Boolean enoughQuota;

    /** 预览数据 */
    @ApiModelProperty("预览数据")
    private List<BizHouseImportVo> dataList;

    /** 错误信息列表 */
    @ApiModelProperty("错误信息列表")
    private List<String> errorList;
}
