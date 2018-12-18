/***********************************************************************
 * $Id: PortEntity.java,v1.0 2013-7-20 上午09:21:58 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;

/**
 * @author Rod John
 * @created @2013-7-20-上午09:21:58
 * 
 */
@TableProperty(tables = { "default", "ifXTable" })
public class PortEntity implements Serializable {
    private static final long serialVersionUID = -1967561137792566008L;
    @SnmpProperty(table = "default", oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Integer ifIndex;
    @SnmpProperty(table = "default", oid = "1.3.6.1.2.1.2.2.1.2")
    private String ifDescr;
    @SnmpProperty(table = "default", oid = "1.3.6.1.2.1.2.2.1.3")
    private Integer ifType;
    @SnmpProperty(table = "default", oid = "1.3.6.1.2.1.2.2.1.4")
    private Integer ifMtu;
    @SnmpProperty(table = "default", oid = "1.3.6.1.2.1.2.2.1.5")
    private Long ifSpeed;
    @SnmpProperty(table = "default", oid = "1.3.6.1.2.1.2.2.1.6")
    private String ifPhysAddress;
    @SnmpProperty(table = "default", oid = "1.3.6.1.2.1.2.2.1.7")
    private Integer ifAdminStatus;
    @SnmpProperty(table = "default", oid = "1.3.6.1.2.1.2.2.1.8")
    private Integer ifOperStatus;
    @SnmpProperty(table = "default", oid = "1.3.6.1.2.1.2.2.1.9")
    private String ifLastChange;
    @SnmpProperty(table = "ifXTable", oid = "1.3.6.1.2.1.31.1.1.1.1")
    private String ifName;
    @SnmpProperty(table = "ifXTable", oid = "1.3.6.1.2.1.31.1.1.1.18")
    private String ifAlias;

    /**
     * @return the ifIndex
     */
    public Integer getIfIndex() {
        return ifIndex;
    }

    /**
     * @param ifIndex
     *            the ifIndex to set
     */
    public void setIfIndex(Integer ifIndex) {
        this.ifIndex = ifIndex;
    }

    /**
     * @return the ifDescr
     */
    public String getIfDescr() {
        return ifDescr;
    }

    /**
     * @param ifDescr
     *            the ifDescr to set
     */
    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }

    /**
     * @return the ifType
     */
    public Integer getIfType() {
        return ifType;
    }

    /**
     * @param ifType
     *            the ifType to set
     */
    public void setIfType(Integer ifType) {
        this.ifType = ifType;
    }

    /**
     * @return the ifMtu
     */
    public Integer getIfMtu() {
        return ifMtu;
    }

    /**
     * @param ifMtu
     *            the ifMtu to set
     */
    public void setIfMtu(Integer ifMtu) {
        this.ifMtu = ifMtu;
    }

    /**
     * @return the ifSpeed
     */
    public Long getIfSpeed() {
        return ifSpeed;
    }

    /**
     * @param ifSpeed
     *            the ifSpeed to set
     */
    public void setIfSpeed(Long ifSpeed) {
        this.ifSpeed = ifSpeed;
    }

    /**
     * @return the ifPhysAddress
     */
    public String getIfPhysAddress() {
        return ifPhysAddress;
    }

    /**
     * @param ifPhysAddress
     *            the ifPhysAddress to set
     */
    public void setIfPhysAddress(String ifPhysAddress) {
        this.ifPhysAddress = ifPhysAddress;
    }

    /**
     * @return the ifAdminStatus
     */
    public Integer getIfAdminStatus() {
        return ifAdminStatus;
    }

    /**
     * @param ifAdminStatus
     *            the ifAdminStatus to set
     */
    public void setIfAdminStatus(Integer ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }

    /**
     * @return the ifOperStatus
     */
    public Integer getIfOperStatus() {
        return ifOperStatus;
    }

    /**
     * @param ifOperStatus
     *            the ifOperStatus to set
     */
    public void setIfOperStatus(Integer ifOperStatus) {
        this.ifOperStatus = ifOperStatus;
    }

    /**
     * @return the ifLastChange
     */
    public String getIfLastChange() {
        return ifLastChange;
    }

    /**
     * @param ifLastChange
     *            the ifLastChange to set
     */
    public void setIfLastChange(String ifLastChange) {
        this.ifLastChange = ifLastChange;
    }

    /**
     * @return the ifName
     */
    public String getIfName() {
        return ifName;
    }

    /**
     * @param ifName
     *            the ifName to set
     */
    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

    /**
     * @return the ifAlias
     */
    public String getIfAlias() {
        return ifAlias;
    }

    /**
     * @param ifAlias
     *            the ifAlias to set
     */
    public void setIfAlias(String ifAlias) {
        this.ifAlias = ifAlias;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortEntity [ifIndex=");
        builder.append(ifIndex);
        builder.append(", ifDescr=");
        builder.append(ifDescr);
        builder.append(", ifType=");
        builder.append(ifType);
        builder.append(", ifMtu=");
        builder.append(ifMtu);
        builder.append(", ifSpeed=");
        builder.append(ifSpeed);
        builder.append(", ifPhysAddress=");
        builder.append(ifPhysAddress);
        builder.append(", ifAdminStatus=");
        builder.append(ifAdminStatus);
        builder.append(", ifOperStatus=");
        builder.append(ifOperStatus);
        builder.append(", ifLastChange=");
        builder.append(ifLastChange);
        builder.append(", ifName=");
        builder.append(ifName);
        builder.append(", ifAlias=");
        builder.append(ifAlias);
        builder.append("]");
        return builder.toString();
    }

}
