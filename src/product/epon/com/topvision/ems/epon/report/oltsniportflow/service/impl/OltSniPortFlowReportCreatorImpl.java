/***********************************************************************
 * $Id: OltSniPortFlowReportCreatorImpl.java,v1.0 2013-10-28 下午3:20:34 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltsniportflow.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

import com.topvision.ems.epon.report.domain.OltSniPortFlowDetail;
import com.topvision.ems.epon.report.domain.OltSniPortFlowStastic;
import com.topvision.ems.epon.report.oltsniportflow.dao.OltSniPortFlowReportDao;
import com.topvision.ems.epon.report.oltsniportflow.service.OltSniPortFlowReportCreator;
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
 * @created @2013-10-28-下午3:20:34
 * 
 */
@Service("oltSniPortFlowReportCreator")
public class OltSniPortFlowReportCreatorImpl extends BaseService implements OltSniPortFlowReportCreator {
    @Autowired
    private OltSniPortFlowReportDao oltSniPortFlowReportDao;
    @Autowired
    private StatReportService statReportService;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void bulidReport(ReportTask reportTask) {
        Date statDate = new Date();
        Map<String, Object> parMap = new HashMap<String, Object>();
        String oltPerfSqlTime = ReportTaskUtil.getTimeSegmentSql(reportTask, "A.stats15EndTime");
        parMap.put("sql", oltPerfSqlTime);
        parMap.put("userId", reportTask.getCondition("userId"));
        Map<String, List<OltSniPortFlowStastic>> statOltPonPortFlow = statSniFlowReport(parMap);
        // 根据task整理出conditions
        String conditions = getTaskConditions(reportTask);
        if (reportTask.isExcelEnabled()) {
            try {
                String filePath = ReportExportUtil.getExportUrl(reportTask, EXCEL);
                OutputStream out = new FileOutputStream(filePath);
                exportSniPortFlowPortToExcel(reportTask, statOltPonPortFlow, statDate, out, conditions);
                reportInstanceService.addReportInstance(filePath, EXCEL, reportTask);
            } catch (FileNotFoundException e) {
                logger.error("bulidReport method is error:{}", e);
            }
        }
    }

    @Override
    public Map<String, List<OltSniPortFlowStastic>> statSniFlowReport(Map<String, Object> map) {
        List<TopoEntityStastic> relates = statReportService.getTopoEntityRelation(entityTypeService.getOltType(), map);
        return oltSniPortFlowReportDao.statSniFlowReport(relates, map);
    }

    @Override
    public void exportSniPortFlowPortToExcel(Map<String, Object> parMap, Date statDate, OutputStream out) {
        Map<String, List<OltSniPortFlowStastic>> statOltPonPortFlow = statSniFlowReport(parMap);
        String condition = String.format(ReportTaskUtil.getString("report.statisticsTimeTo", "report"),
                parMap.get("stTime"), parMap.get("etTime"));
        exportSniPortFlowPortToExcel(null, statOltPonPortFlow, statDate, out, condition);

    }

    @Override
    public List<OltSniPortFlowDetail> getSniFlowDetail(Map<String, Object> map) {
        return oltSniPortFlowReportDao.selectSniFlowDetail(map);
    }

    @Override
    public void exportSniFlowDetailToExcel(Map<String, Object> map, Date statDate, OutputStream out) {
        String conditions = String.format(ReportTaskUtil.getString("report.statisticsTimeTo", "report"),
                map.get("stTime"), map.get("etTime"));
        try {
            List<OltSniPortFlowDetail> list = getSniFlowDetail(map);
            Label label;
            // 创建表头等信息
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);

            sheet.setColumnView(0, 10);
            sheet.setColumnView(1, 15);
            sheet.setColumnView(2, 30);
            sheet.setColumnView(3, 30);
            sheet.setColumnView(4, 30);
            sheet.setColumnView(5, 30);
            int titleCell = 6;
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.sniFlow", "report"), statDate,
                    conditions, titleCell);
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();

