/***********************************************************************
 * $Id: SocketRequestHolder.java,v1.0 2014��10��14�� ����8:43:06 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.socketserver.domain;

import java.util.HashMap;
import java.util.Map;

import com.topvision.platform.zetaframework.var.ZetaValueGetter;

/**
 * @author Bravin
 * 
 */
@ZetaValueGetter("SocketRequest")
public class SocketRequest {
    public static final String USER_ID = "userId";
    private String requestExecBeanName;
    private final Map<String, String> params;
    private SocketResponse response;
    private String requestHost;
    private SocketSession session;
    private String uuid;

    public SocketRequest() {
        params = new HashMap<String, String>();
    }

    /**
     * 添加一个请求参数
     * 
     * @param key
     * @param value
     */
    public void addRequestParameter(String key, String value) {
        params.put(key, value);
        response.addRequestParameter(key, value);
    }

    public int getInt(String paramName) {
        return Integer.parseInt(params.get(paramName));
    }

    public String getString(String paramName) {
        return params.get(paramName);
    }

    public String getRequestExecBeanName() {
        return requestExecBeanName;
    }

    public void setRequestExecBeanName(String requestExecBeanName) {
        this.requestExecBeanName = requestExecBeanName;
    }

    public SocketResponse getResponse() {
        return response;
    }

    public void setResponse(SocketResponse response) {
        this.response = response;
    }

    public String getRequestHost() {
        return requestHost;
    }

    public void setRequestHost(String requestHost) {
        this.requestHost = requestHost;
    }

    public SocketSession getSession() {
        return session;
    }

    public void setSession(SocketSession session) {
        this.session = session;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
