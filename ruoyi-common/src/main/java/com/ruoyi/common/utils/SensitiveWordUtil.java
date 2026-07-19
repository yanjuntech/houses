package com.ruoyi.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 敏感词过滤工具类
 * 基于 DFA（确定有限自动机）算法实现敏感词过滤
 *
 * @author ruoyi
 */
public class SensitiveWordUtil
{
    /** DFA 敏感词树根节点 */
    private static Map<String, Object> sensitiveWordMap = new HashMap<>();

    /** 敏感词终止标志 key */
    private static final String IS_END = "isEnd";

    /** 敏感词终止标志 - 结束 */
    private static final String END_YES = "1";

    /** 敏感词终止标志 - 未结束 */
    private static final String END_NO = "0";

    /** 替换字符 */
    private static final String REPLACEMENT = "***";

    /**
     * 初始化敏感词库
     * 将敏感词列表构建为 DFA 树
     *
     * @param words 敏感词列表
     */
    @SuppressWarnings("unchecked")
    public static synchronized void init(List<String> words)
    {
        if (StringUtils.isEmpty(words))
        {
            return;
        }
        Map<String, Object> newMap = new HashMap<>();
        for (String word : words)
        {
            if (StringUtils.isEmpty(word))
            {
                continue;
            }
            Map<String, Object> currentMap = newMap;
            for (int i = 0; i < word.length(); i++)
            {
                String ch = String.valueOf(word.charAt(i));
                Map<String, Object> nextMap = (Map<String, Object>) currentMap.get(ch);
                if (nextMap == null)
                {
                    nextMap = new HashMap<>();
                    nextMap.put(IS_END, END_NO);
                    currentMap.put(ch, nextMap);
                }
                // 最后一个字符标记为终止
                if (i == word.length() - 1)
                {
                    nextMap.put(IS_END, END_YES);
                }
                currentMap = nextMap;
            }
        }
        sensitiveWordMap = newMap;
    }

    /**
     * 检查文本是否包含敏感词
     *
     * @param text 待检查文本
     * @return 敏感词检查结果
     */
    public static SensitiveWordResult check(String text)
    {
        SensitiveWordResult result = new SensitiveWordResult();
        if (StringUtils.isEmpty(text) || sensitiveWordMap.isEmpty())
        {
            return result;
        }
        List<String> hitWords = new ArrayList<>();
        for (int i = 0; i < text.length(); i++)
        {
            int length = checkSensitiveWord(text, i);
            if (length > 0)
            {
                String hitWord = text.substring(i, i + length);
                if (!hitWords.contains(hitWord))
                {
                    hitWords.add(hitWord);
                }
                // 跳过已匹配的敏感词长度
                i = i + length - 1;
            }
        }
        if (!hitWords.isEmpty())
        {
            result.setHasSensitive(true);
            result.setHitWords(hitWords);
        }
        return result;
    }

    /**
     * 将文本中的敏感词替换为 ***
     *
     * @param text 待替换文本
     * @return 替换后的文本
     */
    public static String replace(String text)
    {
        if (StringUtils.isEmpty(text) || sensitiveWordMap.isEmpty())
        {
            return text;
        }
        StringBuilder builder = new StringBuilder(text);
        for (int i = 0; i < builder.length(); i++)
        {
            int length = checkSensitiveWord(builder.toString(), i);
            if (length > 0)
            {
                builder.replace(i, i + length, REPLACEMENT);
                // 替换后位置后移（替换字符长度为 3）
                i = i + REPLACEMENT.length() - 1;
            }
        }
        return builder.toString();
    }

    /**
     * 从指定位置开始检查敏感词
     *
     * @param text    文本
     * @param beginIndex 起始位置
     * @return 命中敏感词的长度，未命中返回 0
     */
    @SuppressWarnings("unchecked")
    private static int checkSensitiveWord(String text, int beginIndex)
    {
        Map<String, Object> currentMap = sensitiveWordMap;
        int length = 0;
        boolean isEnd = false;
        for (int i = beginIndex; i < text.length(); i++)
        {
            String ch = String.valueOf(text.charAt(i));
            Map<String, Object> nextMap = (Map<String, Object>) currentMap.get(ch);
            if (nextMap == null)
            {
                break;
            }
            length++;
            currentMap = nextMap;
            if (END_YES.equals(nextMap.get(IS_END)))
            {
                isEnd = true;
                break;
            }
        }
        return isEnd ? length : 0;
    }

    /**
     * 敏感词检查结果
     */
    public static class SensitiveWordResult
    {
        /** 是否包含敏感词 */
        private boolean hasSensitive;

        /** 命中的敏感词列表 */
        private List<String> hitWords;

        public SensitiveWordResult()
        {
            this.hasSensitive = false;
            this.hitWords = new ArrayList<>();
        }

        public boolean getHasSensitive()
        {
            return hasSensitive;
        }

        public void setHasSensitive(boolean hasSensitive)
        {
            this.hasSensitive = hasSensitive;
        }

        public List<String> getHitWords()
        {
            return hitWords;
        }

        public void setHitWords(List<String> hitWords)
        {
            this.hitWords = hitWords;
        }
    }
}
