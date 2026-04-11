package com.example.demo.controller;

import com.example.demo.entity.Unit;
import com.example.demo.service.UnitService;
import com.example.demo.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unit")
public class UnitController {

    @Autowired
    private UnitService unitService;

    /**
     * 获取单元列表
     */
    @GetMapping("/list")
    public ResponseVO<List<Unit>> list(@RequestParam(required = false) Long buildingId) {
        if (buildingId != null) {
            return ResponseVO.success(unitService.getUnitsByBuildingId(buildingId));
        }
        return ResponseVO.success(unitService.list());
    }

    /**
     * 根据ID获取单元
     */
    @GetMapping("/get/{id}")
    public ResponseVO<Unit> getById(@PathVariable Long id) {
        return ResponseVO.success(unitService.getById(id));
    }

    /**
     * 添加单元
     */
    @PostMapping("/add")
    public ResponseVO<Boolean> add(@RequestBody Unit unit) {
        return ResponseVO.success(unitService.save(unit));
    }

    /**
     * 更新单元
     */
    @PutMapping("/update")
    public ResponseVO<Boolean> update(@RequestBody Unit unit) {
        return ResponseVO.success(unitService.updateById(unit));
    }

    /**
     * 删除单元
     */
    @DeleteMapping("/delete/{id}")
    public ResponseVO<Boolean> delete(@PathVariable Long id) {
        return ResponseVO.success(unitService.removeById(id));
    }
}
