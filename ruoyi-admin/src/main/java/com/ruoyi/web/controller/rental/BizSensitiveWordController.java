package com.ruoyi.web.controller.rental;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
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
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizSensitiveWord;
import com.ruoyi.system.service.IBizSensitiveWordService;

/**
 * 敏感词库操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/sensitive")
public class BizSensitiveWordController extends BaseController
{
    @Autowired
    private IBizSensitiveWordService bizSensitiveWordService;

    /**
     * 获取敏感词列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:sensitive:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizSensitiveWord bizSensitiveWord)
    {
        startPage();
        List<BizSensitiveWord> list = bizSensitiveWordService.selectBizSensitiveWordList(bizSensitiveWord);
        return getDataTable(list);
    }

    /**
     * 导出敏感词列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:sensitive:export')")
    @Log(title = "敏感词库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizSensitiveWord bizSensitiveWord)
    {
        List<BizSensitiveWord> list = bizSensitiveWordService.selectBizSensitiveWordList(bizSensitiveWord);
        ExcelUtil<BizSensitiveWord> util = new ExcelUtil<BizSensitiveWord>(BizSensitiveWord.class);
        util.exportExcel(response, list, "敏感词数据");
    }

    /**
     * 根据敏感词ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:sensitive:query')")
    @GetMapping(value = "/{wordId}")
    public AjaxResult getInfo(@PathVariable Long wordId)
    {
        return success(bizSensitiveWordService.selectBizSensitiveWordByWordId(wordId));
    }

    /**
     * 新增敏感词
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:sensitive:add')")
    @Log(title = "敏感词库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizSensitiveWord bizSensitiveWord)
    {
        bizSensitiveWord.setCreateBy(getUsername());
        return toAjax(bizSensitiveWordService.insertBizSensitiveWord(bizSensitiveWord));
    }

    /**
     * 修改敏感词
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:sensitive:edit')")
    @Log(title = "敏感词库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizSensitiveWord bizSensitiveWord)
    {
        bizSensitiveWord.setUpdateBy(getUsername());
        return toAjax(bizSensitiveWordService.updateBizSensitiveWord(bizSensitiveWord));
    }

    /**
     * 删除敏感词
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:sensitive:remove')")
    @Log(title = "敏感词库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{wordIds}")
    public AjaxResult remove(@PathVariable Long[] wordIds)
    {
        return toAjax(bizSensitiveWordService.deleteBizSensitiveWordByWordIds(wordIds));
    }
}
