package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_rent_payment")
public class RentPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long contractId;
    private String paymentNo;
    private BigDecimal amount;
    private Date paymentDate;
    private Date dueDate;
    private Integer status; // 0:未支付 1:已支付 2:逾期
    private String paymentMethod;
    private String transactionId;
    private Date createTime;
    private Date updateTime;
}
