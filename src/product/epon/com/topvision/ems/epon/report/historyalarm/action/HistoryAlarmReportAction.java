/***********************************************************************
 * $Id: HistoryAlarmReportAction.java,v1.0 2013-10-29 下午3:19:43 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.historyalarm.action;

import java.io.UnsupportedEncodingException;
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

import com.topvision.ems.epon.report.domain.HistoryAlarmDetail;
import com.topvision.ems.epon.report.historyalarm.service.HistoryAlarmReportCreator;
import com.topvision.ems.report.domain.FolderEntities;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-29-下午3:19:43
 * 
 */
@Controller("historyAlarmReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HistoryAlarmReportAction extends BaseAction {
    private static final long serialVersionUID = 4784312074914110160L;
    @Autowired
    private HistoryAlarmReportCreator historyAlarmReportCreator;
    private boolean query;
    private boolean eponSelected;
    private boolean ccmtsSelected;
    private boolean mainAlarmDisable;
    private boolean minorAlarmDisable;
    private boolean generalAlarmDisable;
    private boolean messageDisable;
    private Integer alarmTypeId;
    private Long entityId;
    private Long folderId;
    private Date statDate;
    private String stTime;
    private String etTime;
    private List<HistoryAlarmDetail> historyAlarmDetails;
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressWarnings("unused")
    private Integer weekStartDay;
    @Autowired
    private EntityTypeService entityTypeService;

    /**
     * historyAlarmReport.jsp 导出历史告警详细报表到EXCEL
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String exportHistoryDetailToExcel() throws UnsupportedEncodingException {
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stTime", stTime);
        map.put("etTime", etTime);
        map.put("entityId", entityId);
        map.put("folderId", folderId);
        map.put("alarmTypeId", alarmTypeId);
        StringBuilder typesString = new StringBuilder();
        if (eponSelected == true) {
            typesString.append(entityTypeService.getOltType());
            if (ccmtsSelected == true) {
                typesString.append("," + entityTypeService.getCcmtsandcmtsType());
            }
        } else {
            if (ccmtsSelected == true) {
                typesString.append(entityTypeService.getCcmtsandcmtsType());
            }
        }
        map.put("types", typesString.toString());
        historyAlarmDetails = historyAlarmReportCreator.statHistoryAlarmDetailReport(map);
        for (HistoryAlarmDetail historyAlarmDetail : historyAlarmDetails) {
            historyAlarmDetail.setDisplayName(getString(historyAlarmDetail.getDisplayName(), "fault"));
            if (historyAlarmDetail.getFirstTime() != null) {
                historyAlarmDetail.setFirstTimeStr(formatter.format(historyAlarmDetail.getFirstTime()));
            }
            if (historyAlarmDetail.getClearTime() != null) {
                historyAlarmDetail.setClearTimeStr(formatter.format(historyAlarmDetail.getClearTime()));
            }
        }
        historyAlarmReportCreator.exportHistoryDetailReportToExcel(historyAlarmDetails, statDate);
        return NONE;
    }

    /**
     * historyAlarmReport.jsp 展示历史告警报表页面
     * 
     * @return
     */
    public String showHistoryAlarmReport() {
        Map<Long, FolderEntities> historyAlarmReport = new HashMap<Long, FolderEntities>();
        statDate = new Date();
        if (query == true) {
            StringBuilder typesString = new StringBuilder();
            // 查询条件
            Map<String, Object> queryMap = new HashMap<String, Object>();
            if (eponSelected == true) {
                typesString.append(entityTypeService.getOltType());
                if (ccmtsSelected == true) {
                    typesString.append("," + entityTypeService.getCcmtsandcmtsType());
                }
            } else {
                if (ccmtsSelected == true) {
                    typesString.append(entityTypeService.getCcmtsandcmtsType());
                }
            }
            queryMap.put("types", typesString.toString());
            String sql = ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "firstTime");
            queryMap.put("sql", sql);
            queryMap.put("userId", CurrentRequest.getCurrentUser().getUserId());
            historyAlarmReport = historyAlarmReportCreator.statHistoryAlarmReport(queryMap);
        } else {
            eponSelected = true;
            ccmtsSelected = true;
        }
        request.setAttribute("historyAlarmReport", historyAlarmReport);
        return SUCCESS;
    }

    /**
     * historyAlarmReport.jsp 导出历史告警报表到EXCEL
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String exportHistoryAlertReportToExcel() throws UnsupportedEncodingException {
        statDate = new Date();
        StringBuilder typesString = new StringBuilder();
        // 查询条件
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (eponSelected == true) {
            typesString.append(entityTypeService.getOltType());
            if (ccmtsSelected == true) {
                typesString.append("," + entityTypeService.getCcmtsandcmtsType());
            }
        } else {
            if (ccmtsSelected == true) {
                typesString.append(entityTypeService.getCcmtsandcmtsType());
            }
        }
        queryMap.put("types", typesString.toString());
        String sql = ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "firstTime");
        queryMap.put("sql", sql);
        queryMap.put("userId", CurrentRequest.getCurrentUser().getUserId());
        Map<Long, FolderEntities> historyAlarmReport = historyAlarmReportCreator.statHistoryAlarmReport(queryMap);
        Map<String, Boolean> columnDisable = new HashMap<String, Boolean>();
        columnDisable.put("mainAlarmDisable", mainAlarmDisable);
        columnDisable.put("minorAlarmDisable", minorAlarmDisable);
        columnDisable.put("generalAlarmDisable", generalAlarmDisable);
        columnDisable.put("messageDisable", messageDisable);
        String conditions = ReportTaskUtil.getString("report.statRange", "report") + ": " + stTime + " "
                + ReportTaskUtil.getString("report.to", "report") + "   " + etTime + "\n";
        conditions += ReportTaskUtil.getString("report.deviceType", "report") + ": ";
        if (eponSelected == true) {
            conditions += "EPON  ";
            if (ccmtsSelected == true) {
                conditions += "CMTS";
            }
        } else {
            if (ccmtsSelected == true) {
                conditions += "CMTS";
            }
        }
        historyAlarmReportCreator.exportHistoryAlertReportToExcel(historyAlarmReport, columnDisable, statDate,
                conditions);
        return NONE;
    }

    /**
     * historyAlarmReport.jsp 展示历史告警详细报表
     * 
     * @return
     */
    public String showHistoryAlertReportDetail() {
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stTime", stTime);
        map.put("etTime", etTime);
        map.put("entityId", entityId);
        map.put("folderId", folderId);
        map.put("alarmTypeId", alarmTypeId);
        StringBuilder typesString = new StringBuilder();
        if (eponSelected == true) {
            typesString.append(entityTypeService.getOltType());
            if (ccmtsSelected == true) {
                typesString.append("," + entityTypeService.getCcmtsandcmtsType());
            }
        } else {
            if (ccmtsSelected == true) {
                typesString.append(entityTypeService.getCcmtsandcmtsType());
            }
        }
        map.put("types", typesString.toString());
        map.put("start", 0);
        map.put("limit", 300);
        historyAlarmDetails = historyAlarmReportCreator.statHistoryAlarmDetailReport(map);
        if (historyAlarmDetails.size() > 300) {
            historyAlarmDetails = historyAlarmDetails.subList(0, 300);
        }
        for (HistoryAlarmDetail historyAlarmDetail : historyAlarmDetails) {
            historyAlarmDetail.setDisplayName(getString(historyAlarmDetail.getDisplayName(), "fault"));
            if (historyAlarmDetail.getFirstTime() != null) {
                historyAlarmDetail.setFirstTimeStr(formatter.format(historyAlarmDetail.getFirstTime()));
            }
            if (historyAlarmDetail.getClearTime() != null) {
                historyAlarmDetail.setClearTimeStr(formatter.format(historyAlarmDetail.getClearTime()));
            }
        }
        return SUCCESS;
    }

    protected String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public boolean isQuery() {
        return query;
    }

    public void setQuery(boolean query) {
        this.query = query;
    }

    public boolean isEponSelected() {
        return eponSelected;
    }

    public void setEponSelected(boolean eponSelected) {
        this.eponSelected = eponSelected;
    }

    public boolean isCcmtsSelected() {
        return ccmtsSelected;
    }

    public void setCcmtsSelected(boolean ccmtsSelected) {
        this.ccmtsSelected = ccmtsSelected;
    }

    public boolean isMainAlarmDisable() {
        return mainAlarmDisable;
    }

    public void setMainAlarmDisable(boolean mainAlarmDisable) {
        this.mainAlarmDisable = mainAlarmDisable;
    }

    public boolean isMinorAlarmDisable() {
        return minorAlarmDisable;
    }

    public void setMinorAlarmDisable(boolean minorAlarmDisable) {
        this.minorAlarmDisable = minorAlarmDisable;
    }

    public boolean isGeneralAlarmDisable() {
        return generalAlarmDisable;
    }

    public void setGeneralAlarmDisable(boolean generalAlarmDisable) {
        this.generalAlarmDisable = generalAlarmDisable;
    }

    public boolean isMessageDisable() {
        return messageDisable;
    }

    public void setMessageDisable(boolean messageDisable) {
        this.messageDisable = messageDisable;
    }

    public Integer getAlarmTypeId() {
        return alarmTypeId;
    }

    public void setAlarmTypeId(Integer alarmTypeId) {
        this.alarmTypeId = alarmTypeId;
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

    public List<HistoryAlarmDetail> getHistoryAlarmDetails() {
        return historyAlarmDetails;
    }

    public void setHistoryAlarmDetails(List<HistoryAlarmDetail> historyAlarmDetails) {
        this.historyAlarmDetails = historyAlarmDetails;
    }

}
