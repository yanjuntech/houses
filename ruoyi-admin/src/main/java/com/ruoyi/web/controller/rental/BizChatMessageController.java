package com.ruoyi.web.controller.rental;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizChatMessage;
import com.ruoyi.system.service.IBizChatMessageService;

/**
 * 聊天消息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/message")
public class BizChatMessageController extends BaseController
{
    @Autowired
    private IBizChatMessageService bizChatMessageService;

    /**
     * 获取聊天消息列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:message:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizChatMessage bizChatMessage)
    {
        startPage();
        List<BizChatMessage> list = bizChatMessageService.selectBizChatMessageList(bizChatMessage);
        return getDataTable(list);
    }

    /**
     * 导出聊天消息列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:message:export')")
    @Log(title = "聊天消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizChatMessage bizChatMessage)
    {
        List<BizChatMessage> list = bizChatMessageService.selectBizChatMessageList(bizChatMessage);
        ExcelUtil<BizChatMessage> util = new ExcelUtil<BizChatMessage>(BizChatMessage.class);
        util.exportExcel(response, list, "聊天消息数据");
    }

    /**
     * 根据消息ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:message:query')")
    @GetMapping(value = "/{messageId}")
    public AjaxResult getInfo(@PathVariable Long messageId)
    {
        return success(bizChatMessageService.selectBizChatMessageByMessageId(messageId));
    }

    /**
     * 发送消息（公开接口）
     */
    @Anonymous
    @Log(title = "聊天消息", businessType = BusinessType.INSERT)
    @PostMapping("/send")
    public AjaxResult send(@RequestBody BizChatMessage bizChatMessage)
    {
        if (bizChatMessage.getSenderId() == null || bizChatMessage.getReceiverId() == null)
        {
            return error("发送方和接收方不能为空");
        }
        int rows = bizChatMessageService.sendMessage(bizChatMessage);
        if (rows == 0 && "1".equals(bizChatMessage.getIsFiltered()))
        {
            return error(bizChatMessage.getFilterReason());
        }
        return toAjax(rows);
    }

    /**
     * 查询聊天记录（公开接口）
     */
    @Anonymous
    @GetMapping("/history")
    public AjaxResult history(@RequestParam Long userId1, @RequestParam Long userId2)
    {
        if (userId1 == null || userId2 == null)
        {
            return error("用户ID不能为空");
        }
        return success(bizChatMessageService.getChatHistory(userId1, userId2));
    }

    /**
     * 标记已读（公开接口）
     */
    @Anonymous
    @Log(title = "聊天消息", businessType = BusinessType.UPDATE)
    @PutMapping("/markAsRead/{messageId}")
    public AjaxResult markAsRead(@PathVariable Long messageId)
    {
        return toAjax(bizChatMessageService.markAsRead(messageId));
    }
}
