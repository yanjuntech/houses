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
import com.ruoyi.system.domain.BizPhonebookApply;
import com.ruoyi.system.service.IBizPhonebookApplyService;

/**
 * 电话簿申请信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/phonebookApply")
public class BizPhonebookApplyController extends BaseController
{
    @Autowired
    private IBizPhonebookApplyService bizPhonebookApplyService;

    /**
     * 获取电话簿申请列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:phonebookApply:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizPhonebookApply bizPhonebookApply)
    {
        startPage();
        List<BizPhonebookApply> list = bizPhonebookApplyService.selectBizPhonebookApplyList(bizPhonebookApply);
        return getDataTable(list);
    }

    /**
     * 根据申请编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:phonebookApply:query')")
    @GetMapping(value = "/{applyId}")
    public AjaxResult getInfo(@PathVariable Long applyId)
    {
        return success(bizPhonebookApplyService.selectBizPhonebookApplyByApplyId(applyId));
    }

    /**
     * 用户申请收录（公开接口）
     */
    @Anonymous
    @Log(title = "电话簿申请", businessType = BusinessType.INSERT)
    @PostMapping("/apply")
    public AjaxResult apply(@RequestBody BizPhonebookApply bizPhonebookApply)
    {
        // 默认申请状态为待审批
        bizPhonebookApply.setApplyStatus("0");
        return toAjax(bizPhonebookApplyService.insertBizPhonebookApply(bizPhonebookApply));
    }

    /**
     * 审批通过：更新申请状态，并将申请的商家信息写入电话簿表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:phonebookApply:approve')")
    @Log(title = "电话簿申请", businessType = BusinessType.UPDATE)
    @PutMapping("/approve")
    public AjaxResult approve(@RequestBody BizPhonebookApply bizPhonebookApply)
    {
        if (bizPhonebookApply.getApplyId() == null)
        {
            return error("申请ID不能为空");
        }
        bizPhonebookApply.setApproveBy(getUsername());
        return toAjax(bizPhonebookApplyService.approve(bizPhonebookApply));
    }

    /**
     * 审批驳回：需要填写驳回原因
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:phonebookApply:reject')")
    @Log(title = "电话簿申请", businessType = BusinessType.UPDATE)
    @PutMapping("/reject")
    public AjaxResult reject(@RequestBody BizPhonebookApply bizPhonebookApply)
    {
        if (bizPhonebookApply.getApplyId() == null)
        {
            return error("申请ID不能为空");
        }
        if (bizPhonebookApply.getApproveRemark() == null || bizPhonebookApply.getApproveRemark().isEmpty())
        {
            return error("驳回原因不能为空");
        }
        bizPhonebookApply.setApproveBy(getUsername());
        return toAjax(bizPhonebookApplyService.reject(bizPhonebookApply));
    }
}
