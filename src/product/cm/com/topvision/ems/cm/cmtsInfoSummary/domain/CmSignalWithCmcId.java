package com.topvision.ems.cm.cmtsInfoSummary.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class CmSignalWithCmcId implements AliasesSuperType  {
    private static final long serialVersionUID = 4196638433984816886L;
    
    private Long cmId;
    private Long cmcId;
    private Timestamp collectTime;
    private Double upChannelTx;// 上行电平
    private Double upChannelSnr; // 下行SNR
    private Double downChannelSnr; // 下行SNR
    private Double downChannelTx;// 下行电平
    private Integer stateValue;
    public Long getCmId() {
        return cmId;
    }
    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public Timestamp getCollectTime() {
        return collectTime;
    }
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }
    public Double getUpChannelTx() {
        return upChannelTx;
    }
    public void setUpChannelTx(Double upChannelTx) {
        this.upChannelTx = upChannelTx;
    }
    public Double getUpChannelSnr() {
        return upChannelSnr;
    }
    public void setUpChannelSnr(Double upChannelSnr) {
        this.upChannelSnr = upChannelSnr;
    }
    public Double getDownChannelSnr() {
        return downChannelSnr;
    }
    public void setDownChannelSnr(Double downChannelSnr) {
        this.downChannelSnr = downChannelSnr;
    }
    public Double getDownChannelTx() {
        return downChannelTx;
    }
    public void setDownChannelTx(Double downChannelTx) {
        this.downChannelTx = downChannelTx;
    }
    public Integer getStateValue() {
        return stateValue;
    }
    public void setStateValue(Integer stateValue) {
        this.stateValue = stateValue;
    }

}
