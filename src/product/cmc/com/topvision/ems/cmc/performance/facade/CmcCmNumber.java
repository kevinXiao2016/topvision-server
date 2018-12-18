/***********************************************************************
 * $Id: CmcCmStatus.java,v1.0 2013-8-12 下午01:42:42 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.facade;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2013-8-12-下午01:42:42
 * 
 */
@Alias("cmcCmNumber")
public class CmcCmNumber implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 2749038178540212568L;
    private Long cmcId;
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long channelIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.1")
    private Integer cmNumTotal;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.2")
    private Integer cmNumOnline;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.5")
    private Integer cmNumOffline;
    private Timestamp collectTime;

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
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex
     *            the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
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

    /**
     * @return the cmNumTotal
     */
    public Integer getCmNumTotal() {
        return cmNumTotal;
    }

    /**
     * @param cmNumTotal
     *            the cmNumTotal to set
     */
    public void setCmNumTotal(Integer cmNumTotal) {
        this.cmNumTotal = cmNumTotal;
    }

    /**
     * @return the cmNumOnline
     */
    public Integer getCmNumOnline() {
        return cmNumOnline;
    }

    /**
     * @param cmNumOnline
     *            the cmNumOnline to set
     */
    public void setCmNumOnline(Integer cmNumOnline) {
        this.cmNumOnline = cmNumOnline;
    }

    /**
     * @return the cmNumOffline
     */
    public Integer getCmNumOffline() {
        return cmNumOffline;
    }

    /**
     * @param cmNumOffline
     *            the cmNumOffline to set
     */
    public void setCmNumOffline(Integer cmNumOffline) {
        this.cmNumOffline = cmNumOffline;
    }

    /**
     * @return the collectTime
     */
    public Timestamp getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

}
