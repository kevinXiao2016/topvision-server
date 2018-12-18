/***********************************************************************
 * $Id: CmtsNetworkInfo.java,v1.0 2017年8月2日 下午2:06:42 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cmtsInfo.domain;

import java.io.Serializable;

import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author ls
 * @created @2017年8月2日-下午2:06:42
 *
 */
public class CmtsNetworkInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 6890240877966659232L;
    
    private Long cmcId;
    private String nmName;
    private String manageIp;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.5", type = "OctetString", writable = true)
    private String topCcmtsSysName;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;// 是表的ifIndex，Mac域
    private String location;
    @SnmpProperty(table = "cmNum", oid = "1.3.6.1.4.1.32285.11.1.1.2.2.1.1.1")
    private Long topCcmtsCmNumTotal;
    @SnmpProperty(table = "cmNum", oid = "1.3.6.1.4.1.32285.11.1.1.2.2.1.1.3")
    private Long topCcmtsCmNumOnline;
    private Double onlineRatio;
    private Integer countTotal;
    private String upSpeed;
    private String downSpeed;
    private String cmOutPowerAvg;
    private Double cmOutPowerNotInRange;
    private String cmRePowerAvg;
    private Double cmRePowerNotInRange;
    private String upSnrAvg;
    private Double upSnrNotInRange;
    private String downSnrAvg;
    private Double downSnrAvgNotInRange;
    private String note;
    private Long entityId;
    private String cmcDeviceStyle;
    private String manageName;
    private Integer onlineNum;
    private Integer allNum;
    
    public Long getEntityId() {
        return entityId;
    }
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    public String getCmcDeviceStyle() {
        return cmcDeviceStyle;
    }
    public void setCmcDeviceStyle(String cmcDeviceStyle) {
        this.cmcDeviceStyle = cmcDeviceStyle;
    }
    public String getManageName() {
        return manageName;
    }
    public void setManageName(String manageName) {
        this.manageName = manageName;
    }
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public String getNmName() {
        return nmName;
    }
    public void setNmName(String nmName) {
        this.nmName = nmName;
    }
    public String getManageIp() {
        return manageIp;
    }
    public void setManageIp(String manageIp) {
        this.manageIp = manageIp;
    }
    public String getTopCcmtsSysName() {
        return topCcmtsSysName;
    }
    public void setTopCcmtsSysName(String topCcmtsSysName) {
        this.topCcmtsSysName = topCcmtsSysName;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public Long getTopCcmtsCmNumTotal() {
        return topCcmtsCmNumTotal;
    }
    public void setTopCcmtsCmNumTotal(Long topCcmtsCmNumTotal) {
        this.topCcmtsCmNumTotal = topCcmtsCmNumTotal;
    }
    public Long getTopCcmtsCmNumOnline() {
        return topCcmtsCmNumOnline;
    }
    public void setTopCcmtsCmNumOnline(Long topCcmtsCmNumOnline) {
        this.topCcmtsCmNumOnline = topCcmtsCmNumOnline;
    }
    public String getUpSpeed() {
        return upSpeed;
    }
    public void setUpSpeed(String upSpeed) {
        this.upSpeed = upSpeed;
    }
    public String getDownSpeed() {
        return downSpeed;
    }
    public void setDownSpeed(String downSpeed) {
        this.downSpeed = downSpeed;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public Double getOnlineRatio() {
        return onlineRatio;
    }
    public void setOnlineRatio(Double onlineRatio) {
        this.onlineRatio = onlineRatio;
    }
    public String getCmOutPowerAvg() {
        return cmOutPowerAvg;
    }
    public void setCmOutPowerAvg(String cmOutPowerAvg) {
        this.cmOutPowerAvg = cmOutPowerAvg;
    }
    public Double getCmOutPowerNotInRange() {
        return cmOutPowerNotInRange;
    }
    public void setCmOutPowerNotInRange(Double cmOutPowerNotInRange) {
        this.cmOutPowerNotInRange = cmOutPowerNotInRange;
    }
    public String getCmRePowerAvg() {
        return cmRePowerAvg;
    }
    public void setCmRePowerAvg(String cmRePowerAvg) {
        this.cmRePowerAvg = cmRePowerAvg;
    }
    public Double getCmRePowerNotInRange() {
        return cmRePowerNotInRange;
    }
    public void setCmRePowerNotInRange(Double cmRePowerNotInRange) {
        this.cmRePowerNotInRange = cmRePowerNotInRange;
    }
    public String getUpSnrAvg() {
        return upSnrAvg;
    }
    public void setUpSnrAvg(String upSnrAvg) {
        this.upSnrAvg = upSnrAvg;
    }
    public Double getUpSnrNotInRange() {
        return upSnrNotInRange;
    }
    public void setUpSnrNotInRange(Double upSnrNotInRange) {
        this.upSnrNotInRange = upSnrNotInRange;
    }
    public String getDownSnrAvg() {
        return downSnrAvg;
    }
    public void setDownSnrAvg(String downSnrAvg) {
        this.downSnrAvg = downSnrAvg;
    }
    public Double getDownSnrAvgNotInRange() {
        return downSnrAvgNotInRange;
    }
    public void setDownSnrAvgNotInRange(Double downSnrAvgNotInRange) {
        this.downSnrAvgNotInRange = downSnrAvgNotInRange;
    }
    public Integer getCountTotal() {
        return countTotal;
    }
    public void setCountTotal(Integer countTotal) {
        this.countTotal = countTotal;
    }
    public Integer getOnlineNum() {
        return onlineNum;
    }
    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }
    public Integer getAllNum() {
        return allNum;
    }
    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }


}
