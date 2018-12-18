/***********************************************************************
 * $Id: BatchDeployResultBundle.java,v1.0 2013年11月30日 下午6:44:06 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.batchdeploy.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author Bravin
 * @created @2013年11月30日-下午6:44:06
 *
 */
public class ResultBundle<T> implements Serializable{
    private static final long serialVersionUID = 1662703257984140894L;
    private List<Result<T>> data;
    private long executeTime;
    private List<Long> canceledList;

    public List<Result<T>> getData() {
        return data;
    }

    public void setData(List<Result<T>> data) {
        this.data = data;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

    public List<Long> getCanceledList() {
        return canceledList;
    }

    public void setCanceledList(List<Long> canceledList) {
        this.canceledList = canceledList;
    }

    @Override
    public String toString() {
        return "ResultBundle [data=" + data + ", executeTime=" + executeTime + ", canceledList=" + canceledList + "]";
    }

}
