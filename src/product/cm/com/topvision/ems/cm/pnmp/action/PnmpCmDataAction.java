/***********************************************************************
 * $Id: PnmpCmDataAction.java,v1.0 2017年8月9日 上午10:00:46 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.ems.cm.pnmp.service.PnmpCmDataService;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.util.StringUtil;

/**
 * @author lizongtian
 * @created @2017年8月9日-上午10:00:46
 *
 */
@Controller("pnmpCmDataAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PnmpCmDataAction extends BaseAction {

    private static final long serialVersionUID = -7579884220602460295L;

    private String cmMac;
    private String startTime;
    private String endTime;
    private Long cmcId;
    private Integer correlationGroup;
    private String cmMacs;

    @Autowired
    private PnmpCmDataService pnmpCmDataService;

    /**
     * 跳转指定cm指定时间内的历史数据页面
     * 
     * @return
     */
    public String showHistoryData() {
        return SUCCESS;
    }

    public String showRealData() {
        return SUCCESS;
    }

    /**
     * 加载指定cm指定时间的历史数据
     *
     * @return
     */
    public String loadHistoryData() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(startTime)) {
            queryMap.put("startTime", startTime);
        }
        if (!StringUtil.isEmpty(endTime)) {
            queryMap.put("endTime", endTime);
        }
        queryMap.put("cmMac", cmMac);
        List<PnmpCmData> pnmpCmDatas = pnmpCmDataService.getHistoryData(queryMap);
        JSONObject json = new JSONObject();
        json.put("data", pnmpCmDatas);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 加载指定cm指定时间的历史数据
     *
     * @return
     */
    public String loadRealData() {
        PnmpCmData pnmpCmDatas = pnmpCmDataService.realPnmp(cmcId,cmMac);
        writeDataToAjax(pnmpCmDatas);
        return NONE;
    }

    /**
     * 加载指定CMTS的指定故障分组的CM列表
     * 
     * @return
     */
    public String loadCmtsDataByGroup() {
        JSONObject json = new JSONObject();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("cmcId", cmcId);
        // -1说明选择所有的CM
        if (correlationGroup != null && correlationGroup != -1) {
            queryMap.put("correlationGroup", correlationGroup);
        }
        if (!StringUtil.isEmpty(cmMac)) {
            String formatQueryMac = MacUtils.formatQueryMac(cmMac);
            if (formatQueryMac.indexOf(":") == -1) {
                queryMap.put("queryMacWithoutSplit", formatQueryMac);
            }
            queryMap.put("cmMac", formatQueryMac);
        }
        List<PnmpCmData> pnmpCmDataList = pnmpCmDataService.getDataByGroup(queryMap);
        // 只展示在线的CM
        for (Iterator<PnmpCmData> iterator = pnmpCmDataList.iterator(); iterator.hasNext();) {
            PnmpCmData pnmpCmData = iterator.next();
            if (!CmAttribute.isCmOnline(pnmpCmData.getStatusValue().intValue())) {
                iterator.remove();
            }
        }
        json.put("data", pnmpCmDataList);
        writeDataToAjax(json);
        return NONE;
    }

    public String getCmMac() {
        return cmMac;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public Integer getCorrelationGroup() {
        return correlationGroup;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public void setCorrelationGroup(Integer correlationGroup) {
        this.correlationGroup = correlationGroup;
    }

    public String getCmMacs() {
        return cmMacs;
    }

    public void setCmMacs(String cmMacs) {
        this.cmMacs = cmMacs;
    }
}
