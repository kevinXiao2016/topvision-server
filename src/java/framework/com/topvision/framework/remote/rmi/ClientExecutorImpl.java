/*
 * @(#)ClientExecutorImpl.java
 *
 * Copyright 2011 Topoview All rights reserved
 */

package com.topvision.framework.remote.rmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端对客户端进行远程调用的执行者实现.
 * 
 * @author niejun
 */
public class ClientExecutorImpl extends UnicastRemoteObject implements ClientExecutor, Unreferenced {
    private static final long serialVersionUID = -3169344934275142554L;
    private static Logger logger = LoggerFactory.getLogger(ClientExecutorImpl.class);

    /**
     * 
     * @throws RemoteException
     */
    public ClientExecutorImpl() throws RemoteException {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.framework.remote.rmi.ClientExecutor#invokeClientCallback(java.lang.Integer,
     * java.lang.String, java.lang.Class<?>[], java.lang.Object[])
     */
    @Override
    public Object invokeClientCallback(Integer key, String method, Class<?>[] paramTypes, Object[] args)
            throws RemoteException {
        Object callback = null;
        synchronized (ClientInvocation.callbacks) {
            callback = ClientInvocation.callbacks.get(key);
            if (callback == null) {
                throw new NoSuchObjectException(key.toString());
            }
        }
        try {
            return callback.getClass().getMethod(method, paramTypes).invoke(callback, args);
        } catch (IllegalArgumentException e) {
            throw new RemoteException(e.getMessage(), e);
        } catch (SecurityException e) {
            throw new RemoteException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RemoteException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new RemoteException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.framework.remote.rmi.ClientExecutor#finalizeClientCallback(java.lang.Integer)
     */
    @Override
    public void finalizeClientCallback(Integer key) throws RemoteException {
        synchronized (ClientInvocation.callbacks) {
            ClientInvocation.callbacks.remove(key);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.rmi.server.Unreferenced#unreferenced()
     */
    @Override
    public void unreferenced() {
        logger.info("ClientExecutorImpl Remote client object is unreferenced!");
        try {
            unexportObject(this, true);
        } catch (NoSuchObjectException nsoe) {
            logger.info("unexportObject ClientExecutorImpl error:", nsoe);
        }
    }
}
