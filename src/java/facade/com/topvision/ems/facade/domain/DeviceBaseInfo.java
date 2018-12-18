/***********************************************************************
 * $Id: DeviceBaseInfo.java,v1.0 2011-10-27 下午04:45:51 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Victor
 * @created @2011-10-27-下午04:45:51
 * 
 */
public class DeviceBaseInfo implements Serializable {
    private static final long serialVersionUID = 5400227430207780657L;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.1.0")
    private String sysDescr;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.2.0")
    private String sysObjectID;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.3.0,V1.10.0.5:1.3.6.1.4.1.32285.11.2.3.1.2.9.0")
    private String sysUpTime;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.4.0", writable = true, type = "OctetString")
    private String sysContact;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.5.0", writable = true, type = "OctetString")
    private String sysName;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.6.0", writable = true, type = "OctetString")
    private String sysLocation;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.7.0")
    private String sysServices;

    /**
     * @return the sysDescr
     */
    public String getSysDescr() {
        return sysDescr;
    }

    /**
     * @param sysDescr
     *            the sysDescr to set
     */
    public void setSysDescr(String sysDescr) {
        this.sysDescr = sysDescr;
    }

    /**
     * @return the sysObjectID
     */
    public String getSysObjectID() {
        return sysObjectID;
    }

    /**
     * @param sysObjectID
     *            the sysObjectID to set
     */
    public void setSysObjectID(String sysObjectID) {
        this.sysObjectID = sysObjectID;
    }

    /**
     * @return the sysUpTime
     */
    public String getSysUpTime() {
        return sysUpTime;
    }

    /**
     * @param sysUpTime
     *            the sysUpTime to set
     */
    public void setSysUpTime(String sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    /**
     * @return the sysContact
     */
    public String getSysContact() {
        return sysContact;
    }

    /**
     * @param sysContact
     *            the sysContact to set
     */
    public void setSysContact(String sysContact) {
        this.sysContact = sysContact;
    }

    /**
     * @return the sysName
     */
    public String getSysName() {
        return sysName;
    }

    /**
     * @param sysName
     *            the sysName to set
     */
    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    /**
     * @return the sysLocation
     */
    public String getSysLocation() {
        return sysLocation;
    }

    /**
     * @param sysLocation
     *            the sysLocation to set
     */
    public void setSysLocation(String sysLocation) {
        this.sysLocation = sysLocation;
    }

    /**
     * @return the sysServices
     */
    public String getSysServices() {
        return sysServices;
    }

    /**
     * @param sysServices
     *            the sysServices to set
     */
    public void setSysServices(String sysServices) {
        this.sysServices = sysServices;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DeviceBaseInfo [sysDescr=");
        builder.append(sysDescr);
        builder.append(", sysObjectID=");
        builder.append(sysObjectID);
        builder.append(", sysUpTime=");
        builder.append(sysUpTime);
        builder.append(", sysContact=");
        builder.append(sysContact);
        builder.append(", sysName=");
        builder.append(sysName);
        builder.append(", sysLocation=");
        builder.append(sysLocation);
        builder.append(", sysServices=");
        builder.append(sysServices);
        builder.append("]");
        return builder.toString();
    }
}
