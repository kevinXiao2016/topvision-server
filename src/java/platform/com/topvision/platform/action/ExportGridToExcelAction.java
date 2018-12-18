/***********************************************************************
 * $Id: ExportGridToExcel.java,v1.0 2014-6-5 上午11:40:32 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.util.FtpClientUtil;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author vanzand
 * @created @2014-6-5-上午11:40:32
 * 
 */
@Controller("exportGridToExcelAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExportGridToExcelAction extends BaseAction {
    private static final long serialVersionUID = 6329616964537920844L;
    public static final String ROOTFOLDER = SystemConstants.ROOT_REAL_PATH + "/META-INF/gridExcelRootFolder/";
    public static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");

    private String exportContent;
    private String exportFile;
    private String excelData;
    private String filePath;

    public String exportGridToExcel() throws IOException {
        JSONObject result = new JSONObject();
        String fileName = "";
        // 获取文件路径
        try {
            File path = new File(ROOTFOLDER);
            path.mkdirs();
            fileName = exportFile + fileNameFormatter.format(new Date()) + ".xls";
            filePath = ROOTFOLDER + fileName;
            // 创造该文件
            File file = new File(filePath);
            file.createNewFile();
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            // 生成excel
            createExcelFile(workbook, exportFile, excelData);
            result.put("filePath", filePath);
        } catch (Exception e) {
            result.put("error", e.getMessage());
        }
        result.write(response.getWriter());
        return NONE;
    }

    public String downloadExcel() {
        File file = new File(filePath);
        FileInputStream fis = null;
        OutputStream out = null;
        try {
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            ServletActionContext.getResponse().setContentType("application/x-download");
            // 转码文件名，解决中文名的问题
            String fileName = file.getName();
            fileName = new String(fileName.getBytes(FtpClientUtil.GBK), FtpClientUtil.ISO);
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            int i;
            while ((i = fis.read(b)) > 0) {
                out.write(b, 0, i);
            }
        } catch (IOException e) {
        } finally {
            // 关闭流,并删除服务器端临时文件
            try {
                if (out != null)
                    out.close();
                if (fis != null)
                    fis.close();
                if (file.exists())
                    file.delete();
            } catch (IOException e) {
            }
        }
        return NONE;
    }

    private void createExcelFile(WritableWorkbook workbook, String title, String excelData) throws Exception,
            WriteException {
        WritableSheet sheet = workbook.createSheet("Sheet1", 0);
        // 开始填充excel内容
        JSONObject excelDataJson = JSONObject.fromObject(excelData);
        // 表头排序信息
        JSONArray columnSort = excelDataJson.getJSONArray("columnSort");
        // 表头内容信息
        JSONObject columns = excelDataJson.getJSONObject("columns");
        // 数据内容
        JSONArray excelContent = excelDataJson.getJSONArray("data");
        Label label;
        // 输出标题
        sheet.setRowView(0, 1000);
        WritableCellFormat titleFormat = getTitleFormat();
        WritableCellFormat columnFormat = getTitleCellFormat();
        WritableCellFormat contentFormat = getContentCellFormat();
        label = new Label(0, 0, title, titleFormat);
        sheet.addCell(label);
        sheet.mergeCells(0, 0, columnSort.size() - 1, 0);
        // 输出表头
        for (int i = 0; i < columnSort.size(); i++) {
            JSONObject column = columns.getJSONObject(columnSort.getString(i));
            sheet.setColumnView(i, column.getInt("width") / 7);
            label = new Label(i, 1, column.getString("title"), columnFormat);
            sheet.addCell(label);
        }
        // 输出内容数据
        JSONObject record = new JSONObject();
        for (int j = 0; j < excelContent.size(); j++) {
            record = excelContent.getJSONObject(j);
            for (int i = 0; i < columnSort.size(); i++) {
                label = new Label(i, j + 2, record.getString(columnSort.getString(i)), contentFormat);
                sheet.addCell(label);
            }
        }
        workbook.write();
        workbook.close();
    }

    private WritableCellFormat getTitleFormat() throws WriteException {
        WritableCellFormat cellFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 16,
                WritableFont.BOLD));
        cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
        return cellFormat;
    }

    private WritableCellFormat getTitleCellFormat() throws WriteException {
        WritableCellFormat titleCellFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10,
                WritableFont.BOLD));
        titleCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
        titleCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        titleCellFormat.setBackground(Colour.GRAY_25);
        titleCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        return titleCellFormat;
    }

    private WritableCellFormat getContentCellFormat() throws WriteException {
        WritableCellFormat contentCellFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10,
                WritableFont.NO_BOLD));
        contentCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
        contentCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        contentCellFormat.setWrap(true);
        return contentCellFormat;
    }

    public String getExportContent() {
        return exportContent;
    }

    public void setExportContent(String exportContent) {
        this.exportContent = exportContent;
    }

    public String getExportFile() {
        return exportFile;
    }

    public void setExportFile(String exportFile) {
        this.exportFile = exportFile;
    }

    public String getExcelData() {
        return excelData;
    }

    public void setExcelData(String excelData) {
        this.excelData = excelData;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
