package com.ruoyi.web.controller.rental;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizHouseRepair;
import com.ruoyi.system.service.IBizHouseRepairService;

/**
 * 房屋维修信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/repair")
public class BizHouseRepairController extends BaseController
{
    @Autowired
    private IBizHouseRepairService bizHouseRepairService;

    /**
     * 获取房屋维修列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:repair:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizHouseRepair bizHouseRepair)
    {
        startPage();
        List<BizHouseRepair> list = bizHouseRepairService.selectBizHouseRepairList(bizHouseRepair);
        return getDataTable(list);
    }

    /**
     * 导出房屋维修列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:repair:export')")
    @Log(title = "房屋维修", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizHouseRepair bizHouseRepair)
    {
        List<BizHouseRepair> list = bizHouseRepairService.selectBizHouseRepairList(bizHouseRepair);
        ExcelUtil<BizHouseRepair> util = new ExcelUtil<BizHouseRepair>(BizHouseRepair.class);
        util.exportExcel(response, list, "房屋维修数据");
    }

    /**
     * 根据维修ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:repair:query')")
    @GetMapping(value = "/{repairId}")
    public AjaxResult getInfo(@PathVariable Long repairId)
    {
        return success(bizHouseRepairService.selectBizHouseRepairByRepairId(repairId));
    }

    /**
     * 维修申请提交（公开接口）
     */
    @Anonymous
    @Log(title = "房屋维修", businessType = BusinessType.INSERT)
    @PostMapping("/apply")
    public AjaxResult apply(@RequestBody BizHouseRepair bizHouseRepair)
    {
        // 默认状态为待房东确认
        bizHouseRepair.setStatus("0");
        return toAjax(bizHouseRepairService.insertBizHouseRepair(bizHouseRepair));
    }

    /**
     * 房东确认维修
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:repair:edit')")
    @Log(title = "房屋维修", businessType = BusinessType.UPDATE)
    @PutMapping("/landlordConfirm/{repairId}")
    public AjaxResult landlordConfirm(@PathVariable Long repairId)
    {
        return toAjax(bizHouseRepairService.landlordConfirm(repairId));
    }

    /**
     * 房东完成维修
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:repair:edit')")
    @Log(title = "房屋维修", businessType = BusinessType.UPDATE)
    @PutMapping("/landlordComplete/{repairId}")
    public AjaxResult landlordComplete(@PathVariable Long repairId)
    {
        return toAjax(bizHouseRepairService.landlordComplete(repairId));
    }

    /**
     * 租客确认完成（公开接口）
     */
    @Anonymous
    @Log(title = "房屋维修", businessType = BusinessType.UPDATE)
    @PutMapping("/tenantConfirm/{repairId}")
    public AjaxResult tenantConfirm(@PathVariable Long repairId)
    {
        return toAjax(bizHouseRepairService.tenantConfirm(repairId));
    }

    /**
     * 房东选择租客自修
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:repair:edit')")
    @Log(title = "房屋维修", businessType = BusinessType.UPDATE)
    @PutMapping("/landlordChooseTenantRepair/{repairId}")
    public AjaxResult landlordChooseTenantRepair(@PathVariable Long repairId)
    {
        return toAjax(bizHouseRepairService.landlordChooseTenantRepair(repairId));
    }

    /**
     * 租客上传凭证（公开接口）
     */
    @Anonymous
    @Log(title = "房屋维修", businessType = BusinessType.UPDATE)
    @PutMapping("/tenantUploadReceipt")
    public AjaxResult tenantUploadReceipt(@RequestBody BizHouseRepair bizHouseRepair)
    {
        if (bizHouseRepair.getRepairId() == null)
        {
            return error("维修ID不能为空");
        }
        return toAjax(bizHouseRepairService.tenantUploadReceipt(bizHouseRepair.getRepairId(),
                bizHouseRepair.getReceiptImages(), bizHouseRepair.getReceiptAmount()));
    }

    /**
     * 房东确认报销
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:repair:edit')")
    @Log(title = "房屋维修", businessType = BusinessType.UPDATE)
    @PutMapping("/landlordConfirmReimburse/{repairId}")
    public AjaxResult landlordConfirmReimburse(@PathVariable Long repairId)
    {
        return toAjax(bizHouseRepairService.landlordConfirmReimburse(repairId));
    }

    /**
     * 取消维修（公开接口）
     */
    @Anonymous
    @Log(title = "房屋维修", businessType = BusinessType.UPDATE)
    @PutMapping("/cancel/{repairId}")
    public AjaxResult cancel(@PathVariable Long repairId)
    {
        return toAjax(bizHouseRepairService.cancelRepair(repairId));
    }
}
