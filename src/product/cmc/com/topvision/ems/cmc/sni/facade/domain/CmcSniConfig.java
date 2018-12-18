/***********************************************************************
 * $Id: CmcSniConfig.java,v1.0 2013-4-23 下午3:34:31 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sni.facade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2013-4-23-下午3:34:31
 *
 */
@Alias("cmcSniConfig")
public class CmcSniConfig implements AliasesSuperType{
    private static final long serialVersionUID = -5648342508166449181L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.5.4.0", writable = true, type = "Integer32")
    private Integer topCcmtsSniUplinkLoopbackStatus;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getTopCcmtsSniUplinkLoopbackStatus() {
        return topCcmtsSniUplinkLoopbackStatus;
    }

    public void setTopCcmtsSniUplinkLoopbackStatus(Integer topCcmtsSniUplinkLoopbackStatus) {
        this.topCcmtsSniUplinkLoopbackStatus = topCcmtsSniUplinkLoopbackStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcSniConfig [cmcId=");
        builder.append(cmcId);
        builder.append(", topCcmtsSniUplinkLoopbackStatus=");
        builder.append(topCcmtsSniUplinkLoopbackStatus);
        builder.append("]");
        return builder.toString();
    }

}
