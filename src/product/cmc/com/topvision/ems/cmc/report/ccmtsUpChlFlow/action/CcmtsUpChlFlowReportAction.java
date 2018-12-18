/***********************************************************************
 * $Id: CcmtsUpChlFlowReportAction.java,v1.0 2014-3-24 下午3:24:44 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtsUpChlFlow.action;

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

import com.topvision.ems.cmc.report.ccmtsUpChlFlow.service.CcmtsUpChlFlowReportCreator;
import com.topvision.ems.cmc.report.domain.CcmtsChlFlowDetail;
import com.topvision.ems.cmc.report.domain.CcmtsChlFlowStatic;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2014-3-24-下午3:24:44
 * 
 */
@Controller("ccmtsUpChlFlowReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CcmtsUpChlFlowReportAction extends BaseAction {
    private static final long serialVersionUID = -269294967517303611L;
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressWarnings("unused")
    private static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final DateFormat todayFormatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    @Autowired
    private CcmtsUpChlFlowReportCreator ccmtsUpChlFlowReportCreator;
    private Date statDate;
    private String stTime;
    private String etTime;
    private String rangeName;
    private Long entityId;

    /**
     * CCMTS上行流量统计报表跳转
     * 
     * @return
     */
    public String showCcmtsUpChlFlowAsset() {
        statDate = new Date();
        Map<String, List<CcmtsChlFlowStatic>> statUpChlFlowStatic = new HashMap<String, List<CcmtsChlFlowStatic>>();
        Map<String, Object> map = new HashMap<String, Object>();
        if (stTime == null || "".equals(stTime) || etTime == null || "".equals(etTime)) {
            //第一次进入，不查数据库
        } else {
            map.put("sql", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "A.collectTime"));
            map.put("userId", CurrentRequest.getCurrentUser().getUserId());
            statUpChlFlowStatic = ccmtsUpChlFlowReportCreator.statUpChlFlowStatic(map);
        }
        request.setAttribute("statUpChlFlowStatic", statUpChlFlowStatic);
        return SUCCESS;
    }

    /**
     * 导出主表
     * 
     * @return
     */
    public String exportCcmtsUpChlFlowReportToExcel() {
        statDate = new Date();
        Map<String, List<CcmtsChlFlowStatic>> statUpChlFlowStatic = new HashMap<String, List<CcmtsChlFlowStatic>>();
        Map<String, Object> map = new HashMap<String, Object>();
        if (stTime == null || "".equals(stTime) || etTime == null || "".equals(etTime)) {
            stTime = todayFormatter.format(statDate);
            etTime = formatter.format(statDate);
        }
        map.put("stTime", stTime);
        map.put("etTime", etTime);
        map.put("sql", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "A.collectTime"));
        map.put("userId", CurrentRequest.getCurrentUser().getUserId());
        statUpChlFlowStatic = ccmtsUpChlFlowReportCreator.statUpChlFlowStatic(map);
        String conditions = ReportTaskUtil.getString("report.statRange", "report") + ": " + stTime + " "
                + ReportTaskUtil.getString("report.to", "report") + "   " + etTime + "\n";
        conditions += ReportTaskUtil.getString("report.assetOnFolder", "report");
        ccmtsUpChlFlowReportCreator.exportReportToExcel(statUpChlFlowStatic, statDate, conditions);
        return NONE;
    }

    /**
     * CCMTS上行流量详细报表跳转
     * 
     * @return
     */
    public String showCcmtsUpChlFlowDetail() {
        Map<String, List<CcmtsChlFlowDetail>> statUpChlFlowDetail = new HashMap<String, List<CcmtsChlFlowDetail>>();
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("flowTimeSql", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "A.collectTime"));
        map.put("userNumTimeSql", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "realTime"));
        if (entityId != null) {
            map.put("entityId", entityId);
        }
        map.put("rangeSql", ReportTaskUtil.getRangeSql(rangeName, "max(channelUtilization)"));
        map.put("userId", CurrentRequest.getCurrentUser().getUserId());
        statUpChlFlowDetail = ccmtsUpChlFlowReportCreator.statUpChlFlowDetail(map);
        request.setAttribute("statUpChlFlowDetail", statUpChlFlowDetail);
        return SUCCESS;
    }

    public String exportCcmtsUpChlDetailToExcel() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stTime", stTime);
        map.put("etTime", etTime);
        map.put("flowTimeSql", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "A.collectTime"));
        map.put("userNumTimeSql", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "realTime"));
        if (entityId != null) {
            map.put("entityId", entityId);
        }
        map.put("rangeSql", ReportTaskUtil.getRangeSql(rangeName, "max(channelUtilization)"));
        map.put("userId", CurrentRequest.getCurrentUser().getUserId());
        ccmtsUpChlFlowReportCreator.exportCcmtsUpChlDetailToExcel(map);
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

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

}
