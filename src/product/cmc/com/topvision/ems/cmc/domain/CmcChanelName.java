/***********************************************************************
 * $ CmcChanelName.java,v1.0 2012-7-19 16:06:31 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.io.Serializable;

/**
 * @author jay
 * @created @2012-7-19-16:06:31
 */
public class CmcChanelName implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    private Long cmcId;
    private Long chanelIndex;
    private String chanelName;

    public Long getChanelIndex() {
        return chanelIndex;
    }

    public void setChanelIndex(Long chanelIndex) {
        this.chanelIndex = chanelIndex;
    }

    public String getChanelName() {
        return chanelName;
    }

    public void setChanelName(String chanelName) {
        this.chanelName = chanelName;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CmcChanelName");
        sb.append("{chanelIndex=").append(chanelIndex);
        sb.append(", entityId=").append(entityId);
        sb.append(", cmcId=").append(cmcId);
        sb.append(", chanelName='").append(chanelName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
