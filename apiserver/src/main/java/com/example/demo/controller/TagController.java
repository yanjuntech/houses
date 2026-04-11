package com.example.demo.controller;

import com.example.demo.entity.HouseTag;
import com.example.demo.service.HouseTagService;
import com.example.demo.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private HouseTagService tagService;

    /**
     * 获取标签列表
     */
    @GetMapping("/list")
    public ResponseVO<List<HouseTag>> list() {
        return ResponseVO.success(tagService.list());
    }

    /**
     * 根据ID获取标签
     */
    @GetMapping("/get/{id}")
    public ResponseVO<HouseTag> getById(@PathVariable Long id) {
        return ResponseVO.success(tagService.getById(id));
    }

    /**
     * 添加标签
     */
    @PostMapping("/add")
    public ResponseVO<Boolean> add(@RequestBody HouseTag tag) {
        return ResponseVO.success(tagService.save(tag));
    }

    /**
     * 更新标签
     */
    @PutMapping("/update")
    public ResponseVO<Boolean> update(@RequestBody HouseTag tag) {
        return ResponseVO.success(tagService.updateById(tag));
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/delete/{id}")
    public ResponseVO<Boolean> delete(@PathVariable Long id) {
        return ResponseVO.success(tagService.removeById(id));
    }
}
