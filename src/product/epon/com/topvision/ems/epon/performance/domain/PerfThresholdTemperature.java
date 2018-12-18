/***********************************************************************
 * $ PerfThreshold.java,v1.0 2011-11-20 17:28:14 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

import java.io.Serializable;

/**
 * @author jay
 * @created @2011-11-20-17:28:14
 */
public class PerfThresholdTemperature implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;

    // 温度阈值对象类型topPerfTemperatureThresholdObject
    /**
     * performace threshold mpuBoard
     */
    public static int MPUBOARD = 1;
    /**
     * performace threshold ponBoard
     */
    public static int PONBOARD = 2;
    /**
     * performace threshold uplinkBoard
     */
    public static int UPLINKBOARD = 3;
    /**
     * performace threshold onu
     */
    public static int ONU = 4;

    /**
     * performace threshold type
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.3.1.1", index = true)
    private Integer topPerfTemperatureThresholdTypeIdx;
    /**
     * performace threshold
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.3.1.2", index = true)
    private Integer topPerfTemperatureThresholdObject;
    /**
     * 阈值上限
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.3.1.3", writable = true, type = "Integer32")
    private Long topPerfTemperatureThresholdUpper;

    /**
     * 阈值下限
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.3.1.4", writable = true, type = "Integer32")
    private Long topPerfTemperatureThresholdLower;

    public Long getTopPerfTemperatureThresholdLower() {
        return topPerfTemperatureThresholdLower;
    }

    public void setTopPerfTemperatureThresholdLower(Long topPerfTemperatureThresholdLower) {
        this.topPerfTemperatureThresholdLower = topPerfTemperatureThresholdLower;
    }

    public Integer getTopPerfTemperatureThresholdObject() {
        return topPerfTemperatureThresholdObject;
    }

    public void setTopPerfTemperatureThresholdObject(Integer topPerfTemperatureThresholdObject) {
        this.topPerfTemperatureThresholdObject = topPerfTemperatureThresholdObject;
    }

    public Integer getTopPerfTemperatureThresholdTypeIdx() {
        return topPerfTemperatureThresholdTypeIdx;
    }

    public void setTopPerfTemperatureThresholdTypeIdx(Integer topPerfTemperatureThresholdTypeIdx) {
        this.topPerfTemperatureThresholdTypeIdx = topPerfTemperatureThresholdTypeIdx;
    }

    public Long getTopPerfTemperatureThresholdUpper() {
        return topPerfTemperatureThresholdUpper;
    }

    public void setTopPerfTemperatureThresholdUpper(Long topPerfTemperatureThresholdUpper) {
        this.topPerfTemperatureThresholdUpper = topPerfTemperatureThresholdUpper;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PerfThresholdTemperature");
        sb.append("{topPerfTemperatureThresholdLower=").append(topPerfTemperatureThresholdLower);
        sb.append(", topPerfTemperatureThresholdTypeIdx=").append(topPerfTemperatureThresholdTypeIdx);
        sb.append(", topPerfTemperatureThresholdObject=").append(topPerfTemperatureThresholdObject);
        sb.append(", topPerfTemperatureThresholdUpper=").append(topPerfTemperatureThresholdUpper);
        sb.append('}');
        return sb.toString();
    }
}