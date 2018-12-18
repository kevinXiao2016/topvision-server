/***********************************************************************
 * $Id: CmcSnrReportCreatorImpl.java,v1.0 2013-10-29 下午4:38:17 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmcsnr.service.impl;

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

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.report.cmcsnr.dao.CmcSnrReportDao;
import com.topvision.ems.cmc.report.cmcsnr.service.CmcSnrReportCreator;
import com.topvision.ems.cmc.report.domain.CmcSnrReportDetail;
import com.topvision.ems.cmc.report.domain.CmcSnrReportStatistics;
import com.topvision.ems.report.domain.ExcutorTimePolicy;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.domain.StartStopTime;
import com.topvision.ems.report.domain.TopoEntityStastic;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.service.StatReportService;
import com.topvision.ems.report.util.NumberFormatterUtil;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.zetaframework.util.ZetaUtil;

/**
 * @author haojie
 * @created @2013-10-29-下午4:38:17
 * 
 */
@Service("cmcSnrReportCreator")
public class CmcSnrReportCreatorImpl extends BaseService implements CmcSnrReportCreator {
    @Autowired
    private CmcSnrReportDao cmcSnrReportDao;
    @Autowired
    private StatReportService statReportService;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void bulidReport(ReportTask reportTask) {
        Date statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        String oltPerfSqlTime = ReportTaskUtil.getTimeSegmentSql(reportTask, "A.dt");
        map.put("sql", oltPerfSqlTime);
        map.put("entityType", reportTask.getCondition("entityType"));
        map.put("userId", reportTask.getCondition("userId"));
        Map<String, List<CmcSnrReportStatistics>> statCmcSnrReport = statCmcSnrReport(map);
        // 根据task整理出conditions
        String conditions = getTaskConditions(reportTask);
        if (reportTask.isExcelEnabled()) {
            try {
                String filePath = ReportExportUtil.getExportUrl(reportTask, EXCEL);
                OutputStream out = new FileOutputStream(filePath);
                exportAsExcel(reportTask, statCmcSnrReport, statDate, out, conditions);
                reportInstanceService.addReportInstance(filePath, EXCEL, reportTask);
            } catch (FileNotFoundException e) {
                logger.error("bulidReport method is error:{}", e);
            }
        }

    }

    @Override
    public Map<String, List<CmcSnrReportStatistics>> statCmcSnrReport(Map<String, Object> map) {
        List<TopoEntityStastic> relates = new ArrayList<TopoEntityStastic>();
        Long entityType = Long.parseLong(map.get("entityType").toString());
        if (entityTypeService.isCcmtsWithoutAgent(entityType)) {
            relates = statReportService.getTopoEntityRelation(entityTypeService.getOltType(), map);
        } else if (entityTypeService.isCcmtsWithAgent(entityType)) {
            relates = statReportService.getTopoEntityRelation(entityType, map);
        }
        return cmcSnrReportDao.statCmcSnrReport(relates, map);
    }

    @Override
    public List<CmcSnrReportDetail> getSnrReportDetail(Map<String, Object> map) {
        return cmcSnrReportDao.getSnrReportDetail(map);
    }

    @Override
    public void exportAsExcel(Map<String, Object> parMap, Date statDate, OutputStream out) {
        ZetaUtil zeta = new ZetaUtil("report");
        Map<String, List<CmcSnrReportStatistics>> statCmcSnrReport = statCmcSnrReport(parMap);
        String condition = String.format(zeta.getString("report.statisticsTimeTo"), parMap.get("stTime"),
                parMap.get("etTime"))
                + "\n";
        condition += zeta.getString("report.deviceType")
                //+ ReportTaskUtil.getString("COMMON.maohao", "base")
                + zeta.getString("COMMON.maohao")
                //+ ZetaUtil.getStaticString("COMMON.maohao", "base")
                + entityTypeService.getEntityType(Long.parseLong(parMap.get("entityType").toString())).getDisplayName()
                + "\n";
        condition += zeta.getString("report.assetOnFolder");
        exportAsExcel(null, statCmcSnrReport, statDate, out, condition);

    }

