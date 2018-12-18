/***********************************************************************
 * $Id: Version.java,v 1.1 Sep 23, 2008 11:13:59 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.domain;

import java.sql.Date;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @Create Date Sep 23, 2008 11:13:59 AM
 * 
 * @author kelers
 * 
 */
@Alias("version")
public class Version implements AliasesSuperType {
    private static final long serialVersionUID = -8895553425378230812L;
    private String moduleName = null;
    private String versionNum = null;
    private Date versionDate = null;
    private Timestamp lastTime = null;
    private String note = null;

    /**
     * @param version
     * @param date
     */
    public Version() {
    }

    /**
     * @param version
     * @param date
     */
    public Version(String module, String version, Date date) {
        this.moduleName = module;
        this.versionNum = version;
        this.versionDate = date;
    }

    /**
     * @return the lastTime
     */
    public Timestamp getLastTime() {
        return lastTime;
    }

    /**
     * @return the moduleName
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @return the versionDate
     */
    public Date getVersionDate() {
        return versionDate;
    }

    /**
     * @return the versionNum
     */
    public String getVersionNum() {
        return versionNum;
    }

    /**
     * @param lastTime
     *            the lastTime to set
     */
    public void setLastTime(Timestamp lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * @param moduleName
     *            the moduleName to set
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @param versionDate
     *            the versionDate to set
     */
    public void setVersionDate(Date versionDate) {
        this.versionDate = versionDate;
    }

    /**
     * @param versionNum
     *            the versionNum to set
     */
    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder data = new StringBuilder();
        data.append(moduleName).append("_V").append(versionNum).append("_D").append(versionDate).append("-")
                .append(note).append(";");
        return data.toString();
    }
}
