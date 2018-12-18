/*
 * @(#)ClientExecutor.java
 *
 * Copyright 2011 Topoview All rights reserved
 */

package com.topvision.framework.remote.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 客户端远程执行者, 提供服务端代理回调客户端方法.
 * 
 * @author niejun
 */
public interface ClientExecutor extends Remote {
    /**
     * 执行映射到Client的ClientCallback上的方法.
     * 
     * @param key
     *            ClientCallback的key值
     * @param method
     *            方法名字
     * @param paramTypes
     *            方法的参数类型
     * @param args
     *            方法的参数
     * 
     * @return 执行的结果.
     * 
     * @throws Throwable
     */
    public Object invokeClientCallback(Integer key, String method, Class<?>[] paramTypes, Object[] args)
            throws RemoteException;

    /**
     * 当Server对应的ClientCallback被回收时,通知Client回收.
     * 
     * @param key
     *            ClientCallback的key值
     * 
     * @throws RemoteException
     */
    void finalizeClientCallback(Integer key) throws RemoteException;
}
