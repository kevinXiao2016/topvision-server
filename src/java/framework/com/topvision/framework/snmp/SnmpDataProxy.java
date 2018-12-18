/***********************************************************************
 * $Id: SnmpDataProxy.java,v1.0 2011-10-14 下午01:16:40 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import java.io.Serializable;
import java.util.List;

/**
 * @author Victor
 * @created @2011-10-14-下午01:16:40
 * 
 */
public class SnmpDataProxy<T> implements Serializable {
    private static final long serialVersionUID = 8866251133545836362L;
    private T data;
    private List<T> dataList;
    private Throwable stacktrace;
    private Object[] params;
    private StackTraceElement[] invokeStacktrace;

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * @return the dataList
     */
    public List<T> getDataList() {
        return dataList;
    }

    /**
     * @param dataList
     *            the dataList to set
     */
    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    /**
     * @return the stacktrace
     */
    public Throwable getStacktrace() {
        return stacktrace;
    }

    /**
     * @param stacktrace
     *            the stacktrace to set
     */
    public void setStacktrace(Throwable stacktrace) {
        this.stacktrace = stacktrace;
    }

    /**
     * @return the params
     */
    public Object[] getParams() {
        return params;
    }

    /**
     * @param params
     *            the params to set
     */
    public void setParams(Object[] params) {
        this.params = params;
    }

    public StackTraceElement[] getInvokeStacktrace() {
        return invokeStacktrace;
    }

    public void setInvokeStacktrace(StackTraceElement[] invokeStacktrace) {
        this.invokeStacktrace = invokeStacktrace;
    }
}
