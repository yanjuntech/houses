package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.BizMallProduct;
import com.ruoyi.system.mapper.BizMallProductMapper;
import com.ruoyi.system.service.IBizMallProductService;

/**
 * 商城商品 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizMallProductServiceImpl implements IBizMallProductService
{
    @Autowired
    private BizMallProductMapper bizMallProductMapper;

    /**
     * 查询商城商品集合
     *
     * @param bizMallProduct 商城商品
     * @return 商城商品集合
     */
    @Override
    public List<BizMallProduct> selectBizMallProductList(BizMallProduct bizMallProduct)
    {
        return bizMallProductMapper.selectBizMallProductList(bizMallProduct);
    }

    /**
     * 通过商品ID查询商城商品
     *
     * @param productId 商品ID
     * @return 商城商品对象信息
     */
    @Override
    public BizMallProduct selectBizMallProductByProductId(Long productId)
    {
        return bizMallProductMapper.selectBizMallProductByProductId(productId);
    }

    /**
     * 新增商城商品
     *
     * @param bizMallProduct 商城商品
     * @return 结果
     */
    @Override
    public int insertBizMallProduct(BizMallProduct bizMallProduct)
    {
        return bizMallProductMapper.insertBizMallProduct(bizMallProduct);
    }

    /**
     * 修改商城商品
     *
     * @param bizMallProduct 商城商品
     * @return 结果
     */
    @Override
    public int updateBizMallProduct(BizMallProduct bizMallProduct)
    {
        return bizMallProductMapper.updateBizMallProduct(bizMallProduct);
    }

    /**
     * 删除商城商品
     *
     * @param productId 商品ID
     * @return 结果
     */
    @Override
    public int deleteBizMallProductByProductId(Long productId)
    {
        return bizMallProductMapper.deleteBizMallProductByProductId(productId);
    }

    /**
     * 批量删除商城商品
     *
     * @param productIds 需要删除的商品ID
     * @return 结果
     */
    @Override
    public int deleteBizMallProductByProductIds(Long[] productIds)
    {
        return bizMallProductMapper.deleteBizMallProductByProductIds(productIds);
    }

    /**
     * 修改商品上下架状态
     *
     * @param bizMallProduct 商城商品
     * @return 结果
     */
    @Override
    public int updateBizMallProductStatus(BizMallProduct bizMallProduct)
    {
        return bizMallProductMapper.updateBizMallProductStatus(bizMallProduct);
    }
}
