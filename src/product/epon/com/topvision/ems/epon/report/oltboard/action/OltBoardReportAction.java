/***********************************************************************
 * $Id: OltBoardReportAction.java,v1.0 2013-10-26 上午9:38:00 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltboard.action;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.domain.EponBoardStatistics;
import com.topvision.ems.epon.report.oltboard.service.OltBoardReportCreator;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author haojie
 * @created @2013-10-26-上午9:38:00
 * 
 */
@Controller("oltBoardReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltBoardReportAction extends BaseAction {
    private static final long serialVersionUID = -782326679673411105L;
    protected Date statDate;
    private List<EponBoardStatistics> eponBoardStatistics;
    @Autowired
    private OltBoardReportCreator oltBoardReportCreator;
    private boolean mpuaDisplayable;
    private boolean mpubDisplayable;
    private boolean geuaDisplayable;
    private boolean geubDisplayable;
    private boolean epuaDisplayable;
    private boolean xguaDisplayable;
    private boolean xgubDisplayable;
    private boolean xgucDisplayable;
    private boolean epubDisplayable;
    private boolean xpuaDisplayable;
    private boolean ipDisplayable = true;
    private boolean allSlotDisplayable = true;
    private boolean onLineDisplayable = true;

    /**
     * 查看板卡统计页面
     * 
     * @return String
     */
    public String showEponBoardReport() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sortName", "ip");
        statDate = new Date();
        try {
            eponBoardStatistics = oltBoardReportCreator.getBoardList(map);
        } catch (Exception e) {
            eponBoardStatistics = null;
        }
        return SUCCESS;
    }

    /**
     * eponDeviceBoardReport.jsp 导出OLT板卡使用情况清单到EXCEL
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String exportDeviceBoardToExcel() throws UnsupportedEncodingException {
        statDate = new Date();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("sortName", "ip");
        List<EponBoardStatistics> eponBoardStatistics = oltBoardReportCreator.getBoardList(queryMap);
        Map<String, Boolean> columnDisable = new HashMap<String, Boolean>();
        columnDisable.put("mpuaDisplayable", mpuaDisplayable);
        columnDisable.put("mpubDisplayable", mpubDisplayable);
        columnDisable.put("geuaDisplayable", geuaDisplayable);
        columnDisable.put("geubDisplayable", geubDisplayable);
        columnDisable.put("xguaDisplayable", xguaDisplayable);
        columnDisable.put("xgubDisplayable", xgubDisplayable);
        columnDisable.put("xgucDisplayable", xgucDisplayable);
        columnDisable.put("epuaDisplayable", epuaDisplayable);
        columnDisable.put("epubDisplayable", epubDisplayable);
        oltBoardReportCreator.exportOltBoardReportToExcel(eponBoardStatistics, columnDisable, statDate);
        return NONE;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public List<EponBoardStatistics> getEponBoardStatistics() {
        return eponBoardStatistics;
    }

    public void setEponBoardStatistics(List<EponBoardStatistics> eponBoardStatistics) {
        this.eponBoardStatistics = eponBoardStatistics;
    }

    public boolean isMpuaDisplayable() {
        return mpuaDisplayable;
    }

    public void setMpuaDisplayable(boolean mpuaDisplayable) {
        this.mpuaDisplayable = mpuaDisplayable;
    }

    public boolean isGeuaDisplayable() {
        return geuaDisplayable;
    }

    public void setGeuaDisplayable(boolean geuaDisplayable) {
        this.geuaDisplayable = geuaDisplayable;
    }

    public boolean isGeubDisplayable() {
        return geubDisplayable;
    }

    public void setGeubDisplayable(boolean geubDisplayable) {
        this.geubDisplayable = geubDisplayable;
    }

    public boolean isEpuaDisplayable() {
        return epuaDisplayable;
    }

    public void setEpuaDisplayable(boolean epuaDisplayable) {
        this.epuaDisplayable = epuaDisplayable;
    }

    public boolean isXguaDisplayable() {
        return xguaDisplayable;
    }

    public void setXguaDisplayable(boolean xguaDisplayable) {
        this.xguaDisplayable = xguaDisplayable;
    }

    public boolean isXgubDisplayable() {
        return xgubDisplayable;
    }

    public void setXgubDisplayable(boolean xgubDisplayable) {
        this.xgubDisplayable = xgubDisplayable;
    }

    public boolean isXgucDisplayable() {
        return xgucDisplayable;
    }

    public void setXgucDisplayable(boolean xgucDisplayable) {
        this.xgucDisplayable = xgucDisplayable;
    }

    public boolean isEpubDisplayable() {
        return epubDisplayable;
    }

    public void setEpubDisplayable(boolean epubDisplayable) {
        this.epubDisplayable = epubDisplayable;
    }

    public boolean isIpDisplayable() {
        return ipDisplayable;
    }

    public void setIpDisplayable(boolean ipDisplayable) {
        this.ipDisplayable = ipDisplayable;
    }

    public boolean isAllSlotDisplayable() {
        return allSlotDisplayable;
    }

    public void setAllSlotDisplayable(boolean allSlotDisplayable) {
        this.allSlotDisplayable = allSlotDisplayable;
    }

    public boolean isOnLineDisplayable() {
        return onLineDisplayable;
    }

    public void setOnLineDisplayable(boolean onLineDisplayable) {
        this.onLineDisplayable = onLineDisplayable;
    }

    public boolean isMpubDisplayable() {
        return mpubDisplayable;
    }

    public void setMpubDisplayable(boolean mpubDisplayable) {
        this.mpubDisplayable = mpubDisplayable;
    }

    public boolean isXpuaDisplayable() {
        return xpuaDisplayable;
    }

    public void setXpuaDisplayable(boolean xpuaDisplayable) {
        this.xpuaDisplayable = xpuaDisplayable;
    }

}
