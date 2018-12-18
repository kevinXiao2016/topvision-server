/***********************************************************************
 * $Id: LoopbackConfigTable.java,v1.0 2013-11-16 下午05:12:18 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.loopback.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2013-11-16-下午05:12:18
 * 
 */
public class LoopbackConfigTable implements AliasesSuperType {
    private static final long serialVersionUID = 361250971500291990L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.6.1.1.1", index = true)
    private Integer loopbackIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.6.1.1.2", writable = true, type = "IpAddress")
    private String loopbackPriIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.6.1.1.3", writable = true, type = "IpAddress")
    private String loopbackPriMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.6.1.1.4")
    private String loopbackPriMac;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.6.1.1.5", writable = true, type = "Integer32")
    private Integer loopbackRowStatus;

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
     * @return the loopbackIndex
     */
    public Integer getLoopbackIndex() {
        return loopbackIndex;
    }

    /**
     * @param loopbackIndex
     *            the loopbackIndex to set
     */
    public void setLoopbackIndex(Integer loopbackIndex) {
        this.loopbackIndex = loopbackIndex;
    }

    /**
     * @return the loopbackPriIpAddr
     */
    public String getLoopbackPriIpAddr() {
        return loopbackPriIpAddr;
    }

    /**
     * @param loopbackPriIpAddr
     *            the loopbackPriIpAddr to set
     */
    public void setLoopbackPriIpAddr(String loopbackPriIpAddr) {
        this.loopbackPriIpAddr = loopbackPriIpAddr;
    }

    /**
     * @return the loopbackPriMask
     */
    public String getLoopbackPriMask() {
        return loopbackPriMask;
    }

    /**
     * @param loopbackPriMask
     *            the loopbackPriMask to set
     */
    public void setLoopbackPriMask(String loopbackPriMask) {
        this.loopbackPriMask = loopbackPriMask;
    }

    /**
     * @return the loopbackPriMac
     */
    public String getLoopbackPriMac() {
        return loopbackPriMac;
    }

    /**
     * @param loopbackPriMac
     *            the loopbackPriMac to set
     */
    public void setLoopbackPriMac(String loopbackPriMac) {
        this.loopbackPriMac = loopbackPriMac;
    }

    /**
     * @return the loopbackRowStatus
     */
    public Integer getLoopbackRowStatus() {
        return loopbackRowStatus;
    }

    /**
     * @param loopbackRowStatus
     *            the loopbackRowStatus to set
     */
    public void setLoopbackRowStatus(Integer loopbackRowStatus) {
        this.loopbackRowStatus = loopbackRowStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LoopbackConfigTable [entityId=");
        builder.append(entityId);
        builder.append(", loopbackIndex=");
        builder.append(loopbackIndex);
        builder.append(", loopbackPriIpAddr=");
        builder.append(loopbackPriIpAddr);
        builder.append(", loopbackPriMask=");
        builder.append(loopbackPriMask);
        builder.append(", loopbackPriMac=");
        builder.append(loopbackPriMac);
        builder.append(", loopbackRowStatus=");
        builder.append(loopbackRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
