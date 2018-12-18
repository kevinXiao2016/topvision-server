/***********************************************************************
 * $Id: PingExecutorService.java,v 1.1 May 27, 2008 2:27:40 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.ping;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.topvision.framework.annotation.Engine;

/**
 * @Create Date May 27, 2008 2:27:40 PM
 * 
 * @author kelers
 * 
 */
@Engine("pingExecutorService")
public class PingExecutorService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private PingThreadPoolExecutor service = null;
    @Value("${PingPool.corePoolSize:32}")
    private Integer corePoolSize;
    @Value("${PingPool.maximumPoolSize:64}")
    private Integer maximumPoolSize;
    @Value("${PingPool.keepAliveTime:10}")
    private Integer keepAliveTime;
    @Value("${PingPool.queueSize:1024}")
    private Integer queueSize;

    @PostConstruct
    public void initialize() {
        PingThreadFactory pingThreadFactory = new PingThreadFactory();
        service = new PingThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(queueSize), pingThreadFactory);
    }

    public long getCompletedTaskCount() {
        return service.getCompletedTaskCount();
    }

    public <T> Future<T> submit(Runnable task, T result) {
        return service.submit(task, result);
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    /**
     * @return the service
     */
    public PingThreadPoolExecutor getService() {
        return service;
    }
}
