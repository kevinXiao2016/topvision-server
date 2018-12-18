/***********************************************************************
 * $Id: HistoryAlarmReportCreatorImpl.java,v1.0 2013-10-29 下午3:20:16 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.historyalarm.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.topvision.ems.epon.report.domain.HistoryAlarmDetail;
import com.topvision.ems.epon.report.domain.HistoryAlarmReport;
import com.topvision.ems.epon.report.historyalarm.dao.HistoryAlarmReportDao;
import com.topvision.ems.epon.report.historyalarm.service.HistoryAlarmReportCreator;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.report.dao.StatReportDao;
import com.topvision.ems.report.domain.ExcutorTimePolicy;
import com.topvision.ems.report.domain.FolderEntities;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.domain.StartStopTime;
import com.topvision.ems.report.domain.TopoEntityStastic;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.service.StatReportService;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-29-下午3:20:16
 * 
 */
@Service("historyAlarmReportCreator")
public class HistoryAlarmReportCreatorImpl extends BaseService implements HistoryAlarmReportCreator {
    private static DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    @Autowired
    private HistoryAlarmReportDao historyAlarmReportDao;
    @Autowired
    private StatReportService statReportService;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private StatReportDao statReportDao;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void bulidReport(ReportTask task) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        Map<String, Object> condition = task.getCondition();
        // 封装起止时间
        ExcutorTimePolicy etp = ReportTaskUtil.decodeCronExpression(task.getCycleType(), task.getCronExpression());
        List<StartStopTime> startStopTimes = ReportTaskUtil.getStAndEtTime(etp, new Date());
        queryMap.put("startStopTimes", startStopTimes);
        StringBuilder typesString = new StringBuilder();
        if (Boolean.valueOf(condition.get("eponSelected").toString())) {
            typesString.append(entityTypeService.getOltType());
            if (Boolean.valueOf(condition.get("ccmtsSelected").toString())) {
                typesString.append(", " + entityTypeService.getCcmtsandcmtsType());
            }
        } else {
            if (Boolean.valueOf(condition.get("ccmtsSelected").toString())) {
                typesString.append(entityTypeService.getCcmtsandcmtsType());
            }
        }
        queryMap.put("types", typesString.toString());
        String sql = ReportTaskUtil.getTimeSegmentSql(task, "firstTime");
        queryMap.put("sql", sql);
        queryMap.put("userId", task.getUserId());
        Map<Long, FolderEntities> folderEntities = statHistoryAlarmReport(queryMap);
        Date statDate = new Date();
        Map<String, Boolean> columnDisable = new HashMap<String, Boolean>();
        columnDisable.put("mainAlarmDisable", Boolean.getBoolean(condition.get("mainAlarmDisable").toString()));
        columnDisable.put("minorAlarmDisable", Boolean.getBoolean(condition.get("minorAlarmDisable").toString()));
        columnDisable.put("generalAlarmDisable", Boolean.getBoolean(condition.get("generalAlarmDisable").toString()));
        columnDisable.put("messageDisable", Boolean.getBoolean(condition.get("messageDisable").toString()));

