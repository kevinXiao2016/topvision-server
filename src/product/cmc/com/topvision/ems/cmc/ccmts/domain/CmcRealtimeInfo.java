/***********************************************************************
 * $Id: CmcRealtimeInfo.java,v1.0 2014年5月11日 上午9:42:06 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author loyal
 * @created @2014年5月11日-上午9:42:06
 *
 */
public class CmcRealtimeInfo implements Serializable {
    private static final long serialVersionUID = 6328326823282015580L;
    private Long cmcId;
    private String alias;
    private String displayName;
    private Integer alertNum;
    private String ip;
    private boolean pingCheck = false;
    private boolean snmpCheck = false;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;
    //@EMS-10489 把类A型设备的运行时长采集从sysUptime改为topCcmtsSysRunTime获取
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.19")
    private Long topCcmtsSysRunTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.3")
    private Long topCcmtsSysUpTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.10")
    private Integer topCcmtsSysRAMRatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.11")
    private Integer topCcmtsSysCPURatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.12")
    private String topCcmtsSysMacAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.13")
    private Integer topCcmtsSysFlashRatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.14")
    private Integer topCcmtsSysStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.15")
    private String topCcmtsSysSwVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.18")
    private String topCcmtsSysHwVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.10.1.1")
    private Long topCcmtsDDRTemperature;//CPU温度
    private String lastCollectTime = sf.format(new Date());

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    /**
     * @return the topCcmtsSysRunTime
     */
    public Long getTopCcmtsSysRunTime() {
        return topCcmtsSysRunTime;
    }

    /**
     * @param topCcmtsSysRunTime the topCcmtsSysRunTime to set
     */
    public void setTopCcmtsSysRunTime(Long topCcmtsSysRunTime) {
        this.topCcmtsSysRunTime = topCcmtsSysRunTime;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getAlertNum() {
        return alertNum;
    }

    public void setAlertNum(Integer alertNum) {
        this.alertNum = alertNum;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getTopCcmtsSysUpTime() {
        if (topCcmtsSysRunTime != null) {
            topCcmtsSysUpTime = topCcmtsSysRunTime;
        }
        return topCcmtsSysUpTime;
    }

    public void setTopCcmtsSysUpTime(Long topCcmtsSysUpTime) {
        if (topCcmtsSysRunTime != null) {
            this.topCcmtsSysUpTime = topCcmtsSysRunTime;
        } else {
            this.topCcmtsSysUpTime = topCcmtsSysUpTime;
        }
    }

    public Integer getTopCcmtsSysRAMRatio() {
        return topCcmtsSysRAMRatio;
    }

    public void setTopCcmtsSysRAMRatio(Integer topCcmtsSysRAMRatio) {
        this.topCcmtsSysRAMRatio = topCcmtsSysRAMRatio;
    }

    public Integer getTopCcmtsSysCPURatio() {
        return topCcmtsSysCPURatio;
    }

    public void setTopCcmtsSysCPURatio(Integer topCcmtsSysCPURatio) {
        this.topCcmtsSysCPURatio = topCcmtsSysCPURatio;
    }

    public String getTopCcmtsSysMacAddr() {
        return topCcmtsSysMacAddr;
    }

    public void setTopCcmtsSysMacAddr(String topCcmtsSysMacAddr) {
        this.topCcmtsSysMacAddr = topCcmtsSysMacAddr;
    }

    public Integer getTopCcmtsSysFlashRatio() {
        return topCcmtsSysFlashRatio;
    }

    public void setTopCcmtsSysFlashRatio(Integer topCcmtsSysFlashRatio) {
        this.topCcmtsSysFlashRatio = topCcmtsSysFlashRatio;
    }

    public Integer getTopCcmtsSysStatus() {
        return topCcmtsSysStatus;
    }

    public void setTopCcmtsSysStatus(Integer topCcmtsSysStatus) {
        this.topCcmtsSysStatus = topCcmtsSysStatus;
    }

    public String getTopCcmtsSysSwVersion() {
        return topCcmtsSysSwVersion;
    }

    public void setTopCcmtsSysSwVersion(String topCcmtsSysSwVersion) {
        this.topCcmtsSysSwVersion = topCcmtsSysSwVersion;
    }

    public String getTopCcmtsSysHwVersion() {
        return topCcmtsSysHwVersion;
    }

    public void setTopCcmtsSysHwVersion(String topCcmtsSysHwVersion) {
        this.topCcmtsSysHwVersion = topCcmtsSysHwVersion;
    }

    public Long getTopCcmtsDDRTemperature() {
        return topCcmtsDDRTemperature;
    }

    public void setTopCcmtsDDRTemperature(Long topCcmtsDDRTemperature) {
        this.topCcmtsDDRTemperature = topCcmtsDDRTemperature;
    }

    public String getLastCollectTime() {
        return lastCollectTime;
    }

    public void setLastCollectTime(String lastCollectTime) {
        this.lastCollectTime = lastCollectTime;
    }

    public boolean isPingCheck() {
        return pingCheck;
    }

    public void setPingCheck(boolean pingCheck) {
        this.pingCheck = pingCheck;
    }

    public boolean isSnmpCheck() {
        return snmpCheck;
    }

    public void setSnmpCheck(boolean snmpCheck) {
        this.snmpCheck = snmpCheck;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcRealtimeInfo [cmcId=");
        builder.append(cmcId);
        builder.append(", alias=");
        builder.append(alias);
        builder.append(", displayName=");
        builder.append(displayName);
        builder.append(", alertNum=");
        builder.append(alertNum);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", pingCheck=");
        builder.append(pingCheck);
        builder.append(", snmpCheck=");
        builder.append(snmpCheck);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", topCcmtsSysUpTime=");
        builder.append(topCcmtsSysUpTime);
        builder.append(", topCcmtsSysRAMRatio=");
        builder.append(topCcmtsSysRAMRatio);
        builder.append(", topCcmtsSysCPURatio=");
        builder.append(topCcmtsSysCPURatio);
        builder.append(", topCcmtsSysMacAddr=");
        builder.append(topCcmtsSysMacAddr);
        builder.append(", topCcmtsSysFlashRatio=");
        builder.append(topCcmtsSysFlashRatio);
        builder.append(", topCcmtsSysStatus=");
        builder.append(topCcmtsSysStatus);
        builder.append(", topCcmtsSysSwVersion=");
        builder.append(topCcmtsSysSwVersion);
        builder.append(", topCcmtsSysHwVersion=");
        builder.append(topCcmtsSysHwVersion);
        builder.append(", topCcmtsDDRTemperature=");
        builder.append(topCcmtsDDRTemperature);
        builder.append(", lastCollectTime=");
        builder.append(lastCollectTime);
        builder.append("]");
        return builder.toString();
    }
}
