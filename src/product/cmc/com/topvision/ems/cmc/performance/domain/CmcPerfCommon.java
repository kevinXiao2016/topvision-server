/***********************************************************************
 * $Id: CmcPerfCommon.java,v1.0 2013-12-3 上午10:12:06 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2013-12-3-上午10:12:06
 * 
 */
public class CmcPerfCommon implements AliasesSuperType {
    private static final long serialVersionUID = -3951321283103548287L;
    private Long cmcId;
    private Long channelIndex;
    private String targetName;
    private Float targetValue;
    private String collectTime;

    /**
     * @return the targetName
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * @param targetName
     *            the targetName to set
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * @return the targetValue
     */
    public Float getTargetValue() {
        return targetValue;
    }

    /**
     * @param targetValue
     *            the targetValue to set
     */
    public void setTargetValue(Float targetValue) {
        this.targetValue = targetValue;
    }

    /**
     * @return the collectTime
     */
    public String getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the channelIndex
     */
    public Long getChannelIndex() {
        return channelIndex;
    }

    /**
     * @param channelIndex
     *            the channelIndex to set
     */
    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcPerfCommon [cmcId=");
        builder.append(cmcId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", targetName=");
        builder.append(targetName);
        builder.append(", targetValue=");
        builder.append(targetValue);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append("]");
        return builder.toString();
    }

}
