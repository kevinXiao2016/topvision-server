package com.topvision.ems.mobile.domain;

public class CmtsUpChannelWithPortId {
    private Integer chanId; // 信道编号
    private Long cmcPortId;
    private Long freq; // 中心频率
    private Long width;// 频宽
    private Integer model;// 调试方式
    private Integer snr; // 上行信道信噪比
    private Integer ifAdminStatus;// up ( 1 ) , down ( 2 ) , testing ( 3 )--ifTable
    private Integer ifOperStatus;// up ( 1 ) , down ( 2 ) , testing ( 3 ) ,
    private Integer docsIf3SignalPower;// 上行接收电平
    private Long channelModulationProfile;
    private Double ccerRate;
    private Double ucerRate;
    private Double noerRate;
    private Long collectInterval;
    private String errorRateString;

    //数据展示
    private String channelId;
    private String channelFreq;
    private String channelWidth;
    private String channelMode;
    private String channelSnr;
    private String recvPower;

    public Integer getChanId() {
        return chanId;
    }

    public void setChanId(Integer chanId) {
        this.chanId = chanId;
    }

    public Long getFreq() {
        return freq;
    }

    public void setFreq(Long freq) {
        this.freq = freq;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public Integer getSnr() {
        return snr;
    }

    public void setSnr(Integer snr) {
        this.snr = snr;
    }

    public Integer getIfAdminStatus() {
        return ifAdminStatus;
    }

    public void setIfAdminStatus(Integer ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }

    public Integer getIfOperStatus() {
        return ifOperStatus;
    }

    public void setIfOperStatus(Integer ifOperStatus) {
        this.ifOperStatus = ifOperStatus;
    }

    public Integer getDocsIf3SignalPower() {
        return docsIf3SignalPower;
    }

    public void setDocsIf3SignalPower(Integer docsIf3SignalPower) {
        this.docsIf3SignalPower = docsIf3SignalPower;
    }

    public Long getChannelModulationProfile() {
        return channelModulationProfile;
    }

    public void setChannelModulationProfile(Long channelModulationProfile) {
        this.channelModulationProfile = channelModulationProfile;
    }

    public Double getCcerRate() {
        return ccerRate;
    }

    public void setCcerRate(Double ccerRate) {
        this.ccerRate = ccerRate;
    }

    public Double getUcerRate() {
        return ucerRate;
    }

    public void setUcerRate(Double ucerRate) {
        this.ucerRate = ucerRate;
    }

    public Double getNoerRate() {
        return noerRate;
    }

    public void setNoerRate(Double noerRate) {
        this.noerRate = noerRate;
    }

    public Long getCollectInterval() {
        return collectInterval;
    }

    public void setCollectInterval(Long collectInterval) {
        this.collectInterval = collectInterval;
    }

    public String getErrorRateString() {
        return errorRateString;
    }

    public void setErrorRateString(String errorRateString) {
        this.errorRateString = errorRateString;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelFreq() {
        return channelFreq;
    }

    public void setChannelFreq(String channelFreq) {
        this.channelFreq = channelFreq;
    }

    public String getChannelWidth() {
        return channelWidth;
    }

    public void setChannelWidth(String channelWidth) {
        this.channelWidth = channelWidth;
    }

    public String getChannelMode() {
        return channelMode;
    }

    public void setChannelMode(String channelMode) {
        this.channelMode = channelMode;
    }

    public String getChannelSnr() {
        return channelSnr;
    }

    public void setChannelSnr(String channelSnr) {
        this.channelSnr = channelSnr;
    }

    public String getRecvPower() {
        return recvPower;
    }

    public void setRecvPower(String recvPower) {
        this.recvPower = recvPower;
    }

    public Long getCmcPortId() {
        return cmcPortId;
    }

    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }
}
