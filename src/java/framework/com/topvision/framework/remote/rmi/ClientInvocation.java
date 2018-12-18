/*
 * @(#)ServerInvocation.java
 *
 * Copyright 2011 Topoview All rights reserved
 */

package com.topvision.framework.remote.rmi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端调用服务器端方法的代理处理器.
 * 
 * @author niejun
 */
class ClientInvocation implements InvocationHandler {
    /**
     * 用于每次服务器进行的回调客户端的编号.
     */
    public static int callbackId = 1;
    /**
     * 保存所有的ClientCallback.
     */
    public static Map<Integer, Object> callbacks = new HashMap<Integer, Object>();
    /**
     * 远程服务执行者.
     */
    private ServiceExecutor serviceExecutor;
    /**
     * 远程执行的服务名.
     */
    private String serviceName;

    /**
     * 构造方法.
     * 
     * @param locator
     *            远程服务执行者.
     * @param name
     *            远程执行的服务名.
     * @param registry
     *            注册对象.
     */
    public ClientInvocation(ServiceExecutor executor, String name, Registry registry) {
        this.serviceExecutor = executor;
        this.serviceName = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method,
     * java.lang.Object[])
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Class<?>[] clazzs = method.getParameterTypes();
        for (int i = clazzs.length - 1; i > -1; i--) {
            if (ClientCallback.class.isAssignableFrom(clazzs[i])) {
                synchronized (callbacks) {
                    callbacks.put(new Integer(callbackId), args[i]);
                    args[i] = new Integer(callbackId++);
                }
            }
        }
        return serviceExecutor.execute(serviceName, method.getName(), clazzs, args);
    }
}
