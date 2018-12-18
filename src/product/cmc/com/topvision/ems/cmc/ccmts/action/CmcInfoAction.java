/***********************************************************************
 * $Id: Cmc_bRouteAction.java,v1.0 2013-8-6 下午4:40:27 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.network.domain.SubDeviceCount;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author dosion
 * @created @2013-8-6-下午4:40:27
 * 
 */
@Controller("cmcInfoAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcInfoAction extends BaseAction {
    private static final long serialVersionUID = 4259869100676362331L;
    private final Logger logger = LoggerFactory.getLogger(CmcInfoAction.class);
    @Autowired
    private CmcService cmcService;
    @Autowired
    private EntityService entityService;
    private Long cmcId;
    private Long onuId;
    private Long sysUpTime;
    private Date statDate;
    private String ccSortName = "cmcDeviceStyle";
    private List<CmcAttribute> deviceListItems = null;

    /**
     * 根据onuId获取cmcId
     * 
     * @return String
     */
    public String getCmcIdByOnuId() {
        cmcId = cmcService.getCmcIdByOnuId(onuId);
        if (cmcId == null) {
            cmcId = 0L;
        }
        Map<String, Long> result = new HashMap<String, Long>();
        result.put("cmcId", cmcId);
        writeDataToAjax(JSONObject.fromObject(result));
        return NONE;
    }

    /**
     * CMC运行时长显示界面
     * 
     * @return String
     */
    public String showCmcUptimeByEntity() {
        return SUCCESS;
    }

    /**
     * 获取CMC运行时长
     * 
     * @return String
     * @throws java.io.IOException
     *             Response流异常
     */
    public String getCmcUptimeByEntity() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            CmcAttribute cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
            if (cmcAttribute.getTopCcmtsSysStatus() != null && cmcAttribute.getTopCcmtsSysUpTime() != null) {
                Long tempTime = System.currentTimeMillis();
                Long dt = cmcAttribute.getDt().getTime();
                if (CmcConstants.TOPCCMTSSYSSTATUS_OFFLINE.equals(cmcAttribute.getTopCcmtsSysStatus())) {
                    sysUpTime = (long) -1;
                } else {
                    // TODO merge by Victor@20130816需要删除，以后代码不要这样写死，类似需要做到可配
                    /*
                     * if (entityTypeService.isCcmtsWithoutAgent(cmcAttribute.getCmcDeviceStyle())) { Long
                     * entityId = cmcService.getEntityIdByCmcId(cmcId); String oltVersion =
                     * entityService.getDeviceVersion(entityId); if
                     * (oltVersion.toUpperCase().indexOf("1.6.9.9-P4") > 0) {
                     * cmcAttribute.setTopCcmtsSysUpTime(cmcAttribute.getTopCcmtsSysUpTime() * 100);
                     * 
                     * } }
                     */
                    sysUpTime = cmcAttribute.getTopCcmtsSysUpTime() / 100 + (tempTime - dt) / 1000;
                }
            } else {
                sysUpTime = (long) -1;
            }
            //增加离线时间 Added by huangdongsheng 2013-12-19
            json.put("statusChangeTimeStr", cmcAttribute.getStatusChangeTimeStr());
        } catch (Exception e) {
            sysUpTime = (long) -1;
            logger.error("", e);
        }
        json.put("sysUpTime", sysUpTime);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取CMC基本信息
     * 
     * @return String
     */
    public String loadCmcBaseInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        CmcAttribute cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        json.put("cmcAttribute", cmcAttribute);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String loadCmcSubInfo() throws IOException {
        SubDeviceCount subCount = cmcService.getSubCountInfo(cmcId);
        JSONObject json = JSONObject.fromObject(subCount);
        json.write(response.getWriter());
        return NONE;
    }

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(Long sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public String getCcSortName() {
        return ccSortName;
    }

    public void setCcSortName(String ccSortName) {
        this.ccSortName = ccSortName;
    }

    public List<CmcAttribute> getDeviceListItems() {
        return deviceListItems;
    }

    public void setDeviceListItems(List<CmcAttribute> deviceListItems) {
        this.deviceListItems = deviceListItems;
    }

    public Logger getLogger() {
        return logger;
    }

}
