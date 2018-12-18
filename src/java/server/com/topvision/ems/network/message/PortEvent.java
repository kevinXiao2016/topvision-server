/***********************************************************************
 * $Id: PortEvent.java,v 1.1 2009-10-10 下午11:06:50 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.message;

import com.topvision.ems.network.domain.Port;
import com.topvision.platform.message.event.EmsEventObject;

/**
 * @Create Date 2009-10-10 下午11:06:50
 * 
 * @author kelers
 * 
 */
public class PortEvent extends EmsEventObject<PortListener> {
    private static final long serialVersionUID = 224818736444298216L;
    private Port port;

    /**
     * @param source
     */
    public PortEvent(Object source) {
        super(source);
    }

    /**
     * @return the port
     */
    public Port getPort() {
        return port;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(Port port) {
        this.port = port;
    }
}
