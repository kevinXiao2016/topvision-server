/***********************************************************************
 * $Id: TopOltDhcpGlobalObjects.java,v1.0 2017年11月16日 下午4:05:00 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2017年11月16日-下午4:05:00
 *
 */
public class TopOltDhcpGlobalObjects implements AliasesSuperType {
    private static final long serialVersionUID = 27090727765285837L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.1.1.0", writable = true, type = "Integer32")
    private Integer topOltDhcpEnable;// enable(1) disable(2)

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.1.2.0", writable = true, type = "Integer32")
    private Integer topOltDhcpOpt82Enable;// enable(1) disable(2)
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.1.3.0", writable = true, type = "Integer32")
    private Integer topOltDhcpOpt82Policy;// insert(1) replace(2)
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.1.4.0", writable = true, type = "OctetString")
    private String topOltDhcpOpt82Format;// 1-127

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.1.5.0", writable = true, type = "Integer32")
    private Integer topOltDhcpSourceVerifyEnable;// enable(1) disable(2)

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.1.6.0", writable = true, type = "Integer32")
    private Integer topOltPPPoEPlusEnable;// enable(1) disable(2)
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.1.7.0", writable = true, type = "Integer32")
    private Integer topOltPPPoEPlusPolicy;// insert(1) replace(2)
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.1.8.0", writable = true, type = "OctetString")
    private String topOltPPPoEPlusFormat;// 1-127

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopOltDhcpEnable() {
        return topOltDhcpEnable;
    }

    public void setTopOltDhcpEnable(Integer topOltDhcpEnable) {
        this.topOltDhcpEnable = topOltDhcpEnable;
    }

    public Integer getTopOltDhcpOpt82Enable() {
        return topOltDhcpOpt82Enable;
    }

    public void setTopOltDhcpOpt82Enable(Integer topOltDhcpOpt82Enable) {
        this.topOltDhcpOpt82Enable = topOltDhcpOpt82Enable;
    }

    public Integer getTopOltDhcpOpt82Policy() {
        return topOltDhcpOpt82Policy;
    }

    public void setTopOltDhcpOpt82Policy(Integer topOltDhcpOpt82Policy) {
        this.topOltDhcpOpt82Policy = topOltDhcpOpt82Policy;
    }

    public String getTopOltDhcpOpt82Format() {
        return topOltDhcpOpt82Format;
    }

    public void setTopOltDhcpOpt82Format(String topOltDhcpOpt82Format) {
        this.topOltDhcpOpt82Format = topOltDhcpOpt82Format;
    }

    public Integer getTopOltDhcpSourceVerifyEnable() {
        return topOltDhcpSourceVerifyEnable;
    }

    public void setTopOltDhcpSourceVerifyEnable(Integer topOltDhcpSourceVerifyEnable) {
        this.topOltDhcpSourceVerifyEnable = topOltDhcpSourceVerifyEnable;
    }

    public Integer getTopOltPPPoEPlusEnable() {
        return topOltPPPoEPlusEnable;
    }

    public void setTopOltPPPoEPlusEnable(Integer topOltPPPoEPlusEnable) {
        this.topOltPPPoEPlusEnable = topOltPPPoEPlusEnable;
    }

    public Integer getTopOltPPPoEPlusPolicy() {
        return topOltPPPoEPlusPolicy;
    }

    public void setTopOltPPPoEPlusPolicy(Integer topOltPPPoEPlusPolicy) {
        this.topOltPPPoEPlusPolicy = topOltPPPoEPlusPolicy;
    }

    public String getTopOltPPPoEPlusFormat() {
        return topOltPPPoEPlusFormat;
    }

    public void setTopOltPPPoEPlusFormat(String topOltPPPoEPlusFormat) {
        this.topOltPPPoEPlusFormat = topOltPPPoEPlusFormat;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopOltDhcpGlobalObjects [entityId=");
        builder.append(entityId);
        builder.append(", topOltDhcpEnable=");
        builder.append(topOltDhcpEnable);
        builder.append(", topOltDhcpOpt82Enable=");
        builder.append(topOltDhcpOpt82Enable);
        builder.append(", topOltDhcpOpt82Policy=");
        builder.append(topOltDhcpOpt82Policy);
        builder.append(", topOltDhcpOpt82Format=");
        builder.append(topOltDhcpOpt82Format);
        builder.append(", topOltDhcpSourceVerifyEnable=");
        builder.append(topOltDhcpSourceVerifyEnable);
        builder.append(", topOltPPPoEPlusEnable=");
        builder.append(topOltPPPoEPlusEnable);
        builder.append(", topOltPPPoEPlusPolicy=");
        builder.append(topOltPPPoEPlusPolicy);
        builder.append(", topOltPPPoEPlusFormat=");
        builder.append(topOltPPPoEPlusFormat);
        builder.append("]");
        return builder.toString();
    }

}
