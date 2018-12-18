/***********************************************************************
 * $Id: CmtsBaseInfo.java,v1.0 2014-10-10 下午4:06:45 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-10-10-下午4:06:45
 *
 */
public class CmtsBaseInfo implements AliasesSuperType {
    private static final long serialVersionUID = 5692659531403494101L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.3.0", type = "TimeTicks")
    private Long sysUpTime;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.5.0", type = "OctetString")
    private String sysName;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.6.0", type = "OctetString")
    private String sysLocation;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(Long sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysLocation() {
        return sysLocation;
    }

    public void setSysLocation(String sysLocation) {
        this.sysLocation = sysLocation;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmtsBaseInfo [entityId=");
        builder.append(entityId);
        builder.append(", sysUpTime=");
        builder.append(sysUpTime);
        builder.append(", sysName=");
        builder.append(sysName);
        builder.append(", sysLocation=");
        builder.append(sysLocation);
        builder.append("]");
        return builder.toString();
    }

}
