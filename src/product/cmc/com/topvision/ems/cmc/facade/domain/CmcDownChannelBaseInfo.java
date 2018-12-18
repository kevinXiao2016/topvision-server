/***********************************************************************
 * $Id: CmcDownChannelBaseInfo.java,v1.0 2011-10-26 下午02:42:14 $
 * 
 * @author: xionghao
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
 * @author xionghao
 * @created @2011-10-26-下午02:42:14
 * 
 */
@Alias("cmcDownChannelBaseInfo")
public class CmcDownChannelBaseInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -2377769592188570249L;
    private Long cmcId;
    private Long cmcPortId;
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long channelIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.1")
    private Integer docsIfDownChannelId; // 下行通道ID
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.2", writable = true, type = "Integer32")
    private Long docsIfDownChannelFrequency; // 下行通道频率
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.3", writable = true, type = "Integer32")
    private Integer docsIfDownChannelWidth; // 下行通道带宽
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.4", writable = true, type = "Integer32")
    private Integer docsIfDownChannelModulation; // 下行通道调制信息
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.5", writable = true, type = "Integer32")
    private Integer docsIfDownChannelInterleave; // 下行通道交织
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.6", writable = true, type = "Integer32")
    private Long docsIfDownChannelPower; // 下行通道能力
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.7")
    private Integer docsIfDownChannelAnnex; // 下行通道标准(eo,usa)
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.8")
    private Integer docsIfDownChannelStorageType; // 下行通道存储类型

    /**
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
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
     * @return the docsIfDownChannelId
     */
    public Integer getDocsIfDownChannelId() {
        return docsIfDownChannelId;
    }

    /**
     * @param docsIfDownChannelId
     *            the docsIfDownChannelId to set
     */
    public void setDocsIfDownChannelId(Integer docsIfDownChannelId) {
        this.docsIfDownChannelId = docsIfDownChannelId;
    }

    /**
     * @return the docsIfDownChannelFrequency
     */
    public Long getDocsIfDownChannelFrequency() {
        return docsIfDownChannelFrequency;
    }

    /**
     * @param docsIfDownChannelFrequency
     *            the docsIfDownChannelFrequency to set
     */
    public void setDocsIfDownChannelFrequency(Long docsIfDownChannelFrequency) {
        this.docsIfDownChannelFrequency = docsIfDownChannelFrequency;
    }

    /**
     * @return the docsIfDownChannelWidth
     */
    public Integer getDocsIfDownChannelWidth() {
        return docsIfDownChannelWidth;
    }

    /**
     * @param docsIfDownChannelWidth
     *            the docsIfDownChannelWidth to set
     */
    public void setDocsIfDownChannelWidth(Integer docsIfDownChannelWidth) {
        this.docsIfDownChannelWidth = docsIfDownChannelWidth;
    }

    /**
     * @return the docsIfDownChannelModulation
     */
    public Integer getDocsIfDownChannelModulation() {
        return docsIfDownChannelModulation;
    }

    /**
     * @param docsIfDownChannelModulation
     *            the docsIfDownChannelModulation to set
     */
    public void setDocsIfDownChannelModulation(Integer docsIfDownChannelModulation) {
        this.docsIfDownChannelModulation = docsIfDownChannelModulation;
    }

    /**
     * @return the docsIfDownChannelInterleave
     */
    public Integer getDocsIfDownChannelInterleave() {
        return docsIfDownChannelInterleave;
    }

    /**
     * @param docsIfDownChannelInterleave
     *            the docsIfDownChannelInterleave to set
     */
    public void setDocsIfDownChannelInterleave(Integer docsIfDownChannelInterleave) {
        this.docsIfDownChannelInterleave = docsIfDownChannelInterleave;
    }

    /**
     * @return the docsIfDownChannelPower
     */
    public Long getDocsIfDownChannelPower() {
        return docsIfDownChannelPower;
    }

    /**
     * @param docsIfDownChannelPower
     *            the docsIfDownChannelPower to set
     */
    public void setDocsIfDownChannelPower(Long docsIfDownChannelPower) {
        this.docsIfDownChannelPower = docsIfDownChannelPower;
    }

    /**
     * @return the docsIfDownChannelAnnex
     */
    public Integer getDocsIfDownChannelAnnex() {
        return docsIfDownChannelAnnex;
    }

    /**
     * @param docsIfDownChannelAnnex
     *            the docsIfDownChannelAnnex to set
     */
    public void setDocsIfDownChannelAnnex(Integer docsIfDownChannelAnnex) {
        this.docsIfDownChannelAnnex = docsIfDownChannelAnnex;
    }

    /**
     * @return the docsIfDownChannelStorageType
     */
    public Integer getDocsIfDownChannelStorageType() {
        return docsIfDownChannelStorageType;
    }

    /**
     * @param docsIfDownChannelStorageType
     *            the docsIfDownChannelStorageType to set
     */
    public void setDocsIfDownChannelStorageType(Integer docsIfDownChannelStorageType) {
        this.docsIfDownChannelStorageType = docsIfDownChannelStorageType;
    }

    /**
     * @return the cmcPortId
     */
    public Long getCmcPortId() {
        return cmcPortId;
    }

    /**
     * @param cmcPortId
     *            the cmcPortId to set
     */
    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
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
        builder.append("CmcDownChannelBaseInfo [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", docsIfDownChannelId=");
        builder.append(docsIfDownChannelId);
        builder.append(", docsIfDownChannelFrequency=");
        builder.append(docsIfDownChannelFrequency);
        builder.append(", docsIfDownChannelWidth=");
        builder.append(docsIfDownChannelWidth);
        builder.append(", docsIfDownChannelModulation=");
        builder.append(docsIfDownChannelModulation);
        builder.append(", docsIfDownChannelInterleave=");
        builder.append(docsIfDownChannelInterleave);
        builder.append(", docsIfDownChannelPower=");
        builder.append(docsIfDownChannelPower);
        builder.append(", docsIfDownChannelAnnex=");
        builder.append(docsIfDownChannelAnnex);
        builder.append(", docsIfDownChannelStorageType=");
        builder.append(docsIfDownChannelStorageType);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
