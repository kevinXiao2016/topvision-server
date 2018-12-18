/***********************************************************************
 * $Id: DhcpRelayExtDevice.java,v1.0 2013-6-25 下午6:52:34 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.domain;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author dosion
 * @created @2013-6-25-下午6:52:34
 * 
 */
public class DhcpRelayExtDevice {
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.1", index = true)
    private String topCcmtsDhcpBundleInterface;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.9.1.1", index = true)
    private String dhcpRelayExtDeviceName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.9.1.2", index = true)
    private Integer dhcpRelayExtDeviceOptionIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.9.1.3", writable = true, type = "OctetString")
    private String dhcpRelayExtDeviceOptionStr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.9.1.4", writable = true, type = "Integer32")
    private Integer dhcpRelayExtDeviceOptionStatus;

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

    public Integer getDhcpRelayExtDeviceOptionIndex() {
        return dhcpRelayExtDeviceOptionIndex;
    }

    public void setDhcpRelayExtDeviceOptionIndex(Integer dhcpRelayExtDeviceOptionIndex) {
        this.dhcpRelayExtDeviceOptionIndex = dhcpRelayExtDeviceOptionIndex;
    }

    public String getDhcpRelayExtDeviceOptionStr() {
        return dhcpRelayExtDeviceOptionStr;
    }

    public void setDhcpRelayExtDeviceOptionStr(String dhcpRelayExtDeviceOptionStr) {
        this.dhcpRelayExtDeviceOptionStr = dhcpRelayExtDeviceOptionStr;
    }

    public Integer getDhcpRelayExtDeviceOptionStatus() {
        return dhcpRelayExtDeviceOptionStatus;
    }

    public void setDhcpRelayExtDeviceOptionStatus(Integer dhcpRelayExtDeviceOptionStatus) {
        this.dhcpRelayExtDeviceOptionStatus = dhcpRelayExtDeviceOptionStatus;
    }

}
