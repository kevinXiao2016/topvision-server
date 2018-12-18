/***********************************************************************
 * $Id: SocketRequestHandler.java,v1.0 2014年10月14日 上午8:44:52 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.socketserver.socket;

import com.topvision.ems.socketserver.domain.SocketRequest;
import com.topvision.ems.socketserver.domain.SocketResponse;

/**
 * 所有可以通过socket方式发出的请求只能被实现了该接口的service所处理
 * @author Bravin
 * @created @2014年10月14日-上午8:44:52
 *
 */
public interface SocketRequestExecutor {
    static final boolean CLOSE_SOCKET = true;
    static final boolean MAINTAIN_SOCKET = false;

    /**
     *  执行的方法
     * @param socketRequest
     * @param socketResponse
     * @return  true:关闭socket，false:不关闭socket
     */
    boolean execute(SocketRequest socketRequest, SocketResponse socketResponse);
}
