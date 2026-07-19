package com.ruoyi.web.controller.rental;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BizUserBrowse;
import com.ruoyi.system.service.IBizUserBrowseService;

/**
 * 用户浏览记录操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/browse")
public class BizUserBrowseController extends BaseController
{
    @Autowired
    private IBizUserBrowseService bizUserBrowseService;

    /**
     * 获取用户浏览记录列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:browse:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizUserBrowse bizUserBrowse)
    {
        startPage();
        List<BizUserBrowse> list = bizUserBrowseService.selectBizUserBrowseList(bizUserBrowse);
        return getDataTable(list);
    }

    /**
     * 查询某用户的浏览记录
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:browse:list')")
    @GetMapping("/userBrowse/{userId}")
    public AjaxResult userBrowse(@PathVariable Long userId)
    {
        BizUserBrowse query = new BizUserBrowse();
        query.setUserId(userId);
        return success(bizUserBrowseService.selectBizUserBrowseList(query));
    }

    /**
     * 记录浏览（公开接口）
     */
    @Anonymous
    @Log(title = "用户浏览记录", businessType = BusinessType.INSERT)
    @PostMapping("/record")
    public AjaxResult record(@RequestBody BizUserBrowse bizUserBrowse)
    {
        if (bizUserBrowse.getUserId() == null || bizUserBrowse.getHouseId() == null)
        {
            return error("用户ID和房源ID不能为空");
        }
        return toAjax(bizUserBrowseService.recordBrowse(
                bizUserBrowse.getUserId(), bizUserBrowse.getHouseId(), bizUserBrowse.getHouseTitle()));
    }
}
