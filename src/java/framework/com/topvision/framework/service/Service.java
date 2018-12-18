/*
 * @(#)Service.java
 *
 * Copyright 2011 Topoview All rights reserved.
 */
package com.topvision.framework.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 业务层标志性接口.
 * 
 * @author Victor
 * @version 1.0, 2007-08-23
 */
public interface Service {
    /**
     * 某个业务层Bean被实例化的时候马上调用此方法.此方法仅进行初始化，不调用采集轮询等任务，以加快服务启动速度
     */
    @PostConstruct
    public void initialize();

    /**
     * 某个业务层Bean被销毁的时候马上调用此方法.
     */
    @PreDestroy
    public void destroy();

    /**
     * 在spring初始化完bean以后调用，也可以通过dd.tv来进行开启任务和停止任务
     */
    public void start();

    /**
     * 停止start启动的任务
     */
    public void stop();
}
