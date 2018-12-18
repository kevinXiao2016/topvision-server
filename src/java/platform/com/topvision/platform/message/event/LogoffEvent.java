/***********************************************************************
 * $Id: LogoffEvent.java,v1.0 2017年1月12日 下午8:24:44 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author vanzand
 * @created @2017年1月12日-下午8:24:44
 *
 */
public class LogoffEvent extends EmsEventObject<LogoffListener> {

    private static final long serialVersionUID = -6577621286483909612L;
    
    private String sessionId;

    public LogoffEvent(Object source) {
        super(source);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
