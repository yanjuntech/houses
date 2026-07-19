package com.ruoyi.web.controller.system;

import java.util.List;
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
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysRegion;
import com.ruoyi.system.service.ISysRegionService;

/**
 * 行政区划信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/region")
public class SysRegionController extends BaseController
{
    @Autowired
    private ISysRegionService regionService;

    /**
     * 获取行政区划列表（树形结构）
     */
    @PreAuthorize("@ss.hasPermi('system:region:list')")
    @GetMapping("/list")
    public AjaxResult list(SysRegion region)
    {
        List<SysRegion> regions = regionService.selectRegionList(region);
        List<SysRegion> regionTree = regionService.buildRegionTree(regions);
        return success(regionTree);
    }

    /**
     * 根据区划编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:region:query')")
    @GetMapping(value = "/{regionId}")
    public AjaxResult getInfo(@PathVariable Long regionId)
    {
        return success(regionService.selectRegionById(regionId));
    }

    /**
     * 根据父ID查询子级列表（用于级联选择）
     */
    @GetMapping("/listByParentId/{parentId}")
    public AjaxResult listByParentId(@PathVariable Long parentId)
    {
        List<SysRegion> regions = regionService.selectRegionByParentId(parentId);
        return success(regions);
    }

    /**
     * 新增行政区划
     */
    @PreAuthorize("@ss.hasPermi('system:region:add')")
    @Log(title = "行政区划管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRegion region)
    {
        if (!regionService.checkRegionNameUnique(region))
        {
            return error("新增区划'" + region.getRegionName() + "'失败，区划名称已存在");
        }
        region.setCreateBy(getUsername());
        return toAjax(regionService.insertRegion(region));
    }

    /**
     * 修改行政区划
     */
    @PreAuthorize("@ss.hasPermi('system:region:edit')")
    @Log(title = "行政区划管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRegion region)
    {
        Long regionId = region.getRegionId();
        if (!regionService.checkRegionNameUnique(region))
        {
            return error("修改区划'" + region.getRegionName() + "'失败，区划名称已存在");
        }
        else if (region.getParentId() != null && region.getParentId().equals(regionId))
        {
            return error("修改区划'" + region.getRegionName() + "'失败，上级区划不能是自己");
        }
        region.setUpdateBy(getUsername());
        return toAjax(regionService.updateRegion(region));
    }

    /**
     * 删除行政区划
     */
    @PreAuthorize("@ss.hasPermi('system:region:remove')")
    @Log(title = "行政区划管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{regionId}")
    public AjaxResult remove(@PathVariable Long regionId)
    {
        if (regionService.hasChildByRegionId(regionId))
        {
            return warn("存在下级区划,不允许删除");
        }
        return toAjax(regionService.deleteRegionById(regionId));
    }

    /**
     * 修改小区登记开关
     */
    @PreAuthorize("@ss.hasPermi('system:region:edit')")
    @Log(title = "行政区划管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeRegisterSwitch")
    public AjaxResult changeRegisterSwitch(@RequestBody SysRegion region)
    {
        region.setUpdateBy(getUsername());
        return toAjax(regionService.updateRegion(region));
    }
}
