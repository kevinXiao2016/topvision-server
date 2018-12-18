/***********************************************************************
 * $Id: PingEvent.java,v1.0 2012-4-25 下午04:33:59 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author huqiao
 * @created @2012-4-25-下午04:33:59
 * 
 */
public class PingEvent extends EmsEventObject<PingListener> {

    private static final long serialVersionUID = -7842269592606674036L;
    private Integer code;
    private Long entityId;

    /**
     * @param source
     */
    public PingEvent(Object source) {
        super(source);
    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
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

}
