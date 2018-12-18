/***********************************************************************
 * $Id: SnmpNotifyFilterTable.java,v1.0 2013-01-09 上午9:40:27 $
 * 
 * @author: yq
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.facade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * SNMP V3 NotifyFilterTable
 * 
 * @author yq
 * 
 */
@Alias("snmpNotifyFilterTable")
public class SnmpNotifyFilterTable implements AliasesSuperType {
    private static final long serialVersionUID = -3005637867000165422L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.6.3.13.1.2.1.1", index = true)
    private String notifyFilterProfileName;
    @SnmpProperty(oid = "1.3.6.1.6.3.13.1.3.1.1", index = true, type = "OBJECT IDENTIFIER")
    private String notifyFilterSubtree;
    @SnmpProperty(oid = "1.3.6.1.6.3.13.1.3.1.2", writable = true, type = "OctetString")
    private String notifyFilterMask;
    @SnmpProperty(oid = "1.3.6.1.6.3.13.1.3.1.3", writable = true, type = "Integer32")
    private Integer notifyFilterType;
    @SnmpProperty(oid = "1.3.6.1.6.3.13.1.3.1.4", writable = true, type = "Integer32")
    private Integer notifyFilterStorType;
    @SnmpProperty(oid = "1.3.6.1.6.3.13.1.3.1.5", writable = true, type = "Integer32")
    private Integer notifyFilterRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getNotifyFilterSubtree() {
        return notifyFilterSubtree;
    }

    public void setNotifyFilterSubtree(String notifyFilterSubtree) {
        this.notifyFilterSubtree = notifyFilterSubtree;
    }

    public String getNotifyFilterMask() {
        return notifyFilterMask;
    }

    public void setNotifyFilterMask(String notifyFilterMask) {
        this.notifyFilterMask = notifyFilterMask;
    }

    public Integer getNotifyFilterType() {
        return notifyFilterType;
    }

    public void setNotifyFilterType(Integer notifyFilterType) {
        this.notifyFilterType = notifyFilterType;
    }

    public Integer getNotifyFilterStorType() {
        return notifyFilterStorType;
    }

    public void setNotifyFilterStorType(Integer notifyFilterStorType) {
        this.notifyFilterStorType = notifyFilterStorType;
    }

    public Integer getNotifyFilterRowStatus() {
        return notifyFilterRowStatus;
    }

    public void setNotifyFilterRowStatus(Integer notifyFilterRowStatus) {
        this.notifyFilterRowStatus = notifyFilterRowStatus;
    }

    public String getNotifyFilterProfileName() {
        return notifyFilterProfileName;
    }

    public void setNotifyFilterProfileName(String notifyFilterProfileName) {
        this.notifyFilterProfileName = notifyFilterProfileName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SnmpNotifyFilterTable [entityId=").append(entityId).append(", notifyFilterProfileName=")
                .append(notifyFilterProfileName).append(", notifyFilterSubtree=").append(notifyFilterSubtree)
                .append(", notifyFilterMask=").append(notifyFilterMask).append(", notifyFilterType=")
                .append(notifyFilterType).append(", notifyFilterStorType=").append(notifyFilterStorType)
                .append(", notifyFilterRowStatus=").append(notifyFilterRowStatus).append("]");
        return sb.toString();
    }

}
