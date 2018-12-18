/***********************************************************************
 * $Id: OltDhcpIpMacDynamic.java,v1.0 2011-11-17 下午04:49:10 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2011-11-17-下午04:49:10
 * 
 */
public class OltDhcpIpMacDynamic implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 6209255505300575829L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.5.1.1", index = true)
    private Integer topOltDHCPDynIpMacIdx;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.5.1.2", writable = true, type = "OctetString")
    private String topOltDHCPDynIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.5.1.3", writable = true, type = "OctetString")
    private String topOltDHCPDynMacAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.5.1.4", writable = true, type = "OctetString")
    private String topOltDHCPDynOnuMacAddr;

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
     * @return the topOltDHCPDynIpMacIdx
     */
    public Integer getTopOltDHCPDynIpMacIdx() {
        return topOltDHCPDynIpMacIdx;
    }

    /**
     * @param topOltDHCPDynIpMacIdx
     *            the topOltDHCPDynIpMacIdx to set
     */
    public void setTopOltDHCPDynIpMacIdx(Integer topOltDHCPDynIpMacIdx) {
        this.topOltDHCPDynIpMacIdx = topOltDHCPDynIpMacIdx;
    }

    /**
     * @return the topOltDHCPDynIpAddr
     */
    public String getTopOltDHCPDynIpAddr() {
        return topOltDHCPDynIpAddr;
    }

    /**
     * @param topOltDHCPDynIpAddr
     *            the topOltDHCPDynIpAddr to set
     */
    public void setTopOltDHCPDynIpAddr(String topOltDHCPDynIpAddr) {
        this.topOltDHCPDynIpAddr = topOltDHCPDynIpAddr;
    }

    /**
     * @return the topOltDHCPDynMacAddr
     */
    public String getTopOltDHCPDynMacAddr() {
        return topOltDHCPDynMacAddr;
    }

    /**
     * @param topOltDHCPDynMacAddr
     *            the topOltDHCPDynMacAddr to set
     */
    public void setTopOltDHCPDynMacAddr(String topOltDHCPDynMacAddr) {
        this.topOltDHCPDynMacAddr = topOltDHCPDynMacAddr;
    }

    /**
     * @return the topOltDHCPDynOnuMacAddr
     */
    public String getTopOltDHCPDynOnuMacAddr() {
        return topOltDHCPDynOnuMacAddr;
    }

    /**
     * @param topOltDHCPDynOnuMacAddr
     *            the topOltDHCPDynOnuMacAddr to set
     */
    public void setTopOltDHCPDynOnuMacAddr(String topOltDHCPDynOnuMacAddr) {
        this.topOltDHCPDynOnuMacAddr = topOltDHCPDynOnuMacAddr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltDhcpIpMacDynamic [entityId=");
        builder.append(entityId);
        builder.append(", topOltDHCPDynIpMacIdx=");
        builder.append(topOltDHCPDynIpMacIdx);
        builder.append(", topOltDHCPDynIpAddr=");
        builder.append(topOltDHCPDynIpAddr);
        builder.append(", topOltDHCPDynMacAddr=");
        builder.append(topOltDHCPDynMacAddr);
        builder.append(", topOltDHCPDynOnuMacAddr=");
        builder.append(topOltDHCPDynOnuMacAddr);
        builder.append("]");
        return builder.toString();
    }

}
