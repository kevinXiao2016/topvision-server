/***********************************************************************
 * $Id: CmtsDeviceListReportCreatorImpl.java,v1.0 2013-11-18 下午1:46:08 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.report.cmtsdevicelist.service.impl;

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

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmts.report.cmtsdevicelist.dao.CmtsDeviceListReportDao;
import com.topvision.ems.cmts.report.cmtsdevicelist.service.CmtsDeviceListReportCreator;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.service.UserPreferencesService;

/**
 * @author haojie
 * @created @2013-11-18-下午1:46:08
 * 
 */
@Service("cmtsDeviceListReportCreator")
public class CmtsDeviceListReportCreatorImpl extends BaseService implements CmtsDeviceListReportCreator {
    @Autowired
    private CmtsDeviceListReportDao cmtsDeviceListReportDao;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void bulidReport(ReportTask task) {
        Date date = new Date();
        Map<String, Object> condition = task.getCondition();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("sortName", condition.get("sortName"));
        List<CmcAttribute> deviceListItems = getDeviceListItem(queryMap);
        // add by fanzidong,格式化MAC地址
        try {
            String displayRule = userPreferencesService.getUserPreference(task.getUserId(), "macDisplayStyle")
                    .getValue();
            for (CmcAttribute attribute : deviceListItems) {
                String formatedMac = MacUtils.convertMacToDisplayFormat(attribute.getTopCcmtsSysMacAddr(), displayRule);
                attribute.setTopCcmtsSysMacAddr(formatedMac);
            }
        } catch (Exception e) {
            logger.debug("error happend, maybe cannot get userId:" + e.getMessage());
        }
        Map<String, Boolean> columnDisable = new HashMap<String, Boolean>();
        columnDisable.put("locationDisplayable", Boolean.valueOf(condition.get("locationDisplayable").toString()));
        columnDisable.put("dutyDisplayable", Boolean.valueOf(condition.get("dutyDisplayable").toString()));
        columnDisable.put("createTimeDisplayable", Boolean.valueOf(condition.get("createTimeDisplayable").toString()));
        if (task.isExcelEnabled()) {
            String filePath = exportAsExcelFromTask(deviceListItems, columnDisable, date, task);
            reportInstanceService.addReportInstance(filePath, EXCEL, task);
        }
    }

    private String exportAsExcelFromTask(List<CmcAttribute> deviceListItems, Map<String, Boolean> columnDisable,
            Date date, ReportTask task) {
        String filePath = ReportExportUtil.getExportUrl(task, EXCEL);
        File file = new File(filePath);
        try {
            file.createNewFile();
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            createCmtsDeviceListReportExcelFile(deviceListItems, columnDisable, workbook, date);
        } catch (IOException e) {
            logger.debug("something wrong with I/O:", e);
        }
        return filePath;
    }

    @Override
    public List<CmcAttribute> getDeviceListItem(Map<String, Object> map) {
        Long cmtsType = entityTypeService.getCmtsType();
        map.put("type", cmtsType);
        return cmtsDeviceListReportDao.getDeviceListItem(map);
    }

    @Override
    public void exportCmtsDeviceListReportToExcel(List<CmcAttribute> deviceListItems,
            Map<String, Boolean> columnDisable, Date statDate) {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");

        String fileName;
        try {
            fileName = URLEncoder.encode(ReportTaskUtil.getString("report.cmtsDeviceListReport", "report") + "-"
                    + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            createCmtsDeviceListReportExcelFile(deviceListItems, columnDisable, workbook, statDate);
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

    private void createCmtsDeviceListReportExcelFile(List<CmcAttribute> deviceListItems,
            Map<String, Boolean> columnDisable, WritableWorkbook workbook, Date date) {
        Label label;
        WritableSheet sheet = workbook.createSheet("Sheet1", 0);
        try {
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();

            label = new Label(0, 2, "#", titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 2, ReportTaskUtil.getString("report.alias", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 2, "IP", titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 2, ReportTaskUtil.getString("report.deviceType", "report"), titleCellFormat);
            sheet.addCell(label);
            int titleCls = 4;
            if (columnDisable.get("locationDisplayable")) {
                label = new Label(titleCls++, 2, ReportTaskUtil.getString("label.location", "report"), titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("dutyDisplayable")) {
                label = new Label(titleCls++, 2, ReportTaskUtil.getString("report.contactor", "report"),
                        titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("createTimeDisplayable")) {
                label = new Label(titleCls++, 2, ReportTaskUtil.getString("report.createTime", "report"),
                        titleCellFormat);
                sheet.addCell(label);
            }
            sheet.setColumnView(0, 10);
            sheet.setColumnView(1, 50);
            for (int i = 2; i < titleCls; i++) {
                sheet.setColumnView(i, 25);
            }
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.cmtsDeviceListReport", "report"),
                    date, titleCls);

            Integer rowNum = 3;
            Integer rowNumView = 1;
            for (int i = 0; i < deviceListItems.size(); i++) {
                label = new Label(0, rowNum, rowNumView.toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, deviceListItems.get(i).getNmName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, deviceListItems.get(i).getIpAddress(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, entityTypeService.getEntityType(deviceListItems.get(i).getCmcDeviceStyle()).getDisplayName(), contentCellFormat);
                sheet.addCell(label);
                int cls = 4;
                if (columnDisable.get("locationDisplayable")) {
                    label = new Label(cls++, rowNum, deviceListItems.get(i).getTopCcmtsSysLocation(), contentCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("dutyDisplayable")) {
                    label = new Label(cls++, rowNum, deviceListItems.get(i).getTopCcmtsSysContact(), contentCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("createTimeDisplayable")) {
                    label = new Label(cls++, rowNum, deviceListItems.get(i).getCreateTimeString(), contentCellFormat);
                    sheet.addCell(label);
                }
                rowNum++;
                rowNumView++;
            }

            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("createCmtsDeviceListReportExcelFile method error:{}", e);
        }
    }
}
