/***********************************************************************
 * $Id: CmStaticIp.java,v1.0 2013-4-26 上午10:53:37 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2013-4-26-上午10:53:37
 *
 */
@Alias("cmStaticIp")
public class CmStaticIp implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -7427662520145385951L;
    private Long cmId;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.1", index = true)
    private Long docsIfCmtsCmStatusIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.6.1.1", index = true)
    private Long topCcmtsCmStaticIPIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.6.1.2")
    private String topCcmtsCmStaticIP;

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getDocsIfCmtsCmStatusIndex() {
        return docsIfCmtsCmStatusIndex;
    }

    public void setDocsIfCmtsCmStatusIndex(Long docsIfCmtsCmStatusIndex) {
        this.docsIfCmtsCmStatusIndex = docsIfCmtsCmStatusIndex;
    }

    public String getTopCcmtsCmStaticIP() {
        return topCcmtsCmStaticIP;
    }

    public void setTopCcmtsCmStaticIP(String topCcmtsCmStaticIP) {
        this.topCcmtsCmStaticIP = topCcmtsCmStaticIP;
    }

    public Long getTopCcmtsCmStaticIPIndex() {
        return topCcmtsCmStaticIPIndex;
    }

    public void setTopCcmtsCmStaticIPIndex(Long topCcmtsCmStaticIPIndex) {
        this.topCcmtsCmStaticIPIndex = topCcmtsCmStaticIPIndex;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmStaticIp [cmId=");
        builder.append(cmId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", docsIfCmtsCmStatusIndex=");
        builder.append(docsIfCmtsCmStatusIndex);
        builder.append(", topCcmtsCmStaticIPIndex=");
        builder.append(topCcmtsCmStaticIPIndex);
        builder.append(", topCcmtsCmStaticIP=");
        builder.append(topCcmtsCmStaticIP);
        builder.append("]");
        return builder.toString();
    }

}
