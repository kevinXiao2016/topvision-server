/***********************************************************************
 * $Id: CmcBerQuality.java,v1.0 2017年7月4日 下午1:30:02 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author vanzand
 * @created @2017年7月4日-下午1:30:02
 *
 */
@Alias("cmcBerQuality")
public class CmcBerQuality implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -5665761988704311196L;
    private Long cmcId;
    private Long channelIndex;
    private Long ccerCode;
    private Long ucerCode;
    private Float ccerRate;
    private Float ucerRate;
    private Long noerCode;
    private Float noerRate;
    private Timestamp collectTime;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public Long getCcerCode() {
        return ccerCode;
    }

    public void setCcerCode(Long ccerCode) {
        this.ccerCode = ccerCode;
    }

    public Long getUcerCode() {
        return ucerCode;
    }

    public void setUcerCode(Long ucerCode) {
        this.ucerCode = ucerCode;
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

    public Long getNoerCode() {
        return noerCode;
    }

    public void setNoerCode(Long noerCode) {
        this.noerCode = noerCode;
    }

    public Float getNoerRate() {
        return noerRate;
    }

    public void setNoerRate(Float noerRate) {
        this.noerRate = noerRate;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    @Override
    public String toString() {
        return "CmcBerQuality [cmcId=" + cmcId + ", channelIndex=" + channelIndex + ", ccerCode=" + ccerCode
                + ", ucerCode=" + ucerCode + ", ccerRate=" + ccerRate + ", ucerRate=" + ucerRate + ", noerCode="
                + noerCode + ", noerRate=" + noerRate + ", collectTime=" + collectTime + "]";
    }

}
