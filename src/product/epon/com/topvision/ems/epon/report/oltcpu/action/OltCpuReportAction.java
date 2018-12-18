/***********************************************************************
 * $Id: OltCpuReportAction.java,v1.0 2013-10-26 上午11:34:59 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltcpu.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.domain.PerforamanceRank;
import com.topvision.ems.epon.report.oltcpu.service.OltCpuReportCreator;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author haojie
 * @created @2013-10-26-上午11:34:59
 * 
 */
@Controller("oltCpuReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltCpuReportAction extends BaseAction {
    private static final long serialVersionUID = 6836898279172055832L;
    private static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    protected Date statDate;
    private List<PerforamanceRank> performanceRank;
    @Autowired
    private OltCpuReportCreator oltCpuReportCreator;
    private boolean ipDisplayable = true;
    private boolean nameDisplayable = true;
    private boolean cpuDisplayable = true;
    private boolean snapTimeDisplayable = true;

    /**
     * 查看CPU使用率TOP 10
     * 
     * @return String
     */
    public String showEponCpuRankReport() {
        DecimalFormat df = new DecimalFormat("#.00");
        statDate = new Date();
        performanceRank = oltCpuReportCreator.getEponCpuRank();
        for (PerforamanceRank rank : performanceRank) {
            if (rank.getCpu() == null) {
                rank.setCPU_p("0%");
            } else if (rank.getCpu() > 0) {
                rank.setCPU_p(df.format(rank.getCpu() * 100) + "%");
            } else {
                rank.setCPU_p("0%");
            }
        }
        return SUCCESS;
    }

    /**
     * eponCpuRankReport.jsp 导出CPU使用率排行报表到excel
     * 
     * @return
     * @throws IOException
     */
    public String exportCpuRankReportToExcel() throws IOException {
        statDate = new Date();
        OutputStream out = response.getOutputStream();
        response.setContentType("application/msexcel");
        String fileName = URLEncoder.encode(ReportTaskUtil.getString("report.cpuUsageOrder", "report") + "-"
                + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        oltCpuReportCreator.exportAsExcel(statDate, out);
        return NONE;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public List<PerforamanceRank> getPerformanceRank() {
        return performanceRank;
    }

    public void setPerformanceRank(List<PerforamanceRank> performanceRank) {
        this.performanceRank = performanceRank;
    }

    public boolean isIpDisplayable() {
        return ipDisplayable;
    }

    public void setIpDisplayable(boolean ipDisplayable) {
        this.ipDisplayable = ipDisplayable;
    }

    public boolean isNameDisplayable() {
        return nameDisplayable;
    }

    public void setNameDisplayable(boolean nameDisplayable) {
        this.nameDisplayable = nameDisplayable;
    }

    public boolean isCpuDisplayable() {
        return cpuDisplayable;
    }

    public void setCpuDisplayable(boolean cpuDisplayable) {
        this.cpuDisplayable = cpuDisplayable;
    }

    public boolean isSnapTimeDisplayable() {
        return snapTimeDisplayable;
    }

    public void setSnapTimeDisplayable(boolean snapTimeDisplayable) {
        this.snapTimeDisplayable = snapTimeDisplayable;
    }

}
