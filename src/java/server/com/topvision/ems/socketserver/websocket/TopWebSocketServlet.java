/***********************************************************************
 * $Id: MyEchoServlet.java,v1.0 2017年10月21日 上午11:15:46 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.socketserver.websocket;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * @author vanzand
 * @created @2017年10月21日-上午11:15:46
 *
 */

public class TopWebSocketServlet extends WebSocketServlet {
    private static final long serialVersionUID = -1930413503077578498L;

    @Override
    public void configure(WebSocketServletFactory factory) {
        // jetty WebSocket hangs in blockingWrite #2061
        // https://github.com/eclipse/jetty.project/issues/2061
        // 增加超时配置
        // factory.getPolicy().setIdleTimeout(10000);
        factory.register(TopWebSocket.class);
    }
}
