/***********************************************************************
 * $Id: PerfSnmpExecutorService.java,v1.0 Aug 25, 2016 5:36:54 PM $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;

import com.topvision.framework.annotation.Engine;

/**
 * @author Victor
 * @created @Aug 25, 2016-5:36:54 PM
 *
 */
@Engine("perfSnmpExecutorService")
public class PerfSnmpExecutorService extends AbstractSnmpExecutorService {
    @Value("${Perf.SnmpPool.corePoolSize:256}")
    private Integer corePoolSize;
    @Value("${Perf.SnmpPool.maximumPoolSize:512}")
    private Integer maximumPoolSize;
    @Value("${Perf.SnmpPool.keepAliveTime:30}")
    private Integer keepAliveTime;
    @Value("${Perf.SnmpPool.queueSize:65535}")
    private Integer queueSize;

    /**
     * 初始化
     */
    @PostConstruct
    public void initialize() {
        service = new SnmpThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(queueSize), new SnmpThreadFactory("PerfSnmpThreadGroup"));
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
