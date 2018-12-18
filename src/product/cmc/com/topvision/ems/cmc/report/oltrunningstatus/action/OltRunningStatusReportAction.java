/***********************************************************************
 * $Id: OltRunningStatusReportAction.java,v1.0 2013-10-29 上午11:47:23 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.oltrunningstatus.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.report.domain.OltRunningStatus;
import com.topvision.ems.cmc.report.oltrunningstatus.service.OltRunningStatusReportCreator;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-29-上午11:47:23
 * 
 */
@Controller("oltRunningStatusReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltRunningStatusReportAction extends BaseAction {
    private static final long serialVersionUID = -6081770730131789062L;
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final DateFormat todayFormatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    @Autowired
    private OltRunningStatusReportCreator oltRunningStatusReportCreator;
    private Date statDate;
    private String stTime;
    private String etTime;

    /**
     * oltRunningStatus.jsp OLT运行状态统计
     * 
     * @return
     */
    public String queryOltRunningStatus() {
        statDate = new Date();
        Map<String, List<OltRunningStatus>> oltRunningReport = new HashMap<String, List<OltRunningStatus>>();
        if (stTime != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (stTime == null || etTime == null) {
                Date date = new Date();
                stTime = todayFormatter.format(date);
                etTime = formatter.format(date);
            }
            map.put("sql", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "d.collectTime"));
            map.put("userId", CurrentRequest.getCurrentUser().getUserId());
            oltRunningReport = oltRunningStatusReportCreator.statOltRunningStatusReport(map);
        }
        request.setAttribute("oltRunningReport", oltRunningReport);
        return SUCCESS;
    }

    /**
     * oltRunningStatus.jsp 导出OLT运行状态报表数据到excel
     * 
     * @return
     * @throws IOException
     */
    public String exportOltRunningStatusToExcel() throws IOException {
        statDate = new Date();
        Map<String, Object> parMap = new HashMap<String, Object>();
        OutputStream out = response.getOutputStream();
        response.setContentType("application/msexcel");
        String fileName = URLEncoder.encode(ReportTaskUtil.getString("report.oltRunningStatusReport", "report") + "-"
                + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        // TODO 时间逻辑处理(结束时间未选择则默认为当前时间，开始时间未选择则默认为不限)
        if (stTime == null || etTime == null) {
            Date date = new Date();
            stTime = todayFormatter.format(date);
            etTime = formatter.format(date);
        }
        parMap.put("stTime", stTime);
        parMap.put("etTime", etTime);
        parMap.put("userId", CurrentRequest.getCurrentUser().getUserId());
        parMap.put("sql", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "d.collectTime"));
        oltRunningStatusReportCreator.exportOltRunningStatusToExcel(parMap, statDate, out);
        return NONE;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public String getStTime() {
        return stTime;
    }

    public void setStTime(String stTime) {
        this.stTime = stTime;
    }

    public String getEtTime() {
        return etTime;
    }

    public void setEtTime(String etTime) {
        this.etTime = etTime;
    }

}
