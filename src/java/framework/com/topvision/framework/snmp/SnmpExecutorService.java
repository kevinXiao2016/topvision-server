/***********************************************************************
 * $Id: SnmpExecutorService.java,v 1.1 May 24, 2008 7:48:11 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.snmp4j.SNMP4JSettings;
import org.springframework.beans.factory.annotation.Value;

import com.topvision.framework.annotation.Engine;

/**
 * @Create Date May 24, 2008 7:48:11 PM
 * 
 * @author kelers
 * 
 */
@Engine("snmpExecutorService")
public class SnmpExecutorService extends AbstractSnmpExecutorService {
    @Value("${SnmpPool.corePoolSize:64}")
    private Integer corePoolSize;
    @Value("${SnmpPool.maximumPoolSize:128}")
    private Integer maximumPoolSize;
    @Value("${SnmpPool.keepAliveTime}")
    private Integer keepAliveTime;
    @Value("${SnmpPool.queueSize:65535}")
    private Integer queueSize;

    /**
     * 初始化
     */
    @PostConstruct
    public void initialize() {
        service = new SnmpThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(queueSize), new SnmpThreadFactory());
        SNMP4JSettings.setThreadFactory(new MyThreadFactory());
    }

    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        service.shutdown();
        service = null;
    }

    /**
     * @return the corePoolSize
     */
    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    /**
     * @param corePoolSize
     *            the corePoolSize to set
     */
    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    /**
     * @return the maximumPoolSize
     */
    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    /**
     * @param maximumPoolSize
     *            the maximumPoolSize to set
     */
    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    /**
     * @return the keepAliveTime
     */
    public Integer getKeepAliveTime() {
        return keepAliveTime;
    }

    /**
     * @param keepAliveTime
     *            the keepAliveTime to set
     */
    public void setKeepAliveTime(Integer keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    /**
     * @return the queueSize
     */
    public Integer getQueueSize() {
        return queueSize;
    }

    /**
     * @param queueSize
     *            the queueSize to set
     */
    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }
}
