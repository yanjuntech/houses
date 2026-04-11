package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Community;
import com.example.demo.mapper.CommunityMapper;
import com.example.demo.service.CommunityService;
import org.springframework.stereotype.Service;

@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {
}
