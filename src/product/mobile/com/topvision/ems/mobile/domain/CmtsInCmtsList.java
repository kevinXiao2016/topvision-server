/***********************************************************************
 * $Id: CmtsInCmList.java,v1.0 2015年6月29日 下午2:23:53 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.domain;

/**
 * @author YangYi
 * @created @2015年6月29日-下午2:23:53
 * 
 */
public class CmtsInCmtsList {
    private Long cmtsId;
    private String name;
    private String mac;
    private String ip;
    private Integer state;
    private Boolean isSupportOptical;// 是否支持光机
    private String topCcmtsSysDorType;
    private Long typeId;
    private Boolean isCcWithoutAgent;
    private Boolean isCcWithAgent;
    private Boolean isFOptical;

    public Boolean getIsSupportOptical() {
        return isSupportOptical;
    }

    public void setIsSupportOptical(Boolean isSupportOptical) {
        this.isSupportOptical = isSupportOptical;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getCmtsId() {
        return cmtsId;
    }

    public void setCmtsId(Long cmtsId) {
        this.cmtsId = cmtsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTopCcmtsSysDorType() {
        return topCcmtsSysDorType;
    }

    public void setTopCcmtsSysDorType(String topCcmtsSysDorType) {
        this.topCcmtsSysDorType = topCcmtsSysDorType;
    }

    public Boolean getIsCcWithoutAgent() {
        return isCcWithoutAgent;
    }

    public void setIsCcWithoutAgent(Boolean isCcWithoutAgent) {
        this.isCcWithoutAgent = isCcWithoutAgent;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Boolean getIsCcWithAgent() {
        return isCcWithAgent;
    }

    public void setIsCcWithAgent(Boolean isCcWithAgent) {
        this.isCcWithAgent = isCcWithAgent;
    }

    public Boolean getIsFOptical() {
        return isFOptical;
    }

    public void setIsFOptical(Boolean isFOptical) {
        this.isFOptical = isFOptical;
    }
}
