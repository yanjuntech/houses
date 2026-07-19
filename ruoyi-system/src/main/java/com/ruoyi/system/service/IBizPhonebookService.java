package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizPhonebook;

/**
 * 电话簿信息 服务层
 *
 * @author ruoyi
 */
public interface IBizPhonebookService
{
    /**
     * 查询电话簿信息集合
     *
     * @param bizPhonebook 电话簿信息
     * @return 电话簿信息集合
     */
    public List<BizPhonebook> selectBizPhonebookList(BizPhonebook bizPhonebook);

    /**
     * 通过电话簿ID查询电话簿信息
     *
     * @param phonebookId 电话簿ID
     * @return 电话簿对象信息
     */
    public BizPhonebook selectBizPhonebookById(Long phonebookId);

    /**
     * 新增电话簿信息
     *
     * @param bizPhonebook 电话簿信息
     * @return 结果
     */
    public int insertBizPhonebook(BizPhonebook bizPhonebook);

    /**
     * 修改电话簿信息
     *
     * @param bizPhonebook 电话簿信息
     * @return 结果
     */
    public int updateBizPhonebook(BizPhonebook bizPhonebook);

    /**
     * 删除电话簿信息
     *
     * @param phonebookId 电话簿ID
     * @return 结果
     */
    public int deleteBizPhonebookById(Long phonebookId);

    /**
     * 批量删除电话簿信息
     *
     * @param phonebookIds 需要删除的电话簿ID
     * @return 结果
     */
    public int deleteBizPhonebookByIds(Long[] phonebookIds);
}
