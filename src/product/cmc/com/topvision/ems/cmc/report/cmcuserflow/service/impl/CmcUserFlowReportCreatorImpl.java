/***********************************************************************
 * $Id: CmcUserFlowReportCreatorImpl.java,v1.0 2013-10-29 下午5:07:11 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmcuserflow.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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

import com.topvision.ems.cmc.report.cmcuserflow.dao.CmcUserFlowReportDao;
import com.topvision.ems.cmc.report.cmcuserflow.service.CmcUserFlowReportCreator;
import com.topvision.ems.cmc.report.domain.CcmtsChannelSnrAvg;
import com.topvision.ems.cmc.report.domain.CmcUserFlowPortValue;
import com.topvision.ems.cmc.report.domain.CmcUserFlowReportDetail;
import com.topvision.ems.cmc.report.domain.CmcUserFlowReportStatistics;
import com.topvision.ems.facade.domain.Entity;
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

/**
 * @author haojie
 * @created @2013-10-29-下午5:07:11
 * 
 */
@Service("cmcUserFlowReportCreator")
public class CmcUserFlowReportCreatorImpl extends BaseService implements CmcUserFlowReportCreator {
    @Autowired
    private CmcUserFlowReportDao cmcUserFlowReportDao;
    @Autowired
    private StatReportService statReportService;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void bulidReport(ReportTask reportTask) {
        Map<String, Object> parMap = new HashMap<String, Object>();
        String channelSqlTime = ReportTaskUtil.getTimeSegmentSql(reportTask, "A.dt");
        String oltPerfSqlTime = ReportTaskUtil.getTimeSegmentSql(reportTask, "A.stats15EndTime");
        parMap.put("channelSqlTime", channelSqlTime);
        parMap.put("oltPerfSqlTime", oltPerfSqlTime);
        parMap.put("userId", reportTask.getCondition("userId"));
        Map<String, Object> statCmcUserFlowReport = statCmcUserFlowReport(parMap);
        Date statDate = new Date();
        // 根据task整理出conditions
        String conditions = getTaskConditions(reportTask);
        if (reportTask.isExcelEnabled()) {
            try {
                String filePath = ReportExportUtil.getExportUrl(reportTask, EXCEL);
                OutputStream out = new FileOutputStream(filePath);
                exportFlowReportToExcel(reportTask, statCmcUserFlowReport, statDate, out, conditions);
                reportInstanceService.addReportInstance(filePath, EXCEL, reportTask);
            } catch (FileNotFoundException e) {
                logger.error("bulidReport method is error:{}", e);
            }
        }

    }

    @Override
    public Map<String, Object> statCmcUserFlowReport(Map<String, Object> map) {
        List<TopoEntityStastic> relates = statReportService.getTopoEntityRelation(entityTypeService.getOltType(), map);
        return cmcUserFlowReportDao.statCmcUserFlowReport(relates, map);
    }

    @Override
    public Map<String, List<CmcUserFlowReportDetail>> getUserFlowDetail(Map<String, Object> map) {
        return cmcUserFlowReportDao.selectUserFlowDetail(map);
    }

    @Override
    public void exportFlowReportToExcel(Map<String, Object> parMap, Date statDate, OutputStream out) {
        Map<String, Object> statCmcUserFlowReport = statCmcUserFlowReport(parMap);
        String condition = String.format(ReportTaskUtil.getString("report.statisticsTimeTo", "report"),
                parMap.get("stTime"), parMap.get("etTime"))
                + "\n";
        condition += ReportTaskUtil.getString("report.assetOnFolder", "report");
        exportFlowReportToExcel(null, statCmcUserFlowReport, statDate, out, condition);
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
        sb.append(ReportTaskUtil.getString("report.assetOnFolder", "report"));
        return sb.toString();
    }

