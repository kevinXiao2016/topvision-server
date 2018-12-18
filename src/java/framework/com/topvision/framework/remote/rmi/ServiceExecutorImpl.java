/*
 * @(#)ServiceExecutorImpl.java
 *
 * Copyright 2011 Topoview All rights reserved
 */

package com.topvision.framework.remote.rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.exception.service.RmiException;
import com.topvision.framework.service.ServiceLocator;

/**
 * 远程服务执行者的实现.
 * 
 * @author niejun
 */
public class ServiceExecutorImpl extends UnicastRemoteObject implements ServiceExecutor, Unreferenced {
    private static final long serialVersionUID = -8066777785247501284L;
    private static Logger logger = LoggerFactory.getLogger(ServiceExecutorImpl.class);
    private ServiceLocator serviceLocator = null;
    // 远程客户端执行者, 用于服务器对客户端的回调处理
    private ClientExecutor clientExecutor = null;

    public ServiceExecutorImpl(ClientExecutor executor, ServiceLocator locator) throws RemoteException {
        super();
        this.clientExecutor = executor;
        this.serviceLocator = locator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.remote.rmi.ServiceExecutor#execute(java.lang.String,
     * java.lang.String, java.lang.Class<?>[], java.lang.Object[])
     */
    @Override
    public Object execute(String serviceName, String method, Class<?>[] paramTypes, Object[] args) throws RmiException {
        Object service = serviceLocator.getService(serviceName);
        Method m = null;
        for (int i = paramTypes.length - 1; i > -1; i--) {
            if (ClientCallback.class.isAssignableFrom(paramTypes[i]) && args[i] instanceof Integer) {
                args[i] = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { paramTypes[i] },
                        new ServerInvocation(clientExecutor, (Integer) args[i]));
            }
        }
        try {
            m = service.getClass().getMethod(method, paramTypes);
            return m.invoke(service, args);
        } catch (SecurityException e) {
            throw new RmiException(e);
        } catch (IllegalArgumentException e) {
            throw new RmiException(e);
        } catch (NoSuchMethodException e) {
            throw new RmiException(e);
        } catch (IllegalAccessException e) {
            throw new RmiException(e);
        } catch (InvocationTargetException e) {
            throw new RmiException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.rmi.server.Unreferenced#unreferenced()
     */
    @Override
    public void unreferenced() {
        logger.info("ServiceExecutorImpl Remote server object is unreferenced!");
        try {
            unexportObject(this, true);
        } catch (NoSuchObjectException nsoe) {
            logger.error("unexportObject ServiceExecutorImpl error:", nsoe);
        }
    }
}
