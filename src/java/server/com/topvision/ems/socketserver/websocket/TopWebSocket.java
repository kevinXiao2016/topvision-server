package com.topvision.ems.socketserver.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.springframework.context.ApplicationContext;

import com.topvision.framework.utils.ApplicationContextUtil;

public class TopWebSocket implements WebSocketListener {
    private Session session;

    @Override
    public void onWebSocketBinary(byte[] arg0, int arg1, int arg2) {
    }

    @Override
    public void onWebSocketClose(int arg0, String arg1) {
        WebSocketDispatcher webSocketDispatcher = getWebSocketDispatcher();
        webSocketDispatcher.dispatcherWecSocketClosed(session);
    }

    @Override
    public void onWebSocketConnect(Session session) {
        this.session = session;
        WebSocketDispatcher webSocketDispatcher = getWebSocketDispatcher();
        webSocketDispatcher.dispatcherNewWecSocket(session);
    }

    @Override
    public void onWebSocketError(Throwable arg0) {
    }

    @Override
    public void onWebSocketText(String text) {
        WebSocketDispatcher webSocketDispatcher = getWebSocketDispatcher();
        webSocketDispatcher.dispatcherMessage(this.session, text);
    }
    
    private WebSocketDispatcher getWebSocketDispatcher() {
        ApplicationContext context = ApplicationContextUtil.getContext();
        WebSocketDispatcher webSocketDispatcher = (WebSocketDispatcher) context.getBean("webSocketDispatcher");
        return webSocketDispatcher;
    }

}
