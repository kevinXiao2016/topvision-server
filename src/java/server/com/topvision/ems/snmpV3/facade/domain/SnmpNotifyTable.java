/***********************************************************************
 * $Id: SnmpNotifyTable.java,v1.0 2013-01-09 上午9:42:57 $
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
 * SNMP V3 Notify
 * 
 * @author yq
 * 
 */
@Alias("snmpNotifyTable")
public class SnmpNotifyTable implements AliasesSuperType {
    private static final long serialVersionUID = -2369491206218294396L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.6.3.13.1.1.1.1", index = true)
    private String notifyName;
    @SnmpProperty(oid = "1.3.6.1.6.3.13.1.1.1.2", writable = true, type = "OctetString")
    private String notifyTag;
    @SnmpProperty(oid = "1.3.6.1.6.3.13.1.1.1.3", writable = true, type = "Integer32")
    private Integer notifyType;
    @SnmpProperty(oid = "1.3.6.1.6.3.13.1.1.1.4", writable = true, type = "Integer32")
    private Integer notifyStorageType;
    @SnmpProperty(oid = "1.3.6.1.6.3.13.1.1.1.5", writable = true, type = "Integer32")
    private Integer notifyRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getNotifyName() {
        return notifyName;
    }

    public void setNotifyName(String notifyName) {
        this.notifyName = notifyName;
    }

    public String getNotifyTag() {
        return notifyTag;
    }

    public void setNotifyTag(String notifyTag) {
        this.notifyTag = notifyTag;
    }

    public Integer getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(Integer notifyType) {
        this.notifyType = notifyType;
    }

    public Integer getNotifyStorageType() {
        return notifyStorageType;
    }

    public void setNotifyStorageType(Integer notifyStorageType) {
        this.notifyStorageType = notifyStorageType;
    }

    public Integer getNotifyRowStatus() {
        return notifyRowStatus;
    }

    public void setNotifyRowStatus(Integer notifyRowStatus) {
        this.notifyRowStatus = notifyRowStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SnmpNotifyTable [entityId=").append(entityId).append(", notifyName=").append(notifyName)
                .append(", notifyTag=").append(notifyTag).append(", notifyType=").append(notifyType)
                .append(", notifyStorageType=").append(notifyStorageType).append(", notifyRowStatus=")
                .append(notifyRowStatus).append("]");
        return sb.toString();
    }

}
