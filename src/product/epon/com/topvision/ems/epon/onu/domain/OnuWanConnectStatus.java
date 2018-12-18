/***********************************************************************
 * $Id: OnuWanConnectStatus.java,v1.0 2016年5月30日 下午5:07:27 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author loyal
 * @created @2016年5月30日-下午5:07:27
 * 
 */
public class OnuWanConnectStatus implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -2693182744131982812L;
    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.6.1.1,V1.10:1.3.6.1.4.1.17409.2.9.1.1.6.1.1", index = true)
    private Long onuMibIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.6.1.2,V1.10:1.3.6.1.4.1.17409.2.9.1.1.6.1.2", index = true)
    private Integer connectId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.6.1.3,V1.10:1.3.6.1.4.1.17409.2.9.1.1.6.1.3")
    private String connectName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.6.1.4,V1.10:1.3.6.1.4.1.17409.2.9.1.1.6.1.4", type = "Integer32")
    private Integer connectMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.6.1.5,V1.10:1.3.6.1.4.1.17409.2.9.1.1.6.1.5", type = "Integer32")
    private Integer connectStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.6.1.6,V1.10:1.3.6.1.4.1.17409.2.9.1.1.6.1.6", type = "Integer32")
    private Integer connectErrorCode;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.6.1.7,V1.10:1.3.6.1.4.1.17409.2.9.1.1.6.1.7")
    private String ipv4Address;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.6.1.8,V1.10:1.3.6.1.4.1.17409.2.9.1.1.6.1.8")
    private String ipv4Mask;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.6.1.9,V1.10:1.3.6.1.4.1.17409.2.9.1.1.6.1.9")
    private String ipv4Gateway;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.6.1.10,V1.10:1.3.6.1.4.1.17409.2.9.1.1.6.1.10")
    private String ipv4Dns;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.6.1.11,V1.10:1.3.6.1.4.1.17409.2.9.1.1.6.1.11")
    private String ipv4DnsAlternative;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        if (onuIndex != null) {
            onuMibIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
        }
    }

    public Integer getConnectId() {
        return connectId;
    }

    public void setConnectId(Integer connectId) {
        this.connectId = connectId;
    }

    public String getConnectName() {
        return connectName;
    }

    public void setConnectName(String connectName) {
        this.connectName = connectName;
    }

    public Integer getConnectMode() {
        return connectMode;
    }

    public void setConnectMode(Integer connectMode) {
        this.connectMode = connectMode;
    }

    public Integer getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(Integer connectStatus) {
        this.connectStatus = connectStatus;
    }

    public Integer getConnectErrorCode() {
        return connectErrorCode;
    }

    public void setConnectErrorCode(Integer connectErrorCode) {
        this.connectErrorCode = connectErrorCode;
    }

    public String getIpv4Address() {
        return ipv4Address;
    }

    public void setIpv4Address(String ipv4Address) {
        this.ipv4Address = ipv4Address;
    }

    public String getIpv4Gateway() {
        return ipv4Gateway;
    }

    public void setIpv4Gateway(String ipv4Gateway) {
        this.ipv4Gateway = ipv4Gateway;
    }

    public String getIpv4Dns() {
        return ipv4Dns;
    }

    public void setIpv4Dns(String ipv4Dns) {
        this.ipv4Dns = ipv4Dns;
    }

    public String getIpv4DnsAlternative() {
        return ipv4DnsAlternative;
    }

    public void setIpv4DnsAlternative(String ipv4DnsAlternative) {
        this.ipv4DnsAlternative = ipv4DnsAlternative;
    }

    public String getIpv4Mask() {
        return ipv4Mask;
    }

    public void setIpv4Mask(String ipv4Mask) {
        this.ipv4Mask = ipv4Mask;
    }

    /**
     * @return the onuMibIndex
     */
    public Long getOnuMibIndex() {
        return onuMibIndex;
    }

    /**
     * @param onuMibIndex
     *            the onuMibIndex to set
     */
    public void setOnuMibIndex(Long onuMibIndex) {
        this.onuMibIndex = onuMibIndex;
        if (onuMibIndex != null) {
            onuIndex = EponIndex.getOnuIndexByMibIndex(onuMibIndex);
        }
    }

}
