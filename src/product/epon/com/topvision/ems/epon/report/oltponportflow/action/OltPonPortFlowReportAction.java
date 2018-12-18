/***********************************************************************
 * $Id: OltPonPortFlowReportAction.java,v1.0 2013-10-28 上午10:47:53 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltponportflow.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
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

import com.topvision.ems.epon.report.domain.OltPonPortFlowDetail;
import com.topvision.ems.epon.report.domain.OltPonPortFlowStastic;
import com.topvision.ems.epon.report.oltponportflow.service.OltPonPortFlowReportCreator;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-28-上午10:47:53
 * 
 */
@Controller("oltPonPortFlowReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltPonPortFlowReportAction extends BaseAction {
    private static final long serialVersionUID = 6767259093364982380L;
    private static final DateFormat todayFormatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    @SuppressWarnings("unused")
    private static final int PERF15_RATE_PARAM = 10;
    @Autowired
    private OltPonPortFlowReportCreator oltPonPortFlowReportCreator;
    private Date statDate;
    private String stTime;
    private String etTime;
    private Long entityId;
    private Integer rangeStart;
    private Integer rangeEnd;

    /**
     * ponPortFlow.jsp 查询PON端口流量的统计
     * 
     * @return
     */
    public String queryPonFlowStastic() {
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, List<OltPonPortFlowStastic>> statSniFlowReport = new HashMap<String, List<OltPonPortFlowStastic>>();
        if (stTime != null) {
            if (stTime == null || etTime == null) {
                Date date = new Date();
                stTime = todayFormatter.format(date);
                etTime = formatter.format(date);
            }
            map.put("sql", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "A.stats15EndTime"));
            map.put("userId", CurrentRequest.getCurrentUser().getUserId());
            statSniFlowReport = oltPonPortFlowReportCreator.statPonFlowReport(map);
        }
        request.setAttribute("statPonFlowReport", statSniFlowReport);
        return SUCCESS;
    }

    /**
     * ponPortFlow 导出PON端口流量报表数据到excel
     * 
     * @return
     * @throws IOException
     */
    public String exportPonPortFlowPortToExcel() throws IOException {
        Map<String, Object> parMap = new HashMap<String, Object>();
        statDate = new Date();
        OutputStream out = response.getOutputStream();
        ServletActionContext.getResponse().setContentType("application/msexcel");
        String fileName = URLEncoder.encode(ReportTaskUtil.getString("report.oltponflow", "report") + "-"
                + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
        ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
        // TODO 时间逻辑处理
        if (stTime == null || etTime == null) {
            Date date = new Date();
            stTime = todayFormatter.format(date);
            etTime = formatter.format(date);
        }
        parMap.put("stTime", stTime);
        parMap.put("etTime", etTime);
        parMap.put("sql", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "A.stats15EndTime"));
        parMap.put("userId", CurrentRequest.getCurrentUser().getUserId());
        oltPonPortFlowReportCreator.exportPonPortFlowPortToExcel(parMap, statDate, out);
        return NONE;
    }

    /**
     * ponPortFlow.jsp 查看PON端口流量详细
     * 
     * @return
     * @throws SQLException
     */
    public String queryPonFlowDetail() throws SQLException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stTime", stTime);
        map.put("etTime", etTime);
        map.put("entityId", entityId);
        map.put("rangeStart", rangeStart);
        map.put("rangeEnd", rangeEnd);
        List<OltPonPortFlowDetail> list = oltPonPortFlowReportCreator.getPonFlowDetail(map);
        request.setAttribute("oltPonPortFlowDetail", list);
        return SUCCESS;
    }

    /**
     * ponPortFlowDetail.jsp 导出PON端口流量详细到EXCEL
     * 
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public String exportPonFlowDetailToExcel() throws SQLException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        statDate = new Date();
        map.put("stTime", stTime);
        map.put("etTime", etTime);
        map.put("entityId", entityId);
        map.put("rangeStart", rangeStart);
        map.put("rangeEnd", rangeEnd);
        response.setContentType("application/msexcel");
        String fileName = URLEncoder.encode(ReportTaskUtil.getString("report.ponFlowDetaillist", "report") + "-"
                + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        OutputStream out = response.getOutputStream();
        oltPonPortFlowReportCreator.exportPonFlowDetailToExcel(map, statDate, out);
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

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(Integer rangeStart) {
        this.rangeStart = rangeStart;
    }

    public Integer getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(Integer rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

}
