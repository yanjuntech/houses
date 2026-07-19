package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.BizUserMessage;
import com.ruoyi.system.mapper.BizUserMessageMapper;
import com.ruoyi.system.service.IBizUserMessageService;

/**
 * 用户消息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizUserMessageServiceImpl implements IBizUserMessageService
{
    @Autowired
    private BizUserMessageMapper bizUserMessageMapper;

    /**
     * 查询用户消息集合
     *
     * @param bizUserMessage 用户消息
     * @return 用户消息集合
     */
    @Override
    public List<BizUserMessage> selectBizUserMessageList(BizUserMessage bizUserMessage)
    {
        return bizUserMessageMapper.selectBizUserMessageList(bizUserMessage);
    }

    /**
     * 通过消息ID查询用户消息
     *
     * @param messageId 消息ID
     * @return 用户消息
     */
    @Override
    public BizUserMessage selectBizUserMessageById(Long messageId)
    {
        return bizUserMessageMapper.selectBizUserMessageById(messageId);
    }

    /**
     * 新增用户消息
     *
     * @param bizUserMessage 用户消息
     * @return 结果
     */
    @Override
    public int insertBizUserMessage(BizUserMessage bizUserMessage)
    {
        return bizUserMessageMapper.insertBizUserMessage(bizUserMessage);
    }

    /**
     * 修改用户消息
     *
     * @param bizUserMessage 用户消息
     * @return 结果
     */
    @Override
    public int updateBizUserMessage(BizUserMessage bizUserMessage)
    {
        return bizUserMessageMapper.updateBizUserMessage(bizUserMessage);
    }

    /**
     * 通过消息ID删除用户消息
     *
     * @param messageId 消息ID
     * @return 结果
     */
    @Override
    public int deleteBizUserMessageById(Long messageId)
    {
        return bizUserMessageMapper.deleteBizUserMessageById(messageId);
    }

    /**
     * 批量删除用户消息
     *
     * @param messageIds 需要删除的消息ID数组
     * @return 结果
     */
    @Override
    public int deleteBizUserMessageByIds(Long[] messageIds)
    {
        return bizUserMessageMapper.deleteBizUserMessageByIds(messageIds);
    }

    /**
     * 查询用户未读消息数量
     *
     * @param userId 用户ID
     * @return 未读消息数量
     */
    @Override
    public int countUnreadByUserId(Long userId)
    {
        return bizUserMessageMapper.countUnreadByUserId(userId);
    }

    /**
     * 标记消息已读
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int markAsRead(Long messageId, Long userId)
    {
        BizUserMessage message = selectBizUserMessageById(messageId);
        if (message == null)
        {
            return 0;
        }
        if (userId != null && !userId.equals(message.getUserId()))
        {
            return 0;
        }
        BizUserMessage update = new BizUserMessage();
        update.setMessageId(messageId);
        update.setIsRead("1");
        update.setReadTime(new Date());
        return bizUserMessageMapper.updateBizUserMessage(update);
    }

    /**
     * 推送消息给用户
     *
     * @param userId 接收用户ID
     * @param title 消息标题
     * @param content 消息内容
     * @param sendBy 发送人
     */
    @Override
    public void pushMessage(Long userId, String title, String content, String sendBy)
    {
        BizUserMessage message = new BizUserMessage();
        message.setUserId(userId);
        message.setTitle(title);
        message.setContent(content);
        message.setSendBy(sendBy);
        message.setSendTime(new Date());
        message.setIsRead("0");
        message.setCreateBy(sendBy);
        bizUserMessageMapper.insertBizUserMessage(message);
    }
}
