package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    /**
     * 根据用户类型获取用户列表
     */
    List<User> getUsersByType(Integer userType);

    /**
     * 审核用户
     */
    boolean auditUser(Long userId, Integer status);

    /**
     * 管理用户黑名单
     */
    boolean manageBlacklist(Long userId, Integer isBlacklisted);

    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(String username);
}
