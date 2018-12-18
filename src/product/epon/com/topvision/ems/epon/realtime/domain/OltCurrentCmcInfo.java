/***********************************************************************
 * $Id: OltCurrentCmcInfo.java,v1.0 2014-7-21 上午10:00:44 $
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
 * @created @2014-7-21-上午10:00:44
 *
 */
public class OltCurrentCmcInfo implements AliasesSuperType {
    private static final long serialVersionUID = 3715014432878245776L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.2", type = "OctetString")
    private String sysObjectId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.5", type = "OctetString")
    private String sysName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.14", type = "Integer32")
    private Integer cmcStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.12", type = "OctetString")
    private String macAddress;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public String getSysObjectId() {
        return sysObjectId;
    }

    public void setSysObjectId(String sysObjectId) {
        this.sysObjectId = sysObjectId;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Integer getCmcStatus() {
        return cmcStatus;
    }

    public void setCmcStatus(Integer cmcStatus) {
        this.cmcStatus = cmcStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltCurrentCmcInfo [entityId=");
        builder.append(entityId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", sysObjectId=");
        builder.append(sysObjectId);
        builder.append(", sysName=");
        builder.append(sysName);
        builder.append(", macAddress=");
        builder.append(macAddress);
        builder.append(", cmcStatus=");
        builder.append(cmcStatus);
        builder.append("]");
        return builder.toString();
    }

}
