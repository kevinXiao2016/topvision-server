/***********************************************************************
 * $ TopCpeAttribute.java,v1.0 2013-6-20 9:56:37 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.domain.PhysAddress;

/**
 * @author jay
 * @created @2013-6-20-9:56:37
 */
public class TopCpeAttribute implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.1.1.1", index = true)
    private PhysAddress topCmCpeMacAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.1.1.2")
    private Integer topCmCpeType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.1.1.3")
    private String topCmCpeIpAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.1.1.4")
    private Long topCmCpeCcmtsIfIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.1.1.5")
    private Long topCmCpeCmStatusIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.1.1.6")
    private String topCmCpeToCmMacAddr;

    public Long getTopCmCpeCcmtsIfIndex() {
        return topCmCpeCcmtsIfIndex;
    }

    public void setTopCmCpeCcmtsIfIndex(Long topCmCpeCcmtsIfIndex) {
        this.topCmCpeCcmtsIfIndex = topCmCpeCcmtsIfIndex;
    }

    public Long getTopCmCpeCmStatusIndex() {
        return topCmCpeCmStatusIndex;
    }

    public void setTopCmCpeCmStatusIndex(Long topCmCpeCmStatusIndex) {
        this.topCmCpeCmStatusIndex = topCmCpeCmStatusIndex;
    }

    public Integer getTopCmCpeType() {
        return topCmCpeType;
    }

    public void setTopCmCpeType(Integer topCmCpeType) {
        this.topCmCpeType = topCmCpeType;
    }

    public String getTopCmCpeIpAddress() {
        return topCmCpeIpAddress;
    }

    public void setTopCmCpeIpAddress(String topCmCpeIpAddress) {
        this.topCmCpeIpAddress = topCmCpeIpAddress;
    }

    public Long getTopCmCpeIpAddressLong() {
        return new IpUtils(topCmCpeIpAddress).longValue();
    }

    public void setTopCmCpeIpAddressLong(Long topCmCpeIpAddressLong) {
        this.topCmCpeIpAddress = new IpUtils(topCmCpeIpAddressLong).toString();
    }

    public PhysAddress getTopCmCpeMacAddress() {
        return topCmCpeMacAddress;
    }

    public void setTopCmCpeMacAddress(PhysAddress topCmCpeMacAddress) {
        this.topCmCpeMacAddress = topCmCpeMacAddress;
    }

    public Long getTopCmCpeMacAddressLong() {
        return new MacUtils(topCmCpeMacAddress.toString()).longValue();
    }

    public void setTopCmCpeMacAddressLong(Long topCmCpeMacAddressLong) {
        this.topCmCpeMacAddress = new PhysAddress(new MacUtils(topCmCpeMacAddressLong).toString(MacUtils.MAOHAO));
    }

    public String getTopCmCpeToCmMacAddr() {
        return topCmCpeToCmMacAddr;
    }

    public void setTopCmCpeToCmMacAddr(String topCmCpeToCmMacAddr) {
        this.topCmCpeToCmMacAddr = topCmCpeToCmMacAddr;
    }

    public Long getTopCmCpeToCmMacAddrLong() {
        return new MacUtils(topCmCpeToCmMacAddr).longValue();
    }

    public void setTopCmCpeToCmMacAddrLong(Long topCmCpeToCmMacAddrLong) {
        this.topCmCpeToCmMacAddr = new MacUtils(topCmCpeToCmMacAddrLong).toString(MacUtils.WJG);
        this.topCmCpeToCmMacAddr = MacUtils.convertToMaohaoFormat(this.topCmCpeToCmMacAddr);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TopCpeAttribute");
        sb.append("{topCmCpeCcmtsIfIndex=").append(topCmCpeCcmtsIfIndex);
        sb.append(", topCmCpeMacAddress=").append(topCmCpeMacAddress);
        sb.append(", topCmCpeType=").append(topCmCpeType);
        sb.append(", topCmCpeIpAddress=").append(topCmCpeIpAddress);
        sb.append(", topCmCpeCmStatusIndex=").append(topCmCpeCmStatusIndex);
        sb.append(", topCmCpeToCmMacAddr=").append(topCmCpeToCmMacAddr);
        sb.append('}');
        return sb.toString();
    }
}
