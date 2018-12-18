/***********************************************************************
 * $ Cmc8800BSnapChange.java,v1.0 2012-9-25 11:56:01 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance;

import java.util.List;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.perf.dao.CmcPerfDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author jay
 * @created @2012-9-25-11:56:01
 * 
 * 
 *          Modify by Rod
 * 
 *          Drop list listener in Performance
 */
public class Cmc8800BSnapChange implements EntityValueListener {
    private EntityTypeService entityTypeService;
    private MessageService messageService;
    private EntityService entityService;
    private CmcService cmcService;
    private CmcPerfDao cmcPerfDao;
    private CmcDao cmcDao;

    public void init() {
        messageService.addListener(EntityValueListener.class, this);
    }

    /**
     * 告警状态变化
     * 
     * @param event
     *            event
     */
    public void alertChanged(EntityValueEvent event) {
    }

    /**
     * 设备的CPU、MEM等负载变化
     * 
     * @param event
     *            event
     */
    public void performanceChanged(EntityValueEvent event) {
    }

    /**
     * 在线状态变化
     * 
     * @param event
     *            event
     */
    public void stateChanged(EntityValueEvent event) {
        // Modified by flackyang CMC状态变更时间由数据库触发器维护 2016-01-12
        if (!event.isState()) {
            Entity entity = entityService.getEntity(event.getEntityId());
            if (entity != null) {
                if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
                    // 8800B
                    Long cmcId = entity.getEntityId();
                    cmcPerfDao.changeCmc8800BStatus(cmcId, false);
                    cmcPerfDao.changeCmStatusOffine(cmcId);
                } else if (entityTypeService.isOlt(entity.getTypeId())) {
                    // 8800A
                    List<Long> cmcIds = cmcService.getCmcIdsByEntityId(entity.getEntityId());
                    for (Long cmcId : cmcIds) {
                        cmcPerfDao.changeCmc8800BStatus(cmcId, false);
                        cmcPerfDao.changeCmStatusOffine(cmcId);
                    }
                } else if (entityTypeService.isCmts(entity.getTypeId())) {
                    // CMTS
                    Long cmcId = cmcService.getCmcIdByEntityId(entity.getEntityId());
                    cmcPerfDao.changeCmc8800BStatus(cmcId, false);
                    cmcPerfDao.changeCmStatusOffine(cmcId);
                }
            }
        }
        // To change body of implemented methods use File | Settings | File Templates.
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public CmcPerfDao getCmcPerfDao() {
        return cmcPerfDao;
    }

    public void setCmcPerfDao(CmcPerfDao cmcPerfDao) {
        this.cmcPerfDao = cmcPerfDao;
    }

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public CmcDao getCmcDao() {
        return cmcDao;
    }

    public void setCmcDao(CmcDao cmcDao) {
        this.cmcDao = cmcDao;
    }

    public EntityTypeService getEntityTypeService() {
        return entityTypeService;
    }

    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }
}
