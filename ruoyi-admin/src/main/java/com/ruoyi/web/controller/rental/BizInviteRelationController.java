package com.ruoyi.web.controller.rental;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.ruoyi.system.domain.BizInviteRelation;
import com.ruoyi.system.service.IBizInviteRelationService;

/**
 * 邀请管理操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/invite")
public class BizInviteRelationController extends BaseController
{
    @Autowired
    private IBizInviteRelationService bizInviteRelationService;

    /**
     * 获取邀请关系列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:invite:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizInviteRelation bizInviteRelation)
    {
        startPage();
        List<BizInviteRelation> list = bizInviteRelationService.selectBizInviteRelationList(bizInviteRelation);
        return getDataTable(list);
    }

    /**
     * 导出邀请关系列表
     */
    @Log(title = "邀请管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('biz:rental:invite:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizInviteRelation bizInviteRelation)
    {
        List<BizInviteRelation> list = bizInviteRelationService.selectBizInviteRelationList(bizInviteRelation);
        ExcelUtil<BizInviteRelation> util = new ExcelUtil<BizInviteRelation>(BizInviteRelation.class);
        util.exportExcel(response, list, "邀请关系数据");
    }

    /**
     * 根据关系ID获取邀请关系详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:invite:query')")
    @GetMapping(value = "/{relationId}")
    public AjaxResult getInfo(@PathVariable Long relationId)
    {
        return success(bizInviteRelationService.selectBizInviteRelationByRelationId(relationId));
    }

    /**
     * 新增邀请关系
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:invite:add')")
    @Log(title = "邀请管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizInviteRelation bizInviteRelation)
    {
        bizInviteRelation.setCreateBy(getUsername());
        return toAjax(bizInviteRelationService.insertBizInviteRelation(bizInviteRelation));
    }

    /**
     * 修改邀请关系
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:invite:edit')")
    @Log(title = "邀请管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizInviteRelation bizInviteRelation)
    {
        bizInviteRelation.setUpdateBy(getUsername());
        return toAjax(bizInviteRelationService.updateBizInviteRelation(bizInviteRelation));
    }

    /**
     * 删除邀请关系
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:invite:remove')")
    @Log(title = "邀请管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{relationIds}")
    public AjaxResult remove(@PathVariable Long[] relationIds)
    {
        return toAjax(bizInviteRelationService.deleteBizInviteRelationByRelationIds(relationIds));
    }

    /**
     * 邀请统计：查询某邀请人的总邀请人数和已认证邀请人数
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:invite:query')")
    @GetMapping("/statistics/{inviterId}")
    public AjaxResult statistics(@PathVariable Long inviterId)
    {
        // 总邀请人数
        BizInviteRelation query = new BizInviteRelation();
        query.setInviterId(inviterId);
        List<BizInviteRelation> allList = bizInviteRelationService.selectBizInviteRelationList(query);
        int totalCount = allList.size();
        // 已认证邀请人数
        int certifiedCount = bizInviteRelationService.selectInviteCountByInviter(inviterId);
        Map<String, Object> data = new HashMap<>();
        data.put("totalCount", totalCount);
        data.put("certifiedCount", certifiedCount);
        return success(data);
    }

    /**
     * 查询某邀请人的邀请列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:invite:query')")
    @GetMapping("/inviteList/{inviterId}")
    public AjaxResult inviteList(@PathVariable Long inviterId)
    {
        return success(bizInviteRelationService.selectInviteListByInviter(inviterId));
    }

    /**
     * 邀请关系绑定（公开接口）
     * 参数：inviterId（邀请人ID）、inviteeId（被邀请人ID）、inviteCode
     */
    @Anonymous
    @Log(title = "邀请管理", businessType = BusinessType.INSERT)
    @PostMapping("/bindInvite")
    public AjaxResult bindInvite(@RequestBody BizInviteRelation bizInviteRelation)
    {
        return toAjax(bizInviteRelationService.bindInvite(bizInviteRelation));
    }

    /**
     * 被邀请人完成实名认证时调用，更新邀请状态为"已认证"
     * 参数：inviteeId
     */
    @Log(title = "邀请管理", businessType = BusinessType.UPDATE)
    @PutMapping("/updateCertified")
    public AjaxResult updateCertified(@RequestBody BizInviteRelation bizInviteRelation)
    {
        return toAjax(bizInviteRelationService.updateCertified(bizInviteRelation.getInviteeId()));
    }
}
