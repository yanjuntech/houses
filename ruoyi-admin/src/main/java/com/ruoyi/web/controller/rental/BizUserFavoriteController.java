package com.ruoyi.web.controller.rental;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BizUserFavorite;
import com.ruoyi.system.service.IBizUserFavoriteService;

/**
 * 用户收藏操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/favorite")
public class BizUserFavoriteController extends BaseController
{
    @Autowired
    private IBizUserFavoriteService bizUserFavoriteService;

    /**
     * 获取用户收藏列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:favorite:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizUserFavorite bizUserFavorite)
    {
        startPage();
        List<BizUserFavorite> list = bizUserFavoriteService.selectBizUserFavoriteList(bizUserFavorite);
        return getDataTable(list);
    }

    /**
     * 查询某用户的收藏列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:favorite:list')")
    @GetMapping("/userFavorite/{userId}")
    public AjaxResult userFavorite(@PathVariable Long userId)
    {
        BizUserFavorite query = new BizUserFavorite();
        query.setUserId(userId);
        return success(bizUserFavoriteService.selectBizUserFavoriteList(query));
    }

    /**
     * 收藏房源（公开接口）
     */
    @Anonymous
    @Log(title = "用户收藏", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BizUserFavorite bizUserFavorite)
    {
        if (bizUserFavorite.getUserId() == null || bizUserFavorite.getHouseId() == null)
        {
            return error("用户ID和房源ID不能为空");
        }
        return toAjax(bizUserFavoriteService.insertBizUserFavorite(bizUserFavorite));
    }

    /**
     * 取消收藏（公开接口）
     */
    @Anonymous
    @Log(title = "用户收藏", businessType = BusinessType.DELETE)
    @DeleteMapping("/cancel")
    public AjaxResult cancel(@RequestParam Long userId, @RequestParam Long houseId)
    {
        if (userId == null || houseId == null)
        {
            return error("用户ID和房源ID不能为空");
        }
        return toAjax(bizUserFavoriteService.cancelFavorite(userId, houseId));
    }
}
