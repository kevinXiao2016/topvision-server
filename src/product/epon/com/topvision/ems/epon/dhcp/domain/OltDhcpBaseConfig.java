/***********************************************************************
 * $Id: OltDhcpBaseConfig.java,v1.0 2011-11-16 上午09:48:42 $
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
 * @created @2011-11-16-上午09:48:42
 * 
 */
public class OltDhcpBaseConfig implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 6547014637323837471L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.1.1.2.1", writable = true, type = "Integer32")
    private Integer topOltPPPOEPlusEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.1.1.3.1", writable = true, type = "Integer32")
    private Integer topOltDHCPRelayMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.1.1.4.1", writable = true, type = "Integer32")
    private Integer topOltDHCPDyncIPMACBind;

    /**
     * @return the topOltPPPOEPlusEnable
     */
    public Integer getTopOltPPPOEPlusEnable() {
        return topOltPPPOEPlusEnable;
    }

    /**
     * @param topOltPPPOEPlusEnable
     *            the topOltPPPOEPlusEnable to set
     */
    public void setTopOltPPPOEPlusEnable(Integer topOltPPPOEPlusEnable) {
        this.topOltPPPOEPlusEnable = topOltPPPOEPlusEnable;
    }

    /**
     * @return the topOltDHCPRelayMode
     */
    public Integer getTopOltDHCPRelayMode() {
        return topOltDHCPRelayMode;
    }

    /**
     * @param topOltDHCPRelayMode
     *            the topOltDHCPRelayMode to set
     */
    public void setTopOltDHCPRelayMode(Integer topOltDHCPRelayMode) {
        this.topOltDHCPRelayMode = topOltDHCPRelayMode;
    }

    /**
     * @return the topOltDHCPDyncIPMACBind
     */
    public Integer getTopOltDHCPDyncIPMACBind() {
        return topOltDHCPDyncIPMACBind;
    }

    /**
     * @param topOltDHCPDyncIPMACBind
     *            the topOltDHCPDyncIPMACBind to set
     */
    public void setTopOltDHCPDyncIPMACBind(Integer topOltDHCPDyncIPMACBind) {
        this.topOltDHCPDyncIPMACBind = topOltDHCPDyncIPMACBind;
    }

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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltDhcpBaseConfig [entityId=");
        builder.append(entityId);
        builder.append(", topOltPPPOEPlusEnable=");
        builder.append(topOltPPPOEPlusEnable);
        builder.append(", topOltDHCPRelayMode=");
        builder.append(topOltDHCPRelayMode);
        builder.append(", topOltDHCPDyncIPMACBind=");
        builder.append(topOltDHCPDyncIPMACBind);
        builder.append("]");
        return builder.toString();
    }

}
