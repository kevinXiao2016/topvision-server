/***********************************************************************
 * $Id: OltSubDeviceCount.java,v1.0 2014-8-7 下午3:01:14 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-8-7-下午3:01:14
 *
 */
public class SubDeviceCount implements AliasesSuperType {
    private static final long serialVersionUID = 3159177369337482563L;

    private Integer onuTotal;
    private Integer onuOnline;
    private Integer cmcTotal;
    private Integer cmcOnline;

    public Integer getOnuTotal() {
        return onuTotal;
    }

    public void setOnuTotal(Integer onuTotal) {
        this.onuTotal = onuTotal;
    }

    public Integer getOnuOnline() {
        return onuOnline;
    }

    public void setOnuOnline(Integer onuOnline) {
        this.onuOnline = onuOnline;
    }

    public Integer getCmcTotal() {
        return cmcTotal;
    }

    public void setCmcTotal(Integer cmcTotal) {
        this.cmcTotal = cmcTotal;
    }

    public Integer getCmcOnline() {
        return cmcOnline;
    }

    public void setCmcOnline(Integer cmcOnline) {
        this.cmcOnline = cmcOnline;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSubDeviceCount [onuTotal=");
        builder.append(onuTotal);
        builder.append(", onuOnline=");
        builder.append(onuOnline);
        builder.append(", cmcTotal=");
        builder.append(cmcTotal);
        builder.append(", cmcOnline=");
        builder.append(cmcOnline);
        builder.append("]");
        return builder.toString();
    }

}
