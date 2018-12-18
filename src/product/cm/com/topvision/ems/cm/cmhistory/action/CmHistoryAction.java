/***********************************************************************
 * $Id: CmHistoryAction.java,v1.0 2015年4月9日 下午8:35:02 $ * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmhistory.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cm.cmhistory.domain.CmHistoryShow;
import com.topvision.ems.cm.cmhistory.service.CmHistoryService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author YangYi
 * @created @2015年4月9日-下午8:35:02
 * 
 */
@Controller("cmHistoryAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmHistoryAction extends BaseAction {
    private static final long serialVersionUID = -6558432252346654678L;
    @Resource(name = "cmHistoryService")
    private CmHistoryService cmHistoryService;
    private String cmId;
    private String cmIp;
    private String cmMac;
    private String startTime;
    private String endTime;

    public String showCmHistory() {
        return SUCCESS;
    }

    /**
     * 读取CM历史信号质量列表
     * 
     * @return
     */
    public String loadCmHistoryList() {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("cmId", cmId);
        queryMap.put("startTime", startTime);
        queryMap.put("endTime", endTime);
        List<CmHistoryShow> cmHistoryList = cmHistoryService.getCmHistory(queryMap);
        Integer size = cmHistoryList.size();
        json.put("rowCount", size);
        json.put("data", cmHistoryList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    // TODO 准备删除
    public String showTestCmHistory() {
        return SUCCESS;
    }

    // TODO 准备删除
    public String insert3600WData() {
        cmHistoryService.insert3600WData();
        return NONE;
    }

    // TODO 准备删除
    public String insert1Data() {
        cmHistoryService.insert1WData();
        return NONE;
    }

    public String getCmIp() {
        return cmIp;
    }

    public void setCmIp(String cmIp) {
        this.cmIp = cmIp;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public String getCmId() {
        return cmId;
    }

    public void setCmId(String cmId) {
        this.cmId = cmId;
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

}
