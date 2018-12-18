/***********************************************************************
 * $Id: NbiSupportService.java,v1.0 2016年3月14日 上午10:58:50 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.nbi;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import com.topvision.ems.facade.nbi.NbiAddress;
import com.topvision.framework.annotation.Engine;
import com.topvision.performance.nbi.api.Engine2NbiInvoke;

/**
 * @author Bravin
 * @created @2016年3月14日-上午10:58:50
 *
 */
@Engine("nbiSupportService")
public class NbiSupportService {
    private static final int MAX_ALLOWD_DISCONNECTED_TIMES = 3;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private NbiAddress nbiAddress;
    private boolean allowDataRedirect;
    private Engine2NbiInvoke engine2NbiInvoke;
    private final AtomicInteger disconnecCounter = new AtomicInteger();

    /**
     * 传递数据到NBI
     * @param oid
     * @param value
     * @param ip
     * @param entityId
     * @param index
     * @throws RemoteException 
     */
    public void redirect(String oid, Object value, String ip, Long entityId, Long index) throws RemoteException {
        if (logger.isDebugEnabled()) {
            logger.debug("redirect:{}", oid);
        }
        if (!allowDataRedirect) {
            return;
        }
        if (ip == null) {
            logger.error("Ip can not be null!");
            return;
        }
        try {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("oid", oid);
            hashMap.put("value", value);
            hashMap.put("ip", ip);
            hashMap.put("entityId", entityId);
            hashMap.put("index", index);
            engine2NbiInvoke.commitNbiPerfData(hashMap);
            disconnecCounter.set(0);
        } catch (Exception e) {
            logger.error("redirect perf nbi data error:{}", e);
            if (disconnecCounter.incrementAndGet() == MAX_ALLOWD_DISCONNECTED_TIMES) {
                logger.error("Engine connect nbi faild!auto stop redirect data!");
                allowDataRedirect = false;
            }
        }
    }

    public NbiAddress getNbiAddress() {
        return nbiAddress;
    }

    public void setNbiAddress(NbiAddress nbiAddress) {
        this.nbiAddress = nbiAddress;
    }

    public boolean isAllowDataRedirect() {
        return allowDataRedirect;
    }

    public void setAllowDataRedirect(boolean allowDataRedirect) {
        this.allowDataRedirect = allowDataRedirect;
        engine2NbiInvoke = getNbiService(Engine2NbiInvoke.class);
    }

    protected <T> T getNbiService(Class<T> clazz) {
        return proxy(clazz, nbiAddress.getNbiAddress(), nbiAddress.getNbiPort(), clazz.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    private <T> T proxy(Class<T> clazz, String ip, int port, String name) {
        String url = "rmi://" + ip + ":" + port + "/" + name;
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
