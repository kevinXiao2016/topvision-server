/***********************************************************************
 * $Id: IgmpForwardingSnooping.java,v1.0 2012-12-18 上午10:45:56 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.util.Date;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2012-12-18-上午10:45:56
 * 
 */
public class IgmpForwardingSnooping implements AliasesSuperType {
    private static final long serialVersionUID = 5047769451624622782L;
    private Long entityId;
    private Integer vid;
    private Long portIndex;
    private String portString;
    private Date lastChangeTime;
    private String lastChangeTimeString;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the vid
     */
    public Integer getVid() {
        return vid;
    }

    /**
     * @param vid
     *            the vid to set
     */
    public void setVid(Integer vid) {
        this.vid = vid;
    }

    /**
     * @return the portIndex
     */
    public Long getPortIndex() {
        return portIndex;
    }

    /**
     * @param portIndex
     *            the portIndex to set
     */
    public void setPortIndex(Long portIndex) {
        this.portString = EponIndex.getPortStringByIndex(portIndex).toString();
        this.portIndex = portIndex;
    }

    /**
     * @return the lastChangeTime
     */
    public Date getLastChangeTime() {
        return lastChangeTime;
    }

    /**
     * @param lastChangeTime
     *            the lastChangeTime to set
     */
    public void setLastChangeTime(Date lastChangeTime) {
        this.lastChangeTimeString = lastChangeTime.toString();
        this.lastChangeTime = lastChangeTime;
    }

    /**
     * @return the lastChangeTimeString
     */
    public String getLastChangeTimeString() {
        return lastChangeTimeString;
    }

    /**
     * @param lastChangeTimeString
     *            the lastChangeTimeString to set
     */
    public void setLastChangeTimeString(String lastChangeTimeString) {
        this.lastChangeTimeString = lastChangeTimeString;
    }

    /**
     * @return the portString
     */
    public String getPortString() {
        return portString;
    }

    /**
     * @param portString
     *            the portString to set
     */
    public void setPortString(String portString) {
        this.portString = portString;
    }

}
