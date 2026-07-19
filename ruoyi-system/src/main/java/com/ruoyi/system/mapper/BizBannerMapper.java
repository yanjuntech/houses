package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizBanner;

/**
 * 轮播图 数据层
 *
 * @author ruoyi
 */
public interface BizBannerMapper
{
    /**
     * 查询轮播图列表
     *
     * @param bizBanner 轮播图
     * @return 轮播图集合
     */
    public List<BizBanner> selectBizBannerList(BizBanner bizBanner);

    /**
     * 通过轮播图ID查询轮播图
     *
     * @param bannerId 轮播图ID
     * @return 轮播图
     */
    public BizBanner selectBizBannerById(Long bannerId);

    /**
     * 新增轮播图
     *
     * @param bizBanner 轮播图
     * @return 结果
     */
    public int insertBizBanner(BizBanner bizBanner);

    /**
     * 修改轮播图
     *
     * @param bizBanner 轮播图
     * @return 结果
     */
    public int updateBizBanner(BizBanner bizBanner);

    /**
     * 删除轮播图
     *
     * @param bannerId 轮播图ID
     * @return 结果
     */
    public int deleteBizBannerById(Long bannerId);

    /**
     * 批量删除轮播图
     *
     * @param bannerIds 需要删除的轮播图ID数组
     * @return 结果
     */
    public int deleteBizBannerByIds(Long[] bannerIds);

    /**
     * 查询有效轮播图列表（有效期内且状态正常）
     *
     * @return 轮播图集合
     */
    public List<BizBanner> selectValidBannerList();
}
