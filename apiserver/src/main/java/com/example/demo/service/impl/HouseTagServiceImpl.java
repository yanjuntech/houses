package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.HouseTag;
import com.example.demo.mapper.HouseTagMapper;
import com.example.demo.service.HouseTagService;
import org.springframework.stereotype.Service;

@Service
public class HouseTagServiceImpl extends ServiceImpl<HouseTagMapper, HouseTag> implements HouseTagService {
}
