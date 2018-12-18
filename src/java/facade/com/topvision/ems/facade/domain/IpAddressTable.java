/***********************************************************************
 * $Id: IpAddressTable.java,v1.0 2013-8-19 上午10:02:08 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Rod John
 * @created @2013-8-19-上午10:02:08
 * 
 */
public class IpAddressTable implements Serializable {

    private static final long serialVersionUID = 3996072848512509644L;
    @SnmpProperty(oid = "1.3.6.1.2.1.4.20.1.1", index = true)
    private String ipAdEntAddr;
    @SnmpProperty(oid = "1.3.6.1.2.1.4.20.1.2")
    private String ipAdEntIfIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.4.20.1.3")
    private String ipAdEntNetMask;
    @SnmpProperty(oid = "1.3.6.1.2.1.4.20.1.4")
    private String ipAdEntBcastAddr;
    @SnmpProperty(oid = "1.3.6.1.2.1.4.20.1.5")
    private String ipAdEntReasmMaxSize;

    /**
     * @return the ipAdEntAddr
     */
    public String getIpAdEntAddr() {
        return ipAdEntAddr;
    }

    /**
     * @param ipAdEntAddr
     *            the ipAdEntAddr to set
     */
    public void setIpAdEntAddr(String ipAdEntAddr) {
        this.ipAdEntAddr = ipAdEntAddr;
    }

    /**
     * @return the ipAdEntIfIndex
     */
    public String getIpAdEntIfIndex() {
        return ipAdEntIfIndex;
    }

    /**
     * @param ipAdEntIfIndex
     *            the ipAdEntIfIndex to set
     */
    public void setIpAdEntIfIndex(String ipAdEntIfIndex) {
        this.ipAdEntIfIndex = ipAdEntIfIndex;
    }

    /**
     * @return the ipAdEntNetMask
     */
    public String getIpAdEntNetMask() {
        return ipAdEntNetMask;
    }

    /**
     * @param ipAdEntNetMask
     *            the ipAdEntNetMask to set
     */
    public void setIpAdEntNetMask(String ipAdEntNetMask) {
        this.ipAdEntNetMask = ipAdEntNetMask;
    }

    /**
     * @return the ipAdEntBcastAddr
     */
    public String getIpAdEntBcastAddr() {
        return ipAdEntBcastAddr;
    }

    /**
     * @param ipAdEntBcastAddr
     *            the ipAdEntBcastAddr to set
     */
    public void setIpAdEntBcastAddr(String ipAdEntBcastAddr) {
        this.ipAdEntBcastAddr = ipAdEntBcastAddr;
    }

    /**
     * @return the ipAdEntReasmMaxSize
     */
    public String getIpAdEntReasmMaxSize() {
        return ipAdEntReasmMaxSize;
    }

    /**
     * @param ipAdEntReasmMaxSize
     *            the ipAdEntReasmMaxSize to set
     */
    public void setIpAdEntReasmMaxSize(String ipAdEntReasmMaxSize) {
        this.ipAdEntReasmMaxSize = ipAdEntReasmMaxSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IpAddressTable [ipAdEntAddr=");
        builder.append(ipAdEntAddr);
        builder.append(", ipAdEntIfIndex=");
        builder.append(ipAdEntIfIndex);
        builder.append(", ipAdEntNetMask=");
        builder.append(ipAdEntNetMask);
        builder.append(", ipAdEntBcastAddr=");
        builder.append(ipAdEntBcastAddr);
        builder.append(", ipAdEntReasmMaxSize=");
        builder.append(ipAdEntReasmMaxSize);
        builder.append("]");
        return builder.toString();
    }

}
