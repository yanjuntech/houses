package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizHouse;

/**
 * 房屋信息 数据层
 *
 * @author ruoyi
 */
public interface BizHouseMapper
{
    /**
     * 查询房屋信息列表
     *
     * @param bizHouse 房屋信息
     * @return 房屋信息集合
     */
    public List<BizHouse> selectBizHouseList(BizHouse bizHouse);

    /**
     * 通过房屋ID查询房屋信息
     *
     * @param houseId 房屋ID
     * @return 房屋信息
     */
    public BizHouse selectBizHouseByHouseId(Long houseId);

    /**
     * 新增房屋信息
     *
     * @param bizHouse 房屋信息
     * @return 结果
     */
    public int insertBizHouse(BizHouse bizHouse);

    /**
     * 修改房屋信息
     *
     * @param bizHouse 房屋信息
     * @return 结果
     */
    public int updateBizHouse(BizHouse bizHouse);

    /**
     * 删除房屋信息
     *
     * @param houseId 房屋ID
     * @return 结果
     */
    public int deleteBizHouseByHouseId(Long houseId);

    /**
     * 批量删除房屋信息
     *
     * @param houseIds 需要删除的房屋ID数组
     * @return 结果
     */
    public int deleteBizHouseByHouseIds(Long[] houseIds);

    /**
     * 修改房屋上下架状态
     *
     * @param bizHouse 房屋信息
     * @return 结果
     */
    public int updateBizHouseStatus(BizHouse bizHouse);
}
