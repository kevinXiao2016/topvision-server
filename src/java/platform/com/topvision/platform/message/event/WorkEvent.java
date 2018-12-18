/***********************************************************************
 * $ WorkEvent.java,v1.0 2011-7-21 18:31:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author jay
 * @created @2011-7-21-18:31:16
 */
public class WorkEvent extends EmsEventObject<WorkListener> {
    private static final long serialVersionUID = 7494220547976551595L;

    /**
     * @param source
     */
    public WorkEvent(Object source) {
        super(source);
    }
}