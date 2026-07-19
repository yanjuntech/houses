package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.BizBanner;
import com.ruoyi.system.mapper.BizBannerMapper;
import com.ruoyi.system.service.IBizBannerService;

/**
 * 轮播图 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizBannerServiceImpl implements IBizBannerService
{
    @Autowired
    private BizBannerMapper bizBannerMapper;

    /**
     * 查询轮播图列表
     *
     * @param bizBanner 轮播图
     * @return 轮播图集合
     */
    @Override
    public List<BizBanner> selectBizBannerList(BizBanner bizBanner)
    {
        return bizBannerMapper.selectBizBannerList(bizBanner);
    }

    /**
     * 通过轮播图ID查询轮播图
     *
     * @param bannerId 轮播图ID
     * @return 轮播图
     */
    @Override
    public BizBanner selectBizBannerById(Long bannerId)
    {
        return bizBannerMapper.selectBizBannerById(bannerId);
    }

    /**
     * 新增轮播图
     *
     * @param bizBanner 轮播图
     * @return 结果
     */
    @Override
    public int insertBizBanner(BizBanner bizBanner)
    {
        return bizBannerMapper.insertBizBanner(bizBanner);
    }

    /**
     * 修改轮播图
     *
     * @param bizBanner 轮播图
     * @return 结果
     */
    @Override
    public int updateBizBanner(BizBanner bizBanner)
    {
        return bizBannerMapper.updateBizBanner(bizBanner);
    }

    /**
     * 删除轮播图
     *
     * @param bannerId 轮播图ID
     * @return 结果
     */
    @Override
    public int deleteBizBannerById(Long bannerId)
    {
        return bizBannerMapper.deleteBizBannerById(bannerId);
    }

    /**
     * 批量删除轮播图
     *
     * @param bannerIds 需要删除的轮播图ID数组
     * @return 结果
     */
    @Override
    public int deleteBizBannerByIds(Long[] bannerIds)
    {
        return bizBannerMapper.deleteBizBannerByIds(bannerIds);
    }

    /**
     * 查询有效轮播图列表（有效期内且状态正常）
     *
     * @return 轮播图集合
     */
    @Override
    public List<BizBanner> selectValidBannerList()
    {
        return bizBannerMapper.selectValidBannerList();
    }
}
