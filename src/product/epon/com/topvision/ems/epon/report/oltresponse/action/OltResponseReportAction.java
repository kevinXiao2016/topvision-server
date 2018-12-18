/***********************************************************************
 * $Id: OltResponseReportAction.java,v1.0 2013-10-28 上午11:38:47 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltresponse.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.domain.PerforamanceRank;
import com.topvision.ems.epon.report.oltresponse.service.OltResponseReportCreator;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author haojie
 * @created @2013-10-28-上午11:38:47
 * 
 */
@Controller("oltResponseReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltResponseReportAction extends BaseAction {
    private static final long serialVersionUID = -5120416392254060023L;
    private static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    @Autowired
    private OltResponseReportCreator oltResponseReportCreator;
    private List<PerforamanceRank> performanceRank;
    private boolean nameDisplayable = true;
    private boolean ipDisplayable = true;
    private boolean delayDisplayable = true;
    private boolean snapTimeDisplayable = true;
    protected Date statDate;

    /**
     * 查看设备响应延迟（delay）使用率TOP 10
     * 
     * @return String
     */
    public String showEponDelayRankReport() {
        statDate = new Date();
        performanceRank = oltResponseReportCreator.getEponDelayRank();
        return SUCCESS;
    }

    /**
     * eponDelayRankReport.jsp 导出设备响应延迟排行报表到excel
     * 
     * @return
     * @throws IOException
     */
    public String exportDelayRankReportToExcel() throws IOException {
        statDate = new Date();
        OutputStream out = response.getOutputStream();
        response.setContentType("application/msexcel");
        String fileName = URLEncoder.encode(ReportTaskUtil.getString("report.devicedelay", "report") + "-"
                + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        oltResponseReportCreator.exportAsExcel(statDate, out);
        return NONE;
    }

    public List<PerforamanceRank> getPerformanceRank() {
        return performanceRank;
    }

    public void setPerformanceRank(List<PerforamanceRank> performanceRank) {
        this.performanceRank = performanceRank;
    }

    public boolean isNameDisplayable() {
        return nameDisplayable;
    }

    public void setNameDisplayable(boolean nameDisplayable) {
        this.nameDisplayable = nameDisplayable;
    }

    public boolean isIpDisplayable() {
        return ipDisplayable;
    }

    public void setIpDisplayable(boolean ipDisplayable) {
        this.ipDisplayable = ipDisplayable;
    }

    public boolean isDelayDisplayable() {
        return delayDisplayable;
    }

    public void setDelayDisplayable(boolean delayDisplayable) {
        this.delayDisplayable = delayDisplayable;
    }

    public boolean isSnapTimeDisplayable() {
        return snapTimeDisplayable;
    }

    public void setSnapTimeDisplayable(boolean snapTimeDisplayable) {
        this.snapTimeDisplayable = snapTimeDisplayable;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

}
