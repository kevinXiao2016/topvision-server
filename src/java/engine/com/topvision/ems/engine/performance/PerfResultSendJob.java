/***********************************************************************
 * $ PerfResultSend.java,v1.0 2012-5-6 9:59:09 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author jay
 * @created @2012-5-6-9:59:09
 */
public class PerfResultSendJob implements Runnable {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private PerformanceResult<OperClass> performanceResult;
    private PerfEngineSaver<PerformanceResult<OperClass>, OperClass> dbSaver;

    public PerfResultSendJob(PerformanceResult<OperClass> performanceResult,
            PerfEngineSaver<PerformanceResult<OperClass>, OperClass> dbSaver) {
        this.performanceResult = performanceResult;
        this.dbSaver = dbSaver;
    }

    @Override
    public void run() {
        // Modify by Victor@20151222增加线程的名称
        String name = Thread.currentThread().getName();
        try {
            if(performanceResult != null){
                Thread.currentThread().setName(performanceResult.getEntityId() + "-" + performanceResult.getFileName());
                dbSaver.save(performanceResult);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        Thread.currentThread().setName(name);
    }
}