    @Override
    public void exportSnrDetailReportToExcel(Map<String, Object> parMap, Date statDate, OutputStream out) {
        ZetaUtil zeta = new ZetaUtil("report");
        String conditions = String.format(ReportTaskUtil.getString("report.statisticsTimeTo", "report"),
                parMap.get("stTime"), parMap.get("etTime"))
                + "\n";
        conditions += zeta.getString("report.deviceType") + zeta.getString("COMMON.maohao")
                + entityTypeService.getEntityType(Long.parseLong(parMap.get("entityType").toString())).getDisplayName()
                + "\n";
        conditions += zeta.getString("report.assetOnFolder");
        try {
            List<CmcSnrReportDetail> cmcSnrReportDetailList = getSnrReportDetail(parMap);

            // 判断是否需要打印所属OLT列
            Boolean needOltColumn = false;
            if (parMap.containsKey("entityType")) {
                String entityType = (String) parMap.get("entityType");
                if (entityTypeService.isCcmtsWithoutAgent(new Long(entityType))) {
                    needOltColumn = true;
                }
            }

            out = ServletActionContext.getResponse().getOutputStream();
            Label label;
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);

            sheet.setColumnView(0, 10);
            sheet.setColumnView(1, 20);
            sheet.setColumnView(2, 30);
            sheet.setColumnView(3, 30);
            sheet.setColumnView(4, 30);
            int titleCell = 4;
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.ccmtsSnrDetail", "report"),
                    statDate, conditions, titleCell);
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();

