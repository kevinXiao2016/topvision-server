/***********************************************************************
 * $ SystemAttribute.java,v1.0 2013-8-17 14:58:30 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.domain;

import java.sql.Timestamp;
import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author jay
 * @created @2013-8-17-14:58:30
 */
public class SystemAttribute implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.1.0")
    private String sysDescr;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.3.0")
    private String sysUpTime;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.4.0")
    private String sysContact;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.5.0")
    private String sysName;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.6.0")
    private String sysLocation;
    private Timestamp dt;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Timestamp getDt() {
        return dt;
    }

    public void setDt(Timestamp dt) {
        this.dt = dt;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getSysContact() {
        return sysContact;
    }

    public void setSysContact(String sysContact) {
        this.sysContact = sysContact;
    }

    public String getSysDescr() {
        return sysDescr;
    }

    public void setSysDescr(String sysDescr) {
        this.sysDescr = sysDescr;
    }

    public String getSysLocation() {
        return sysLocation;
    }

    public void setSysLocation(String sysLocation) {
        this.sysLocation = sysLocation;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(String sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SystemAttribute");
        sb.append("{cmcId=").append(cmcId);
        sb.append(", entityId=").append(entityId);
        sb.append(", sysDescr='").append(sysDescr).append('\'');
        sb.append(", sysUpTime='").append(sysUpTime).append('\'');
        sb.append(", sysContact='").append(sysContact).append('\'');
        sb.append(", sysName='").append(sysName).append('\'');
        sb.append(", sysLocation='").append(sysLocation).append('\'');
        sb.append(", dt=").append(dt);
        sb.append('}');
        return sb.toString();
    }
}
