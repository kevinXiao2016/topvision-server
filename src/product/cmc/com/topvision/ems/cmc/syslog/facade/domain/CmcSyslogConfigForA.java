/***********************************************************************
 * $Id: CmcSyslogConfigForA.java,v1.0 2013-8-30 下午02:59:41 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.syslog.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author lizongtian
 * @created @2013-8-30-下午02:59:41
 *
 */
public class CmcSyslogConfigForA implements Serializable {

    private static final long serialVersionUID = 7011265651761999128L;

    public static final int EMERGENCYLEVEL = 1;
    public static final int ALERTLEVEL = 2;
    public static final int CRITICALLEVEL = 3;
    public static final int ERRORLEVEL = 4;
    public static final int WARNINGLEVEL = 5;
    public static final int NOTIFICATIONLEVEL = 6;
    public static final int INFORMATIONALLEVEL = 7;
    public static final int DEBUGLEVEL = 8;
    public static final int NONELEVEL = 28;
    public static final int DEFAULTLEVEL = NONELEVEL;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.5.0", writable = true, type = "Integer32")
    private Integer topCcmtsSyslogMaxnum;
    //@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.6.0", writable = true, type = "Integer32")
    private Integer topCcmtsSyslogSwitch = 0;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.7.0", writable = true, type = "Integer32")
    private Integer topCcmtsSyslogFlash;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.8.0", writable = true, type = "Integer32")
    private Integer topCcmtsSyslogMemory;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.9.0", writable = true, type = "Integer32")
    private Integer topCcmtsSysloglogServer;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.10.0", writable = true, type = "Integer32")
    private Integer topCcmtsSyslogTrapServer;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.11.0", writable = true, type = "Integer32")
    private Integer topCcmtsSyslogTrotMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.12.0", writable = true, type = "Integer32")
    private Integer topCcmtsSyslogTrotTrhd;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.13.0", writable = true, type = "Integer32")
    private Integer topCcmtsSyslogTrotInvl;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopCcmtsSyslogMaxnum() {
        return topCcmtsSyslogMaxnum;
    }

    public void setTopCcmtsSyslogMaxnum(Integer topCcmtsSyslogMaxnum) {
        this.topCcmtsSyslogMaxnum = topCcmtsSyslogMaxnum;
    }

    public Integer getTopCcmtsSyslogSwitch() {
        return topCcmtsSyslogSwitch;
    }

    public void setTopCcmtsSyslogSwitch(Integer topCcmtsSyslogSwitch) {
        this.topCcmtsSyslogSwitch = topCcmtsSyslogSwitch;
    }

    public Integer getTopCcmtsSyslogFlash() {
        return topCcmtsSyslogFlash;
    }

    public void setTopCcmtsSyslogFlash(Integer topCcmtsSyslogFlash) {
        this.topCcmtsSyslogFlash = topCcmtsSyslogFlash;
    }

    public Integer getTopCcmtsSyslogMemory() {
        return topCcmtsSyslogMemory;
    }

    public void setTopCcmtsSyslogMemory(Integer topCcmtsSyslogMemory) {
        this.topCcmtsSyslogMemory = topCcmtsSyslogMemory;
    }

    public Integer getTopCcmtsSysloglogServer() {
        return topCcmtsSysloglogServer;
    }

    public void setTopCcmtsSysloglogServer(Integer topCcmtsSysloglogServer) {
        this.topCcmtsSysloglogServer = topCcmtsSysloglogServer;
    }

    public Integer getTopCcmtsSyslogTrapServer() {
        return topCcmtsSyslogTrapServer;
    }

    public void setTopCcmtsSyslogTrapServer(Integer topCcmtsSyslogTrapServer) {
        this.topCcmtsSyslogTrapServer = topCcmtsSyslogTrapServer;
    }

    public Integer getTopCcmtsSyslogTrotMode() {
        return topCcmtsSyslogTrotMode;
    }

    public void setTopCcmtsSyslogTrotMode(Integer topCcmtsSyslogTrotMode) {
        this.topCcmtsSyslogTrotMode = topCcmtsSyslogTrotMode;
    }

    public Integer getTopCcmtsSyslogTrotTrhd() {
        return topCcmtsSyslogTrotTrhd;
    }

    public void setTopCcmtsSyslogTrotTrhd(Integer topCcmtsSyslogTrotTrhd) {
        this.topCcmtsSyslogTrotTrhd = topCcmtsSyslogTrotTrhd;
    }

    public Integer getTopCcmtsSyslogTrotInvl() {
        return topCcmtsSyslogTrotInvl;
    }

    public void setTopCcmtsSyslogTrotInvl(Integer topCcmtsSyslogTrotInvl) {
        this.topCcmtsSyslogTrotInvl = topCcmtsSyslogTrotInvl;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "CmcSyslogConfig [entityId=" + entityId + ", topCcmtsSyslogMaxnum=" + topCcmtsSyslogMaxnum
                + ", topCcmtsSyslogSwitch=" + topCcmtsSyslogSwitch + ", topCcmtsSyslogFlash=" + topCcmtsSyslogFlash
                + ", topCcmtsSyslogMemory=" + topCcmtsSyslogMemory + ", topCcmtsSysloglogServer="
                + topCcmtsSysloglogServer + ", topCcmtsSyslogTrapServer=" + topCcmtsSyslogTrapServer
                + ", topCcmtsSyslogTrotMode=" + topCcmtsSyslogTrotMode + ", topCcmtsSyslogTrotTrhd="
                + topCcmtsSyslogTrotTrhd + ", topCcmtsSyslogTrotInvl=" + topCcmtsSyslogTrotInvl + "]";
    }

}
