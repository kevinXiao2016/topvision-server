/***********************************************************************
 * $ PerformanceFacadeImpl.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor.performance;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.common.EngineThreadPool;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.engine.performance.PerfExecutorService;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.ems.facade.performance.PerformanceFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.common.CollectTimeUtil;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
@Facade("performanceFacade")
public class PerformanceFacadeImpl extends EmsFacade implements PerformanceFacade {
    @Autowired
    private PerfExecutorService perfExecutorService;
    @Autowired
    private EngineThreadPool engineThreadPool;

    @Override
    public Integer invoke(ScheduleMessage<OperClass> message) {
        logger.info("invoked by server to execute:{}", message);
        return perfExecutorService.invoke(message);
    }

    @Override
    public void clear() {
        logger.info("PerformanceFacade.clear");
        perfExecutorService.clear();
    }

    @Override
    public void createCollectTimeUtil(String name, Long startTime, Long period) {
        CollectTimeUtil.createCollectTimeUtil(name, startTime, period);
    }

    public PerfExecutorService getPerfExecutorService() {
        return perfExecutorService;
    }

    public void setPerfExecutorService(PerfExecutorService perfExecutorService) {
        this.perfExecutorService = perfExecutorService;
    }

    @Override
    public Map<String, String> getStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("fileCache", String.valueOf(!perfExecutorService.isNoFileCache()));
        AtomicInteger counts = new AtomicInteger();
        doFileCounts(new File("./cache"), counts);
        status.put("fileCacheSize", String.valueOf(counts.get()));
        status.put("schedulerSize", String.valueOf(perfExecutorService.getScheduledFutureConcurrentHashMaps().size()));
        ScheduledThreadPoolExecutor executor = perfExecutorService.getScheduledThreadPoolExecutor();
        status.put("schedulerStatus",
                executor.getActiveCount() + "/" + executor.getPoolSize() + "/" + executor.getQueue().size());
        status.put("threadpool", engineThreadPool.getActiveCount() + "/" + engineThreadPool.getPoolSize() + "/"
                + engineThreadPool.getQueue().size());
        return status;
    }

    private void doFileCounts(File dir, AtomicInteger counts) {
        if (dir == null || dir.isFile()) {
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                counts.incrementAndGet();
            } else if (file.isDirectory()) {
                doFileCounts(file, counts);
            }
        }
    }
}