/***********************************************************************
 * $Id: UsBitErrorRate.java,v1.0 2012-7-12 下午04:00:37 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.facade.nbi.NbiSnmpProperty;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2012-7-12-下午04:00:37
 * 
 */
@Alias("usBitErrorRate")
public class UsBitErrorRate implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7429492382814595671L;
    private Long entityId;
    private Long parentId;
    private String cmcName;
    private String topCcmtsSysName;
    private String cmcPortName;
    private Long cmcId;
    private Timestamp dt;
    private String recentUpdateTime;// 距离上次更新时间
    private Long channelIndex;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.7.1.1")
    private Integer bitErrorRate;// 可纠错码率
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.7.1.2")
    private Integer unBitErrorRate;// 不可纠错码率
    private String macAddress;
    private String topCcmtsSysMacAddr;
    private String cmcType;
    private Long cmcPortId;
    private String channelTypeString;
    private Float ccerRate;
    private Float ucerRate;
    private Long ifType;
    private String ifDescr;
    private String ip;
    private Timestamp collectTime;
    private String displayName;
    private String uplinkDevice;

    public String getChannelTypeString() {
        Long channelType = CmcIndexUtils.getChannelType(channelIndex);
        if (channelType == 0) {
            channelTypeString = "US";
        } else if (channelType == 1) {
            channelTypeString = "DS";
        }
        return channelTypeString;
    }

    public void setChannelTypeString(String channelTypeString) {
        this.channelTypeString = channelTypeString;
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
        Long channelType = CmcIndexUtils.getChannelType(channelIndex);
        String chanelTypeString = "";
        if (channelType == 0) {
            chanelTypeString = "US";
        } else if (channelType == 1) {
            chanelTypeString = "DS";
        }
        Long chennelId = CmcIndexUtils.getChannelId(channelIndex);
        String Slot_Pon_cmcId = CmcIndexUtils.getSlotNo(channelIndex.longValue()) + Symbol.SLASH
                + CmcIndexUtils.getPonNo(channelIndex.longValue()) + Symbol.SLASH
                + CmcIndexUtils.getCmcId(channelIndex.longValue());
        cmcPortName = chanelTypeString + " " + Slot_Pon_cmcId + Symbol.SLASH + chennelId.toString();
    }

    /**
     * @return the bitErrorRate
     */
    public Integer getBitErrorRate() {
        return bitErrorRate;
    }

    /**
     * @param bitErrorRate
     *            the bitErrorRate to set
     */
    public void setBitErrorRate(Integer bitErrorRate) {
        if (bitErrorRate != null) {
            this.bitErrorRate = bitErrorRate;
        } else {
            this.bitErrorRate = 0;
        }

    }

    /**
     * @return the unBitErrorRate
     */
    public Integer getUnBitErrorRate() {
        return unBitErrorRate;
    }

    /**
     * @param unBitErrorRate
     *            the unBitErrorRate to set
     */
    public void setUnBitErrorRate(Integer unBitErrorRate) {
        if (unBitErrorRate != null) {
            this.unBitErrorRate = unBitErrorRate;
        } else {
            this.unBitErrorRate = 0;
        }
    }

    /**
     * @return the dt
     */
    public Timestamp getDt() {
        return dt;
    }

    /**
     * @param dt
     *            the dt to set
     */
    public void setDt(Timestamp dt) {
        this.dt = dt;
    }

    public String getRecentUpdateTime() {
        return recentUpdateTime;
    }

    public void setRecentUpdateTime(String recentUpdateTime) {
        this.recentUpdateTime = recentUpdateTime;
    }

    /**
     * @return the cmcName
     */
    public String getCmcName() {
        return cmcName;
    }

    /**
     * @param cmcName
     *            the cmcName to set
     */
    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
        this.topCcmtsSysName = cmcName;
    }

    /**
     * @return the cmcPort
     */
    public String getCmcPortName() {
        return cmcPortName;
    }

    /**
     * @param cmcPort
     *            the cmcPort to set
     */
    public void setCmcPortName(String cmcPortName) {
        this.cmcPortName = cmcPortName;
    }

    /**
     * @return the macAddress
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * @param macAddress
     *            the macAddress to set
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
        this.topCcmtsSysMacAddr = macAddress;
    }

    /**
     * @return the cmcType
     */
    public String getCmcType() {
        return cmcType;
    }

    /**
     * @param cmcType
     *            the cmcType to set
     */
    public void setCmcType(String cmcType) {
        this.cmcType = cmcType;
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

    public Float getCcerRate() {
        return ccerRate;
    }

    public void setCcerRate(Float ccerRate) {
        this.ccerRate = ccerRate;
    }

    public Float getUcerRate() {
        return ucerRate;
    }

    public void setUcerRate(Float ucerRate) {
        this.ucerRate = ucerRate;
    }

    public Long getIfType() {
        return ifType;
    }

    public void setIfType(Long ifType) {
        this.ifType = ifType;
    }

    public String getIfDescr() {
        return ifDescr;
    }

    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }

    public String getTopCcmtsSysName() {
        return topCcmtsSysName;
    }

    public void setTopCcmtsSysName(String topCcmtsSysName) {
        this.topCcmtsSysName = topCcmtsSysName;
    }

    public String getTopCcmtsSysMacAddr() {
        return topCcmtsSysMacAddr;
    }

    public void setTopCcmtsSysMacAddr(String topCcmtsSysMacAddr) {
        this.topCcmtsSysMacAddr = topCcmtsSysMacAddr;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public String getDtStr() {
        return DateUtils.format(this.collectTime);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UsBitErrorRate [entityId=");
        builder.append(entityId);
        builder.append(", parentId=");
        builder.append(parentId);
        builder.append(", cmcName=");
        builder.append(cmcName);
        builder.append(", topCcmtsSysName=");
        builder.append(topCcmtsSysName);
        builder.append(", cmcPortName=");
        builder.append(cmcPortName);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(", recentUpdateTime=");
        builder.append(recentUpdateTime);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", bitErrorRate=");
        builder.append(bitErrorRate);
        builder.append(", unBitErrorRate=");
        builder.append(unBitErrorRate);
        builder.append(", macAddress=");
        builder.append(macAddress);
        builder.append(", topCcmtsSysMacAddr=");
        builder.append(topCcmtsSysMacAddr);
        builder.append(", cmcType=");
        builder.append(cmcType);
        builder.append(", cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", channelTypeString=");
        builder.append(channelTypeString);
        builder.append(", ccerRate=");
        builder.append(ccerRate);
        builder.append(", ucerRate=");
        builder.append(ucerRate);
        builder.append(", ifType=");
        builder.append(ifType);
        builder.append(", ifDescr=");
        builder.append(ifDescr);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append(", displayName=");
        builder.append(displayName);
        builder.append(", uplinkDevice=");
        builder.append(uplinkDevice);
        builder.append("]");
        return builder.toString();
    }

}
