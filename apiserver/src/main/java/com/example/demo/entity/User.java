package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private Integer userType; // 1:管理员 2:租户 3:房东 4:中介
    private Integer status; // 0:禁用 1:启用 2:待审核
    private Integer isBlacklisted; // 0:正常 1:黑名单
    private Date createTime;
    private Date updateTime;
}
