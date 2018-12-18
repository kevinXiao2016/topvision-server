/***********************************************************************
 * $Id: Message.java,v1.0 2012-12-22 上午11:30:26 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.message;

import java.util.ArrayList;
import java.util.List;

/**
 * 前端消息推送封装
 * 
 * @author Victor
 * @created @2012-12-22-上午11:30:26
 * 
 */
public class Message {
    // 消息推送类型
    public static final String MESSAGE_TYPE = "message";
    // 公告类型
    public static final String NOTICE_TYPE = "notice";
    // 告警类型
    public static final String ALERT_TYPE = "alert";
    // 事件类型
    public static final String ALERT_EVENT = "event";
    // 此字段为需要推送的前端session ID，如果是广播到所有连接上的客户端，此字段为空
    private List<String> sessionIdList = new ArrayList<String>();
    // 消息的ID，用于前端分发判断
    private String id;
    // 消息的类型，用于前端类型判断走不同处理流程，可以为空
    private String type;
    // 消息实体，所包括的内容只能是字符串或者在dwr:configuration中dwr:convert定义的bean，还可以是前者数据的集合封装
    private Object data;
    // 单播时使用.如果希望消息只发送到某一个客户端,则在消息中配置此值,这个值可以需要从前端请求中携带过来
    private String jconnectID;

    public Message(String t) {
        type = t;
    }

    public void addSessionId(String sessionId) {
        sessionIdList.add(sessionId);
    }

    /**
     * @return the sessionIdList
     */
    public List<String> getSessionIdList() {
        return sessionIdList;
    }

    /**
     * @param sessionIdList
     *            the sessionIdList to set
     */
    public void setSessionIdList(List<String> sessionIdList) {
        this.sessionIdList = sessionIdList;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Object data) {
        this.data = data;
    }

    public String getJconnectID() {
        return jconnectID;
    }

    public void setJconnectID(String jconnectID) {
        this.jconnectID = jconnectID;
    }

}
