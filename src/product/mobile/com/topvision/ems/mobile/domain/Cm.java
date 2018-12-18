package com.topvision.ems.mobile.domain;

import com.topvision.framework.utils.CmcIndexUtils;

public class Cm {
    private Integer id; // CM_ID
    private Integer state; // CM的在线状态
    private Double upSnr; // 上行SNR
    private Double upRx; // 上行接收电平
    private Double upFre; // 上行频率
    private Double downFre; // 下行频率
    private Double downSnr; // 下行SNR
    private Double upTx; // 上行发送电平
    private Double downRx; // 下行接收电平
    private Double downTx; // 下行发送电平

    private String upAtten;// 上行衰减 = upTx - upRx
    private String downAtten;// 下行衰减 = downTx - downRx
    private Long upChanId; // 上行主信道ID
    private Long downChanId; // 下行主信道ID

    // 用于展示的字段
    private String upChannelSnr;
    private String upRecvPower;
    private String downChannelSnr;
    private String upTransPower;
    private String downRecvPower;
    private String downTransPower;

    private String cmMac;
    private String cmImgUrl;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUpChanId() {
        return upChanId;
    }

    public Double getUpSnr() {
        return upSnr;
    }

    public void setUpSnr(Double upSnr) {
        this.upSnr = upSnr;
    }

    public Double getUpRx() {
        return upRx;
    }

    public void setUpRx(Double upRx) {
        this.upRx = upRx;
    }

    public Double getUpFre() {
        return upFre;
    }

    public void setUpFre(Double upFre) {
        this.upFre = upFre;
    }

    public Double getDownFre() {
        return downFre;
    }

    public void setDownFre(Double downFre) {
        this.downFre = downFre;
    }

    public Double getDownSnr() {
        return downSnr;
    }

    public void setDownSnr(Double downSnr) {
        this.downSnr = downSnr;
    }

    public Double getUpTx() {
        return upTx;
    }

    public void setUpTx(Double upTx) {
        this.upTx = upTx;
    }

    public Double getDownRx() {
        return downRx;
    }

    public void setDownRx(Double downRx) {
        this.downRx = downRx;
    }

    public Double getDownTx() {
        return downTx;
    }

    public void setDownTx(Double downTx) {
        this.downTx = downTx;
    }

    public String getUpAtten() {
        return upAtten;
    }

    public void setUpAtten(String upAtten) {
        this.upAtten = upAtten;
    }

    public String getDownAtten() {
        return downAtten;
    }

    public void setDownAtten(String downAtten) {
        this.downAtten = downAtten;
    }

    public void setUpChanId(Long upChanId) {
        if (upChanId != null) {
            upChanId = CmcIndexUtils.getChannelId(upChanId);
        }
        this.upChanId = upChanId;
    }

    public Long getDownChanId() {
        return downChanId;
    }

    public void setDownChanId(Long downChanId) {
        if (downChanId != null) {
            downChanId = CmcIndexUtils.getChannelId(downChanId);
        }
        this.downChanId = downChanId;
    }

    public String getUpChannelSnr() {
        return upChannelSnr;
    }

    public void setUpChannelSnr(String upChannelSnr) {
        this.upChannelSnr = upChannelSnr;
    }

    public String getUpRecvPower() {
        return upRecvPower;
    }

    public void setUpRecvPower(String upRecvPower) {
        this.upRecvPower = upRecvPower;
    }

    public String getDownChannelSnr() {
        return downChannelSnr;
    }

    public void setDownChannelSnr(String downChannelSnr) {
        this.downChannelSnr = downChannelSnr;
    }

    public String getUpTransPower() {
        return upTransPower;
    }

    public void setUpTransPower(String upTransPower) {
        this.upTransPower = upTransPower;
    }

    public String getDownRecvPower() {
        return downRecvPower;
    }

    public void setDownRecvPower(String downRecvPower) {
        this.downRecvPower = downRecvPower;
    }

    public String getDownTransPower() {
        return downTransPower;
    }

    public void setDownTransPower(String downTransPower) {
        this.downTransPower = downTransPower;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public String getCmImgUrl() {
        return cmImgUrl;
    }

    public void setCmImgUrl(String cmImgUrl) {
        this.cmImgUrl = cmImgUrl;
    }

}
