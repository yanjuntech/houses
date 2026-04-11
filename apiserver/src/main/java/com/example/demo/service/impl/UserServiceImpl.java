package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public List<User> getUsersByType(Integer userType) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_type", userType);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean auditUser(Long userId, Integer status) {
        User user = baseMapper.selectById(userId);
        if (user != null) {
            user.setStatus(status);
            return updateById(user);
        }
        return false;
    }

    @Override
    public boolean manageBlacklist(Long userId, Integer isBlacklisted) {
        User user = baseMapper.selectById(userId);
        if (user != null) {
            user.setIsBlacklisted(isBlacklisted);
            return updateById(user);
        }
        return false;
    }

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return baseMapper.selectOne(wrapper);
    }
}
