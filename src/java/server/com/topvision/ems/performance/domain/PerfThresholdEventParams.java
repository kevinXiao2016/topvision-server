/***********************************************************************
 * $Id: PerfThresholdEventParams.java,v1.0 2013-6-18 下午03:18:40 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;


/**
 * @author Rod John
 * @created @2013-6-18-下午03:18:40
 * 
 */
public class PerfThresholdEventParams {

    private String perfKey;
    private Float perfValue;
    private String source;
    private String message;
    private Integer alertEventId;
    private Integer clearEventId;
    private String targetId;
    private Long targetIndex;
 
    /**
     * @return the perfKey
     */
    public String getPerfKey() {
        return perfKey;
    }

    /**
     * @param perfKey the perfKey to set
     */
    public void setPerfKey(String perfKey) {
        this.perfKey = perfKey;
    }

    /**
     * @return the perfValue
     */
    public Float getPerfValue() {
        return perfValue;
    }

    /**
     * @param perfValue the perfValue to set
     */
    public void setPerfValue(Float perfValue) {
        this.perfValue = perfValue;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the alertEventId
     */
    public Integer getAlertEventId() {
        return alertEventId;
    }

    /**
     * @param alertEventId
     *            the alertEventId to set
     */
    public void setAlertEventId(Integer alertEventId) {
        this.alertEventId = alertEventId;
    }

    /**
     * @return the clearEventId
     */
    public Integer getClearEventId() {
        return clearEventId;
    }

    /**
     * @param clearEventId
     *            the clearEventId to set
     */
    public void setClearEventId(Integer clearEventId) {
        this.clearEventId = clearEventId;
    }

    /**
     * @return the targetId
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * @param targetId
     *            the targetId to set
     */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Long getTargetIndex() {
        return targetIndex;
    }

    public void setTargetIndex(Long targetIndex) {
        this.targetIndex = targetIndex;
    }

}
