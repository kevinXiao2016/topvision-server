/***********************************************************************
 * $Id: AutoDiscoveryPeriodConfig.java,v1.0 2014-5-11 下午1:46:40 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2014-5-11-下午1:46:40
 * 
 */
public class AutoDiscoveryPeriodConfig implements AliasesSuperType {
    private static final long serialVersionUID = 5514093898466951967L;
    public static final Integer PERIOD_TIMING = 1;
    public static final Integer PERIOD_REGULAR = 2;
    private Integer periodType;
    private Integer periodStartTime;
    private Integer period;
    private Boolean activating;

    /**
     * @return the periodType
     */
    public Integer getPeriodType() {
        return periodType;
    }

    /**
     * @param periodType
     *            the periodType to set
     */
    public void setPeriodType(Integer periodType) {
        this.periodType = periodType;
    }

    /**
     * @return the periodStartTime
     */
    public Integer getPeriodStartTime() {
        return periodStartTime;
    }

    /**
     * @param periodStartTime
     *            the periodStartTime to set
     */
    public void setPeriodStartTime(Integer periodStartTime) {
        this.periodStartTime = periodStartTime;
    }

    /**
     * @return the period
     */
    public Integer getPeriod() {
        return period;
    }

    /**
     * @param period
     *            the period to set
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }

    /**
     * @return the activating
     */
    public Boolean getActivating() {
        return activating;
    }

    /**
     * @param activating
     *            the activating to set
     */
    public void setActivating(Boolean activating) {
        this.activating = activating;
    }

    public String getPeriodStartExpression() {
        Integer hour = this.periodStartTime / 3600;
        Integer minute = (this.periodStartTime - hour * 3600) / 60;
        Integer second = this.periodStartTime - hour * 3600 - minute * 60;
        StringBuilder sbBuilder = new StringBuilder().append(second).append(" ").append(minute).append(" ")
                .append(hour).append(" ").append("* ").append("* ").append("? ").append("*");
        return sbBuilder.toString();
    }
}
