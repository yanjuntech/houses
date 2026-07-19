package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizUserMessage;

/**
 * 用户消息 数据层
 *
 * @author ruoyi
 */
public interface BizUserMessageMapper
{
    /**
     * 查询用户消息列表
     *
     * @param bizUserMessage 用户消息
     * @return 用户消息集合
     */
    public List<BizUserMessage> selectBizUserMessageList(BizUserMessage bizUserMessage);

    /**
     * 通过消息ID查询用户消息
     *
     * @param messageId 消息ID
     * @return 用户消息
     */
    public BizUserMessage selectBizUserMessageById(Long messageId);

    /**
     * 新增用户消息
     *
     * @param bizUserMessage 用户消息
     * @return 结果
     */
    public int insertBizUserMessage(BizUserMessage bizUserMessage);

    /**
     * 修改用户消息
     *
     * @param bizUserMessage 用户消息
     * @return 结果
     */
    public int updateBizUserMessage(BizUserMessage bizUserMessage);

    /**
     * 通过消息ID删除用户消息
     *
     * @param messageId 消息ID
     * @return 结果
     */
    public int deleteBizUserMessageById(Long messageId);

    /**
     * 批量删除用户消息
     *
     * @param messageIds 需要删除的消息ID数组
     * @return 结果
     */
    public int deleteBizUserMessageByIds(Long[] messageIds);

    /**
     * 查询用户未读消息数量
     *
     * @param userId 用户ID
     * @return 未读消息数量
     */
    public int countUnreadByUserId(Long userId);
}
