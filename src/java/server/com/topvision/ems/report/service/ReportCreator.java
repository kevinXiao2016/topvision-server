package com.topvision.ems.report.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.topvision.ems.report.domain.ReportTask;
import com.topvision.framework.service.Service;

/**
 * 
 * @author Bravin
 * @created @2013-6-17-上午9:53:23
 * 
 */
public interface ReportCreator extends Service {

    public static final int PDF_MARGIN_LEFT = 50;
    public static final int PDF_MARGIN_RIGHT = 50;
    public static final int PDF_MARGIN_TOP = 50;
    public static final int PDF_MARGIN_BOTTOM = 50;

    public static final int EXCEL = 0;
    public static final int PDF = 1;
    public static final int HTML = 2;

    public static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 构建任务报表
     * 
     * @param reportTask
     */
    void bulidReport(ReportTask reportTask);

}
