package com.ruoyi.web.controller.rental;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizRentalContract;
import com.ruoyi.system.service.IBizRentalContractService;

/**
 * 在租房屋管理 操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/rentalContract")
public class BizRentalContractController extends BaseController
{
    @Autowired
    private IBizRentalContractService bizRentalContractService;

    /**
     * 获取在租房屋列表
     * 查询前先刷新到期状态
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:rentalContract:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizRentalContract bizRentalContract)
    {
        // 刷新到期状态
        bizRentalContractService.refreshExpireStatus();
        startPage();
        List<BizRentalContract> list = bizRentalContractService.selectBizRentalContractList(bizRentalContract);
        return getDataTable(list);
    }

    /**
     * 导出在租房屋列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:rentalContract:export')")
    @Log(title = "在租房屋管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizRentalContract bizRentalContract)
    {
        List<BizRentalContract> list = bizRentalContractService.selectBizRentalContractList(bizRentalContract);
        ExcelUtil<BizRentalContract> util = new ExcelUtil<BizRentalContract>(BizRentalContract.class);
        util.exportExcel(response, list, "在租房屋数据");
    }

    /**
     * 根据在租ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:rentalContract:query')")
    @GetMapping(value = "/{rentalId}")
    public AjaxResult getInfo(@PathVariable Long rentalId)
    {
        return success(bizRentalContractService.selectBizRentalContractByRentalId(rentalId));
    }

    /**
     * 新增在租房屋
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:rentalContract:add')")
    @Log(title = "在租房屋管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizRentalContract bizRentalContract)
    {
        bizRentalContract.setCreateBy(getUsername());
        return toAjax(bizRentalContractService.insertBizRentalContract(bizRentalContract));
    }

    /**
     * 修改在租房屋
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:rentalContract:edit')")
    @Log(title = "在租房屋管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizRentalContract bizRentalContract)
    {
        bizRentalContract.setUpdateBy(getUsername());
        return toAjax(bizRentalContractService.updateBizRentalContract(bizRentalContract));
    }

    /**
     * 删除在租房屋
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:rentalContract:remove')")
    @Log(title = "在租房屋管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{rentalIds}")
    public AjaxResult remove(@PathVariable Long[] rentalIds)
    {
        return toAjax(bizRentalContractService.deleteBizRentalContractByRentalIds(rentalIds));
    }

    /**
     * 即将到期列表（7天内）
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:rentalContract:list')")
    @GetMapping("/expiringSoon")
    public TableDataInfo expiringSoon()
    {
        startPage();
        List<BizRentalContract> list = bizRentalContractService.selectExpiringSoon(7);
        return getDataTable(list);
    }

    /**
     * 手动刷新到期状态
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:rentalContract:list')")
    @Log(title = "在租房屋管理", businessType = BusinessType.UPDATE)
    @GetMapping("/refreshExpire")
    public AjaxResult refreshExpire()
    {
        return toAjax(bizRentalContractService.refreshExpireStatus());
    }
}
