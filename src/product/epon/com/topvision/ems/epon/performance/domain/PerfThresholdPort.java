/***********************************************************************
 * $ PerfThreshold.java,v1.0 2011-11-20 17:28:14 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.io.Serializable;
import java.math.BigInteger;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-20-17:28:14
 */
public class PerfThresholdPort implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    // 性能阈值对象类型topPerfThresholdObject
    /**
     * performace threshold object sni
     */
    public static int SNIOBJECT = 1;
    /**
     * performace threshold object pon
     */
    public static int PONOBJECT = 2;
    /**
     * performace threshold object onupon
     */
    public static int ONUPONOBJECT = 3;
    /**
     * performace threshold object uni
     */
    public static int ONUUNIOBJECT = 4;

    /**
     * performace threshold type
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.2.1.1", index = true)
    private Integer topPerfThresholdTypeIndex;
    /**
     * performace threshold object
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.2.1.2", index = true)
    private Integer topPerfThresholdObject;
    /**
     * 阈值上限
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.2.1.3", writable = true, type = "Counter64")
    private String topPerfThresholdUpperMib;
    private String topPerfThresholdUpper;
    /**
     * 阈值下限
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.2.1.4", writable = true, type = "Counter64")
    private String topPerfThresholdLowerMib;
    private String topPerfThresholdLower;

    public Integer getTopPerfThresholdObject() {
        return topPerfThresholdObject;
    }

    public void setTopPerfThresholdObject(Integer topPerfThresholdObject) {
        this.topPerfThresholdObject = topPerfThresholdObject;
    }

    public Integer getTopPerfThresholdTypeIndex() {
        return topPerfThresholdTypeIndex;
    }

    public void setTopPerfThresholdTypeIndex(Integer topPerfThresholdTypeIndex) {
        this.topPerfThresholdTypeIndex = topPerfThresholdTypeIndex;
    }

    /**
     * @return the topPerfThresholdUpperMib
     */
    public String getTopPerfThresholdUpperMib() {
        return topPerfThresholdUpperMib;
    }

    /**
     * @param topPerfThresholdUpperMib
     *            the topPerfThresholdUpperMib to set
     */
    public void setTopPerfThresholdUpperMib(String topPerfThresholdUpperMib) {
        this.topPerfThresholdUpperMib = this.topPerfThresholdUpper = topPerfThresholdUpperMib;
    }

    /**
     * @return the topPerfThresholdLowerMib
     */
    public String getTopPerfThresholdLowerMib() {
        return topPerfThresholdLowerMib;
    }

    /**
     * @param topPerfThresholdLowerMib
     *            the topPerfThresholdLowerMib to set
     */
    public void setTopPerfThresholdLowerMib(String topPerfThresholdLowerMib) {
        this.topPerfThresholdLowerMib = this.topPerfThresholdLower = topPerfThresholdLowerMib;
    }

    /**
     * @return the topPerfThresholdUpper
     */
    public String getTopPerfThresholdUpper() {
        return topPerfThresholdUpper;
    }

    /**
     * @param topPerfThresholdUpperStr
     *            the topPerfThresholdUpperStr to set
     */
    public void setTopPerfThresholdUpper(String topPerfThresholdUpper) {
        this.topPerfThresholdUpper = topPerfThresholdUpper;
        this.topPerfThresholdUpperMib = new BigInteger(topPerfThresholdUpper).longValue() + "";
    }

    /**
     * @return the topPerfThresholdLowerStr
     */
    public String getTopPerfThresholdLower() {
        return topPerfThresholdLower;
    }

    /**
     * @param topPerfThresholdLowerStr
     *            the topPerfThresholdLowerStr to set
     */
    public void setTopPerfThresholdLower(String topPerfThresholdLower) {
        this.topPerfThresholdLower = topPerfThresholdLower;
        this.topPerfThresholdLowerMib = new BigInteger(topPerfThresholdLower).longValue() + "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PerfThresholdPort [topPerfThresholdTypeIndex=");
        builder.append(topPerfThresholdTypeIndex);
        builder.append(", topPerfThresholdObject=");
        builder.append(topPerfThresholdObject);
        builder.append(", topPerfThresholdUpperMib=");
        builder.append(topPerfThresholdUpperMib);
        builder.append(", topPerfThresholdUpper=");
        builder.append(topPerfThresholdUpper);
        builder.append(", topPerfThresholdLowerMib=");
        builder.append(topPerfThresholdLowerMib);
        builder.append(", topPerfThresholdLower=");
        builder.append(topPerfThresholdLower);
        builder.append("]");
        return builder.toString();
    }
}
