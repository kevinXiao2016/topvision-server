/***********************************************************************
 * $Id: DwrInfo.java,v1.0 2013-1-8 上午10:00:07 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;

/**
 * @author RodJohn
 * @created @2013-1-8-上午10:00:07
 *
 */
public class DwrInfo implements Serializable {

    private static final long serialVersionUID = -3234829277107567387L;

    private String dwrId;
    private String jconnectID;
    private Long userId;
    private Long segmentId;

    public DwrInfo(String dwrId, String jconnectID, Long userId, Long segmentId) {
        this.dwrId = dwrId;
        this.setJconnectID(jconnectID);
        this.userId = userId;
        this.segmentId = segmentId;
    }

    /**
     * @return the dwrId
     */
    public String getDwrId() {
        return dwrId;
    }

    /**
     * @param dwrId the dwrId to set
     */
    public void setDwrId(String dwrId) {
        this.dwrId = dwrId;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }

    public String getJconnectID() {
        return jconnectID;
    }

    public void setJconnectID(String jconnectID) {
        this.jconnectID = jconnectID;
    }

}
