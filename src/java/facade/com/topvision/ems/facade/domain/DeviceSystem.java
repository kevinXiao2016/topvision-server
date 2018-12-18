/***********************************************************************
 * $Id: DeviceSystem.java,v 1.1 Mar 16, 2009 6:52:02 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @Create Date Mar 16, 2009 6:52:02 PM
 * 
 * @author kelers
 * 
 */
public class DeviceSystem extends AbstractProperty {
    private static final long serialVersionUID = -3502924142315337452L;
    @SnmpProperty(oid = ".1.3.6.1.2.1.1.1.0")
    private String sysDescr;
    @SnmpProperty(oid = ".1.3.6.1.2.1.1.2.0")
    private String sysObjectID;
    @SnmpProperty(oid = ".1.3.6.1.2.1.1.3.0,V1.10.0.5:1.3.6.1.4.1.32285.11.2.3.1.2.9.0")
    private String sysUpTime;
    @SnmpProperty(oid = ".1.3.6.1.2.1.1.4.0", writable = true)
    private String sysContact;
    @SnmpProperty(oid = ".1.3.6.1.2.1.1.5.0", writable = true)
    private String sysName;
    @SnmpProperty(oid = ".1.3.6.1.2.1.1.6.0", writable = true)
    private String sysLocation;
    @SnmpProperty(oid = ".1.3.6.1.2.1.1.7.0", type = "Integer32")
    private Integer sysServices;

    /**
     * @return the sysContact
     */
    public String getSysContact() {
        return sysContact;
    }

    /**
     * @return the sysDescr
     */
    public String getSysDescr() {
        return sysDescr;
    }

    /**
     * @return the sysLocation
     */
    public String getSysLocation() {
        return sysLocation;
    }

    /**
     * @return the sysName
     */
    public String getSysName() {
        return sysName;
    }

    /**
     * @return the sysObjectID
     */
    public String getSysObjectID() {
        return sysObjectID;
    }

    /**
     * @return the sysServices
     */
    public Integer getSysServices() {
        return sysServices;
    }

    /**
     * @return the sysUpTime
     */
    public String getSysUpTime() {
        return sysUpTime;
    }

    /**
     * @param sysContact
     *            the sysContact to set
     */
    public void setSysContact(String sysContact) {
        this.sysContact = sysContact;
    }

    /**
     * @param sysDescr
     *            the sysDescr to set
     */
    public void setSysDescr(String sysDescr) {
        this.sysDescr = sysDescr;
    }

    /**
     * @param sysLocation
     *            the sysLocation to set
     */
    public void setSysLocation(String sysLocation) {
        this.sysLocation = sysLocation;
    }

    /**
     * @param sysName
     *            the sysName to set
     */
    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    /**
     * @param sysObjectID
     *            the sysObjectID to set
     */
    public void setSysObjectID(String sysObjectID) {
        this.sysObjectID = sysObjectID;
    }

    /**
     * @param sysServices
     *            the sysServices to set
     */
    public void setSysServices(Integer sysServices) {
        this.sysServices = sysServices;
    }

    /**
     * @param sysUpTime
     *            the sysUpTime to set
     */
    public void setSysUpTime(String sysUpTime) {
        this.sysUpTime = sysUpTime;
    }
}
