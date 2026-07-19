package com.ruoyi.web.controller.rental;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.ruoyi.system.domain.BizBanner;
import com.ruoyi.system.service.IBizBannerService;

/**
 * 轮播图操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/banner")
public class BizBannerController extends BaseController
{
    @Autowired
    private IBizBannerService bizBannerService;

    /**
     * 获取轮播图列表
     */
    @PreAuthorize("@ss.hasPermi('rental:banner:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizBanner bizBanner)
    {
        startPage();
        List<BizBanner> list = bizBannerService.selectBizBannerList(bizBanner);
        return getDataTable(list);
    }

    /**
     * 导出轮播图列表
     */
    @PreAuthorize("@ss.hasPermi('rental:banner:export')")
    @Log(title = "轮播图管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizBanner bizBanner)
    {
        List<BizBanner> list = bizBannerService.selectBizBannerList(bizBanner);
        ExcelUtil<BizBanner> util = new ExcelUtil<BizBanner>(BizBanner.class);
        util.exportExcel(response, list, "轮播图数据");
    }

    /**
     * 根据轮播图ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('rental:banner:query')")
    @GetMapping(value = "/{bannerId}")
    public AjaxResult getInfo(@PathVariable Long bannerId)
    {
        return success(bizBannerService.selectBizBannerById(bannerId));
    }

    /**
     * 新增轮播图
     */
    @PreAuthorize("@ss.hasPermi('rental:banner:add')")
    @Log(title = "轮播图管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizBanner bizBanner)
    {
        bizBanner.setCreateBy(getUsername());
        return toAjax(bizBannerService.insertBizBanner(bizBanner));
    }

    /**
     * 修改轮播图
     */
    @PreAuthorize("@ss.hasPermi('rental:banner:edit')")
    @Log(title = "轮播图管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizBanner bizBanner)
    {
        bizBanner.setUpdateBy(getUsername());
        return toAjax(bizBannerService.updateBizBanner(bizBanner));
    }

    /**
     * 删除轮播图
     */
    @PreAuthorize("@ss.hasPermi('rental:banner:remove')")
    @Log(title = "轮播图管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bannerIds}")
    public AjaxResult remove(@PathVariable Long[] bannerIds)
    {
        return toAjax(bizBannerService.deleteBizBannerByIds(bannerIds));
    }

    /**
     * 获取有效轮播图列表（小程序端，匿名访问）
     */
    @Anonymous
    @GetMapping("/validList")
    public AjaxResult validList()
    {
        List<BizBanner> list = bizBannerService.selectValidBannerList();
        return success(list);
    }
}
