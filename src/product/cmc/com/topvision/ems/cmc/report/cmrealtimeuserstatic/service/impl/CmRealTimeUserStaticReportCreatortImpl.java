/***********************************************************************
 * $Id: CmRealTimeUserStaticReporCreatorImpl.java,v1.0 2013-10-30 上午10:13:34 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmrealtimeuserstatic.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.report.cmrealtimeuserstatic.dao.CmRealTimeUserStaticReportDao;
import com.topvision.ems.cmc.report.cmrealtimeuserstatic.service.CmRealTimeUserStaticReportCreator;
import com.topvision.ems.cmc.report.domain.CmRealTimeUserStaticReport;
import com.topvision.ems.cmc.report.domain.FolderOltRelation;
import com.topvision.ems.cmc.report.domain.OltPonRelation;
import com.topvision.ems.cmc.report.domain.PonCmcRelation;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.domain.TopoEntityStastic;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.service.StatReportService;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;

/**
 * @author haojie
 * @created @2013-10-30-上午10:13:34
 * 
 */
@Service("cmRealTimeUserStaticReportCreator")
public class CmRealTimeUserStaticReportCreatortImpl extends BaseService implements CmRealTimeUserStaticReportCreator {
    @Autowired
    private CmRealTimeUserStaticReportDao cmRealTimeUserStaticReportDao;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private StatReportService statReportService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void bulidReport(ReportTask reportTask) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("userId", reportTask.getUserId());
        Map<String, FolderOltRelation> cmRealTimeUserStaticReport = statCmRealTimeUserStaticReport(queryMap);
        Date statDate = new Date();

