/***********************************************************************
 * $Id: ChannelPerfInfo.java,v1.0 2012-7-16 上午09:24:40 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.io.Serializable;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.framework.common.NumberUtils;

/**
 * @author haojie
 * @created @2012-7-16-上午09:24:40
 * 
 */
public class ChannelPerfInfo implements Serializable {
    private static final long serialVersionUID = 596597466412321366L;
    private Long entityId;
    private Long cmcPortId;
    private Long cmcId;
    private Integer channelId;
    private Long channelIndex;
    private int channelType;
    private String channelTypeString;
    private float channelInOctetsRate;
    private String channelInOctetsRateString;
    private float channelOutOctetsRate;
    private String channelOutOctetsRateString;
    private double channelOctetsRate;
    private String channelOctetsRateStr;
    private float channelUtilization;
    private String channelUtilizationString;
    private Integer channelStatus;
    private Long ifSpeed;
    private String ifSpeedString = "-";
    private Integer ifOperStatus;
    private String ifDescr;
    private String cmcPortName;

    public Long getIfSpeed() {
        return ifSpeed;
    }

    public void setIfSpeed(Long ifSpeed) {
        this.ifSpeed = ifSpeed;
    }

    public String getIfSpeedString() {
        if (getIfSpeed() != null && getIfSpeed() != 0) {
            ifSpeedString = CmcUtil.turnDevObjToEndWithKOrM(getIfSpeed());
        }
        return ifSpeedString;
    }

    public void setIfSpeedString(String ifSpeedString) {
        this.ifSpeedString = ifSpeedString;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
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
        channelId = CmcIndexUtils.getChannelId(channelIndex).intValue();
        setChannelType(CmcIndexUtils.getChannelType(channelIndex).intValue());
    }

    /**
     * @return the channelType
     */
    public int getChannelType() {
        return channelType;
    }

    /**
     * @param channelType
     *            the channelType to set
     */
    public void setChannelType(int channelType) {
        this.channelType = channelType;
        if (channelType == 0) {
            channelTypeString = "US";
        } else if (channelType == 1) {
            channelTypeString = "DS";
        }
    }

    /**
     * @return the channelInOctetsRate
     */
    public float getChannelInOctetsRate() {
        return channelInOctetsRate;
    }

    /**
     * @param channelInOctetsRate
     *            the channelInOctetsRate to set
     */
    public void setChannelInOctetsRate(float channelInOctetsRate) {
        this.channelInOctetsRate = channelInOctetsRate;
        if (CmcIndexUtils.getChannelType(this.channelIndex) == 0) {
            channelOctetsRateStr = NumberUtils.getIfSpeedStr(channelInOctetsRate);
        }
        channelInOctetsRateString = CmcUtil.getIfOctetRateString(channelInOctetsRate);

    }

    /**
     * @return the channelOutOctetsRate
     */
    public float getChannelOutOctetsRate() {
        return channelOutOctetsRate;
    }

    /**
     * @param channelOutOctetsRate
     *            the channelOutOctetsRate to set
     */
    public void setChannelOutOctetsRate(float channelOutOctetsRate) {
        this.channelOutOctetsRate = channelOutOctetsRate;
        if (CmcIndexUtils.getChannelType(this.channelIndex) == 1) {
            channelOctetsRateStr = NumberUtils.getIfSpeedStr(channelOutOctetsRate);
        }
        channelOutOctetsRateString = CmcUtil.getIfOctetRateString(channelOutOctetsRate);
    }

    /**
     * @return the channelOctetsRate
     */
    public double getChannelOctetsRate() {
        return channelOctetsRate;
    }

    /**
     * @param channelOctetsRate
     *            the channelOctetsRate to set
     */
    public void setChannelOctetsRate(double channelOctetsRate) {
        channelOctetsRateStr = NumberUtils.getIfSpeedStr(channelOctetsRate);
        this.channelOctetsRate = channelOctetsRate;
    }

    /**
     * @return the channelUtilization
     */
    public float getChannelUtilization() {
        return channelUtilization;
    }

