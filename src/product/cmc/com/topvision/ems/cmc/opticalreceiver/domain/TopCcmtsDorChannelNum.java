/***********************************************************************
 * $Id: TopCcmtsDorChannelNum.java,v1.0 2016年9月13日 下午1:18:16 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2016年9月13日-下午1:18:16
 *
 */
public class TopCcmtsDorChannelNum implements AliasesSuperType {
    private static final long serialVersionUID = -3470136342826180735L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.20.1.1", index = true)
    private Long channelNumIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.20.1.2")
    private Integer channelNum;// 正向频道数量

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
     * @return the channelNumIndex
     */
    public Long getChannelNumIndex() {
        return channelNumIndex;
    }

    /**
     * @param channelNumIndex
     *            the channelNumIndex to set
     */
    public void setChannelNumIndex(Long channelNumIndex) {
        this.channelNumIndex = channelNumIndex;
    }

    /**
     * @return the channelNum
     */
    public Integer getChannelNum() {
        return channelNum;
    }

    /**
     * @param channelNum
     *            the channelNum to set
     */
    public void setChannelNum(Integer channelNum) {
        this.channelNum = channelNum;
    }

}
