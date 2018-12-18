/***********************************************************************
 * $Id: CmFileNameChangeLog.java,v1.0 2016年11月14日 下午2:00:03 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2016年11月14日-下午2:00:03
 *
 */
public class CmFileNameChangeLog implements AliasesSuperType {
    private static final long serialVersionUID = 4845465887391407715L;

    private Long cmId;
    private String cmMac;
    private String oldFileName;
    private String oldServiceType;
    private String newFileName;
    private String newServiceType;
    private String changeTime;

    /**
     * @return the cmId
     */
    public Long getCmId() {
        return cmId;
    }

    /**
     * @param cmId
     *            the cmId to set
     */
    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    /**
     * @return the cmMac
     */
    public String getCmMac() {
        return cmMac;
    }

    /**
     * @param cmMac
     *            the cmMac to set
     */
    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    /**
     * @return the oldFileName
     */
    public String getOldFileName() {
        if (this.oldServiceType != null && !"".equals(oldServiceType)) {
            oldFileName = oldFileName + "(" + oldServiceType + ")";
        }
        return oldFileName;
    }

    /**
     * @param oldFileName
     *            the oldFileName to set
     */
    public void setOldFileName(String oldFileName) {
        this.oldFileName = oldFileName;
    }

    /**
     * @return the newFileName
     */
    public String getNewFileName() {
        if (this.newServiceType != null && !"".equals(newServiceType)) {
            newFileName = newFileName + "(" + newServiceType + ")";
        }
        return newFileName;
    }

    /**
     * @param newFileName
     *            the newFileName to set
     */
    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    /**
     * @return the changeTime
     */
    public String getChangeTime() {
        return changeTime;
    }

    /**
     * @param changeTime
     *            the changeTime to set
     */
    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    public String getOldServiceType() {
        return oldServiceType;
    }

    public void setOldServiceType(String oldServiceType) {
        this.oldServiceType = oldServiceType;
    }

    public String getNewServiceType() {
        return newServiceType;
    }

    public void setNewServiceType(String newServiceType) {
        this.newServiceType = newServiceType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmFileNameChangeLog [cmId=");
        builder.append(cmId);
        builder.append(", cmMac=");
        builder.append(cmMac);
        builder.append(", oldFileName=");
        builder.append(oldFileName);
        builder.append(", oldServiceType=");
        builder.append(oldServiceType);
        builder.append(", newFileName=");
        builder.append(newFileName);
        builder.append(", newServiceType=");
        builder.append(newServiceType);
        builder.append(", changeTime=");
        builder.append(changeTime);
        builder.append("]");
        return builder.toString();
    }

}
