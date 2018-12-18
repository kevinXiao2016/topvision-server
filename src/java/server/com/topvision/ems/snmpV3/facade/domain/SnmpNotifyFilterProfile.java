/***********************************************************************
 * $Id: SnmpNotifyFilterProfile.java,v1.0 2013-01-09 上午9:42:27 $
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
 * SNMP V3 NotifyFilterProfile
 * 
 * @author yq
 * 
 */
@Alias("snmpNotifyFilterProfile")
public class SnmpNotifyFilterProfile implements AliasesSuperType {
    private static final long serialVersionUID = 2379937183209353886L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.3.1.1", index = true)
    private String targetParamsName;
    @SnmpProperty(oid = "1.3.6.1.6.3.13.1.2.1.1", writable = true, type = "OctetString")
    private String notifyFilterProfileName;
    @SnmpProperty(oid = "1.3.6.1.6.3.13.1.2.1.2", writable = true, type = "Integer32")
    private Integer notifyFilterProfileStorType;
    @SnmpProperty(oid = "1.3.6.1.6.3.13.1.2.1.3", writable = true, type = "Integer32")
    private Integer notifyFilterProfileRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getNotifyFilterProfileName() {
        return notifyFilterProfileName;
    }

    public void setNotifyFilterProfileName(String notifyFilterProfileName) {
        this.notifyFilterProfileName = notifyFilterProfileName;
    }

    public Integer getNotifyFilterProfileStorType() {
        return notifyFilterProfileStorType;
    }

    public void setNotifyFilterProfileStorType(Integer notifyFilterProfileStorType) {
        this.notifyFilterProfileStorType = notifyFilterProfileStorType;
    }

    public Integer getNotifyFilterProfileRowStatus() {
        return notifyFilterProfileRowStatus;
    }

    public void setNotifyFilterProfileRowStatus(Integer notifyFilterProfileRowStatus) {
        this.notifyFilterProfileRowStatus = notifyFilterProfileRowStatus;
    }

    public String getTargetParamsName() {
        return targetParamsName;
    }

    public void setTargetParamsName(String targetParamsName) {
        this.targetParamsName = targetParamsName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SnmpNotifyFilterProfile [entityId=").append(entityId).append(", targetParamsName=")
                .append(targetParamsName).append(", notifyFilterProfileName=").append(notifyFilterProfileName)
                .append(", notifyFilterProfileStorType=").append(notifyFilterProfileStorType)
                .append(", notifyFilterProfileRowStatus=").append(notifyFilterProfileRowStatus).append("]");
        return sb.toString();
    }

}
