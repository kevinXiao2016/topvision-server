/***********************************************************************
 * $Id: OltCpuReportCreatorImpl.java,v1.0 2013-10-26 上午11:36:30 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltcpu.service.impl;

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
import com.topvision.ems.epon.report.oltcpu.dao.OltCpuReportDao;
import com.topvision.ems.epon.report.oltcpu.service.OltCpuReportCreator;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.util.NumberFormatterUtil;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;

/**
 * @author haojie
 * @created @2013-10-26-上午11:36:30
 * 
 */
@Service("oltCpuReportCreator")
public class OltCpuReportCreatorImpl extends BaseService implements OltCpuReportCreator {
    private static final Logger logger = LoggerFactory.getLogger(OltCpuReportCreatorImpl.class);
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private OltCpuReportDao OltCpuReportDao;
    @Autowired
    private EntityTypeService entityTypeService;
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<PerforamanceRank> getEponCpuRank() {
        Long oltType = entityTypeService.getOltType();
        return OltCpuReportDao.getEponCpuRank(oltType);
    }

    @Override
    public void exportAsExcel(Date statDate, OutputStream out) {
        List<PerforamanceRank> performanceRank = getEponCpuRank();
        for (PerforamanceRank rank : performanceRank) {
            if (rank.getCpu() == null) {
                rank.setCPU_p("0%");
            } else if (rank.getCpu() > 0) {
                rank.setCPU_p(NumberFormatterUtil.formatDecimalTwo(new Double(rank.getCpu() * 100)) + "%");
            } else {
                rank.setCPU_p("0%");
            }
        }
        exportAsExcel(null, performanceRank, out, statDate);

    }

    @Override
    public void bulidReport(ReportTask task) {
        Date statDate = new Date();
        List<PerforamanceRank> performanceRank = getEponCpuRank();
        for (PerforamanceRank rank : performanceRank) {
            if (rank.getCpu() == null) {
                rank.setCPU_p("0%");
            } else if (rank.getCpu() > 0) {
                rank.setCPU_p(NumberFormatterUtil.formatDecimalTwo(new Double(rank.getCpu() * 100)) + "%");
            } else {
                rank.setCPU_p("0%");
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
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.oltcpurate", "report"), statDate,
                    titleCell);
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            label = new Label(0, 2, ReportTaskUtil.getString("report.deviceIP", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 2, ReportTaskUtil.getString("report.alias", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 2, ReportTaskUtil.getString("report.cpuUsage", "report"), titleCellFormat);
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
                label = new Label(2, rowNum, performanceRank1.getCPU_p(), contentCellFormat);
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
