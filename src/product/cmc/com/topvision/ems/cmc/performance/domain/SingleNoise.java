/***********************************************************************
 * $ SingleNoise.java,v1.0 2012-5-6 14:56:47 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2012-5-6-14:56:47
 */
@Alias("singleNoise")
public class SingleNoise implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private long entityId;
    private long parentId;
    private long cmcId;
    private String cmcName;
    private String name;
    private String cmcPortName;
    private Long ifIndex;
    private Long channelIndex;
    private Integer noise;
    private Integer collectValue;
    private String macAddress;
    private String mac;
    private String cmcType;
    private Timestamp dt;
    private Timestamp collectTime;
    private String recentUpdateTime; // 最近更新时间
    private long x;
    private float y;
    private Long cmcPortId;
    private String channelTypeString;
    private String noiseString;
    private String manageIp;
    private String ip;
    private String ifDescr;
    private Long typeId;
    private String typeName;
    private Long ifType;
    private String uplinkDevice;
    private String ifName;
    private boolean spectrum;// added by wubo 2017.01.23
    public String location;
    public String note;

    public String getChannelTypeString() {
        Long channelType = CmcIndexUtils.getChannelType(ifIndex.longValue());
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

    public long getCmcId() {
        return cmcId;
    }

    public void setCmcId(long cmcId) {
        this.cmcId = cmcId;
    }

    public Timestamp getDt() {
        return dt;
    }

    public void setDt(Timestamp dt) {
        this.dt = dt;
        this.collectTime = dt;
        x = dt.getTime();
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
        this.channelIndex = ifIndex;
        Long channelType = CmcIndexUtils.getChannelType(ifIndex.longValue());
        String chanelTypeString = "";
        if (channelType == 0) {
            chanelTypeString = "US";
        } else if (channelType == 1) {
            chanelTypeString = "DS";
        }
        Long chennelId = CmcIndexUtils.getChannelId(ifIndex.longValue());
        String Slot_Pon_cmcId = CmcIndexUtils.getSlotNo(ifIndex.longValue()) + Symbol.SLASH
                + CmcIndexUtils.getPonNo(ifIndex.longValue()) + Symbol.SLASH
                + CmcIndexUtils.getCmcId(ifIndex.longValue());
        cmcPortName = chanelTypeString + " " + Slot_Pon_cmcId + Symbol.SLASH + chennelId.toString();
    }

    public int getNoise() {
        return noise;
    }

    public void setNoise(int noise) {
        this.noise = noise;
        this.collectValue = noise;
        y = ((float) noise) / 10;
    }

    /**
     * @return the x
     */
    public long getX() {
        return x;
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(long x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(float y) {
        this.y = y;
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
        this.name = cmcName;
    }

    /**
     * @return the cmcPortName
     */
    public String getCmcPortName() {
        return cmcPortName;
    }

    /**
     * @param cmcPortName
     *            the cmcPortName to set
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
        this.mac = macAddress;
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

    /**
     * @return the noiseString
     */
    public String getNoiseString() {
        String str = " dB";
        Float l = 0f;
        if (this.getNoise() > -1) {
            l = (float) this.getNoise() / 10;
        } else {
            l = 0f;
        }
        noiseString = l.toString();
        return noiseString + str;
    }

    /**
     * @param noiseString
     *            the noiseString to set
     */
    public void setNoiseString(String noiseString) {
        this.noiseString = noiseString;
    }

    public String getDtStr() {
        return DateUtils.format(this.dt);
    }

    @Deprecated
    public String getCmcTypeStr() {
        return String.valueOf(this.cmcType);
    }

    public String getManageIp() {
        return manageIp;
    }

    public void setManageIp(String manageIp) {
        this.manageIp = manageIp;
        this.ip = manageIp;
    }

    public String getIfDescr() {
        return ifDescr;
    }

    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setNoise(Integer noise) {
        this.noise = noise;
        this.collectValue = noise;
    }

    public Long getIfType() {
        return ifType;
    }

    public void setIfType(Long ifType) {
        this.ifType = ifType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCollectValue() {
        return collectValue;
    }

    public void setCollectValue(Integer collectValue) {
        this.collectValue = collectValue;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getIfName() {
        return ifName;
    }

    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

    public boolean isSpectrum() {
        return spectrum;
    }

    public void setSpectrum(boolean spectrum) {
        this.spectrum = spectrum;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRecentUpdateTime() {
        return recentUpdateTime;
    }

    public void setRecentUpdateTime(String recentUpdateTime) {
        this.recentUpdateTime = recentUpdateTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SingleNoise [entityId=");
        builder.append(entityId);
        builder.append(", parentId=");
        builder.append(parentId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmcName=");
        builder.append(cmcName);
        builder.append(", name=");
        builder.append(name);
        builder.append(", cmcPortName=");
        builder.append(cmcPortName);
        builder.append(", ifIndex=");
        builder.append(ifIndex);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", noise=");
        builder.append(noise);
        builder.append(", collectValue=");
        builder.append(collectValue);
        builder.append(", macAddress=");
        builder.append(macAddress);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", cmcType=");
        builder.append(cmcType);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append(", recentUpdateTime=");
        builder.append(recentUpdateTime);
        builder.append(", x=");
        builder.append(x);
        builder.append(", y=");
        builder.append(y);
        builder.append(", cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", channelTypeString=");
        builder.append(channelTypeString);
        builder.append(", noiseString=");
        builder.append(noiseString);
        builder.append(", manageIp=");
        builder.append(manageIp);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", ifDescr=");
        builder.append(ifDescr);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", typeName=");
        builder.append(typeName);
        builder.append(", ifType=");
        builder.append(ifType);
        builder.append(", uplinkDevice=");
        builder.append(uplinkDevice);
        builder.append(", ifName=");
        builder.append(ifName);
        builder.append(", spectrum=");
        builder.append(spectrum);
        builder.append(", location=");
        builder.append(location);
        builder.append(", note=");
        builder.append(note);
        builder.append("]");
        return builder.toString();
    }

}