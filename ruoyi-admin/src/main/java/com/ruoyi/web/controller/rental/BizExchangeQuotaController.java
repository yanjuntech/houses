package com.ruoyi.web.controller.rental;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.BizExchangeQuota;
import com.ruoyi.system.service.IBizExchangeQuotaService;

/**
 * 用户兑换配额操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/exchangeQuota")
public class BizExchangeQuotaController extends BaseController
{
    @Autowired
    private IBizExchangeQuotaService bizExchangeQuotaService;

    /**
     * 获取用户兑换配额列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BizExchangeQuota bizExchangeQuota)
    {
        startPage();
        List<BizExchangeQuota> list = bizExchangeQuotaService.selectBizExchangeQuotaList(bizExchangeQuota);
        return getDataTable(list);
    }

    /**
     * 查询指定用户的所有配额
     */
    @GetMapping("/userQuota/{userId}")
    public AjaxResult userQuota(@PathVariable Long userId)
    {
        BizExchangeQuota query = new BizExchangeQuota();
        query.setUserId(userId);
        List<BizExchangeQuota> list = bizExchangeQuotaService.selectBizExchangeQuotaList(query);
        return success(list);
    }

    /**
     * 扣减配额接口（供房屋批量导入调用）
     * 参数：userId、quotaType、count
     * 校验剩余配额是否足够，不足返回错误
     */
    @PostMapping("/deduct")
    public AjaxResult deduct(@RequestParam Long userId,
                             @RequestParam String quotaType,
                             @RequestParam int count)
    {
        return toAjax(bizExchangeQuotaService.deductQuota(userId, quotaType, count));
    }
}
