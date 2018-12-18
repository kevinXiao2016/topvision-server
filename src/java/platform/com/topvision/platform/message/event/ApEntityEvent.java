/***********************************************************************
 * $Id: ApEntityEvent.java,v1.0 2013-1-22 下午4:59:29 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author loyal
 * @created @2013-1-22-下午4:59:29
 *
 */
public class ApEntityEvent extends EmsEventObject<ApEntityListener>{
    private static final long serialVersionUID = -3167311161078455150L;
    public ApEntityEvent(Object source) {
        super(source);
    }
    private String ip;
    private Long entityId;
    private String mac;
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public Long getEntityId() {
        return entityId;
    }
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
}
