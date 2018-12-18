/***********************************************************************
 * $Id: PortIsolationGroup.java,v1.0 2014-12-18 上午11:43:57 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.portisolation.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-12-18-上午11:43:57
 *
 */
public class PortIsolationGroup implements AliasesSuperType {
    private static final long serialVersionUID = 4616680599440166065L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.16.1.1.1", index = true)
    private Integer groupIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.16.1.1.2", writable = true, type = "OctetString")
    private String groupDesc;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.16.1.1.4", writable = true, type = "Integer32")
    private Integer groupRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public Integer getGroupRowStatus() {
        return groupRowStatus;
    }

    public void setGroupRowStatus(Integer groupRowStatus) {
        this.groupRowStatus = groupRowStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortIsolationGroup [entityId=");
        builder.append(entityId);
        builder.append(", groupIndex=");
        builder.append(groupIndex);
        builder.append(", groupDesc=");
        builder.append(groupDesc);
        builder.append(", groupRowStatus=");
        builder.append(groupRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
