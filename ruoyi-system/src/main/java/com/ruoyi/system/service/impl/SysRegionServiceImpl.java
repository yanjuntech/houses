package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysRegion;
import com.ruoyi.system.mapper.SysRegionMapper;
import com.ruoyi.system.service.ISysRegionService;

/**
 * 行政区划 服务实现
 * 
 * @author ruoyi
 */
@Service
public class SysRegionServiceImpl implements ISysRegionService
{
    @Autowired
    private SysRegionMapper regionMapper;

    /**
     * 查询行政区划管理数据
     * 
     * @param region 行政区划信息
     * @return 行政区划信息集合
     */
    @Override
    public List<SysRegion> selectRegionList(SysRegion region)
    {
        return regionMapper.selectRegionList(region);
    }

    /**
     * 构建前端所需要树结构
     * 
     * @param regions 区划列表
     * @return 树结构列表
     */
    @Override
    public List<SysRegion> buildRegionTree(List<SysRegion> regions)
    {
        List<SysRegion> returnList = new ArrayList<SysRegion>();
        List<Long> tempList = regions.stream().map(SysRegion::getRegionId).collect(Collectors.toList());
        for (SysRegion region : regions)
        {
            if (!tempList.contains(region.getParentId()))
            {
                recursionFn(regions, region);
                returnList.add(region);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = regions;
        }
        return returnList;
    }

    /**
     * 根据区划ID查询信息
     * 
     * @param regionId 区划ID
     * @return 行政区划信息
     */
    @Override
    public SysRegion selectRegionById(Long regionId)
    {
        return regionMapper.selectRegionById(regionId);
    }

    /**
     * 根据ID查询所有子区划
     * 
     * @param regionId 区划ID
     * @return 区划列表
     */
    @Override
    public List<SysRegion> selectChildrenRegionById(Long regionId)
    {
        return regionMapper.selectChildrenRegionById(regionId);
    }

    /**
     * 根据父ID查询子区划列表
     * 
     * @param parentId 父级ID
     * @return 子区划列表
     */
    @Override
    public List<SysRegion> selectRegionByParentId(Long parentId)
    {
        return regionMapper.selectRegionByParentId(parentId);
    }

    /**
     * 新增保存行政区划信息
     * 
     * @param region 行政区划信息
     * @return 结果
     */
    @Override
    public int insertRegion(SysRegion region)
    {
        return regionMapper.insertRegion(region);
    }

    /**
     * 修改保存行政区划信息
     * 
     * @param region 行政区划信息
     * @return 结果
     */
    @Override
    public int updateRegion(SysRegion region)
    {
        return regionMapper.updateRegion(region);
    }

    /**
     * 删除行政区划管理信息
     * 
     * @param regionId 区划ID
     * @return 结果
     */
    @Override
    public int deleteRegionById(Long regionId)
    {
        return regionMapper.deleteRegionById(regionId);
    }

    /**
     * 校验区划名称是否唯一
     * 
     * @param region 行政区划信息
     * @return 结果
     */
    @Override
    public boolean checkRegionNameUnique(SysRegion region)
    {
        Long regionId = StringUtils.isNull(region.getRegionId()) ? -1L : region.getRegionId();
        SysRegion info = regionMapper.checkRegionNameUnique(region.getRegionName(), region.getParentId());
        if (StringUtils.isNotNull(info) && info.getRegionId().longValue() != regionId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 是否存在区划子节点
     * 
     * @param regionId 区划ID
     * @return 结果
     */
    @Override
    public boolean hasChildByRegionId(Long regionId)
    {
        int result = regionMapper.hasChildByRegionId(regionId);
        return result > 0;
    }

    /**
     * 根据区县名称查询区县信息
     * 
     * @param districtName 区县名称
     * @return 区县信息
     */
    @Override
    public SysRegion selectRegionByDistrictName(String districtName)
    {
        return regionMapper.selectRegionByDistrictName(districtName);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysRegion> list, SysRegion t)
    {
        List<SysRegion> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysRegion tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysRegion> getChildList(List<SysRegion> list, SysRegion t)
    {
        List<SysRegion> tlist = new ArrayList<SysRegion>();
        Iterator<SysRegion> it = list.iterator();
        while (it.hasNext())
        {
            SysRegion n = (SysRegion) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getRegionId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysRegion> list, SysRegion t)
    {
        return getChildList(list, t).size() > 0;
    }
}
