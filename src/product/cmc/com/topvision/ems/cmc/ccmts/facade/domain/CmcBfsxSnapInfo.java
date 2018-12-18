/***********************************************************************
 * $Id: CmcBfsxSnapInfo.java,v1.0 2014年9月23日 上午10:59:50 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2014年9月23日-上午10:59:50
 *
 */
public class CmcBfsxSnapInfo implements AliasesSuperType {
    private static final long serialVersionUID = -3297468979466883140L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.2")
    private String cmcSysObjectId;
    //@EMS-10489 把类A型设备的运行时长采集从sysUptime改为topCcmtsSysRunTime获取
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.19")
    private Long topCcmtsSysRunTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.3")
    private Long cmcSysUpTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.5")
    private String cmcSysName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.6")
    private String cmcSysLocation;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.10")
    private Integer cmcSysRAMRatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.11")
    private Integer cmcSysCPURatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.12")
    private String cmcSysMacAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.13")
    private Integer cmcSysFlashRatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.14")
    private Integer cmcSysStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.15")
    private String cmcSysSwVersion;

    private Long macAddrLong;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public String getCmcSysObjectId() {
        return cmcSysObjectId;
    }

    public void setCmcSysObjectId(String cmcSysObjectId) {
        this.cmcSysObjectId = cmcSysObjectId;
    }

    public Long getCmcSysUpTime() {
        if (topCcmtsSysRunTime != null) {
            cmcSysUpTime = topCcmtsSysRunTime;
        }
        return cmcSysUpTime;
    }

    public void setCmcSysUpTime(Long cmcSysUpTime) {
        if (topCcmtsSysRunTime != null) {
            this.cmcSysUpTime = topCcmtsSysRunTime;
        } else {
            this.cmcSysUpTime = cmcSysUpTime;
        }
    }

    public String getCmcSysName() {
        return cmcSysName;
    }

    public void setCmcSysName(String cmcSysName) {
        this.cmcSysName = cmcSysName;
    }

    public String getCmcSysLocation() {
        return cmcSysLocation;
    }

    public void setCmcSysLocation(String cmcSysLocation) {
        this.cmcSysLocation = cmcSysLocation;
    }

    public Integer getCmcSysRAMRatio() {
        return cmcSysRAMRatio;
    }

    public void setCmcSysRAMRatio(Integer cmcSysRAMRatio) {
        this.cmcSysRAMRatio = cmcSysRAMRatio;
    }

    public Integer getCmcSysCPURatio() {
        return cmcSysCPURatio;
    }

    public void setCmcSysCPURatio(Integer cmcSysCPURatio) {
        this.cmcSysCPURatio = cmcSysCPURatio;
    }

    public String getCmcSysMacAddr() {
        return cmcSysMacAddr;
    }

    public void setCmcSysMacAddr(String cmcSysMacAddr) {
        this.cmcSysMacAddr = cmcSysMacAddr;
        if (cmcSysMacAddr != null) {
            this.macAddrLong = new MacUtils(cmcSysMacAddr).longValue();
        }
    }

    public Integer getCmcSysFlashRatio() {
        return cmcSysFlashRatio;
    }

    public void setCmcSysFlashRatio(Integer cmcSysFlashRatio) {
        this.cmcSysFlashRatio = cmcSysFlashRatio;
    }

    public Integer getCmcSysStatus() {
        return cmcSysStatus;
    }

    public void setCmcSysStatus(Integer cmcSysStatus) {
        this.cmcSysStatus = cmcSysStatus;
    }

    public String getCmcSysSwVersion() {
        return cmcSysSwVersion;
    }

    public void setCmcSysSwVersion(String cmcSysSwVersion) {
        this.cmcSysSwVersion = cmcSysSwVersion;
    }

    public Long getMacAddrLong() {
        return macAddrLong;
    }

    public void setMacAddrLong(Long macAddrLong) {
        this.macAddrLong = macAddrLong;
    }

    public Long getTopCcmtsSysRunTime() {
        return topCcmtsSysRunTime;
    }

    public void setTopCcmtsSysRunTime(Long topCcmtsSysRunTime) {
        this.topCcmtsSysRunTime = topCcmtsSysRunTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcBfsxSnapInfo [entityId=");
        builder.append(entityId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", cmcSysObjectId=");
        builder.append(cmcSysObjectId);
        builder.append(", cmcSysUpTime=");
        builder.append(cmcSysUpTime);
        builder.append(", cmcSysName=");
        builder.append(cmcSysName);
        builder.append(", cmcSysLocation=");
        builder.append(cmcSysLocation);
        builder.append(", cmcSysRAMRatio=");
        builder.append(cmcSysRAMRatio);
        builder.append(", cmcSysCPURatio=");
        builder.append(cmcSysCPURatio);
        builder.append(", cmcSysMacAddr=");
        builder.append(cmcSysMacAddr);
        builder.append(", cmcSysFlashRatio=");
        builder.append(cmcSysFlashRatio);
        builder.append(", cmcSysStatus=");
        builder.append(cmcSysStatus);
        builder.append(", cmcSysSwVersion=");
        builder.append(cmcSysSwVersion);
        builder.append(", macAddrLong=");
        builder.append(macAddrLong);
        builder.append("]");
        return builder.toString();
    }

}
