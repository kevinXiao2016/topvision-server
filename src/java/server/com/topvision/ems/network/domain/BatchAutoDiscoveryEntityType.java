/***********************************************************************
 * $Id: BatchAutoDiscoveryEntityType.java,v1.0 2014-5-11 下午2:14:51 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2014-5-11-下午2:14:51
 *
 */
public class BatchAutoDiscoveryEntityType implements AliasesSuperType {
    private static final long serialVersionUID = -6715676308588480396L;
    private Long typeId;
    private String typeName;
    private String sysObjectId;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSysObjectId() {
        return sysObjectId;
    }

    public void setSysObjectId(String sysObjectId) {
        this.sysObjectId = sysObjectId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BatchAutoDiscoveryEntityType [typeId=");
        builder.append(typeId);
        builder.append(", typeName=");
        builder.append(typeName);
        builder.append(", sysObjectId=");
        builder.append(sysObjectId);
        builder.append("]");
        return builder.toString();
    }

}
