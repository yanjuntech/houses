package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.BizSensitiveWord;
import com.ruoyi.system.mapper.BizSensitiveWordMapper;
import com.ruoyi.system.service.IBizSensitiveWordService;

/**
 * 敏感词库 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizSensitiveWordServiceImpl implements IBizSensitiveWordService
{
    @Autowired
    private BizSensitiveWordMapper bizSensitiveWordMapper;

    /**
     * 查询敏感词集合
     *
     * @param bizSensitiveWord 敏感词
     * @return 敏感词集合
     */
    @Override
    public List<BizSensitiveWord> selectBizSensitiveWordList(BizSensitiveWord bizSensitiveWord)
    {
        return bizSensitiveWordMapper.selectBizSensitiveWordList(bizSensitiveWord);
    }

    /**
     * 通过敏感词ID查询敏感词
     *
     * @param wordId 敏感词ID
     * @return 敏感词
     */
    @Override
    public BizSensitiveWord selectBizSensitiveWordByWordId(Long wordId)
    {
        return bizSensitiveWordMapper.selectBizSensitiveWordByWordId(wordId);
    }

    /**
     * 新增敏感词
     *
     * @param bizSensitiveWord 敏感词
     * @return 结果
     */
    @Override
    public int insertBizSensitiveWord(BizSensitiveWord bizSensitiveWord)
    {
        return bizSensitiveWordMapper.insertBizSensitiveWord(bizSensitiveWord);
    }

    /**
     * 修改敏感词
     *
     * @param bizSensitiveWord 敏感词
     * @return 结果
     */
    @Override
    public int updateBizSensitiveWord(BizSensitiveWord bizSensitiveWord)
    {
        return bizSensitiveWordMapper.updateBizSensitiveWord(bizSensitiveWord);
    }

    /**
     * 通过敏感词ID删除敏感词
     *
     * @param wordId 敏感词ID
     * @return 结果
     */
    @Override
    public int deleteBizSensitiveWordByWordId(Long wordId)
    {
        return bizSensitiveWordMapper.deleteBizSensitiveWordByWordId(wordId);
    }

    /**
     * 批量删除敏感词
     *
     * @param wordIds 需要删除的敏感词ID数组
     * @return 结果
     */
    @Override
    public int deleteBizSensitiveWordByWordIds(Long[] wordIds)
    {
        return bizSensitiveWordMapper.deleteBizSensitiveWordByWordIds(wordIds);
    }

    /**
     * 查询所有启用的敏感词
     *
     * @return 敏感词集合
     */
    @Override
    public List<BizSensitiveWord> selectAllEnabledWords()
    {
        return bizSensitiveWordMapper.selectAllEnabledWords();
    }
}
