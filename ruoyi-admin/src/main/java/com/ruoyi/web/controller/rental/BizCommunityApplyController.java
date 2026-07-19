package com.ruoyi.web.controller.rental;

import java.util.List;
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
import com.ruoyi.system.domain.BizCommunityApply;
import com.ruoyi.system.service.IBizCommunityApplyService;

/**
 * 小区申请信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/communityApply")
public class BizCommunityApplyController extends BaseController
{
    @Autowired
    private IBizCommunityApplyService bizCommunityApplyService;

    /**
     * 获取小区申请列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:communityApply:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizCommunityApply bizCommunityApply)
    {
        startPage();
        List<BizCommunityApply> list = bizCommunityApplyService.selectBizCommunityApplyList(bizCommunityApply);
        return getDataTable(list);
    }

    /**
     * 根据申请编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:communityApply:query')")
    @GetMapping(value = "/{applyId}")
    public AjaxResult getInfo(@PathVariable Long applyId)
    {
        return success(bizCommunityApplyService.selectBizCommunityApplyByApplyId(applyId));
    }

    /**
     * 用户申请登记（公开接口）
     */
    @Anonymous
    @Log(title = "小区申请", businessType = BusinessType.INSERT)
    @PostMapping("/apply")
    public AjaxResult apply(@RequestBody BizCommunityApply bizCommunityApply)
    {
        // 默认申请状态为待审批
        bizCommunityApply.setApplyStatus("0");
        return toAjax(bizCommunityApplyService.insertBizCommunityApply(bizCommunityApply));
    }

    /**
     * 审批通过：更新申请状态，并将申请的小区信息写入小区表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:communityApply:approve')")
    @Log(title = "小区申请", businessType = BusinessType.UPDATE)
    @PutMapping("/approve")
    public AjaxResult approve(@RequestBody BizCommunityApply bizCommunityApply)
    {
        if (bizCommunityApply.getApplyId() == null)
        {
            return error("申请ID不能为空");
        }
        bizCommunityApply.setApproveBy(getUsername());
        return toAjax(bizCommunityApplyService.approve(bizCommunityApply));
    }

    /**
     * 审批驳回：需要填写驳回原因
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:communityApply:reject')")
    @Log(title = "小区申请", businessType = BusinessType.UPDATE)
    @PutMapping("/reject")
    public AjaxResult reject(@RequestBody BizCommunityApply bizCommunityApply)
    {
        if (bizCommunityApply.getApplyId() == null)
        {
            return error("申请ID不能为空");
        }
        if (bizCommunityApply.getApproveRemark() == null || bizCommunityApply.getApproveRemark().isEmpty())
        {
            return error("驳回原因不能为空");
        }
        bizCommunityApply.setApproveBy(getUsername());
        return toAjax(bizCommunityApplyService.reject(bizCommunityApply));
    }
}
