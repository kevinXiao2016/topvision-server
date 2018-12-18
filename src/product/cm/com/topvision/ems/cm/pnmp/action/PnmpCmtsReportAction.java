/***********************************************************************
 * $Id: PnmpCmtsReportAction.java,v1.0 2017年8月9日 上午10:00:31 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.action;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmtsReport;
import com.topvision.ems.cm.pnmp.service.PnmpCmDataService;
import com.topvision.ems.cm.pnmp.service.PnmpCmtsReportService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author lizongtian
 * @created @2017年8月9日-上午10:00:31
 *
 */
@Controller("pnmpCmtsReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PnmpCmtsReportAction extends BaseAction {

    private static final long serialVersionUID = -7134794269745256340L;

    private Integer onlineCmNum; // 在线CM总数
    private Integer healthCmNum; // 健康CM总数
    private Integer marginalCmNum; // 轻度劣化倾向CM总数
    private Integer badCmNum; // 严重劣化倾向CM总数
    private String startTime;
    private String endTime;
    private String cmMac;
    private Long cmcId;
    private String cmcName;
    private String ipAddress;
    private Long maxUpChannelWidth;// 上行信道宽度
    private JSONArray correlationGroups;// 故障分组

    @Autowired
    private PnmpCmtsReportService pnmpCmtsReportService;
    @Autowired
    private PnmpCmDataService pnmpCmDataService;

    public String showCmtsReportView() {
        onlineCmNum = pnmpCmtsReportService.getOnlineCmNums();
        healthCmNum = pnmpCmtsReportService.getHealthCmNums();
        marginalCmNum = pnmpCmtsReportService.getMarginalCmNums();
        badCmNum = pnmpCmtsReportService.getBadCmNums();
        return SUCCESS;
    }

    public String loadCmtsReportData() {
        JSONObject json = new JSONObject();
        List<PnmpCmtsReport> cmtsReports = pnmpCmtsReportService.selectReports();
        Integer rowCount = pnmpCmtsReportService.selectCmtsReportsNums();
        json.put("data", cmtsReports);
        json.put("rowCount", rowCount);
        writeDataToAjax(json);
        return NONE;
    }

    public String showCmtsReportDetailView() {
        maxUpChannelWidth = pnmpCmDataService.getMaxUpChannelWidthByCmcId(cmcId);
        return SUCCESS;
    }

    public String loadCorrelationGroup() {
        correlationGroups = JSONArray.fromObject(pnmpCmDataService.getCorrelationGroup(cmcId));
        writeDataToAjax(correlationGroups);
        return NONE;
    }

    public String loadCmtsReportDetailData() {
        return NONE;
    }

    public String loadCmDetailData() {
        return NONE;
    }

    public Integer getOnlineCmNum() {
        return onlineCmNum;
    }

    public Integer getHealthCmNum() {
        return healthCmNum;
    }

    public Integer getMarginalCmNum() {
        return marginalCmNum;
    }

    public Integer getBadCmNum() {
        return badCmNum;
    }

    public void setOnlineCmNum(Integer onlineCmNum) {
        this.onlineCmNum = onlineCmNum;
    }

    public void setHealthCmNum(Integer healthCmNum) {
        this.healthCmNum = healthCmNum;
    }

    public void setMarginalCmNum(Integer marginalCmNum) {
        this.marginalCmNum = marginalCmNum;
    }

    public void setBadCmNum(Integer badCmNum) {
        this.badCmNum = badCmNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getCmcName() {
        return cmcName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getMaxUpChannelWidth() {
        return maxUpChannelWidth;
    }

    public void setMaxUpChannelWidth(Long maxUpChannelWidth) {
        this.maxUpChannelWidth = maxUpChannelWidth;
    }

    public JSONArray getCorrelationGroups() {
        return correlationGroups;
    }

    public void setCorrelationGroups(JSONArray correlationGroups) {
        this.correlationGroups = correlationGroups;
    }
}
