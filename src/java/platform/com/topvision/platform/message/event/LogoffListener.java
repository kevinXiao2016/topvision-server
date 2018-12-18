/***********************************************************************
 * $Id: LogoffListener.java,v1.0 2017年1月12日 下午8:24:10 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author vanzand
 * @created @2017年1月12日-下午8:24:10
 *
 */
public interface LogoffListener extends EmsListener {
    void logoff(LogoffEvent evt);
}
