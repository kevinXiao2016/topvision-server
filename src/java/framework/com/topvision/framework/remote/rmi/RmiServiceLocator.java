/*
 * @(#)RmiServiceLocator.java
 *
 * Copyright 2011 Topoview All rights reserved
 */
package com.topvision.framework.remote.rmi;

import java.lang.reflect.Proxy;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.exception.service.ServiceNotFoundException;
import com.topvision.framework.service.ServiceLocator;

/**
 * RMI服务定位器实现, 用于客户端获取远程服务.
 * 
 * @author niejun
 */
public class RmiServiceLocator implements ServiceLocator {
    private static Logger logger = LoggerFactory.getLogger(RmiServiceLocator.class);
    private Map<String, Object> serviceInstances = new Hashtable<String, Object>();
    private Map<String, String> serviceInterfaces = new HashMap<String, String>();
    // 远程服务执行者
    private ServiceExecutor serviceExecutor = null;
    // 本地客户端执行者, 用于服务端调用客户端方法
    private ClientExecutor clientExecutor = null;

    /**
     * 默认构造器.
     */
    public RmiServiceLocator() {
    }

    /**
     * 
     * @param serviceExecutor
     * @param interfaces
     */
    public RmiServiceLocator(RegisterService registerService) throws RemoteException {
        setRegisterService(registerService);
    }

    /**
     * 
     * @param serviceExecutor
     */
    public void setServiceExecutor(ServiceExecutor serviceExecutor) {
        this.serviceExecutor = serviceExecutor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.ServiceLocator#getService(java.lang.String)
     */
    @Override
    public Object getService(String serviceName) throws ServiceNotFoundException {
        Object instance = serviceInstances.get(serviceName);
        if (instance == null) {
            String interfaceName = serviceInterfaces.get(serviceName);
            logger.info("Create Remote service proxy[" + serviceName + "] by " + interfaceName + ".");
            try {
                instance = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { getClass()
                        .getClassLoader().loadClass(interfaceName) }, new ClientInvocation(serviceExecutor,
                        serviceName, null));
            } catch (ClassNotFoundException cnfe) {
                throw new ServiceNotFoundException("Not found service by name: " + serviceName, cnfe);
            }
            serviceInstances.put(serviceName, instance);
        }
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.ServiceLocator#getServiceInterfaces()
     */
    @Override
    public Map<String, String> getServiceInterfaces() {
        return serviceInterfaces;
    }

    /**
     * 
     * @param registerService
     * @throws Exception
     */
    public void setRegisterService(RegisterService registerService) throws RemoteException {
        serviceInterfaces = registerService.getServiceInterfaces();
        // 提供给服务端的回调执行者
        if (clientExecutor == null) {
            clientExecutor = new ClientExecutorImpl();
        }
        // 提供给客户端的服务执行者
        serviceExecutor = registerService.buildServiceExecutor(clientExecutor);
    }
}
