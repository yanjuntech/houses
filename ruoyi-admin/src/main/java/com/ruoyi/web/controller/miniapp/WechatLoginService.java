package com.ruoyi.web.controller.miniapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

/**
 * 微信登录服务
 * 调用微信 code2Session 接口换取 openid
 *
 * @author ruoyi
 */
@Service
public class WechatLoginService
{
    private static final Logger log = LoggerFactory.getLogger(WechatLoginService.class);

    private static final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private WechatMiniappConfig wechatMiniappConfig;

    /**
     * 调用微信 code2Session 接口换取 openid（静态方法）
     *
     * @param appid 小程序 appId
     * @param secret 小程序 secret
     * @param code 前端 wx.login 获取的 code
     * @param authUrl code2Session 接口地址
     * @return openid 字符串，失败返回 null
     */
    public static String code2Session(String appid, String secret, String code, String authUrl)
    {
        try
        {
            String url = String.format(
                    "%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                    authUrl, appid, secret, code);
            String response = restTemplate.getForObject(url, String.class);
            if (response == null || response.isEmpty())
            {
                log.error("调用微信 code2Session 失败，返回为空");
                return null;
            }
            JSONObject obj = JSON.parseObject(response);
            // 微信接口失败时会返回 errcode（非 0）和 errmsg
            if (obj.containsKey("errcode") && obj.getIntValue("errcode") != 0)
            {
                log.error("调用微信 code2Session 失败：{}", response);
                return null;
            }
            String openid = obj.getString("openid");
            if (openid == null || openid.isEmpty())
            {
                log.error("调用微信 code2Session 未获取到 openid：{}", response);
                return null;
            }
            log.debug("微信 code2Session 成功获取 openid：{}", openid);
            return openid;
        }
        catch (Exception e)
        {
            log.error("调用微信 code2Session 异常", e);
            return null;
        }
    }

    /**
     * 调用微信 code2Session 接口换取 openid（实例方法，使用注入的配置）
     *
     * @param code 前端 wx.login 获取的 code
     * @return openid 字符串，失败返回 null
     */
    public String code2Session(String code)
    {
        return code2Session(wechatMiniappConfig.getAppid(),
                wechatMiniappConfig.getSecret(),
                code,
                wechatMiniappConfig.getAuthUrl());
    }
}
