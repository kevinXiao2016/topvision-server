/***********************************************************************
 * $Id: Batchautodiscoveryperiod.java,v1.0 2014-5-11 下午2:20:55 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2014-5-11-下午2:20:55
 *
 */
public class BatchAutoDiscoveryPeriod implements AliasesSuperType {
    private static final long serialVersionUID = 578519179143356497L;

    private Integer strategyType;
    private Long periodStart;
    private Long period;
    private Integer active;

    public Integer getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(Integer strategyType) {
        this.strategyType = strategyType;
    }

    public Long getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Long periodStart) {
        this.periodStart = periodStart;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Batchautodiscoveryperiod [strategyType=");
        builder.append(strategyType);
        builder.append(", periodStart=");
        builder.append(periodStart);
        builder.append(", period=");
        builder.append(period);
        builder.append(", active=");
        builder.append(active);
        builder.append("]");
        return builder.toString();
    }

}
