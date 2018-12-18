/***********************************************************************
 * $Id: CmImportInfo.java,v1.0 2013-2-4 上午10:57:57 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.sql.Date;

/**
 * @author dosion
 * 
 */
public class CmImportInfo {
    private String cmMacAddr;
    private String cmAlias;
    private String cmClassified;
    private long importTime;
    private String time;
    private String cmPhoneNo;

    /**
     * @return the cmMacAddr
     */
    public String getCmMacAddr() {
        return cmMacAddr;
    }

    /**
     * @param cmMacAddr
     *            the cmMacAddr to set
     */
    public void setCmMacAddr(String cmMacAddr) {
        this.cmMacAddr = cmMacAddr;
    }

    /**
     * @return the cmAlias
     */
    public String getCmAlias() {
        return cmAlias;
    }

    /**
     * @param cmAlias
     *            the cmAlias to set
     */
    public void setCmAlias(String cmAlias) {
        this.cmAlias = cmAlias;
    }

    /**
     * @return the cmClassified
     */
    public String getCmClassified() {
        return cmClassified;
    }

    /**
     * @param cmClassified
     *            the cmClassified to set
     */
    public void setCmClassified(String cmClassified) {
        this.cmClassified = cmClassified;
    }

    /**
     * @return the importTime
     */
    public long getImportTime() {
        return importTime;
    }

    /**
     * @param importTime
     *            the importTime to set
     */
    public void setImportTime(long importTime) {
        this.importTime = importTime;
        this.time = new Date(importTime).toString();
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time
     *            the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    public String getCmPhoneNo() {
        return cmPhoneNo;
    }

    public void setCmPhoneNo(String cmPhoneNo) {
        this.cmPhoneNo = cmPhoneNo;
    }

}
