package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.House;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HouseMapper extends BaseMapper<House> {
}
