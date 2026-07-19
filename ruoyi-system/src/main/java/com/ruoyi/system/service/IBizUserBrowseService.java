package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizUserBrowse;

/**
 * 用户浏览记录 服务层
 *
 * @author ruoyi
 */
public interface IBizUserBrowseService
{
    /**
     * 查询用户浏览记录集合
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
     * 通过浏览记录ID删除用户浏览记录
     *
     * @param browseId 浏览记录ID
     * @return 结果
     */
    public int deleteBizUserBrowseByBrowseId(Long browseId);

    /**
     * 记录浏览（核心方法）
     * 去重逻辑：
     * 1. 查询最近一条浏览记录（同一用户同一房源）
     * 2. 如果存在且 browse_time 在 1 小时内，则只更新 browse_time
     * 3. 否则插入新记录
     *
     * @param userId     用户ID
     * @param houseId    房源ID
     * @param houseTitle 房源标题
     * @return 结果
     */
    public int recordBrowse(Long userId, Long houseId, String houseTitle);
}
