/***********************************************************************
 * $Id: CmDailyNumStaticReportAction.java,v1.0 2013-10-30 上午8:36:12 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmdailynumstatic.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.report.cmdailynumstatic.service.CmDailyNumStaticReportCreator;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author haojie
 * @created @2013-10-30-上午8:36:12
 * 
 */
@Controller("cmDailyNumStaticReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmDailyNumStaticReportAction extends BaseAction {
    private static final long serialVersionUID = -6635753477000299146L;
    @Autowired
    private CmDailyNumStaticReportCreator cmDailyNumStaticReportCreator;

    public static final SimpleDateFormat START_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    public static final SimpleDateFormat END_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 23:59:59");

    private JSONObject rangeList;
    private String range;
    private String rangeDetail;
    private String stTime;
    private String etTime;

    /**
     * 跳转cm实时数量
     * 
     * @return
     */
    public String loadCmDailyNumStatic() {
        rangeList = cmDailyNumStaticReportCreator.loadFolderOltCmcLists();
        return SUCCESS;
    }

    /**
     * 获取cm实时数量信息
     * 
     * @return
     * @throws IOException
     */
    public String loadCmDailyNumStaticGraphData() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startTime", stTime);
        map.put("endTime", etTime);
        if ("folders".equals(range) && rangeDetail != "") {
            map.put("folderId", rangeDetail);
        } else if ("olts".equals(range) && rangeDetail != "") {
            map.put("oltId", rangeDetail);
        } else if ("cmcs".equals(range) && rangeDetail != "") {
            map.put("cmcId", rangeDetail);
        }
        JSONObject json = cmDailyNumStaticReportCreator.statCmDailyNumStaticReport(map);
        json.write(response.getWriter());
        return NONE;
    }

    public JSONObject getRangeList() {
        return rangeList;
    }

    public void setRangeList(JSONObject rangeList) {
        this.rangeList = rangeList;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getRangeDetail() {
        return rangeDetail;
    }

    public void setRangeDetail(String rangeDetail) {
        this.rangeDetail = rangeDetail;
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

}
