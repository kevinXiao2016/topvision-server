/***********************************************************************
 * $Id: AlertEvent.java,v 1.1 Sep 17, 2009 9:56:56 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.message;

import com.topvision.ems.fault.domain.Alert;
import com.topvision.platform.message.event.EmsEventObject;

/**
 * @Create Date Sep 17, 2009 9:56:56 AM
 * 
 * @author kelers
 * 
 */
public class AlertEvent extends EmsEventObject<AlertListener> {
    private static final long serialVersionUID = 7494290547976551595L;
    private Alert alert;

    /**
     * @param source
     */
    public AlertEvent(Object source) {
        super(source);
    }

    /**
     * @return the alert
     */
    public final Alert getAlert() {
        return alert;
    }

    /**
     * @param alert
     *            the alert to set
     */
    public final void setAlert(Alert alert) {
        this.alert = alert;
    }
}
