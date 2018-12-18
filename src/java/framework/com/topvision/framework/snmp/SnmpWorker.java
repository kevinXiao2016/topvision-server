/***********************************************************************
 * $Id: SnmpWorker.java,v 1.1 May 27, 2008 3:54:13 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.common.DateUtils;

/**
 * @Create Date May 27, 2008 3:54:13 PM
 * 
 * @author kelers
 * 
 */
public abstract class SnmpWorker<T> implements Runnable, Serializable {
    private static final long serialVersionUID = -5236351326410920344L;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Logger log = LoggerFactory.getLogger(SnmpWorker.class);
    protected SnmpUtil snmpUtil;
    protected SnmpParam snmpParam;
    protected T result;
    protected Object[] params;
    // 用于封装对象重新初始化
    private SnmpDataProxy<T> dataProxy;
    private String callThreadName;

    public SnmpWorker(SnmpParam snmpParam) {
        // Modify by Rod
        setCallThreadName(Thread.currentThread().getName());
        this.snmpParam = snmpParam;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public final void run() {
        result = dataProxy.getData();
        params = dataProxy.getParams();
        String name = Thread.currentThread().getName();
        Thread.currentThread().setName(snmpParam.getIpAddress());
        long l = System.currentTimeMillis();
        // Thread.currentThread().setName(getCallThreadName());
        try {
            exec();
            if (log.isDebugEnabled() && result != null) {
                l = System.currentTimeMillis() - l;
                if (l > 60000) {
                    log.debug("*********************SNMP expends {} for {} -- {}\n-Invoke:{}",
                            DateUtils.getTimePeriod(l), snmpParam.getIpAddress(), result.getClass().getName(),
                            dataProxy.getInvokeStacktrace());
                } else {
                    log.debug("SNMP expends {} for {} -- {}\n-Invoke:{}", DateUtils.getTimePeriod(l),
                            snmpParam.getIpAddress(), result.getClass().getName(), dataProxy.getInvokeStacktrace());
                }
            }
        } catch (Exception e) {
            if (dataProxy != null) {
                dataProxy.setStacktrace(e);
            }
        }
        Thread.currentThread().setName(name);
        dataProxy.setData(result);
    }

    /**
     * 实际执行程序，需要在实际worker中实现
     * 
     * @throws Exception
     */
    protected abstract void exec() throws Exception;

    /**
     * 传入参数
     * 
     * @return 参数数据
     */
    protected Object[] getParams() {
        if (dataProxy == null || dataProxy.getParams() == null || dataProxy.getParams().length == 0) {
            return null;
        }
        return dataProxy.getParams();
    }

    /**
     * @return the snmpUtil
     */
    public SnmpUtil getSnmpUtil() {
        return snmpUtil;
    }

    /**
     * @param snmpUtil
     *            the snmpUtil to set
     */
    public void setSnmpUtil(SnmpUtil snmpUtil) {
        this.snmpUtil = snmpUtil;
    }

    /**
     * @return the snmpParam
     */
    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    /**
     * @param snmpParam
     *            the snmpParam to set
     */
    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    /**
     * @return the dataProxy
     */
    public SnmpDataProxy<T> getDataProxy() {
        return dataProxy;
    }

    /**
     * @param dataProxy
     *            the dataProxy to set
     */
    public void setDataProxy(SnmpDataProxy<T> dataProxy) {
        this.dataProxy = dataProxy;
    }

    /**
     * @return the callThreadName
     */
    public String getCallThreadName() {
        return callThreadName;
    }

    /**
     * @param callThreadName
     *            the callThreadName to set
     */
    public void setCallThreadName(String callThreadName) {
        this.callThreadName = callThreadName;
    }
}
