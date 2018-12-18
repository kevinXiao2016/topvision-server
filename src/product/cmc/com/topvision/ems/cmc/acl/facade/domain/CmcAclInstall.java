/***********************************************************************
 * $Id: CmcAclInstall.java,v1.0 2018年1月29日 上午10:00:25 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.acl.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2018年1月29日-上午10:00:25
 *
 */
public class CmcAclInstall implements AliasesSuperType {
    private static final long serialVersionUID = -2218665312696913617L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.1", index = true)
    private Integer topCcmtsAclListIndex;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.23", type = "OctetString", writable = true)
    private String topInstallPosition;

    public Integer getTopCcmtsAclListIndex() {
        return topCcmtsAclListIndex;
    }

    public void setTopCcmtsAclListIndex(Integer topCcmtsAclListIndex) {
        this.topCcmtsAclListIndex = topCcmtsAclListIndex;
    }

    public String getTopInstallPosition() {
        return topInstallPosition;
    }

    public void setTopInstallPosition(String topInstallPosition) {
        this.topInstallPosition = topInstallPosition;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcAclInstall [topCcmtsAclListIndex=");
        builder.append(topCcmtsAclListIndex);
        builder.append(", topInstallPosition=");
        builder.append(topInstallPosition);
        builder.append("]");
        return builder.toString();
    }

}
