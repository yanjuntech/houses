package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    public ResponseVO<List<User>> list(@RequestParam(required = false) Integer userType) {
        if (userType != null) {
            return ResponseVO.success(userService.getUsersByType(userType));
        }
        return ResponseVO.success(userService.list());
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/get/{id}")
    public ResponseVO<User> getById(@PathVariable Long id) {
        return ResponseVO.success(userService.getById(id));
    }

    /**
     * 添加用户
     */
    @PostMapping("/add")
    public ResponseVO<Boolean> add(@RequestBody User user) {
        return ResponseVO.success(userService.save(user));
    }

    /**
     * 更新用户
     */
    @PutMapping("/update")
    public ResponseVO<Boolean> update(@RequestBody User user) {
        return ResponseVO.success(userService.updateById(user));
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete/{id}")
    public ResponseVO<Boolean> delete(@PathVariable Long id) {
        return ResponseVO.success(userService.removeById(id));
    }

    /**
     * 审核用户
     */
    @PutMapping("/audit")
    public ResponseVO<Boolean> audit(@RequestParam Long userId, @RequestParam Integer status) {
        return ResponseVO.success(userService.auditUser(userId, status));
    }

    /**
     * 管理黑名单
     */
    @PutMapping("/blacklist")
    public ResponseVO<Boolean> blacklist(@RequestParam Long userId, @RequestParam Integer isBlacklisted) {
        return ResponseVO.success(userService.manageBlacklist(userId, isBlacklisted));
    }
}
