/***********************************************************************
 * $Id: CmcChannelCmNum.java,v1.0 2014-7-18 上午10:51:39 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-7-18-上午10:51:39
 *
 */
public class ChannelCmNumInfo implements AliasesSuperType {
    private static final long serialVersionUID = 5107719269261904003L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long channelIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.1", type = "Integer32")
    private Integer totalNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.2", type = "Integer32")
    private Integer onlineNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.5", type = "Integer32")
    private Integer offlieNum;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }

    public Integer getOfflieNum() {
        return offlieNum;
    }

    public void setOfflieNum(Integer offlieNum) {
        this.offlieNum = offlieNum;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcChannelCmNum [entityId=");
        builder.append(entityId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", totalNum=");
        builder.append(totalNum);
        builder.append(", onlineNum=");
        builder.append(onlineNum);
        builder.append(", offlieNum=");
        builder.append(offlieNum);
        builder.append("]");
        return builder.toString();
    }
    
}
