/***********************************************************************
 * $Id: CmcUpChannelBaseInfo.java,v1.0 2011-10-26 下午02:17:42 $
 * 
 * @author: xionghao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.upchannel.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author xionghao
 * @created @2011-10-26-下午02:17:42
 * 
 */
@Alias("cmcUpChannelBaseInfo")
public class CmcUpChannelBaseInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -7857051059795322925L;
    private Long cmcId;
    private Long cmcPortId;
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long channelIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.1")
    private Integer channelId; // 上行频道Id
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.2", writable = true, type = "Integer32")
    private Long channelFrequency; // 上行频道频率
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.3", writable = true, type = "Integer32")
    private Long channelWidth; // 上行频道带宽
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.4", writable = true, type = "Gauge32")
    private Long channelModulationProfile; // 上行调制配置文件
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.5", writable = true, type = "Integer32")
    private Long channelSlotSize; // 上行minislot大小
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.6")
    private Long channelTxTimingOffset; // 上行Tx偏移时间
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.7", writable = true, type = "Integer32")
    private Integer channelRangingBackoffStart; // 上行测距BackoffStart
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.8", writable = true, type = "Integer32")
    private Integer channelRangingBackoffEnd; // 上行测距BackoffEnd
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.9", writable = true, type = "Integer32")
    private Integer channelTxBackoffStart;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.10", writable = true, type = "Integer32")
    private Integer channelTxBackoffEnd;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.11", writable = true, type = "Integer32")
    private Integer channelScdmaActiveCodes; // ActiveCodes数目
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.12", writable = true, type = "Integer32")
    private Integer channelScdmaCodesPerSlot; // 每个minislotcode数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.13", writable = true, type = "Integer32")
    private Integer channelScdmaFrameSize; // 上行S-CDMA帧size
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.14", writable = true, type = "Integer32")
    private Integer channelScdmaHoppingSeed; // 上行hop种子
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.15")
    private Integer channelType; // 上行频道类型
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.16", writable = true, type = "Integer32")
    private Long channelCloneFrom; // 上行频道克隆
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.17", writable = true, type = "Integer32")
    private Integer channelUpdate; // 上行频道更新
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.18", writable = true, type = "Integer32")
    private Integer channelStatus; // 上行频道状态
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.19", writable = true, type = "Integer32")
    private Integer channelPreEqEnable; // 上行预均衡开关
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.8.1.1", writable = true, type = "Integer32")
    private Integer channelExtMode;// 上行信道模式 v2,v3,other
    private Long cmtsChannelModulationProfile;// 数据库使用，CMTS的上行信道配置模板

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
     * @return the docsIfUpChannelId
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * @param channelId
     *            the docsIfUpChannelId to set
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * @return the docsIfUpChannelFrequency
     */
    public Long getChannelFrequency() {
        return channelFrequency;
    }

    /**
     * @param channelFrequency
     *            the docsIfUpChannelFrequency to set
     */
    public void setChannelFrequency(Long channelFrequency) {
        this.channelFrequency = channelFrequency;
    }

    /**
     * @return the docsIfUpChannelWidth
     */
    public Long getChannelWidth() {
        return channelWidth;
    }

    /**
     * @param channelWidth
     *            the docsIfUpChannelWidth to set
     */
    public void setChannelWidth(Long channelWidth) {
        this.channelWidth = channelWidth;
    }

    /**
     * @return the docsIfUpChannelModulationProfile
     */
    public Long getChannelModulationProfile() {
        return channelModulationProfile;
    }

    /**
     * @param channelModulationProfile
     *            the docsIfUpChannelModulationProfile to set
     */
    public void setChannelModulationProfile(Long channelModulationProfile) {
        this.channelModulationProfile = channelModulationProfile;
    }

    /**
     * @return the docsIfUpChannelSlotSize
     */
    public Long getChannelSlotSize() {
        return channelSlotSize;
    }

    /**
     * @param channelSlotSize
     *            the docsIfUpChannelSlotSize to set
     */
    public void setChannelSlotSize(Long channelSlotSize) {
        this.channelSlotSize = channelSlotSize;
    }

    /**
     * @return the docsIfUpChannelTxTimingOffset
     */
    public Long getChannelTxTimingOffset() {
        return channelTxTimingOffset;
    }

    /**
     * @param channelTxTimingOffset
     *            the docsIfUpChannelTxTimingOffset to set
     */
    public void setChannelTxTimingOffset(Long channelTxTimingOffset) {
        this.channelTxTimingOffset = channelTxTimingOffset;
    }

    /**
     * @return the docsIfUpChannelRangingBackoffStart
     */
    public Integer getChannelRangingBackoffStart() {
        return channelRangingBackoffStart;
    }

    /**
     * @param channelRangingBackoffStart
     *            the docsIfUpChannelRangingBackoffStart to set
     */
    public void setChannelRangingBackoffStart(Integer channelRangingBackoffStart) {
        this.channelRangingBackoffStart = channelRangingBackoffStart;
    }

    /**
     * @return the docsIfUpChannelRangingBackoffEnd
     */
    public Integer getChannelRangingBackoffEnd() {
        return channelRangingBackoffEnd;
    }

    /**
     * @param channelRangingBackoffEnd
     *            the docsIfUpChannelRangingBackoffEnd to set
     */
    public void setChannelRangingBackoffEnd(Integer channelRangingBackoffEnd) {
        this.channelRangingBackoffEnd = channelRangingBackoffEnd;
    }

    /**
     * @return the docsIfUpChannelTxBackoffStart
     */
    public Integer getChannelTxBackoffStart() {
        return channelTxBackoffStart;
    }

    /**
     * @param channelTxBackoffStart
     *            the docsIfUpChannelTxBackoffStart to set
     */
    public void setChannelTxBackoffStart(Integer channelTxBackoffStart) {
        this.channelTxBackoffStart = channelTxBackoffStart;
    }

    /**
     * @return the docsIfUpChannelTxBackoffEnd
     */
    public Integer getChannelTxBackoffEnd() {
        return channelTxBackoffEnd;
    }

    /**
     * @param channelTxBackoffEnd
     *            the docsIfUpChannelTxBackoffEnd to set
     */
    public void setChannelTxBackoffEnd(Integer channelTxBackoffEnd) {
        this.channelTxBackoffEnd = channelTxBackoffEnd;
    }

    /**
     * @return the docsIfUpChannelScdmaActiveCodes
     */
    public Integer getChannelScdmaActiveCodes() {
        return channelScdmaActiveCodes;
    }

    /**
     * @param channelScdmaActiveCodes
     *            the docsIfUpChannelScdmaActiveCodes to set
     */
    public void setChannelScdmaActiveCodes(Integer channelScdmaActiveCodes) {
        this.channelScdmaActiveCodes = channelScdmaActiveCodes;
    }

    /**
     * @return the docsIfUpChannelScdmaCodesPerSlot
     */
    public Integer getChannelScdmaCodesPerSlot() {
        return channelScdmaCodesPerSlot;
    }

    /**
     * @param channelScdmaCodesPerSlot
     *            the docsIfUpChannelScdmaCodesPerSlot to set
     */
    public void setChannelScdmaCodesPerSlot(Integer channelScdmaCodesPerSlot) {
        this.channelScdmaCodesPerSlot = channelScdmaCodesPerSlot;
    }

    /**
     * @return the docsIfUpChannelScdmaFrameSize
     */
    public Integer getChannelScdmaFrameSize() {
        return channelScdmaFrameSize;
    }

    /**
     * @param channelScdmaFrameSize
     *            the docsIfUpChannelScdmaFrameSize to set
     */
    public void setChannelScdmaFrameSize(Integer channelScdmaFrameSize) {
        this.channelScdmaFrameSize = channelScdmaFrameSize;
    }

    /**
     * @return the docsIfUpChannelScdmaHoppingSeed
     */
    public Integer getChannelScdmaHoppingSeed() {
        return channelScdmaHoppingSeed;
    }

    /**
     * @param channelScdmaHoppingSeed
     *            the docsIfUpChannelScdmaHoppingSeed to set
     */
    public void setChannelScdmaHoppingSeed(Integer channelScdmaHoppingSeed) {
        this.channelScdmaHoppingSeed = channelScdmaHoppingSeed;
    }

    /**
     * @return the docsIfUpChannelType
     */
    public Integer getChannelType() {
        return channelType;
    }

    /**
     * @param channelType
     *            the docsIfUpChannelType to set
     */
    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    /**
     * @return the docsIfUpChannelCloneFrom
     */
    public Long getChannelCloneFrom() {
        return channelCloneFrom;
    }

    /**
     * @param channelCloneFrom
     *            the docsIfUpChannelCloneFrom to set
     */
    public void setChannelCloneFrom(Long channelCloneFrom) {
        this.channelCloneFrom = channelCloneFrom;
    }

    /**
     * @return the docsIfUpChannelUpdate
     */
    public Integer getChannelUpdate() {
        return channelUpdate;
    }

    /**
     * @param channelUpdate
     *            the docsIfUpChannelUpdate to set
     */
    public void setChannelUpdate(Integer channelUpdate) {
        this.channelUpdate = channelUpdate;
    }

    /**
     * @return the docsIfUpChannelStatus
     */
    public Integer getChannelStatus() {
        return channelStatus;
    }

    /**
     * @param channelStatus
     *            the docsIfUpChannelStatus to set
     */
    public void setChannelStatus(Integer channelStatus) {
        this.channelStatus = channelStatus;
    }

    /**
     * @return the docsIfUpChannelPreEqEnable
     */
    public Integer getChannelPreEqEnable() {
        return channelPreEqEnable;
    }

    /**
     * @param channelPreEqEnable
     *            the docsIfUpChannelPreEqEnable to set
     */
    public void setChannelPreEqEnable(Integer channelPreEqEnable) {
        this.channelPreEqEnable = channelPreEqEnable;
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

    /**
     * @return the channelExtMode
     */
    public Integer getChannelExtMode() {
        return channelExtMode;
    }

    /**
     * @param channelExtMode
     *            the channelExtMode to set
     */
    public void setChannelExtMode(Integer channelExtMode) {
        if (channelExtMode == null || channelExtMode > 127) {
            this.channelExtMode = null;
        } else {
            this.channelExtMode = channelExtMode;
        }
    }

    public Long getCmtsChannelModulationProfile() {
        return cmtsChannelModulationProfile;
    }

    public void setCmtsChannelModulationProfile(Long cmtsChannelModulationProfile) {
        this.cmtsChannelModulationProfile = cmtsChannelModulationProfile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcUpChannelBaseInfo [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", channelId=");
        builder.append(channelId);
        builder.append(", channelFrequency=");
        builder.append(channelFrequency);
        builder.append(", channelWidth=");
        builder.append(channelWidth);
        builder.append(", channelModulationProfile=");
        builder.append(channelModulationProfile);
        builder.append(", channelSlotSize=");
        builder.append(channelSlotSize);
        builder.append(", channelTxTimingOffset=");
        builder.append(channelTxTimingOffset);
        builder.append(", channelRangingBackoffStart=");
        builder.append(channelRangingBackoffStart);
        builder.append(", channelRangingBackoffEnd=");
        builder.append(channelRangingBackoffEnd);
        builder.append(", channelTxBackoffStart=");
        builder.append(channelTxBackoffStart);
        builder.append(", channelTxBackoffEnd=");
        builder.append(channelTxBackoffEnd);
        builder.append(", channelScdmaActiveCodes=");
        builder.append(channelScdmaActiveCodes);
        builder.append(", channelScdmaCodesPerSlot=");
        builder.append(channelScdmaCodesPerSlot);
        builder.append(", channelScdmaFrameSize=");
        builder.append(channelScdmaFrameSize);
        builder.append(", channelScdmaHoppingSeed=");
        builder.append(channelScdmaHoppingSeed);
        builder.append(", channelType=");
        builder.append(channelType);
        builder.append(", channelCloneFrom=");
        builder.append(channelCloneFrom);
        builder.append(", channelUpdate=");
        builder.append(channelUpdate);
        builder.append(", channelStatus=");
        builder.append(channelStatus);
        builder.append(", channelPreEqEnable=");
        builder.append(channelPreEqEnable);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
