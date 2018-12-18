/***********************************************************************
 * $Id: DhcpRelayExtGiaddrConfig.java,v1.0 2013-6-25 下午6:59:03 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.domain;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author dosion
 * @created @2013-6-25-下午6:59:03
 * 
 */
public class DhcpRelayExtGiaddrConfig {
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.1", index = true)
    private String topCcmtsDhcpBundleInterface;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.9.1.1", index = true)
    private String dhcpRelayExtDeviceName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.10.1.2", writable = true, type = "IpAddress")
    private String dhcpRelayExtDevGiAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.10.1.3", writable = true, type = "Integer32")
    private Integer dhcpRelayExtDevGiAddrStatus;

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

    public String getDhcpRelayExtDevGiAddress() {
        return dhcpRelayExtDevGiAddress;
    }

    public void setDhcpRelayExtDevGiAddress(String dhcpRelayExtDevGiAddress) {
        this.dhcpRelayExtDevGiAddress = dhcpRelayExtDevGiAddress;
    }

    public Integer getDhcpRelayExtDevGiAddrStatus() {
        return dhcpRelayExtDevGiAddrStatus;
    }

    public void setDhcpRelayExtDevGiAddrStatus(Integer dhcpRelayExtDevGiAddrStatus) {
        this.dhcpRelayExtDevGiAddrStatus = dhcpRelayExtDevGiAddrStatus;
    }

}
