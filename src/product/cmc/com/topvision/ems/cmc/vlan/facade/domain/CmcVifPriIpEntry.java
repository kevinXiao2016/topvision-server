package com.topvision.ems.cmc.vlan.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;

public class CmcVifPriIpEntry {

    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.3.1.1", writable = true, index = true)
    private Integer topCcmtsVifPriIpVlanId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.3.1.2", writable = true)
    private String topCcmtsVifPriIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.3.1.3", writable = true)
    private String topCcmtsVifPriIpMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.3.1.4", writable = true)
    private Integer topCcmtsVifPriIpStatus;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getTopCcmtsVifPriIpVlanId() {
        return topCcmtsVifPriIpVlanId;
    }

    public void setTopCcmtsVifPriIpVlanId(Integer topCcmtsVifPriIpVlanId) {
        this.topCcmtsVifPriIpVlanId = topCcmtsVifPriIpVlanId;
    }

    public String getTopCcmtsVifPriIpAddr() {
        return topCcmtsVifPriIpAddr;
    }

    public void setTopCcmtsVifPriIpAddr(String topCcmtsVifPriIpAddr) {
        this.topCcmtsVifPriIpAddr = topCcmtsVifPriIpAddr;
    }

    public String getTopCcmtsVifPriIpMask() {
        return topCcmtsVifPriIpMask;
    }

    public void setTopCcmtsVifPriIpMask(String topCcmtsVifPriIpMask) {
        this.topCcmtsVifPriIpMask = topCcmtsVifPriIpMask;
    }

    public Integer getTopCcmtsVifPriIpStatus() {
        return topCcmtsVifPriIpStatus;
    }

    public void setTopCcmtsVifPriIpStatus(Integer topCcmtsVifPriIpStatus) {
        this.topCcmtsVifPriIpStatus = topCcmtsVifPriIpStatus;
    }

}
