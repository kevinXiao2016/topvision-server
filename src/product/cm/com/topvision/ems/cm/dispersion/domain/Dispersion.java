/***********************************************************************
 * $Id: Dispersion.java,v1.0 2015-3-12 下午2:08:00 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.dispersion.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author fanzidong
 * @created @2015-3-12-下午2:08:00
 * 
 */
public class Dispersion implements AliasesSuperType {
    private static final long serialVersionUID = -2429347159027070358L;
    public static final String UP_SNR = "upSnr";
    public static final String UP_POWER = "upPower";

    private Long opticalNodeId;
    private String opticalNodeName;
    private Timestamp collectTime;
    private Integer cmNum;
    private Double upSnrAvg;
    private Double upSnrStd;
    private String upSnrDist;
    private Double upPowerAvg;
    private Double upPowerStd;
    private String upPowerDist;

    private String cmtsName;
    private String typeName;
    private String manageIp;
    private String channelIds;
    private String folderNames;

    public Long getOpticalNodeId() {
        return opticalNodeId;
    }

    public void setOpticalNodeId(Long opticalNodeId) {
        this.opticalNodeId = opticalNodeId;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public Integer getCmNum() {
        return cmNum;
    }

    public void setCmNum(Integer cmNum) {
        this.cmNum = cmNum;
    }

    public Double getUpSnrAvg() {
        return upSnrAvg;
    }

    public void setUpSnrAvg(Double upSnrAvg) {
        this.upSnrAvg = upSnrAvg;
    }

    public Double getUpSnrStd() {
        return upSnrStd;
    }

    public void setUpSnrStd(Double upSnrStd) {
        this.upSnrStd = upSnrStd;
    }

    public String getUpSnrDist() {
        return upSnrDist;
    }

    public void setUpSnrDist(String upSnrDist) {
        this.upSnrDist = upSnrDist;
    }

    public Double getUpPowerAvg() {
        return upPowerAvg;
    }

    public void setUpPowerAvg(Double upPowerAvg) {
        this.upPowerAvg = upPowerAvg;
    }

    public Double getUpPowerStd() {
        return upPowerStd;
    }

    public void setUpPowerStd(Double upPowerStd) {
        this.upPowerStd = upPowerStd;
    }

    public String getUpPowerDist() {
        return upPowerDist;
    }

    public void setUpPowerDist(String upPowerDist) {
        this.upPowerDist = upPowerDist;
    }

    public String getOpticalNodeName() {
        return opticalNodeName;
    }

    public void setOpticalNodeName(String opticalNodeName) {
        this.opticalNodeName = opticalNodeName;
    }

    public String getCmtsName() {
        return cmtsName;
    }

    public void setCmtsName(String cmtsName) {
        this.cmtsName = cmtsName;
    }

    public String getManageIp() {
        return manageIp;
    }

    public void setManageIp(String manageIp) {
        this.manageIp = manageIp;
    }

    public String getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(String channelIds) {
        this.channelIds = channelIds;
    }

    public String getFolderNames() {
        return folderNames;
    }

    public void setFolderNames(String folderNames) {
        this.folderNames = folderNames;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
