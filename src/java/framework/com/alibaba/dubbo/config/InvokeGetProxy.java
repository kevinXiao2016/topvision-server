/***********************************************************************
 * $Id: InvokeGetProxy.java,v1.0 2016年10月19日 下午5:59:20 $
 * 
 * @author: Victorli
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.alibaba.dubbo.config;

/**
 * @author Victorli
 * @created @2016年10月19日-下午5:59:20
 *
 */
public class InvokeGetProxy {
    /**
     * ReferenceConfig 中getInvoker是包内访问权限，此类解决访问权限问题
     * 
     * @param config
     * @return
     */
    public static String getInvoker(ReferenceConfig<?> config) {
        return config.getInvoker().toString();
    }
}
