package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizMallProduct;

/**
 * 商城商品 数据层
 *
 * @author ruoyi
 */
public interface BizMallProductMapper
{
    /**
     * 查询商城商品列表
     *
     * @param bizMallProduct 商城商品
     * @return 商城商品集合
     */
    public List<BizMallProduct> selectBizMallProductList(BizMallProduct bizMallProduct);

    /**
     * 通过商品ID查询商城商品
     *
     * @param productId 商品ID
     * @return 商城商品对象信息
     */
    public BizMallProduct selectBizMallProductByProductId(Long productId);

    /**
     * 新增商城商品
     *
     * @param bizMallProduct 商城商品
     * @return 结果
     */
    public int insertBizMallProduct(BizMallProduct bizMallProduct);

    /**
     * 修改商城商品
     *
     * @param bizMallProduct 商城商品
     * @return 结果
     */
    public int updateBizMallProduct(BizMallProduct bizMallProduct);

    /**
     * 删除商城商品
     *
     * @param productId 商品ID
     * @return 结果
     */
    public int deleteBizMallProductByProductId(Long productId);

    /**
     * 批量删除商城商品
     *
     * @param productIds 需要删除的商品ID
     * @return 结果
     */
    public int deleteBizMallProductByProductIds(Long[] productIds);

    /**
     * 修改商品上下架状态
     *
     * @param bizMallProduct 商城商品
     * @return 结果
     */
    public int updateBizMallProductStatus(BizMallProduct bizMallProduct);
}