    /**
     * @param channelUtilization
     *            the channelUtilization to set
     */
    public void setChannelUtilization(float channelUtilization) {
        this.channelUtilization = channelUtilization;
        if (channelUtilization > 0) {
            channelUtilizationString = channelUtilization + Symbol.PERCENT;
        } else {
            channelUtilizationString = 0 + Symbol.PERCENT;
        }
    }

    /**
     * @return the channelStatus
     */
    public Integer getChannelStatus() {
        return channelStatus;
    }

    /**
     * @param channelStatus
     *            the channelStatus to set
     */
    public void setChannelStatus(Integer channelStatus) {
        this.channelStatus = channelStatus;
    }

    /**
     * @return the channelId
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * @param channelId
     *            the channelId to set
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * @return the channelTypeString
     */
    public String getChannelTypeString() {
        return channelTypeString;
    }

    /**
     * @param channelTypeString
     *            the channelTypeString to set
     */
    public void setChannelTypeString(String channelTypeString) {
        this.channelTypeString = channelTypeString;
    }

    /**
     * @return the channelUtilizationString
     */
    public String getChannelUtilizationString() {
        return channelUtilizationString;
    }

    /**
     * @param channelUtilizationString
     *            the channelUtilizationString to set
     */
    public void setChannelUtilizationString(String channelUtilizationString) {
        this.channelUtilizationString = channelUtilizationString;
    }

    /**
     * @return the channelInOctetsRateString
     */
    public String getChannelInOctetsRateString() {
        return channelInOctetsRateString;
    }

    /**
     * @param channelInOctetsRateString
     *            the channelInOctetsRateString to set
     */
    public void setChannelInOctetsRateString(String channelInOctetsRateString) {
        this.channelInOctetsRateString = channelInOctetsRateString;
    }

    /**
     * @return the channelOutOctetsRateString
     */
    public String getChannelOutOctetsRateString() {
        return channelOutOctetsRateString;
    }

    /**
     * @param channelOutOctetsRateString
     *            the channelOutOctetsRateString to set
     */
    public void setChannelOutOctetsRateString(String channelOutOctetsRateString) {
        this.channelOutOctetsRateString = channelOutOctetsRateString;
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
     * @return the ifOperStatus
     */
    public Integer getIfOperStatus() {
        return ifOperStatus;
    }

    /**
     * @param ifOperStatus
     *            the ifOperStatus to set
     */
    public void setIfOperStatus(Integer ifOperStatus) {
        this.ifOperStatus = ifOperStatus;
    }

    /**
     * @return the ifDescr
     */
    public String getIfDescr() {
        return ifDescr;
    }

    /**
     * @param ifDescr
     *            the ifDescr to set
     */
    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }

    public String getCmcPortName() {
        Long index = getChannelIndex();
        String Slot_Pon_cmcId = "";
        if (index != null) {
            Slot_Pon_cmcId = CmcIndexUtils.getSlotNo(index) + Symbol.SLASH + CmcIndexUtils.getPonNo(index)
                    + Symbol.SLASH + CmcIndexUtils.getCmcId(index);
        }
        cmcPortName = channelTypeString + " " + Slot_Pon_cmcId + Symbol.SLASH + getChannelId();
        return cmcPortName;
    }

    public void setCmcPortName(String cmcPortName) {
        this.cmcPortName = cmcPortName;
    }

    public String getChannelOctetsRateStr() {
        return channelOctetsRateStr;
    }

    public void setChannelOctetsRateStr(String channelOctetsRateStr) {
        this.channelOctetsRateStr = channelOctetsRateStr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ChannelPerfInfo [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", channelId=");
        builder.append(channelId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", channelType=");
        builder.append(channelType);
        builder.append(", channelInOctetsRate=");
        builder.append(channelInOctetsRate);
        builder.append(", channelOutOctetsRate=");
        builder.append(channelOutOctetsRate);
        builder.append(", channelOctetsRate=");
        builder.append(channelOctetsRate);
        builder.append(", channelUtilization=");
        builder.append(channelUtilization);
        builder.append(", channelStatus=");
        builder.append(channelStatus);
        builder.append(", ifDescr=");
        builder.append(ifDescr);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
