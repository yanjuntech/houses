package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizUserBrowse;

/**
 * 用户浏览记录 数据层
 *
 * @author ruoyi
 */
public interface BizUserBrowseMapper
{
    /**
     * 查询用户浏览记录列表
     *
     * @param bizUserBrowse 用户浏览记录
     * @return 用户浏览记录集合
     */
    public List<BizUserBrowse> selectBizUserBrowseList(BizUserBrowse bizUserBrowse);

    /**
     * 通过浏览记录ID查询用户浏览记录
     *
     * @param browseId 浏览记录ID
     * @return 用户浏览记录
     */
    public BizUserBrowse selectBizUserBrowseByBrowseId(Long browseId);

    /**
     * 新增用户浏览记录
     *
     * @param bizUserBrowse 用户浏览记录
     * @return 结果
     */
    public int insertBizUserBrowse(BizUserBrowse bizUserBrowse);

    /**
     * 修改用户浏览记录（更新浏览时间）
     *
     * @param bizUserBrowse 用户浏览记录
     * @return 结果
     */
    public int updateBizUserBrowse(BizUserBrowse bizUserBrowse);

    /**
     * 通过浏览记录ID删除用户浏览记录
     *
     * @param browseId 浏览记录ID
     * @return 结果
     */
    public int deleteBizUserBrowseByBrowseId(Long browseId);

    /**
     * 按用户ID和房源ID删除浏览记录
     *
     * @param bizUserBrowse 用户浏览记录（包含 userId、houseId）
     * @return 结果
     */
    public int deleteByUserIdAndHouseId(BizUserBrowse bizUserBrowse);

    /**
     * 查询某用户某房源最近一条浏览记录
     *
     * @param bizUserBrowse 用户浏览记录（包含 userId、houseId）
     * @return 用户浏览记录
     */
    public BizUserBrowse selectLatestByUserIdAndHouseId(BizUserBrowse bizUserBrowse);
}
