/***********************************************************************
 * $Id: TopWebSocketHandler.java,v1.0 2017年10月23日 下午12:57:49 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.socketserver.websocket;

import com.topvision.ems.socketserver.domain.SocketRequest;

/**
 * websocket业务类需要实现的接口
 * 
 * @author vanzand
 * @created @2017年10月23日-下午12:57:49
 *
 */
public interface TopWebSocketHandler {

    /**
     * websocket连接上时触发
     * 
     * @param socketRequest
     */
    void onConnected(SocketRequest socketRequest);

    /**
     * websocket关闭时触发
     * 
     * @param socketRequest
     */
    void onClose(SocketRequest socketRequest);

    /**
     * websocket收到消息时触发
     * 
     * @param socketRequest
     */
    void onMessage(SocketRequest socketRequest);

}
