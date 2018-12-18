/***********************************************************************
 * $Id: CmcSnrQuality.java,v1.0 2017年7月4日 下午1:24:53 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author vanzand
 * @created @2017年7月4日-下午1:24:53
 *
 */
@Alias("cmcSnrQuality")
public class CmcSnrQuality implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6812226002418740424L;
    private Long cmcId;
    private Long channelIndex;
    private Long collectValue;
    private Timestamp collectTime;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public Long getCollectValue() {
        return collectValue;
    }

    public void setCollectValue(Long collectValue) {
        this.collectValue = collectValue;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    @Override
    public String toString() {
        return "CmcSnrQuality [cmcId=" + cmcId + ", channelIndex=" + channelIndex + ", collectValue=" + collectValue
                + ", collectTime=" + collectTime + "]";
    }

}
