/***********************************************************************
 * $Id: WebSocketDispatcher.java,v1.0 2017年10月21日 下午2:13:23 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.socketserver.websocket;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.topvision.ems.socketserver.domain.SocketRequest;
import com.topvision.ems.socketserver.domain.WebSocketResponse;

/**
 * websocket分发器，将socket发送给合适的处理service
 * 
 * @author vanzand
 * @created @2017年10月21日-下午2:13:23
 * 
 *
 */
@Service("webSocketDispatcher")
public class WebSocketDispatcher {
    private static Logger logger = LoggerFactory.getLogger(WebSocketDispatcher.class);
    @Autowired
    private BeanFactory beanFacatory;

    private ConcurrentHashMap<Session, SocketRequest> socketRequestMap = new ConcurrentHashMap<Session, SocketRequest>();

    public void dispatcherNewWecSocket(Session session) {
        SocketRequest socketRequest = initWebSocketRequest(session);
        socketRequestMap.put(session, socketRequest);

        try {
            // 找到对应的业务service
            TopWebSocketHandler handler = getTopWebSocketHandler(socketRequest.getRequestExecBeanName());
            handler.onConnected(socketRequest);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void dispatcherMessage(Session session, String message) {
        SocketRequest socketRequest = socketRequestMap.get(session);

        if ("~H#B~".equals(message)) {
            // 心跳报文，忽略
            return;
        }

        JSONObject json = JSONObject.parseObject(message);
        for (String key : json.keySet()) {
            socketRequest.addRequestParameter(key, json.getString(key));
        }

        try {
            // 找到对应的业务service
            TopWebSocketHandler handler = getTopWebSocketHandler(socketRequest.getRequestExecBeanName());
            handler.onMessage(socketRequest);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void dispatcherWecSocketClosed(Session session) {
        SocketRequest socketRequest = socketRequestMap.get(session);
        try {
            // 找到对应的业务service
            TopWebSocketHandler handler = getTopWebSocketHandler(socketRequest.getRequestExecBeanName());
            handler.onClose(socketRequest);
        } catch (Exception e) {
            logger.error("", e);
        }
        socketRequestMap.remove(session);
    }
    
    
    
    public ConcurrentHashMap<Session, SocketRequest> getSocketRequestMap() {
        return socketRequestMap;
    }

    private SocketRequest initWebSocketRequest(Session session) {
        SocketRequest socketRequest = new SocketRequest();
        WebSocketResponse response = new WebSocketResponse();
        response.setSession(session);
        socketRequest.setResponse(response);
        socketRequest.setUuid(UUID.randomUUID().toString());

        URI uri = session.getUpgradeRequest().getRequestURI();
        socketRequest.setRequestHost(uri.getHost());

        String path = session.getUpgradeRequest().getRequestURI().getPath();
        if (path.startsWith("/websocket/")) {
            path = path.substring(path.indexOf("/websocket/") + "/websocket/".length());
            String beanName = path.split("/")[0];
            socketRequest.setRequestExecBeanName(beanName);

            String queryStr = session.getUpgradeRequest().getRequestURI().getQuery();
            if (queryStr != null && queryStr != "") {
                String[] params = queryStr.split("&");
                for (String param : params) {
                    if (!param.contains("=")) {
                        continue;
                    }
                    int c = param.indexOf("=");
                    socketRequest.addRequestParameter(param.substring(0, c), param.substring(c + 1));
                }
            }
        } else {
            return null;
        }
        return socketRequest;

    }

    private TopWebSocketHandler getTopWebSocketHandler(String serviceName) {
        TopWebSocketHandler handler = beanFacatory.getBean(serviceName, TopWebSocketHandler.class);
        return handler;
    }
}
