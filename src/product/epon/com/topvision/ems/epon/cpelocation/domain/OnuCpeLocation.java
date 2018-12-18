/***********************************************************************
 * $Id: OnuCpeLocation.java,v1.0 2016-5-6 上午9:22:40 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.cpelocation.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.PhysAddress;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2016-5-6-上午9:22:40
 *
 */
public class OnuCpeLocation implements AliasesSuperType {
    private static final long serialVersionUID = -1542362332696660128L;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.8.4.1.1", index = true)
    private PhysAddress macLocIndex;
    private String macLocation;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.8.4.1.2", type = "Integer32")
    private Integer slotLocation;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.8.4.1.3", type = "Integer32")
    private Integer portLocation;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.8.4.1.4", type = "Integer32")
    private Integer onuLocation;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.8.4.1.5", type = "Integer32")
    private Integer uniLocation;

    private Long entityId;
    private Long onuId;
    private String oltName;
    private String oltIp;
    private String onuName;
    private Long onuIndex;

    private String ipAddress;
    private Integer cpeType;
    private Integer vlanId;
    private Integer macType;
    private Boolean isGpon;

    public String getUniDisplay() {
        return String.format("%s/%s:%s:%s", slotLocation, portLocation, onuLocation, uniLocation);
    }

    public String getMacLocation() {
        if (macLocation == null && macLocIndex != null) {
            macLocation = macLocIndex.toString();
        }
        return macLocation;
    }

    public void setMacLocation(String macLocation) {
        this.macLocation = macLocation;
    }

    public Integer getSlotLocation() {
        return slotLocation;
    }

    public void setSlotLocation(Integer slotLocation) {
        this.slotLocation = slotLocation;
    }

    public Integer getPortLocation() {
        return portLocation;
    }

    public void setPortLocation(Integer portLocation) {
        this.portLocation = portLocation;
    }

    public Integer getOnuLocation() {
        return onuLocation;
    }

    public void setOnuLocation(Integer onuLocation) {
        this.onuLocation = onuLocation;
    }

    public Integer getUniLocation() {
        return uniLocation;
    }

    public void setUniLocation(Integer uniLocation) {
        this.uniLocation = uniLocation;
    }

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

    public String getOltName() {
        return oltName;
    }

    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    public String getOltIp() {
        return oltIp;
    }

    public void setOltIp(String oltIp) {
        this.oltIp = oltIp;
    }

    public String getOnuName() {
        return onuName;
    }

    public void setOnuName(String onuName) {
        this.onuName = onuName;
    }

    public Long getOnuIndex() {
        if (onuIndex == null) {
            onuIndex = EponIndex.getOnuIndex(this.slotLocation, this.portLocation, this.onuLocation);
        }
        return onuIndex;
    }

    public Long getUniIndex() {
        return EponIndex.getUniIndex(this.slotLocation, this.portLocation, this.onuLocation, uniLocation);
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public PhysAddress getMacLocIndex() {
        if (macLocIndex == null && macLocation != null) {
            macLocIndex = new PhysAddress(macLocation);
        }
        return macLocIndex;
    }

    public void setMacLocIndex(PhysAddress macLocIndex) {
        this.macLocIndex = macLocIndex;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getCpeType() {
        return cpeType;
    }

    public void setCpeType(Integer cpeType) {
        this.cpeType = cpeType;
    }

    public Integer getVlanId() {
        return vlanId;
    }

    public void setVlanId(Integer vlanId) {
        this.vlanId = vlanId;
    }

    public Integer getMacType() {
        return macType;
    }

    public void setMacType(Integer macType) {
        this.macType = macType;
    }

    public Boolean getIsGpon() {
        return isGpon;
    }

    public void setIsGpon(Boolean isGpon) {
        this.isGpon = isGpon;
    }

}
