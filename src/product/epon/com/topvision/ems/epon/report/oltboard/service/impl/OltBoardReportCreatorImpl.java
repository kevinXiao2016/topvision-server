/***********************************************************************
 * $Id: OltBoardReportCreatorImpl.java,v1.0 2013-10-26 上午9:40:08 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltboard.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.domain.EponBoardStatistics;
import com.topvision.ems.epon.report.oltboard.dao.OltBoardReportDao;
import com.topvision.ems.epon.report.oltboard.service.OltBoardReportCreator;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.service.BaseService;

/**
 * @author haojie
 * @created @2013-10-26-上午9:40:08
 * 
 */
@Service("oltBoardReportCreator")
public class OltBoardReportCreatorImpl extends BaseService implements OltBoardReportCreator {
    @Autowired
    private OltBoardReportDao oltBoardReportDao;
    @Autowired
    private ReportInstanceService reportInstanceService;

    @Override
    public void bulidReport(ReportTask task) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sortName", "ip");

        Map<String, Object> condition = task.getCondition();
        Date date = new Date();
        List<EponBoardStatistics> eponBoardStatistics = oltBoardReportDao.getBoardList(map);

        Map<String, Boolean> columnDisable = new HashMap<String, Boolean>();
        columnDisable.put("mpuaDisplayable", (Boolean) condition.get("mpuaDisplayable"));
        columnDisable.put("mpubDisplayable", (Boolean) condition.get("mpubDisplayable"));
        columnDisable.put("geuaDisplayable", (Boolean) condition.get("geuaDisplayable"));
        columnDisable.put("geubDisplayable", (Boolean) condition.get("geubDisplayable"));
        columnDisable.put("xguaDisplayable", (Boolean) condition.get("xguaDisplayable"));
        columnDisable.put("xgubDisplayable", (Boolean) condition.get("xgubDisplayable"));
        columnDisable.put("xgucDisplayable", (Boolean) condition.get("xgucDisplayable"));
        columnDisable.put("epuaDisplayable", (Boolean) condition.get("epuaDisplayable"));
        columnDisable.put("epubDisplayable", (Boolean) condition.get("epubDisplayable"));

