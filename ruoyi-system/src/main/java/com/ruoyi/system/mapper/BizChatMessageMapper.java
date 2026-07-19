package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.BizChatMessage;

/**
 * 聊天消息 数据层
 *
 * @author ruoyi
 */
public interface BizChatMessageMapper
{
    /**
     * 查询聊天消息列表
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
     * 查询两个用户的聊天记录
     *
     * @param userId1 用户ID1
     * @param userId2 用户ID2
     * @return 聊天消息集合
     */
    public List<BizChatMessage> selectChatHistory(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}