    /**
     * 导出用户流量数据到excel
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("unchecked")
    public void exportFlowReportToExcel(ReportTask task, Map<String, Object> statCmcUserFlowReport, Date statDate,
            OutputStream out, String conditions) {
        try {
            List<String> portIndexColumnList;
            if (statCmcUserFlowReport.get("columns") != null) {
                portIndexColumnList = (List<String>) statCmcUserFlowReport.get("columns");
            } else {
                portIndexColumnList = new ArrayList<String>();
            }
            Label label;
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);
            int titleCell = 7 + portIndexColumnList.size();
            sheet.setColumnView(0, 15);
            sheet.setColumnView(1, 20);
            sheet.setColumnView(2, 12);
            sheet.setColumnView(3, 25);
            sheet.setColumnView(4, 15);
            for (int i = 5; i < titleCell; i++) {
                sheet.setColumnView(i, 20);
            }
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.ccmtsUserFlow", "report"),
                    statDate, conditions, titleCell);
            label = new Label(0, 5, ReportTaskUtil.getString("report.folder", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 5, ReportTaskUtil.getString("report.entity", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 5, ReportTaskUtil.getString("report.ccmtsNum", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 5, ReportTaskUtil.getString("report.totalOnlineUser", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 5, ReportTaskUtil.getString("report.totalUser", "report"), titleCellFormat);
            sheet.addCell(label);
            int startIndex = 5;
            label = new Label(startIndex++, 5, ReportTaskUtil.getString("report.sniPeekSeg", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(startIndex++, 5, "", titleCellFormat);
            sheet.addCell(label);
            for (@SuppressWarnings("unused")
            String column : portIndexColumnList) {
                label = new Label(startIndex++, 5, "", titleCellFormat);
                sheet.addCell(label);
            }

            label = new Label(0, 6, "", titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 6, "", titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 6, "", titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 6, "", titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 6, "", titleCellFormat);
            sheet.addCell(label);

            startIndex = 5;
            for (String column : portIndexColumnList) {
                label = new Label(startIndex++, 6, column, titleCellFormat);
                sheet.addCell(label);
            }
            label = new Label(startIndex++, 6, ReportTaskUtil.getString("report.recvRate", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(startIndex, 6, ReportTaskUtil.getString("report.bandwidthUsage", "report"),
                    titleCellFormat);
            sheet.addCell(label);

            sheet.mergeCells(0, 5, 0, 6);
            sheet.mergeCells(1, 5, 1, 6);
            sheet.mergeCells(2, 5, 2, 6);
            sheet.mergeCells(3, 5, 3, 6);
            sheet.mergeCells(4, 5, 4, 6);
            sheet.mergeCells(5, 5, startIndex, 5);

            int rowNum = 7;
            Integer totalCcmtsNum = 0;
            Integer totalPeakOnlineNum = 0;
            Integer totalUserNum = 0;
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            List<Long> calculatedEntity = new ArrayList<Long>();
            for (String folderName : statCmcUserFlowReport.keySet()) {
                if ("columns".equals(folderName)) {
                    continue;
                }
                List<CmcUserFlowReportStatistics> list = (List<CmcUserFlowReportStatistics>) statCmcUserFlowReport
                        .get(folderName);
                if (list == null) {
                    continue;
                }
                label = new Label(0, rowNum, folderName, contentCellFormat);
                sheet.addCell(label);
                sheet.mergeCells(0, rowNum, 0, rowNum + list.size());
                Integer ccmtsNum = 0;
                Integer peakOnlineNum = 0;
                Integer userNum = 0;
                for (int i = 0; i < list.size(); i++) {
                    label = new Label(1, rowNum, list.get(i).getEntityName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(2, rowNum, list.get(i).getCcmtsTotal().toString(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(3, rowNum, list.get(i).getUserNumOnline().toString(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(4, rowNum, list.get(i).getUserNumTotal().toString(), contentCellFormat);
                    sheet.addCell(label);
                    Double maxValue = 0.0;
                    Double sumValue = 0.0;
                    startIndex = 5;
                    for (String column : portIndexColumnList) {
                        boolean columnFound = false;
                        for (CmcUserFlowPortValue pv : list.get(i).getPortValueList()) {
                            if (column.equals(pv.getPortName())) {
                                columnFound = true;
                                if (maxValue < pv.getValue()) {
                                    maxValue = pv.getValue();
                                }
                                sumValue += pv.getValue();
                                label = new Label(startIndex++, rowNum, pv.getValueString(), contentCellFormat);
                                sheet.addCell(label);
                                break;
                            }
                        }
                        if (!columnFound) {
                            label = new Label(startIndex++, rowNum, " - ", contentCellFormat);
                            sheet.addCell(label);
                        }
                    }

                    label = new Label(startIndex++, rowNum, NumberFormatterUtil.formatDecimalTwo(sumValue),
                            contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(startIndex, rowNum, NumberFormatterUtil.formatDecimalTwo(maxValue / 10),
                            contentCellFormat);
                    sheet.addCell(label);
                    rowNum++;
                    ccmtsNum += list.get(i).getCcmtsTotal();
                    peakOnlineNum += list.get(i).getUserNumOnline();
                    userNum += list.get(i).getUserNumTotal();
                    if (!calculatedEntity.contains(list.get(i).getEntityId())) {
                        totalCcmtsNum += list.get(i).getCcmtsTotal();
                        totalPeakOnlineNum += list.get(i).getUserNumOnline();
                        totalUserNum += list.get(i).getUserNumTotal();
                        calculatedEntity.add(list.get(i).getEntityId());
                    }
                }
                label = new Label(1, rowNum, ReportTaskUtil.getString("report.subtotal", "report"), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, ccmtsNum.toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, peakOnlineNum.toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(4, rowNum, userNum.toString(), contentCellFormat);
                sheet.addCell(label);
                startIndex = 5;
                for (@SuppressWarnings("unused")
                String column : portIndexColumnList) {
                    label = new Label(startIndex++, rowNum, "", contentCellFormat);
                    sheet.addCell(label);
                }
                label = new Label(startIndex++, rowNum, "", contentCellFormat);
                sheet.addCell(label);
                label = new Label(startIndex, rowNum, "", contentCellFormat);
                sheet.addCell(label);
                rowNum++;
            }
            label = new Label(1, rowNum, ReportTaskUtil.getString("report.totalling", "report"), contentCellFormat);
            sheet.addCell(label);
            label = new Label(2, rowNum, totalCcmtsNum.toString(), contentCellFormat);
            sheet.addCell(label);
            label = new Label(3, rowNum, totalPeakOnlineNum.toString(), contentCellFormat);
            sheet.addCell(label);
            label = new Label(4, rowNum, totalUserNum.toString(), contentCellFormat);
            sheet.addCell(label);
            startIndex = 5;
            for (@SuppressWarnings("unused")
            String column : portIndexColumnList) {
                label = new Label(startIndex++, rowNum, "", contentCellFormat);
                sheet.addCell(label);
            }
            label = new Label(startIndex++, rowNum, "", contentCellFormat);
            sheet.addCell(label);
            label = new Label(startIndex, rowNum, "", contentCellFormat);
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

    @Override
    public void exportCmcUserFlowReportToExcel(Map<String, Object> parMap, Date statDate, OutputStream out) {
        try {
            String conditions = String.format(ReportTaskUtil.getString("report.statisticsTimeTo", "report"),
                    parMap.get("stTime"), parMap.get("etTime"))
                    + "\n";
            conditions += ReportTaskUtil.getString("report.assetOnOlt", "report");
            Entity entity = (Entity) parMap.get("entity");
            Map<String, List<CmcUserFlowReportDetail>> details = getUserFlowDetail(parMap);
            out = ServletActionContext.getResponse().getOutputStream();

            Label label;
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);

            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 10);
            sheet.setColumnView(2, 10);
            sheet.setColumnView(3, 40);
            sheet.setColumnView(4, 20);
            /* sheet.setColumnView(5, 15); */
            sheet.setColumnView(5, 10);
            sheet.setColumnView(6, 10);
            sheet.setColumnView(7, 10);
            sheet.setColumnView(8, 10);
            sheet.setColumnView(9, 10);
            sheet.setColumnView(10, 10);
            sheet.setColumnView(11, 35);
            sheet.setColumnView(12, 35);
            sheet.setColumnView(13, 35);
            sheet.setColumnView(14, 35);
            int titleCell = 15;
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.ccmtsUserDetail", "report"),
                    statDate, conditions, titleCell);
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();

            label = new Label(0, 5, "OLT", titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 5, ReportTaskUtil.getString("report.no", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 5, ReportTaskUtil.getString("report.pon", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 5, ReportTaskUtil.getString("report.ponSendRateTop", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 5, ReportTaskUtil.getString("report.ccmtsName", "report"), titleCellFormat);
            sheet.addCell(label);

            /*
             * label = new Label(5, 5, ReportTaskUtil.getString("report.dsFlow", "report"),
             * titleCellFormat); sheet.addCell(label);
             */
            label = new Label(5, 5, ReportTaskUtil.getString("report.interactiveCmNum", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(6, 5, ReportTaskUtil.getString("report.broadbandCmNum", "report"), titleCellFormat);

            sheet.addCell(label);
            label = new Label(7, 5, ReportTaskUtil.getString("report.cmTotal", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(8, 5, ReportTaskUtil.getString("report.cpeInteractiveNum", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(9, 5, ReportTaskUtil.getString("report.cpeBroadbandNum", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(10, 5, ReportTaskUtil.getString("report.cpeNum", "report"), titleCellFormat);
            sheet.addCell(label);

            label = new Label(11, 5, ReportTaskUtil.getString("report.channel1Snr", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(12, 5, ReportTaskUtil.getString("report.channel2Snr", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(13, 5, ReportTaskUtil.getString("report.channel3Snr", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(14, 5, ReportTaskUtil.getString("report.channel4Snr", "report"), titleCellFormat);
            sheet.addCell(label);
            int rowNum = 6;
            // CM总数和总计数目
            int totalInteractiveNum = 0;
            int totalBroadbandNum = 0;
            int totalCmNum = 0;
            int totalCpeInteractiveNum = 0;
            int totalCpeBroadbandNum = 0;
            int totalCpeNumTotal = 0;
            int counter = 0;
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            label = new Label(0, rowNum, entity.getName() + "(" + entity.getIp() + ")", contentCellFormat);
            sheet.addCell(label);
            for (String ponName : details.keySet()) {
                // 获取属于该PON口的所有CC的用户流量详细信息
                List<CmcUserFlowReportDetail> list = details.get(ponName);
                if (list != null) {
                    // 针对PON口列和发送速率列单独处理,因为需要合并
                    label = new Label(2, rowNum, ponName, contentCellFormat);
                    sheet.addCell(label);
                    sheet.mergeCells(2, rowNum, 2, rowNum + list.size() - 1);
                    label = new Label(3, rowNum, list.get(0).getPonMaxSendString(), contentCellFormat);
                    sheet.addCell(label);
                    sheet.mergeCells(3, rowNum, 3, rowNum + list.size() - 1);
                    for (int i = 0; i < list.size(); i++) {
                        CmcUserFlowReportDetail stastic = list.get(i);
                        // 填充不需要合并的cell
                        label = new Label(1, rowNum, Integer.toString(counter + 1), contentCellFormat);
                        sheet.addCell(label);
                        label = new Label(4, rowNum, stastic.getName(), contentCellFormat);
                        sheet.addCell(label);

                        /*
                         * label = new Label(5, rowNum, stastic.getCcmtsMaxSendString(),
                         * contentCellFormat); sheet.addCell(label);
                         */
                        label = new Label(5, rowNum, stastic.getInteractiveNum().toString(), contentCellFormat);
                        sheet.addCell(label);
                        label = new Label(6, rowNum, stastic.getBroadbandNum().toString(), contentCellFormat);

                        sheet.addCell(label);
                        label = new Label(7, rowNum, stastic.getCmNumTotal().toString(), contentCellFormat);
                        sheet.addCell(label);
                        label = new Label(8, rowNum, stastic.getCpeInteractiveNum().toString(), contentCellFormat);
                        sheet.addCell(label);
                        label = new Label(9, rowNum, stastic.getCpeBroadbandNum().toString(), contentCellFormat);
                        sheet.addCell(label);
                        label = new Label(10, rowNum, stastic.getCpeNumTotal().toString(), contentCellFormat);
                        sheet.addCell(label);

                        int cellNum = 11;
                        for (CcmtsChannelSnrAvg avg : stastic.getSnrAvgs()) {
                            if (avg != null) {
                                label = new Label(cellNum++, rowNum, avg.getSnrDisplay(), contentCellFormat);
                            } else {
                                label = new Label(cellNum++, rowNum, "-", contentCellFormat);
                            }
                            sheet.addCell(label);
                        }

                        rowNum++;
                        counter++;
                        totalInteractiveNum += stastic.getInteractiveNum();
                        totalBroadbandNum += stastic.getBroadbandNum();
                        totalCmNum += stastic.getCmNumTotal();
                        totalCpeInteractiveNum += stastic.getCpeInteractiveNum();
                        totalCpeBroadbandNum += stastic.getCpeBroadbandNum();
                        totalCpeNumTotal += stastic.getCpeNumTotal();

                    }
                }
            }
            sheet.mergeCells(0, rowNum - counter, 0, rowNum);
            label = new Label(0, rowNum, ReportTaskUtil.getString("label.total", "report"), contentCellFormat);
            sheet.addCell(label);
            label = new Label(1, rowNum, Integer.toString(counter), contentCellFormat);
            sheet.addCell(label);
            label = new Label(5, rowNum, Integer.toString(totalInteractiveNum), contentCellFormat);
            sheet.addCell(label);
            label = new Label(6, rowNum, Integer.toString(totalBroadbandNum), contentCellFormat);
            sheet.addCell(label);
            label = new Label(7, rowNum, Integer.toString(totalCmNum), contentCellFormat);
            sheet.addCell(label);
            label = new Label(8, rowNum, Integer.toString(totalCpeInteractiveNum), contentCellFormat);
            sheet.addCell(label);
            label = new Label(9, rowNum, Integer.toString(totalCpeBroadbandNum), contentCellFormat);
            sheet.addCell(label);
            label = new Label(10, rowNum, Integer.toString(totalCpeNumTotal), contentCellFormat);
            sheet.addCell(label);
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
