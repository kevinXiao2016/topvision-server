/***********************************************************************
 * $Id: DhcpRelayExtServerConfig.java,v1.0 2013-6-25 下午7:02:29 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.domain;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author dosion
 * @created @2013-6-25-下午7:02:29
 * 
 */
public class DhcpRelayExtServerConfig {
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.1", index = true)
    private String topCcmtsDhcpBundleInterface;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.9.1.1", index = true)
    private String dhcpRelayExtDeviceName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.11.1.1", index = true)
    private Integer dhcpRelayExtHelperIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.11.1.2", writable = true, type = "IpAddress")
    private String dhcpRelayExtHelperIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.11.1.3", writable = true, type = "Integer32")
    private Integer dhcpRelayExtHelperStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getTopCcmtsDhcpBundleInterface() {
        return topCcmtsDhcpBundleInterface;
    }

    public void setTopCcmtsDhcpBundleInterface(String topCcmtsDhcpBundleInterface) {
        this.topCcmtsDhcpBundleInterface = topCcmtsDhcpBundleInterface;
    }

    public String getDhcpRelayExtDeviceName() {
        return dhcpRelayExtDeviceName;
    }

    public void setDhcpRelayExtDeviceName(String dhcpRelayExtDeviceName) {
        this.dhcpRelayExtDeviceName = dhcpRelayExtDeviceName;
    }

    public Integer getDhcpRelayExtHelperIndex() {
        return dhcpRelayExtHelperIndex;
    }

    public void setDhcpRelayExtHelperIndex(Integer dhcpRelayExtHelperIndex) {
        this.dhcpRelayExtHelperIndex = dhcpRelayExtHelperIndex;
    }

    public String getDhcpRelayExtHelperIpAddr() {
        return dhcpRelayExtHelperIpAddr;
    }

    public void setDhcpRelayExtHelperIpAddr(String dhcpRelayExtHelperIpAddr) {
        this.dhcpRelayExtHelperIpAddr = dhcpRelayExtHelperIpAddr;
    }

    public Integer getDhcpRelayExtHelperStatus() {
        return dhcpRelayExtHelperStatus;
    }

    public void setDhcpRelayExtHelperStatus(Integer dhcpRelayExtHelperStatus) {
        this.dhcpRelayExtHelperStatus = dhcpRelayExtHelperStatus;
    }

}
