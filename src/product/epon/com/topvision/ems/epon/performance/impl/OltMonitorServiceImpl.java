/***********************************************************************
 * $Id: OltMonitorServiceImpl.java,v1.0 2011-10-11 下午09:35:21 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.impl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.performance.job.OnuMonitorJob;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.performance.dao.MonitorDao;
import com.topvision.ems.performance.domain.Monitor;
import com.topvision.ems.performance.message.MonitorEvent;
import com.topvision.ems.performance.message.MonitorListener;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.platform.message.event.EntityAdapter;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.event.EntityTypeChangeEvent;
import com.topvision.platform.message.event.EntityTypeChangeListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Victor
 * @created @2011-10-11-下午09:35:21
 * 
 */
@Service("oltMonitorService")
public class OltMonitorServiceImpl extends EntityAdapter implements EntityTypeChangeListener {
    public static final String MONITOR_OLT = "olt";
    public static final String MONITOR_ONU = "onu";
    @Autowired
    private MonitorDao monitorDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Value("${OltMonitorJob.normal.interval}")
    private Long oltMonitorJobTime;
    @Value("${OnuMonitorJob.normal.interval}")
    private Long onuMonitorJobTime;

    /**
     * initialize
     */
    @PostConstruct
    public void initialize() {
         //messageService.addListener(EntityListener.class, this);
         //messageService.addListener(EntityTypeChangeListener.class, this);
    }

    @PreDestroy
    public void destroy() {
         //messageService.removeListener(EntityListener.class, this);
         //messageService.addListener(EntityTypeChangeListener.class, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.message.event.EntityAdapter#entityAdded(com.topvision.ems.message.event
     * .EntityEvent)
     */
    @Override
    public void entityAdded(EntityEvent event) {
        Entity entity = event.getEntity();
        if (entity.getIp() == null || entity.getIp().equals("0.0.0.0")) {
            return;
        }
        EntityType type = entityTypeService.getEntityType(entity.getTypeId());
        if (!type.getModule().equals("olt")) {
            return;
        }
        logger.debug("Olt monitor added");
        //Monitor oltMo = null;
        Monitor onuMo = null;
        /*if (!monitorDao.existMonitor(entity.getEntityId(), MONITOR_OLT)) {
            oltMo = new Monitor();
            oltMo.setCategory(MONITOR_OLT);
            oltMo.setIp(entity.getIp());
            oltMo.setName(entity.getIp());
            oltMo.setEntityId(entity.getEntityId());
            oltMo.setContent(entity.getIp().getBytes());
            oltMo.setIntervalOfNormal(oltMonitorJobTime);
            oltMo.setJobClass(OltMonitorJob.class.getName());
            oltMo.setIntervalStart(oltMonitorJobTime);
            monitorDao.insertEntity(oltMo);
            MonitorEvent evt = new MonitorEvent(oltMo);
            evt.setActionName("monitorAdded");
            evt.setListener(MonitorListener.class);
            evt.setMonitor(oltMo);
            messageService.addMessage(evt);
            if (logger.isDebugEnabled()) {
                logger.debug("[{}]created olt monitor successful!!!", entity.getIp());
            }
        }*/
        // create ONU AutoDiscovery Monitor
        if (!monitorDao.existMonitor(entity.getEntityId(), MONITOR_ONU)) {
            onuMo = new Monitor();
            onuMo.setCategory(MONITOR_ONU);
            onuMo.setIp(entity.getIp());
            onuMo.setName(entity.getIp());
            onuMo.setEntityId(entity.getEntityId());
            onuMo.setContent(entity.getIp().getBytes());
            onuMo.setIntervalOfNormal(onuMonitorJobTime);
            onuMo.setIntervalStart(onuMonitorJobTime);
            onuMo.setJobClass(OnuMonitorJob.class.getName());
            monitorDao.insertEntity(onuMo);
            MonitorEvent onuEvt = new MonitorEvent(onuMo);
            onuEvt.setActionName("monitorAdded");
            onuEvt.setListener(MonitorListener.class);
            onuEvt.setMonitor(onuMo);
            messageService.addMessage(onuEvt);
            if (logger.isDebugEnabled()) {
                logger.debug("[{}]created onu auto discovery monitor successful!!!", entity.getIp());
            }

        }
    }

