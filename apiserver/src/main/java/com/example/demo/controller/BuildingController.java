package com.example.demo.controller;

import com.example.demo.entity.Building;
import com.example.demo.service.BuildingService;
import com.example.demo.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/building")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    /**
     * 获取楼栋列表
     */
    @GetMapping("/list")
    public ResponseVO<List<Building>> list(@RequestParam(required = false) Long communityId) {
        if (communityId != null) {
            return ResponseVO.success(buildingService.getBuildingsByCommunityId(communityId));
        }
        return ResponseVO.success(buildingService.list());
    }

    /**
     * 根据ID获取楼栋
     */
    @GetMapping("/get/{id}")
    public ResponseVO<Building> getById(@PathVariable Long id) {
        return ResponseVO.success(buildingService.getById(id));
    }

    /**
     * 添加楼栋
     */
    @PostMapping("/add")
    public ResponseVO<Boolean> add(@RequestBody Building building) {
        return ResponseVO.success(buildingService.save(building));
    }

    /**
     * 更新楼栋
     */
    @PutMapping("/update")
    public ResponseVO<Boolean> update(@RequestBody Building building) {
        return ResponseVO.success(buildingService.updateById(building));
    }

    /**
     * 删除楼栋
     */
    @DeleteMapping("/delete/{id}")
    public ResponseVO<Boolean> delete(@PathVariable Long id) {
        return ResponseVO.success(buildingService.removeById(id));
    }
}
