/***********************************************************************
 * $Id: CC8800ABaseInfo.java,v1.0 2014-10-14 下午5:25:04 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-10-14-下午5:25:04
 *
 */
public class CC8800ABaseInfo implements AliasesSuperType {
    private static final long serialVersionUID = 8904813509851484765L;

    private Long entityId;
    private Long cmcId;
    //类A型设备对应的onuIndex
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.2")
    private String cmcSysObjectId;
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

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
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
        return cmcSysUpTime;
    }

    public void setCmcSysUpTime(Long cmcSysUpTime) {
        this.cmcSysUpTime = cmcSysUpTime;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CC8800ABaseInfo [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
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
