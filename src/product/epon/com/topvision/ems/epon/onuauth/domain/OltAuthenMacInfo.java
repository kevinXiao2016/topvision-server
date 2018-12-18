/***********************************************************************
 * $Id: OltAuthenMacInfo.java,v1.0 2011-10-17 下午05:19:12 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.domain;

import java.io.Serializable;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author huqiao
 * @created @2011-10-17-下午05:19:12
 * 
 */
public class OltAuthenMacInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 1L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.5.2.1.1,V1.10:1.3.6.1.4.1.32285.11.2.3.4.2.4.1.1", index = true)
    private Long onuMibIndex;
    private Long onuIndex; // 系统中所用Index
    private Integer authType;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.5.2.1.2,V1.10:1.3.6.1.4.1.32285.11.2.3.4.2.4.1.2", writable = true, type = "OctetString")
    private String onuAuthenMacAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.5.2.1.3,V1.10:1.3.6.1.4.1.32285.11.2.3.4.2.4.1.3", writable = true, type = "Integer32")
    private Integer authAction;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.5.2.1.4,V1.10:1.3.6.1.4.1.32285.11.2.3.4.2.4.1.4", writable = true, type = "Integer32")
    private Integer onuAuthenRowStatus;
    private Integer onuPreType;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
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
        onuIndex = EponIndex.getOnuIndexByMibIndex(onuMibIndex);
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        onuMibIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
    }

    /**
     * @return the authType
     */
    public Integer getAuthType() {
        return authType;
    }

    /**
     * @param authType
     *            the authType to set
     */
    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    /**
     * @return the authAction
     */
    public Integer getAuthAction() {
        return authAction;
    }

    /**
     * @param authAction
     *            the authAction to set
     */
    public void setAuthAction(Integer authAction) {
        this.authAction = authAction;
    }

    /**
     * @return the onuAuthenMacAddress
     */
    public String getOnuAuthenMacAddress() {
        return onuAuthenMacAddress;
    }

    /**
     * @param onuAuthenMacAddress
     *            the onuAuthenMacAddress to set
     */
    public void setOnuAuthenMacAddress(String onuAuthenMacAddress) {
        this.onuAuthenMacAddress = EponUtil.getMacStringFromNoISOControl(onuAuthenMacAddress);
    }

    /**
     * @return the onuAuthenRowStatus
     */
    public Integer getOnuAuthenRowStatus() {
        return onuAuthenRowStatus;
    }

    /**
     * @param onuAuthenRowStatus
     *            the onuAuthenRowStatus to set
     */
    public void setOnuAuthenRowStatus(Integer onuAuthenRowStatus) {
        this.onuAuthenRowStatus = onuAuthenRowStatus;
    }

    /**
     * @return the onuPreType
     */
    public Integer getOnuPreType() {
        return onuPreType;
    }

    /**
     * @param onuPreType the onuPreType to set
     */
    public void setOnuPreType(Integer onuPreType) {
        this.onuPreType = onuPreType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltAuthenMacInfo [entityId=");
        builder.append(entityId);
        builder.append(", onuMibIndex=");
        builder.append(onuMibIndex);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", authType=");
        builder.append(authType);
        builder.append(", onuAuthenMacAddress=");
        builder.append(onuAuthenMacAddress);
        builder.append(", authAction=");
        builder.append(authAction);
        builder.append(", onuAuthenRowStatus=");
        builder.append(onuAuthenRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
