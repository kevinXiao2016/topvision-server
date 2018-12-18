/***********************************************************************
 * $Id: ThreadPoolServiceImpl.java,v1.0 2015年5月30日 上午10:46:36 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.platform.service.ThreadPoolService;

/**
 * @author Victor
 * @created @2015年5月30日-上午10:46:36
 *
 */
@Service("threadPoolService")
public class ThreadPoolServiceImpl implements ThreadPoolService {
    protected static final Logger logger = LoggerFactory.getLogger(ThreadPoolServiceImpl.class);
    private ThreadPoolExecutor service = null;
    @Value("${ThreadPool.corePoolSize:50}")
    private Integer corePoolSize;
    @Value("${ThreadPool.maximumPoolSize:200}")
    private Integer maximumPoolSize;
    //单位是分钟
    @Value("${ThreadPool.keepAliveTime:60}")
    private Integer keepAliveTime;
    @Value("${ThreadPool.queueSize:65535}")
    private Integer queueSize;

    /**
     * 初始化
     */
    @PostConstruct
    public void initialize() {
        final ThreadGroup group = new ThreadGroup("ThreadPool");
        service = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(queueSize), new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(group, r);
                    }
                });
    }

    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        service.shutdown();
        service = null;
    }

    /**
     * @return the service
     */
    public ThreadPoolExecutor getService() {
        return service;
    }

    @Override
    public void execute(Runnable command) {
        service.execute(command);
    }

    @Override
    public List<Runnable> shutdownNow() {
        return service.shutdownNow();
    }

    @Override
    public int getCorePoolSize() {
        return service.getCorePoolSize();
    }

    @Override
    public int getMaximumPoolSize() {
        return service.getMaximumPoolSize();
    }

    @Override
    public BlockingQueue<Runnable> getQueue() {
        return service.getQueue();
    }

    @Override
    public int getPoolSize() {
        return service.getPoolSize();
    }

    @Override
    public int getActiveCount() {
        return service.getActiveCount();
    }

    @Override
    public int getLargestPoolSize() {
        return service.getLargestPoolSize();
    }

    @Override
    public long getTaskCount() {
        return service.getTaskCount();
    }

    @Override
    public long getCompletedTaskCount() {
        return service.getCompletedTaskCount();
    }
}
