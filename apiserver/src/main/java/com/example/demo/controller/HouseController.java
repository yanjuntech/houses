package com.example.demo.controller;

import com.example.demo.entity.House;
import com.example.demo.service.HouseService;
import com.example.demo.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/house")
public class HouseController {

    @Autowired
    private HouseService houseService;

    /**
     * 获取房屋列表
     */
    @GetMapping("/list")
    public ResponseVO<List<House>> list(
            @RequestParam(required = false) Integer houseType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) Long ownerId
    ) {
        if (houseType != null) {
            return ResponseVO.success(houseService.getHousesByType(houseType));
        } else if (status != null) {
            return ResponseVO.success(houseService.getHousesByStatus(status));
        } else if (communityId != null) {
            return ResponseVO.success(houseService.getHousesByCommunityId(communityId));
        } else if (ownerId != null) {
            return ResponseVO.success(houseService.getHousesByOwnerId(ownerId));
        }
        return ResponseVO.success(houseService.list());
    }

    /**
     * 根据ID获取房屋
     */
    @GetMapping("/get/{id}")
    public ResponseVO<House> getById(@PathVariable Long id) {
        return ResponseVO.success(houseService.getById(id));
    }

    /**
     * 添加房屋
     */
    @PostMapping("/add")
    public ResponseVO<Boolean> add(@RequestBody House house) {
        return ResponseVO.success(houseService.save(house));
    }

    /**
     * 更新房屋
     */
    @PutMapping("/update")
    public ResponseVO<Boolean> update(@RequestBody House house) {
        return ResponseVO.success(houseService.updateById(house));
    }

    /**
     * 删除房屋
     */
    @DeleteMapping("/delete/{id}")
    public ResponseVO<Boolean> delete(@PathVariable Long id) {
        return ResponseVO.success(houseService.removeById(id));
    }

    /**
     * 上下架房屋
     */
    @PutMapping("/publish")
    public ResponseVO<Boolean> publish(@RequestParam Long houseId, @RequestParam Integer isPublished) {
        return ResponseVO.success(houseService.publishHouse(houseId, isPublished));
    }

    /**
     * 更新房屋状态
     */
    @PutMapping("/status")
    public ResponseVO<Boolean> status(@RequestParam Long houseId, @RequestParam Integer status) {
        return ResponseVO.success(houseService.updateHouseStatus(houseId, status));
    }
}
