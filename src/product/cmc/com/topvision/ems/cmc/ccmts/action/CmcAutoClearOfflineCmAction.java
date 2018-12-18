/***********************************************************************
 * $Id: CmcAutoClearOfflineCm.java,v1.0 2017年5月20日 下午2:42:02 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.service.CmcClearCmOnTimeService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author ls
 * @created @2017年5月20日-下午2:42:02
 *
 */
@Controller("cmcAutoClearOfflineCmAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcAutoClearOfflineCmAction extends BaseAction{
    private static final long serialVersionUID = 5941713272267016130L;
    
    private Long entityId;
    private Long cmcId;
    private Entity entity;
    private int time;
    private int setTime;
    
    @Autowired
    private CmcService cmcService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private CmcClearCmOnTimeService cmcClearCmOnTimeService;
    
    public String showAutoClearOfflineCm(){
        setTime=cmcClearCmOnTimeService.getCmcClearTime(cmcId);
        return SUCCESS;
    }
    
    public String timeConfigSet(){
        String message;
        try{
            cmcClearCmOnTimeService.setClearTimeOnCC(time,cmcId);
            message = "success";
        }catch(Exception e){
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }
        
    public Long getEntityId() {
        return entityId;
    }
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public Entity getEntity() {
        return entity;
    }
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getSetTime() {
        return setTime;
    }

    public void setSetTime(int setTime) {
        this.setTime = setTime;
    }

}
