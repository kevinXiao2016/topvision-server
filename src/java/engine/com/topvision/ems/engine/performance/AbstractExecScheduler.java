/***********************************************************************
 * $Id: AbstractExecScheduler.java,v1.0 2013-12-27 下午2:59:29 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.facade.callback.CpuAndMemCallBack;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;
import com.topvision.ems.facade.exec.ExecScheduler;
import com.topvision.ems.facade.util.LocalFileData;
import com.topvision.framework.snmp.AbstractSnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2013-12-27-下午2:59:29
 *
 */
public abstract class AbstractExecScheduler<T extends OperClass> extends BaseEngine implements ExecScheduler<T> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected SnmpParam snmpParam;
    protected T operClass;
    protected PerformanceCallback performanceCallback;
    protected CpuAndMemCallBack cpuAndMemCallBack;
    @Autowired
    @Qualifier("perfSnmpExecutorService")
    protected AbstractSnmpExecutorService snmpExecutorService;
    @Autowired
    private PerfExecutorService perfExecutorService;

    public static final long EXEC_COST_THRESHOLD = 30000L;

    protected synchronized PerformanceCallback getCallback() {
        if (performanceCallback != null) {
            try {
                performanceCallback.test();
                return performanceCallback;
            } catch (Exception e) {
            }
        }
        try {
            performanceCallback = getCallback(PerformanceCallback.class);
            performanceCallback.test();
            return performanceCallback;
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equalsIgnoreCase("Property 'serviceUrl' is required")) {
                throw e;
            }
            return null;
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    @Override
    public void run() {
        // add by fanzidong,计算线程执行时间
        Long startTime = System.currentTimeMillis();
        operClass.setPreviousFireTime(System.currentTimeMillis());
        operClass.setNextFireTime(operClass.getPreviousFireTime());
        Thread.currentThread().setName(getClass().getSimpleName() + operClass.getEntityId());
        if (logger.isDebugEnabled()) {
            logger.debug("[{}] [perfmonitor execute start] happens. [entityId:{}].", getClass().getSimpleName(),
                    operClass.getEntityId());
        }
        ((OperClass) operClass).setIpAddress(snmpParam.getIpAddress());
        exec();
        if (logger.isDebugEnabled()) {
            logger.debug("[{}] [perfmonitor execute end] happens. [entityId:{}].", getClass().getSimpleName(),
                    operClass.getEntityId());
        }
        Long endTime = System.currentTimeMillis();
        Long costTime = endTime - startTime;
        if (logger.isDebugEnabled() || costTime > EXEC_COST_THRESHOLD) {
            // 如果执行时间超过了阈值，则记录
            logger.warn(
                    "[{}] [perfmonitor execute cost so much time] happens. [Probably Because Entity is busy or network slow]. [Probably need to check entity state]  [cost: {}ms, entityId:{}].",
                    getClass().getSimpleName(), costTime, operClass.getIdentifyKey());
        }
        Thread.currentThread().setName("Idle");
    }

    /**
     * 各个子类执行采集任务
     */
    public abstract void exec();

    @Override
    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    @Override
    public void setOperClass(T operClass) {
        this.operClass = operClass;
    }

    /**
     * @return the snmpExecutorService
     */
    public AbstractSnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    /**
     * @param snmpExecutorService
     *            the snmpExecutorService to set
     */
    public void setSnmpExecutorService(AbstractSnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    /**
     * 
     * @param result
     */
    protected void addLocalFileData(PerformanceResult<OperClass> result) {
        try {
            // Modify by Victor@20160927增加一个配置，可以通过配置更改不通过文件缓存，默认采用文件缓存
            // Modify by Victor@20170518增加判断，如果本地缓存已经有数据则全部走本地缓存
            LocalFileData<PerformanceResult<OperClass>> localFileData = LocalFileData
                    .createLocalFileData(operClass.getCategory() + operClass.getIdentifyKey());
            if (perfExecutorService.isNoFileCache() && localFileData.length() == 0) {
                try {
                    perfExecutorService.doResult(result);
                } catch (Exception e) {
                    // 内存处理错误时加入缓存
                    localFileData.add(result);
                }
            } else {
                localFileData.add(result);
            }
        } catch (Exception e) {
            logger.warn("Send performance result error:{}", e.getMessage());
            logger.debug("addLocalFileData", e);
        }
    }
}
