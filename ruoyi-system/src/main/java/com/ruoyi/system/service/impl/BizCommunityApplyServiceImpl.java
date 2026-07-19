package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizCommunity;
import com.ruoyi.system.domain.BizCommunityApply;
import com.ruoyi.system.domain.SysRegion;
import com.ruoyi.system.mapper.BizCommunityApplyMapper;
import com.ruoyi.system.mapper.BizCommunityMapper;
import com.ruoyi.system.service.IBizCommunityApplyService;
import com.ruoyi.system.service.ISysRegionService;

/**
 * 小区申请信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class BizCommunityApplyServiceImpl implements IBizCommunityApplyService
{
    @Autowired
    private BizCommunityApplyMapper bizCommunityApplyMapper;

    @Autowired
    private BizCommunityMapper bizCommunityMapper;

    @Autowired
    private ISysRegionService sysRegionService;

    /**
     * 查询小区申请信息集合
     *
     * @param bizCommunityApply 小区申请信息
     * @return 小区申请信息集合
     */
    @Override
    public List<BizCommunityApply> selectBizCommunityApplyList(BizCommunityApply bizCommunityApply)
    {
        return bizCommunityApplyMapper.selectBizCommunityApplyList(bizCommunityApply);
    }

    /**
     * 通过申请ID查询小区申请信息
     *
     * @param applyId 申请ID
     * @return 小区申请对象信息
     */
    @Override
    public BizCommunityApply selectBizCommunityApplyByApplyId(Long applyId)
    {
        return bizCommunityApplyMapper.selectBizCommunityApplyByApplyId(applyId);
    }

    /**
     * 新增小区申请信息
     *
     * @param bizCommunityApply 小区申请信息
     * @return 结果
     */
    @Override
    public int insertBizCommunityApply(BizCommunityApply bizCommunityApply)
    {
        checkCommunityRegisterSwitch(bizCommunityApply.getProvince(), bizCommunityApply.getCity(), bizCommunityApply.getDistrict());
        // 默认申请状态为待审批
        if (StringUtils.isEmpty(bizCommunityApply.getApplyStatus()))
        {
            bizCommunityApply.setApplyStatus("0");
        }
        return bizCommunityApplyMapper.insertBizCommunityApply(bizCommunityApply);
    }

    /**
     * 修改小区申请信息
     *
     * @param bizCommunityApply 小区申请信息
     * @return 结果
     */
    @Override
    public int updateBizCommunityApply(BizCommunityApply bizCommunityApply)
    {
        return bizCommunityApplyMapper.updateBizCommunityApply(bizCommunityApply);
    }

    /**
     * 删除小区申请信息
     *
     * @param applyId 申请ID
     * @return 结果
     */
    @Override
    public int deleteBizCommunityApplyByApplyId(Long applyId)
    {
        return bizCommunityApplyMapper.deleteBizCommunityApplyByApplyId(applyId);
    }

    /**
     * 审批通过：更新申请记录状态为已通过，同时将申请的小区信息插入到小区表
     *
     * @param bizCommunityApply 小区申请信息（包含审批人和审批备注）
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approve(BizCommunityApply bizCommunityApply)
    {
        // 查询申请记录详情
        BizCommunityApply apply = bizCommunityApplyMapper.selectBizCommunityApplyByApplyId(bizCommunityApply.getApplyId());
        if (apply == null)
        {
            return 0;
        }
        // 更新申请记录状态为已通过
        apply.setApplyStatus("1");
        apply.setApproveBy(bizCommunityApply.getApproveBy());
        apply.setApproveTime(new Date());
        apply.setApproveRemark(bizCommunityApply.getApproveRemark());
        int rows = bizCommunityApplyMapper.updateBizCommunityApply(apply);

        // 将申请的小区信息插入到小区表
        if (rows > 0)
        {
            BizCommunity community = new BizCommunity();
            community.setCommunityName(apply.getCommunityName());
            community.setProvince(apply.getProvince());
            community.setCity(apply.getCity());
            community.setDistrict(apply.getDistrict());
            community.setAddress(apply.getAddress());
            community.setStatus("0");
            community.setCreateBy(apply.getApproveBy());
            bizCommunityMapper.insertBizCommunity(community);
        }
        return rows;
    }

    /**
     * 审批驳回：更新申请记录状态为已驳回（需要填写驳回原因）
     *
     * @param bizCommunityApply 小区申请信息（包含审批人和驳回原因）
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reject(BizCommunityApply bizCommunityApply)
    {
        // 查询申请记录详情
        BizCommunityApply apply = bizCommunityApplyMapper.selectBizCommunityApplyByApplyId(bizCommunityApply.getApplyId());
        if (apply == null)
        {
            return 0;
        }
        // 更新申请记录状态为已驳回
        apply.setApplyStatus("2");
        apply.setApproveBy(bizCommunityApply.getApproveBy());
        apply.setApproveTime(new Date());
        apply.setApproveRemark(bizCommunityApply.getApproveRemark());
        return bizCommunityApplyMapper.updateBizCommunityApply(apply);
    }

    /**
     * 校验小区登记开关是否开启
     *
     * @param province 省份
     * @param city 城市
     * @param district 区县
     */
    @Override
    public void checkCommunityRegisterSwitch(String province, String city, String district)
    {
        if (StringUtils.isEmpty(district))
        {
            return;
        }
        SysRegion region = sysRegionService.selectRegionByDistrictName(district);
        if (region == null)
        {
            throw new ServiceException("该地区未开放使用，请联系平台");
        }
        if ("1".equals(region.getCommunityRegisterSwitch()))
        {
            throw new ServiceException("该地区未开放使用，请联系平台");
        }
    }
}
