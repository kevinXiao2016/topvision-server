/***********************************************************************
 * $Id: CurrentAlarmReportAction.java,v1.0 2013-10-29 下午2:36:14 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.currentalarm.action;

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

import com.topvision.ems.epon.report.currentalarm.service.CurrentAlarmReportCreator;
import com.topvision.ems.epon.report.domain.CurrentAlarmDetail;
import com.topvision.ems.report.domain.FolderEntities;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-29-下午2:36:14
 * 
 */
@Controller("currentAlarmReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CurrentAlarmReportAction extends BaseAction {
    private static final long serialVersionUID = -1252685001158616597L;
    @Autowired
    private CurrentAlarmReportCreator currentAlarmReportCreator;
    private boolean query;
    private boolean eponSelected;
    private boolean ccmtsSelected;
    private boolean messageDisable;
    private boolean mainAlarmDisable;
    private boolean minorAlarmDisable;
    private boolean generalAlarmDisable;
    private Long entityId;
    private Long folderId;
    private Date statDate;
    private Integer alarmTypeId;
    private String stTime;
    private String etTime;
    private List<CurrentAlarmDetail> currentAlarmDetails;
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressWarnings("unused")
    private Integer weekStartDay;
    @Autowired
    private EntityTypeService entityTypeService;

    /**
     * curAlertReport.jsp 导出当前告警详细报表到EXCEL
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String exportCurrentDetailToExcel() throws UnsupportedEncodingException {
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
        currentAlarmDetails = currentAlarmReportCreator.statCurrentAlarmDetailReport(map);
        for (CurrentAlarmDetail currentAlarmDetail : currentAlarmDetails) {
            currentAlarmDetail.setDisplayName(getString(currentAlarmDetail.getDisplayName(), "fault"));
            if (currentAlarmDetail.getLastTime() != null) {
                currentAlarmDetail.setLastTimeStr(formatter.format(currentAlarmDetail.getLastTime()));
            }
            if (currentAlarmDetail.getConfirmTime() != null) {
                currentAlarmDetail.setConfirmTimeStr(formatter.format(currentAlarmDetail.getConfirmTime()));
            }
            if (currentAlarmDetail.getStatus() == 0) {
                currentAlarmDetail.setStatusStr(ReportTaskUtil.getString("report.unRecognized", "report"));
            }
        }
        currentAlarmReportCreator.exportCurrentDetailReportToExcel(currentAlarmDetails, statDate);
        return NONE;
    }

    /**
     * curAlertReport.jsp 导出当前告警报表到EXCEL
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String exportCurAlertReportToExcel() throws UnsupportedEncodingException {
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
        String sql = ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "lastTime");
        queryMap.put("sql", sql);
        queryMap.put("userId", CurrentRequest.getCurrentUser().getUserId());
        Map<Long, FolderEntities> currentAlarmReport = currentAlarmReportCreator.statCurrentAlarmReport(queryMap);
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
        currentAlarmReportCreator.exportCurAlertReportToExcel(currentAlarmReport, columnDisable, statDate, conditions);
        return NONE;
    }

    /**
     * curAlertReport.jsp 显示当前告警统计报表界面
     * 
     * @return String
     */
    public String showCurAlertReport() {
        Map<Long, FolderEntities> currentAlarmReport = new HashMap<Long, FolderEntities>();
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
            String sql = ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "lastTime");
            queryMap.put("sql", sql);
            queryMap.put("userId", CurrentRequest.getCurrentUser().getUserId());
            currentAlarmReport = currentAlarmReportCreator.statCurrentAlarmReport(queryMap);
        } else {
            eponSelected = true;
            ccmtsSelected = true;
        }
        request.setAttribute("currentAlarmReport", currentAlarmReport);
        return SUCCESS;
    }

    /**
     * curAlertReport.jsp 展示当前告警详细报表页面
     * 
     * @return
     */
    public String showCurAlertReportDetail() {
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
        currentAlarmDetails = currentAlarmReportCreator.statCurrentAlarmDetailReport(map);
        if (currentAlarmDetails.size() > 300) {
            currentAlarmDetails = currentAlarmDetails.subList(0, 300);
        }
        for (CurrentAlarmDetail currentAlarmDetail : currentAlarmDetails) {
            currentAlarmDetail.setDisplayName(getString(currentAlarmDetail.getDisplayName(), "fault"));
            if (currentAlarmDetail.getLastTime() != null) {
                currentAlarmDetail.setLastTimeStr(formatter.format(currentAlarmDetail.getLastTime()));
            }
            if (currentAlarmDetail.getConfirmTime() != null) {
                currentAlarmDetail.setConfirmTimeStr(formatter.format(currentAlarmDetail.getConfirmTime()));
            }
            if (currentAlarmDetail.getStatus() == 0) {
                currentAlarmDetail.setStatusStr(ReportTaskUtil.getString("report.unRecognized", "report"));
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

    public boolean isMessageDisable() {
        return messageDisable;
    }

    public void setMessageDisable(boolean messageDisable) {
        this.messageDisable = messageDisable;
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

    public Integer getAlarmTypeId() {
        return alarmTypeId;
    }

    public void setAlarmTypeId(Integer alarmTypeId) {
        this.alarmTypeId = alarmTypeId;
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

    public List<CurrentAlarmDetail> getCurrentAlarmDetails() {
        return currentAlarmDetails;
    }

    public void setCurrentAlarmDetails(List<CurrentAlarmDetail> currentAlarmDetails) {
        this.currentAlarmDetails = currentAlarmDetails;
    }

}
