package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.House;

import java.util.List;

public interface HouseService extends IService<House> {

    /**
     * 根据房屋类型获取房屋列表
     */
    List<House> getHousesByType(Integer houseType);

    /**
     * 根据房屋状态获取房屋列表
     */
    List<House> getHousesByStatus(Integer status);

    /**
     * 上下架房屋
     */
    boolean publishHouse(Long houseId, Integer isPublished);

    /**
     * 更新房屋状态
     */
    boolean updateHouseStatus(Long houseId, Integer status);

    /**
     * 根据小区ID获取房屋列表
     */
    List<House> getHousesByCommunityId(Long communityId);

    /**
     * 根据房东ID获取房屋列表
     */
    List<House> getHousesByOwnerId(Long ownerId);
}
