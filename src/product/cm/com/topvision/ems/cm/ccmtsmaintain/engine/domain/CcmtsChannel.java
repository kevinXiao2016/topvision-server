/***********************************************************************
 * $Id: CcmtsChannel.java,v1.0 2015-5-26 下午8:18:04 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.ccmtsmaintain.engine.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author fanzidong
 * @created @2015-5-26-下午8:18:04
 * 
 */
public class CcmtsChannel implements AliasesSuperType {
    private static final long serialVersionUID = 7029823080590611817L;

    private Long cmcId;
    private Integer channelId;
    private Long channelIndex;
    private Long channelWidth;

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

    public Long getChannelWidth() {
        return channelWidth;
    }

    public void setChannelWidth(Long channelWidth) {
        this.channelWidth = channelWidth;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

}
