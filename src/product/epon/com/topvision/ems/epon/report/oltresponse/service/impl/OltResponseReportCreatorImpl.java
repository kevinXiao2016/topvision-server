/***********************************************************************
 * $Id: OltResponseReportCreatorImpl.java,v1.0 2013-10-28 上午11:40:26 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltresponse.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.domain.PerforamanceRank;
import com.topvision.ems.epon.report.oltresponse.dao.OltResponseReportDao;
import com.topvision.ems.epon.report.oltresponse.service.OltResponseReportCreator;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;

/**
 * @author haojie
 * @created @2013-10-28-上午11:40:26
 * 
 */
@Service("oltResponseReportCreator")
public class OltResponseReportCreatorImpl extends BaseService implements OltResponseReportCreator {
    private static final Logger logger = LoggerFactory.getLogger(OltResponseReportCreatorImpl.class);
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private OltResponseReportDao oltResponseReportDao;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void bulidReport(ReportTask task) {
        Date statDate = new Date();
        List<PerforamanceRank> performanceRank = getEponDelayRank();
        if (task.isExcelEnabled()) {
            try {
                String filePath = ReportExportUtil.getExportUrl(task, EXCEL);
                OutputStream out = new FileOutputStream(filePath);
                exportAsExcel(task, performanceRank, out, statDate);
                reportInstanceService.addReportInstance(filePath, EXCEL, task);
            } catch (FileNotFoundException e) {
                logger.error("bulidReport method is error:{}", e);
            }
        }
    }

    @Override
    public List<PerforamanceRank> getEponDelayRank() {
        Long oltType = entityTypeService.getOltType();
        return oltResponseReportDao.getEponDelayRank(oltType);
    }

    @Override
    public void exportAsExcel(Date statDate, OutputStream out) {
        List<PerforamanceRank> performanceRank = getEponDelayRank();
        exportAsExcel(null, performanceRank, out, statDate);
    }

    /**
     * 导出为Excel
     * 
     * @param task
     * @param performanceRank
     * @param out
     * @param statDate
     */
    private void exportAsExcel(ReportTask task, List<PerforamanceRank> performanceRank, OutputStream out, Date statDate) {
        try {
            Label label;
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);
            sheet.setColumnView(0, 30);
            sheet.setColumnView(1, 30);
            sheet.setColumnView(2, 20);
            sheet.setColumnView(3, 40);
            int titleCell = 4;
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.oltresponse", "report"), statDate,
                    titleCell);
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            label = new Label(0, 2, ReportTaskUtil.getString("report.deviceIP", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 2, ReportTaskUtil.getString("report.alias", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 2, ReportTaskUtil.getString("report.entityResponseDelay", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 2, ReportTaskUtil.getString("report.collectTime", "report"), titleCellFormat);
            sheet.addCell(label);
            int rowNum = 3;

            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            for (PerforamanceRank performanceRank1 : performanceRank) {
                label = new Label(0, rowNum, performanceRank1.getIp(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, performanceRank1.getName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, performanceRank1.getDelay() + "ms", contentCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, formatter.format(performanceRank1.getSnapTime()), contentCellFormat);
                sheet.addCell(label);
                rowNum++;
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

    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }

}
