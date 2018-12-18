/***********************************************************************
 * $Id: OltSniPortFlowDetail.java,v1.0 2013-6-9 下午2:47:51 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.domain;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2013-6-9-下午2:47:51
 * 
 */
public class OltSniPortFlowDetail implements AliasesSuperType {
    private static final long serialVersionUID = 1022220545783625722L;
    private Long portIndex;
    private Double stats15InOctets;
    private String flowDisplay;
    private Date collectTime;
    private String collectTimeString;
    private String name;
    private String portName;
    private Double portUsedMax;
    private static final NumberFormat formater = NumberFormat.getInstance();
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static {
        // 设置小数点后面最多2位
        formater.setMaximumFractionDigits(2);
    }

    /**
     * @return the usage
     */
    public String getUsage() {
        return formater.format(portUsedMax);
    }

    /**
     * @return the flowDisplay
     */
    public String getFlowDisplay() {
        return flowDisplay;
    }

    /**
     * @param flowDisplay
     *            the flowDisplay to set
     */
    public void setFlowDisplay(String flowDisplay) {
        this.flowDisplay = flowDisplay;
    }

    /**
     * @return the portName
     */
    public String getPortName() {
        return portName;
    }

    /**
     * @param portName
     *            the portName to set
     */
    public void setPortName(String portName) {
        this.portName = portName;
    }

    /**
     * @return the portIndex
     */
    public Long getPortIndex() {
        return portIndex;
    }

    /**
     * @param portIndex
     *            the portIndex to set
     */
    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
        this.portName = EponIndex.getPortStringByIndex(this.portIndex).toString();
    }

    /**
     * @return the stats15InOctets
     */
    public Double getStats15InOctets() {
        return stats15InOctets;
    }

    /**
     * @param stats15InOctets
     *            the stats15InOctets to set
     */
    public void setStats15InOctets(Double stats15InOctets) {
        this.flowDisplay = formater.format(stats15InOctets);
        this.stats15InOctets = stats15InOctets;
    }

    /**
     * @return the collectTime
     */
    public Date getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getCollectTimeString() {
        collectTimeString = "";
        if (this.collectTime != null) {
            collectTimeString = formatter.format(this.collectTime);
        }
        return collectTimeString;
    }

    public void setCollectTimeString(String collectTimeString) {
        this.collectTimeString = collectTimeString;
    }

    public Double getPortUsedMax() {
        return portUsedMax;
    }

    public void setPortUsedMax(Double portUsedMax) {
        this.portUsedMax = portUsedMax;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSniPortFlowDetail [portIndex=");
        builder.append(portIndex);
        builder.append(", stats15InOctets=");
        builder.append(stats15InOctets);
        builder.append(", flowDisplay=");
        builder.append(flowDisplay);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append(", collectTimeString=");
        builder.append(collectTimeString);
        builder.append(", name=");
        builder.append(name);
        builder.append(", portName=");
        builder.append(portName);
        builder.append(", portUsedMax=");
        builder.append(portUsedMax);
        builder.append("]");
        return builder.toString();
    }

}
