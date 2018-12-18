/***********************************************************************
 * $Id: NbiRmiService.java,v1.0 2016年3月28日 下午2:46:04 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.rmi;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Service;

/**
 * @author Bravin
 * @created @2016年3月28日-下午2:46:04
 *
 */
@Service
public class NbiRmiService {
    public <T> T getNbiService(Class<T> clazz, String ip, int port) {
        return proxy(clazz, ip, port, clazz.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    private <T> T proxy(Class<T> clazz, String ip, int port, String name) {
        String url = "rmi://" + ip + ":" + port + "/" + clazz.getSimpleName();
        RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
        proxy.setServiceUrl(url);
        proxy.setServiceInterface(clazz);
        proxy.setLookupStubOnStartup(false);
        proxy.setCacheStub(true);
        proxy.setRefreshStubOnConnectFailure(true);
        proxy.afterPropertiesSet();
        return (T) proxy.getObject();
    }
}
