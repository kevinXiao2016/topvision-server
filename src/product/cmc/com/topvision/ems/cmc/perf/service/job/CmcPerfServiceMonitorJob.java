/***********************************************************************
 * $Id: CmcPerfServiceMonitorJob.java,v1.0 2013-12-7 下午3:39:47 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.service.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.dao.PerformanceDao;
import com.topvision.ems.performance.service.PerformanceService;
import com.topvision.framework.scheduler.AbstractJob;

/**
 * @author Victor
 * @created @2013-12-7-下午3:39:47
 *
 */
public class CmcPerfServiceMonitorJob extends AbstractJob {

    /* (non-Javadoc)
     * @see com.topvision.framework.scheduler.AbstractJob#doJob(org.quartz.JobExecutionContext)
     */
    @Override
    public void doJob(JobExecutionContext ctx) throws JobExecutionException {
        PerformanceDao performanceDao = (PerformanceDao) jobDataMap.get("performanceDao");
        @SuppressWarnings("unchecked")
        PerformanceService<? extends OperClass> performanceService = (PerformanceService<? extends OperClass>) jobDataMap
                .get("performanceService");
        EntityService entityService = (EntityService) jobDataMap.get("entityService");
        List<ScheduleMessage<OperClass>> scheduleMessageList = performanceDao.selectByMap(null);
        for (ScheduleMessage<?> scheduleMessage : scheduleMessageList) {
            if (scheduleMessage.getCategory().equalsIgnoreCase("CC_CMSTATUS")
                    || scheduleMessage.getCategory().equalsIgnoreCase("CC_CCSPECTRUM")) {
                Entity entity = entityService.getEntity(scheduleMessage.getIdentifyKey());
                if (entity == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("stopMonitor ip[" + scheduleMessage.getSnmpParam().getIpAddress()
                                + "] IdentifyKey[" + scheduleMessage.getIdentifyKey() + "] Category["
                                + scheduleMessage.getCategory() + "] monitorId[" + scheduleMessage.getMonitorId() + "]");
                    }
                    performanceService.stopMonitor(scheduleMessage.getSnmpParam(), scheduleMessage.getMonitorId());
                }
            }
        }
    }
}
