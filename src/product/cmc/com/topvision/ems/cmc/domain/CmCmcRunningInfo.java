/***********************************************************************
 * $Id: CmcRunningInfo.java,v1.0 2013-7-17 上午11:21:22 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2013-7-17-上午11:21:22
 *
 */
@Alias("cmCmcRunningInfo")
public class CmCmcRunningInfo implements AliasesSuperType{
    private static final long serialVersionUID = 5688076454155531223L;
    private Long cmId;
    private Long cmcId;
    private Integer cmcTypeId;
    private String cmcTypeName;
    private Long cmcIndex;
    private Long channelIndex;
    private Long snr;
    private Integer bitErrorRate;// 可纠错码率
    private Integer unBitErrorRate;// 不可纠错码率
    private Long cmOnlineNum;
    private Long cmOfflineNum;
    private Float channelInOctetsRate;
    private String interfaceString;
    
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public Long getCmcIndex() {
        return cmcIndex;
    }
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }
    public Long getChannelIndex() {
        return channelIndex;
    }
    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }
    public Long getSnr() {
        return snr;
    }
    public void setSnr(Long snr) {
        this.snr = snr;
    }
    public Integer getBitErrorRate() {
        return bitErrorRate;
    }
    public void setBitErrorRate(Integer bitErrorRate) {
        this.bitErrorRate = bitErrorRate;
    }
    public Integer getUnBitErrorRate() {
        return unBitErrorRate;
    }
    public void setUnBitErrorRate(Integer unBitErrorRate) {
        this.unBitErrorRate = unBitErrorRate;
    }
    public Long getCmOnlineNum() {
        return cmOnlineNum;
    }
    public void setCmOnlineNum(Long cmOnlineNum) {
        this.cmOnlineNum = cmOnlineNum;
    }
    public Long getCmOfflineNum() {
        return cmOfflineNum;
    }
    public void setCmOfflineNum(Long cmOfflineNum) {
        this.cmOfflineNum = cmOfflineNum;
    }
    public Float getChannelInOctetsRate() {
        return channelInOctetsRate;
    }
    public void setChannelInOctetsRate(Float channelInOctetsRate) {
        this.channelInOctetsRate = channelInOctetsRate;
    }
    public Long getCmId() {
        return cmId;
    }
    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }
    public Integer getCmcTypeId() {
        return cmcTypeId;
    }
    public void setCmcTypeId(Integer cmcTypeId) {
        this.cmcTypeId = cmcTypeId;
    }
    public String getCmcTypeName() {
        return cmcTypeName;
    }
    public void setCmcTypeName(String cmcTypeName) {
        this.cmcTypeName = cmcTypeName;
    }
    public String getInterfaceString() {
        return interfaceString;
    }
    public void setInterfaceString(String interfaceString) {
        this.interfaceString = interfaceString;
    }
}
