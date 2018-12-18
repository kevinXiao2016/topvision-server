/***********************************************************************
 * $Id: CcmtsDownChlFlowReportCreatorImpl.java,v1.0 2014-3-24 下午3:30:56 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtsDownChlFlow.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
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

import com.topvision.ems.cmc.report.ccmtsDownChlFlow.dao.CcmtsDownChlFlowReportDao;
import com.topvision.ems.cmc.report.ccmtsDownChlFlow.service.CcmtsDownChlFlowReportCreator;
import com.topvision.ems.cmc.report.domain.CcmtsChlFlowDetail;
import com.topvision.ems.cmc.report.domain.CcmtsChlFlowInfo;
import com.topvision.ems.cmc.report.domain.CcmtsChlFlowStatic;
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
 * @created @2014-3-24-下午3:30:56
 * 
 */
@Service("ccmtsDownChlFlowReportCreator")
public class CcmtsDownChlFlowReportCreatorImpl extends BaseService implements CcmtsDownChlFlowReportCreator {
    @Autowired
    private StatReportService statReportService;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private CcmtsDownChlFlowReportDao ccmtsDownChlFlowReportDao;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void bulidReport(ReportTask reportTask) {
        Map<String, Object> parMap = new HashMap<String, Object>();
        String colTime = ReportTaskUtil.getTimeSegmentSql(reportTask, "A.collectTime");
        parMap.put("sql", colTime);
        parMap.put("userId", reportTask.getCondition("userId"));
        Map<String, List<CcmtsChlFlowStatic>> downChlFlowReport = statDownChlFlowStatic(parMap);
        Date statDate = new Date();
        // 根据task整理出conditions
        String conditions = getTaskConditions(reportTask);
        if (reportTask.isExcelEnabled()) {
            try {
                String filePath = ReportExportUtil.getExportUrl(reportTask, EXCEL);
                OutputStream out = new FileOutputStream(filePath);
                exportDownChlFlowReport(reportTask, downChlFlowReport, statDate, out, conditions);
                reportInstanceService.addReportInstance(filePath, EXCEL, reportTask);
            } catch (FileNotFoundException e) {
                logger.error("bulidReport method is error:{}", e);
            }
        }
    }

