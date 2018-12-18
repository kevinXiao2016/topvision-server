/*
 * @(#)ServiceExecutor.java
 *
 * Copyright 2011 Topoview All rights reserved
 */

package com.topvision.framework.remote.rmi;

import java.rmi.Remote;

import com.topvision.framework.exception.service.RmiException;

/**
 * 客户端调用服务端方法的服务执行者.
 * 
 * @author niejun
 */
public interface ServiceExecutor extends Remote {
    /**
     * 执行给定服务名称和方法的远程服务.
     * 
     * @param serviceName
     * @param method
     * @param paramTypes
     * @param args
     * @return
     * @throws Exception
     */
    Object execute(String serviceName, String method, Class<?>[] paramTypes, Object[] args) throws RmiException;
}
