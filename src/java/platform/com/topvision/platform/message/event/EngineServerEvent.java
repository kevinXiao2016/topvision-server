/***********************************************************************
 * $Id: EngineServerEvent.java,v1.0 2015年3月25日 上午8:46:23 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

import com.topvision.platform.domain.EngineServer;

/**
 * @author Victor
 * @created @2015年3月25日-上午8:46:23
 *
 */
public class EngineServerEvent extends EmsEventObject<EngineServerListener> {
    public static final byte STATUS_ADDED = 1;
    public static final byte STATUS_REMOVED = 2;
    public static final byte STATUS_ENABLED = 3;
    public static final byte STATUS_DISABLED = 4;
    public static final byte STATUS_CONNECTED = 5;
    public static final byte STATUS_DISCONNECTED = 6;
    public static final byte STATUS_MODIFIED = 7;
    private static final long serialVersionUID = 3991739826265480500L;
    private EngineServer engineServer;
    private byte status;

    /**
     * @param obj
     */
    public EngineServerEvent(Object obj) {
        super(obj);
    }

    public EngineServerEvent(Class<?> listener, String actionName) {
        super(listener);
        setListener(listener);
        setActionName(actionName);
    }

    public EngineServer getEngineServer() {
        return engineServer;
    }

    public void setEngineServer(EngineServer engineServer) {
        this.engineServer = engineServer;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EngineServerEvent [engineServer=").append(engineServer).append(", status=").append(status)
                .append("]");
        return builder.toString();
    }
}
