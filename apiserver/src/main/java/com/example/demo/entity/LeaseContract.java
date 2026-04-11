package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_lease_contract")
public class LeaseContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String contractNo;
    private Long houseId;
    private Long landlordId;
    private Long tenantId;
    private Date startDate;
    private Date endDate;
    private BigDecimal rentPrice;
    private BigDecimal deposit;
    private Integer paymentFrequency; // 1:月付 2:季付 3:年付
    private Integer status; // 0:待签署 1:已签署 2:已过期 3:已解除
    private String content;
    private Date createTime;
    private Date updateTime;
}
