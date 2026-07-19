package com.ruoyi.web.controller.rental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizCommunity;
import com.ruoyi.system.domain.BizExchangeQuota;
import com.ruoyi.system.domain.BizHouse;
import com.ruoyi.system.domain.BizMiniappUser;
import com.ruoyi.system.domain.vo.BizHouseImportPreviewVo;
import com.ruoyi.system.domain.vo.BizHouseImportVo;
import com.ruoyi.system.mapper.BizMiniappUserMapper;
import com.ruoyi.system.service.IBizCommunityService;
import com.ruoyi.system.service.IBizExchangeQuotaService;
import com.ruoyi.system.service.IBizHouseService;

/**
 * 房屋信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/house")
public class BizHouseController extends BaseController
{
    @Autowired
    private IBizHouseService bizHouseService;

    @Autowired
    private IBizExchangeQuotaService bizExchangeQuotaService;

    @Autowired
    private IBizCommunityService bizCommunityService;

    @Autowired
    private BizMiniappUserMapper bizMiniappUserMapper;

    /**
     * 兑换配额类型：房屋发布次数
     */
    private static final String QUOTA_TYPE_HOUSE_PUBLISH = "HOUSE_PUBLISH";

    /**
     * 获取房屋列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:house:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizHouse bizHouse)
    {
        startPage();
        List<BizHouse> list = bizHouseService.selectBizHouseList(bizHouse);
        return getDataTable(list);
    }

    /**
     * 导出房屋列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:house:export')")
    @Log(title = "房屋管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizHouse bizHouse)
    {
        List<BizHouse> list = bizHouseService.selectBizHouseList(bizHouse);
        ExcelUtil<BizHouse> util = new ExcelUtil<BizHouse>(BizHouse.class);
        util.exportExcel(response, list, "房屋数据");
    }

    /**
     * 根据房屋ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:house:query')")
    @GetMapping(value = "/{houseId}")
    public AjaxResult getInfo(@PathVariable Long houseId)
    {
        return success(bizHouseService.selectBizHouseByHouseId(houseId));
    }

    /**
     * 新增房屋
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:house:add')")
    @Log(title = "房屋管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizHouse bizHouse)
    {
        bizHouse.setCreateBy(getUsername());
        return toAjax(bizHouseService.insertBizHouse(bizHouse));
    }

    /**
     * 修改房屋
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:house:edit')")
    @Log(title = "房屋管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizHouse bizHouse)
    {
        bizHouse.setUpdateBy(getUsername());
        return toAjax(bizHouseService.updateBizHouse(bizHouse));
    }

    /**
     * 删除房屋
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:house:remove')")
    @Log(title = "房屋管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{houseIds}")
    public AjaxResult remove(@PathVariable Long[] houseIds)
    {
        return toAjax(bizHouseService.deleteBizHouseByHouseIds(houseIds));
    }

    /**
     * 修改房屋上下架状态
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:house:changeStatus')")
    @Log(title = "房屋管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody BizHouse bizHouse)
    {
        bizHouse.setUpdateBy(getUsername());
        return toAjax(bizHouseService.updateBizHouseStatus(bizHouse));
    }

    /**
     * 下载房屋批量导入模板
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:house:import')")
    @Log(title = "房屋管理", businessType = BusinessType.EXPORT)
    @PostMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response)
    {
        ExcelUtil<BizHouseImportVo> util = new ExcelUtil<BizHouseImportVo>(BizHouseImportVo.class);
        util.importTemplateExcel(response, "房屋数据");
    }

    /**
     * 预览导入：解析 Excel 并校验数据，返回预览结果及配额是否充足
     *
     * @param file 上传的 Excel 文件
     * @param userId 用户ID
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:house:import')")
    @PostMapping("/previewImport")
    public AjaxResult previewImport(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId)
            throws Exception
    {
        if (file == null || file.isEmpty())
        {
            return error("上传文件不能为空");
        }
        if (userId == null)
        {
            return error("用户ID不能为空");
        }
        // 解析 Excel
        ExcelUtil<BizHouseImportVo> util = new ExcelUtil<BizHouseImportVo>(BizHouseImportVo.class);
        List<BizHouseImportVo> dataList = util.importExcel(file.getInputStream());

        BizHouseImportPreviewVo preview = new BizHouseImportPreviewVo();
        List<String> errorList = new ArrayList<String>();
        List<BizHouseImportVo> validList = new ArrayList<BizHouseImportVo>();

        int total = dataList == null ? 0 : dataList.size();
        int valid = 0;
        if (total > 0)
        {
            for (int i = 0; i < dataList.size(); i++)
            {
                BizHouseImportVo vo = dataList.get(i);
                int rowNum = i + 1;
                String err = validateImportVo(vo, rowNum);
                if (err != null)
                {
                    errorList.add(err);
                }
                else
                {
                    validList.add(vo);
                    valid++;
                }
            }
        }

        // 查询用户剩余配额
        int remaining = getRemainingQuota(userId);
        boolean enough = remaining >= valid;

        preview.setTotalCount(total);
        preview.setValidCount(valid);
        preview.setInvalidCount(total - valid);
        preview.setRemainingQuota(remaining);
        preview.setEnoughQuota(enough);
        preview.setDataList(validList);
        preview.setErrorList(errorList);

        return success(preview);
    }

    /**
     * 确认导入：扣减配额并批量写入房屋
     *
     * @param userId 用户ID
     * @param dataList 确认导入的数据列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:house:import')")
    @Log(title = "房屋管理", businessType = BusinessType.IMPORT)
    @PostMapping("/confirmImport")
    public AjaxResult confirmImport(@RequestParam("userId") Long userId, @RequestBody List<BizHouseImportVo> dataList)
    {
        if (userId == null)
        {
            return error("用户ID不能为空");
        }
        if (dataList == null || dataList.isEmpty())
        {
            return error("导入数据不能为空");
        }

        // 校验配额是否足够
        int remaining = getRemainingQuota(userId);
        if (remaining < dataList.size())
        {
            return error("导入数量超过可用配额，请先兑换");
        }

        // 查询发布者用户类型（1房东 2中介），若用户不存在则默认房东
        BizMiniappUser publishUser = bizMiniappUserMapper.selectBizMiniappUserByUserId(userId);
        String publishUserType = "1";
        if (publishUser != null && StringUtils.isNotEmpty(publishUser.getUserType()))
        {
            // userType: 0普通 1房东 2中介；发布者类型 publish_user_type: 1房东 2中介
            if ("2".equals(publishUser.getUserType()))
            {
                publishUserType = "2";
            }
        }

        String operName = getUsername();
        int successCount = 0;
        int failureCount = 0;
        List<String> errorList = new ArrayList<String>();

        for (int i = 0; i < dataList.size(); i++)
        {
            BizHouseImportVo vo = dataList.get(i);
            int rowNum = i + 1;
            String err = validateImportVo(vo, rowNum);
            if (err != null)
            {
                failureCount++;
                errorList.add(err);
                continue;
            }
            // 根据小区名称查询小区ID（精确匹配）
            BizCommunity community = findCommunityByName(vo.getCommunityName());
            if (community == null)
            {
                failureCount++;
                errorList.add("第" + rowNum + "行：小区[" + vo.getCommunityName() + "]不存在");
                continue;
            }

            try
            {
                BizHouse house = new BizHouse();
                house.setTitle(vo.getTitle());
                house.setHouseType(vo.getHouseType());
                house.setCommunityId(community.getCommunityId());
                house.setCommunityName(community.getCommunityName());
                house.setRoomCount(vo.getRoomCount());
                house.setHallCount(vo.getHallCount());
                house.setBathCount(vo.getBathCount());
                house.setArea(vo.getArea());
                house.setFloor(vo.getFloor());
                house.setTotalFloor(vo.getTotalFloor());
                house.setDecoration(vo.getDecoration());
                house.setOrientation(vo.getOrientation());
                house.setPrice(vo.getPrice());
                house.setDescription(vo.getDescription());
                house.setPublishUserId(userId);
                house.setPublishUserType(publishUserType);
                house.setStatus("1");
                house.setCreateBy(operName);
                bizHouseService.insertBizHouse(house);
                successCount++;
            }
            catch (ServiceException se)
            {
                failureCount++;
                errorList.add("第" + rowNum + "行：" + se.getMessage());
            }
            catch (Exception ex)
            {
                failureCount++;
                errorList.add("第" + rowNum + "行：保存失败，" + ex.getMessage());
            }
        }

        // 仅当全部成功时才扣减配额（避免部分成功后扣减全部数量导致用户损失）
        if (successCount > 0 && failureCount == 0)
        {
            bizExchangeQuotaService.deductQuota(userId, QUOTA_TYPE_HOUSE_PUBLISH, successCount);
        }
        else if (successCount > 0)
        {
            // 部分成功时仅扣减成功部分
            bizExchangeQuotaService.deductQuota(userId, QUOTA_TYPE_HOUSE_PUBLISH, successCount);
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("successCount", successCount);
        result.put("failureCount", failureCount);
        result.put("errorList", errorList);
        return success(result);
    }

    /**
     * 校验导入行数据
     *
     * @param vo 导入对象
     * @param rowNum 行号（从1开始）
     * @return 错误信息，校验通过返回 null
     */
    private String validateImportVo(BizHouseImportVo vo, int rowNum)
    {
        if (vo == null)
        {
            return "第" + rowNum + "行：数据为空";
        }
        if (StringUtils.isEmpty(vo.getTitle()))
        {
            return "第" + rowNum + "行：标题不能为空";
        }
        if (StringUtils.isEmpty(vo.getCommunityName()))
        {
            return "第" + rowNum + "行：小区名称不能为空";
        }
        if (vo.getPrice() == null)
        {
            return "第" + rowNum + "行：价格不能为空";
        }
        if (StringUtils.isEmpty(vo.getHouseType()))
        {
            return "第" + rowNum + "行：租赁类型不能为空";
        }
        if (!"1".equals(vo.getHouseType()) && !"2".equals(vo.getHouseType())
                && !"3".equals(vo.getHouseType()) && !"4".equals(vo.getHouseType()))
        {
            return "第" + rowNum + "行：租赁类型只能为1出租、2求租、3合租、4出售";
        }
        return null;
    }

    /**
     * 查询用户房屋发布剩余配额
     *
     * @param userId 用户ID
     * @return 剩余数量，无记录返回0
     */
    private int getRemainingQuota(Long userId)
    {
        BizExchangeQuota quota = bizExchangeQuotaService.selectByUserIdAndType(userId, QUOTA_TYPE_HOUSE_PUBLISH);
        if (quota == null || quota.getRemainingCount() == null)
        {
            return 0;
        }
        return quota.getRemainingCount();
    }

    /**
     * 根据小区名称精确匹配小区
     *
     * @param communityName 小区名称
     * @return 小区对象，未找到返回 null
     */
    private BizCommunity findCommunityByName(String communityName)
    {
        if (StringUtils.isEmpty(communityName))
        {
            return null;
        }
        BizCommunity query = new BizCommunity();
        query.setCommunityName(communityName);
        List<BizCommunity> list = bizCommunityService.selectBizCommunityList(query);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        // 优先精确匹配
        for (BizCommunity c : list)
        {
            if (communityName.equals(c.getCommunityName()))
            {
                return c;
            }
        }
        // 退而取第一条 LIKE 命中
        return list.get(0);
    }
}
