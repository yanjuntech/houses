package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizUserFavorite;

/**
 * 用户收藏 数据层
 *
 * @author ruoyi
 */
public interface BizUserFavoriteMapper
{
    /**
     * 查询用户收藏列表
     *
     * @param bizUserFavorite 用户收藏
     * @return 用户收藏集合
     */
    public List<BizUserFavorite> selectBizUserFavoriteList(BizUserFavorite bizUserFavorite);

    /**
     * 通过收藏ID查询用户收藏
     *
     * @param favoriteId 收藏ID
     * @return 用户收藏
     */
    public BizUserFavorite selectBizUserFavoriteByFavoriteId(Long favoriteId);

    /**
     * 新增用户收藏
     *
     * @param bizUserFavorite 用户收藏
     * @return 结果
     */
    public int insertBizUserFavorite(BizUserFavorite bizUserFavorite);

    /**
     * 修改用户收藏
     *
     * @param bizUserFavorite 用户收藏
     * @return 结果
     */
    public int updateBizUserFavorite(BizUserFavorite bizUserFavorite);

    /**
     * 通过收藏ID删除用户收藏
     *
     * @param favoriteId 收藏ID
     * @return 结果
     */
    public int deleteBizUserFavoriteByFavoriteId(Long favoriteId);

    /**
     * 批量删除用户收藏
     *
     * @param favoriteIds 需要删除的收藏ID数组
     * @return 结果
     */
    public int deleteBizUserFavoriteByFavoriteIds(Long[] favoriteIds);

    /**
     * 按用户ID和房源ID查询收藏（用于判断是否已收藏）
     *
     * @param bizUserFavorite 用户收藏（包含 userId、houseId）
     * @return 用户收藏
     */
    public BizUserFavorite selectByUserIdAndHouseId(BizUserFavorite bizUserFavorite);

    /**
     * 按用户ID和房源ID删除收藏（取消收藏）
     *
     * @param bizUserFavorite 用户收藏（包含 userId、houseId）
     * @return 结果
     */
    public int deleteByUserIdAndHouseId(BizUserFavorite bizUserFavorite);
}
