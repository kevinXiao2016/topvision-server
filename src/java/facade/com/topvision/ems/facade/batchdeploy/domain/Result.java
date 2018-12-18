/***********************************************************************
 * $Id: BatchDeployResult.java,v1.0 2013年11月30日 下午3:34:16 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.batchdeploy.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bravin
 * @created @2013年11月30日-下午3:34:16
 *
 */
public class Result<T> implements Serializable{
    private static final long serialVersionUID = -869127704965032193L;
    private List<T> failureList;
    private List<T> successList;
    private Long entityId;

    public Result() {
        failureList = new ArrayList<>();
        successList = new ArrayList<>();
    }
    
    public void addFailure(T target) {
        failureList.add(target);
    }

    public void addSuccess(T target) {
        successList.add(target);
    }

    public List<T> getFailureList() {
        return failureList;
    }

    public void setFailureList(List<T> failureList) {
        this.failureList = failureList;
    }

    public List<T> getSuccessList() {
        return successList;
    }

    public void setSuccessList(List<T> successList) {
        this.successList = successList;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        return "Result [failureList=" + failureList + ", successList=" + successList + ", entityId=" + entityId + "]";
    }


}
