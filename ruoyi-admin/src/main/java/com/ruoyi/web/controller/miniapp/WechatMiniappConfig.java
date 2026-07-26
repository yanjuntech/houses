package com.ruoyi.web.controller.miniapp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

/**
 * 微信小程序配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wechat.miniapp")
public class WechatMiniappConfig {
    /** 小程序 appId */
    private String appid;
    /** 小程序 secret */
    private String secret;
    /** code2Session 接口地址 */
    private String authUrl;
}
