/***********************************************************************
 * $Id: OltRunningStatusReportCreatorImpl.java,v1.0 2013-10-29 上午11:48:04 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.oltrunningstatus.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.report.domain.OltPonRunningStatus;
import com.topvision.ems.cmc.report.domain.OltRunningStatus;
import com.topvision.ems.cmc.report.oltrunningstatus.dao.OltRunningStatusReportDao;
import com.topvision.ems.cmc.report.oltrunningstatus.service.OltRunningStatusReportCreator;
import com.topvision.ems.report.domain.ExcutorTimePolicy;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.domain.StartStopTime;
import com.topvision.ems.report.domain.TopoEntityStastic;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.service.StatReportService;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;

/**
 * @author haojie
 * @created @2013-10-29-上午11:48:04
 * 
 */
@Service("oltRunningStatusReportCreator")
public class OltRunningStatusReportCreatorImpl extends BaseService implements OltRunningStatusReportCreator {
    @Autowired
    private OltRunningStatusReportDao oltRunningStatusReportDao;
    @Autowired
    private StatReportService statReportService;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void bulidReport(ReportTask reportTask) {
        Map<String, Object> parMap = new HashMap<String, Object>();
        String colTime = ReportTaskUtil.getTimeSegmentSql(reportTask, "d.collectTime");
        parMap.put("sql", colTime);
        parMap.put("userId", reportTask.getCondition("userId"));
        Map<String, List<OltRunningStatus>> oltRunningReport = statOltRunningStatusReport(parMap);
        Date statDate = new Date();
        // 根据task整理出conditions
        String conditions = getTaskConditions(reportTask);
        if (reportTask.isExcelEnabled()) {
            try {
                String filePath = ReportExportUtil.getExportUrl(reportTask, EXCEL);
                OutputStream out = new FileOutputStream(filePath);
                exportOltRunningStatus(reportTask, oltRunningReport, statDate, out, conditions);
                reportInstanceService.addReportInstance(filePath, EXCEL, reportTask);
            } catch (FileNotFoundException e) {
                logger.error("bulidReport method is error:{}", e);
            }
        }

    }