        if (task.isExcelEnabled()) {
            String filePath = exportAsExcelFromTask(folderEntities, columnDisable, statDate, task);
            reportInstanceService.addReportInstance(filePath, EXCEL, task);
        }
    }

    @Override
    public List<HistoryAlarmDetail> statHistoryAlarmDetailReport(Map<String, Object> map) {
        List<HistoryAlarmDetail> historyAlarmDetails = new ArrayList<HistoryAlarmDetail>();
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        // 判断是统计所有的还是小计还是单个设备
        if (map.get("entityId") != null) {
            // 统计单个设备
            historyAlarmDetails = historyAlarmReportDao.statEntityHistoryAlarmDetail(map);
        } else if (map.get("folderId") != null) {
            Long folderId = (Long) map.get("folderId");
            // 统计一个区域,获取该地域下的所有设备，针对每个设备去获取相关告警
            List<Long> entityIds = statReportDao.selectEntityIdsByFolderId(folderId, (String) map.get("types"));
            for (Long entityId : entityIds) {
                map.put("entityId", entityId);
                List<HistoryAlarmDetail> alarmDetails = historyAlarmReportDao.statEntityHistoryAlarmDetail(map);
                historyAlarmDetails.addAll(alarmDetails);
            }
        } else {
            // 统计所有设备
            // 获取所选设备类型的所有设备ID
            List<Long> entityIds = new ArrayList<Long>();
            String typeArr = (String) map.get("types");
            String[] types = typeArr.split(",");
            for (String type : types) {
                entityIds.addAll(entityDao.getEntityIdsByAuthority(Long.valueOf(type)));
            }
            for (Long entityId : entityIds) {
                map.put("entityId", entityId);
                List<HistoryAlarmDetail> alarmDetails = historyAlarmReportDao.statEntityHistoryAlarmDetail(map);
                historyAlarmDetails.addAll(alarmDetails);
            }
        }
        return historyAlarmDetails;
    }

    @Override
    public void exportHistoryDetailReportToExcel(List<HistoryAlarmDetail> historyAlarmDetails, Date statDate) {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");
        String fileName;
        try {
            fileName = URLEncoder.encode(ReportTaskUtil.getString("report.historyAlarmDetailStatic", "report") + "-"
                    + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();

            Label label;
            // 创建表头等信息
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            // 计算出应该分多少个sheet(每个sheet放60000个CM)
            Integer sheetNum = historyAlarmDetails.size() / 60000 + 1, lastPageCm = historyAlarmDetails.size() % 60000;

            // 第一页需要包含表头等信息
            WritableSheet sheet = workbook.createSheet("Sheet_1", 0);
            // 创建头部信息
            ReportTaskUtil.formatExcelHeader(sheet,
                    ReportTaskUtil.getString("report.historyAlarmDetailStatic", "report"), statDate, 6);

            // excel的内容cell的格式
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            for (int curSheetNum = 0, rowNum = 2, endNum = 60000; curSheetNum < sheetNum; curSheetNum++) {
                if (curSheetNum > 0) {
                    sheet = workbook.createSheet("Sheet_" + (curSheetNum + 1), curSheetNum);
                }
                // 设置宽度
                sheet.setColumnView(0, 70);
                sheet.setColumnView(1, 40);
                for (int i = 2; i < 5; i++) {
                    sheet.setColumnView(i, 15);
                }
                sheet.setColumnView(5, 25);
                WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
                label = new Label(0, rowNum, ReportTaskUtil.getString("report.alarmDescr", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, ReportTaskUtil.getString("report.alertType", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, ReportTaskUtil.getString("report.relateDevice", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, ReportTaskUtil.getString("report.firstTime", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(4, rowNum, ReportTaskUtil.getString("report.clearUser", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(5, rowNum, ReportTaskUtil.getString("report.clearTime", "report"), titleCellFormat);
                sheet.addCell(label);
                rowNum++;
                if (curSheetNum == sheetNum - 1) {// the last sheet
                    endNum = lastPageCm;
                }
                for (int k = 0; k < endNum; k++) {
                    HistoryAlarmDetail historyAlarmDetail = historyAlarmDetails.get(curSheetNum * 60000 + k);
                    label = new Label(0, rowNum, historyAlarmDetail.getMessage(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(1, rowNum, historyAlarmDetail.getDisplayName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(2, rowNum, historyAlarmDetail.getEntityName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(3, rowNum, historyAlarmDetail.getFirstTimeStr(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(4, rowNum, historyAlarmDetail.getClearUser(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(5, rowNum, historyAlarmDetail.getClearTimeStr(), contentCellFormat);
                    sheet.addCell(label);
                    rowNum++;
                }
                rowNum = 0;
            }

            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("export method is error:{}", e);
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
                logger.debug("something wrong with I/O:", e);
            }
        }

    }

    @Override
    public Map<Long, FolderEntities> statHistoryAlarmReport(Map<String, Object> map) {
        // 获取当前用户权限下的地域与指定类型设备之间的关系
        List<TopoEntityStastic> entityStastics = new ArrayList<TopoEntityStastic>();
        // 获取统计的设备类型
        String types = map.get("types").toString();
        if (types.indexOf(",") != -1) {
            entityStastics.addAll(statReportService.getTopoEntityRelation(entityTypeService.getOltType(), map));
            entityStastics
                    .addAll(statReportService.getTopoEntityRelation(entityTypeService.getCcmtsandcmtsType(), map));
        } else {
            entityStastics.addAll(statReportService.getTopoEntityRelation(Long.valueOf(types), map));
        }
        Map<Long, FolderEntities> folderMap = new LinkedHashMap<Long, FolderEntities>();
        for (TopoEntityStastic stastic : entityStastics) {
            Long folderId = stastic.getFolderId();
            if (folderMap.containsKey(folderId)) {
                FolderEntities folderEntities = folderMap.get(folderId);
                Map<Long, Object> entitiesMap = folderEntities.getEntities();
                entitiesMap.put(stastic.getEntityId(), null);
            } else {
                Map<Long, Object> entities = new LinkedHashMap<Long, Object>();
                entities.put(stastic.getEntityId(), null);
                FolderEntities folderEntities = new FolderEntities();
                folderEntities.setFolderId(folderId);
                folderEntities.setFolderName(stastic.getFolderName());
                folderEntities.setEntities(entities);
                folderEntities.setRowspan(1);
                folderMap.put(folderId, folderEntities);
            }
        }
        return historyAlarmReportDao.statHistoryAlarmReport(folderMap, map);
    }

    @Override
    public void exportHistoryAlertReportToExcel(Map<Long, FolderEntities> historyAlarmReport,
            Map<String, Boolean> columnDisable, Date statDate, String conditions) {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");
        String fileName;
        try {
            fileName = URLEncoder.encode(ReportTaskUtil.getString("report.historyAlarmStatic", "report") + "-"
                    + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            createHistoryAlarmReportExcelFile(historyAlarmReport, columnDisable, workbook, statDate, conditions);
        } catch (Exception e) {
            logger.debug("exportHistoryAlertReportToExcel method is error:{}", e);
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

    private String exportAsExcelFromTask(Map<Long, FolderEntities> folderEntities, Map<String, Boolean> columnDisable,
            Date statDate, ReportTask task) {
        String filePath = ReportExportUtil.getExportUrl(task, EXCEL);
        File file = new File(filePath);
        try {
            file.createNewFile();
            WritableWorkbook workbook = Workbook.createWorkbook(file);

            // 根据task整理出conditions
            String conditions = getTaskConditions(task);
            createHistoryAlarmReportExcelFile(folderEntities, columnDisable, workbook, statDate, conditions);
        } catch (IOException e) {
            logger.debug("something wrong with I/O:", e);
            e.printStackTrace();
        }
        return filePath;
    }

    private void createHistoryAlarmReportExcelFile(Map<Long, FolderEntities> folderEntities,
            Map<String, Boolean> columnDisable, WritableWorkbook workbook, Date statDate, String conditions) {
        // 创建sheet页和一些格式化信息
        WritableSheet sheet = workbook.createSheet("Sheet1", 0);
        // 第2列为设备名称，偏长，需要设置宽度
        sheet.setColumnView(0, 20);
        sheet.setColumnView(1, 50);
        for (int i = 2; i < 10; i++) {
            sheet.setColumnView(i, 15);
        }
        Label label;
        try {
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();

            label = new Label(0, 5, ReportTaskUtil.getString("report.folder", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 5, ReportTaskUtil.getString("label.entityName", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 5, ReportTaskUtil.getString("report.deviceType", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 5, ReportTaskUtil.getString("report.allAlarm", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 5, ReportTaskUtil.getString("report.emergencyAlarm", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(5, 5, ReportTaskUtil.getString("report.seriousAlarm", "report"), titleCellFormat);
            sheet.addCell(label);
            int titleCell = 6;
            if (columnDisable.get("mainAlarmDisable")) {
                label = new Label(titleCell++, 5, ReportTaskUtil.getString("report.mainAlarm", "report"),
                        titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("minorAlarmDisable")) {
                label = new Label(titleCell++, 5, ReportTaskUtil.getString("report.minorAlarm", "report"),
                        titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("generalAlarmDisable")) {
                label = new Label(titleCell++, 5, ReportTaskUtil.getString("report.generalAlarm", "report"),
                        titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("messageDisable")) {
                label = new Label(titleCell++, 5, ReportTaskUtil.getString("report.message", "report"), titleCellFormat);
                sheet.addCell(label);
            }
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.historyAlarmStatic", "report"),
                    statDate, conditions, titleCell);
            int rowNum = 6;

            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            List<Long> countedEntityIds = new ArrayList<Long>();
            // 总计
            Long totalAllAlarmNum = 0l;
            Long totalEmergencyAlarmNum = 0l;
            Long totalSeriousAlarmNum = 0l;
            Long totalMainAlarmNum = 0l;
            Long totalMinorAlarmNum = 0l;
            Long totalGeneralAlarmNum = 0l;
            Long totalMessageNum = 0l;
            if (folderEntities == null) {
                return;
            }
            // 遍历每个地域
            for (Long folderId : folderEntities.keySet()) {
                // 地域小计
                Long allAlarmNum = 0l;
                Long emergencyAlarmNum = 0l;
                Long seriousAlarmNum = 0l;
                Long mainAlarmNum = 0l;
                Long minorAlarmNum = 0l;
                Long generalAlarmNum = 0l;
                Long messageNum = 0l;
                // 遍历该地域下的每台设备
                Map<Long, Object> entityMap = folderEntities.get(folderId).getEntities();
                if (entityMap == null) {
                    continue;
                }
                for (Long entityId : entityMap.keySet()) {
                    HistoryAlarmReport historyAlarmReport = (HistoryAlarmReport) entityMap.get(entityId);
                    if (historyAlarmReport == null) {
                        continue;
                    }
                    // 更新统计值
                    allAlarmNum += historyAlarmReport.getAllAlarmNum();
                    emergencyAlarmNum += historyAlarmReport.getEmergencyAlarmNum();
                    seriousAlarmNum += historyAlarmReport.getSeriousAlarmNum();
                    mainAlarmNum += historyAlarmReport.getMainAlarmNum();
                    minorAlarmNum += historyAlarmReport.getMinorAlarmNum();
                    generalAlarmNum += historyAlarmReport.getGeneralAlarmNum();
                    messageNum += historyAlarmReport.getMessageNum();

                    if (!countedEntityIds.contains(entityId)) {
                        countedEntityIds.add(entityId);
                        totalAllAlarmNum += historyAlarmReport.getAllAlarmNum();
                        totalEmergencyAlarmNum += historyAlarmReport.getEmergencyAlarmNum();
                        totalSeriousAlarmNum += historyAlarmReport.getSeriousAlarmNum();
                        totalMainAlarmNum += historyAlarmReport.getMainAlarmNum();
                        totalMinorAlarmNum += historyAlarmReport.getMinorAlarmNum();
                        totalGeneralAlarmNum += historyAlarmReport.getGeneralAlarmNum();
                        totalMessageNum += historyAlarmReport.getMessageNum();
                    }
                    // 输出数据行
                    label = new Label(0, rowNum, folderEntities.get(folderId).getFolderName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(1, rowNum, historyAlarmReport.getEntityName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(2, rowNum, historyAlarmReport.getDisplayName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(3, rowNum, historyAlarmReport.getAllAlarmNum().toString(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(4, rowNum, historyAlarmReport.getEmergencyAlarmNum().toString(),
                            contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(5, rowNum, historyAlarmReport.getSeriousAlarmNum().toString(), contentCellFormat);
                    sheet.addCell(label);
                    int contentCell = 6;
                    if (columnDisable.get("mainAlarmDisable")) {
                        label = new Label(contentCell++, rowNum, historyAlarmReport.getMainAlarmNum().toString(),
                                contentCellFormat);
                        sheet.addCell(label);
                    }
                    if (columnDisable.get("minorAlarmDisable")) {
                        label = new Label(contentCell++, rowNum, historyAlarmReport.getMinorAlarmNum().toString(),
                                contentCellFormat);
                        sheet.addCell(label);
                    }
                    if (columnDisable.get("generalAlarmDisable")) {
                        label = new Label(contentCell++, rowNum, historyAlarmReport.getGeneralAlarmNum().toString(),
                                contentCellFormat);
                        sheet.addCell(label);
                    }
                    if (columnDisable.get("messageDisable")) {
                        label = new Label(contentCell++, rowNum, historyAlarmReport.getMessageNum().toString(),
                                contentCellFormat);
                        sheet.addCell(label);
                    }
                    rowNum++;
                }
                // 输出地域小计
                label = new Label(1, rowNum, ReportTaskUtil.getString("report.subtotal", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, "", titleCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, allAlarmNum.toString(), titleCellFormat);
                sheet.addCell(label);
                label = new Label(4, rowNum, emergencyAlarmNum.toString(), titleCellFormat);
                sheet.addCell(label);
                label = new Label(5, rowNum, seriousAlarmNum.toString(), titleCellFormat);
                sheet.addCell(label);
                int sumCell = 6;
                if (columnDisable.get("mainAlarmDisable")) {
                    label = new Label(sumCell++, rowNum, mainAlarmNum.toString(), titleCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("minorAlarmDisable")) {
                    label = new Label(sumCell++, rowNum, minorAlarmNum.toString(), titleCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("generalAlarmDisable")) {
                    label = new Label(sumCell++, rowNum, generalAlarmNum.toString(), titleCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("messageDisable")) {
                    label = new Label(sumCell++, rowNum, messageNum.toString(), titleCellFormat);
                    sheet.addCell(label);
                }
                // 合并地域单元格
                sheet.mergeCells(0, rowNum - folderEntities.get(folderId).getRowspan() + 1, 0, rowNum);

                rowNum++;
            }
            // 输出总计
            label = new Label(1, rowNum, ReportTaskUtil.getString("report.totalling", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, rowNum, "", titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, rowNum, totalAllAlarmNum.toString(), titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, rowNum, totalEmergencyAlarmNum.toString(), titleCellFormat);
            sheet.addCell(label);
            label = new Label(5, rowNum, totalSeriousAlarmNum.toString(), titleCellFormat);
            sheet.addCell(label);
            int sumCell = 6;
            if (columnDisable.get("mainAlarmDisable")) {
                label = new Label(sumCell++, rowNum, totalMainAlarmNum.toString(), titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("minorAlarmDisable")) {
                label = new Label(sumCell++, rowNum, totalMinorAlarmNum.toString(), titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("generalAlarmDisable")) {
                label = new Label(sumCell++, rowNum, totalGeneralAlarmNum.toString(), titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("messageDisable")) {
                label = new Label(sumCell++, rowNum, totalMessageNum.toString(), titleCellFormat);
                sheet.addCell(label);
            }
            rowNum++;
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("createCurrentAlarmReportExcelFile method is error:{}", e);
        }
    }

    /**
     * 得到生成EXCEL文件的统计条件
     * 
     * @param reportTask
     * @return
     */
    private static String getTaskConditions(ReportTask reportTask) {
        ExcutorTimePolicy policy = ReportTaskUtil.decodeCronExpression(reportTask);
        Map<String, Object> condition = reportTask.getCondition();
        List<StartStopTime> stAndEtTimes = ReportTaskUtil.getStAndEtTime(policy, new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(ReportTaskUtil.getString("report.statTime", "report") + ":\n");
        for (StartStopTime time : stAndEtTimes) {
            Date st = time.getStTime();
            Date et = time.getEtTime();
            sb.append(formatter.format(st)).append("    " + ReportTaskUtil.getString("report.to", "report") + "    ")
                    .append(formatter.format(et)).append("\n");
        }
        sb.append(ReportTaskUtil.getString("report.deviceType", "report") + ":    ");

        if (Boolean.valueOf(condition.get("eponSelected").toString())) {
            sb.append("EPON  ");
            if (Boolean.valueOf(condition.get("ccmtsSelected").toString())) {
                sb.append("CMTS");
            }
        } else {
            if (Boolean.valueOf(condition.get("ccmtsSelected").toString())) {
                sb.append("CMTS");
            }
        }
        return sb.toString();
    }

}