        try {
            if (task.isExcelEnabled()) {
                String filePath = exportAsExcelFromTask(task, eponBoardStatistics, columnDisable, date);
                reportInstanceService.addReportInstance(filePath, EXCEL, task);
            }
        } catch (Exception e) {
            logger.debug("bulidReport method is error:{}", e);
        }
    }

    @Override
    public void exportOltBoardReportToExcel(List<EponBoardStatistics> eponBoardStatistics,
            Map<String, Boolean> columnDisable, Date statDate) {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");

        String fileName;
        try {
            fileName = URLEncoder.encode(ReportTaskUtil.getString("report.oltBoardReport", "report") + "-"
                    + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            createOltBoardReportExcelFile(eponBoardStatistics, columnDisable, workbook, statDate);
        } catch (UnsupportedEncodingException e) {
            logger.debug("unsupported Encoding Exception:", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.debug("something wrong with I/O:", e);
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (out != null) {
                    out.flush();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.debug("close fileStream error:{}", e);
            }
        }
    }

    /**
     * 任务报表导出为 Excel
     * 
     * @param task
     * @param eponBoardStatistics
     * @return
     * @throws UnsupportedEncodingException
     */
    private String exportAsExcelFromTask(ReportTask task, List<EponBoardStatistics> eponBoardStatistics,
            Map<String, Boolean> columnDisable, Date date) throws UnsupportedEncodingException {
        String filePath = ReportExportUtil.getExportUrl(task, EXCEL);
        File file = new File(filePath);
        try {
            file.createNewFile();
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            createOltBoardReportExcelFile(eponBoardStatistics, columnDisable, workbook, date);
        } catch (IOException e) {
            logger.debug("exportAsExcelFromTask method is error:{}", e);
        }
        return filePath;
    }

    /**
     * 为excel文件填充内容
     * 
     * @param eponBoardStatistics
     * @param columnDisable
     * @param workbook
     */
    private void createOltBoardReportExcelFile(List<EponBoardStatistics> eponBoardStatistics,
            Map<String, Boolean> columnDisable, WritableWorkbook workbook, Date date) {
        try {
            Label label;
            // 创建表头等信息
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);

            int titleCls = 4;
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();

            label = new Label(0, 2, ReportTaskUtil.getString("report.deviceIP", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 2, ReportTaskUtil.getString("report.deviceName", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 2, ReportTaskUtil.getString("report.totalBoard", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 2, ReportTaskUtil.getString("report.onlineBoard", "report"), titleCellFormat);
            sheet.addCell(label);
            if (columnDisable.get("mpuaDisplayable")) {
                label = new Label(titleCls++, 2, "MPUA", titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("mpubDisplayable")) {
                label = new Label(titleCls++, 2, "MPUB", titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("geuaDisplayable")) {
                label = new Label(titleCls++, 2, "GEUA", titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("geubDisplayable")) {
                label = new Label(titleCls++, 2, "GEUB", titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("xguaDisplayable")) {
                label = new Label(titleCls++, 2, "XGUA", titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("xgubDisplayable")) {
                label = new Label(titleCls++, 2, "XGUB", titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("xgucDisplayable")) {
                label = new Label(titleCls++, 2, "XGUC", titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("epuaDisplayable")) {
                label = new Label(titleCls++, 2, "EPUA", titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("epubDisplayable")) {
                label = new Label(titleCls++, 2, "EPUB", titleCellFormat);
                sheet.addCell(label);
            }
            // 第1列为设备IP，偏长，需要设置宽度
            sheet.setColumnView(0, 30);
            for (int i = 1; i < titleCls; i++) {
                sheet.setColumnView(i, 15);
            }

            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.oltBoardReport", "report"), date,
                    titleCls);

            Integer rowNum = 3;
            for (int i = 0; i < eponBoardStatistics.size(); i++) {
                label = new Label(0, rowNum, eponBoardStatistics.get(i).getIp(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, eponBoardStatistics.get(i).getName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, String.valueOf(eponBoardStatistics.get(i).getAllSlot()), contentCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, String.valueOf(eponBoardStatistics.get(i).getOnline()), contentCellFormat);
                sheet.addCell(label);
                int cls = 4;
                if (columnDisable.get("mpuaDisplayable")) {
                    label = new Label(cls++, rowNum, String.valueOf(eponBoardStatistics.get(i).getMpua()),
                            contentCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("mpubDisplayable")) {
                    label = new Label(cls++, rowNum, String.valueOf(eponBoardStatistics.get(i).getMpub()),
                            contentCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("geuaDisplayable")) {
                    label = new Label(cls++, rowNum, String.valueOf(eponBoardStatistics.get(i).getGeua()),
                            contentCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("geubDisplayable")) {
                    label = new Label(cls++, rowNum, String.valueOf(eponBoardStatistics.get(i).getGeub()),
                            contentCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("xguaDisplayable")) {
                    label = new Label(cls++, rowNum, String.valueOf(eponBoardStatistics.get(i).getXgua()),
                            contentCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("xgubDisplayable")) {
                    label = new Label(cls++, rowNum, String.valueOf(eponBoardStatistics.get(i).getXgub()),
                            contentCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("xgucDisplayable")) {
                    label = new Label(cls++, rowNum, String.valueOf(eponBoardStatistics.get(i).getXguc()),
                            contentCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("epuaDisplayable")) {
                    label = new Label(cls++, rowNum, String.valueOf(eponBoardStatistics.get(i).getEpua()),
                            contentCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("epubDisplayable")) {
                    label = new Label(cls++, rowNum, String.valueOf(eponBoardStatistics.get(i).getEpub()),
                            contentCellFormat);
                    sheet.addCell(label);
                }
                rowNum++;
            }

            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("createOltBoardReportExcelFile method is error:{}", e);
        }
    }

    @Override
    public List<EponBoardStatistics> getBoardList(Map<String, Object> map) {
        return oltBoardReportDao.getBoardList(map);
    }

}
