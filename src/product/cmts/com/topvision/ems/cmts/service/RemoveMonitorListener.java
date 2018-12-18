/***********************************************************************
 * $Id: Cmc8800AEntityListener.java,v1.0 2012-9-25 下午3:44:17 $
 *
 * @author: haojie
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author haojie
 * @created @2012-9-25-下午3:44:17
 */
@Service("removeMonitorListener")
public class RemoveMonitorListener implements EntityListener {
    private final Logger logger = LoggerFactory.getLogger(RemoveMonitorListener.class);
    @Resource(name = "entityService")
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "cmtsPerfService")
    private CmtsPerfService cmtsPerfService;
    @Resource(name = "messageService")
    private MessageService messageService;
    @Resource(name = "cmcPerfService")
    private CmcPerfService cmcPerfService;

    @PostConstruct
    public void init() {
        messageService.addListener(EntityListener.class, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.message.event.EntityListener#entityAdded(com.topvision.ems.message.event
     * .EntityEvent)
     */
    @Override
    public void entityAdded(EntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.message.event.EntityListener#entityDiscovered(com.topvision.ems.message
     * .event.EntityEvent)
     */
    @Override
    public void entityDiscovered(EntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#attributeChanged(long,
     * java.lang.String[], java.lang.String[])
     */
    @Override
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.message.event.EntityListener#entityChanged(com.topvision.ems.message.event
     * .EntityEvent)
     */
    @Override
    public void entityChanged(EntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.message.event.EntityListener#entityRemoved(com.topvision.ems.message.event
     * .EntityEvent)
     */
    @Override
    public void entityRemoved(EntityEvent event) {
        Long entityId = event.getEntity().getEntityId();
        Entity entity = entityService.getEntity(entityId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // Add by Rod 规范entityRemove用法， 删除CMTS设备处理相关逻辑
        if (entity != null && entityTypeService.isCmts(entity.getTypeId())) {
            try {
                cmcPerfService.stopCmStatusMonitor(entityId, snmpParam);
            } catch (Exception e) {
                logger.debug("", e);
            }
            try {
                cmtsPerfService.stopCmtsOnlineQuality(entityId, snmpParam);
            } catch (Exception e) {
                logger.debug("", e);
            }
            try {
                cmtsPerfService.stopCmtsSignalQuality(entityId, snmpParam);
            } catch (Exception e) {
                logger.debug("", e);
            }
            try {
                cmtsPerfService.stopCmtsFlowQuality(entityId, snmpParam);
            } catch (Exception e) {
                logger.debug("", e);
            }
            try {
                cmtsPerfService.stopCmtsServiceQuality(entityId, snmpParam);
            } catch (Exception e) {
                logger.debug("", e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.message.event.EntityListener#managerChanged(com.topvision.ems.message.event
     * .EntityEvent)
     */
    @Override
    public void managerChanged(EntityEvent event) {
    }

    /**
     * @return the entityService
     */
    public EntityService getEntityService() {
        return entityService;
    }

    /**
     * @param entityService
     *            the entityService to set
     */
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    /**
     * @return the cmcService
     */
    public CmcService getCmcService() {
        return cmcService;
    }

    /**
     * @param cmcService
     *            the cmcService to set
     */
    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public CmtsPerfService getCmtsPerfService() {
        return cmtsPerfService;
    }

    public void setCmtsPerfService(CmtsPerfService cmtsPerfService) {
        this.cmtsPerfService = cmtsPerfService;
    }

    /**
     * @return the messageService
     */
    public MessageService getMessageService() {
        return messageService;
    }

    /**
     * @param messageService
     *            the messageService to set
     */
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void entityReplaced(EntityEvent event) {
    }

}