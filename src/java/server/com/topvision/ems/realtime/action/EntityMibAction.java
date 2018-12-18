/***********************************************************************
 * $Id: EntityMibAction.java,v 1.1 Sep 30, 2009 3:13:24 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.realtime.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.platform.domain.Attribute;

import net.sf.json.JSONObject;

/**
 * @Create Date Sep 30, 2009 3:13:24 PM
 * 
 * @author kelers
 * 
 */
@Controller("entityMibAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityMibAction extends MibAction {
    private static final long serialVersionUID = 2754572419230833816L;
    protected static final Logger logger = LoggerFactory.getLogger(EntityMibAction.class);
    private Entity entity;
    @Autowired
    private EntityService entityService;
    private List<Attribute> tableTypes;
    private String ip;
    private int module = 4;

    public String loadMibInfo() throws Exception {
        JSONObject json = mibService.getData(entityId, tableType);
        writeDataToAjax(json);
        return NONE;
    }

    public String showEntityMibJsp() throws Exception {
        entity = entityService.getEntity(entityId);
        tableTypes = mibService.getTypes(entity);
        if (tableType == null) {
            tableType = tableTypes.get(0).getValue();
        }
        headers = mibService.getHeaders(tableType);
        return SUCCESS;
    }

    public Entity getEntity() {
        return entity;
    }

    public String getIp() {
        return ip;
    }

    public int getModule() {
        return module;
    }

    public List<Attribute> getTableTypes() {
        return tableTypes;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public void setTableTypes(List<Attribute> tableTypes) {
        this.tableTypes = tableTypes;
    }

}
