/***********************************************************************
 * $Id: OltMemReportAction.java,v1.0 2013-10-26 下午5:09:10 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltmem.action;

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
import com.topvision.ems.epon.report.oltmem.service.OltMemReportCreator;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author haojie
 * @created @2013-10-26-下午5:09:10
 * 
 */
@Controller("oltMemReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltMemReportAction extends BaseAction {
    private static final long serialVersionUID = 7122237854680402169L;
    protected Date statDate;
    private List<PerforamanceRank> performanceRank;
    @Autowired
    private OltMemReportCreator oltMemReportCreator;
    private static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    private boolean ipDisplayable = true;
    private boolean nameDisplayable = true;
    private boolean memDisplayable = true;
    private boolean snapTimeDisplayable = true;

    /**
     * 查看MEM使用率TOP 10
     * 
     * @return String
     */
    public String showEponMemRankReport() {
        DecimalFormat df = new DecimalFormat("#.00");
        statDate = new Date();
        performanceRank = oltMemReportCreator.getEponMemRank();
        for (PerforamanceRank rank : performanceRank) {
            if (rank.getMem() == null) {
                rank.setMEM_p("0%");
            } else if (rank.getMem() > 0) {
                rank.setMEM_p(df.format(rank.getMem() * 100) + "%");
            } else {
                rank.setMEM_p("0%");
            }
        }
        return SUCCESS;
    }

    /**
     * eponMemRankReport.jsp 导出内存使用率排行报表到excel
     * 
     * @return
     * @throws IOException
     */
    public String exportMenRankReportToExcel() throws IOException {
        statDate = new Date();
        OutputStream out = response.getOutputStream();
        response.setContentType("application/msexcel");
        String fileName = URLEncoder.encode(ReportTaskUtil.getString("report.memrate", "report") + "-"
                + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        oltMemReportCreator.exportAsExcel(statDate, out);
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

    public boolean isMemDisplayable() {
        return memDisplayable;
    }

    public void setMemDisplayable(boolean memDisplayable) {
        this.memDisplayable = memDisplayable;
    }

    public boolean isSnapTimeDisplayable() {
        return snapTimeDisplayable;
    }

    public void setSnapTimeDisplayable(boolean snapTimeDisplayable) {
        this.snapTimeDisplayable = snapTimeDisplayable;
    }

}
