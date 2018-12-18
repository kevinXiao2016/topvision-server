/***********************************************************************
 * $Id: CmcSystemIpInfo.java,v1.0 2012-2-13 下午02:09:28 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2012-2-13-下午02:09:28
 * 
 */
@Alias("cmcSystemIpInfo")
public class CmcSystemIpInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6465332682803338157L;
    private Long entityId;
    private Long cmcIndex;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.4.1.0", writable = true, type = "IpAddress")
    private String topCcmtsEthIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.4.2.0", writable = true, type = "IpAddress")
    private String topCcmtsEthIpMask;
    /**
     * modified by huangdongsheng 修改OID，使用另外一个节点修改网关
     * 之前修改网关的OID：1.3.6.1.4.1.32285.11.1.1.2.1.4.3
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.1.2.0", writable = true, type = "IpAddress")
    private String topCcmtsEthGateway;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.4.4.0", writable = true, type = "Integer")
    private Long topCcmtsEthVlanId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.4.5.0", writable = true, type = "OctetString")
    private String topCcmtsEthVlanIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.4.6.0", writable = true, type = "OctetString")
    private String topCcmtsEthVlanIpMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.4.7.0", writable = true, type = "OctetString")
    private String topCcmtsEthVlanGateway;

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
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex
     *            the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the topCcmtsEthIpAddr
     */
    public String getTopCcmtsEthIpAddr() {
        return topCcmtsEthIpAddr;
    }

    /**
     * @param topCcmtsEthIpAddr
     *            the topCcmtsEthIpAddr to set
     */
    public void setTopCcmtsEthIpAddr(String topCcmtsEthIpAddr) {
        this.topCcmtsEthIpAddr = topCcmtsEthIpAddr;
    }

    /**
     * @return the topCcmtsEthIpMask
     */
    public String getTopCcmtsEthIpMask() {
        return topCcmtsEthIpMask;
    }

    /**
     * @param topCcmtsEthIpMask
     *            the topCcmtsEthIpMask to set
     */
    public void setTopCcmtsEthIpMask(String topCcmtsEthIpMask) {
        this.topCcmtsEthIpMask = topCcmtsEthIpMask;
    }

    /**
     * @return the topCcmtsEthGateway
     */
    public String getTopCcmtsEthGateway() {
        return topCcmtsEthGateway;
    }

    /**
     * @param topCcmtsEthGateway
     *            the topCcmtsEthGateway to set
     */
    public void setTopCcmtsEthGateway(String topCcmtsEthGateway) {
        this.topCcmtsEthGateway = topCcmtsEthGateway;
    }

    /**
     * @return the topCcmtsEthVlanId
     */
    public Long getTopCcmtsEthVlanId() {
        return topCcmtsEthVlanId;
    }

    /**
     * @param topCcmtsEthVlanId
     *            the topCcmtsEthVlanId to set
     */
    public void setTopCcmtsEthVlanId(Long topCcmtsEthVlanId) {
        this.topCcmtsEthVlanId = topCcmtsEthVlanId;
    }

    /**
     * @return the topCcmtsEthVlanIpAddr
     */
    public String getTopCcmtsEthVlanIpAddr() {
        return topCcmtsEthVlanIpAddr;
    }

    /**
     * @param topCcmtsEthVlanIpAddr
     *            the topCcmtsEthVlanIpAddr to set
     */
    public void setTopCcmtsEthVlanIpAddr(String topCcmtsEthVlanIpAddr) {
        this.topCcmtsEthVlanIpAddr = topCcmtsEthVlanIpAddr;
    }

    /**
     * @return the topCcmtsEthVlanIpMask
     */
    public String getTopCcmtsEthVlanIpMask() {
        return topCcmtsEthVlanIpMask;
    }

    /**
     * @param topCcmtsEthVlanIpMask
     *            the topCcmtsEthVlanIpMask to set
     */
    public void setTopCcmtsEthVlanIpMask(String topCcmtsEthVlanIpMask) {
        this.topCcmtsEthVlanIpMask = topCcmtsEthVlanIpMask;
    }

    /**
     * @return the topCcmtsEthVlanGateway
     */
    public String getTopCcmtsEthVlanGateway() {
        return topCcmtsEthVlanGateway;
    }

    /**
     * @param topCcmtsEthVlanGateway
     *            the topCcmtsEthVlanGateway to set
     */
    public void setTopCcmtsEthVlanGateway(String topCcmtsEthVlanGateway) {
        this.topCcmtsEthVlanGateway = topCcmtsEthVlanGateway;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcSystemIpInfo [entityId=");
        builder.append(entityId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", topCcmtsEthIpAddr=");
        builder.append(topCcmtsEthIpAddr);
        builder.append(", topCcmtsEthIpMask=");
        builder.append(topCcmtsEthIpMask);
        builder.append(", topCcmtsEthGateway=");
        builder.append(topCcmtsEthGateway);
        builder.append(", topCcmtsEthVlanId=");
        builder.append(topCcmtsEthVlanId);
        builder.append(", topCcmtsEthVlanIpAddr=");
        builder.append(topCcmtsEthVlanIpAddr);
        builder.append(", topCcmtsEthVlanIpMask=");
        builder.append(topCcmtsEthVlanIpMask);
        builder.append(", topCcmtsEthVlanGateway=");
        builder.append(topCcmtsEthVlanGateway);
        builder.append("]");
        return builder.toString();
    }

}
