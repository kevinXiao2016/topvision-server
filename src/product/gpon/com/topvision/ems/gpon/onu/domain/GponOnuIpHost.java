/***********************************************************************
 * $Id: GponOnuIpHost.java,v1.0 2016年10月15日 下午3:31:10 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2016年10月15日-下午3:31:10
 *
 */
public class GponOnuIpHost implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -7283742253934246383L;
    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.3.1.1", index = true)
    private Long onuIpHostDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.3.1.2", index = true)
    private Integer onuIpHostIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.3.1.3", writable = true, type = "Integer32")
    private Integer onuIpHostAddressConfigMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.3.1.4", writable = true, type = "IpAddress")
    private String onuIpHostAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.3.1.5", writable = true, type = "IpAddress")
    private String onuIpHostSubnetMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.3.1.6", writable = true, type = "IpAddress")
    private String onuIpHostGateway;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.3.1.7", writable = true, type = "IpAddress")
    private String onuIpHostPrimaryDNS;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.3.1.8", writable = true, type = "IpAddress")
    private String onuIpHostSecondaryDNS;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.3.1.9", writable = true, type = "Integer32")
    private Integer onuIpHostVlanTagPriority;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.3.1.10", writable = true, type = "Integer32")
    private Integer onuIpHostVlanPVid;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.3.1.11")
    private String onuIpHostMacAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.3.1.12", writable = true, type = "Integer32")
    private Integer onuIpHostRowStatus;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the onuId
     */
    public Long getOnuId() {
        return onuId;
    }

    /**
     * @param onuId the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        onuIpHostDeviceIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
    }

    /**
     * @return the onuIpHostDeviceIndex
     */
    public Long getOnuIpHostDeviceIndex() {
        return onuIpHostDeviceIndex;
    }

    /**
     * @param onuIpHostDeviceIndex the onuIpHostDeviceIndex to set
     */
    public void setOnuIpHostDeviceIndex(Long onuIpHostDeviceIndex) {
        this.onuIpHostDeviceIndex = onuIpHostDeviceIndex;
        onuIndex = EponIndex.getOnuIndexByMibIndex(onuIpHostDeviceIndex);
    }

    /**
     * @return the onuIpHostIndex
     */
    public Integer getOnuIpHostIndex() {
        return onuIpHostIndex;
    }

    /**
     * @param onuIpHostIndex the onuIpHostIndex to set
     */
    public void setOnuIpHostIndex(Integer onuIpHostIndex) {
        this.onuIpHostIndex = onuIpHostIndex;
    }

    /**
     * @return the onuIpHostAddressConfigMode
     */
    public Integer getOnuIpHostAddressConfigMode() {
        return onuIpHostAddressConfigMode;
    }

    /**
     * @param onuIpHostAddressConfigMode the onuIpHostAddressConfigMode to set
     */
    public void setOnuIpHostAddressConfigMode(Integer onuIpHostAddressConfigMode) {
        this.onuIpHostAddressConfigMode = onuIpHostAddressConfigMode;
    }

    /**
     * @return the onuIpHostAddress
     */
    public String getOnuIpHostAddress() {
        return onuIpHostAddress;
    }

    /**
     * @param onuIpHostAddress the onuIpHostAddress to set
     */
    public void setOnuIpHostAddress(String onuIpHostAddress) {
        this.onuIpHostAddress = onuIpHostAddress;
    }

    /**
     * @return the onuIpHostSubnetMask
     */
    public String getOnuIpHostSubnetMask() {
        return onuIpHostSubnetMask;
    }

    /**
     * @param onuIpHostSubnetMask the onuIpHostSubnetMask to set
     */
    public void setOnuIpHostSubnetMask(String onuIpHostSubnetMask) {
        this.onuIpHostSubnetMask = onuIpHostSubnetMask;
    }

    /**
     * @return the onuIpHostGateway
     */
    public String getOnuIpHostGateway() {
        return onuIpHostGateway;
    }

    /**
     * @param onuIpHostGateway the onuIpHostGateway to set
     */
    public void setOnuIpHostGateway(String onuIpHostGateway) {
        this.onuIpHostGateway = onuIpHostGateway;
    }

    /**
     * @return the onuIpHostPrimaryDNS
     */
    public String getOnuIpHostPrimaryDNS() {
        return onuIpHostPrimaryDNS;
    }

    /**
     * @param onuIpHostPrimaryDNS the onuIpHostPrimaryDNS to set
     */
    public void setOnuIpHostPrimaryDNS(String onuIpHostPrimaryDNS) {
        this.onuIpHostPrimaryDNS = onuIpHostPrimaryDNS;
    }

    /**
     * @return the onuIpHostSecondaryDNS
     */
    public String getOnuIpHostSecondaryDNS() {
        return onuIpHostSecondaryDNS;
    }

    /**
     * @param onuIpHostSecondaryDNS the onuIpHostSecondaryDNS to set
     */
    public void setOnuIpHostSecondaryDNS(String onuIpHostSecondaryDNS) {
        this.onuIpHostSecondaryDNS = onuIpHostSecondaryDNS;
    }

    /**
     * @return the onuIpHostVlanTagPriority
     */
    public Integer getOnuIpHostVlanTagPriority() {
        return onuIpHostVlanTagPriority;
    }

    /**
     * @param onuIpHostVlanTagPriority the onuIpHostVlanTagPriority to set
     */
    public void setOnuIpHostVlanTagPriority(Integer onuIpHostVlanTagPriority) {
        this.onuIpHostVlanTagPriority = onuIpHostVlanTagPriority;
    }

    /**
     * @return the onuIpHostVlanPVid
     */
    public Integer getOnuIpHostVlanPVid() {
        return onuIpHostVlanPVid;
    }

    /**
     * @param onuIpHostVlanPVid the onuIpHostVlanPVid to set
     */
    public void setOnuIpHostVlanPVid(Integer onuIpHostVlanPVid) {
        this.onuIpHostVlanPVid = onuIpHostVlanPVid;
    }

    /**
     * @return the onuIpHostMacAddress
     */
    public String getOnuIpHostMacAddress() {
        return onuIpHostMacAddress;
    }

    /**
     * @param onuIpHostMacAddress the onuIpHostMacAddress to set
     */
    public void setOnuIpHostMacAddress(String onuIpHostMacAddress) {
        this.onuIpHostMacAddress = onuIpHostMacAddress;
    }

    /**
     * @return the onuIpHostRowStatus
     */
    public Integer getOnuIpHostRowStatus() {
        return onuIpHostRowStatus;
    }

    /**
     * @param onuIpHostRowStatus the onuIpHostRowStatus to set
     */
    public void setOnuIpHostRowStatus(Integer onuIpHostRowStatus) {
        this.onuIpHostRowStatus = onuIpHostRowStatus;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GponOnuIpHost [entityId=" + entityId + ", onuId=" + onuId + ", onuIndex=" + onuIndex
                + ", onuIpHostDeviceIndex=" + onuIpHostDeviceIndex + ", onuIpHostIndex=" + onuIpHostIndex
                + ", onuIpHostAddressConfigMode=" + onuIpHostAddressConfigMode + ", onuIpHostAddress="
                + onuIpHostAddress + ", onuIpHostSubnetMask=" + onuIpHostSubnetMask + ", onuIpHostGateway="
                + onuIpHostGateway + ", onuIpHostPrimaryDNS=" + onuIpHostPrimaryDNS + ", onuIpHostSecondaryDNS="
                + onuIpHostSecondaryDNS + ", onuIpHostVlanTagPriority=" + onuIpHostVlanTagPriority
                + ", onuIpHostVlanPVid=" + onuIpHostVlanPVid + ", onuIpHostMacAddress=" + onuIpHostMacAddress
                + ", onuIpHostRowStatus=" + onuIpHostRowStatus + "]";
    }

}
