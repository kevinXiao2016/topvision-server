/***********************************************************************
 * $ CmNum.java,v1.0 2013-6-21 16:48:14 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2013-6-21-16:48:14
 */
@Alias("cmNum")
public class CmNum implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    public static Long UPCHANNEL = 0l;
    public static Long DOWNCHANNEL = 1l;
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Long areaId;
    private String areaName;
    private Long entityId;
    private String entityName;
    private String entityIp;
    private Long cmcId;
    private Long ccIfIndex;
    private Long ponIndex;
    private String ccName;
    private Long portIfIndex;
    private String portName;
    private Long portType;
    private Double cpuRate = -1d;
    private Integer allNum = 0;
    private Integer onlineNum = 0;
    private Integer otherNum = 0;
    private Integer offlineNum = 0;
    private Integer interactiveNum = 0;
    private Integer broadbandNum = 0;
    private Integer mtaNum = 0;
    private Integer integratedNum = 0;
    private Integer cpeNum = 0;
    private Integer cpeInteractiveNum = 0;
    private Integer cpeBroadbandNum = 0;
    private Integer cpeMtaNum = 0;
    private Long timeLong;
    private String timeString;
    private Long realTimeLong;
    private String realTimeString;

    public Integer getBroadbandNum() {
        return broadbandNum;
    }

    public void setBroadbandNum(Integer broadbandNum) {
        this.broadbandNum = broadbandNum;
    }

    public Integer getCpeNum() {
        return cpeNum;
    }

    public void setCpeNum(Integer cpeNum) {
        this.cpeNum = cpeNum;
    }

    public Integer getInteractiveNum() {
        return interactiveNum;
    }

    public void setInteractiveNum(Integer interactiveNum) {
        this.interactiveNum = interactiveNum;
    }

    public Integer getOfflineNum() {
        return offlineNum;
    }

    public void setOfflineNum(Integer offlineNum) {
        this.offlineNum = offlineNum;
    }

    public Integer getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }

    public Integer getOtherNum() {
        return otherNum;
    }

    public void setOtherNum(Integer otherNum) {
        this.otherNum = otherNum;
    }

    public Integer getMtaNum() {
        return mtaNum;
    }

    public void setMtaNum(Integer mtaNum) {
        this.mtaNum = mtaNum;
    }

    public Integer getIntegratedNum() {
        return integratedNum;
    }

    public void setIntegratedNum(Integer integratedNum) {
        this.integratedNum = integratedNum;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getCcIfIndex() {
        return ccIfIndex;
    }

    public void setCcIfIndex(Long ccIfIndex) {
        this.ccIfIndex = ccIfIndex;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPortIfIndex() {
        return portIfIndex;
    }

    public void setPortIfIndex(Long portIfIndex) {
        this.portIfIndex = portIfIndex;
    }

    public Long getPortType() {
        return portType;
    }

    public void setPortType(Long portType) {
        this.portType = portType;
    }

    public Timestamp getRealTime() {
        if (realTimeLong != null) {
            return new Timestamp(realTimeLong);
        } else {
            return null;
        }
    }

    public void setRealTime(Timestamp realTime) {
        if (realTime != null) {
            this.realTimeLong = realTime.getTime();
            this.realTimeString = df.format(realTime);
        }
    }

    public Long getRealTimeLong() {
        return realTimeLong;
    }

    public void setRealTimeLong(Long realTimeLong) {
        this.realTimeLong = realTimeLong;
    }

    public Timestamp getTime() {
        if (timeLong != null) {
            return new Timestamp(timeLong);
        } else {
            return null;
        }
    }

    public void setTime(Timestamp time) {
        if (time != null) {
            this.timeLong = time.getTime();
            this.timeString = df.format(time);
        }
    }

    public Long getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(Long timeLong) {
        this.timeLong = timeLong;
    }

    public void addOnline() {
        onlineNum++;
    }

    public void addOffline() {
        offlineNum++;
    }

    public void addOther() {
        otherNum++;
    }

    public void addBroadbandNum() {
        broadbandNum++;
    }

    public void addInteractiveNum() {
        interactiveNum++;
    }

    public void addIntegratedNum() {
        integratedNum++;
    }

    public void addMtaNum() {
        mtaNum++;
    }

    public void addCpeNum() {
        cpeNum++;
    }

    public void addCpeBroadbandNum() {
        cpeBroadbandNum++;
    }

    public void addCpeMtaNum() {
        cpeMtaNum++;
    }

    public void addCpeInteractiveNum() {
        cpeInteractiveNum++;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCcName() {
        return ccName;
    }

    public void setCcName(String ccName) {
        this.ccName = ccName;
    }

    public Double getCpuRate() {
        return cpuRate;
    }

    public void setCpuRate(Double cpuRate) {
        this.cpuRate = cpuRate;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public Integer getAllNum() {
        return allNum;
    }

    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getRealTimeString() {
        return realTimeString;
    }

    public void setRealTimeString(String realTimeString) {
        this.realTimeString = realTimeString;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public Integer getCpeInteractiveNum() {
        return cpeInteractiveNum;
    }

    public void setCpeInteractiveNum(Integer cpeInteractiveNum) {
        this.cpeInteractiveNum = cpeInteractiveNum;
    }

    public Integer getCpeBroadbandNum() {
        return cpeBroadbandNum;
    }

    public void setCpeBroadbandNum(Integer cpeBroadbandNum) {
        this.cpeBroadbandNum = cpeBroadbandNum;
    }

    public Integer getCpeMtaNum() {
        return cpeMtaNum;
    }

    public void setCpeMtaNum(Integer cpeMtaNum) {
        this.cpeMtaNum = cpeMtaNum;
    }

    public Long getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }

    @Override
    public String toString() {
        return "CmNum{" + "areaId=" + areaId + ", areaName='" + areaName + '\'' + ", entityId=" + entityId
                + ", entityName='" + entityName + '\'' + ", entityIp='" + entityIp + '\'' + ", cmcId=" + cmcId
                + ", ccIfIndex=" + ccIfIndex + ", ponIndex=" + ponIndex + ", ccName='" + ccName + '\''
                + ", portIfIndex=" + portIfIndex + ", portName='" + portName + '\'' + ", portType=" + portType
                + ", cpuRate=" + cpuRate + ", allNum=" + allNum + ", onlineNum=" + onlineNum + ", otherNum=" + otherNum
                + ", offlineNum=" + offlineNum + ", interactiveNum=" + interactiveNum + ", broadbandNum="
                + broadbandNum + ", mtaNum=" + mtaNum + ", integratedNum=" + integratedNum + ", cpeNum=" + cpeNum
                + ", cpeInteractiveNum=" + cpeInteractiveNum + ", cpeBroadbandNum=" + cpeBroadbandNum + ", cpeMtaNum="
                + cpeMtaNum + ", timeLong=" + timeLong + ", realTimeLong=" + realTimeLong + ", realTimeString='"
                + realTimeString + '\'' + '}';
    }
}
