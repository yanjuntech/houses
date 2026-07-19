package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.BizUserBrowse;
import com.ruoyi.system.mapper.BizUserBrowseMapper;
import com.ruoyi.system.service.IBizUserBrowseService;

/**
 * 用户浏览记录 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizUserBrowseServiceImpl implements IBizUserBrowseService
{
    /** 浏览去重时间窗口（毫秒）：1 小时 */
    private static final long DEDUP_TIME_WINDOW_MS = 60 * 60 * 1000L;

    @Autowired
    private BizUserBrowseMapper bizUserBrowseMapper;

    /**
     * 查询用户浏览记录集合
     *
     * @param bizUserBrowse 用户浏览记录
     * @return 用户浏览记录集合
     */
    @Override
    public List<BizUserBrowse> selectBizUserBrowseList(BizUserBrowse bizUserBrowse)
    {
        return bizUserBrowseMapper.selectBizUserBrowseList(bizUserBrowse);
    }

    /**
     * 通过浏览记录ID查询用户浏览记录
     *
     * @param browseId 浏览记录ID
     * @return 用户浏览记录
     */
    @Override
    public BizUserBrowse selectBizUserBrowseByBrowseId(Long browseId)
    {
        return bizUserBrowseMapper.selectBizUserBrowseByBrowseId(browseId);
    }

    /**
     * 新增用户浏览记录
     *
     * @param bizUserBrowse 用户浏览记录
     * @return 结果
     */
    @Override
    public int insertBizUserBrowse(BizUserBrowse bizUserBrowse)
    {
        return bizUserBrowseMapper.insertBizUserBrowse(bizUserBrowse);
    }

    /**
     * 通过浏览记录ID删除用户浏览记录
     *
     * @param browseId 浏览记录ID
     * @return 结果
     */
    @Override
    public int deleteBizUserBrowseByBrowseId(Long browseId)
    {
        return bizUserBrowseMapper.deleteBizUserBrowseByBrowseId(browseId);
    }

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
    @Override
    public int recordBrowse(Long userId, Long houseId, String houseTitle)
    {
        BizUserBrowse query = new BizUserBrowse();
        query.setUserId(userId);
        query.setHouseId(houseId);
        BizUserBrowse latest = bizUserBrowseMapper.selectLatestByUserIdAndHouseId(query);

        Date now = new Date();
        // 1 小时内有浏览记录，则只更新 browse_time
        if (latest != null && latest.getBrowseTime() != null
                && (now.getTime() - latest.getBrowseTime().getTime()) < DEDUP_TIME_WINDOW_MS)
        {
            latest.setBrowseTime(now);
            latest.setHouseTitle(houseTitle);
            return bizUserBrowseMapper.updateBizUserBrowse(latest);
        }
        // 否则插入新记录
        BizUserBrowse browse = new BizUserBrowse();
        browse.setUserId(userId);
        browse.setHouseId(houseId);
        browse.setHouseTitle(houseTitle);
        browse.setBrowseTime(now);
        return bizUserBrowseMapper.insertBizUserBrowse(browse);
    }
}
