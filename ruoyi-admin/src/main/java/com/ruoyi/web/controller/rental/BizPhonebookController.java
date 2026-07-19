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
import com.ruoyi.system.domain.BizPhonebook;
import com.ruoyi.system.service.IBizPhonebookService;

/**
 * 电话簿信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/phonebook")
public class BizPhonebookController extends BaseController
{
    @Autowired
    private IBizPhonebookService bizPhonebookService;

    /**
     * 获取电话簿列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:phonebook:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizPhonebook bizPhonebook)
    {
        startPage();
        List<BizPhonebook> list = bizPhonebookService.selectBizPhonebookList(bizPhonebook);
        return getDataTable(list);
    }

    /**
     * 导出电话簿列表
     */
    @Log(title = "电话簿管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('biz:rental:phonebook:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizPhonebook bizPhonebook)
    {
        List<BizPhonebook> list = bizPhonebookService.selectBizPhonebookList(bizPhonebook);
        ExcelUtil<BizPhonebook> util = new ExcelUtil<BizPhonebook>(BizPhonebook.class);
        util.exportExcel(response, list, "电话簿数据");
    }

    /**
     * 根据电话簿编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:phonebook:query')")
    @GetMapping(value = "/{phonebookId}")
    public AjaxResult getInfo(@PathVariable Long phonebookId)
    {
        return success(bizPhonebookService.selectBizPhonebookById(phonebookId));
    }

    /**
     * 新增电话簿
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:phonebook:add')")
    @Log(title = "电话簿管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizPhonebook bizPhonebook)
    {
        // 校验电话：phone1 必填，phone2 可选，最多 2 个电话
        String errorMsg = validatePhone(bizPhonebook);
        if (errorMsg != null)
        {
            return error(errorMsg);
        }
        bizPhonebook.setCreateBy(getUsername());
        return toAjax(bizPhonebookService.insertBizPhonebook(bizPhonebook));
    }

    /**
     * 修改电话簿
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:phonebook:edit')")
    @Log(title = "电话簿管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizPhonebook bizPhonebook)
    {
        // 校验电话：phone1 必填，phone2 可选，最多 2 个电话
        String errorMsg = validatePhone(bizPhonebook);
        if (errorMsg != null)
        {
            return error(errorMsg);
        }
        bizPhonebook.setUpdateBy(getUsername());
        return toAjax(bizPhonebookService.updateBizPhonebook(bizPhonebook));
    }

    /**
     * 删除电话簿
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:phonebook:remove')")
    @Log(title = "电话簿管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{phonebookIds}")
    public AjaxResult remove(@PathVariable Long[] phonebookIds)
    {
        return toAjax(bizPhonebookService.deleteBizPhonebookByIds(phonebookIds));
    }

    /**
     * 获取全部电话簿下拉选择列表（公开接口）
     */
    @Anonymous
    @GetMapping("/selectAll")
    public AjaxResult selectAll()
    {
        BizPhonebook query = new BizPhonebook();
        query.setStatus("0");
        List<BizPhonebook> list = bizPhonebookService.selectBizPhonebookList(query);
        return success(list);
    }

    /**
     * 校验电话格式：支持 11 位手机号或座机格式（区号-号码）
     * @param phone 电话号码
     * @return true=格式正确，false=格式错误
     */
    private boolean isValidPhone(String phone)
    {
        if (phone == null || phone.trim().isEmpty())
        {
            return false;
        }
        // 11 位手机号 或 座机格式（3-4 位区号-7-8 位号码）
        return phone.matches("^1[3-9]\\d{9}$") || phone.matches("^\\d{3,4}-\\d{7,8}$");
    }

    /**
     * 校验电话簿电话字段：phone1 必填，phone2 可选，最多 2 个电话
     * @param bizPhonebook 电话簿对象
     * @return 错误信息，null 表示校验通过
     */
    private String validatePhone(BizPhonebook bizPhonebook)
    {
        // phone1 必填
        if (bizPhonebook.getPhone1() == null || bizPhonebook.getPhone1().trim().isEmpty())
        {
            return "联系电话1为必填项";
        }
        if (!isValidPhone(bizPhonebook.getPhone1()))
        {
            return "联系电话1格式不正确（需为11位手机号或座机格式，如010-12345678）";
        }
        // phone2 可选，但若填写则校验格式
        if (bizPhonebook.getPhone2() != null && !bizPhonebook.getPhone2().trim().isEmpty())
        {
            if (!isValidPhone(bizPhonebook.getPhone2()))
            {
                return "联系电话2格式不正确（需为11位手机号或座机格式，如010-12345678）";
            }
        }
        // 最多 2 个电话已由字段数（phone1、phone2）限制
        return null;
    }
}
