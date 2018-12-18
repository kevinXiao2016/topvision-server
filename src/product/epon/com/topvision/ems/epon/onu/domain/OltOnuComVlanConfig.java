/***********************************************************************
 * $Id: OltOnuComVlanConfig.java,v1.0 2012-12-18 下午14:47:59 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lzt
 * @created @2012-12-18-下午14:47:59
 * 
 */
public class OltOnuComVlanConfig implements AliasesSuperType {
    private static final long serialVersionUID = 3470047672755651977L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.1.1.0", writable = true, type = "Integer32")
    private Integer onuComVlan;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getOnuComVlan() {
        return onuComVlan;
    }

    public void setOnuComVlan(Integer onuComVlan) {
        this.onuComVlan = onuComVlan;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltOnuComVlanConfig [entityId=");
        builder.append(entityId);
        builder.append(", onuComVlan=");
        builder.append(onuComVlan);
        builder.append("]");
        return builder.toString();
    }

}
