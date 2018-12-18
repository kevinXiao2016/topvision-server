/***********************************************************************
 * $Id: OnuWanSsid.java,v1.0 2016年5月30日 下午5:00:10 $
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
 * @created @2016年5月30日-下午5:00:10
 * 
 */
public class OnuWanSsid implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 9157440975047385361L;
    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.2.1.1,V1.10:1.3.6.1.4.1.17409.2.9.1.1.2.1.1", index = true)
    private Long onuMibIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.2.1.2,V1.10:1.3.6.1.4.1.17409.2.9.1.1.2.1.2", index = true)
    private Integer ssid;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.2.1.3,V1.10:1.3.6.1.4.1.17409.2.9.1.1.2.1.3", writable = true)
    private String ssidName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.2.1.4,V1.10:1.3.6.1.4.1.17409.2.9.1.1.2.1.4", writable = true, type = "Integer32")
    private Integer encryptMode;// 无线加密模式
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.2.1.5,V1.10:1.3.6.1.4.1.17409.2.9.1.1.2.1.5", writable = true)
    private String password;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.2.1.6,V1.10:1.3.6.1.4.1.17409.2.9.1.1.2.1.6", writable = true, type = "Integer32")
    private Integer ssidEnnable;// ssid使能
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.2.1.7,V1.10:1.3.6.1.4.1.17409.2.9.1.1.2.1.7", writable = true, type = "Integer32")
    private Integer ssidBroadcastEnnable;// ssid广播使能
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.2.1.8,V1.10:1.3.6.1.4.1.17409.2.9.1.1.2.1.8", writable = true, type = "Integer32")
    private Integer ssidMaxUser;// 最大用户接入数
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.2.1.9,V1.10:1.3.6.1.4.1.17409.2.9.1.1.2.1.9", writable = true, type = "Integer32")
    private Integer ssidRowStatus;

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

    public Integer getSsid() {
        return ssid;
    }

    public void setSsid(Integer ssid) {
        this.ssid = ssid;
    }

    public String getSsidName() {
        return ssidName;
    }

    public void setSsidName(String ssidName) {
        this.ssidName = ssidName;
    }

    public Integer getEncryptMode() {
        return encryptMode;
    }

    public void setEncryptMode(Integer encryptMode) {
        this.encryptMode = encryptMode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSsidEnnable() {
        return ssidEnnable;
    }

    public void setSsidEnnable(Integer ssidEnnable) {
        this.ssidEnnable = ssidEnnable;
    }

    public Integer getSsidBroadcastEnnable() {
        return ssidBroadcastEnnable;
    }

    public void setSsidBroadcastEnnable(Integer ssidBroadcastEnnable) {
        this.ssidBroadcastEnnable = ssidBroadcastEnnable;
    }

    public Integer getSsidMaxUser() {
        return ssidMaxUser;
    }

    public void setSsidMaxUser(Integer ssidMaxUser) {
        this.ssidMaxUser = ssidMaxUser;
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

    /**
     * @return the ssidRowStatus
     */
    public Integer getSsidRowStatus() {
        return ssidRowStatus;
    }

    /**
     * @param ssidRowStatus
     *            the ssidRowStatus to set
     */
    public void setSsidRowStatus(Integer ssidRowStatus) {
        this.ssidRowStatus = ssidRowStatus;
    }

}
