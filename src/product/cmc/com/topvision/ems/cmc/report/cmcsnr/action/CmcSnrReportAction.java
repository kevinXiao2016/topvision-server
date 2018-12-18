/***********************************************************************
 * $Id: CmcSnrReportAction.java,v1.0 2013-10-29 下午4:37:47 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmcsnr.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.report.cmcsnr.service.CmcSnrReportCreator;
import com.topvision.ems.cmc.report.domain.CmcSnrReportDetail;
import com.topvision.ems.cmc.report.domain.CmcSnrReportStatistics;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-29-下午4:37:47
 * 
 */
@Controller("cmcSnrReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcSnrReportAction extends BaseAction {
    private static final long serialVersionUID = 3658656214291283031L;
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final DateFormat todayFormatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    @Autowired
    private CmcSnrReportCreator cmcSnrReportCreator;
    @Autowired
    private EntityTypeService entityTypeService;
    private List<CmcSnrReportDetail> cmcSnrReportDetailList;
    private Date statDate;
    private String stTime;
    private String etTime;
    private String entityType;
    private Long entityId;
    private Long folderId;

    /**
     * ccmtsUsSnrStatistics.jsp 显示C-CMTS设备上行SNR统计报表界面
     * 
     * @return String
     */
    public String showCcmtsUsSnrStatistics() {
        statDate = new Date();
        Map<String, List<CmcSnrReportStatistics>> statCmcSnrReport = new HashMap<String, List<CmcSnrReportStatistics>>();
        if (entityType != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (stTime == null || "".equals(stTime) || etTime == null || "".equals(etTime)) {
                Date date = new Date();
                stTime = todayFormatter.format(date);
                etTime = formatter.format(date);
            }
            map.put("stTime", stTime);
            map.put("etTime", etTime);
            map.put("sql", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "A.dt"));
            map.put("entityType", entityType);
            map.put("userId", CurrentRequest.getCurrentUser().getUserId());
            statCmcSnrReport = cmcSnrReportCreator.statCmcSnrReport(map);
        }
        request.setAttribute("statCmcSnrReport", statCmcSnrReport);
        return SUCCESS;
    }

    /**
     * ccmtsUsSnrStatistics.jsp 显示CMC上行信噪详细页面
     * 
     * @return String
     */
    public String showSnrReprotDetail() {
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stTime", stTime);
        map.put("etTime", etTime);
        map.put("entityType", entityType);
        map.put("entityId", entityId);
        map.put("folderId", folderId);
        cmcSnrReportDetailList = cmcSnrReportCreator.getSnrReportDetail(map);
        return SUCCESS;
    }

    /**
     * ccmtsUsSnrStatistics.jsp 导出性能数据到excel
     * 
     * @return
     * @throws IOException
     */
    public String exportSnrReportToExcel() throws IOException {
        statDate = new Date();
        Map<String, Object> parMap = new HashMap<String, Object>();
        OutputStream out = response.getOutputStream();
        ServletActionContext.getResponse().setContentType("application/msexcel");
        String fileName = URLEncoder.encode(ReportTaskUtil.getString("report.ccmtsSnr", "report") + "-"
                + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
        ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
        if (stTime == null || etTime == null) {
            Date date = new Date();
            stTime = todayFormatter.format(date);
            etTime = formatter.format(date);
        }
        if (entityType == null) {
            entityType = entityTypeService.getCcmtswithoutagentType().toString();
        }
        parMap.put("stTime", stTime);
        parMap.put("etTime", etTime);
        parMap.put("sql", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "A.dt"));
        parMap.put("entityType", entityType);
        parMap.put("userId", CurrentRequest.getCurrentUser().getUserId());
        cmcSnrReportCreator.exportAsExcel(parMap, statDate, out);
        return NONE;
    }

    /**
     * ccmtsUsSnrDetail.jsp 导出CCMTS设备SNR详细到excel
     * 
     * @return
     * @throws IOException
     */
    public String exportSnrDetailReportToExcel() throws IOException {
        statDate = new Date();
        Map<String, Object> parMap = new HashMap<String, Object>();
        OutputStream out = response.getOutputStream();
        ServletActionContext.getResponse().setContentType("application/msexcel");
        String fileName = URLEncoder.encode(ReportTaskUtil.getString("report.ccmtsSnrDetail", "report") + "-"
                + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
        ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
        parMap.put("stTime", stTime);
        parMap.put("etTime", etTime);
        parMap.put("entityType", entityType);
        parMap.put("entityId", entityId);
        parMap.put("folderId", folderId);
        cmcSnrReportCreator.exportSnrDetailReportToExcel(parMap, statDate, out);
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

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public List<CmcSnrReportDetail> getCmcSnrReportDetailList() {
        return cmcSnrReportDetailList;
    }

    public void setCmcSnrReportDetailList(List<CmcSnrReportDetail> cmcSnrReportDetailList) {
        this.cmcSnrReportDetailList = cmcSnrReportDetailList;
    }

}
