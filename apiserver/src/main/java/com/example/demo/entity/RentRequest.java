package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_rent_request")
public class RentRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private BigDecimal minBudget;
    private BigDecimal maxBudget;
    private String area;
    private String type;
    private String orientation;
    private String facilities;
    private String communityName;
    private String address;
    private Integer typeFlag; // 1:求租 2:合租
    private Integer status; // 0:下架 1:上架
    private Date createTime;
    private Date updateTime;
}
