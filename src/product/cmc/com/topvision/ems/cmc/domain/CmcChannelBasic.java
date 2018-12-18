/***********************************************************************
 * $Id: CmcChannelBasic.java,v1.0 2013-8-8 下午03:21:37 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lizongtian
 * @created @2013-8-8-下午03:21:37
 *
 */
@Alias("cmcChannelBasic")
public class CmcChannelBasic implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -828131138244851783L;

    private Long entityId;
    private Long channelId;
    private Long channelIndex;
    private String chanelName;
    private String channelType;
    private Integer channelStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChanelName() {
        return chanelName;
    }

    public void setChanelName(String chanelName) {
        this.chanelName = chanelName;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public Integer getChannelStatus() {
        return channelStatus;
    }

    public void setChannelStatus(Integer channelStatus) {
        this.channelStatus = channelStatus;
    }

}
