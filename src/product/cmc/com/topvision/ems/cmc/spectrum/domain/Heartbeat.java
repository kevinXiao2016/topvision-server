/***********************************************************************
 * $ Heartbeat.java,v1.0 2014-1-8 11:02:47 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.domain;

import java.io.Serializable;

/**
 * @author jay
 * @created @2014-1-8-11:02:47
 */
public class Heartbeat implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long callbackId;
    private String sessionId;
    private Long cmcId;
    private Integer type;// 0 web 1 service
    private Long heartbeatTime;
    private Long createTime;
    private String dwrId;

    public static int WEB = 0;
    public static int REALTIME = 1;
    public static int HISTORY = 2;
    public static int MOBILE = 3;

    public Heartbeat() {
    }

    public String getDwrId() {
        return dwrId;
    }

    public void setDwrId(String dwrId) {
        this.dwrId = dwrId;
    }

    public Long getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(Long callbackId) {
        this.callbackId = callbackId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getHeartbeatTime() {
        return heartbeatTime;
    }

    public void setHeartbeatTime(Long heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Heartbeat))
            return false;
        Heartbeat heartbeat = (Heartbeat) o;
        return !(callbackId != null ? !callbackId.equals(heartbeat.callbackId) : heartbeat.callbackId != null);
    }

    public int hashCode() {
        int result;
        result = (callbackId != null ? callbackId.hashCode() : 0);
        result = 31 * result + (callbackId != null ? callbackId.hashCode() : 0);
        result = 31 * result + (sessionId != null ? sessionId.hashCode() : 0);
        result = 31 * result + (cmcId != null ? cmcId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (heartbeatTime != null ? heartbeatTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Heartbeat");
        sb.append("{callbackId=").append(callbackId);
        sb.append(", sessionId='").append(sessionId).append('\'');
        sb.append(", cmcId=").append(cmcId);
        sb.append(", type=").append(type);
        sb.append(", heartbeatTime=").append(heartbeatTime);
        sb.append('}');
        return sb.toString();
    }
}
