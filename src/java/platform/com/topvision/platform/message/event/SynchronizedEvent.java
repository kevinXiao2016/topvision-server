/***********************************************************************
 * $Id: SynchronizedEvent.java,v1.0 2011-10-30 上午08:59:42 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author huqiao
 * @created @2011-10-30-上午08:59:42
 * 
 */
public class SynchronizedEvent extends EmsEventObject<SynchronizedListener> {
    private static final long serialVersionUID = 2306735643415635953L;
    public static final int ADD_SYNCHRONIZED = 1;
    public static final int DELETE_SYNCHRONIZED = 2;
    public static final int UPDATE_SYNCHRONIZED = 3;
    private int action = DELETE_SYNCHRONIZED;
    private long entityId;
    private String eventType;
    private String ipAddress;
    private Object data;

    /**
     * @param source
     */
    public SynchronizedEvent(Object source) {
        super(source);
    }

    /**
     * @return the action
     */
    public int getAction() {
        return action;
    }

    /**
     * @param action
     *            the action to set
     */
    public void setAction(int action) {
        this.action = action;
    }

    /**
     * @return the entityId
     */
    public long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the eventType
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * @param eventType
     *            the eventType to set
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
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
    
    
}


