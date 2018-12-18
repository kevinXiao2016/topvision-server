/***********************************************************************
 * $Id: SummaryEvent.java,v1.0 2013-7-2 下午3:40:18 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.message;

import com.topvision.platform.message.event.EmsEventObject;

/**
 * @author Bravin
 * @created @2013年07月02日-下午3:40:18
 * 
 */
public class SummaryEvent extends EmsEventObject<SummaryListener> {
    private static final long serialVersionUID = -1581993422552382391L;

    public SummaryEvent(Object source) {
        super(source);
    }

}
