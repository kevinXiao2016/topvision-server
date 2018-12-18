package com.topvision.ems.mobile.domain;

public class CmtsUpchannelConfig {
    private Long cmcId;
    private Long cmcPortId;
    private Long channelFrequency;
    private Long channelWidth;
    private Integer ifAdminStatus;
    private Long module;
    
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public Long getCmcPortId() {
        return cmcPortId;
    }
    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }
    public Long getChannelFrequency() {
        return channelFrequency;
    }
    public void setChannelFrequency(Long channelFrequency) {
        this.channelFrequency = channelFrequency;
    }
    public Long getChannelWidth() {
        return channelWidth;
    }
    public void setChannelWidth(Long channelWidth) {
        this.channelWidth = channelWidth;
    }
    public Integer getIfAdminStatus() {
        return ifAdminStatus;
    }
    public void setIfAdminStatus(Integer ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }
    public Long getModule() {
        return module;
    }
    public void setModule(Long module) {
        this.module = module;
    }
}