    private void exportDownChlFlowReport(ReportTask reportTask,
            Map<String, List<CcmtsChlFlowStatic>> downChlFlowReport, Date statDate, OutputStream out, String conditions) {
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            createReportExcelFile(downChlFlowReport, workbook, statDate, conditions);
        } catch (Exception e) {
            logger.debug("something wrong with I/O:", e);
        } finally {
            try {
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

    @Override
    public Map<String, List<CcmtsChlFlowStatic>> statDownChlFlowStatic(Map<String, Object> map) {
        List<TopoEntityStastic> relates = statReportService.getTopoEntityRelation(entityTypeService.getOltType(), map);
        return ccmtsDownChlFlowReportDao.statDownChlFlowStatic(relates, map);
    }

    @Override
    public void exportReportToExcel(Map<String, List<CcmtsChlFlowStatic>> statDownChlFlowStatic, Date statDate,
            String conditions) {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");

        String fileName;
        try {
            fileName = URLEncoder.encode(ReportTaskUtil.getString("report.ccmtsDownChlFlow", "report") + "-"
                    + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            createReportExcelFile(statDownChlFlowStatic, workbook, statDate, conditions);
        } catch (Exception e) {
            logger.debug("something wrong with I/O:", e);
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

    private void createReportExcelFile(Map<String, List<CcmtsChlFlowStatic>> statUpChlFlowStatic,
            WritableWorkbook workbook, Date statDate, String conditions) {
        // 创建sheet页和一些格式化信息
        WritableSheet sheet = workbook.createSheet("Sheet1", 0);

        sheet.setColumnView(0, 20);
        sheet.setColumnView(1, 20);
        for (int i = 2; i < 13; i++) {
            sheet.setColumnView(i, 12);
        }

        try {
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.ccmtsDownChlFlow", "report"),
                    statDate, conditions, 14);
        } catch (Exception e1) {
            logger.debug("formatExcelHeader error:{}", e1);
        }

        try {
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            String[] titleValues = { ReportTaskUtil.getString("report.folder", "report"),
                    ReportTaskUtil.getString("report.entity", "report"),
                    ReportTaskUtil.getString("report.channelNum", "report"), "90%-100%", "80%-90%", "70%-80%",
                    "60%-70%", "50%-60%", "40%-50%", "30%-40%", "20%-30%", "10%-20%", "0%-10%", "0%" };
            addRowToExcel(sheet, titleValues, 5, titleCellFormat);
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            int rowNum = 6;

            // 填充内容
            // 总计
            Integer totalChlNum = 0;
            Integer totalUsage90to100 = 0;
            Integer totalUsage80to90 = 0;
            Integer totalUsage70to80 = 0;
            Integer totalUsage60to70 = 0;
            Integer totalUsage50to60 = 0;
            Integer totalUsage40to50 = 0;
            Integer totalUsage30to40 = 0;
            Integer totalUsage20to30 = 0;
            Integer totalUsage10to20 = 0;
            Integer totalUsage0to10 = 0;
            Integer totalUsage0 = 0;
            List<Long> caculatedIds = new ArrayList<Long>();
            // 遍历每一个地域
            for (String folderName : statUpChlFlowStatic.keySet()) {
                int folderStartRow = rowNum;
                // 获取该地域下的所有局方设备
                List<CcmtsChlFlowStatic> list = statUpChlFlowStatic.get(folderName);
                // 如果该地域下没有设备，则直接跳过该地域
                if (list != null) {
                    // 遍历该地域下面的所有设备，输出数据
                    for (CcmtsChlFlowStatic entity : list) {
                        // 填入数据
                        String[] values = { folderName, entity.getEntityName(), entity.getChlNum().toString(),
                                entity.getUsage90to100().toString(), entity.getUsage80to90().toString(),
                                entity.getUsage70to80().toString(), entity.getUsage60to70().toString(),
                                entity.getUsage50to60().toString(), entity.getUsage40to50().toString(),
                                entity.getUsage30to40().toString(), entity.getUsage20to30().toString(),
                                entity.getUsage10to20().toString(), entity.getUsage0to10().toString(),
                                entity.getUsage0().toString() };
                        if (!caculatedIds.contains(entity.getEntityId())) {
                            caculatedIds.add(entity.getEntityId());
                            totalChlNum += entity.getChlNum();
                            totalUsage90to100 += entity.getUsage90to100();
                            totalUsage80to90 += entity.getUsage80to90();
                            totalUsage70to80 += entity.getUsage70to80();
                            totalUsage60to70 += entity.getUsage60to70();
                            totalUsage50to60 += entity.getUsage50to60();
                            totalUsage40to50 += entity.getUsage40to50();
                            totalUsage30to40 += entity.getUsage30to40();
                            totalUsage20to30 += entity.getUsage20to30();
                            totalUsage10to20 += entity.getUsage10to20();
                            totalUsage0to10 += entity.getUsage0to10();
                            totalUsage0 += entity.getUsage0();
                        }
                        addRowToExcel(sheet, values, rowNum, contentCellFormat);
                        rowNum++;
                    }
                    // 合并单元格
                    sheet.mergeCells(0, folderStartRow, 0, folderStartRow + list.size() - 1);
                }
            }
            // 统计总计
            String[] values = { ReportTaskUtil.getString("report.totalling", "report"), "", totalChlNum.toString(),
                    totalUsage90to100.toString(), totalUsage80to90.toString(), totalUsage70to80.toString(),
                    totalUsage60to70.toString(), totalUsage50to60.toString(), totalUsage40to50.toString(),
                    totalUsage30to40.toString(), totalUsage20to30.toString(), totalUsage10to20.toString(),
                    totalUsage0to10.toString(), totalUsage0.toString() };
            addRowToExcel(sheet, values, rowNum, contentCellFormat);

            workbook.write();
            workbook.close();

        } catch (Exception e) {
            logger.debug("close fileStream error:{}", e);
        }

    }

    private void addRowToExcel(WritableSheet sheet, String[] values, int rowNum, WritableCellFormat cellFormat)
            throws Exception {
        Label label = null;
        for (int i = 0; i < values.length; i++) {
            label = new Label(i, rowNum, values[i], cellFormat);
            sheet.addCell(label);
        }
    }

    @Override
    public Map<String, List<CcmtsChlFlowDetail>> statDownChlFlowDetail(Map<String, Object> map) {
        List<TopoEntityStastic> relates = statReportService.getTopoEntityRelation(
                entityTypeService.getCcmtswithoutagentType(), map);
        return ccmtsDownChlFlowReportDao.statDownChlFlowDetail(relates, map);
    }

    @Override
    public void exportCcmtsDownChlDetailToExcel(Map<String, Object> map) {
        Map<String, List<CcmtsChlFlowDetail>> statUpChlFlowDetail = statDownChlFlowDetail(map);
        Date statDate = new Date();

        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");

        String fileName;
        try {
            fileName = URLEncoder.encode(ReportTaskUtil.getString("report.ccmtsDownChlFlowDetail", "report") + "-"
                    + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            // 创建sheet页和一些格式化信息
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);

            sheet.setColumnView(0, 13);
            sheet.setColumnView(1, 13);
            sheet.setColumnView(2, 13);
            sheet.setColumnView(3, 15);
            sheet.setColumnView(4, 15);
            sheet.setColumnView(5, 25);
            sheet.setColumnView(6, 25);
            sheet.setColumnView(7, 25);
            sheet.setColumnView(8, 25);
            sheet.setColumnView(9, 25);
            try {
                String conditions = ReportTaskUtil.getString("report.statRange", "report") + ": " + map.get("stTime")
                        + " " + ReportTaskUtil.getString("report.to", "report") + "   " + map.get("etTime") + "\n";
                conditions += ReportTaskUtil.getString("report.assetOnFolder", "report");
                ReportTaskUtil.formatExcelHeader(sheet,
                        ReportTaskUtil.getString("report.ccmtsDownChlFlowDetail", "report"), statDate, conditions, 10);
            } catch (Exception e1) {
                logger.debug("formatExcelHeader error:{}", e1);
            }

            try {
                WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
                String[] titleValues = { ReportTaskUtil.getString("report.folder", "report"),
                        ReportTaskUtil.getString("report.ccmtsName", "report"),
                        ReportTaskUtil.getString("report.uplinkOlt", "report"),
                        ReportTaskUtil.getString("report.ds", "report"),
                        ReportTaskUtil.getString("report.modulationProfile", "report"),
                        ReportTaskUtil.getString("report.dsFlowMax", "report"),
                        ReportTaskUtil.getString("report.dsUsageMax", "report"),
                        ReportTaskUtil.getString("report.dsUsageAve", "report"),
                        ReportTaskUtil.getString("report.userRegNumMax", "report"),
                        ReportTaskUtil.getString("report.userNumMax", "report") };
                addRowToExcel(sheet, titleValues, 5, titleCellFormat);
                WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
                int rowNum = 6;

                // 填充内容
                // 遍历每一个地域
                for (String folderName : statUpChlFlowDetail.keySet()) {
                    // 获取该地域下的所有设备信息
                    List<CcmtsChlFlowDetail> cmtsDetails = statUpChlFlowDetail.get(folderName);
                    // 获取该地域总计多少行
                    Integer folderRowSpan = 0;
                    if (cmtsDetails != null && cmtsDetails.size() != 0) {
                        // 获取该设备下的所有上行信道
                        for (CcmtsChlFlowDetail cmtsDetail : cmtsDetails) {
                            List<CcmtsChlFlowInfo> channels = cmtsDetail.getCcmtsChlFlowInfos();
                            if (channels != null && channels.size() != 0) {
                                folderRowSpan += cmtsDetail.getCcmtsChlFlowInfos().size();
                            }
                        }
                    }

                    if (cmtsDetails != null && cmtsDetails.size() != 0) {
                        // 遍历该地域下的所有设备
                        for (CcmtsChlFlowDetail cmtsDetail : cmtsDetails) {
                            // 获取该设备下的所有上行信道
                            List<CcmtsChlFlowInfo> channels = cmtsDetail.getCcmtsChlFlowInfos();
                            if (channels == null || channels.size() == 0) {
                                continue;
                            }
                            // 遍历所有信道
                            for (CcmtsChlFlowInfo channel : channels) {
                                // 输出数据
                                // 填入数据
                                String[] values = { folderName, cmtsDetail.getEntityName(), cmtsDetail.getOltName(),
                                        channel.getChannelName(), channel.getModulationProfileString(),
                                        channel.getMaxFlowString(), channel.getMaxFlowUsageString(),
                                        cmtsDetail.getUsageAvg().toString(), cmtsDetail.getMaxRegUserNum().toString(),
                                        cmtsDetail.getMaxUserNum().toString() };
                                addRowToExcel(sheet, values, rowNum, contentCellFormat);
                                rowNum++;
                            }
                            // 合并设备单元格
                            sheet.mergeCells(1, rowNum - channels.size(), 1, rowNum - 1);
                            sheet.mergeCells(2, rowNum - channels.size(), 2, rowNum - 1);
                            sheet.mergeCells(7, rowNum - channels.size(), 7, rowNum - 1);
                            sheet.mergeCells(8, rowNum - channels.size(), 8, rowNum - 1);
                            sheet.mergeCells(9, rowNum - channels.size(), 9, rowNum - 1);
                        }
                        // 合并地域单元格
                        sheet.mergeCells(0, rowNum - folderRowSpan, 0, rowNum - 1);
                    }
                }

                workbook.write();
                workbook.close();

            } catch (Exception e) {
                logger.debug("close fileStream error:{}", e);
            }

        } catch (Exception e) {
            logger.debug("something wrong with I/O:", e);
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

} 
