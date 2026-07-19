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
import com.ruoyi.system.domain.BizCommunity;
import com.ruoyi.system.service.IBizCommunityService;

/**
 * 小区信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/community")
public class BizCommunityController extends BaseController
{
    @Autowired
    private IBizCommunityService bizCommunityService;

    /**
     * 获取小区列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:community:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizCommunity bizCommunity)
    {
        startPage();
        List<BizCommunity> list = bizCommunityService.selectBizCommunityList(bizCommunity);
        return getDataTable(list);
    }

    /**
     * 导出小区列表
     */
    @Log(title = "小区管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('biz:rental:community:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizCommunity bizCommunity)
    {
        List<BizCommunity> list = bizCommunityService.selectBizCommunityList(bizCommunity);
        ExcelUtil<BizCommunity> util = new ExcelUtil<BizCommunity>(BizCommunity.class);
        util.exportExcel(response, list, "小区数据");
    }

    /**
     * 根据小区编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:community:query')")
    @GetMapping(value = "/{communityId}")
    public AjaxResult getInfo(@PathVariable Long communityId)
    {
        return success(bizCommunityService.selectBizCommunityByCommunityId(communityId));
    }

    /**
     * 新增小区
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:community:add')")
    @Log(title = "小区管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizCommunity bizCommunity)
    {
        bizCommunity.setCreateBy(getUsername());
        return toAjax(bizCommunityService.insertBizCommunity(bizCommunity));
    }

    /**
     * 修改小区
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:community:edit')")
    @Log(title = "小区管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizCommunity bizCommunity)
    {
        bizCommunity.setUpdateBy(getUsername());
        return toAjax(bizCommunityService.updateBizCommunity(bizCommunity));
    }

    /**
     * 删除小区
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:community:remove')")
    @Log(title = "小区管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{communityIds}")
    public AjaxResult remove(@PathVariable Long[] communityIds)
    {
        return toAjax(bizCommunityService.deleteBizCommunityByCommunityIds(communityIds));
    }

    /**
     * 获取全部小区下拉选择列表（公开接口）
     */
    @Anonymous
    @GetMapping("/selectAll")
    public AjaxResult selectAll()
    {
        BizCommunity query = new BizCommunity();
        query.setStatus("0");
        List<BizCommunity> list = bizCommunityService.selectBizCommunityList(query);
        return success(list);
    }
}
