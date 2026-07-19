package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.BizPhonebook;
import com.ruoyi.system.mapper.BizPhonebookMapper;
import com.ruoyi.system.service.IBizPhonebookService;

/**
 * 电话簿信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizPhonebookServiceImpl implements IBizPhonebookService
{
    @Autowired
    private BizPhonebookMapper bizPhonebookMapper;

    /**
     * 查询电话簿信息集合
     *
     * @param bizPhonebook 电话簿信息
     * @return 电话簿信息集合
     */
    @Override
    public List<BizPhonebook> selectBizPhonebookList(BizPhonebook bizPhonebook)
    {
        return bizPhonebookMapper.selectBizPhonebookList(bizPhonebook);
    }

    /**
     * 通过电话簿ID查询电话簿信息
     *
     * @param phonebookId 电话簿ID
     * @return 电话簿对象信息
     */
    @Override
    public BizPhonebook selectBizPhonebookById(Long phonebookId)
    {
        return bizPhonebookMapper.selectBizPhonebookById(phonebookId);
    }

    /**
     * 新增电话簿信息
     *
     * @param bizPhonebook 电话簿信息
     * @return 结果
     */
    @Override
    public int insertBizPhonebook(BizPhonebook bizPhonebook)
    {
        return bizPhonebookMapper.insertBizPhonebook(bizPhonebook);
    }

    /**
     * 修改电话簿信息
     *
     * @param bizPhonebook 电话簿信息
     * @return 结果
     */
    @Override
    public int updateBizPhonebook(BizPhonebook bizPhonebook)
    {
        return bizPhonebookMapper.updateBizPhonebook(bizPhonebook);
    }

    /**
     * 删除电话簿信息
     *
     * @param phonebookId 电话簿ID
     * @return 结果
     */
    @Override
    public int deleteBizPhonebookById(Long phonebookId)
    {
        return bizPhonebookMapper.deleteBizPhonebookById(phonebookId);
    }

    /**
     * 批量删除电话簿信息
     *
     * @param phonebookIds 需要删除的电话簿ID
     * @return 结果
     */
    @Override
    public int deleteBizPhonebookByIds(Long[] phonebookIds)
    {
        return bizPhonebookMapper.deleteBizPhonebookByIds(phonebookIds);
    }
}
