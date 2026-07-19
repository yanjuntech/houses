package com.ruoyi.web.controller.rental;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
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
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizContract;
import com.ruoyi.system.domain.BizContractSignature;
import com.ruoyi.system.service.IBizContractService;

/**
 * 电子合同信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rental/contract")
public class BizContractController extends BaseController
{
    @Autowired
    private IBizContractService bizContractService;

    /**
     * 获取电子合同列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:contract:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizContract bizContract)
    {
        startPage();
        List<BizContract> list = bizContractService.selectBizContractList(bizContract);
        return getDataTable(list);
    }

    /**
     * 导出电子合同列表
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:contract:export')")
    @Log(title = "电子合同", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizContract bizContract)
    {
        List<BizContract> list = bizContractService.selectBizContractList(bizContract);
        ExcelUtil<BizContract> util = new ExcelUtil<BizContract>(BizContract.class);
        util.exportExcel(response, list, "电子合同数据");
    }

    /**
     * 根据合同ID获取详细信息（含签名列表）
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:contract:query')")
    @GetMapping(value = "/{contractId}")
    public AjaxResult getInfo(@PathVariable Long contractId)
    {
        return success(bizContractService.selectBizContractByContractId(contractId));
    }

    /**
     * 发起新合同
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:contract:add')")
    @Log(title = "电子合同", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizContract bizContract)
    {
        bizContract.setCreateBy(getUsername());
        return toAjax(bizContractService.insertBizContract(bizContract));
    }

    /**
     * 修改合同
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:contract:edit')")
    @Log(title = "电子合同", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizContract bizContract)
    {
        bizContract.setUpdateBy(getUsername());
        return toAjax(bizContractService.updateBizContract(bizContract));
    }

    /**
     * 删除合同
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:contract:remove')")
    @Log(title = "电子合同", businessType = BusinessType.DELETE)
    @DeleteMapping("/{contractIds}")
    public AjaxResult remove(@PathVariable Long[] contractIds)
    {
        return toAjax(bizContractService.deleteBizContractByContractIds(contractIds));
    }

    /**
     * 上传合同签名
     * 参数：contractId、signerId、signerName、signerRole、signatureImage（base64或URL）
     * 当出租方和承租方都签名后，自动将合同状态改为已签署
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:contract:edit')")
    @Log(title = "电子合同", businessType = BusinessType.UPDATE)
    @PostMapping("/uploadSignature")
    public AjaxResult uploadSignature(@RequestParam Long contractId,
                                      @RequestParam Long signerId,
                                      @RequestParam String signerName,
                                      @RequestParam String signerRole,
                                      @RequestParam String signatureImage)
    {
        BizContractSignature signature = new BizContractSignature();
        signature.setContractId(contractId);
        signature.setSignerId(signerId);
        signature.setSignerName(signerName);
        signature.setSignerRole(signerRole);
        signature.setSignatureImage(signatureImage);
        signature.setSignTime(DateUtils.getNowDate());
        signature.setCreateBy(getUsername());
        return toAjax(bizContractService.uploadSignature(signature));
    }

    /**
     * 完成签署
     * 将合同状态改为已签署，记录签署时间
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:contract:edit')")
    @Log(title = "电子合同", businessType = BusinessType.UPDATE)
    @PutMapping("/complete")
    public AjaxResult complete(@RequestBody BizContract bizContract)
    {
        bizContract.setUpdateBy(getUsername());
        return toAjax(bizContractService.completeContract(bizContract));
    }

    /**
     * 下载合同 PDF
     * 使用 iText 7 生成包含合同信息及签名图片的 PDF
     */
    @PreAuthorize("@ss.hasPermi('biz:rental:contract:download')")
    @Log(title = "电子合同", businessType = BusinessType.EXPORT)
    @GetMapping("/download/{contractId}")
    public void download(@PathVariable Long contractId, HttpServletResponse response)
    {
        BizContract contract = bizContractService.selectBizContractByContractId(contractId);
        if (StringUtils.isNull(contract))
        {
            throw new ServiceException("合同不存在");
        }
        try
        {
            byte[] pdfBytes = generateContractPdf(contract);
            String fileName = "合同_" + contract.getContractNo() + ".pdf";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition",
                    "attachment; filename=" + encodedFileName + ";filename*=utf-8''" + encodedFileName);
            response.setHeader("download-filename", encodedFileName);
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
            response.getOutputStream().write(pdfBytes);
            response.getOutputStream().flush();
        }
        catch (ServiceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger.error("生成合同PDF失败", e);
            throw new ServiceException("生成合同PDF失败：" + e.getMessage());
        }
    }

    /**
     * 生成合同 PDF
     *
     * @param contract 合同信息
     * @return PDF 字节数组
     */
    private byte[] generateContractPdf(BizContract contract) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDoc);
        // 中文字体
        PdfFont font = createChineseFont();
        document.setFont(font).setFontSize(11);

        // 1. 合同标题
        Paragraph title = new Paragraph(StringUtils.isNotEmpty(contract.getContractTitle())
                ? contract.getContractTitle() : "房屋租赁合同")
                .setFont(font).setFontSize(20).setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(title);

        // 2. 合同编号
        document.add(new Paragraph("合同编号：" + nullToEmpty(contract.getContractNo()))
                .setFont(font).setFontSize(11).setTextAlignment(TextAlignment.RIGHT).setMarginBottom(10));

        // 3. 基本信息
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{30, 70})).useAllAvailableWidth();
        infoTable.setBorder(new SolidBorder(1));
        addInfoRow(infoTable, font, "合同标题", nullToEmpty(contract.getContractTitle()));
        addInfoRow(infoTable, font, "合同编号", nullToEmpty(contract.getContractNo()));
        addInfoRow(infoTable, font, "房源标题", nullToEmpty(contract.getHouseTitle()));
        addInfoRow(infoTable, font, "所在小区", nullToEmpty(contract.getCommunityName()));
        addInfoRow(infoTable, font, "出租方姓名", nullToEmpty(contract.getLandlordName()));
        addInfoRow(infoTable, font, "出租方电话", nullToEmpty(contract.getLandlordPhone()));
        addInfoRow(infoTable, font, "承租方姓名", nullToEmpty(contract.getTenantName()));
        addInfoRow(infoTable, font, "承租方电话", nullToEmpty(contract.getTenantPhone()));
        addInfoRow(infoTable, font, "月租金（元）", contract.getMonthlyRent() == null ? "" : contract.getMonthlyRent().toPlainString());
        addInfoRow(infoTable, font, "押金（元）", contract.getDeposit() == null ? "" : contract.getDeposit().toPlainString());
        addInfoRow(infoTable, font, "租期（月）", contract.getRentPeriod() == null ? "" : String.valueOf(contract.getRentPeriod()));
        addInfoRow(infoTable, font, "起租日期", formatDate(contract.getStartDate()));
        addInfoRow(infoTable, font, "到期日期", formatDate(contract.getEndDate()));
        addInfoRow(infoTable, font, "支付周期", nullToEmpty(contract.getPayCycle()));
        addInfoRow(infoTable, font, "合同状态", statusToLabel(contract.getStatus()));
        addInfoRow(infoTable, font, "签署时间", formatDateTime(contract.getSignTime()));
        document.add(infoTable);
        document.add(new Paragraph("").setMarginBottom(10));

        // 4. 合同条款
        document.add(new Paragraph("一、合同条款").setFont(font).setFontSize(13).setBold().setMarginBottom(6));
        String content = StringUtils.isNotEmpty(contract.getContractContent())
                ? contract.getContractContent()
                : "甲乙双方经协商一致，就房屋租赁事宜达成如下协议：\n"
                + "1. 甲方将上述房屋出租给乙方使用；\n"
                + "2. 乙方应按时足额支付租金及押金；\n"
                + "3. 租赁期内，未经甲方同意，乙方不得擅自转租；\n"
                + "4. 乙方应爱护房屋及其设施，如有损坏应照价赔偿；\n"
                + "5. 本合同一式两份，甲乙双方各执一份，自双方签字之日起生效。";
        // 多行内容
        for (String line : content.split("\n"))
        {
            document.add(new Paragraph(line).setFont(font).setFontSize(11).setMarginBottom(2));
        }
        document.add(new Paragraph("").setMarginBottom(20));

        // 5. 签名区域
        document.add(new Paragraph("二、双方签名").setFont(font).setFontSize(13).setBold().setMarginBottom(6));
        Table signTable = new Table(UnitValue.createPercentArray(new float[]{50, 50})).useAllAvailableWidth();
        signTable.setBorder(Border.NO_BORDER);
        // 表头
        signTable.addHeaderCell(createHeaderCell(font, "出租方（甲方）"));
        signTable.addHeaderCell(createHeaderCell(font, "承租方（乙方）"));

        // 签名图片
        Image landlordImg = null;
        Image tenantImg = null;
        List<BizContractSignature> signatures = contract.getSignatureList();
        if (signatures != null)
        {
            for (BizContractSignature sig : signatures)
            {
                Image img = buildSignatureImage(sig.getSignatureImage());
                if (img != null)
                {
                    if ("1".equals(sig.getSignerRole()))
                    {
                        landlordImg = img;
                    }
                    else if ("2".equals(sig.getSignerRole()))
                    {
                        tenantImg = img;
                    }
                }
            }
        }
        signTable.addCell(createSignatureCell(font, landlordImg, contract.getLandlordName(), contract.getSignTime()));
        signTable.addCell(createSignatureCell(font, tenantImg, contract.getTenantName(), contract.getSignTime()));
        document.add(signTable);

        document.add(new Paragraph("").setMarginBottom(20));
        document.add(new Paragraph("本合同由系统自动生成，仅供电子签署使用。")
                .setFont(font).setFontSize(9).setTextAlignment(TextAlignment.CENTER).setItalic());

        document.close();
        return baos.toByteArray();
    }

    /**
     * 创建中文字体（优先使用 iText Asian 内置字体）
     */
    private PdfFont createChineseFont()
    {
        try
        {
            // iText font-asian 内置的 STSong-Light 字体支持中文
            return PdfFontFactory.createFont("STSong-Light", PdfEncodings.IDENTITY_H);
        }
        catch (Exception e)
        {
            logger.warn("加载中文字体 STSong-Light 失败，使用默认字体", e);
            try
            {
                return PdfFontFactory.createFont();
            }
            catch (Exception ex)
            {
                throw new ServiceException("创建PDF字体失败：" + ex.getMessage());
            }
        }
    }

    /**
     * 添加信息表行
     */
    private void addInfoRow(Table table, PdfFont font, String label, String value)
    {
        Cell labelCell = new Cell().add(new Paragraph(label).setFont(font).setFontSize(11))
                .setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(245, 245, 245))
                .setBorder(new SolidBorder(1));
        Cell valueCell = new Cell().add(new Paragraph(value).setFont(font).setFontSize(11))
                .setBorder(new SolidBorder(1));
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    /**
     * 创建表头单元格
     */
    private Cell createHeaderCell(PdfFont font, String text)
    {
        return new Cell().add(new Paragraph(text).setFont(font).setFontSize(12).setBold()
                .setTextAlignment(TextAlignment.CENTER))
                .setBorder(new SolidBorder(1))
                .setTextAlignment(TextAlignment.CENTER);
    }

    /**
     * 创建签名单元格
     */
    private Cell createSignatureCell(PdfFont font, Image image, String signerName, Date signTime)
    {
        Cell cell = new Cell().setBorder(new SolidBorder(1)).setPadding(5);
        if (image != null)
        {
            cell.add(image.setHorizontalAlignment(HorizontalAlignment.CENTER));
        }
        cell.add(new Paragraph("签名：" + nullToEmpty(signerName)).setFont(font).setFontSize(10));
        cell.add(new Paragraph("签署日期：" + formatDateTime(signTime)).setFont(font).setFontSize(10));
        return cell;
    }

    /**
     * 根据签名内容（base64 或 URL）构建签名图片对象
     */
    private Image buildSignatureImage(String signatureImage)
    {
        if (StringUtils.isEmpty(signatureImage))
        {
            return null;
        }
        try
        {
            // base64 形式（兼容 data:image/png;base64,xxx 与纯 base64）
            if (signatureImage.startsWith("data:"))
            {
                int idx = signatureImage.indexOf(",");
                String base64 = idx > 0 ? signatureImage.substring(idx + 1) : signatureImage;
                byte[] bytes = Base64.getDecoder().decode(base64);
                Image img = new Image(ImageDataFactory.create(bytes));
                img.scaleToFit(150, 80);
                return img;
            }
            else if (signatureImage.length() > 200)
            {
                // 长字符串当作 base64 处理
                byte[] bytes = Base64.getDecoder().decode(signatureImage);
                Image img = new Image(ImageDataFactory.create(bytes));
                img.scaleToFit(150, 80);
                return img;
            }
            else
            {
                // URL 形式
                Image img = new Image(ImageDataFactory.create(signatureImage));
                img.scaleToFit(150, 80);
                return img;
            }
        }
        catch (Exception e)
        {
            logger.warn("加载签名图片失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 空值转空串
     */
    private String nullToEmpty(String s)
    {
        return s == null ? "" : s;
    }

    /**
     * 格式化日期（yyyy-MM-dd）
     */
    private String formatDate(Date date)
    {
        return date == null ? "" : DateUtils.parseDateToStr("yyyy-MM-dd", date);
    }

    /**
     * 格式化日期时间（yyyy-MM-dd HH:mm:ss）
     */
    private String formatDateTime(Date date)
    {
        return date == null ? "" : DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", date);
    }

    /**
     * 合同状态码转中文
     */
    private String statusToLabel(String status)
    {
        if (status == null)
        {
            return "";
        }
        switch (status)
        {
            case "0": return "待签署";
            case "1": return "已签署";
            case "2": return "已取消";
            case "3": return "已过期";
            default: return status;
        }
    }
}
