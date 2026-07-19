package com.ruoyi.web.controller.miniapp;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizUserMessage;
import com.ruoyi.system.service.IBizUserMessageService;

/**
 * 用户消息管理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/miniapp/message")
public class BizUserMessageController extends BaseController
{
    @Autowired
    private IBizUserMessageService bizUserMessageService;

    /**
     * 查询用户消息列表
     */
    @PreAuthorize("@ss.hasPermi('miniapp:message:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizUserMessage bizUserMessage)
    {
        startPage();
        List<BizUserMessage> list = bizUserMessageService.selectBizUserMessageList(bizUserMessage);
        return getDataTable(list);
    }

    /**
     * 导出用户消息列表
     */
    @PreAuthorize("@ss.hasPermi('miniapp:message:export')")
    @Log(title = "用户消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizUserMessage bizUserMessage)
    {
        List<BizUserMessage> list = bizUserMessageService.selectBizUserMessageList(bizUserMessage);
        ExcelUtil<BizUserMessage> util = new ExcelUtil<BizUserMessage>(BizUserMessage.class);
        util.exportExcel(response, list, "用户消息数据");
    }

    /**
     * 获取用户消息详细信息
     */
    @PreAuthorize("@ss.hasPermi('miniapp:message:query')")
    @GetMapping(value = "/{messageId}")
    public AjaxResult getInfo(@PathVariable("messageId") Long messageId)
    {
        return success(bizUserMessageService.selectBizUserMessageById(messageId));
    }

    /**
     * 新增/发送用户消息
     */
    @PreAuthorize("@ss.hasPermi('miniapp:message:add')")
    @Log(title = "用户消息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizUserMessage bizUserMessage)
    {
        String username = getUsername();
        bizUserMessage.setCreateBy(username);
        bizUserMessage.setSendBy(username);
        return toAjax(bizUserMessageService.insertBizUserMessage(bizUserMessage));
    }

    /**
     * 删除用户消息
     */
    @PreAuthorize("@ss.hasPermi('miniapp:message:remove')")
    @Log(title = "用户消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{messageIds}")
    public AjaxResult remove(@PathVariable Long[] messageIds)
    {
        return toAjax(bizUserMessageService.deleteBizUserMessageByIds(messageIds));
    }
}
