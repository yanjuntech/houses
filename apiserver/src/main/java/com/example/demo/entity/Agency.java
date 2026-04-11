package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_agency")
public class Agency implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String agencyName;
    private String contactPerson;
    private String contactPhone;
    private String address;
    private String licenseNumber;
    private Integer status; // 0:禁用 1:启用 2:待审核
    private Date createTime;
    private Date updateTime;
}
