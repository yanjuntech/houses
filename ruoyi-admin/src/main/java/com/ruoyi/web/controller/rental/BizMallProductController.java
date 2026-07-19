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
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizMallProduct;
import com.ruoyi.system.service.IBizMallProductService;

/**
 * 商城商品操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/mallProduct")
public class BizMallProductController extends BaseController
{
    @Autowired
    private IBizMallProductService bizMallProductService;

    /**
     * 获取商城商品列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:mallProduct:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizMallProduct bizMallProduct)
    {
        startPage();
        List<BizMallProduct> list = bizMallProductService.selectBizMallProductList(bizMallProduct);
        return getDataTable(list);
    }

    /**
     * 导出商城商品列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:mallProduct:export')")
    @Log(title = "商城商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizMallProduct bizMallProduct)
    {
        List<BizMallProduct> list = bizMallProductService.selectBizMallProductList(bizMallProduct);
        ExcelUtil<BizMallProduct> util = new ExcelUtil<BizMallProduct>(BizMallProduct.class);
        util.exportExcel(response, list, "商城商品数据");
    }

    /**
     * 根据商品ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:mallProduct:query')")
    @GetMapping(value = "/{productId}")
    public AjaxResult getInfo(@PathVariable Long productId)
    {
        return success(bizMallProductService.selectBizMallProductByProductId(productId));
    }

    /**
     * 新增商城商品
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:mallProduct:add')")
    @Log(title = "商城商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizMallProduct bizMallProduct)
    {
        bizMallProduct.setCreateBy(getUsername());
        return toAjax(bizMallProductService.insertBizMallProduct(bizMallProduct));
    }

    /**
     * 修改商城商品
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:mallProduct:edit')")
    @Log(title = "商城商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizMallProduct bizMallProduct)
    {
        bizMallProduct.setUpdateBy(getUsername());
        return toAjax(bizMallProductService.updateBizMallProduct(bizMallProduct));
    }

    /**
     * 删除商城商品
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:mallProduct:remove')")
    @Log(title = "商城商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{productIds}")
    public AjaxResult remove(@PathVariable Long[] productIds)
    {
        return toAjax(bizMallProductService.deleteBizMallProductByProductIds(productIds));
    }

    /**
     * 修改商品上下架状态
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:mallProduct:edit')")
    @Log(title = "商城商品", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody BizMallProduct bizMallProduct)
    {
        bizMallProduct.setUpdateBy(getUsername());
        return toAjax(bizMallProductService.updateBizMallProductStatus(bizMallProduct));
    }

    /**
     * 获取全部上架商品下拉列表（公开接口）
     */
    @Anonymous
    @GetMapping("/selectAll")
    public AjaxResult selectAll()
    {
        BizMallProduct query = new BizMallProduct();
        query.setStatus("0");
        List<BizMallProduct> list = bizMallProductService.selectBizMallProductList(query);
        return success(list);
    }
}
