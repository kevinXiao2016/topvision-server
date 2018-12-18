/***********************************************************************
 * $Id: EmsInfo.java,v1.0 2016年9月26日 下午6:49:36 $
 * 
 * @author: Victorli
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin.domain;

import java.util.Date;

/**
 * @author Victorli
 * @created @2016年9月26日-下午6:49:36
 *
 */
public class EmsInfo implements java.io.Serializable {
    private static final long serialVersionUID = -4454742076754313870L;
    private String hid;
    private String version;
    private Date emsCurrentTime;
    private Date emsSysUptime;
    private Date mysqlUptime;
    private long totalMem;
    private long totalVmem;
    private long totalDisk;
    private long initMem;
    private long maxMem;
    private long committedMem;

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getEmsCurrentTime() {
        return emsCurrentTime;
    }

    public void setEmsCurrentTime(Date emsCurrentTime) {
        this.emsCurrentTime = emsCurrentTime;
    }

    public Date getEmsSysUptime() {
        return emsSysUptime;
    }

    public void setEmsSysUptime(Date emsSysUptime) {
        this.emsSysUptime = emsSysUptime;
    }

    public Date getMysqlUptime() {
        return mysqlUptime;
    }

    public void setMysqlUptime(Date mysqlUptime) {
        this.mysqlUptime = mysqlUptime;
    }

    public long getTotalMem() {
        return totalMem;
    }

    public void setTotalMem(long totalMem) {
        this.totalMem = totalMem;
    }

    public long getTotalVmem() {
        return totalVmem;
    }

    public void setTotalVmem(long totalVmem) {
        this.totalVmem = totalVmem;
    }

    public long getTotalDisk() {
        return totalDisk;
    }

    public void setTotalDisk(long totalDisk) {
        this.totalDisk = totalDisk;
    }

    public long getInitMem() {
        return initMem;
    }

    public void setInitMem(long initMem) {
        this.initMem = initMem;
    }

    public long getMaxMem() {
        return maxMem;
    }

    public void setMaxMem(long maxMem) {
        this.maxMem = maxMem;
    }

    public long getCommittedMem() {
        return committedMem;
    }

    public void setCommittedMem(long committedMem) {
        this.committedMem = committedMem;
    }
}
