/***********************************************************************
 * $Id: CmcSnrReportStatistics.java,v1.0 2013-5-28 下午8:34:40 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import java.text.DecimalFormat;

import com.topvision.ems.report.domain.TopoEntityStastic;

/**
 * @author haojie
 * @created @2013-5-28-下午8:34:40
 *
 */
public class CmcSnrReportStatistics extends TopoEntityStastic {
    private Long portTotalNum = 0l;
    private Long portNum0To20 = 0l;
    private String portRateString;
    private final static DecimalFormat df = new DecimalFormat("0.00");

    public Long getPortTotalNum() {
        return portTotalNum;
    }

    public void setPortTotalNum(Long portTotalNum) {
        this.portTotalNum = portTotalNum;
    }

    public Long getPortNum0To20() {
        return portNum0To20;
    }

    public void setPortNum0To20(Long portNum0To20) {
        this.portNum0To20 = portNum0To20;
    }

    public String getPortRateString() {
        if (this.portTotalNum != null && this.portNum0To20 != null && this.portTotalNum != 0) {
            portRateString = df.format(((Double.parseDouble(this.portNum0To20.toString()) / Double
                    .parseDouble(this.portTotalNum.toString())) * 100)) + "%";
        } else {
            portRateString = "0%";
        }
        return portRateString;
    }

    public void setPortRateString(String portRateString) {
        this.portRateString = portRateString;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcSnrReportStatistics [portTotalNum=");
        builder.append(portTotalNum);
        builder.append(", portNum0To20=");
        builder.append(portNum0To20);
        builder.append(", portRateString=");
        builder.append(portRateString);
        builder.append("]");
        return builder.toString();
    }

}
