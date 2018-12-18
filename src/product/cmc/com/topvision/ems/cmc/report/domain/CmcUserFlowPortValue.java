/***********************************************************************
 * $Id: CmcUserFlowPortValue.java,v1.0 2013-6-21 上午9:37:47 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import java.text.NumberFormat;

import com.topvision.framework.utils.CmcIndexUtils;

/**
 * @author Bravin
 * @created @2013-6-21-上午9:37:47
 * 
 */
public class CmcUserFlowPortValue {
    private Long portIndex;
    private String portName;
    private Double value;
    private static final NumberFormat formater = NumberFormat.getInstance();
    static {
        // 设置小数点后面最多2位
        formater.setMaximumFractionDigits(2);
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
        if (this.portIndex != null) {
            this.portName = CmcIndexUtils.getEponPortFromIndex(this.portIndex);
        }
    }

    /**
     * @return the value
     */
    public Double getValue() {
        return value;
    }

    /**
     * @param double1
     *            the value to set
     */
    public void setValue(Double double1) {
        this.value = double1;
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
     * @return the valueString
     */
    public String getValueString() {
        return formater.format(this.value);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcUserFlowPortValue [portIndex=");
        builder.append(portIndex);
        builder.append(", portName=");
        builder.append(portName);
        builder.append(", value=");
        builder.append(value);
        builder.append("]");
        return builder.toString();
    }

}
