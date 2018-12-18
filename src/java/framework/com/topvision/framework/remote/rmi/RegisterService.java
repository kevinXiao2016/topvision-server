/*
 * @(#)RegisterService.java
 *
 * Copyright 2011 Topoview All rights reserved.
 */
package com.topvision.framework.remote.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * RMI远程注册接口.
 * 
 * @author niejun
 */
public interface RegisterService extends Remote {
    /**
     * 得到所有的服务映射, key表示服务名称, value表示服务的接口实现.
     * 
     * @return
     * @throws RemoteException
     */
    Map<String, String> getServiceInterfaces() throws RemoteException;

    /**
     * 测试远程连接是否连通.
     * 
     * @throws RemoteException
     */
    void ping() throws RemoteException;

    /**
     * 创建某个客户端的服务执行接口, 用于所有远程服务的获取.
     * 
     * @param executor
     *            当前客户端执行者, 又服务端保存并使用.
     * @return 客户端使用服务执行者
     * @throws RemoteException
     */
    ServiceExecutor buildServiceExecutor(ClientExecutor executor) throws RemoteException;
}
