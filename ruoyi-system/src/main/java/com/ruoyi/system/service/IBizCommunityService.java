package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizCommunity;

/**
 * 小区信息 服务层
 *
 * @author ruoyi
 */
public interface IBizCommunityService
{
    /**
     * 查询小区信息集合
     *
     * @param bizCommunity 小区信息
     * @return 小区信息集合
     */
    public List<BizCommunity> selectBizCommunityList(BizCommunity bizCommunity);

    /**
     * 通过小区ID查询小区信息
     *
     * @param communityId 小区ID
     * @return 小区对象信息
     */
    public BizCommunity selectBizCommunityByCommunityId(Long communityId);

    /**
     * 新增小区信息
     *
     * @param bizCommunity 小区信息
     * @return 结果
     */
    public int insertBizCommunity(BizCommunity bizCommunity);

    /**
     * 修改小区信息
     *
     * @param bizCommunity 小区信息
     * @return 结果
     */
    public int updateBizCommunity(BizCommunity bizCommunity);

    /**
     * 删除小区信息
     *
     * @param communityId 小区ID
     * @return 结果
     */
    public int deleteBizCommunityByCommunityId(Long communityId);

    /**
     * 批量删除小区信息
     *
     * @param communityIds 需要删除的小区ID
     * @return 结果
     */
    public int deleteBizCommunityByCommunityIds(Long[] communityIds);
}
