package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_house")
public class House implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long communityId;
    private Long buildingId;
    private Long unitId;
    private String houseNumber;
    private String type;
    private BigDecimal area;
    private String orientation;
    private String decoration;
    private Integer status; // 0:空置 1:已租 2:维护中
    private Integer isPublished; // 0:下架 1:上架
    private BigDecimal rentPrice;
    private BigDecimal sellPrice;
    private Integer houseType; // 1:出租 2:出售
    private Long ownerId;
    private String description;
    private Date createTime;
    private Date updateTime;
}
