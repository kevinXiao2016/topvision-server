/*
 * @(#)ServiceLocator.java
 *
 * Copyright 2011 Topoview All rights reserved
 */

package com.topvision.framework.service;

import java.util.Map;

import com.topvision.framework.exception.service.ServiceNotFoundException;

/**
 * 服务定位器.
 * 
 * @author niejun
 */
public interface ServiceLocator {

    /**
     * 获取给定服务名称的服务对象.
     * 
     * @param name
     * @return
     * @throws ServiceNotFoundException
     */
    Object getService(String name) throws ServiceNotFoundException;

    /**
     * 获取服务映射集合, key为服务名称, value为服务接口定义.
     * 
     * @return
     */
    Map<String, String> getServiceInterfaces();

}
