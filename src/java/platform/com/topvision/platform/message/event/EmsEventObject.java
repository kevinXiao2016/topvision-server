/***********************************************************************
 * $Id: EmsEventObject.java,v 1.1 Sep 17, 2009 9:51:32 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

import java.util.EventObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Create Date Sep 17, 2009 9:51:32 AM
 * 
 * @author kelers
 * 
 */
public class EmsEventObject<T> extends EventObject {
    private static final long serialVersionUID = 444646939414018122L;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private String actionName;
    private Class<?> listener;
    // 用于监控消息执行，可以通过回调接口告知消息发送者被谁执行和执行完成。
    private MessageCallback callback;

    /**
     * @param source
     */
    public EmsEventObject(Object source) {
        super(source);
    }

    /**
     * @return the actionName
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * @return the listener
     */
    public Class<?> getListener() {
        return listener;
    }

    /**
     * @param actionName
     *            the actionName to set
     */
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    /**
     * @param listener
     *            the listener to set
     */
    public void setListener(Class<?> listener) {
        this.listener = listener;
    }

    /**
     * @return the callback
     */
    public MessageCallback getCallback() {
        return callback;
    }

    /**
     * @param callback
     *            the callback to set
     */
    public void setCallback(MessageCallback callback) {
        this.callback = callback;
    }
}
