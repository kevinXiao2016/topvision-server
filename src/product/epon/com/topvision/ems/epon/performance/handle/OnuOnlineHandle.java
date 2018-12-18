/***********************************************************************
 * $Id: OnuOnlineHandle.java,v1.0 2015年6月23日 上午11:38:30 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.handle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.performance.dao.OnuPerfDao;
import com.topvision.ems.epon.performance.domain.OnuOnlineResult;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Victor
 * @created @2015年6月23日-上午11:38:30
 *
 */
@Service("onuOnlineHandle")
public class OnuOnlineHandle extends PerformanceHandle {
    public static String HANDLE_ID = "ONU_ONLINE";
    @Autowired
    private MessageService messageService;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private OnuPerfDao onuPerfDao;

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
        OnuOnlineResult performanceResult = (OnuOnlineResult) data.getPerfData();

        Long onuId = performanceResult.getOnuId();
        // EntitySnap
        try {
            EntitySnap snap = new EntitySnap();
            snap.setEntityId(onuId);
            snap.setDelay(-1);
            snap.setState(performanceResult.getOnuOnlneStatus() == 1);
            snap.setSnapTime(performanceResult.getCollectTime());
            entityDao.updateEntityState(snap);
            //更新ONU状态
            onuDao.updateOnuOperationStatus(onuId, performanceResult.getOnuOnlneStatus());
        } catch (Exception e) {
            logger.error(onuId + " updateEntityState error ", e);
        }
        // StateChange
        EntityValueEvent event = new EntityValueEvent(onuId);
        event.setEntityId(onuId);
        event.setState(performanceResult.getOnuOnlneStatus() == 1);
        event.setActionName("stateChanged");
        event.setListener(EntityValueListener.class);
        messageService.addMessage(event);
        //onu onlinestatus history
        try {
            onuPerfDao.insertOnuOnlineStatus(performanceResult);
        } catch (Exception e) {
            logger.error("insertOnuOnlineStatus failed", e);
        }
    }
}
