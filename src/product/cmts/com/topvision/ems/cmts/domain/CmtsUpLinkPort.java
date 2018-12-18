/***********************************************************************
 * $Id: CmtsUpLinkPort.java,v1.0 2013-10-23 下午4:06:33 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.domain;

import com.topvision.ems.network.domain.Port;
import com.topvision.framework.common.NumberUtils;

/**
 * @author loyal
 * @created @2013-10-23-下午4:06:33
 *
 */
public class CmtsUpLinkPort extends Port {
    private static final long serialVersionUID = 6753897840334197933L;
    private Float ifInOctetsRate;
    private Float ifOutOctetsRate;
    private String ifInOctetsRateForunit;
    private String ifOutOctetsRateForunit;

    public Float getIfInOctetsRate() {
        return ifInOctetsRate;
    }

    public void setIfInOctetsRate(Float ifInOctetsRate) {
        this.ifInOctetsRate = ifInOctetsRate;
        this.ifInOctetsRateForunit = NumberUtils.TWODOT_FORMAT.format((ifInOctetsRate / NumberUtils.M10));
    }

    public Float getIfOutOctetsRate() {
        return ifOutOctetsRate;
    }

    public void setIfOutOctetsRate(Float ifOutOctetsRate) {
        this.ifOutOctetsRate = ifOutOctetsRate;
        this.ifOutOctetsRateForunit = NumberUtils.TWODOT_FORMAT.format((ifOutOctetsRate / NumberUtils.M10));
    }

    public String getIfInOctetsRateForunit() {
        return ifInOctetsRateForunit;
    }

    public void setIfInOctetsRateForunit(String ifInOctetsRateForunit) {
        this.ifInOctetsRateForunit = ifInOctetsRateForunit;
    }

    public String getIfOutOctetsRateForunit() {
        return ifOutOctetsRateForunit;
    }

    public void setIfOutOctetsRateForunit(String ifOutOctetsRateForunit) {
        this.ifOutOctetsRateForunit = ifOutOctetsRateForunit;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmtsUpLinkPort [ifInOctetsRate=");
        builder.append(ifInOctetsRate);
        builder.append(", ifOutOctetsRate=");
        builder.append(ifOutOctetsRate);
        builder.append(", ifInOctetsRateForunit=");
        builder.append(ifInOctetsRateForunit);
        builder.append(", ifOutOctetsRateForunit=");
        builder.append(ifOutOctetsRateForunit);
        builder.append("]");
        return builder.toString();
    }

}
