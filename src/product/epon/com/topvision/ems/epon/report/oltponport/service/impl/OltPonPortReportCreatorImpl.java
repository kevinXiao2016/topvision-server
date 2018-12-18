/***********************************************************************
 * $Id: OltPonPortReportCreator.java,v1.0 2013-10-28 上午9:58:48 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltponport.service.impl;

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

import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.report.oltponport.dao.OltPonPortReportDao;
import com.topvision.ems.epon.report.oltponport.service.OltPonPortReportCreator;
import com.topvision.framework.utils.EponIndex;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.service.BaseService;

/**
 * @author haojie
 * @created @2013-10-28-上午9:58:48
 * 
 */
@Service("oltPonPortReportCreator")
public class OltPonPortReportCreatorImpl extends BaseService implements OltPonPortReportCreator {
    @Autowired
    private OltPonPortReportDao oltPonPortReportDao;
    @Autowired
    private ReportInstanceService reportInstanceService;
    private Integer entityType;
    private String entityIp;
    private Integer adminState;
    private Integer operationState;

    @Override
    public void bulidReport(ReportTask task) {
        Date date = new Date();
        Map<String, Object> condition = task.getCondition();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (condition.get("entityType") != null && Integer.valueOf((String) condition.get("entityType")) != 0) {
            queryMap.put("entityType", Integer.valueOf((String)condition.get("entityType")));
        }
        if (condition.get("entityIp") != null && !condition.get("entityIp").equals("")) {
            queryMap.put("entityIp", condition.get("entityIp"));
        }
        if (condition.get("adminState") != null && Integer.valueOf((String) condition.get("adminState")) != 0) {
            queryMap.put("adminState", Integer.valueOf((String) condition.get("adminState")));
        }
        if (condition.get("operationState") != null && Integer.valueOf((String) condition.get("operationState")) != 0) {
            queryMap.put("operationState", Integer.valueOf((String) condition.get("operationState")));
        }
        List<OltPonAttribute> ponlist = getPonPortList(queryMap);
        for (OltPonAttribute aPonlist : ponlist) {
            if (aPonlist.getPonPortAdminStatus() == 1) {
                aPonlist.setPonPortAdminStatusString("UP");
            } else if (aPonlist.getPonPortAdminStatus() == 2) {
                aPonlist.setPonPortAdminStatusString("DOWN");
            }
            if (aPonlist.getPonOperationStatus() == 1) {
                aPonlist.setPonOperationStatusString("UP");
            } else if (aPonlist.getPonOperationStatus() == 2) {
                aPonlist.setPonOperationStatusString("DOWN");
            }
            switch (aPonlist.getPonPortType()) {
            case 1:
                aPonlist.setPonPortTypeString("ge-epon");
                break;
            case 2:
                aPonlist.setPonPortTypeString("tenge-epon");
                break;
            case 3:
                aPonlist.setPonPortTypeString("gpon");
                break;
            default:
                break;
            }
            aPonlist.setPonPort(EponIndex.getSlotNo(aPonlist.getPonIndex()) + "-"
                    + EponIndex.getPonNo(aPonlist.getPonIndex()));
            aPonlist.setPonPortName("PORT-" + EponIndex.getSlotNo(aPonlist.getPonIndex()).toString() + "-"
                    + EponIndex.getPonNo(aPonlist.getPonIndex()).toString());
        }

        try {
            if (task.isExcelEnabled()) {
                String filePath = exportAsExcelFromTask(task, ponlist, date);
                reportInstanceService.addReportInstance(filePath, EXCEL, task);
            }
        } catch (Exception e) {
            logger.debug("bulidReport method error:{}", e);
        }
    }

    @Override
    public List<OltPonAttribute> getPonPortList(Map<String, Object> map) {
        return oltPonPortReportDao.getPonPortList(map);
    }

    @Override
    public void exportOltPonPortReportToExcel(List<OltPonAttribute> oltPonAttributes, Date statDate) {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");

        String fileName;
        try {
            fileName = URLEncoder.encode(ReportTaskUtil.getString("report.oltPonPortReport", "report") + "-"
                    + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            createOltPonPortReportExcelFile(oltPonAttributes, workbook, statDate);
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
     * @param oltPonAttributes
     * @param workbook
     */
    private void createOltPonPortReportExcelFile(List<OltPonAttribute> oltPonAttributes, WritableWorkbook workbook,
            Date date) {
        try {
            Label label;
            // 创建表头等信息
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);

            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();

            label = new Label(0, 2, ReportTaskUtil.getString("report.device", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 2, ReportTaskUtil.getString("report.deviceName", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 2, ReportTaskUtil.getString("label.portIndex", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 2, ReportTaskUtil.getString("label.portType", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 2, ReportTaskUtil.getString("label.adminStatus", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(5, 2, ReportTaskUtil.getString("label.operStatus", "report"), titleCellFormat);
            sheet.addCell(label);

            // 第1列为设备IP，偏长，需要设置宽度
            sheet.setColumnView(0, 25);
            for (int i = 1; i < 6; i++) {
                sheet.setColumnView(i, 15);
            }

            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.oltPonPortReport", "report"),
                    date, 6);

            Integer rowNum = 3;
            for (int i = 0; i < oltPonAttributes.size(); i++) {
                label = new Label(0, rowNum, oltPonAttributes.get(i).getEntityIp(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, oltPonAttributes.get(i).getEntityName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, oltPonAttributes.get(i).getPonPort(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, oltPonAttributes.get(i).getPonPortTypeString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(4, rowNum, oltPonAttributes.get(i).getPonPortAdminStatusString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(5, rowNum, oltPonAttributes.get(i).getPonOperationStatusString(), contentCellFormat);
                sheet.addCell(label);
                rowNum++;
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("createOltPonPortReportExcelFile method is error:{}", e);
        }
    }

    /**
     * 任务报表导出为 Excel
     * 
     * @param task
     * @return
     * @throws UnsupportedEncodingException
     */
    private String exportAsExcelFromTask(ReportTask task, List<OltPonAttribute> oltPonAttributes, Date date)
            throws UnsupportedEncodingException {
        String filePath = ReportExportUtil.getExportUrl(task, EXCEL);
        File file = new File(filePath);
        try {
            file.createNewFile();
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            createOltPonPortReportExcelFile(oltPonAttributes, workbook, date);
        } catch (IOException e) {
            logger.debug("exportAsExcelFromTask method is error:{}", e);
        }
        return filePath;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public Integer getOperationState() {
        return operationState;
    }

    public void setOperationState(Integer operationState) {
        this.operationState = operationState;
    }

    public Integer getAdminState() {
        return adminState;
    }

    public void setAdminState(Integer adminState) {
        this.adminState = adminState;
    }

}
