package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizChatMessage;

/**
 * 聊天消息 服务层
 *
 * @author ruoyi
 */
public interface IBizChatMessageService
{
    /**
     * 查询聊天消息集合
     *
     * @param bizChatMessage 聊天消息
     * @return 聊天消息集合
     */
    public List<BizChatMessage> selectBizChatMessageList(BizChatMessage bizChatMessage);

    /**
     * 通过消息ID查询聊天消息
     *
     * @param messageId 消息ID
     * @return 聊天消息
     */
    public BizChatMessage selectBizChatMessageByMessageId(Long messageId);

    /**
     * 新增聊天消息
     *
     * @param bizChatMessage 聊天消息
     * @return 结果
     */
    public int insertBizChatMessage(BizChatMessage bizChatMessage);

    /**
     * 修改聊天消息
     *
     * @param bizChatMessage 聊天消息
     * @return 结果
     */
    public int updateBizChatMessage(BizChatMessage bizChatMessage);

    /**
     * 通过消息ID删除聊天消息
     *
     * @param messageId 消息ID
     * @return 结果
     */
    public int deleteBizChatMessageByMessageId(Long messageId);

    /**
     * 批量删除聊天消息
     *
     * @param messageIds 需要删除的消息ID数组
     * @return 结果
     */
    public int deleteBizChatMessageByMessageIds(Long[] messageIds);

    /**
     * 发送消息（核心方法）
     * 1. 调用 SensitiveWordUtil.check(content) 进行敏感词过滤
     * 2. 如果含敏感词，设置 is_filtered='1'，filter_reason 记录命中词列表，不保存
     * 3. 如果合规，设置 is_filtered='0'，保存消息
     *
     * @param bizChatMessage 聊天消息
     * @return 结果
     */
    public int sendMessage(BizChatMessage bizChatMessage);

    /**
     * 标记已读
     *
     * @param messageId 消息ID
     * @return 结果
     */
    public int markAsRead(Long messageId);

    /**
     * 查询两个用户的聊天记录
     *
     * @param userId1 用户ID1
     * @param userId2 用户ID2
     * @return 聊天消息集合
     */
    public List<BizChatMessage> getChatHistory(Long userId1, Long userId2);
}
