package com.topvision.ems.cmc.report.domain;

/**
 * CCMTS设备信道使用情况报表，CCMTS层级，包含CCMTS的具体信息
 * 
 * @author YangYi add
 * @created @2013-9-11
 * 
 */
public class CcmtsChannelReportCCMTS {
    private String cmcId; // CCMTS的Id
    private String ccmtsName;// CCMTS名称
    private String ccmtsMAC;// CCMTS的MAC地址
    private String ccmtsType;// CCMTS的类型
    private String ccmtsUpChannelNum;// CCMTS开启上行信道的数量
    private String ccmtsDownChannelNum;// CCMTS开启下行信道的数量
    private String ccmtsDownChannelModule;// CCMTS开启下行信道的调制方式
    private String ccmtsUpChannelModule;// CCMTS开启上行信道的调制方式

    public String getCcmtsDownChannelNum() {
        return ccmtsDownChannelNum;
    }

    public void setCcmtsDownChannelNum(String ccmtsDownChannelNum) {
        this.ccmtsDownChannelNum = ccmtsDownChannelNum;
    }

    public String getCcmtsUpChannelNum() {
        return ccmtsUpChannelNum;
    }

    public void setCcmtsUpChannelNum(String ccmtsUpChannelNum) {
        this.ccmtsUpChannelNum = ccmtsUpChannelNum;
    }

    public String getCmcId() {
        return cmcId;
    }

    public void setCmcId(String cmcId) {
        this.cmcId = cmcId;
    }

    public String getCcmtsName() {
        return ccmtsName;
    }

    public void setCcmtsName(String ccmtsName) {
        this.ccmtsName = ccmtsName;
    }

    public String getCcmtsMAC() {
        return ccmtsMAC;
    }

    public void setCcmtsMAC(String ccmtsMAC) {
        this.ccmtsMAC = ccmtsMAC;
    }

    public String getCcmtsType() {
        return ccmtsType;
    }

    public void setCcmtsType(String ccmtsType) {
        this.ccmtsType = ccmtsType;
    }

    public String getCcmtsDownChannelModule() {
        return ccmtsDownChannelModule;
    }

    public void setCcmtsDownChannelModule(String ccmtsDownChannelModule) {
        this.ccmtsDownChannelModule = ccmtsDownChannelModule;
    }

    public String getCcmtsUpChannelModule() {
        return ccmtsUpChannelModule;
    }

    public void setCcmtsUpChannelModule(String ccmtsUpChannelModule) {
        this.ccmtsUpChannelModule = ccmtsUpChannelModule;
    }

}
