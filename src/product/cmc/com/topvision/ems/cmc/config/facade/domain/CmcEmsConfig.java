/***********************************************************************
 * $Id: CmcEmsConfig.java,v1.0 2012-2-15 下午01:55:10 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.config.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2012-2-15-下午01:55:10
 * 
 */
@Alias("cmcEmsConfig")
public class CmcEmsConfig implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7383568422520409602L;
    private Long entityId;
    private Long cmcId;
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.4.1.1.1", index = true)
    private Integer docsDevNmAccessIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.4.1.1.2", writable = true, type = "IpAddress")
    private String topCcmtsNmAccessIp;
    private Long topCcmtsNmAccessIpLong;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.4.1.1.3")
    private String topCcmtsNmAccessIpMask;
    private Long topCcmtsNmAccessIpMaskLong;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.4.1.1.4")
    private String topCcmtsNmAccessReadCommunity;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.4.1.1.5")
    private String topCcmtsNmAccessWriteCommunity;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.4.1.1.6")
    private Integer topCcmtsNmAccessControl;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.4.1.1.7")
    private Integer topCcmtsNmAccessStatus;

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
     * @return the topCcmtsNmAccessIp
     */
    public String getTopCcmtsNmAccessIp() {
        return topCcmtsNmAccessIp;
    }

    /**
     * @param topCcmtsNmAccessIp
     *            the topCcmtsNmAccessIp to set
     */
    public void setTopCcmtsNmAccessIp(String topCcmtsNmAccessIp) {
        this.topCcmtsNmAccessIp = topCcmtsNmAccessIp;
    }

    /**
     * @return the topCcmtsNmAccessIpLong
     */
    public Long getTopCcmtsNmAccessIpLong() {
        return topCcmtsNmAccessIpLong;
    }

    /**
     * @param topCcmtsNmAccessIpLong
     *            the topCcmtsNmAccessIpLong to set
     */
    public void setTopCcmtsNmAccessIpLong(Long topCcmtsNmAccessIpLong) {
        this.topCcmtsNmAccessIpLong = topCcmtsNmAccessIpLong;
    }

    /**
     * @return the topCcmtsNmAccessIpMask
     */
    public String getTopCcmtsNmAccessIpMask() {
        return topCcmtsNmAccessIpMask;
    }

    /**
     * @param topCcmtsNmAccessIpMask
     *            the topCcmtsNmAccessIpMask to set
     */
    public void setTopCcmtsNmAccessIpMask(String topCcmtsNmAccessIpMask) {
        this.topCcmtsNmAccessIpMask = topCcmtsNmAccessIpMask;
    }

    /**
     * @return the topCcmtsNmAccessIpMaskLong
     */
    public Long getTopCcmtsNmAccessIpMaskLong() {
        return topCcmtsNmAccessIpMaskLong;
    }

    /**
     * @param topCcmtsNmAccessIpMaskLong
     *            the topCcmtsNmAccessIpMaskLong to set
     */
    public void setTopCcmtsNmAccessIpMaskLong(Long topCcmtsNmAccessIpMaskLong) {
        this.topCcmtsNmAccessIpMaskLong = topCcmtsNmAccessIpMaskLong;
    }

    /**
     * @return the topCcmtsNmAccessReadCommunity
     */
    public String getTopCcmtsNmAccessReadCommunity() {
        return topCcmtsNmAccessReadCommunity;
    }

    /**
     * @param topCcmtsNmAccessReadCommunity
     *            the topCcmtsNmAccessReadCommunity to set
     */
    public void setTopCcmtsNmAccessReadCommunity(String topCcmtsNmAccessReadCommunity) {
        this.topCcmtsNmAccessReadCommunity = topCcmtsNmAccessReadCommunity;
    }

    /**
     * @return the topCcmtsNmAccessWriteCommunity
     */
    public String getTopCcmtsNmAccessWriteCommunity() {
        return topCcmtsNmAccessWriteCommunity;
    }

    /**
     * @param topCcmtsNmAccessWriteCommunity
     *            the topCcmtsNmAccessWriteCommunity to set
     */
    public void setTopCcmtsNmAccessWriteCommunity(String topCcmtsNmAccessWriteCommunity) {
        this.topCcmtsNmAccessWriteCommunity = topCcmtsNmAccessWriteCommunity;
    }

    /**
     * @return the topCcmtsNmAccessControl
     */
    public Integer getTopCcmtsNmAccessControl() {
        return topCcmtsNmAccessControl;
    }

    /**
     * @param topCcmtsNmAccessControl
     *            the topCcmtsNmAccessControl to set
     */
    public void setTopCcmtsNmAccessControl(Integer topCcmtsNmAccessControl) {
        this.topCcmtsNmAccessControl = topCcmtsNmAccessControl;
    }

    /**
     * @return the docsDevNmAccessIndex
     */
    public Integer getDocsDevNmAccessIndex() {
        return docsDevNmAccessIndex;
    }

    /**
     * @param docsDevNmAccessIndex
     *            the docsDevNmAccessIndex to set
     */
    public void setDocsDevNmAccessIndex(Integer docsDevNmAccessIndex) {
        this.docsDevNmAccessIndex = docsDevNmAccessIndex;
    }

    public Integer getTopCcmtsNmAccessStatus() {
        return topCcmtsNmAccessStatus;
    }

    public void setTopCcmtsNmAccessStatus(Integer topCcmtsNmAccessStatus) {
        this.topCcmtsNmAccessStatus = topCcmtsNmAccessStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcEmsConfig [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", docsDevNmAccessIndex=");
        builder.append(docsDevNmAccessIndex);
        builder.append(", topCcmtsNmAccessIp=");
        builder.append(topCcmtsNmAccessIp);
        builder.append(", topCcmtsNmAccessIpLong=");
        builder.append(topCcmtsNmAccessIpLong);
        builder.append(", topCcmtsNmAccessIpMask=");
        builder.append(topCcmtsNmAccessIpMask);
        builder.append(", topCcmtsNmAccessIpMaskLong=");
        builder.append(topCcmtsNmAccessIpMaskLong);
        builder.append(", topCcmtsNmAccessReadCommunity=");
        builder.append(topCcmtsNmAccessReadCommunity);
        builder.append(", topCcmtsNmAccessWriteCommunity=");
        builder.append(topCcmtsNmAccessWriteCommunity);
        builder.append(", topCcmtsNmAccessControl=");
        builder.append(topCcmtsNmAccessControl);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