    /**
     * 得到导出的Excel的条件字符串
     * 
     * @param task
     * @return
     */
    private String getTaskConditions(ReportTask task) {
        ExcutorTimePolicy policy = ReportTaskUtil.decodeCronExpression(task);
        List<StartStopTime> stAndEtTimes = ReportTaskUtil.getStAndEtTime(policy, new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(ReportTaskUtil.getString("report.statisticsTimen", "report"));
        for (StartStopTime time : stAndEtTimes) {
            Date st = time.getStTime();
            Date et = time.getEtTime();
            sb.append(formatter.format(st)).append(ReportTaskUtil.getString("report.timeTo", "report"))
                    .append(formatter.format(et)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public Map<String, List<OltRunningStatus>> statOltRunningStatusReport(Map<String, Object> map) {
        List<TopoEntityStastic> relates = statReportService.getTopoEntityRelation(entityTypeService.getOltType(), map);
        return oltRunningStatusReportDao.statOltRunningStatusReport(relates, map);
    }

    @Override
    public void exportOltRunningStatusToExcel(Map<String, Object> parMap, Date statDate, OutputStream out) {
        Map<String, List<OltRunningStatus>> oltRunningReport = statOltRunningStatusReport(parMap);
        String condition = String.format(ReportTaskUtil.getString("report.statisticsTimeTo", "report"),
                parMap.get("stTime"), parMap.get("etTime"));
        exportOltRunningStatus(null, oltRunningReport, statDate, out, condition);

    }

    /**
     * 实际的统计报表的导出
     * 
     * @param task
     * @param oltRunningReport
     * @param statDate
     * @param out
     */
    private void exportOltRunningStatus(ReportTask task, Map<String, List<OltRunningStatus>> oltRunningReport,
            Date statDate, OutputStream out, String conditions) {
        try {
            Label label;
            // 创建表头等信息
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);
            sheet.setColumnView(0, 15);
            sheet.setColumnView(1, 15);
            sheet.setColumnView(2, 20);
            sheet.setColumnView(3, 15);
            sheet.setColumnView(4, 15);
            sheet.setColumnView(5, 15);
            sheet.setColumnView(6, 15);
            int titleCell = 7;
            ReportTaskUtil.formatExcelHeader(sheet,
                    ReportTaskUtil.getString("report.oltRunningStatusReport", "report"), statDate, conditions,
                    titleCell);
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();

            label = new Label(0, 5, ReportTaskUtil.getString("report.folder", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 5, ReportTaskUtil.getString("report.oltName", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 5, ReportTaskUtil.getString("report.sniUsageTop", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 5, ReportTaskUtil.getString("report.ponPortStatistics", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 5, "", titleCellFormat);
            sheet.addCell(label);
            label = new Label(5, 5, "", titleCellFormat);
            sheet.addCell(label);
            label = new Label(6, 5, "", titleCellFormat);
            sheet.addCell(label);

            label = new Label(0, 6, "", titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 6, "", titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 6, "", titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 6, ReportTaskUtil.getString("report.ponPortName", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 6, ReportTaskUtil.getString("report.usageTop", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(5, 6, ReportTaskUtil.getString("report.ccmtsRealtimeNum", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(6, 6, ReportTaskUtil.getString("report.cmNum", "report"), titleCellFormat);
            sheet.addCell(label);
            // 合并表头
            sheet.mergeCells(0, 5, 0, 6);
            sheet.mergeCells(1, 5, 1, 6);
            sheet.mergeCells(2, 5, 2, 6);
            sheet.mergeCells(3, 5, 6, 5);
            int rowNum = 7;
            int oltRowNum = 7;
            int folderRowNum = 7;

            WritableCellFormat cFormat = ReportTaskUtil.getContentCellFormat();
            // 绘制表格
            for (String folderName : oltRunningReport.keySet()) {
                List<OltRunningStatus> oltRunningStatusList = oltRunningReport.get(folderName);
                if (oltRunningStatusList != null && oltRunningStatusList.size() != 0) {
                    for (int i = 0; i < oltRunningStatusList.size(); i++) {
                        for (int j = 0; j < oltRunningStatusList.get(i).getOltPonRunningStatusList().size(); j++) {
                            OltPonRunningStatus oltPonRunningStatus = oltRunningStatusList.get(i)
                                    .getOltPonRunningStatusList().get(j);
                            if (i == 0 && j == 0) {// 每个地域的第一行
                                label = new Label(0, rowNum, folderName, cFormat);
                                sheet.addCell(label);
                                label = new Label(1, rowNum, oltPonRunningStatus.getOltName(), cFormat);
                                sheet.addCell(label);
                                label = new Label(2, rowNum, oltPonRunningStatus.getSniUsageString(), cFormat);
                                sheet.addCell(label);
                                label = new Label(3, rowNum, oltPonRunningStatus.getPonName(), cFormat);
                                sheet.addCell(label);
                                label = new Label(4, rowNum, oltPonRunningStatus.getPonUsage(), cFormat);
                                sheet.addCell(label);
                                label = new Label(5, rowNum, oltPonRunningStatus.getCcNum().toString(), cFormat);
                                sheet.addCell(label);
                                label = new Label(6, rowNum, oltPonRunningStatus.getCmNum().toString(), cFormat);
                                sheet.addCell(label);
                                rowNum++;
                            } else if (i != 0 && j == 0) {// 每个设备的第一行,不是地域的第一行
                                label = new Label(0, rowNum, "", cFormat);
                                sheet.addCell(label);
                                label = new Label(1, rowNum, oltPonRunningStatus.getOltName(), cFormat);
                                sheet.addCell(label);
                                label = new Label(2, rowNum, oltPonRunningStatus.getSniUsageString(), cFormat);
                                sheet.addCell(label);
                                label = new Label(3, rowNum, oltPonRunningStatus.getPonName(), cFormat);
                                sheet.addCell(label);
                                label = new Label(4, rowNum, oltPonRunningStatus.getPonUsage(), cFormat);
                                sheet.addCell(label);
                                label = new Label(5, rowNum, oltPonRunningStatus.getCcNum().toString(), cFormat);
                                sheet.addCell(label);
                                label = new Label(6, rowNum, oltPonRunningStatus.getCmNum().toString(), cFormat);
                                sheet.addCell(label);
                                rowNum++;
                            } else {
                                label = new Label(0, rowNum, "", cFormat);
                                sheet.addCell(label);
                                label = new Label(1, rowNum, "", cFormat);
                                sheet.addCell(label);
                                label = new Label(2, rowNum, "", cFormat);
                                sheet.addCell(label);
                                label = new Label(3, rowNum, oltPonRunningStatus.getPonName(), cFormat);
                                sheet.addCell(label);
                                label = new Label(4, rowNum, oltPonRunningStatus.getPonUsage(), cFormat);
                                sheet.addCell(label);
                                label = new Label(5, rowNum, oltPonRunningStatus.getCcNum().toString(), cFormat);
                                sheet.addCell(label);
                                label = new Label(6, rowNum, oltPonRunningStatus.getCmNum().toString(), cFormat);
                                sheet.addCell(label);
                                rowNum++;
                            }
                        }
                        // 合并设备单元格
                        int oltSpendRow = oltRunningStatusList.get(i).getOltPonRunningStatusList().size();
                        sheet.mergeCells(1, oltRowNum, 1, oltRowNum + oltSpendRow - 1);
                        sheet.mergeCells(2, oltRowNum, 2, oltRowNum + oltSpendRow - 1);
                        oltRowNum = oltRowNum + oltSpendRow;
                    }
                    // 合并地域单元格
                    int folderSpendRow = 0;
                    for (int k = 0; k < oltRunningStatusList.size(); k++) {
                        folderSpendRow += oltRunningStatusList.get(k).getOltPonRunningStatusList().size();
                    }
                    sheet.mergeCells(0, folderRowNum, 0, folderRowNum + folderSpendRow - 1);
                    folderRowNum = folderRowNum + folderSpendRow;
                }
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("downFile method is error:{}", e);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.debug("downFile method is error:{}", e);
            }
        }
    }

}
