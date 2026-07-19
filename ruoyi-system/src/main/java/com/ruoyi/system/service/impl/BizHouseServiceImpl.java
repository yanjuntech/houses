package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizHouse;
import com.ruoyi.system.domain.BizMiniappUser;
import com.ruoyi.system.mapper.BizHouseMapper;
import com.ruoyi.system.mapper.BizMiniappUserMapper;
import com.ruoyi.system.service.IBizHouseService;

/**
 * 房屋信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizHouseServiceImpl implements IBizHouseService
{
    @Autowired
    private BizHouseMapper bizHouseMapper;

    @Autowired
    private BizMiniappUserMapper bizMiniappUserMapper;

    /**
     * 查询房屋信息集合
     * 同时将 publish_user_type 转换为中文标签（房东直租/房屋中介）
     *
     * @param bizHouse 房屋信息
     * @return 房屋信息集合
     */
    @Override
    public List<BizHouse> selectBizHouseList(BizHouse bizHouse)
    {
        List<BizHouse> list = bizHouseMapper.selectBizHouseList(bizHouse);
        for (BizHouse house : list)
        {
            buildPublishUserTypeLabel(house);
        }
        return list;
    }

    /**
     * 通过房屋ID查询房屋信息
     * 同时将 publish_user_type 转换为中文标签（房东直租/房屋中介）
     *
     * @param houseId 房屋ID
     * @return 房屋信息
     */
    @Override
    public BizHouse selectBizHouseByHouseId(Long houseId)
    {
        BizHouse house = bizHouseMapper.selectBizHouseByHouseId(houseId);
        if (StringUtils.isNotNull(house))
        {
            buildPublishUserTypeLabel(house);
        }
        return house;
    }

    /**
     * 新增房屋信息
     * 校验发布者是否已完成实名认证（id_card_verified='1'）
     * 扣减发布次数，次数不足时抛出异常
     *
     * @param bizHouse 房屋信息
     * @return 结果
     */
    @Override
    public int insertBizHouse(BizHouse bizHouse)
    {
        // 校验发布者是否已完成实名认证
        if (bizHouse.getPublishUserId() != null)
        {
            BizMiniappUser publishUser = bizMiniappUserMapper.selectBizMiniappUserByUserId(bizHouse.getPublishUserId());
            if (publishUser == null || !"1".equals(publishUser.getIdCardVerified()))
            {
                throw new ServiceException("请先完成实名认证");
            }
            // 校验发布次数
            if (publishUser.getPublishCount() == null || publishUser.getPublishCount() <= 0)
            {
                throw new ServiceException("发布次数不足，请先兑换或联系管理员");
            }
            // 扣减发布次数，累加累计发布次数
            publishUser.setPublishCount(publishUser.getPublishCount() - 1);
            publishUser.setTotalPublishCount(publishUser.getTotalPublishCount() == null ? 1 : publishUser.getTotalPublishCount() + 1);
            bizMiniappUserMapper.updateBizMiniappUser(publishUser);
        }
        return bizHouseMapper.insertBizHouse(bizHouse);
    }

    /**
     * 修改房屋信息
     *
     * @param bizHouse 房屋信息
     * @return 结果
     */
    @Override
    public int updateBizHouse(BizHouse bizHouse)
    {
        return bizHouseMapper.updateBizHouse(bizHouse);
    }

    /**
     * 删除房屋信息
     *
     * @param houseId 房屋ID
     * @return 结果
     */
    @Override
    public int deleteBizHouseByHouseId(Long houseId)
    {
        return bizHouseMapper.deleteBizHouseByHouseId(houseId);
    }

    /**
     * 批量删除房屋信息
     *
     * @param houseIds 需要删除的房屋ID数组
     * @return 结果
     */
    @Override
    public int deleteBizHouseByHouseIds(Long[] houseIds)
    {
        return bizHouseMapper.deleteBizHouseByHouseIds(houseIds);
    }

    /**
     * 修改房屋上下架状态
     *
     * @param bizHouse 房屋信息
     * @return 结果
     */
    @Override
    public int updateBizHouseStatus(BizHouse bizHouse)
    {
        return bizHouseMapper.updateBizHouseStatus(bizHouse);
    }

    /**
     * 批量新增房屋信息
     *
     * @param houseList 房屋信息集合
     * @return 成功插入数量
     */
    @Override
    public int batchInsert(List<BizHouse> houseList)
    {
        if (houseList == null || houseList.isEmpty())
        {
            return 0;
        }
        int rows = 0;
        for (BizHouse house : houseList)
        {
            rows += bizHouseMapper.insertBizHouse(house);
        }
        return rows;
    }

    /**
     * 调整房屋有效期
     *
     * @param bizHouse 房屋信息（包含 houseId 和 validUntil）
     * @return 结果
     */
    @Override
    public int updateValidUntil(BizHouse bizHouse)
    {
        return bizHouseMapper.updateBizHouse(bizHouse);
    }

    /**
     * 构建发布者身份中文标签
     * 1=房东直租 2=房屋中介
     *
     * @param house 房屋信息
     */
    private void buildPublishUserTypeLabel(BizHouse house)
    {
        if (StringUtils.isEmpty(house.getPublishUserType()))
        {
            return;
        }
        if ("1".equals(house.getPublishUserType()))
        {
            house.setPublishUserTypeLabel("房东直租");
        }
        else if ("2".equals(house.getPublishUserType()))
        {
            house.setPublishUserTypeLabel("房屋中介");
        }
    }
}
