/***********************************************************************
 * $Id: FlowListener.java,v 1.1 Sep 12, 2009 5:16:47 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @Create Date Sep 12, 2009 5:16:47 PM
 * 
 * @author kelers
 * 
 */
public interface FlowListener extends EmsListener {
    /**
     * 实时流量变化
     * 
     * @param event
     */
    void flowChanged(FlowEvent event);
}
