package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.SensitiveWordUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizChatMessage;
import com.ruoyi.system.domain.BizSensitiveWord;
import com.ruoyi.system.mapper.BizChatMessageMapper;
import com.ruoyi.system.mapper.BizSensitiveWordMapper;
import com.ruoyi.system.service.IBizChatMessageService;

/**
 * 聊天消息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizChatMessageServiceImpl implements IBizChatMessageService
{
    @Autowired
    private BizChatMessageMapper bizChatMessageMapper;

    @Autowired
    private BizSensitiveWordMapper bizSensitiveWordMapper;

    /** 敏感词库最近一次初始化时间（毫秒），5 分钟刷新一次 */
    private volatile long lastInitTime = 0L;
    private static final long INIT_INTERVAL = 5 * 60 * 1000L;

    /**
     * 初始化敏感词库（带 5 分钟缓存，避免每次发消息都查询数据库）
     */
    private void initSensitiveWordsIfNeeded()
    {
        long now = System.currentTimeMillis();
        if (now - lastInitTime > INIT_INTERVAL)
        {
            synchronized (this)
            {
                if (now - lastInitTime > INIT_INTERVAL)
                {
                    List<BizSensitiveWord> words = bizSensitiveWordMapper.selectAllEnabledWords();
                    List<String> wordList = words.stream()
                            .map(BizSensitiveWord::getWord)
                            .filter(StringUtils::isNotEmpty)
                            .collect(Collectors.toList());
                    SensitiveWordUtil.init(wordList);
                    lastInitTime = now;
                }
            }
        }
    }

    /**
     * 查询聊天消息集合
     *
     * @param bizChatMessage 聊天消息
     * @return 聊天消息集合
     */
    @Override
    public List<BizChatMessage> selectBizChatMessageList(BizChatMessage bizChatMessage)
    {
        return bizChatMessageMapper.selectBizChatMessageList(bizChatMessage);
    }

    /**
     * 通过消息ID查询聊天消息
     *
     * @param messageId 消息ID
     * @return 聊天消息
     */
    @Override
    public BizChatMessage selectBizChatMessageByMessageId(Long messageId)
    {
        return bizChatMessageMapper.selectBizChatMessageByMessageId(messageId);
    }

    /**
     * 新增聊天消息
     *
     * @param bizChatMessage 聊天消息
     * @return 结果
     */
    @Override
    public int insertBizChatMessage(BizChatMessage bizChatMessage)
    {
        return bizChatMessageMapper.insertBizChatMessage(bizChatMessage);
    }

    /**
     * 修改聊天消息
     *
     * @param bizChatMessage 聊天消息
     * @return 结果
     */
    @Override
    public int updateBizChatMessage(BizChatMessage bizChatMessage)
    {
        return bizChatMessageMapper.updateBizChatMessage(bizChatMessage);
    }

    /**
     * 通过消息ID删除聊天消息
     *
     * @param messageId 消息ID
     * @return 结果
     */
    @Override
    public int deleteBizChatMessageByMessageId(Long messageId)
    {
        return bizChatMessageMapper.deleteBizChatMessageByMessageId(messageId);
    }

    /**
     * 批量删除聊天消息
     *
     * @param messageIds 需要删除的消息ID数组
     * @return 结果
     */
    @Override
    public int deleteBizChatMessageByMessageIds(Long[] messageIds)
    {
        return bizChatMessageMapper.deleteBizChatMessageByMessageIds(messageIds);
    }

    /**
     * 发送消息（核心方法）
     * 1. 调用 SensitiveWordUtil.check(content) 进行敏感词过滤
     * 2. 如果含敏感词，设置 is_filtered='1'，filter_reason 记录命中词列表，不保存
     * 3. 如果合规，设置 is_filtered='0'，保存消息
     *
     * @param bizChatMessage 聊天消息
     * @return 结果
     */
    @Override
    public int sendMessage(BizChatMessage bizChatMessage)
    {
        String content = bizChatMessage.getContent();
        if (StringUtils.isNotEmpty(content))
        {
            // 先初始化敏感词库（带缓存）
            initSensitiveWordsIfNeeded();
            SensitiveWordUtil.SensitiveWordResult result = SensitiveWordUtil.check(content);
            if (result.getHasSensitive())
            {
                // 命中敏感词：标记拦截，不保存消息
                bizChatMessage.setIsFiltered("1");
                bizChatMessage.setFilterReason("命中敏感词：" + String.join(",", result.getHitWords()));
                return 0;
            }
        }
        // 合规消息：保存
        bizChatMessage.setIsFiltered("0");
        bizChatMessage.setReadStatus("0");
        return bizChatMessageMapper.insertBizChatMessage(bizChatMessage);
    }

    /**
     * 标记已读
     *
     * @param messageId 消息ID
     * @return 结果
     */
    @Override
    public int markAsRead(Long messageId)
    {
        BizChatMessage message = new BizChatMessage();
        message.setMessageId(messageId);
        message.setReadStatus("1");
        message.setReadTime(new Date());
        return bizChatMessageMapper.updateBizChatMessage(message);
    }

    /**
     * 查询两个用户的聊天记录
     *
     * @param userId1 用户ID1
     * @param userId2 用户ID2
     * @return 聊天消息集合
     */
    @Override
    public List<BizChatMessage> getChatHistory(Long userId1, Long userId2)
    {
        return bizChatMessageMapper.selectChatHistory(userId1, userId2);
    }
}
