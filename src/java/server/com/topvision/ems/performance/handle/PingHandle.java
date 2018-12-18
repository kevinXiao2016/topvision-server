/***********************************************************************
 * $Id: PingHandle.java,v1.0 2015年6月23日 上午11:02:19 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.handle;

import java.sql.Timestamp;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.performance.dao.MonitorDao;
import com.topvision.ems.performance.domain.MonitorValue;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.domain.PingResult;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Victor
 * @created @2015年6月23日-上午11:02:19
 *
 */
@Service("pingHandle")
public class PingHandle extends PerformanceHandle {
    public static String HANDLE_ID = "PING";
    @Autowired
    private MessageService messageService;
    @Autowired
    private MonitorDao monitorDao;

    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(HANDLE_ID, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(HANDLE_ID);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        return HANDLE_ID;
    }

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        return null;
    }

    @Override
    public void handle(PerformanceData data) {
        PingResult performanceResult = (PingResult) data.getPerfData();
        Long entityId = performanceResult.getEntityId();
        Integer delay = performanceResult.getDelay();
        Timestamp collectTime = performanceResult.getCollectTime();
        // EntitySnap
        try {
            EntitySnap snap = null;
            snap = new EntitySnap();
            snap.setEntityId(entityId);
            snap.setDelay(delay);
            snap.setState(delay >= 0);
            snap.setSnapTime(collectTime);
            entityDao.updateEntityState(snap);
        } catch (Exception e) {
            logger.error(entityId + " updateEntityState error ", e);
        }
        // StateChange
        EntityValueEvent event = new EntityValueEvent(entityId);
        event.setEntityId(entityId);
        event.setState(delay >= 0);
        event.setDelay(delay);
        event.setActionName("stateChanged");
        event.setListener(EntityValueListener.class);
        messageService.addMessage(event);
        // Connectivity
        try {
            MonitorValue value = new MonitorValue();
            value.setCollectTime(collectTime);
            value.setItemIndex(0);
            value.setItemValue(Double.valueOf(delay));
            value.setEntityId(entityId);
            value.setItemName("ping");
            monitorDao.insertConnectivity(value);
        } catch (Exception e) {
            logger.error(entityId + " insertConnectivity error ", e);
        }
    }
}