            label = new Label(0, 5, "#", titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 5, ReportTaskUtil.getString("report.portLocation", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 5, ReportTaskUtil.getString("report.belongOlt", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 5, ReportTaskUtil.getString("report.maxUsage", "report") + "(%)", titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 5, ReportTaskUtil.getString("report.maxRecvRate", "report") + "(Mbps)",
                    titleCellFormat);
            sheet.addCell(label);
            label = new Label(5, 5, ReportTaskUtil.getString("report.collectTime", "report"), titleCellFormat);
            sheet.addCell(label);
            Integer rowNum = 6;
            Integer rowNumView = 1;
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            for (OltSniPortFlowDetail oltSniPortFlowDetail : list) {
                label = new Label(0, rowNum, rowNumView.toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, oltSniPortFlowDetail.getPortName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, oltSniPortFlowDetail.getName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, oltSniPortFlowDetail.getUsage(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(4, rowNum, oltSniPortFlowDetail.getFlowDisplay(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(5, rowNum, oltSniPortFlowDetail.getCollectTimeString(), contentCellFormat);
                sheet.addCell(label);
                rowNum++;
                rowNumView++;
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

    /**
     * 实际的统计报表的导出
     * 
     * @param task
     * @param statOltPonPortFlow
     * @param statDate
     * @param out
     */
    private void exportSniPortFlowPortToExcel(ReportTask task,
            Map<String, List<OltSniPortFlowStastic>> statOltSniPortFlow, Date statDate, OutputStream out,
            String conditions) {
        try {
            Label label;
            // 创建表头等信息
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);
            sheet.setColumnView(0, 15);
            sheet.setColumnView(1, 15);
            sheet.setColumnView(2, 10);
            sheet.setColumnView(3, 15);
            sheet.setColumnView(4, 15);
            for (int i = 5; i < 10; i++) {
                sheet.setColumnView(i, 12);
            }
            int titleCell = 10;
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.sniFlow", "report"), statDate,
                    conditions, titleCell);
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            label = new Label(0, 5, ReportTaskUtil.getString("report.folder", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 5, ReportTaskUtil.getString("report.entity", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 5, ReportTaskUtil.getString("report.linkedPorts", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 5, ReportTaskUtil.getString("report.maxSendPower", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 5, ReportTaskUtil.getString("report.maxRecvPower", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(5, 5, "0%~20%", titleCellFormat);
            sheet.addCell(label);
            label = new Label(6, 5, "20%~40%", titleCellFormat);
            sheet.addCell(label);
            label = new Label(7, 5, "40%~60%", titleCellFormat);
            sheet.addCell(label);
            label = new Label(8, 5, "60%~80%", titleCellFormat);
            sheet.addCell(label);
            label = new Label(9, 5, "80%~100%", titleCellFormat);
            sheet.addCell(label);
            int rowNum = 6;
            // 总计(连接端口数、U20/U40/U60/U80/U100)
            Long totalSum = 0l;
            Long totalU20 = 0l;
            Long totalU40 = 0l;
            Long totalU60 = 0l;
            Long totalU80 = 0l;
            Long totalU100 = 0l;
            List<Long> calculatedEntity = new ArrayList<Long>();
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            for (String folderName : statOltSniPortFlow.keySet()) {
                List<OltSniPortFlowStastic> list = statOltSniPortFlow.get(folderName);
                if (list == null) {
                    continue;
                }
                label = new Label(0, rowNum, folderName, contentCellFormat);
                sheet.addCell(label);
                sheet.mergeCells(0, rowNum, 0, rowNum + list.size());
                // 小计(连接端口数、U20/U40/U60/U80/U100)
                Long subTotalSum = 0l;
                Long subTotalU20 = 0l;
                Long subTotalU40 = 0l;
                Long subTotalU60 = 0l;
                Long subTotalU80 = 0l;
                Long subTotalU100 = 0l;
                for (int i = 0; i < list.size(); i++) {
                    label = new Label(1, rowNum, list.get(i).getEntityName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(2, rowNum, list.get(i).getCurrentLinkedPortCount().toString(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(3, rowNum, list.get(i).getMaxSendFlowString(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(4, rowNum, list.get(i).getMaxRecvFlowString(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(5, rowNum, list.get(i).getRange20().toString(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(6, rowNum, list.get(i).getRange40().toString(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(7, rowNum, list.get(i).getRange60().toString(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(8, rowNum, list.get(i).getRange80().toString(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(9, rowNum, list.get(i).getRange100().toString(), contentCellFormat);
                    sheet.addCell(label);
                    rowNum++;
                    subTotalSum += list.get(i).getCurrentLinkedPortCount();
                    subTotalU20 += list.get(i).getRange20();
                    subTotalU40 += list.get(i).getRange40();
                    subTotalU60 += list.get(i).getRange60();
                    subTotalU80 += list.get(i).getRange80();
                    subTotalU100 += list.get(i).getRange100();

                    if (!calculatedEntity.contains(list.get(i).getEntityId())) {
                        totalSum += list.get(i).getCurrentLinkedPortCount();
                        totalU20 += list.get(i).getRange20();
                        totalU40 += list.get(i).getRange40();
                        totalU60 += list.get(i).getRange60();
                        totalU80 += list.get(i).getRange80();
                        totalU100 += list.get(i).getRange100();
                        calculatedEntity.add(list.get(i).getEntityId());
                    }
                }
                label = new Label(1, rowNum, ReportTaskUtil.getString("report.subtotal", "report"), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, subTotalSum.toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(5, rowNum, subTotalU20.toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(6, rowNum, subTotalU40.toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(7, rowNum, subTotalU60.toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(8, rowNum, subTotalU80.toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(9, rowNum, subTotalU100.toString(), contentCellFormat);
                sheet.addCell(label);
                rowNum++;
            }
            label = new Label(1, rowNum, ReportTaskUtil.getString("report.totalling", "report"), contentCellFormat);
            sheet.addCell(label);
            label = new Label(2, rowNum, totalSum.toString(), contentCellFormat);
            sheet.addCell(label);
            label = new Label(5, rowNum, totalU20.toString(), contentCellFormat);
            sheet.addCell(label);
            label = new Label(6, rowNum, totalU40.toString(), contentCellFormat);
            sheet.addCell(label);
            label = new Label(7, rowNum, totalU60.toString(), contentCellFormat);
            sheet.addCell(label);
            label = new Label(8, rowNum, totalU80.toString(), contentCellFormat);
            sheet.addCell(label);
            label = new Label(9, rowNum, totalU100.toString(), contentCellFormat);
            sheet.addCell(label);
            rowNum++;
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

}
