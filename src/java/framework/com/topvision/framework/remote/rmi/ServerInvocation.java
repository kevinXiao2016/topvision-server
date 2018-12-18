/*
 * @(#)ClientInvocation.java
 *
 * Copyright 2011 Topoview All rights reserved
 */

package com.topvision.framework.remote.rmi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 服务器端调用客户端方法的处理器.
 * 
 * @author niejun
 */
class ServerInvocation implements InvocationHandler {
    private Integer callbackId;
    private ClientExecutor clientExecutor = null;

    /**
     * 
     * @param handler
     * @param id
     */
    public ServerInvocation(ClientExecutor executor, Integer id) {
        this.clientExecutor = executor;
        this.callbackId = id;
    }

    /**
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        clientExecutor.finalizeClientCallback(callbackId);
    }

    /**
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method,
     *      java.lang.Object[])
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String mn = method.getName();
        if (mn.equals("clone")) {
            if (method.getParameterTypes().length == 0) {
                throw new CloneNotSupportedException();
            }
        } else if (mn.equals("equals")) {
            if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0].equals(Object.class)) {
                Object obj = args[0];
                if (obj == null || !Proxy.isProxyClass(obj.getClass()) || !this.equals(Proxy.getInvocationHandler(obj))) {
                    return Boolean.FALSE;
                } else {
                    return Boolean.TRUE;
                }
            }
        } else if (mn.equals("finalize")) {
            if (method.getParameterTypes().length == 0) {
                this.finalize();
            }
        } else if (mn.equals("getClass")) {
            if (method.getParameterTypes().length == 0) {
                return ServerInvocation.class;
            }
        } else if (mn.equals("hashCode")) {
            if (method.getParameterTypes().length == 0) {
                return callbackId;
            }
        } else if (mn.equals("notify")) {
            if (method.getParameterTypes().length == 0) {
                this.notify();
                return null;
            }
        } else if (mn.equals("notifyAll")) {
            if (method.getParameterTypes().length == 0) {
                this.notifyAll();
                return null;
            }
        } else if (mn.equals("toString")) {
            if (method.getParameterTypes().length == 0) {
                return "ClientCallback[" + callbackId + "] at " + callbackId;
            }
        } else if (mn.equals("wait")) {
            if (method.getParameterTypes().length == 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                }
                return null;
            } else if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0].equals(Long.TYPE)) {
                try {
                    this.wait(((Long) args[0]).longValue());
                } catch (InterruptedException e) {
                }
                return null;
            } else if (method.getParameterTypes().length == 2 && method.getParameterTypes()[0].equals(Long.TYPE)
                    && method.getParameterTypes()[1].equals(Integer.TYPE)) {
                try {
                    this.wait(((Long) args[0]).longValue(), ((Integer) args[1]).intValue());
                } catch (InterruptedException e) {
                }
                return null;
            }
        }
        return clientExecutor.invokeClientCallback(callbackId, method.getName(), method.getParameterTypes(), args);
    }
}
