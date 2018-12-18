/***********************************************************************
 * $Id: PerformanceShiftListener.java,v1.0 2016-6-29 上午10:42:47 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author Rod John
 * @created @2016-6-29-上午10:42:47
 *
 */
public interface PerformanceShiftListener extends EmsListener {

    /**
     * Performance Task Shift
     * 
     * @param engineId
     */
    void performanceShift(PerformanceShiftEvent event);
           
}
