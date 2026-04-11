package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_maintenance_request")
public class MaintenanceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long houseId;
    private Long tenantId;
    private String title;
    private String description;
    private String images;
    private Integer status; // 0:待处理 1:处理中 2:已完成 3:已取消
    private BigDecimal cost;
    private String handler;
    private Date completeTime;
    private Integer rating; // 1-5星
    private String comment;
    private Date createTime;
    private Date updateTime;
}
