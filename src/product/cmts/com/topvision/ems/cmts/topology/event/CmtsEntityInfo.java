/***********************************************************************
 * $Id: CmtsEntityInfo.java,v1.0 2015-9-14 上午10:35:41 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.topology.event;

/**
 * @author Rod John
 * @created @2015-9-14-上午10:35:41
 *
 */
public class CmtsEntityInfo {

    private Long entityId;
    private String ipAddress;
    private Long typeId;

    /**
     * 
     * @param entityId
     * @param ipAddress
     * @param typeId
     */
    public CmtsEntityInfo(Long entityId, String ipAddress, Long typeId) {
        this.entityId = entityId;
        this.ipAddress = ipAddress;
        this.typeId = typeId;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return the typeId
     */
    public Long getTypeId() {
        return typeId;
    }

    /**
     * @param typeId the typeId to set
     */
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

}
