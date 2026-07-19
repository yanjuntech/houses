package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizCommunity;
import com.ruoyi.system.domain.SysRegion;
import com.ruoyi.system.mapper.BizCommunityMapper;
import com.ruoyi.system.service.IBizCommunityService;
import com.ruoyi.system.service.ISysRegionService;

/**
 * 小区信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizCommunityServiceImpl implements IBizCommunityService
{
    @Autowired
    private BizCommunityMapper bizCommunityMapper;

    @Autowired
    private ISysRegionService sysRegionService;

    /**
     * 查询小区信息集合
     *
     * @param bizCommunity 小区信息
     * @return 小区信息集合
     */
    @Override
    public List<BizCommunity> selectBizCommunityList(BizCommunity bizCommunity)
    {
        return bizCommunityMapper.selectBizCommunityList(bizCommunity);
    }

    /**
     * 通过小区ID查询小区信息
     *
     * @param communityId 小区ID
     * @return 小区对象信息
     */
    @Override
    public BizCommunity selectBizCommunityByCommunityId(Long communityId)
    {
        return bizCommunityMapper.selectBizCommunityByCommunityId(communityId);
    }

    /**
     * 新增小区信息
     *
     * @param bizCommunity 小区信息
     * @return 结果
     */
    @Override
    public int insertBizCommunity(BizCommunity bizCommunity)
    {
        checkCommunityRegisterSwitch(bizCommunity.getProvince(), bizCommunity.getCity(), bizCommunity.getDistrict());
        return bizCommunityMapper.insertBizCommunity(bizCommunity);
    }

    /**
     * 修改小区信息
     *
     * @param bizCommunity 小区信息
     * @return 结果
     */
    @Override
    public int updateBizCommunity(BizCommunity bizCommunity)
    {
        checkCommunityRegisterSwitch(bizCommunity.getProvince(), bizCommunity.getCity(), bizCommunity.getDistrict());
        return bizCommunityMapper.updateBizCommunity(bizCommunity);
    }

    /**
     * 删除小区信息
     *
     * @param communityId 小区ID
     * @return 结果
     */
    @Override
    public int deleteBizCommunityByCommunityId(Long communityId)
    {
        return bizCommunityMapper.deleteBizCommunityByCommunityId(communityId);
    }

    /**
     * 批量删除小区信息
     *
     * @param communityIds 需要删除的小区ID
     * @return 结果
     */
    @Override
    public int deleteBizCommunityByCommunityIds(Long[] communityIds)
    {
        return bizCommunityMapper.deleteBizCommunityByCommunityIds(communityIds);
    }

    /**
     * 校验小区登记开关是否开启
     *
     * @param province 省份
     * @param city 城市
     * @param district 区县
     */
    @Override
    public void checkCommunityRegisterSwitch(String province, String city, String district)
    {
        if (StringUtils.isEmpty(district))
        {
            return;
        }
        SysRegion region = sysRegionService.selectRegionByDistrictName(district);
        if (region == null)
        {
            throw new ServiceException("该地区未开放使用，请联系平台");
        }
        if ("1".equals(region.getCommunityRegisterSwitch()))
        {
            throw new ServiceException("该地区未开放使用，请联系平台");
        }
    }
}
