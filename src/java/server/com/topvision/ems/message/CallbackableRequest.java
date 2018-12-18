/***********************************************************************
 * $Id: CallbackableRequest.java,v1.0 2016年4月29日 上午11:31:00 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.message;

import net.sf.json.JSONObject;

/**
 * @author Bravin
 * @created @2016年4月29日-上午11:31:00
 *
 */
public abstract class CallbackableRequest implements Runnable {
    protected String jconnectionId;
    protected String registerHandler;
    protected MessagePusher messagePusher;
    private boolean compelted;

    public CallbackableRequest(String jconnectionId) {
        this.jconnectionId = jconnectionId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            execute();
            if (!compelted) {
                completeRequest(LongRequest.COMPLETE);
            }
        } catch (Exception e) {
            completeRequest(LongRequest.ERROR_INTERRUPT);
        }
    }

    protected void returnMessage(Object o) {
        Message message = getMessage();
        addParam(message, "status", LongRequest.PROCESS);
        addParam(message, "data", o);
        messagePusher.sendMessage(message);
    }

    /**
     * 长连接执行完毕
     * @param success 
     */
    protected void completeRequest(Integer status) {
        compelted = true;
        Message message = getMessage();
        addParam(message, "status", status);
        messagePusher.sendMessage(message);
    }

    protected void completeRequest(Object data) {
        compelted = true;
        Message message = getMessage();
        addParam(message, "status", LongRequest.COMPLETE);
        addParam(message, "data", data);
        messagePusher.sendMessage(message);
    }

    /**
     * @return
     */
    private Message getMessage() {
        Message message = new Message(registerHandler == null ? LongRequest.LONG_REQUEST_REGISTER : registerHandler);
        message.setJconnectID(jconnectionId);
        JSONObject object = new JSONObject();
        message.setData(object);
        return message;
    }

    private void addParam(Message message, String key, Object value) {
        JSONObject data = (JSONObject) message.getData();
        data.put(key, value);
    }

    protected abstract void execute();

    public String getJconnectionId() {
        return jconnectionId;
    }

    public void setJconnectionId(String jconnectionId) {
        this.jconnectionId = jconnectionId;
    }

    public String getRegisterHandler() {
        return registerHandler;
    }

    public void setRegisterHandler(String registerHandler) {
        this.registerHandler = registerHandler;
    }

    public MessagePusher getMessagePusher() {
        return messagePusher;
    }

    public void setMessagePusher(MessagePusher messagePusher) {
        this.messagePusher = messagePusher;
    }

}
