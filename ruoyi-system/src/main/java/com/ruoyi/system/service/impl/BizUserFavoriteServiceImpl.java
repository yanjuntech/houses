package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.BizUserFavorite;
import com.ruoyi.system.mapper.BizUserFavoriteMapper;
import com.ruoyi.system.service.IBizUserFavoriteService;

/**
 * 用户收藏 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizUserFavoriteServiceImpl implements IBizUserFavoriteService
{
    @Autowired
    private BizUserFavoriteMapper bizUserFavoriteMapper;

    /**
     * 查询用户收藏集合
     *
     * @param bizUserFavorite 用户收藏
     * @return 用户收藏集合
     */
    @Override
    public List<BizUserFavorite> selectBizUserFavoriteList(BizUserFavorite bizUserFavorite)
    {
        return bizUserFavoriteMapper.selectBizUserFavoriteList(bizUserFavorite);
    }

    /**
     * 通过收藏ID查询用户收藏
     *
     * @param favoriteId 收藏ID
     * @return 用户收藏
     */
    @Override
    public BizUserFavorite selectBizUserFavoriteByFavoriteId(Long favoriteId)
    {
        return bizUserFavoriteMapper.selectBizUserFavoriteByFavoriteId(favoriteId);
    }

    /**
     * 新增用户收藏（收藏房源）
     * 已存在收藏则不再重复添加
     *
     * @param bizUserFavorite 用户收藏
     * @return 结果
     */
    @Override
    public int insertBizUserFavorite(BizUserFavorite bizUserFavorite)
    {
        if (bizUserFavorite.getUserId() == null || bizUserFavorite.getHouseId() == null)
        {
            throw new ServiceException("用户ID和房源ID不能为空");
        }
        BizUserFavorite exist = bizUserFavoriteMapper.selectByUserIdAndHouseId(bizUserFavorite);
        if (exist != null)
        {
            return 1;
        }
        return bizUserFavoriteMapper.insertBizUserFavorite(bizUserFavorite);
    }

    /**
     * 修改用户收藏
     *
     * @param bizUserFavorite 用户收藏
     * @return 结果
     */
    @Override
    public int updateBizUserFavorite(BizUserFavorite bizUserFavorite)
    {
        return bizUserFavoriteMapper.updateBizUserFavorite(bizUserFavorite);
    }

    /**
     * 通过收藏ID删除用户收藏
     *
     * @param favoriteId 收藏ID
     * @return 结果
     */
    @Override
    public int deleteBizUserFavoriteByFavoriteId(Long favoriteId)
    {
        return bizUserFavoriteMapper.deleteBizUserFavoriteByFavoriteId(favoriteId);
    }

    /**
     * 批量删除用户收藏
     *
     * @param favoriteIds 需要删除的收藏ID数组
     * @return 结果
     */
    @Override
    public int deleteBizUserFavoriteByFavoriteIds(Long[] favoriteIds)
    {
        return bizUserFavoriteMapper.deleteBizUserFavoriteByFavoriteIds(favoriteIds);
    }

    /**
     * 取消收藏（按用户ID和房源ID）
     *
     * @param userId  用户ID
     * @param houseId 房源ID
     * @return 结果
     */
    @Override
    public int cancelFavorite(Long userId, Long houseId)
    {
        BizUserFavorite favorite = new BizUserFavorite();
        favorite.setUserId(userId);
        favorite.setHouseId(houseId);
        return bizUserFavoriteMapper.deleteByUserIdAndHouseId(favorite);
    }
}
