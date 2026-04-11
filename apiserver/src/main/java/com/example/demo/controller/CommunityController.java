package com.example.demo.controller;

import com.example.demo.entity.Community;
import com.example.demo.service.CommunityService;
import com.example.demo.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    /**
     * 获取小区列表
     */
    @GetMapping("/list")
    public ResponseVO<List<Community>> list() {
        return ResponseVO.success(communityService.list());
    }

    /**
     * 根据ID获取小区
     */
    @GetMapping("/get/{id}")
    public ResponseVO<Community> getById(@PathVariable Long id) {
        return ResponseVO.success(communityService.getById(id));
    }

    /**
     * 添加小区
     */
    @PostMapping("/add")
    public ResponseVO<Boolean> add(@RequestBody Community community) {
        return ResponseVO.success(communityService.save(community));
    }

    /**
     * 更新小区
     */
    @PutMapping("/update")
    public ResponseVO<Boolean> update(@RequestBody Community community) {
        return ResponseVO.success(communityService.updateById(community));
    }

    /**
     * 删除小区
     */
    @DeleteMapping("/delete/{id}")
    public ResponseVO<Boolean> delete(@PathVariable Long id) {
        return ResponseVO.success(communityService.removeById(id));
    }
}
