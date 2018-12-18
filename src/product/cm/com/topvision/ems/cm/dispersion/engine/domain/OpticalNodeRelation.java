/***********************************************************************
 * $Id: OpticalNodeRelation.java,v1.0 2015-3-26 下午1:52:02 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.dispersion.engine.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author fanzidong
 * @created @2015-3-26-下午1:52:02
 * 
 */
public class OpticalNodeRelation implements AliasesSuperType {

    private static final long serialVersionUID = 1309611453064124997L;

    private Long opticalNodeId;
    private Long cmtsId;
    private Long channelIndex;

    public Long getOpticalNodeId() {
        return opticalNodeId;
    }

    public void setOpticalNodeId(Long opticalNodeId) {
        this.opticalNodeId = opticalNodeId;
    }

    public Long getCmtsId() {
        return cmtsId;
    }

    public void setCmtsId(Long cmtsId) {
        this.cmtsId = cmtsId;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

}