            label = new Label(0, 5, "#", titleCellFormat);
            sheet.addCell(label);
            if (needOltColumn) {
                label = new Label(1, 5, ReportTaskUtil.getString("report.belongOlt", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(2, 5, ReportTaskUtil.getString("report.channelName", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(3, 5, ReportTaskUtil.getString("report.belongCcmts", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(4, 5, ReportTaskUtil.getString("report.below20dbTimes", "report"), titleCellFormat);
                sheet.addCell(label);
            } else {
                label = new Label(1, 5, ReportTaskUtil.getString("report.channelName", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(2, 5, ReportTaskUtil.getString("report.belongCcmts", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(3, 5, ReportTaskUtil.getString("report.below20dbTimes", "report"), titleCellFormat);
                sheet.addCell(label);
            }

            Integer rowNum = 6;
            Integer rowNumView = 1;
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            for (CmcSnrReportDetail detail : cmcSnrReportDetailList) {
                label = new Label(0, rowNum, rowNumView.toString(), contentCellFormat);
                sheet.addCell(label);
                if (needOltColumn) {
                    label = new Label(1, rowNum, detail.getEntityName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(2, rowNum, detail.getChannelName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(3, rowNum, detail.getCmcName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(4, rowNum, detail.getLowTimes().toString(), contentCellFormat);
                    sheet.addCell(label);
                } else {
                    label = new Label(1, rowNum, detail.getChannelName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(2, rowNum, detail.getCmcName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(3, rowNum, detail.getLowTimes().toString(), contentCellFormat);
                    sheet.addCell(label);
                }
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
     * 得到导出的Excel的条件字符串
     * 
     * @return
     */
    private String getTaskConditions(ReportTask reportTask) {
        ExcutorTimePolicy policy = ReportTaskUtil.decodeCronExpression(reportTask);
        List<StartStopTime> stAndEtTimes = ReportTaskUtil.getStAndEtTime(policy, new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(ReportTaskUtil.getString("report.statisticsTimen", "report"));
        for (StartStopTime time : stAndEtTimes) {
            Date st = time.getStTime();
            Date et = time.getEtTime();
            sb.append(formatter.format(st)).append(ReportTaskUtil.getString("report.timeTo", "report"))
                    .append(formatter.format(et)).append("\n");
        }
        sb.append(ReportTaskUtil.getString("report.deviceType", "report") + ":    ");
        Map<String, Object> condition = reportTask.getCondition();
        Long iEntityType = Long.parseLong((String) condition.get("entityType"));
        return entityTypeService.getEntityType(iEntityType).getDisplayName();
    }

    /**
     * 导出为Excel
     * 
     * @param task
     * @param statCmcSnrReport
     * @param statDate
     * @param out
     */
    private void exportAsExcel(ReportTask task, Map<String, List<CmcSnrReportStatistics>> statCmcSnrReport,
            Date statDate, OutputStream out, String conditions) {
        try {
            Label label;
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);
            sheet.setColumnView(0, 20);
            sheet.setColumnView(1, 20);
            sheet.setColumnView(2, 20);
            sheet.setColumnView(3, 20);
            sheet.setColumnView(4, 20);
            int titleCell = 5;
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.ccmtsSnr", "report"), statDate,
                    conditions, titleCell);
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            label = new Label(0, 5, ReportTaskUtil.getString("report.folder", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 5, ReportTaskUtil.getString("report.entity", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 5, ReportTaskUtil.getString("report.below20DB", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 5, ReportTaskUtil.getString("report.totalChannel", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 5, ReportTaskUtil.getString("report.percentage", "report"), titleCellFormat);
            sheet.addCell(label);
            int rowNum = 6;
            Long totalPorts = 0l;
            Long totalPorts20 = 0l;
            String totalPortsPercent = "";
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            List<Long> calculatedEntity = new ArrayList<Long>();
            for (String folderName : statCmcSnrReport.keySet()) {
                List<CmcSnrReportStatistics> list = statCmcSnrReport.get(folderName);
                if (list == null) {
                    continue;
                }
                label = new Label(0, rowNum, folderName, contentCellFormat);
                sheet.addCell(label);
                sheet.mergeCells(0, rowNum, 0, rowNum + list.size());
                Long count = 0l;
                Long count20 = 0l;
                String countPercent = "";
                for (int i = 0; i < list.size(); i++) {
                    label = new Label(1, rowNum, list.get(i).getEntityName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(2, rowNum, list.get(i).getPortNum0To20().toString(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(3, rowNum, list.get(i).getPortTotalNum().toString(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(4, rowNum, list.get(i).getPortRateString(), contentCellFormat);
                    sheet.addCell(label);
                    rowNum++;
                    count += list.get(i).getPortTotalNum();
                    count20 += list.get(i).getPortNum0To20();
                    if (!calculatedEntity.contains(list.get(i).getEntityId())) {
                        totalPorts += list.get(i).getPortTotalNum().intValue();
                        totalPorts20 += list.get(i).getPortNum0To20().intValue();
                        calculatedEntity.add(list.get(i).getEntityId());
                    }
                }
                if (count == 0) {
                    countPercent = "0%";
                } else {
                    countPercent = NumberFormatterUtil
                            .formatDecimalTwo((count20.doubleValue() / count.doubleValue()) * 100) + "%";
                }
                label = new Label(1, rowNum, ReportTaskUtil.getString("report.subtotal", "report"), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, count20.toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, count.toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(4, rowNum, countPercent, contentCellFormat);
                sheet.addCell(label);
                rowNum++;
            }
            if (totalPorts == 0) {
                totalPortsPercent = "0%";
            } else {
                totalPortsPercent = NumberFormatterUtil.formatDecimalTwo((totalPorts20.doubleValue() / totalPorts
                        .doubleValue()) * 100) + "%";
            }
            label = new Label(1, rowNum, ReportTaskUtil.getString("report.totalling", "report"), contentCellFormat);
            sheet.addCell(label);
            label = new Label(2, rowNum, totalPorts20.toString(), contentCellFormat);
            sheet.addCell(label);
            label = new Label(3, rowNum, totalPorts.toString(), contentCellFormat);
            sheet.addCell(label);
            label = new Label(4, rowNum, totalPortsPercent, contentCellFormat);
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

}
