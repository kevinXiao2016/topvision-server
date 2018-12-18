/***********************************************************************
 * $Id: SocketResponse.java,v1.0 2014��10��14�� ����8:43:55 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.socketserver.domain;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bravin
 * @created @2014��10��14��-����8:43:55
 * 
 */
public class SocketResponse {
    protected Socket socket;
    protected boolean closed;
    protected Map<String, String> params = new HashMap<String, String>();;

    /**
     * 添加一个请求参数
     * 
     * @param key
     * @param value
     */
    public void addRequestParameter(String key, String value) {
        params.put(key, value);
    }

    public int getInt(String paramName) {
        return Integer.parseInt(params.get(paramName));
    }

    public long getLong(String paramName) {
        return Long.parseLong(params.get(paramName));
    }

    public String getString(String paramName) {
        return params.get(paramName);
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void write(String message) throws IOException {
        OutputStream os = socket.getOutputStream();
        os.write((message+"@").getBytes("utf-8"));
        os.flush();
    }

    public void writeIgnoreExcp(String message, Object... args) {
        try {
            write(String.format(message, args));
        } catch (IOException e) {
        }
    }

    public void flush() throws IOException {
        OutputStream os = socket.getOutputStream();
        os.flush();
    }

    public boolean isClosed() {
        if (!closed) {
            return socket.isClosed();
        }
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

}
