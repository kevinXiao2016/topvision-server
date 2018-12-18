/***********************************************************************
 * $Id: PnmpMonitorCmAction.java,v1.0 2017年8月9日 上午10:00:12 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.ems.cm.pnmp.service.PnmpMonitorCmService;
import com.topvision.ems.cm.pnmp.util.PnmpConstants;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.util.StringUtil;

/**
 * @author lizongtian
 * @created @2017年8月9日-上午10:00:12
 *
 */
@Controller("pnmpMonitorCmAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PnmpMonitorCmAction extends BaseAction {

    private static final long serialVersionUID = -8149269998322873335L;

    private String cmcName;
    private String cmMac;
    private String cmAddress;
    private String cmMacs;
    private String pageId;

    @Autowired
    private PnmpMonitorCmService pnmpMonitorCmService;

    /**
     * 跳转高频监控CM列表页
     * 
     * @return
     */
    public String showHighMonitorCmList() {
        return SUCCESS;
    }

    /**
     * 查询高频监控CM列表
     * 
     * @return
     */
    public String queryHighMonitorCmList() {
        JSONObject json = new JSONObject();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(cmcName)) {
            // mysql中下划线是特殊的，like的时候必须转义
            if (cmcName.contains("_")) {
                cmcName = cmcName.replace("_", "\\_");
            }
            queryMap.put("cmcName", cmcName);
        }
        if (!StringUtil.isEmpty(cmMac)) {
            String formatQueryMac = MacUtils.formatQueryMac(cmMac);
            if (formatQueryMac.indexOf(":") == -1) {
                queryMap.put("queryMacWithoutSplit", formatQueryMac);
            }
            queryMap.put("cmMac", formatQueryMac);
        }
        if (!StringUtil.isEmpty(cmAddress)) {
            if (cmAddress.contains("_")) {
                cmAddress = cmAddress.replace("_", "\\_");
            }
            queryMap.put("cmAddress", cmAddress);
        }
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        List<PnmpCmData> pnmpCmDatas = pnmpMonitorCmService.getHighMonitorCmList(queryMap);
        Integer rowCount = pnmpMonitorCmService.getHighMonitorCmListNum(queryMap);
        json.put("data", pnmpCmDatas);
        json.put("rowCount", rowCount);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 跳转到增加CM到高频队列页面
     * 
     * @return
     */
    public String showAddHighMonitorCm() {
        return SUCCESS;
    }

    /**
     * 添加高频CM
     * 
     * @return
     */
    public String addHighMonitorCm() {
        JSONObject json = new JSONObject();
        Integer code = PnmpConstants.MAC_NOT_NULL;
        if (!StringUtil.isEmpty(cmMac)) {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            String formatQueryMac = MacUtils.formatQueryMac(cmMac);
            if (formatQueryMac.indexOf(":") == -1) {
                queryMap.put("queryMacWithoutSplit", formatQueryMac);
            }
            queryMap.put("cmMac", formatQueryMac);
            code = pnmpMonitorCmService.addHighMonitorCm(queryMap);
        }
        json.put(PnmpConstants.CODE, code);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 从高频队列中移除单个CM
     * 
     * @return
     */
    public String removeHighMonitorCm() {
        String message = "success";
        try {
            pnmpMonitorCmService.removeHighMonitorCm(cmMac);
        } catch (Exception e) {
            logger.error("", e);
            message = "fail";
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 从高频队列中批量移除CM
     * 
     * @return
     */
    public String batchRemoveHighMonitorCm() {
        List<String> cmMacList = new ArrayList<String>();
        if (!StringUtil.isEmpty(cmMacs)) {
            String[] cmMacArr = cmMacs.split("\\$");
            for (String cmMac : cmMacArr) {
                cmMacList.add(cmMac);
            }
            pnmpMonitorCmService.batchRemoveHighMonitorCm(cmMacList);
        }
        return NONE;
    }

    /**
     * 判断手动加入高频监控队列的CM 在网管中是否存在
     * 
     * @return
     */
    public String checkCmMac() {
        String message = "fail";
        JSONObject json = new JSONObject();
        if (!StringUtil.isEmpty(cmMac)) {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            String formatQueryMac = MacUtils.formatQueryMac(cmMac);
            if (formatQueryMac.indexOf(":") == -1) {
                queryMap.put("queryMacWithoutSplit", formatQueryMac);
            }
            queryMap.put("cmMac", formatQueryMac);
            Map<String, Object> map = pnmpMonitorCmService.getCmcAttributeByCmMac(queryMap);
            if (map != null) {
                message = "success";
                json.put(PnmpConstants.IP_STRING, map.get(PnmpConstants.IP_STRING));
                json.put(PnmpConstants.ALIAS, map.get(PnmpConstants.ALIAS));
                json.put(PnmpConstants.LOCATION, map.get(PnmpConstants.LOCATION));
            }
        }
        json.put("message", message);
        writeDataToAjax(json);
        return NONE;
    }

    public String getCmcName() {
        return cmcName;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    public String getCmMac() {
        return cmMac;
    }

    public String getCmAddress() {
        return cmAddress;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public void setCmAddress(String cmAddress) {
        this.cmAddress = cmAddress;
    }

    public String getCmMacs() {
        return cmMacs;
    }

    public void setCmMacs(String cmMacs) {
        this.cmMacs = cmMacs;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }
}
