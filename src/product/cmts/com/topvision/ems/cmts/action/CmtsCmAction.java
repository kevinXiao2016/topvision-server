/***********************************************************************
 * $Id: CmtsCmAction.java,v1.0 2013-8-8 上午11:49:20 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcChannelService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author loyal
 * @created @2013-8-8-上午11:49:20
 * 
 */
@Controller("cmtsCmAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmtsCmAction extends BaseAction{
    private static final long serialVersionUID = 1015039613032459572L;
    @Resource(name = "cmcChannelService")
    private CmcChannelService cmcChannelService;
    @Resource(name = "cmcUpChannelService")
    private CmcUpChannelService cmcUpChannelService;
    @Resource(name = "cmcDownChannelService")
    private CmcDownChannelService cmcDownChannelService;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    private Long cmcId;
    @Resource(name = "cmService")
    private CmService cmService;
    private int start;
    private int limit;
    private Long upChannelIndex;
    private Long downChannelIndex;
    private JSONArray cmcUpChannelBaseShowInfoListObject = new JSONArray();
    private JSONArray cmcDownChannelBaseShowInfoObject = new JSONArray();
    private Long totalNum;
    private Long registeredNum;
    private CmcAttribute cmcAttribute;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * 显示CM列表tab页面
     * 
     * @return String
     */
    public String showContactedCmList() {
        setCmcAttribute(cmcService.getCmcAttributeByCmcId(cmcId));
        List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                .getUpChannelBaseShowInfoList(cmcId);
        List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList = cmcDownChannelService
                .getDownChannelBaseShowInfoList(cmcId);
        
        cmcUpChannelBaseShowInfoListObject = JSONArray.fromObject(cmcUpChannelBaseShowInfoList);
        cmcDownChannelBaseShowInfoObject = JSONArray.fromObject(cmcDownChannelBaseShowInfoList); 
        Map<String, Object> cmQueryMap = new HashMap<String, Object>();
        cmQueryMap.put("cmcId", cmcId.toString());
        totalNum = cmService.getCmNumByStatus(cmQueryMap);
        cmQueryMap.put("status", Integer.toString(6));
        registeredNum = cmService.getCmNumByStatus(cmQueryMap);
        return SUCCESS;
    }
    
    /**
     * 刷新在CMTS上的CM信息
     * 
     * @return String
     */
    public String refreshCmOnCmtsInfo() {
        String message;
        try {
            cmService.refreshCmtsContactedCmList(cmcId);
            message = "true";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }
    
    public String getCmtsChannel(){
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcUpChannelBaseShowInfo> cmtsUpChannelList = cmcUpChannelService
                .getUpChannelBaseShowInfoList(cmcId);
        
        List<CmcDownChannelBaseShowInfo> cmtsDownChannelList = cmcDownChannelService
                .getDownChannelBaseShowInfoList(cmcId);
        
        json.put("cmtsUpChannelList", cmtsUpChannelList);
        json.put("cmtsDownChannelList", cmtsDownChannelList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public CmService getCmService() {
        return cmService;
    }

    public void setCmService(CmService cmService) {
        this.cmService = cmService;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public CmcChannelService getCmcChannelService() {
        return cmcChannelService;
    }

    public void setCmcChannelService(CmcChannelService cmcChannelService) {
        this.cmcChannelService = cmcChannelService;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public JSONArray getCmcUpChannelBaseShowInfoListObject() {
        return cmcUpChannelBaseShowInfoListObject;
    }

    public void setCmcUpChannelBaseShowInfoListObject(JSONArray cmcUpChannelBaseShowInfoListObject) {
        this.cmcUpChannelBaseShowInfoListObject = cmcUpChannelBaseShowInfoListObject;
    }

    public JSONArray getCmcDownChannelBaseShowInfoObject() {
        return cmcDownChannelBaseShowInfoObject;
    }

    public void setCmcDownChannelBaseShowInfoObject(JSONArray cmcDownChannelBaseShowInfoObject) {
        this.cmcDownChannelBaseShowInfoObject = cmcDownChannelBaseShowInfoObject;
    }

    public Long getUpChannelIndex() {
        return upChannelIndex;
    }

    public void setUpChannelIndex(Long upChannelIndex) {
        this.upChannelIndex = upChannelIndex;
    }

    public Long getDownChannelIndex() {
        return downChannelIndex;
    }

    public void setDownChannelIndex(Long downChannelIndex) {
        this.downChannelIndex = downChannelIndex;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getRegisteredNum() {
        return registeredNum;
    }

    public void setRegisteredNum(Long registeredNum) {
        this.registeredNum = registeredNum;
    }

    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }
}
