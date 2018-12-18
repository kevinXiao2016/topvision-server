/***********************************************************************
 * $Id: PortListener.java,v 1.1 2009-10-10 下午11:07:03 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.message;

import com.topvision.platform.message.event.EmsListener;

/**
 * @Create Date 2009-10-10 下午11:07:03
 * 
 * @author kelers
 * 
 */
public interface PortListener extends EmsListener {
    void stateChanged(PortEvent event);
}
