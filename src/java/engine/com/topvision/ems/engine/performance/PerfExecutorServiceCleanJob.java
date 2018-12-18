/***********************************************************************
 * $Id: PerfExecutorServiceCleanJob.java,v1.0 2013-12-7 下午4:01:08 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.performance;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.ems.engine.dao.EngineStatisticsDao;
import com.topvision.framework.scheduler.AbstractJob;

/**
 * @author Victor
 * @created @2013-12-7-下午4:01:08
 *
 */
public class PerfExecutorServiceCleanJob extends AbstractJob {
    private static long lastCompletedCount = 0L;

    @Override
    public void doJob(JobExecutionContext ctx) throws JobExecutionException {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) jobDataMap
                .get("scheduledThreadPoolExecutor");
        ScheduledThreadPoolExecutor realtimeScheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) jobDataMap
                .get("realtimeScheduledThreadPoolExecutor");
        EngineStatisticsDao engineStatisticsDao = (EngineStatisticsDao) jobDataMap.get("engineStatisticsDao");
        Integer poolSize = (Integer) jobDataMap.get("poolSize");
        Integer engineId = (Integer) jobDataMap.get("engineId");
        ConcurrentHashMap scheduledFutureConcurrentHashMaps = (ConcurrentHashMap) jobDataMap
                .get("scheduledFutureConcurrentHashMaps");
        if (logger.isDebugEnabled()) {
            logger.debug("scheduledThreadPoolExecutor thread pool size[" + poolSize + "]"
                    + " pool Queue size before purge[" + scheduledThreadPoolExecutor.getQueue().size() + "]");
        }
        scheduledThreadPoolExecutor.purge();
        realtimeScheduledThreadPoolExecutor.purge();
        if (logger.isDebugEnabled()) {
            logger.debug("scheduledThreadPoolExecutor thread pool size[" + poolSize + "]"
                    + " pool Queue size after purge[" + scheduledThreadPoolExecutor.getQueue().size() + "]");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("scheduledFutureConcurrentHashMaps size[" + scheduledFutureConcurrentHashMaps.size() + "]");
        }

        // add by fanzidong, 记录当前的scheduledThreadPoolExecutor的状态
        int activeCount = scheduledThreadPoolExecutor.getActiveCount();
        int queueSize = scheduledThreadPoolExecutor.getQueue().size();
        long completedTaskCount = scheduledThreadPoolExecutor.getCompletedTaskCount();

        ExecutorThreadSnap snap = new ExecutorThreadSnap(engineId, activeCount, poolSize, completedTaskCount - lastCompletedCount);
        engineStatisticsDao.insertExecutorSnap(snap);
        lastCompletedCount = completedTaskCount;
    }
}
