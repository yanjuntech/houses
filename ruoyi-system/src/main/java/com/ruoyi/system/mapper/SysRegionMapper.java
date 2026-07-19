package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysRegion;

/**
 * 行政区划 数据层
 * 
 * @author ruoyi
 */
public interface SysRegionMapper
{
    /**
     * 查询行政区划列表
     * 
     * @param region 行政区划信息
     * @return 行政区划集合
     */
    public List<SysRegion> selectRegionList(SysRegion region);

    /**
     * 根据区划ID查询信息
     * 
     * @param regionId 区划ID
     * @return 行政区划信息
     */
    public SysRegion selectRegionById(Long regionId);

    /**
     * 根据ID查询所有子区划
     * 
     * @param regionId 区划ID
     * @return 区划列表
     */
    public List<SysRegion> selectChildrenRegionById(Long regionId);

    /**
     * 根据父ID查询子区划列表
     * 
     * @param parentId 父级ID
     * @return 子区划列表
     */
    public List<SysRegion> selectRegionByParentId(Long parentId);

    /**
     * 新增行政区划信息
     * 
     * @param region 行政区划信息
     * @return 结果
     */
    public int insertRegion(SysRegion region);

    /**
     * 修改行政区划信息
     * 
     * @param region 行政区划信息
     * @return 结果
     */
    public int updateRegion(SysRegion region);

    /**
     * 删除行政区划管理信息
     * 
     * @param regionId 区划ID
     * @return 结果
     */
    public int deleteRegionById(Long regionId);

    /**
     * 是否存在子节点
     * 
     * @param regionId 区划ID
     * @return 结果
     */
    public int hasChildByRegionId(Long regionId);

    /**
     * 校验区划名称是否唯一
     * 
     * @param regionName 区划名称
     * @param parentId 父级ID
     * @return 结果
     */
    public SysRegion checkRegionNameUnique(String regionName, Long parentId);

    /**
     * 根据区县名称查询区县信息
     * 
     * @param districtName 区县名称
     * @return 区县信息
     */
    public SysRegion selectRegionByDistrictName(String districtName);
}
