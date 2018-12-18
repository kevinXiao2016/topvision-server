/***********************************************************************
 * $Id: Cmc8800BSynchronizedEvent.java,v1.0 2014-1-8 下午2:28:57 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

import java.util.List;

/**
 * @author Rod John
 * @created @2014-1-8-下午2:28:57
 * 
 */
public class CmcSynchronizedEvent extends EmsEventObject<CmcSynchronizedListener> {
    private static final long serialVersionUID = 1491159505722557316L;
    private Long entityId;
    private List<String> ipAddress;
    private List<Long> cmcIndexList;
    private Long entityType;//CCMTS大类型，CCMTSWITHAGENT，CCMTSWITHOUTAGENT

    /**
     * @param source
     */
    public CmcSynchronizedEvent(Object source) {
        super(source);
    }

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
     * @return the ipAddress
     */
    public List<String> getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(List<String> ipAddress) {
        this.ipAddress = ipAddress;
    }

    public List<Long> getCmcIndexList() {
        return cmcIndexList;
    }

    public void setCmcIndexList(List<Long> cmcIndexList) {
        this.cmcIndexList = cmcIndexList;
    }

    public Long getEntityType() {
        return entityType;
    }

    public void setEntityType(Long entityType) {
        this.entityType = entityType;
    }

}
