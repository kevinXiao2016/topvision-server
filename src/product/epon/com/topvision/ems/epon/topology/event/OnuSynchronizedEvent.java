/***********************************************************************
 * $Id: OnuSynchronizedEvent.java,v1.0 2015-8-7 上午10:17:24 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.event;

import java.util.List;

import com.topvision.platform.message.event.EmsEventObject;

/**
 * @author Rod John
 * @created @2015-8-7-上午10:17:24
 *
 */
public class OnuSynchronizedEvent extends EmsEventObject<OnuSynchronizedListener> {
    private static final long serialVersionUID = -3673483649958371503L;
    private Long entityId;
    private List<Long> onuIndexList;

    /**
     * @param source
     */
    public OnuSynchronizedEvent(Object source) {
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
     * @return the onuIndexList
     */
    public List<Long> getOnuIndexList() {
        return onuIndexList;
    }

    /**
     * @param onuIndexList the onuIndexList to set
     */
    public void setOnuIndexList(List<Long> onuIndexList) {
        this.onuIndexList = onuIndexList;
    }

}
