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
import com.ruoyi.system.domain.BizMallExchangeRecord;
import com.ruoyi.system.service.IBizMallExchangeRecordService;

/**
 * 兑换记录操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/mallRecord")
public class BizMallExchangeRecordController extends BaseController
{
    @Autowired
    private IBizMallExchangeRecordService bizMallExchangeRecordService;

    /**
     * 获取兑换记录列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:mallRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizMallExchangeRecord bizMallExchangeRecord)
    {
        startPage();
        List<BizMallExchangeRecord> list = bizMallExchangeRecordService.selectBizMallExchangeRecordList(bizMallExchangeRecord);
        return getDataTable(list);
    }

    /**
     * 导出兑换记录列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:mallRecord:export')")
    @Log(title = "兑换记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizMallExchangeRecord bizMallExchangeRecord)
    {
        List<BizMallExchangeRecord> list = bizMallExchangeRecordService.selectBizMallExchangeRecordList(bizMallExchangeRecord);
        ExcelUtil<BizMallExchangeRecord> util = new ExcelUtil<BizMallExchangeRecord>(BizMallExchangeRecord.class);
        util.exportExcel(response, list, "兑换记录数据");
    }

    /**
     * 根据兑换记录ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:mallRecord:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable Long recordId)
    {
        return success(bizMallExchangeRecordService.selectBizMallExchangeRecordByRecordId(recordId));
    }

    /**
     * 用户兑换商品（公开接口）
     */
    @Anonymous
    @Log(title = "兑换记录", businessType = BusinessType.INSERT)
    @PostMapping("/exchange")
    public AjaxResult exchange(@RequestBody BizMallExchangeRecord bizMallExchangeRecord)
    {
        return toAjax(bizMallExchangeRecordService.exchange(bizMallExchangeRecord));
    }

    /**
     * 查询某用户的兑换记录
     */
    @GetMapping("/userRecord/{userId}")
    public AjaxResult userRecord(@PathVariable Long userId)
    {
        return success(bizMallExchangeRecordService.selectBizMallExchangeRecordByUserId(userId));
    }

    /**
     * 修改兑换记录（如失效生效状态）
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:mallRecord:edit')")
    @Log(title = "兑换记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizMallExchangeRecord bizMallExchangeRecord)
    {
        bizMallExchangeRecord.setUpdateBy(getUsername());
        return toAjax(bizMallExchangeRecordService.updateBizMallExchangeRecord(bizMallExchangeRecord));
    }

    /**
     * 删除兑换记录
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:mallRecord:remove')")
    @Log(title = "兑换记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(bizMallExchangeRecordService.deleteBizMallExchangeRecordByRecordIds(recordIds));
    }
}
