/***********************************************************************
 * $Id: AlertEvent.java,v 1.1 Sep 17, 2009 9:56:56 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.message;

import com.topvision.platform.message.event.EmsEventObject;

/**
 * @Create Date Sep 17, 2009 9:56:56 AM
 * 
 * @author kelers
 * 
 */
public class CmPollStateEvent extends EmsEventObject<CmPollStateListener> {
    private static final long serialVersionUID = 7494290547976551595L;
    /**
     * @param source
     */
    public CmPollStateEvent(Object source) {
        super(source);
    }

}
