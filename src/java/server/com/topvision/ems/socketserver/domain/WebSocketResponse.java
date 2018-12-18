/***********************************************************************
 * $Id: WebSocketResponse.java,v1.0 2017年10月23日 上午10:40:31 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.socketserver.domain;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;

/**
 * @author vanzand
 * @created @2017年10月23日-上午10:40:31
 *
 */
public class WebSocketResponse extends SocketResponse {

    private Session session;

    @Override
    public void write(String message) throws IOException {
        session.getRemote().sendString(message);
    }

    @Override
    public void flush() throws IOException {
        session.getRemote().flush();
    }

    @Override
    public boolean isClosed() {
        if (!closed) {
            return !session.isOpen();
        }
        return closed;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

}
