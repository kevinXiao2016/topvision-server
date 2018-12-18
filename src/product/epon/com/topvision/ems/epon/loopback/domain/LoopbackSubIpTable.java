/***********************************************************************
 * $Id: LoopbackSubIpTable.java,v1.0 2013-11-16 下午05:12:37 $
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
 * @created @2013-11-16-下午05:12:37
 *
 */
public class LoopbackSubIpTable implements AliasesSuperType{
    private static final long serialVersionUID = -7061397741750799677L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.6.2.1.1", index = true)
    private Integer loopbackSubIpIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.6.2.1.2", index = true)
    private Integer loopbackSubIpSeqIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.6.2.1.3", writable = true, type = "IpAddress")
    private String loopbackSubIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.6.2.1.4", writable = true, type = "IpAddress")
    private String loopbackSubMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.6.2.1.5")
    private String loopbackSubMac;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.6.2.1.6", writable = true, type = "Integer32")
    private Integer loopbackSubIpRowStatus;

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

    public Integer getLoopbackSubIpIndex() {
        return loopbackSubIpIndex;
    }

    public void setLoopbackSubIpIndex(Integer loopbackSubIpIndex) {
        this.loopbackSubIpIndex = loopbackSubIpIndex;
    }

    public Integer getLoopbackSubIpSeqIndex() {
        return loopbackSubIpSeqIndex;
    }

    public void setLoopbackSubIpSeqIndex(Integer loopbackSubIpSeqIndex) {
        this.loopbackSubIpSeqIndex = loopbackSubIpSeqIndex;
    }

    /**
     * @return the loopbackSubIpAddr
     */
    public String getLoopbackSubIpAddr() {
        return loopbackSubIpAddr;
    }

    /**
     * @param loopbackSubIpAddr the loopbackSubIpAddr to set
     */
    public void setLoopbackSubIpAddr(String loopbackSubIpAddr) {
        this.loopbackSubIpAddr = loopbackSubIpAddr;
    }

    /**
     * @return the loopbackSubMask
     */
    public String getLoopbackSubMask() {
        return loopbackSubMask;
    }

    /**
     * @param loopbackSubMask the loopbackSubMask to set
     */
    public void setLoopbackSubMask(String loopbackSubMask) {
        this.loopbackSubMask = loopbackSubMask;
    }

    /**
     * @return the loopbackSubMac
     */
    public String getLoopbackSubMac() {
        return loopbackSubMac;
    }

    /**
     * @param loopbackSubMac the loopbackSubMac to set
     */
    public void setLoopbackSubMac(String loopbackSubMac) {
        this.loopbackSubMac = loopbackSubMac;
    }

    /**
     * @return the loopbackSubIpRowStatus
     */
    public Integer getLoopbackSubIpRowStatus() {
        return loopbackSubIpRowStatus;
    }

    /**
     * @param loopbackSubIpRowStatus the loopbackSubIpRowStatus to set
     */
    public void setLoopbackSubIpRowStatus(Integer loopbackSubIpRowStatus) {
        this.loopbackSubIpRowStatus = loopbackSubIpRowStatus;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LoopbackSubIpTable [entityId=");
        builder.append(entityId);
        builder.append(", loopbackSubIpIndex=");
        builder.append(loopbackSubIpIndex);
        builder.append(", loopbackSubIpSeqIndex=");
        builder.append(loopbackSubIpSeqIndex);
        builder.append(", loopbackSubIpAddr=");
        builder.append(loopbackSubIpAddr);
        builder.append(", loopbackSubMask=");
        builder.append(loopbackSubMask);
        builder.append(", loopbackSubMac=");
        builder.append(loopbackSubMac);
        builder.append(", loopbackSubIpRowStatus=");
        builder.append(loopbackSubIpRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
