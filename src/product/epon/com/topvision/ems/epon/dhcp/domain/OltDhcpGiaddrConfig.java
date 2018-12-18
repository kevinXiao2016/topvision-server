/***********************************************************************
 * $Id: OltDhcpGiaddrConfig.java,v1.0 2011-11-17 上午09:38:48 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2011-11-17-上午09:38:48
 * 
 */
public class OltDhcpGiaddrConfig implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6943348705062277079L;
    private Long entityId;
    private String onuType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.3.1.1", index = true)
    private Integer topOltDHCPGiaddrIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.3.1.2", writable = true, type = "Integer32")
    private Integer topOltDHCPGiaddrVid;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.3.1.3", writable = true, type = "IpAddress")
    private String topOltDHCPGiaddrIpAddr;
    private Long topOltDHCPGiaddrIpAddrLong;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.3.1.4", writable = true, type = "Integer32")
    private Integer topOltDHCPGiaddrRowStatus;

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
     * @return the onuType
     */
    public String getOnuType() {
        return onuType;
    }

    /**
     * @param onuType
     *            the onuType to set
     */
    public void setOnuType(String onuType) {
        this.onuType = onuType;
    }

    /**
     * @return the topOltDHCPGiaddrIndex
     */
    public Integer getTopOltDHCPGiaddrIndex() {
        return topOltDHCPGiaddrIndex;
    }

    /**
     * @param topOltDHCPGiaddrIndex
     *            the topOltDHCPGiaddrIndex to set
     */
    public void setTopOltDHCPGiaddrIndex(Integer topOltDHCPGiaddrIndex) {
        this.topOltDHCPGiaddrIndex = topOltDHCPGiaddrIndex;
        if (topOltDHCPGiaddrIndex == 0) {
            onuType = "HOST";
        } else if (topOltDHCPGiaddrIndex == 1) {
            onuType = "CM";
        } else if (topOltDHCPGiaddrIndex == 2) {
            onuType = "STB";
        } else if (topOltDHCPGiaddrIndex == 3) {
            onuType = "MTA";
        } else {
            onuType = "DEFAULT";
        }
    }

    /**
     * @return the topOltDHCPGiaddrVid
     */
    public Integer getTopOltDHCPGiaddrVid() {
        return topOltDHCPGiaddrVid;
    }

    /**
     * @param topOltDHCPGiaddrVid
     *            the topOltDHCPGiaddrVid to set
     */
    public void setTopOltDHCPGiaddrVid(Integer topOltDHCPGiaddrVid) {
        this.topOltDHCPGiaddrVid = topOltDHCPGiaddrVid;
    }

    /**
     * @return the topOltDHCPGiaddrIpAddr
     */
    public String getTopOltDHCPGiaddrIpAddr() {
        return topOltDHCPGiaddrIpAddr;
    }

    /**
     * @param topOltDHCPGiaddrIpAddr
     *            the topOltDHCPGiaddrIpAddr to set
     */
    public void setTopOltDHCPGiaddrIpAddr(String topOltDHCPGiaddrIpAddr) {
        this.topOltDHCPGiaddrIpAddr = topOltDHCPGiaddrIpAddr;
        topOltDHCPGiaddrIpAddrLong = new IpUtils(topOltDHCPGiaddrIpAddr).longValue();
    }

    /**
     * @return the topOltDHCPGiaddrRowStatus
     */
    public Integer getTopOltDHCPGiaddrRowStatus() {
        return topOltDHCPGiaddrRowStatus;
    }

    /**
     * @param topOltDHCPGiaddrRowStatus
     *            the topOltDHCPGiaddrRowStatus to set
     */
    public void setTopOltDHCPGiaddrRowStatus(Integer topOltDHCPGiaddrRowStatus) {
        this.topOltDHCPGiaddrRowStatus = topOltDHCPGiaddrRowStatus;
    }

    /**
     * @return the topOltDHCPGiaddrIpAddrLong
     */
    public Long getTopOltDHCPGiaddrIpAddrLong() {
        return topOltDHCPGiaddrIpAddrLong;
    }

    /**
     * @param topOltDHCPGiaddrIpAddrLong
     *            the topOltDHCPGiaddrIpAddrLong to set
     */
    public void setTopOltDHCPGiaddrIpAddrLong(Long topOltDHCPGiaddrIpAddrLong) {
        this.topOltDHCPGiaddrIpAddrLong = topOltDHCPGiaddrIpAddrLong;
        topOltDHCPGiaddrIpAddr = new IpUtils(topOltDHCPGiaddrIpAddrLong).toString();
    }

    public boolean equalsForIndex(OltDhcpGiaddrConfig config) {
        if (this.getOnuType().equals(config.getOnuType())
                && this.getTopOltDHCPGiaddrIndex().equals(config.getTopOltDHCPGiaddrIndex())) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltDhcpGiaddrConfig [entityId=");
        builder.append(entityId);
        builder.append(", topOltDHCPGiaddrIndex=");
        builder.append(topOltDHCPGiaddrIndex);
        builder.append(", topOltDHCPGiaddrVid=");
        builder.append(topOltDHCPGiaddrVid);
        builder.append(", topOltDHCPGiaddrIpAddr=");
        builder.append(topOltDHCPGiaddrIpAddr);
        builder.append("]");
        return builder.toString();
    }

}
