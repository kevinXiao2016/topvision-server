/***********************************************************************
 * $Id: PerformanceShiftEvent.java,v1.0 2016-6-29 上午10:43:56 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author Rod John
 * @created @2016-6-29-上午10:43:56
 *
 */
public class PerformanceShiftEvent extends EmsEventObject<PerformanceShiftListener> {
    private static final long serialVersionUID = 2289650771229011420L;
    private Integer engineId;

    public PerformanceShiftEvent(Object source) {
        super(source);
    }

    public PerformanceShiftEvent(Class<?> listener, String actionName) {
        super(listener);
        setListener(listener);
        setActionName(actionName);
    }
    
    /**
     * @return the engineId
     */
    public Integer getEngineId() {
        return engineId;
    }

    /**
     * @param engineId the engineId to set
     */
    public void setEngineId(Integer engineId) {
        this.engineId = engineId;
    }

}
