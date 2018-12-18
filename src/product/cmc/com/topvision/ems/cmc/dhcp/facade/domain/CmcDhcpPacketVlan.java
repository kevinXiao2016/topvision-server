/***********************************************************************
 * $Id: CmcDhcpPacketVlan.java,v1.0 2013-4-22 下午2:24:14 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.dhcp.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author dosion
 * @created @2013-4-22-下午2:24:14
 *
 */
@Alias("cmcDhcpPacketVlan")
public class CmcDhcpPacketVlan implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -3413216362943747151L;
    private Long packetVlanId;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.1", index = true)
    private String topCcmtsDhcpBundleInterface;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.7.1.1", index = true)
    private Integer topCcmtsDhcpDeviceType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.7.1.2", writable = true, type = "Integer32")
    private Integer topCcmtsDhcpTagVlan;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.7.1.3", writable = true, type = "Integer32")
    private Integer topCcmtsDhcpTagPriority;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.7.1.4", writable = true, type = "Integer32")
    private Integer topCcmtsDhcpPacketVlanStatus;
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
    /**
     * @return the topCcmtsDhcpBundleInterface
     */
    public String getTopCcmtsDhcpBundleInterface() {
        return topCcmtsDhcpBundleInterface;
    }
    /**
     * @param topCcmtsDhcpBundleInterface the topCcmtsDhcpBundleInterface to set
     */
    public void setTopCcmtsDhcpBundleInterface(String topCcmtsDhcpBundleInterface) {
        this.topCcmtsDhcpBundleInterface = topCcmtsDhcpBundleInterface;
    }

    /**
     * @return the topCcmtsDhcpDeviceType
     */
    public Integer getTopCcmtsDhcpDeviceType() {
        return topCcmtsDhcpDeviceType;
    }
    /**
     * @param topCcmtsDhcpDeviceType the topCcmtsDhcpDeviceType to set
     */
    public void setTopCcmtsDhcpDeviceType(Integer topCcmtsDhcpDeviceType) {
        this.topCcmtsDhcpDeviceType = topCcmtsDhcpDeviceType;
    }
    /**
     * @return the topCcmtsDhcpTagVlan
     */
    public Integer getTopCcmtsDhcpTagVlan() {
        return topCcmtsDhcpTagVlan;
    }
    /**
     * @param topCcmtsDhcpTagVlan the topCcmtsDhcpTagVlan to set
     */
    public void setTopCcmtsDhcpTagVlan(Integer topCcmtsDhcpTagVlan) {
        this.topCcmtsDhcpTagVlan = topCcmtsDhcpTagVlan;
    }
    /**
     * @return the topCcmtsDhcpTagPriority
     */
    public Integer getTopCcmtsDhcpTagPriority() {
        return topCcmtsDhcpTagPriority;
    }
    /**
     * @param topCcmtsDhcpTagPriority the topCcmtsDhcpTagPriority to set
     */
    public void setTopCcmtsDhcpTagPriority(Integer topCcmtsDhcpTagPriority) {
        this.topCcmtsDhcpTagPriority = topCcmtsDhcpTagPriority;
    }
    /**
     * @return the topCcmtsDhcpPacketVlanStatus
     */
    public Integer getTopCcmtsDhcpPacketVlanStatus() {
        return topCcmtsDhcpPacketVlanStatus;
    }
    /**
     * @param topCcmtsDhcpPacketVlanStatus the topCcmtsDhcpPacketVlanStatus to set
     */
    public void setTopCcmtsDhcpPacketVlanStatus(Integer topCcmtsDhcpPacketVlanStatus) {
        this.topCcmtsDhcpPacketVlanStatus = topCcmtsDhcpPacketVlanStatus;
    }
    /**
     * @return the packetVlanId
     */
    public Long getPacketVlanId() {
        return packetVlanId;
    }
    /**
     * @param packetVlanId the packetVlanId to set
     */
    public void setPacketVlanId(Long packetVlanId) {
        this.packetVlanId = packetVlanId;
    }
    
    public CmcDhcpPacketVlan clone(){
        CmcDhcpPacketVlan cl = new CmcDhcpPacketVlan();
        cl.setEntityId(entityId);
        cl.setPacketVlanId(packetVlanId);
        cl.setTopCcmtsDhcpBundleInterface(topCcmtsDhcpBundleInterface);
        cl.setTopCcmtsDhcpDeviceType(topCcmtsDhcpDeviceType);
        cl.setTopCcmtsDhcpPacketVlanStatus(topCcmtsDhcpPacketVlanStatus);
        cl.setTopCcmtsDhcpTagPriority(topCcmtsDhcpTagPriority);
        cl.setTopCcmtsDhcpTagVlan(topCcmtsDhcpTagPriority);
        return cl;
    }
    
}
