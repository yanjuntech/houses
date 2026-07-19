package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizSensitiveWord;

/**
 * 敏感词库 服务层
 *
 * @author ruoyi
 */
public interface IBizSensitiveWordService
{
    /**
     * 查询敏感词集合
     *
     * @param bizSensitiveWord 敏感词
     * @return 敏感词集合
     */
    public List<BizSensitiveWord> selectBizSensitiveWordList(BizSensitiveWord bizSensitiveWord);

    /**
     * 通过敏感词ID查询敏感词
     *
     * @param wordId 敏感词ID
     * @return 敏感词
     */
    public BizSensitiveWord selectBizSensitiveWordByWordId(Long wordId);

    /**
     * 新增敏感词
     *
     * @param bizSensitiveWord 敏感词
     * @return 结果
     */
    public int insertBizSensitiveWord(BizSensitiveWord bizSensitiveWord);

    /**
     * 修改敏感词
     *
     * @param bizSensitiveWord 敏感词
     * @return 结果
     */
    public int updateBizSensitiveWord(BizSensitiveWord bizSensitiveWord);

    /**
     * 通过敏感词ID删除敏感词
     *
     * @param wordId 敏感词ID
     * @return 结果
     */
    public int deleteBizSensitiveWordByWordId(Long wordId);

    /**
     * 批量删除敏感词
     *
     * @param wordIds 需要删除的敏感词ID数组
     * @return 结果
     */
    public int deleteBizSensitiveWordByWordIds(Long[] wordIds);

    /**
     * 查询所有启用的敏感词
     *
     * @return 敏感词集合
     */
    public List<BizSensitiveWord> selectAllEnabledWords();
}
