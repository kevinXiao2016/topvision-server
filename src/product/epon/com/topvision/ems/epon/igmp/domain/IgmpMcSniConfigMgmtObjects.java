/***********************************************************************
 * $ IgmpMcSniConfigMgmtObjects.java,v1.0 2011-11-23 9:31:25 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.domain;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author jay
 * @created @2011-11-23-9:31:25
 */
@TableProperty(tables = { "default" })
public class IgmpMcSniConfigMgmtObjects implements AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;

    /**
     * config multicast uplink port type, one record only allow to exists. default value indicate
     * that all uplinks ports will transit/receive igmp package, in case they have received such
     * package. INTEGER { anyPort(0), phyPort(1), aggPort(2) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.2.1.0", writable = true, type = "Integer32")
    private Integer topMcSniPortType;
    /**
     * From MSB, the first octet is SLOT-INDEX, and then PORT-INDEX. OCTET STRING (SIZE (2))
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.2.2.0", writable = true, type = "OctetString")
    private String topMcSniPort;
    private Long topMcSniIndex;
    /**
     * uplink Agg group id
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.2.3.0", writable = true, type = "Integer32")
    private Integer topMcSniAggPort;
    /**
     * aging time, unit: seconds INTEGER (1..2000)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.2.4.0", writable = true, type = "Integer32")
    private Integer topMcSniAgingTime;

    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.2.5.0", writable = true, type = "Integer32")
    private Integer topMcSniPhyPortType;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopMcSniAggPort() {
        return topMcSniAggPort;
    }

    public void setTopMcSniAggPort(Integer topMcSniAggPort) {
        this.topMcSniAggPort = topMcSniAggPort;
    }

    public Integer getTopMcSniAgingTime() {
        return topMcSniAgingTime;
    }

    public void setTopMcSniAgingTime(Integer topMcSniAgingTime) {
        this.topMcSniAgingTime = topMcSniAgingTime;
    }

    public String getTopMcSniPort() {
        return topMcSniPort;
    }

    public void setTopMcSniPort(String topMcSniPort) {
        this.topMcSniPort = topMcSniPort;
        if (topMcSniPort != null && topMcSniPort.split(":").length == 2) {
            topMcSniIndex = EponIndex.getSniIndex(Integer.parseInt(topMcSniPort.split(":")[0], 16),
                    Integer.parseInt(topMcSniPort.split(":")[1], 16));
        }
    }

    /**
     * @return the topMcSniIndex
     */
    public Long getTopMcSniIndex() {
        return topMcSniIndex;
    }

    /**
     * @param topMcSniIndex
     *            the topMcSniIndex to set
     */
    public void setTopMcSniIndex(Long topMcSniIndex) {
        this.topMcSniIndex = topMcSniIndex;
        topMcSniPort = EponUtil.getSniPortBitMapFormSniIndex(topMcSniIndex);
    }

    public Integer getTopMcSniPortType() {
        return topMcSniPortType;
    }

    public void setTopMcSniPortType(Integer topMcSniPortType) {
        this.topMcSniPortType = topMcSniPortType;
    }

    public Integer getTopMcSniPhyPortType() {
        return topMcSniPhyPortType;
    }

    public void setTopMcSniPhyPortType(Integer topMcSniPhyPortType) {
        this.topMcSniPhyPortType = topMcSniPhyPortType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IgmpMcSniConfigMgmtObjects");
        sb.append("{entityId=").append(entityId);
        sb.append(", topMcSniPortType=").append(topMcSniPortType);
        sb.append(", topMcSniPort='").append(topMcSniPort).append('\'');
        sb.append(", topMcSniAggPort=").append(topMcSniAggPort);
        sb.append(", topMcSniAgingTime=").append(topMcSniAgingTime);
        sb.append('}');
        return sb.toString();
    }
}
