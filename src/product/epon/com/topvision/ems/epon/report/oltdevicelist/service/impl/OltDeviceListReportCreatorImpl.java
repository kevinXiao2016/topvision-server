/***********************************************************************
 * $Id: OltDeviceListReportCreatorImpl.java,v1.0 2013-10-26 下午2:52:27 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltdevicelist.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.domain.DeviceListItem;
import com.topvision.ems.epon.report.oltdevicelist.dao.OltDeviceListReportDao;
import com.topvision.ems.epon.report.oltdevicelist.service.OltDeviceListReportCreator;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;

/**
 * @author haojie
 * @created @2013-10-26-下午2:52:27
 * 
 */
@Service("oltDeviceListReportCreator")
public class OltDeviceListReportCreatorImpl extends BaseService implements OltDeviceListReportCreator {
    private static final Logger logger = LoggerFactory.getLogger(OltDeviceListReportCreatorImpl.class);
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private OltDeviceListReportDao oltDeviceListReportDao;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void bulidReport(ReportTask task) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> condition = task.getCondition();
        map.put("sortName", condition.get("sortInfo"));
        Date statDate = new Date();
        List<DeviceListItem> deviceListItem = getDeviceListItem(map);
        if (task.isExcelEnabled()) {
            try {
                String filePath = ReportExportUtil.getExportUrl(task, EXCEL);
                OutputStream out = new FileOutputStream(filePath);
                Map<String, Object> displayMap = new HashMap<String, Object>();
                displayMap.put("locationDisplayable", task.containsColumn("sysLocation"));
                displayMap.put("createTimeDisplayable", task.containsColumn("createTime"));
                exportAsExcelFromTask(task, deviceListItem, out, statDate, displayMap);
                reportInstanceService.addReportInstance(filePath, EXCEL, task);
            } catch (FileNotFoundException e) {
                logger.error("bulidReport method is error:{}", e);
            }
        }
    }

    @Override
    public void exportAsExcelFromRequest(Map<String, Object> queryMap, Date statDate, OutputStream out) {
        List<DeviceListItem> deviceListItems = getDeviceListItem(queryMap);
        String sortInfo = (String) queryMap.get("sortName");
        ReportTask task = new ReportTask();
        task.addCondition("sortInfo", sortInfo);
        exportAsExcelFromTask(task, deviceListItems, out, statDate, queryMap);
    }

    /**
     * 任务报表导出为 Excel
     * 
     * @param task
     * @param deviceListItems
     * @param out
     * @throws UnsupportedEncodingException
     */
    private void exportAsExcelFromTask(ReportTask task, List<DeviceListItem> deviceListItems, OutputStream out,
            Date statDate, Map<String, Object> displayMap) {
        try {
            Label label;
            // 创建表头等信息
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);
            sheet.setColumnView(0, 10);
            sheet.setColumnView(1, 20);
            sheet.setColumnView(2, 30);
            sheet.setColumnView(3, 20);
            int titleCell = 4;
            if ((Boolean) displayMap.get("locationDisplayable")) {
                sheet.setColumnView(titleCell, 20);
                titleCell++;
            }
            if ((Boolean) displayMap.get("createTimeDisplayable")) {
                sheet.setColumnView(titleCell, 30);
                titleCell++;
            }
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.oltDeviceList", "report") + "",
                    statDate, titleCell);
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            label = new Label(0, 2, "#", titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 2, ReportTaskUtil.getString("report.alias", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 2, ReportTaskUtil.getString("label.ip", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 2, ReportTaskUtil.getString("label.type", "report"), titleCellFormat);
            sheet.addCell(label);
            int titleCls = 4;
            if ((Boolean) displayMap.get("locationDisplayable")) {
                label = new Label(titleCls++, 2, ReportTaskUtil.getString("label.location", "report"), titleCellFormat);
                sheet.addCell(label);
            }
            if ((Boolean) displayMap.get("createTimeDisplayable")) {
                label = new Label(titleCls++, 2, ReportTaskUtil.getString("report.createTime", "report"),
                        titleCellFormat);
                sheet.addCell(label);
            }

            Integer rowNum = 3;
            Integer rowNumView = 1;
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            for (int i = 0; i < deviceListItems.size(); i++) {
                label = new Label(0, rowNum, rowNumView.toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, deviceListItems.get(i).getName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, deviceListItems.get(i).getIp(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, deviceListItems.get(i).getTypeName(), contentCellFormat);
                sheet.addCell(label);
                int cls = 4;
                if ((Boolean) displayMap.get("locationDisplayable")) {
                    label = new Label(cls++, rowNum, deviceListItems.get(i).getSysLocation(), contentCellFormat);
                    sheet.addCell(label);
                }
                if ((Boolean) displayMap.get("createTimeDisplayable")) {
                    label = new Label(cls++, rowNum, deviceListItems.get(i).getCreateTimeString(), contentCellFormat);
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

    @Override
    public List<DeviceListItem> getDeviceListItem(Map<String, Object> map) {
        Long oltType = entityTypeService.getOltType();
        map.put("type", oltType);
        return oltDeviceListReportDao.getDeviceListItem(map);
    }

}
