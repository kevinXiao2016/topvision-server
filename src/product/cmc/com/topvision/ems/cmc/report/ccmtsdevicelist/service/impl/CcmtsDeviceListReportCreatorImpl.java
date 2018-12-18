/***********************************************************************
 * $Id: CcmtsDeviceListReportCreatorImpl.java,v1.0 2013-10-29 上午9:24:20 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtsdevicelist.service.impl;

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
import com.topvision.ems.cmc.report.ccmtsdevicelist.dao.CcmtsDeviceListReportDao;
import com.topvision.ems.cmc.report.ccmtsdevicelist.service.CcmtsDeviceListReportCreator;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.service.UserPreferencesService;

/**
 * @author haojie
 * @created @2013-10-29-上午9:24:20
 * 
 */
@Service("ccmtsDeviceListReportCreator")
public class CcmtsDeviceListReportCreatorImpl extends BaseService implements CcmtsDeviceListReportCreator {
    @Autowired
    private CcmtsDeviceListReportDao ccmtsDeviceListReportDao;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private UserPreferencesService userPreferencesService;
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
            createCcmtsDeviceListReportExcelFile(deviceListItems, columnDisable, workbook, date);
        } catch (IOException e) {
            logger.debug("something wrong with I/O:", e);
        }
        return filePath;
    }

    @Override
    public List<CmcAttribute> getDeviceListItem(Map<String, Object> map) {
        return ccmtsDeviceListReportDao.getDeviceListItem(map);
    }

    @Override
    public void exportCcmtsDeviceListReportToExcel(List<CmcAttribute> deviceListItems,
            Map<String, Boolean> columnDisable, Date statDate) {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");

        String fileName;
        try {
            fileName = URLEncoder.encode(ReportTaskUtil.getString("report.ccDeviceList", "report") + "-"
                    + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            createCcmtsDeviceListReportExcelFile(deviceListItems, columnDisable, workbook, statDate);
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

    private void createCcmtsDeviceListReportExcelFile(List<CmcAttribute> deviceListItems,
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
            label = new Label(2, 2, ReportTaskUtil.getString("report.folderName", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 2, "IP", titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 2, ReportTaskUtil.getString("label.mac", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(5, 2, ReportTaskUtil.getString("report.deviceType", "report"), titleCellFormat);
            sheet.addCell(label);
            int titleCls = 6;
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
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.ccDeviceList", "report"), date,
                    titleCls);

            Integer rowNum = 3;
            Integer rowNumView = 1;
            for (int i = 0; i < deviceListItems.size(); i++) {
                label = new Label(0, rowNum, rowNumView.toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, deviceListItems.get(i).getNmName(), contentCellFormat);
                sheet.addCell(label);
                if (deviceListItems.get(i).getFolderName() != null) {
                    label = new Label(2, rowNum, ReportTaskUtil.getString(deviceListItems.get(i).getFolderName(),
                            "resources"), contentCellFormat);
                } else {
                    label = new Label(2, rowNum, "-", contentCellFormat);
                }
                sheet.addCell(label);
                label = new Label(3, rowNum, deviceListItems.get(i).getIpAddress(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(4, rowNum, deviceListItems.get(i).getTopCcmtsSysMacAddr(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(5, rowNum, deviceListItems.get(i).getCmcDeviceStyleString(), contentCellFormat);
                sheet.addCell(label);
                int cls = 6;
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
            logger.debug("createCcmtsDeviceListReportExcelFile method error:{}", e);
        }
    }

}
