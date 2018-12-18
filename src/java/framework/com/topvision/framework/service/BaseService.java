/*
 * @(#)BaseService.java
 *
 * Copyright 2011 Topoview All rights reserved.
 */
package com.topvision.framework.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务层基类.
 * 
 * @author niejun
 * @version 1.0, 2007-08-23
 */
public abstract class BaseService implements Service {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @see com.topvision.framework.service.Service#initialize()
     */
    @PostConstruct
    public void initialize() {
        if (logger.isInfoEnabled()) {
            logger.info("Invoke " + getClass() + " initialize method...");
        }
    }

    /**
     * @see com.topvision.framework.service.Service#destroy()
     */
    @PreDestroy
    public void destroy() {
        if (logger.isInfoEnabled()) {
            logger.info("Invoke " + getClass() + " destroy method...");
        }
    }

    /**
     * @see com.topvision.framework.service.Service#start()
     */
    public void start() {
    }

    /**
     * @see com.topvision.framework.service.Service#stop()
     */
    public void stop() {
    }

    /**
     * 得到系统运行的日志记录器.
     * 
     * @return Logger
     */
    public final Logger getLogger() {
        return logger;
    }
}
