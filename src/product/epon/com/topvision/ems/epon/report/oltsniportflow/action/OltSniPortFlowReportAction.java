/***********************************************************************
 * $Id: OltSniPortFlowReportAction.java,v1.0 2013-10-28 下午3:19:53 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltsniportflow.action;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.report.domain.OltSniPortFlowDetail;
import com.topvision.ems.epon.report.domain.OltSniPortFlowStastic;
import com.topvision.ems.epon.report.oltsniportflow.service.OltSniPortFlowReportCreator;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-28-下午3:19:53
 * 
 */
@Controller("oltSniPortFlowReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltSniPortFlowReportAction extends BaseAction {
    private static final long serialVersionUID = -6876300612208958696L;
    private static final DateFormat todayFormatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    private static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressWarnings("unused")
    private static final int PERF15_RATE_PARAM = 10;
    @Autowired
    private OltSniPortFlowReportCreator oltSniPortFlowReportCreator;
    private Date statDate;
    private String stTime;
    private String etTime;
    private Integer rangeStart;
    private Integer rangeEnd;
    private Long entityId;

    /**
     * sniPortFlow.jsp 查询SNI端口流量的统计
     * 
     * @return
     */
    public String querySniFlowStastic() {
        Map<String, List<OltSniPortFlowStastic>> statSniFlowReport = new HashMap<String, List<OltSniPortFlowStastic>>();
        if (stTime != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (stTime == null || etTime == null) {
                Date date = new Date();
                stTime = todayFormatter.format(date);
                etTime = formatter.format(date);
            }
            map.put("sql", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "A.stats15EndTime"));
            map.put("userId", CurrentRequest.getCurrentUser().getUserId());
            statSniFlowReport = oltSniPortFlowReportCreator.statSniFlowReport(map);
        }
        request.setAttribute("statSniFlowReport", statSniFlowReport);
        return SUCCESS;
    }

    /**
     * sniPortFlow 导出SNI端口流量报表数据到excel
     * 
     * @return
     * @throws IOException
     */
    public String exportSniPortFlowPortToExcel() throws IOException {
        statDate = new Date();
        Map<String, Object> parMap = new HashMap<String, Object>();
        OutputStream out = response.getOutputStream();
        response.setContentType("application/msexcel");
        String fileName = URLEncoder.encode(ReportTaskUtil.getString("report.oltsniflow", "report") + "-"
                + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        // TODO 时间逻辑处理(结束时间未选择则默认为当前时间，开始时间未选择则默认为不限)
        if (stTime == null || etTime == null) {
            Date date = new Date();
            stTime = todayFormatter.format(date);
            etTime = formatter.format(date);
        }
        parMap.put("stTime", stTime);
        parMap.put("etTime", etTime);
        parMap.put("sql", ReportTaskUtil.getTimeSegmentSql(stTime, etTime, "A.stats15EndTime"));
        parMap.put("userId", CurrentRequest.getCurrentUser().getUserId());
        oltSniPortFlowReportCreator.exportSniPortFlowPortToExcel(parMap, statDate, out);
        return NONE;
    }

    /**
     * sniPortFlow.jsp 查看SNI端口流量详细
     * 
     * @return
     * @throws SQLException
     */
    public String querySniFlowDetail() throws SQLException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stTime", stTime);
        map.put("etTime", etTime);
        map.put("entityId", entityId);
        map.put("rangeStart", rangeStart);
        map.put("rangeEnd", rangeEnd);
        List<OltSniPortFlowDetail> list = oltSniPortFlowReportCreator.getSniFlowDetail(map);
        request.setAttribute("oltSniPortFlowDetail", list);
        return SUCCESS;
    }

    /**
     * sniPortFlowDetail.jsp 导出SNI端口流量详细到EXCEL
     * 
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public String exportSniFlowDetailToExcel() throws SQLException, IOException {
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stTime", stTime);
        map.put("etTime", etTime);
        map.put("entityId", entityId);
        map.put("rangeStart", rangeStart);
        map.put("rangeEnd", rangeEnd);
        response.setContentType("application/msexcel");
        String fileName = URLEncoder.encode(ReportTaskUtil.getString("report.sniFlowDetaillist", "report") + "-"
                + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        OutputStream out = response.getOutputStream();
        oltSniPortFlowReportCreator.exportSniFlowDetailToExcel(map, statDate, out);
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

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
