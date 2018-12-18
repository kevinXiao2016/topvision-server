/***********************************************************************
 * $Id: AlertEvent.java,v 1.1 Sep 17, 2009 9:56:56 AM kelers Exp $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.message;

import com.topvision.ems.cm.cmpoll.message.CmPollStateListener;
import com.topvision.platform.message.event.EmsEventObject;

/**
 * @Create Date Sep 17, 2009 9:56:56 AM
 * 
 * @author jay
 * 
 */
public class PnmpPollStateEvent extends EmsEventObject<CmPollStateListener> {
    private static final long serialVersionUID = 7494290547976551595L;
    public static String LOW = "LOW";
    public static String MIDDLE = "MIDDLE";
    public static String HIGH = "HIGH";
    public static String ALL = "ALL";
    private String type;//
    /**
     * @param source
     */
    public PnmpPollStateEvent(Object source) {
        super(source);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PnmpPollStateEvent{" +
                "type='" + type + '\'' +
                '}';
    }
}
