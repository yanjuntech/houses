package com.ruoyi.web.controller.miniapp;

import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizMiniappUser;
import com.ruoyi.system.domain.BizUserMessage;
import com.ruoyi.system.service.IBizMiniappUserService;
import com.ruoyi.system.service.IBizUserMessageService;

/**
 * 小程序用户管理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/miniapp/user")
public class BizMiniappUserController extends BaseController
{
    @Autowired
    private IBizMiniappUserService bizMiniappUserService;

    @Autowired
    private IBizUserMessageService bizUserMessageService;

    /**
     * 查询小程序用户列表
     */
    @PreAuthorize("@ss.hasPermi('biz:miniapp:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizMiniappUser bizMiniappUser)
    {
        startPage();
        List<BizMiniappUser> list = bizMiniappUserService.selectBizMiniappUserList(bizMiniappUser);
        return getDataTable(list);
    }

    /**
     * 导出小程序用户列表
     */
    @PreAuthorize("@ss.hasPermi('biz:miniapp:user:export')")
    @Log(title = "小程序用户管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizMiniappUser bizMiniappUser)
    {
        List<BizMiniappUser> list = bizMiniappUserService.selectBizMiniappUserList(bizMiniappUser);
        ExcelUtil<BizMiniappUser> util = new ExcelUtil<BizMiniappUser>(BizMiniappUser.class);
        util.exportExcel(response, list, "小程序用户数据");
    }

    /**
     * 根据用户ID获取小程序用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:miniapp:user:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(bizMiniappUserService.selectBizMiniappUserByUserId(userId));
    }

    /**
     * 新增小程序用户
     */
    @PreAuthorize("@ss.hasPermi('biz:miniapp:user:add')")
    @Log(title = "小程序用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizMiniappUser bizMiniappUser)
    {
        bizMiniappUser.setCreateBy(getUsername());
        return toAjax(bizMiniappUserService.insertBizMiniappUser(bizMiniappUser));
    }

    /**
     * 修改小程序用户
     */
    @PreAuthorize("@ss.hasPermi('biz:miniapp:user:edit')")
    @Log(title = "小程序用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizMiniappUser bizMiniappUser)
    {
        bizMiniappUser.setUpdateBy(getUsername());
        return toAjax(bizMiniappUserService.updateBizMiniappUser(bizMiniappUser));
    }

    /**
     * 删除小程序用户
     */
    @PreAuthorize("@ss.hasPermi('biz:miniapp:user:remove')")
    @Log(title = "小程序用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(bizMiniappUserService.deleteBizMiniappUserByUserIds(userIds));
    }

    /**
     * 修改用户身份标签（普通/房东/中介）
     */
    @PreAuthorize("@ss.hasPermi('biz:miniapp:user:edit')")
    @Log(title = "小程序用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeUserType")
    public AjaxResult changeUserType(@RequestBody BizMiniappUser bizMiniappUser)
    {
        bizMiniappUser.setUpdateBy(getUsername());
        return toAjax(bizMiniappUserService.updateUserType(bizMiniappUser));
    }

    /**
     * 实名认证审核
     * 校验昵称（nickname）不能与真实姓名（real_name）相同
     * 实名认证通过时（verifyStatus='2'）同步更新 id_card_verified = '1'
     */
    @PreAuthorize("@ss.hasPermi('biz:miniapp:user:edit')")
    @Log(title = "小程序用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/verify")
    public AjaxResult verify(@RequestBody BizMiniappUser bizMiniappUser)
    {
        if (bizMiniappUser.getUserId() == null)
        {
            return error("用户ID不能为空");
        }
        // 查询用户当前信息以校验昵称与真实姓名
        BizMiniappUser existUser = bizMiniappUserService.selectBizMiniappUserByUserId(bizMiniappUser.getUserId());
        if (existUser == null)
        {
            return error("用户不存在");
        }
        String nickname = StringUtils.isNotEmpty(bizMiniappUser.getNickname()) ? bizMiniappUser.getNickname() : existUser.getNickname();
        String realName = StringUtils.isNotEmpty(bizMiniappUser.getRealName()) ? bizMiniappUser.getRealName() : existUser.getRealName();
        // 校验昵称不能与真实姓名相同
        if (StringUtils.isNotEmpty(nickname)
                && StringUtils.isNotEmpty(realName)
                && nickname.equals(realName))
        {
            return error("昵称不能与真实姓名相同");
        }
        // 实名认证通过时，标记身份证已认证
        if ("2".equals(bizMiniappUser.getVerifyStatus()))
        {
            bizMiniappUser.setIdCardVerified("1");
        }
        bizMiniappUser.setUpdateBy(getUsername());
        return toAjax(bizMiniappUserService.updateVerifyStatus(bizMiniappUser));
    }

    /**
     * 查询黑名单用户列表
     */
    @PreAuthorize("@ss.hasPermi('biz:miniapp:blacklist:list')")
    @GetMapping("/blacklist/list")
    public TableDataInfo blacklistList(BizMiniappUser bizMiniappUser)
    {
        startPage();
        List<BizMiniappUser> list = bizMiniappUserService.selectBlacklist(bizMiniappUser);
        return getDataTable(list);
    }

    /**
     * 加入黑名单
     * 请求体包含 userId 和 blacklistReason
     */
    @PreAuthorize("@ss.hasPermi('biz:miniapp:blacklist:add')")
    @Log(title = "小程序用户黑名单", businessType = BusinessType.UPDATE)
    @PutMapping("/blacklist/add")
    public AjaxResult addBlacklist(@RequestBody BizMiniappUser bizMiniappUser)
    {
        if (bizMiniappUser.getUserId() == null)
        {
            return error("用户ID不能为空");
        }
        bizMiniappUser.setUpdateBy(getUsername());
        return toAjax(bizMiniappUserService.addToBlacklist(bizMiniappUser));
    }

    /**
     * 解除黑名单
     */
    @PreAuthorize("@ss.hasPermi('biz:miniapp:blacklist:remove')")
    @Log(title = "小程序用户黑名单", businessType = BusinessType.UPDATE)
    @PutMapping("/blacklist/remove/{userId}")
    public AjaxResult removeBlacklist(@PathVariable("userId") Long userId)
    {
        return toAjax(bizMiniappUserService.removeFromBlacklist(userId));
    }

    /**
     * 手机号登录（公开接口）
     */
    @Anonymous
    @PostMapping("/loginByPhone")
    public AjaxResult loginByPhone(@RequestBody BizMiniappUser bizMiniappUser)
    {
        BizMiniappUser user = bizMiniappUserService.selectBizMiniappUserByPhone(bizMiniappUser.getPhone());
        if (user == null)
        {
            // 用户不存在则自动注册
            bizMiniappUser.setUserType("0");
            bizMiniappUser.setVerifyStatus("0");
            bizMiniappUser.setStatus("0");
            bizMiniappUser.setCreateBy("miniapp");
            bizMiniappUserService.insertBizMiniappUser(bizMiniappUser);
            user = bizMiniappUserService.selectBizMiniappUserByPhone(bizMiniappUser.getPhone());
        }
        return success(user);
    }

    /**
     * 微信登录（公开接口）
     * 登录时记录 wechat_nickname、wechat_avatar 到用户表
     */
    @Anonymous
    @PostMapping("/loginByWechat")
    public AjaxResult loginByWechat(@RequestBody BizMiniappUser bizMiniappUser)
    {
        BizMiniappUser user = bizMiniappUserService.selectBizMiniappUserByOpenid(bizMiniappUser.getOpenid());
        if (user == null)
        {
            // 用户不存在则自动注册
            bizMiniappUser.setUserType("0");
            bizMiniappUser.setVerifyStatus("0");
            bizMiniappUser.setStatus("0");
            bizMiniappUser.setCreateBy("miniapp");
            bizMiniappUserService.insertBizMiniappUser(bizMiniappUser);
            user = bizMiniappUserService.selectBizMiniappUserByOpenid(bizMiniappUser.getOpenid());
        }
        else
        {
            // 已存在用户，更新微信昵称和微信头像
            BizMiniappUser update = new BizMiniappUser();
            update.setUserId(user.getUserId());
            update.setWechatNickname(bizMiniappUser.getWechatNickname());
            update.setWechatAvatar(bizMiniappUser.getWechatAvatar());
            update.setUpdateBy("miniapp");
            bizMiniappUserService.updateBizMiniappUser(update);
            user.setWechatNickname(bizMiniappUser.getWechatNickname());
            user.setWechatAvatar(bizMiniappUser.getWechatAvatar());
        }
        return success(user);
    }

    /**
     * 绑定手机号（公开接口）
     */
    @Anonymous
    @PostMapping("/bindPhone")
    public AjaxResult bindPhone(@RequestBody BizMiniappUser bizMiniappUser)
    {
        if (bizMiniappUser.getUserId() == null)
        {
            return error("用户ID不能为空");
        }
        if (bizMiniappUser.getPhone() == null || bizMiniappUser.getPhone().isEmpty())
        {
            return error("手机号不能为空");
        }
        // 校验手机号是否已被其他用户绑定
        BizMiniappUser existUser = bizMiniappUserService.selectBizMiniappUserByPhone(bizMiniappUser.getPhone());
        if (existUser != null && existUser.getUserId().longValue() != bizMiniappUser.getUserId().longValue())
        {
            return error("该手机号已被其他用户绑定");
        }
        BizMiniappUser update = new BizMiniappUser();
        update.setUserId(bizMiniappUser.getUserId());
        update.setPhone(bizMiniappUser.getPhone());
        update.setUpdateBy("miniapp");
        return toAjax(bizMiniappUserService.updateBizMiniappUser(update));
    }

    /**
     * 调整发布次数
     */
    @PreAuthorize("@ss.hasPermi('biz:miniapp:user:edit')")
    @Log(title = "小程序用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/adjustPublishCount")
    public AjaxResult adjustPublishCount(@RequestBody Map<String, Object> params)
    {
        Long userId = params.get("userId") != null ? Long.valueOf(params.get("userId").toString()) : null;
        Integer count = params.get("count") != null ? Integer.valueOf(params.get("count").toString()) : null;
        String reason = params.get("reason") != null ? params.get("reason").toString() : null;
        if (userId == null)
        {
            return error("用户ID不能为空");
        }
        if (count == null)
        {
            return error("调整数量不能为空");
        }
        return toAjax(bizMiniappUserService.adjustPublishCount(userId, count, reason));
    }

    /**
     * 延长发布周期
     */
    @PreAuthorize("@ss.hasPermi('biz:miniapp:user:edit')")
    @Log(title = "小程序用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/extendPublishPeriod")
    public AjaxResult extendPublishPeriod(@RequestBody Map<String, Object> params)
    {
        Long userId = params.get("userId") != null ? Long.valueOf(params.get("userId").toString()) : null;
        Integer days = params.get("days") != null ? Integer.valueOf(params.get("days").toString()) : null;
        if (userId == null)
        {
            return error("用户ID不能为空");
        }
        if (days == null || days <= 0)
        {
            return error("延长天数必须大于0");
        }
        return toAjax(bizMiniappUserService.extendPublishPeriod(userId, days));
    }

    /**
     * 小程序端 - 查询用户消息列表
     */
    @Anonymous
    @GetMapping("/message/list")
    public TableDataInfo messageList(BizUserMessage bizUserMessage)
    {
        startPage();
        List<BizUserMessage> list = bizUserMessageService.selectBizUserMessageList(bizUserMessage);
        return getDataTable(list);
    }

    /**
     * 小程序端 - 获取消息详情（标记已读）
     */
    @Anonymous
    @GetMapping(value = "/message/{messageId}")
    public AjaxResult getMessageDetail(@PathVariable("messageId") Long messageId)
    {
        BizUserMessage message = bizUserMessageService.selectBizUserMessageById(messageId);
        if (message != null && "0".equals(message.getIsRead()))
        {
            bizUserMessageService.markAsRead(messageId, message.getUserId());
            message.setIsRead("1");
        }
        return success(message);
    }

    /**
     * 小程序端 - 查询未读消息数量
     */
    @Anonymous
    @GetMapping("/message/unreadCount")
    public AjaxResult unreadCount(Long userId)
    {
        if (userId == null)
        {
            return error("用户ID不能为空");
        }
        int count = bizUserMessageService.countUnreadByUserId(userId);
        return success(count);
    }
}
