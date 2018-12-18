package com.topvision.platform.domain;

import com.topvision.framework.domain.BaseEntity;

public class SystemMessage<T> extends BaseEntity {
    private static final long serialVersionUID = -8957385532913608596L;

    public static final int ENTITY_MSG_TYPE = 1;
    private static final int UNKNOWN_MSG_TYPE = 0;
    private int msgType = UNKNOWN_MSG_TYPE;
    private T msgObj = null;

    public T getMsgObj() {
        return msgObj;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgObj(T msgObj) {
        this.msgObj = msgObj;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "SystemMessage{" + "msgObj=" + msgObj + ", msgType=" + msgType + '}';
    }
}
