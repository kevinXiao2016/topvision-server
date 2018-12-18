/***********************************************************************
 * $Id: OltMemReportCreatorImpl.java,v1.0 2013-10-26 下午5:11:04 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltmem.service.impl;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.domain.PerforamanceRank;
import com.topvision.ems.epon.report.oltmem.dao.OltMemReportDao;
import com.topvision.ems.epon.report.oltmem.service.OltMemReportCreator;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.util.NumberFormatterUtil;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;

/**
 * @author haojie
 * @created @2013-10-26-下午5:11:04
 * 
 */
@Service("oltMemReportCreator")
public class OltMemReportCreatorImpl extends BaseService implements OltMemReportCreator {
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private OltMemReportDao oltMemReportDao;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void bulidReport(ReportTask task) {
        Date statDate = new Date();
        List<PerforamanceRank> performanceRank = getEponMemRank();
        for (PerforamanceRank rank : performanceRank) {
            if (rank.getMem() == null) {
                rank.setMEM_p("0%");
            } else if (rank.getMem() > 0) {
                rank.setMEM_p(NumberFormatterUtil.formatDecimalTwo(new Double(rank.getMem() * 100)) + "%");
            } else {
                rank.setMEM_p("0%");
            }
        }
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
    public List<PerforamanceRank> getEponMemRank() {
        Long oltType = entityTypeService.getOltType();
        return oltMemReportDao.getEponMemRank(oltType);
    }

    @Override
    public void exportAsExcel(Date statDate, OutputStream out) {
        List<PerforamanceRank> performanceRank = getEponMemRank();
        for (PerforamanceRank rank : performanceRank) {
            if (rank.getMem() == null) {
                rank.setMEM_p("0%");
            } else if (rank.getMem() > 0) {
                rank.setMEM_p(NumberFormatterUtil.formatDecimalTwo(new Double(rank.getMem() * 100)) + "%");
            } else {
                rank.setMEM_p("0%");
            }
        }
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
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.oltmemrate", "report"), statDate,
                    titleCell);
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            label = new Label(0, 2, ReportTaskUtil.getString("report.deviceIP", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 2, ReportTaskUtil.getString("report.alias", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 2, ReportTaskUtil.getString("report.memUsage", "report"), titleCellFormat);
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
                label = new Label(2, rowNum, performanceRank1.getMEM_p(), contentCellFormat);
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
