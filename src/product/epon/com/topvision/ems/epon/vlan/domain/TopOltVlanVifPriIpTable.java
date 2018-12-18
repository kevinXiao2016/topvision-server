/***********************************************************************
 * $Id: TopOltVlanVifPriIpTable.java,v1.0 2012-4-17 下午07:10:34 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2012-4-17-下午07:10:34
 * 
 */
@TableProperty(tables = { "default" })
public class TopOltVlanVifPriIpTable implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -684635690561923117L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.4.1.1.1", index = true)
    private Integer topOltVifPriIpVlanIdx;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.4.1.1.2", writable = true, type = "IpAddress")
    private String topOltVifPriIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.4.1.1.3", writable = true, type = "IpAddress")
    private String topOltVifPriIpMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.4.1.1.4", writable = true, type = "Integer32")
    private Integer topOltVifPriIpRowStatus;

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
     * @return the topOltVifPriIpVlanIdx
     */
    public Integer getTopOltVifPriIpVlanIdx() {
        return topOltVifPriIpVlanIdx;
    }

    /**
     * @param topOltVifPriIpVlanIdx
     *            the topOltVifPriIpVlanIdx to set
     */
    public void setTopOltVifPriIpVlanIdx(Integer topOltVifPriIpVlanIdx) {
        this.topOltVifPriIpVlanIdx = topOltVifPriIpVlanIdx;
    }

    /**
     * @return the topOltVifPriIpAddr
     */
    public String getTopOltVifPriIpAddr() {
        return topOltVifPriIpAddr;
    }

    /**
     * @param topOltVifPriIpAddr
     *            the topOltVifPriIpAddr to set
     */
    public void setTopOltVifPriIpAddr(String topOltVifPriIpAddr) {
        this.topOltVifPriIpAddr = topOltVifPriIpAddr;
    }

    /**
     * @return the topOltVifPriIpMask
     */
    public String getTopOltVifPriIpMask() {
        return topOltVifPriIpMask;
    }

    /**
     * @param topOltVifPriIpMask
     *            the topOltVifPriIpMask to set
     */
    public void setTopOltVifPriIpMask(String topOltVifPriIpMask) {
        this.topOltVifPriIpMask = topOltVifPriIpMask;
    }

    /**
     * @return the topOltVifPriIpRowStatus
     */
    public Integer getTopOltVifPriIpRowStatus() {
        return topOltVifPriIpRowStatus;
    }

    /**
     * @param topOltVifPriIpRowStatus
     *            the topOltVifPriIpRowStatus to set
     */
    public void setTopOltVifPriIpRowStatus(Integer topOltVifPriIpRowStatus) {
        this.topOltVifPriIpRowStatus = topOltVifPriIpRowStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopOltVlanVifPriIpTable [entityId=");
        builder.append(entityId);
        builder.append(", topOltVifPriIpVlanIdx=");
        builder.append(topOltVifPriIpVlanIdx);
        builder.append(", topOltVifPriIpAddr=");
        builder.append(topOltVifPriIpAddr);
        builder.append(", topOltVifPriIpMask=");
        builder.append(topOltVifPriIpMask);
        builder.append(", topOltVifPriIpRowStatus=");
        builder.append(topOltVifPriIpRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
