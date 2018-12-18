/***********************************************************************
 * $Id: CmcUserFlowReportAction.java,v1.0 2013-10-29 下午5:06:40 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmcuserflow.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.report.cmcuserflow.service.CmcUserFlowReportCreator;
import com.topvision.ems.cmc.report.domain.CmcUserFlowReportDetail;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-29-下午5:06:40
 * 
 */
@Controller("cmcUserFlowReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcUserFlowReportAction extends BaseAction {
    private static final long serialVersionUID = 589379100692275654L;
    private static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final DateFormat todayFormatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private CmcUserFlowReportCreator cmcUserFlowReportCreator;
    @Resource(name = "entityService")
    private EntityService entityService;
    private String stTime;
    private String etTime;
    private Date statDate;
    private Long entityId;
    private String entityType;

    /**
     * ccmtsUsSnrStatistics.jsp 显示C-CMTS设备CCMTS用户流量报表界面
     * 
     * @return String
     */
    public String showCcmtsUserFlowStatistics() {
        statDate = new Date();
        Map<String, Object> statCmcUserFlowReport = new HashMap<String, Object>();
        if (stTime != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (stTime == null || etTime == null) {
                Date date = new Date();
                stTime = todayFormatter.format(date);
                etTime = formatter.format(date);
            }
            /** 由于service层的reportCreator也会传递 channelSqlTime，oltPerfSqlTime。故把时间SQL的拼装放到了Action层 */
            map.put("channelSqlTime", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "A.dt"));
            map.put("oltPerfSqlTime", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "A.stats15EndTime"));
            map.put("entityType", entityType);
            map.put("userId", CurrentRequest.getCurrentUser().getUserId());
            statCmcUserFlowReport = cmcUserFlowReportCreator.statCmcUserFlowReport(map);
        }
        request.setAttribute("statCmcUserFlowReport", statCmcUserFlowReport);
        return SUCCESS;
    }

    /**
     * ccmtsUserFlowStatistics.jsp 显示用户流量详细信息，分别列出每个CCMTS的CM数，上联端口最大发送速率，各上行信道的SNR
     * 
     * @return
     */
    public String showCcmtsUserFlowDetail() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stTime", stTime);
        map.put("etTime", etTime);
        map.put("entityId", entityId);
        Entity entity = entityService.getEntity(entityId);
        Map<String, List<CmcUserFlowReportDetail>> details = cmcUserFlowReportCreator.getUserFlowDetail(map);
        request.setAttribute("details", details);
        request.setAttribute("entity", entity);
        return SUCCESS;
    }

    /**
     * ccmtsUserFlowDetail.jsp 导出用户流量详细报表到EXCEL
     * 
     * @return
     * @throws IOException
     */
    public String exportUserFlowDetailReportToExcel() throws IOException {
        statDate = new Date();
        Map<String, Object> parMap = new HashMap<String, Object>();
        OutputStream out = response.getOutputStream();
        ServletActionContext.getResponse().setContentType("application/msexcel");
        String fileName = URLEncoder.encode(ReportTaskUtil.getString("report.ccmtsUserDetail", "report") + "-"
                + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
        ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
        parMap.put("stTime", stTime);
        parMap.put("etTime", etTime);
        parMap.put("entityId", entityId);
        Entity entity = entityService.getEntity(entityId);
        parMap.put("entity", entity);
        cmcUserFlowReportCreator.exportCmcUserFlowReportToExcel(parMap, statDate, out);

        return NONE;
    }

    /**
     * ccmtsUserFlowStatistics.jsp 导出用户流量数据到excel
     * 
     * @return
     * @throws IOException
     */
    public String exportUserFlowReportToExcel() throws IOException {
        Map<String, Object> parMap = new HashMap<String, Object>();
        Date statDate = new Date();
        if (stTime == null || etTime == null) {
            Date date = new Date();
            stTime = todayFormatter.format(date);
            etTime = formatter.format(date);
        }
        parMap.put("stTime", stTime);
        parMap.put("etTime", etTime);
        /** 由于service层的reportCreator也会传递 channelSqlTime，oltPerfSqlTime。故把时间SQL的拼装放到了Action层 */
        parMap.put("channelSqlTime", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "A.dt"));
        parMap.put("oltPerfSqlTime", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "A.stats15EndTime"));
        parMap.put("userId", CurrentRequest.getCurrentUser().getUserId());
        OutputStream out = response.getOutputStream();
        response.setContentType("application/msexcel");
        String fileName = URLEncoder.encode(ReportTaskUtil.getString("report.ccmtsUserFlow", "report") + "-"
                + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        cmcUserFlowReportCreator.exportFlowReportToExcel(parMap, statDate, out);
        return NONE;
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

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

}
