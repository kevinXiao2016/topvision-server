/***********************************************************************
 * $Id: OltSniPortReportCreatorImpl.java,v1.0 2013-10-28 下午2:01:53 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltsniport.service.impl;

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

import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.report.oltsniport.dao.OltSniPortReportDao;
import com.topvision.ems.epon.report.oltsniport.service.OltSniPortReportCreator;
import com.topvision.framework.utils.EponIndex;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.service.BaseService;

/**
 * @author haojie
 * @created @2013-10-28-下午2:01:53
 * 
 */
@Service("oltSniPortReportCreator")
public class OltSniPortReportCreatorImpl extends BaseService implements OltSniPortReportCreator {
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private OltSniPortReportDao oltSniPortReportDao;

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
        List<OltSniAttribute> snilist = getSniPortList(queryMap);
        for (OltSniAttribute aSnilist : snilist) {
            if (aSnilist.getSniAdminStatus() == 1) {
                aSnilist.setSniAdminStatusString("UP");
            } else if (aSnilist.getSniAdminStatus() == 2) {
                aSnilist.setSniAdminStatusString("DOWN");
            }

            if (aSnilist.getSniOperationStatus() == 1) {
                aSnilist.setSniOperationStatusString("UP");
            } else if (aSnilist.getSniOperationStatus() == 2) {
                aSnilist.setSniOperationStatusString("DOWN");
            }

            switch (aSnilist.getSniMediaType()) {
            case 1:
                aSnilist.setSniMediaTypeString("twistedPair");
                break;
            case 2:
                aSnilist.setSniMediaTypeString("fiber");
                break;
            default:
                break;
            }
            aSnilist.setSniPort(EponIndex.getSlotNo(aSnilist.getSniIndex()) + "-"
                    + EponIndex.getSniNo(aSnilist.getSniIndex()));
        }

        try {
            if (task.isExcelEnabled()) {
                String filePath = exportAsExcelFromTask(task, snilist, date);
                reportInstanceService.addReportInstance(filePath, EXCEL, task);
            }
        } catch (Exception e) {
            logger.debug("bulidReport method error:{}", e);
        }
    }

    @Override
    public List<OltSniAttribute> getSniPortList(Map<String, Object> map) {
        return oltSniPortReportDao.getSniPortList(map);
    }

    /**
     * 任务报表导出为 Excel
     * 
     * @param task
     * @param oltSniAttributes
     * @return
     * @throws UnsupportedEncodingException
     */
    private String exportAsExcelFromTask(ReportTask task, List<OltSniAttribute> oltSniAttributes, Date date)
            throws UnsupportedEncodingException {
        String filePath = ReportExportUtil.getExportUrl(task, EXCEL);
        File file = new File(filePath);
        try {
            file.createNewFile();
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            createOltSniPortReportExcelFile(oltSniAttributes, workbook, date);
        } catch (IOException e) {
            logger.debug("exportAsExcelFromTask method error:{}", e);
        }
        return filePath;
    }

    @Override
    public void exportOltSniPortReportToExcel(List<OltSniAttribute> oltSniAttributes, Date statDate) {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");

        String fileName;
        try {
            fileName = URLEncoder.encode(ReportTaskUtil.getString("report.oltSniPortReport", "report") + "-"
                    + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            createOltSniPortReportExcelFile(oltSniAttributes, workbook, statDate);
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
     * @param eponBoardStatistics
     * @param columnDisable
     * @param workbook
     */
    private void createOltSniPortReportExcelFile(List<OltSniAttribute> oltSniAttributes, WritableWorkbook workbook,
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
            label = new Label(3, 2, ReportTaskUtil.getString("label.portDescr", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 2, ReportTaskUtil.getString("label.portType", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(5, 2, ReportTaskUtil.getString("label.adminStatus", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(6, 2, ReportTaskUtil.getString("label.operStatus", "report"), titleCellFormat);
            sheet.addCell(label);

            // 第1列为设备IP，偏长，需要设置宽度
            sheet.setColumnView(0, 25);
            for (int i = 1; i < 7; i++) {
                sheet.setColumnView(i, 15);
            }

            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.oltSniPortReport", "report"),
                    date, 7);

            Integer rowNum = 3;
            for (int i = 0; i < oltSniAttributes.size(); i++) {
                label = new Label(0, rowNum, oltSniAttributes.get(i).getEntityIp(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, oltSniAttributes.get(i).getEntityName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, oltSniAttributes.get(i).getSniPort(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, oltSniAttributes.get(i).getSniPortName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(4, rowNum, oltSniAttributes.get(i).getSniMediaTypeString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(5, rowNum, oltSniAttributes.get(i).getSniAdminStatusString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(6, rowNum, oltSniAttributes.get(i).getSniOperationStatusString(), contentCellFormat);
                sheet.addCell(label);
                rowNum++;
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("createOltSniPortReportExcelFile method error:{}", e);
        }
    }

}
