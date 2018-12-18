/***********************************************************************
 * $Id: OnuDeviceListReportCreatorImpl.java,v1.0 2013-10-28 下午4:44:31 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.onudevice.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.report.onudevice.dao.OnuDeviceListReportDao;
import com.topvision.ems.epon.report.onudevice.service.OnuDeviceListReportCreator;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;

/**
 * @author haojie
 * @created @2013-10-28-下午4:44:31
 * 
 */
@Service("onuDeviceListReportCreator")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuDeviceListReportCreatorImpl extends BaseService implements OnuDeviceListReportCreator {
    @Autowired
    private OnuDeviceListReportDao onuDeviceListReportDao;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void bulidReport(ReportTask task) {
        Date date = new Date();
        Map<String, Object> condition = task.getCondition();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sortName", condition.get("onuSortName"));
        List<OltOnuAttribute> deviceListItems = getOnuDeviceListItem(map);

        for (Iterator<OltOnuAttribute> iterator = deviceListItems.iterator(); iterator.hasNext();) {
            OltOnuAttribute oltOnuAttr = iterator.next();
            if (oltOnuAttr.getOnuAdminStatus() == 1) {
                oltOnuAttr.setOnuAdminStatusString("UP");
            } else if (oltOnuAttr.getOnuAdminStatus() == 2) {
                oltOnuAttr.setOnuAdminStatusString("DOWN");
            }
            if (oltOnuAttr.getOnuOperationStatus() == 1) {
                oltOnuAttr.setOnuOperationStatusString("UP");
            } else if (oltOnuAttr.getOnuOperationStatus() == 2) {
                oltOnuAttr.setOnuOperationStatusString("DOWN");
            }
            String s = Long.toHexString(oltOnuAttr.getOnuIndex());
            String lid = Integer.valueOf(s.substring(3, 5), 16).toString();
            String position = oltOnuAttr.getEntityIp() + ":" + oltOnuAttr.getPonName() + ":" + lid;
            oltOnuAttr.setPosition(position);
            switch (oltOnuAttr.getOnuPreType()) {
            // modify by loyal 改为从数据库中取
            /*case 33:
                oltOnuAttr.setOnuTypeString("TA-PN8621");
                break;
            case 34:
                oltOnuAttr.setOnuTypeString("TA-PN8622");
                break;
            case 36:
                oltOnuAttr.setOnuTypeString("TA-PN8624");
                break;
            case 37:
                oltOnuAttr.setOnuTypeString("TA-PN8625");
                break;
            case 65:
                oltOnuAttr.setOnuTypeString("TA-PN8641");
                break;
            case 68:
                oltOnuAttr.setOnuTypeString("TA-PN8643");
                break;
            case 71:
                oltOnuAttr.setOnuTypeString("TA-PN8645");
                break;
            case 81:
                oltOnuAttr.setOnuTypeString("TA-PN8651");
                break;
            case 82:
                oltOnuAttr.setOnuTypeString("TA-PN8652");
                break;
            case 83:
                oltOnuAttr.setOnuTypeString("TA-PN8653");
                break;
            case 84:
                oltOnuAttr.setOnuTypeString("TA-PN8654");
                break;*/
            case 241:
                iterator.remove();
                break;
            case 242:
                iterator.remove();
                break;
            case 243:
                iterator.remove();
                break;
            /*default:
                oltOnuAttr.setOnuTypeString(ReportTaskUtil.getString("ONU.unkownType", "epon"));
                break;*/
            }
        }

        Map<String, Boolean> columnDisable = new HashMap<String, Boolean>();
        columnDisable.put("operationStatusDisplayable",
                Boolean.valueOf(condition.get("operationStatusDisplayable").toString()));
        columnDisable
                .put("adminStatusDisplayable", Boolean.valueOf(condition.get("adminStatusDisplayable").toString()));

        try {
            if (task.isExcelEnabled()) {
                String filePath = exportAsExcelFromTask(task, deviceListItems, columnDisable, date);
                reportInstanceService.addReportInstance(filePath, EXCEL, task);
            }
        } catch (Exception e) {
            logger.error("bulidReport method is error:{}", e);
        }
    }

    /**
     * 任务报表导出为 Excel
     * 
     * @param task
     * @param deviceListItems
     * @param columnDisable
     * @param date
     * @return
     */
    private String exportAsExcelFromTask(ReportTask task, List<OltOnuAttribute> deviceListItems,
            Map<String, Boolean> columnDisable, Date date) {
        String filePath = ReportExportUtil.getExportUrl(task, EXCEL);
        File file = new File(filePath);
        try {
            file.createNewFile();
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            createOnuDeviceListReportExcelFile(deviceListItems, columnDisable, workbook, date);
        } catch (IOException e) {
            logger.error("exportAsExcelFromTask error:{}", e);
        }
        return filePath;
    }

    @Override
    public List<OltOnuAttribute> getOnuDeviceListItem(Map<String, Object> map) {
        Long onuType = entityTypeService.getOnuType();
        map.put("type", onuType);
        return onuDeviceListReportDao.getOnuDeviceListItem(map);
    }

    @Override
    public void exportOnuDeviceListReportToExcel(List<OltOnuAttribute> deviceListItems,
            Map<String, Boolean> columnDisable, Date statDate) {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");

        String fileName;
        try {
            fileName = URLEncoder.encode(ReportTaskUtil.getString("report.onuDeviceList", "report") + "-"
                    + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            createOnuDeviceListReportExcelFile(deviceListItems, columnDisable, workbook, statDate);
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

    /**
     * 为excel文件填充内容
     * 
     * @param oltOnuAttributes
     * @param workbook
     */
    private void createOnuDeviceListReportExcelFile(List<OltOnuAttribute> oltOnuAttributes,
            Map<String, Boolean> columnDisable, WritableWorkbook workbook, Date date) {
        try {
            Label label;
            // 创建表头等信息
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);

            int titleCls = 5;
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();

            label = new Label(0, 2, "#", titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 2, ReportTaskUtil.getString("label.name", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 2, ReportTaskUtil.getString("report.onuLocation", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 2, ReportTaskUtil.getString("label.mac", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 2, ReportTaskUtil.getString("label.type", "report"), titleCellFormat);
            sheet.addCell(label);
            if (columnDisable.get("operationStatusDisplayable")) {
                label = new Label(titleCls++, 2, ReportTaskUtil.getString("label.operationStatus", "report"),
                        titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("adminStatusDisplayable")) {
                label = new Label(titleCls++, 2, ReportTaskUtil.getString("label.adminStatus", "report"),
                        titleCellFormat);
                sheet.addCell(label);
            }

            sheet.setColumnView(0, 10);
            for (int i = 1; i < titleCls; i++) {
                sheet.setColumnView(i, 25);
            }
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.onuDeviceList", "report"), date,
                    titleCls);

            Integer rowNum = 3;
            Integer rowNumView = 1;
            for (int i = 0; i < oltOnuAttributes.size(); i++) {
                label = new Label(0, rowNum, rowNumView.toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, oltOnuAttributes.get(i).getOnuName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, oltOnuAttributes.get(i).getPosition(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, oltOnuAttributes.get(i).getOnuMac(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(4, rowNum, oltOnuAttributes.get(i).getOnuTypeString(), contentCellFormat);
                sheet.addCell(label);
                int cls = 5;
                if (columnDisable.get("operationStatusDisplayable")) {
                    label = new Label(cls++, rowNum, oltOnuAttributes.get(i).getOnuOperationStatusString(),
                            contentCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("adminStatusDisplayable")) {
                    label = new Label(cls++, rowNum, oltOnuAttributes.get(i).getOnuAdminStatusString(),
                            contentCellFormat);
                    sheet.addCell(label);
                }
                rowNum++;
                rowNumView++;
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("createOnuDeviceListReportExcelFile method error:{}", e);
        }
    }

}