        if (reportTask.isExcelEnabled()) {
            String filePath = exportAsExcelFromTask(cmRealTimeUserStaticReport, statDate, reportTask);
            reportInstanceService.addReportInstance(filePath, EXCEL, reportTask);
        }
    }

    @Override
    public Map<String, FolderOltRelation> statCmRealTimeUserStaticReport(Map<String, Object> queryMap) {
        // 获取当前用户权限下的地域与指定类型设备之间的关系
        List<TopoEntityStastic> entityStastics = statReportService.getTopoEntityRelation(entityTypeService.getOltType(), queryMap);
        // 将entityStastics转为fold-entity的结构
        Map<String, FolderOltRelation> folderOltRelations = new LinkedHashMap<String, FolderOltRelation>();
        for (TopoEntityStastic stastic : entityStastics) {
            Long folderId = stastic.getFolderId();
            if (folderOltRelations.containsKey(folderId.toString())) {
                // 如果该地域存在，则更新该地域下的OLT信息
                FolderOltRelation folderOltRelation = folderOltRelations.get(folderId.toString());
                Map<String, OltPonRelation> entitiesMap = folderOltRelation.getOltPonRelations();
                entitiesMap.put(stastic.getEntityIp(), null);
            } else {
                // 如果该地域不存在，则初始化该地域
                Map<String, OltPonRelation> entitiesMap = new HashMap<String, OltPonRelation>();
                entitiesMap.put(stastic.getEntityIp(), null);
                FolderOltRelation folderOltRelation = new FolderOltRelation(folderId, stastic.getFolderName(),
                        entitiesMap, 0);
                folderOltRelations.put(folderId.toString(), folderOltRelation);
            }
        }
        // 查询出以CC8800A为单位的用户统计信息
        List<CmRealTimeUserStaticReport> lists = cmRealTimeUserStaticReportDao.loadCmRealTimeUserStaticData();
        // 遍历来插入数据
        for (CmRealTimeUserStaticReport singleLine : lists) {
            for (String folderIdStr : folderOltRelations.keySet()) {
                FolderOltRelation folderOltRelation = folderOltRelations.get(folderIdStr);
                Map<String, OltPonRelation> oltPonRelations = folderOltRelation.getOltPonRelations();
                for (String oltIp : oltPonRelations.keySet()) {
                    if (oltIp.equals(singleLine.getOltIp())) {
                        // 该数据需放在该OLT下
                        OltPonRelation oltPonRelation = oltPonRelations.get(oltIp);
                        if (oltPonRelation == null) {
                            // 如果该OLT下还未初始化，则初始化数据
                            // 初始化PON-CMC的关系
                            List<CmRealTimeUserStaticReport> reportDatas = new ArrayList<CmRealTimeUserStaticReport>();
                            reportDatas.add(singleLine);
                            PonCmcRelation ponCmcRelation = new PonCmcRelation(singleLine.getPonIndex(),
                                    singleLine.getPonIndexStr(), reportDatas);
                            // 初始化OLT-PON的关系
                            Map<String, PonCmcRelation> ponCmcMap = new LinkedHashMap<String, PonCmcRelation>();
                            ponCmcMap.put(singleLine.getPonIndex().toString(), ponCmcRelation);
                            oltPonRelation = new OltPonRelation(singleLine.getOltIp(), singleLine.getOltName(),
                                    ponCmcMap, 1);
                            // 向当前地域添加该OLT
                            oltPonRelations.put(singleLine.getOltIp(), oltPonRelation);
                            // 更新当前地域的rowspan(多一个OLT就多一个OLT小计行)
                            folderOltRelation.setRowspan(folderOltRelation.getRowspan() + 2);
                        } else {
                            // 当前OLT下PON口信息
                            Map<String, PonCmcRelation> ponCmcRelations = oltPonRelation.getPonCmcRelations();
                            // 该数据需放在该PON口下
                            PonCmcRelation ponCmcRelation = ponCmcRelations.get(singleLine.getPonIndex().toString());
                            if (ponCmcRelation == null) {
                                // 如果该PON下还未初始化，则初始化数据
                                // 向当前地域添加该PON(初始化CMC)
                                List<CmRealTimeUserStaticReport> reportDatas = new ArrayList<CmRealTimeUserStaticReport>();
                                reportDatas.add(singleLine);
                                ponCmcRelation = new PonCmcRelation(singleLine.getPonIndex(),
                                        singleLine.getPonIndexStr(), reportDatas);
                                ponCmcRelations.put(singleLine.getPonIndex().toString(), ponCmcRelation);
                                // 更新当前地域及OLT的rowspan
                                oltPonRelation.setRowspan(oltPonRelation.getRowspan() + 1);
                                folderOltRelation.setRowspan(folderOltRelation.getRowspan() + 1);
                            } else {
                                // 当前PON口下的所有CCMTS的信息
                                List<CmRealTimeUserStaticReport> reportDatas = ponCmcRelation.getReportDatas();
                                // 因为CCMTS是叶子元素，因此肯定不会重复，故直接插入即可
                                reportDatas.add(singleLine);
                                // 更新当前地域、OLT的rowspan
                                oltPonRelation.setRowspan(oltPonRelation.getRowspan() + 1);
                                folderOltRelation.setRowspan(folderOltRelation.getRowspan() + 1);
                            }
                        }
                    }
                }
            }
        }
        // 由于可能某OLT下未挂有8800A，将其去掉
        for (String folderIdStr : folderOltRelations.keySet()) {
            FolderOltRelation folderOltRelation = folderOltRelations.get(folderIdStr);
            Map<String, OltPonRelation> oltPonRelations = folderOltRelation.getOltPonRelations();
            Iterator<Entry<String, OltPonRelation>> i = oltPonRelations.entrySet().iterator();
            while (i.hasNext()) {
                if (i.next().getValue() == null) {
                    i.remove();
                }
            }
        }
        return folderOltRelations;
    }

    @Override
    public void exportReportToExcel(Map<String, FolderOltRelation> cmRealTimeUserStaticReport, Date date) {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");

        String fileName;
        try {
            fileName = URLEncoder.encode(ReportTaskUtil.getString("report.cmRealTimeUserStaticReportCreator", "report")
                    + "-" + fileNameFormatter.format(date) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            createCmRealTimeUserStaticReportExcelFile(cmRealTimeUserStaticReport, workbook, date);
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

    private String exportAsExcelFromTask(Map<String, FolderOltRelation> cmRealTimeUserStaticReport, Date statDate,
            ReportTask reportTask) {
        String filePath = ReportExportUtil.getExportUrl(reportTask, EXCEL);
        File file = new File(filePath);
        try {
            file.createNewFile();
            WritableWorkbook workbook = Workbook.createWorkbook(file);

            createCmRealTimeUserStaticReportExcelFile(cmRealTimeUserStaticReport, workbook, statDate);
        } catch (IOException e) {
            logger.debug("something wrong with I/O:", e);
        }
        return filePath;
    }

    private void createCmRealTimeUserStaticReportExcelFile(Map<String, FolderOltRelation> folderOltRelations,
            WritableWorkbook workbook, Date statDate) {
        // 创建sheet页和一些格式化信息
        WritableSheet sheet = workbook.createSheet("Sheet1", 0);
        // 第2/3/5列为设备名称，偏长，需要设置宽度
        for (int i = 2; i < 10; i++) {
            sheet.setColumnView(i, 15);
        }
        sheet.setColumnView(0, 20);
        sheet.setColumnView(1, 30);
        sheet.setColumnView(2, 20);
        sheet.setColumnView(4, 30);
        try {
            ReportTaskUtil.formatExcelHeader(sheet,
                    ReportTaskUtil.getString("report.cmRealTimeUserStaticReportCreator", "report"), statDate, null, 9);
        } catch (Exception e1) {
            logger.debug("formatExcelHeader error:{}", e1);
        }
        try {
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            String[] titleValues = { ReportTaskUtil.getString("report.folder", "report"),
                    ReportTaskUtil.getString("report.oltName", "report"), "OLT_IP",
                    ReportTaskUtil.getString("report.pon", "report"),
                    ReportTaskUtil.getString("report.ccmtsName", "report"),
                    ReportTaskUtil.getString("report.onLineCmNum", "report"),
                    ReportTaskUtil.getString("report.offLineCmNum", "report"),
                    ReportTaskUtil.getString("report.otherStatusCmNum", "report"),
                    ReportTaskUtil.getString("report.allStatusCmNum", "report") };
            addRowToExcel(sheet, titleValues, 3, titleCellFormat);
            int rowNum = 4;

            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            // 填充内容
            List<Long> countedEntityIds = new ArrayList<Long>();
            // 总计
            int totalOnlineSummary = 0;
            int totalOfflineSummary = 0;
            int totalOtherStatusSummary = 0;
            int totalAllStatusSummary = 0;
            if (folderOltRelations == null) {
                return;
            }
            // 遍历所有的地域
            for (String folderId : folderOltRelations.keySet()) {
                // 地域小计
                int folderOnlineSummary = 0;
                int folderOfflineSummary = 0;
                int folderOtherStatusSummary = 0;
                int folderAllStatusSummary = 0;
                // 地域信息
                FolderOltRelation folderOltRelation = folderOltRelations.get(folderId);
                // 该地域开始行
                int folderRowStartNum = rowNum;
                // 地域下OLT信息
                Map<String, OltPonRelation> oltPonRelations = folderOltRelation.getOltPonRelations();
                // 遍历该地域下的所有OLT
                for (String oltIp : oltPonRelations.keySet()) {
                    // 该OLT开始行
                    int oltRowStartNum = rowNum;
                    // OLT小计
                    int oltOnlineSummary = 0;
                    int oltOfflineSummary = 0;
                    int oltOtherStatusSummary = 0;
                    int oltAllStatusSummary = 0;
                    // OLT信息
                    OltPonRelation oltPonRelation = oltPonRelations.get(oltIp);
                    // OLT下PON信息
                    Map<String, PonCmcRelation> ponCmcRelations = oltPonRelation.getPonCmcRelations();
                    if (ponCmcRelations == null) {
                        continue;
                    }
                    // 遍历该OLT下的所有PON
                    for (String ponIndex : ponCmcRelations.keySet()) {
                        // 该PON口开始行
                        int ponRowStartNum = rowNum;
                        // PON信息
                        PonCmcRelation ponCmcRelation = ponCmcRelations.get(ponIndex);
                        // PON下的CCMTS信息
                        List<CmRealTimeUserStaticReport> reportDatas = ponCmcRelation.getReportDatas();
                        int ponRowSpan = reportDatas.size();
                        // 遍历PON口下的所有数据
                        for (CmRealTimeUserStaticReport data : reportDatas) {
                            // 更新统计值
                            oltOnlineSummary += data.getOnLineCmNum();
                            oltOfflineSummary += data.getOffLineCmNum();
                            oltOtherStatusSummary += data.getOtherStatusCmNum();
                            oltAllStatusSummary += data.getAllStatusCmNum();

                            folderOnlineSummary += data.getOnLineCmNum();
                            folderOfflineSummary += data.getOffLineCmNum();
                            folderOtherStatusSummary += data.getOtherStatusCmNum();
                            folderAllStatusSummary += data.getAllStatusCmNum();

                            if (!countedEntityIds.contains(data.getCmcId())) {
                                countedEntityIds.add(data.getCmcId());
                                totalOnlineSummary += data.getOnLineCmNum();
                                totalOfflineSummary += data.getOffLineCmNum();
                                totalOtherStatusSummary += data.getOtherStatusCmNum();
                                totalAllStatusSummary += data.getAllStatusCmNum();
                            }
                            // 填入数据
                            String[] values = { folderOltRelation.getFolderName(), oltPonRelation.getOltName(),
                                    oltPonRelation.getOltIp(), ponCmcRelation.getPonIndexStr(), data.getCmcName(),
                                    data.getOnLineCmNum().toString(), data.getOffLineCmNum().toString(),
                                    data.getOtherStatusCmNum().toString(), data.getAllStatusCmNum().toString() };
                            addRowToExcel(sheet, values, rowNum, contentCellFormat);
                            rowNum++;
                        }
                        // 合并PON口单元格
                        sheet.mergeCells(3, ponRowStartNum, 3, ponRowStartNum + ponRowSpan - 1);
                    }
                    // 合并OLT名称和OLT的IP单元格
                    sheet.mergeCells(2, oltRowStartNum, 2, oltRowStartNum + oltPonRelation.getRowspan() - 1);
                    sheet.mergeCells(1, oltRowStartNum, 1, oltRowStartNum + oltPonRelation.getRowspan() - 1);
                    // 统计OLT小计
                    String[] values = { "", ReportTaskUtil.getString("report.oltSummary", "report"), "", "", "",
                            String.valueOf(oltOnlineSummary), String.valueOf(oltOfflineSummary),
                            String.valueOf(oltOtherStatusSummary), String.valueOf(oltAllStatusSummary) };
                    addRowToExcel(sheet, values, rowNum, titleCellFormat);
                    rowNum++;
                }
                // 合并地域
                sheet.mergeCells(0, folderRowStartNum, 0, folderRowStartNum + folderOltRelation.getRowspan() - 1);
                // 统计地域小计
                String[] values = { ReportTaskUtil.getString("report.folderSummary", "report"), "", "", "", "",
                        String.valueOf(folderOnlineSummary), String.valueOf(folderOfflineSummary),
                        String.valueOf(folderOtherStatusSummary), String.valueOf(folderAllStatusSummary) };
                addRowToExcel(sheet, values, rowNum, titleCellFormat);
                rowNum++;
            }
            // 添加总计行
            String[] values = { ReportTaskUtil.getString("label.total", "report"), "", "", "", "",
                    String.valueOf(totalOnlineSummary), String.valueOf(totalOfflineSummary),
                    String.valueOf(totalOtherStatusSummary), String.valueOf(totalAllStatusSummary) };
            addRowToExcel(sheet, values, rowNum, titleCellFormat);
            rowNum++;

            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("close fileStream error:{}", e);
        }
    }

    private void addRowToExcel(WritableSheet sheet, String[] values, int rowNum, WritableCellFormat cellFormat)
            throws Exception {
        if (values.length != 9) {
            return;
        }
        Label label = null;
        for (int i = 0; i < values.length; i++) {
            label = new Label(i, rowNum, values[i], cellFormat);
            sheet.addCell(label);
        }
    }
}
