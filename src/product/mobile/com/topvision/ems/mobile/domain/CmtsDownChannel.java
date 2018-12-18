package com.topvision.ems.mobile.domain;

public class CmtsDownChannel {
    private Long cmtsId;
    private Integer chanId; // 信道编号
    private Long freq; // 中心频率
    private Long width;// 频宽
    private Integer model;// 调试方式
    private Integer annex; // 欧美标
    private Integer ifAdminStatus;// up ( 1 ) , down ( 2 ) , testing ( 3 )--ifTable
    private Integer ifOperStatus;// up ( 1 ) , down ( 2 ) , testing ( 3 ) ,
    private Long docsIfDownChannelPower; // 下行通道电平dBmV
    private Boolean eqam = false;

    //数据展示
    private String channelId;
    private String channelFreq;
    private String channelWidth;
    private String channelMode;
    private String channelAnnex;
    private String transPower;

    public Long getCmtsId() {
        return cmtsId;
    }

    public void setCmtsId(Long cmtsId) {
        this.cmtsId = cmtsId;
    }

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

    public Integer getAnnex() {
        return annex;
    }

    public void setAnnex(Integer annex) {
        this.annex = annex;
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

    public Long getDocsIfDownChannelPower() {
        return docsIfDownChannelPower;
    }

    public void setDocsIfDownChannelPower(Long docsIfDownChannelPower) {
        this.docsIfDownChannelPower = docsIfDownChannelPower;
    }

    public Boolean getEqam() {
        return eqam;
    }

    public void setEqam(Boolean eqam) {
        this.eqam = eqam;
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

    public String getChannelAnnex() {
        return channelAnnex;
    }

    public void setChannelAnnex(String channelAnnex) {
        this.channelAnnex = channelAnnex;
    }

    public String getTransPower() {
        return transPower;
    }

    public void setTransPower(String transPower) {
        this.transPower = transPower;
    }

}
