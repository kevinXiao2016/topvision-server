/***********************************************************************
 * $Id: OnuCpeIpLocation.java,v1.0 2016年9月17日 下午3:30:26 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.cpelocation.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Bravin
 * @created @2016年9月17日-下午3:30:26
 *
 */
public class OnuCpeIpLocation implements Serializable {
    private static final long serialVersionUID = -7847521447495876002L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.12.1.1.1", index = true, type = "OctetString")
    private String ipAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.12.1.1.2", type = "OctetString")
    private String macAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.12.1.1.3", type = "Integer32")
    private Integer slot;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.12.1.1.4", type = "Integer32")
    private Integer pon;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.12.1.1.5", type = "Integer32")
    private Integer onu;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.12.1.1.6", type = "Integer32")
    private Integer cpeType;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    public Integer getPon() {
        return pon;
    }

    public void setPon(Integer pon) {
        this.pon = pon;
    }

    public Integer getOnu() {
        return onu;
    }

    public void setOnu(Integer onu) {
        this.onu = onu;
    }

    public Integer getCpeType() {
        return cpeType;
    }

    public void setCpeType(Integer cpeType) {
        this.cpeType = cpeType;
    }

    @Override
    public String toString() {
        return "OnuCpeIpLocation [ipAddress=" + ipAddress + ", macAddress=" + macAddress + ", slot=" + slot + ", pon="
                + pon + ", onu=" + onu + ", cpeType=" + cpeType + "]";
    }

}
