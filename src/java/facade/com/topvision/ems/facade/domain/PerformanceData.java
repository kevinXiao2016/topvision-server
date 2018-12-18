/***********************************************************************
 * $Id: PerformanceData.java,v1.0 2013-6-19 下午02:03:39 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;

/**
 * @author Rod John
 * @created @2013-6-19-下午02:03:39
 * 
 */
public class PerformanceData implements Serializable {
    private static final long serialVersionUID = -3013458384036648588L;
    private String handleId;
    private Object perfData;
    private Long entityId;
    // Add by Victor@20160823增加字段，为了解决推送时无接收方停止采集用
    private Long monitorId;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the handleFlag
     */
    public String getHandleId() {
        return handleId;
    }

    /**
     * @param handleFlag
     *            the handleFlag to set
     */
    public void setHandleId(String handleFlag) {
        this.handleId = handleFlag;
    }

    /**
     * @return the perfData
     */
    public Object getPerfData() {
        return perfData;
    }

    /**
     * @param perfData
     *            the perfData to set
     */
    public void setPerfData(Object perfData) {
        this.perfData = perfData;
    }

    /**
     * 
     * @param handleFlag
     * @param perfData
     */
    public PerformanceData(Long entityId, String handleId, Object perfData) {
        this.entityId = entityId;
        this.handleId = handleId;
        this.perfData = perfData;
    }

    public Long getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }

    @Override
    public String toString() {
        return "PerformanceData [handleId=" + handleId + ", perfData=" + perfData + ", entityId=" + entityId
                + ", monitorId=" + monitorId + "]";
    }

}
