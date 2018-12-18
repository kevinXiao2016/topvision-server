/***********************************************************************
 * $Id: OnuCpeIpInfo.java,v1.0 2016年9月17日 下午2:14:48 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.io.Serializable;

/**
 * @author Bravin
 * @created @2016年9月17日-下午2:14:48
 *
 */
public class OnuCpeIpInfo implements Serializable {
    private static final long serialVersionUID = -5324824871671823269L;

    private String ip;
    private String mac;
    private Integer cpeType;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getCpeType() {
        return cpeType;
    }

    public void setCpeType(Integer cpeType) {
        this.cpeType = cpeType;
    }

    @Override
    public String toString() {
        return "OnuCpeIpInfo [mac=" + mac + ", cpeType=" + cpeType + "]";
    }

}
