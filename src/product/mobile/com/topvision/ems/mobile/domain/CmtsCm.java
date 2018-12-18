package com.topvision.ems.mobile.domain;

import com.topvision.framework.utils.CmcIndexUtils;

/**
 * CMTS下联的CM的Domain
 * 
 * @author YangYi
 * @created @2014年6月23日-上午11:04:41
 * 
 */
public class CmtsCm {
    private Long cmId; // CM_ID
    private Long cmtsId;
    private Long cmIndex; // CM_INDEX
    private String ip; // CM的IP
    private String mac; // CM的MAC
    private String alias; // CM的别名
    private String classified; // CM的位置
    private Integer state; // CM的在线状态
    private String upSnr;// CM的上行SNR
    private String upPower;// CM的上行发送电平
    private String downSnr; // CM的下行SNR
    private String downPower; // CM的下行接收电平
    private Integer docsisMode;// DocsisMode;
    private Integer realtimeState;// 实时状态
    private Long downChanId;// 下行信道ID
    private Long upChanId; // 上行信道ID

    public Long getDownChanId() {
        return downChanId;
    }

    public Long getUpChanId() {
        return upChanId;
    }

    public void setDownChanId(Long downChanId) {
        if (downChanId != null) {
            downChanId = CmcIndexUtils.getChannelId(downChanId);
        }
        this.downChanId = downChanId;
    }

    public void setUpChanId(Long upChanId) {
        if (upChanId != null) {
            upChanId = CmcIndexUtils.getChannelId(upChanId);
        }
        this.upChanId = upChanId;
    }

    public Integer getDocsisMode() {
        return docsisMode;
    }

    public Integer getRealtimeState() {
        return realtimeState;
    }

    public void setDocsisMode(Integer docsisMode) {
        this.docsisMode = docsisMode;
    }

    public void setRealtimeState(Integer realtimeState) {
        this.realtimeState = realtimeState;
    }

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getClassified() {
        return classified;
    }

    public void setClassified(String classified) {
        this.classified = classified;
    }

    public String getUpSnr() {
        return upSnr;
    }

    public void setUpSnr(String upSnr) {
        this.upSnr = upSnr;
    }

    public String getUpPower() {
        return upPower;
    }

    public void setUpPower(String upPower) {
        this.upPower = upPower;
    }

    public String getDownSnr() {
        return downSnr;
    }

    public void setDownSnr(String downSnr) {
        this.downSnr = downSnr;
    }

    public String getDownPower() {
        return downPower;
    }

    public void setDownPower(String downPower) {
        this.downPower = downPower;
    }

    public Long getCmtsId() {
        return cmtsId;
    }

    public void setCmtsId(Long cmtsId) {
        this.cmtsId = cmtsId;
    }

}