    /**
     * @param monitorDao
     *            the monitorDao to set
     */
    public void setMonitorDao(MonitorDao monitorDao) {
        this.monitorDao = monitorDao;
    }

    /**
     * @param messageService
     *            the messageService to set
     */
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * @param entityTypeService
     *            the entityTypeService to set
     */
    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }

    /**
     * @return the oltMonitorJobTime
     */
    public Long getOltMonitorJobTime() {
        return oltMonitorJobTime;
    }

    /**
     * @param oltMonitorJobTime
     *            the oltMonitorJobTime to set
     */
    public void setOltMonitorJobTime(Long oltMonitorJobTime) {
        this.oltMonitorJobTime = oltMonitorJobTime;
    }

    /**
     * @return the onuMonitorJobTime
     */
    public Long getOnuMonitorJobTime() {
        return onuMonitorJobTime;
    }

    /**
     * @param onuMonitorJobTime
     *            the onuMonitorJobTime to set
     */
    public void setOnuMonitorJobTime(Long onuMonitorJobTime) {
        this.onuMonitorJobTime = onuMonitorJobTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.message.event.EntityTypeChangeListener#entityTypeChange(com.topvision.ems
     * .message.event.EntityTypeChangeEvent)
     */
    @Override
    public void entityTypeChange(EntityTypeChangeEvent event) {
        Entity entity = event.getEntity();
        if (entity.getIp() == null || entity.getIp().equals("0.0.0.0")) {
            return;
        }
        EntityType type = entityTypeService.getEntityType(event.getNewEntityTypeId());
        if (!type.getModule().equals("olt")) {
            return;
        }
        logger.debug("Olt monitor added");
        //Monitor oltMo = null;
        Monitor onuMo = null;
        /*if (!monitorDao.existMonitor(entity.getEntityId(), MONITOR_OLT)) {
            oltMo = new Monitor();
            oltMo.setCategory(MONITOR_OLT);
            oltMo.setIp(entity.getIp());
            oltMo.setName(entity.getIp());
            oltMo.setEntityId(entity.getEntityId());
            oltMo.setContent(entity.getIp().getBytes());
            oltMo.setJobClass(OltMonitorJob.class.getName());
            oltMo.setIntervalOfNormal(oltMonitorJobTime);
            monitorDao.insertEntity(oltMo);
            MonitorEvent evt = new MonitorEvent(oltMo);
            evt.setActionName("monitorAdded");
            evt.setListener(MonitorListener.class);
            evt.setMonitor(oltMo);
            messageService.addMessage(evt);
            if (logger.isDebugEnabled()) {
                logger.debug("[{}] created olt monitor successful!!!", entity.getIp());
            }
        }*/
        // create ONU AutoDiscovery Monitor
        if (!monitorDao.existMonitor(entity.getEntityId(), MONITOR_ONU)) {
            onuMo = new Monitor();
            onuMo.setCategory(MONITOR_ONU);
            onuMo.setIp(entity.getIp());
            onuMo.setName(entity.getIp());
            onuMo.setEntityId(entity.getEntityId());
            onuMo.setContent(entity.getIp().getBytes());
            onuMo.setIntervalOfNormal(onuMonitorJobTime);
            onuMo.setIntervalStart(onuMonitorJobTime);
            onuMo.setJobClass(OnuMonitorJob.class.getName());
            monitorDao.insertEntity(onuMo);
            MonitorEvent onuEvt = new MonitorEvent(onuMo);
            onuEvt.setActionName("monitorAdded");
            onuEvt.setListener(MonitorListener.class);
            onuEvt.setMonitor(onuMo);
            messageService.addMessage(onuEvt);
            if (logger.isDebugEnabled()) {
                logger.debug("[{}] created onu auto discovery monitor successful!!!", entity.getIp());
            }
        }
    }
}
