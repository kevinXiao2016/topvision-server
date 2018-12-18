/***********************************************************************
 * $Id: CmcUserFlowReportStatistics.java,v1.0 2013-5-31 上午9:53:24 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.report.domain.TopoEntityStastic;

/**
 * @author Bravin
 * @created @2013-5-31-上午9:53:24
 * 
 */
public class CmcUserFlowReportStatistics extends TopoEntityStastic {
    private Integer ccmtsTotal = 0;
    private Integer userNumTotal = 0;
    private Integer userNumOnline = 0;
    private Double uplinkPortRate = 0.0;
    private Double bandWidthUsage = 0.0;
    /**
     * 端口的INDEX
     */
    private Long portIndex;
    /**
     * 端口的显示名称
     */
    private String portName;
    private List<CmcUserFlowPortValue> portValueList;

    public CmcUserFlowReportStatistics() {
        portValueList = new ArrayList<CmcUserFlowPortValue>();
    }

    /**
     * @return the ccmtsTotal
     */
    public Integer getCcmtsTotal() {
        return ccmtsTotal;
    }

    /**
     * @param ccmtsTotal
     *            the ccmtsTotal to set
     */
    public void setCcmtsTotal(Integer ccmtsTotal) {
        this.ccmtsTotal = ccmtsTotal;
    }

    /**
     * @return the userNumTotal
     */
    public Integer getUserNumTotal() {
        return userNumTotal;
    }

    /**
     * @param userNumTotal
     *            the userNumTotal to set
     */
    public void setUserNumTotal(Integer userNumTotal) {
        this.userNumTotal = userNumTotal;
    }

    /**
     * @return the userNumOnline
     */
    public Integer getUserNumOnline() {
        return userNumOnline;
    }

    /**
     * @param userNumOnline
     *            the userNumOnline to set
     */
    public void setUserNumOnline(Integer userNumOnline) {
        this.userNumOnline = userNumOnline;
    }

    /**
     * @return the uplinkPortRate
     */
    public Double getUplinkPortRate() {
        return uplinkPortRate;
    }

    /**
     * @param uplinkPortRate
     *            the uplinkPortRate to set
     */
    public void setUplinkPortRate(Double uplinkPortRate) {
        if (uplinkPortRate != null) {
            this.uplinkPortRate = uplinkPortRate;
        }
    }

    /**
     * @return the bandWidthUsage
     */
    public Double getBandWidthUsage() {
        return bandWidthUsage;
    }

    /**
     * @param bandWidthUsage
     *            the bandWidthUsage to set
     */
    public void setBandWidthUsage(Double bandWidthUsage) {
        if (bandWidthUsage != null) {
            this.bandWidthUsage = bandWidthUsage;
        }
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
    }

    /**
     * @return the portName
     */
    public String getPortName() {
        return CmcIndexUtils.getEponPortFromIndex(this.portIndex);
    }

    /**
     * @param portName
     *            the portName to set
     */
    public void setPortName(String portName) {
        this.portName = portName;
    }

    /**
     * @return the portValueList
     */
    public List<CmcUserFlowPortValue> getPortValueList() {
        return portValueList;
    }

    /**
     * @param portValueList
     *            the portValueList to set
     */
    public void setPortValueList(List<CmcUserFlowPortValue> portValueList) {
        this.portValueList = portValueList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcUserFlowReportStatistics [ccmtsTotal=");
        builder.append(ccmtsTotal);
        builder.append(", userNumTotal=");
        builder.append(userNumTotal);
        builder.append(", userNumOnline=");
        builder.append(userNumOnline);
        builder.append(", uplinkPortRate=");
        builder.append(uplinkPortRate);
        builder.append(", bandWidthUsage=");
        builder.append(bandWidthUsage);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", portName=");
        builder.append(portName);
        builder.append(", portValueList=");
        builder.append(portValueList);
        builder.append("]");
        return builder.toString();
    }

}
