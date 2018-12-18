/***********************************************************************
 * $Id: CmcChannelCmNum.java,v1.0 2012-5-30 下午05:57:47 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author dosion
 * @created @2012-5-30-下午05:57:47
 * 
 */
@Alias("cmcChannelCmNum")
public class CmcChannelCmNum implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = 7571528153789951514L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Integer channelIndex;
    /* @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.3.1.1") */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.1")
    private Integer topCcmtsChannelCmNumTotal;
    /* @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.3.1.2") */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.2")
    private Integer topCcmtsChannelCmNumOnline;

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
    public Integer getChannelIndex() {
        return channelIndex;
    }

    /**
     * @param channelIndex
     *            the channelIndex to set
     */
    public void setChannelIndex(Integer channelIndex) {
        this.channelIndex = channelIndex;
    }

    /**
     * @return the topCcmtsChannelCmNumTotal
     */
    public Integer getTopCcmtsChannelCmNumTotal() {
        return topCcmtsChannelCmNumTotal;
    }

    /**
     * @param topCcmtsChannelCmNumTotal
     *            the topCcmtsChannelCmNumTotal to set
     */
    public void setTopCcmtsChannelCmNumTotal(Integer topCcmtsChannelCmNumTotal) {
        this.topCcmtsChannelCmNumTotal = topCcmtsChannelCmNumTotal;
    }

    /**
     * @return the topCcmtsChannelCmNumOnline
     */
    public Integer getTopCcmtsChannelCmNumOnline() {
        return topCcmtsChannelCmNumOnline;
    }

    /**
     * @param topCcmtsChannelCmNumOnline
     *            the topCcmtsChannelCmNumOnline to set
     */
    public void setTopCcmtsChannelCmNumOnline(Integer topCcmtsChannelCmNumOnline) {
        this.topCcmtsChannelCmNumOnline = topCcmtsChannelCmNumOnline;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcChannelCmNum [cmcId=");
        builder.append(cmcId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", topCcmtsChannelCmNumTotal=");
        builder.append(topCcmtsChannelCmNumTotal);
        builder.append(", topCcmtsChannelCmNumOnline=");
        builder.append(topCcmtsChannelCmNumOnline);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
