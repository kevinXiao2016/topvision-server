/***********************************************************************
 * $Id: CurrentAlarmReportCreatorImpl.java,v1.0 2013-10-29 下午2:36:49 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.currentalarm.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import com.topvision.ems.epon.report.currentalarm.dao.CurrentAlarmReportDao;
import com.topvision.ems.epon.report.currentalarm.service.CurrentAlarmReportCreator;
import com.topvision.ems.epon.report.domain.CurrentAlarmDetail;
import com.topvision.ems.epon.report.domain.CurrentAlarmReport;
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
 * @created @2013-10-29-下午2:36:49
 * 
 */
@Service("currentAlarmReportCreator")
public class CurrentAlarmReportCreatorImpl extends BaseService implements CurrentAlarmReportCreator {
    @Autowired
    private CurrentAlarmReportDao currentAlarmReportDao;
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
        String startStopTimes = ReportTaskUtil.getTimeSegmentSql(task, "lastTime");
        queryMap.put("sql", startStopTimes);
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
        queryMap.put("userId", task.getUserId());
        Map<Long, FolderEntities> currentAlarmReport = statCurrentAlarmReport(queryMap);
        Date statDate = new Date();
        Map<String, Boolean> columnDisable = new HashMap<String, Boolean>();
        columnDisable.put("mainAlarmDisable", Boolean.valueOf(condition.get("mainAlarmDisable").toString()));
        columnDisable.put("minorAlarmDisable", Boolean.valueOf(condition.get("minorAlarmDisable").toString()));
        columnDisable.put("generalAlarmDisable", Boolean.valueOf(condition.get("generalAlarmDisable").toString()));
        columnDisable.put("messageDisable", Boolean.valueOf(condition.get("messageDisable").toString()));
        if (task.isExcelEnabled()) {
            String filePath = exportAsExcelFromTask(currentAlarmReport, columnDisable, statDate, task);
            reportInstanceService.addReportInstance(filePath, EXCEL, task);
        }
    }

    @Override
    public List<CurrentAlarmDetail> statCurrentAlarmDetailReport(Map<String, Object> map) {
        List<CurrentAlarmDetail> currentAlarmDetails = new ArrayList<CurrentAlarmDetail>();
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        // 判断是统计所有的还是小计还是单个设备
        if (map.get("entityId") != null) {
            // 统计单个设备
            currentAlarmDetails = currentAlarmReportDao.statEntityCurrentAlarmDetail(map);
        } else if (map.get("folderId") != null) {
            Long folderId = (Long) map.get("folderId");
            // 统计一个区域,获取该地域下的所有设备，针对每个设备去获取相关告警
            List<Long> entityIds = statReportDao.selectEntityIdsByFolderId(folderId, (String) map.get("types"));
            for (Long entityId : entityIds) {
                map.put("entityId", entityId);
                List<CurrentAlarmDetail> alarmDetails = currentAlarmReportDao.statEntityCurrentAlarmDetail(map);
                currentAlarmDetails.addAll(alarmDetails);
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
                List<CurrentAlarmDetail> alarmDetails = currentAlarmReportDao.statEntityCurrentAlarmDetail(map);
                currentAlarmDetails.addAll(alarmDetails);
            }
        }
        return currentAlarmDetails;
    }

    @Override
    public Map<Long, FolderEntities> statCurrentAlarmReport(Map<String, Object> map) {
        // 获取当前用户权限下的地域与指定类型设备之间的关系
        List<TopoEntityStastic> entityStastics = new ArrayList<TopoEntityStastic>();
        // 获取统计的设备类型
        String typeStr = map.get("types").toString();
        if (typeStr.indexOf(",") != -1) {
            entityStastics.addAll(statReportService.getTopoEntityRelation(entityTypeService.getOltType(), map));
            entityStastics
                    .addAll(statReportService.getTopoEntityRelation(entityTypeService.getCcmtsandcmtsType(), map));
        } else {
            entityStastics.addAll(statReportService.getTopoEntityRelation(Long.valueOf(typeStr), map));
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
        return currentAlarmReportDao.statCurrentAlarmReport(folderMap, map);
    }

    @Override
    public void exportCurAlertReportToExcel(Map<Long, FolderEntities> currentAlarmReport,
            Map<String, Boolean> columnDisable, Date statDate, String conditions) {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");

        String fileName;
        try {
            fileName = URLEncoder.encode(ReportTaskUtil.getString("report.currentDetailReport", "report") + "-"
                    + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            createCurrentAlarmReportExcelFile(currentAlarmReport, columnDisable, workbook, statDate, conditions);
        } catch (UnsupportedEncodingException e) {
            logger.debug("unsupported Encoding Exception:", e);
        } catch (IOException e) {
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

    private String exportAsExcelFromTask(Map<Long, FolderEntities> currentAlarmReport,
            Map<String, Boolean> columnDisable, Date statDate, ReportTask task) {
        String filePath = ReportExportUtil.getExportUrl(task, EXCEL);
        File file = new File(filePath);
        try {
            file.createNewFile();
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            // 根据task整理出conditions
            String conditions = getTaskConditions(task);

            createCurrentAlarmReportExcelFile(currentAlarmReport, columnDisable, workbook, statDate, conditions);
        } catch (IOException e) {
            logger.debug("something wrong with I/O:", e);
        }
        return filePath;
    }

    private void createCurrentAlarmReportExcelFile(Map<Long, FolderEntities> folderEntities,
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
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.currentAlarmStatic", "report"),
                    statDate, conditions, 6);

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
                    CurrentAlarmReport currentAlarmReport = (CurrentAlarmReport) entityMap.get(entityId);
                    if (currentAlarmReport == null) {
                        continue;
                    }
                    // 更新统计值
                    allAlarmNum += currentAlarmReport.getAllAlarmNum();
                    emergencyAlarmNum += currentAlarmReport.getEmergencyAlarmNum();
                    seriousAlarmNum += currentAlarmReport.getSeriousAlarmNum();
                    mainAlarmNum += currentAlarmReport.getMainAlarmNum();
                    minorAlarmNum += currentAlarmReport.getMinorAlarmNum();
                    generalAlarmNum += currentAlarmReport.getGeneralAlarmNum();
                    messageNum += currentAlarmReport.getMessageNum();

                    if (!countedEntityIds.contains(entityId)) {
                        countedEntityIds.add(entityId);
                        totalAllAlarmNum += currentAlarmReport.getAllAlarmNum();
                        totalEmergencyAlarmNum += currentAlarmReport.getEmergencyAlarmNum();
                        totalSeriousAlarmNum += currentAlarmReport.getSeriousAlarmNum();
                        totalMainAlarmNum += currentAlarmReport.getMainAlarmNum();
                        totalMinorAlarmNum += currentAlarmReport.getMinorAlarmNum();
                        totalGeneralAlarmNum += currentAlarmReport.getGeneralAlarmNum();
                        totalMessageNum += currentAlarmReport.getMessageNum();
                    }
                    // 输出数据行
                    label = new Label(0, rowNum, folderEntities.get(folderId).getFolderName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(1, rowNum, currentAlarmReport.getEntityName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(2, rowNum, currentAlarmReport.getDisplayName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(3, rowNum, currentAlarmReport.getAllAlarmNum().toString(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(4, rowNum, currentAlarmReport.getEmergencyAlarmNum().toString(),
                            contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(5, rowNum, currentAlarmReport.getSeriousAlarmNum().toString(), contentCellFormat);
                    sheet.addCell(label);
                    int contentCell = 6;
                    if (columnDisable.get("mainAlarmDisable")) {
                        label = new Label(contentCell++, rowNum, currentAlarmReport.getMainAlarmNum().toString(),
                                contentCellFormat);
                        sheet.addCell(label);
                    }
                    if (columnDisable.get("minorAlarmDisable")) {
                        label = new Label(contentCell++, rowNum, currentAlarmReport.getMinorAlarmNum().toString(),
                                contentCellFormat);
                        sheet.addCell(label);
                    }
                    if (columnDisable.get("generalAlarmDisable")) {
                        label = new Label(contentCell++, rowNum, currentAlarmReport.getGeneralAlarmNum().toString(),
                                contentCellFormat);
                        sheet.addCell(label);
                    }
                    if (columnDisable.get("messageDisable")) {
                        label = new Label(contentCell++, rowNum, currentAlarmReport.getMessageNum().toString(),
                                contentCellFormat);
                        sheet.addCell(label);
                    }
                    rowNum++;
                }
                // 输出地域小计
                label = new Label(0, rowNum, folderEntities.get(folderId).getFolderName(), contentCellFormat);
                sheet.addCell(label);
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

    @Override
    public void exportCurrentDetailReportToExcel(List<CurrentAlarmDetail> currentAlarmDetails, Date statDate) {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");
        try {
            String fileName = URLEncoder.encode(ReportTaskUtil.getString("report.currentDetailReport", "report") + "-"
                    + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();

            Label label;
            // 创建表头等信息
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            // 计算出应该分多少个sheet(每个sheet放60000个CM)
            Integer sheetNum = currentAlarmDetails.size() / 60000 + 1, lastPageCm = currentAlarmDetails.size() % 60000;

            // 第一页需要包含表头等信息
            WritableSheet sheet = workbook.createSheet("Sheet_1", 0);
            // 创建头部信息
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.currentDetailReport", "report"),
                    statDate, 7);

            // excel的内容cell的格式
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            for (int curSheetNum = 0, rowNum = 2, endNum = 60000; curSheetNum < sheetNum; curSheetNum++) {
                if (curSheetNum > 0) {
                    sheet = workbook.createSheet("Sheet_" + (curSheetNum + 1), curSheetNum);
                }
                // 设置宽度
                sheet.setColumnView(0, 60);
                sheet.setColumnView(1, 25);
                for (int i = 2; i < 6; i++) {
                    sheet.setColumnView(i, 15);
                }
                WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
                label = new Label(0, rowNum, ReportTaskUtil.getString("report.alarmDescr", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, ReportTaskUtil.getString("report.alertType", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, ReportTaskUtil.getString("report.relateDevice", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, ReportTaskUtil.getString("report.lastTime", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(4, rowNum, ReportTaskUtil.getString("report.queryStatus", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(5, rowNum, ReportTaskUtil.getString("report.queryUser", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(6, rowNum, ReportTaskUtil.getString("report.queryTime", "report"), titleCellFormat);
                sheet.addCell(label);
                rowNum++;
                if (curSheetNum == sheetNum - 1) {// the last sheet
                    endNum = lastPageCm;
                }
                for (int k = 0; k < endNum; k++) {
                    CurrentAlarmDetail currentAlarmDetail = currentAlarmDetails.get(curSheetNum * 60000 + k);
                    label = new Label(0, rowNum, currentAlarmDetail.getMessage(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(1, rowNum, currentAlarmDetail.getDisplayName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(2, rowNum, currentAlarmDetail.getEntityName(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(3, rowNum, currentAlarmDetail.getLastTimeStr(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(4, rowNum, currentAlarmDetail.getStatusStr(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(5, rowNum, currentAlarmDetail.getConfirmUser(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(6, rowNum, currentAlarmDetail.getConfirmTimeStr(), contentCellFormat);
                    sheet.addCell(label);
                    rowNum++;
                }
                rowNum = 0;
            }

            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("exportCurrentDetailReportToExcel method is error:{}", e);
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
            sb.append(formatter.format(st)).append("    ").append(ReportTaskUtil.getString("report.to", "report"))
                    .append("    ").append(formatter.format(et)).append("\n");
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
