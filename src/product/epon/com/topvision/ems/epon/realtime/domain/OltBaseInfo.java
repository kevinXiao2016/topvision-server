/***********************************************************************
 * $Id: OltBaseInfo.java,v1.0 2014-7-12 上午11:57:10 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-7-12-上午11:57:10
 *
 */
public class OltBaseInfo implements AliasesSuperType {
    private static final long serialVersionUID = 981599830110410339L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.3.0,V1.10.0.5:1.3.6.1.4.1.32285.11.2.3.1.2.9.0", type = "TimeTicks")
    private Long sysUpTime;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.5.0", type = "OctetString")
    private String deviceName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.2.7.0", type = "OctetString")
    private String softwareVersion;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Long getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(Long sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        // 兼容设备版本取不到的情况
        if (softwareVersion == null || "".equals(softwareVersion) || "noSuchObject".equals(softwareVersion)) {
            this.softwareVersion = "--";
        } else {
            this.softwareVersion = softwareVersion;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltBaseInfo [entityId=");
        builder.append(entityId);
        builder.append(", deviceName=");
        builder.append(deviceName);
        builder.append(", sysUpTime=");
        builder.append(sysUpTime);
        builder.append(", softwareVersion=");
        builder.append(softwareVersion);
        builder.append("]");
        return builder.toString();
    }

}
