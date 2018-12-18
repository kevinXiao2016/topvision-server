/***********************************************************************
 * $Id: SocketSession.java,v1.0 2014-12-31 上午11:39:00 $
 * 
 * @author: zhangdongming
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.socketserver.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangdongming
 * @created @2014-12-31-上午11:39:00
 *
 */
public class SocketSession {
    private String sessionId;
    private Map<Object,Object> data = new HashMap<Object, Object>();
    
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setAttribute(Object name, Object value){
        data.put(name, value);
    }
    
    public Object getAttribute(Object name){
        return data.get(name);
    }
    
}
