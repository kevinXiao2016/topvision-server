/***********************************************************************
 * $Id: EventSource.java,v1.0 2017年1月12日 下午1:11:19 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.domain;

/**
 * @author vanzand
 * @created @2017年1月12日-下午1:11:19
 *
 */
public abstract class EventSource {
    protected String source;

    protected Long entityId;

    public EventSource(Long entityId) {
        super();
        this.entityId = entityId;
    }

    public abstract String formatSource();

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

}
