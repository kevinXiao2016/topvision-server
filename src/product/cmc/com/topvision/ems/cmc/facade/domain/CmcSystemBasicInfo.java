/***********************************************************************
 * $Id: CmcSystemBasicInfo.java,v1.0 2012-2-13 下午04:33:49 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2012-2-13-下午04:33:49
 * 
 */
@Alias("cmcSystemBasicInfo")
@TableProperty(tables = { "default", "cmNum" })
public class CmcSystemBasicInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 5393463483628466020L;
    private Long entityId;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.1", index = true)
    private Long ifIndex;
    private Integer cmcDeviceStyle;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.1")
    private String topCcmtsSysDescr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.2")
    private String topCcmtsSysObjectId;
    //@EMS-10489 把类A型设备的运行时长采集从sysUptime改为topCcmtsSysRunTime获取         
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.19")
    private Long topCcmtsSysRunTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.3")
    private Long topCcmtsSysUpTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.4", writable = true, type = "OctetString")
    private String topCcmtsSysContact;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.5", writable = true, type = "OctetString")
    private String topCcmtsSysName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.6", writable = true, type = "OctetString")
    private String topCcmtsSysLocation;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.7")
    private Integer topCcmtsSysService;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.8")
    private Long topCcmtsSysORLastChange;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.9")
    private Integer topCcmtsDocsisBaseCapability;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.10")
    private Integer topCcmtsSysRAMRatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.11")
    private Integer topCcmtsSysCPURatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.12")
    private String topCcmtsSysMacAddr;
    private Long topCcmtsSysMacAddrLong;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.13")
    private Integer topCcmtsSysFlashRatio;
    @SnmpProperty(table = "cmNum", oid = "1.3.6.1.4.1.32285.11.1.1.2.2.1.1.1")
    private Long topCcmtsCmNumTotal;
    @SnmpProperty(table = "cmNum", oid = "1.3.6.1.4.1.32285.11.1.1.2.2.1.1.2")
    private Long topCcmtsCmNumOutline;
    @SnmpProperty(table = "cmNum", oid = "1.3.6.1.4.1.32285.11.1.1.2.2.1.1.3")
    private Long topCcmtsCmNumOnline;
    @SnmpProperty(table = "cmNum", oid = "1.3.6.1.4.1.32285.11.1.1.2.2.1.1.4")
    private Long topCcmtsCmNumReg;
    @SnmpProperty(table = "cmNum", oid = "1.3.6.1.4.1.32285.11.1.1.2.2.1.1.5")
    private Long topCcmtsCmNumRanged;
    @SnmpProperty(table = "cmNum", oid = "1.3.6.1.4.1.32285.11.1.1.2.2.1.1.6")
    private Long topCcmtsCmNumRanging;

    /**
     * @return the ifIndex
     */
    public Long getIfIndex() {
        return ifIndex;
    }

    /**
     * @param ifIndex
     *            the ifIndex to set
     */
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    /**
     * @return the topCcmtsSysDescr
     */
    public String getTopCcmtsSysDescr() {
        return topCcmtsSysDescr;
    }

    /**
     * @param topCcmtsSysDescr
     *            the topCcmtsSysDescr to set
     */
    public void setTopCcmtsSysDescr(String topCcmtsSysDescr) {
        this.topCcmtsSysDescr = topCcmtsSysDescr;
    }

    /**
     * @return the topCcmtsSysObjectId
     */
    public String getTopCcmtsSysObjectId() {
        return topCcmtsSysObjectId;
    }

    /**
     * @param topCcmtsSysObjectId
     *            the topCcmtsSysObjectId to set
     */
    public void setTopCcmtsSysObjectId(String topCcmtsSysObjectId) {
        this.topCcmtsSysObjectId = topCcmtsSysObjectId;
    }

    /**
     * @return the topCcmtsSysUpTime
     */
    public Long getTopCcmtsSysUpTime() {
        if (topCcmtsSysRunTime != null) {
            topCcmtsSysUpTime = topCcmtsSysRunTime;
        }
        return topCcmtsSysUpTime;
    }

    /**
     * @param topCcmtsSysUpTime
     *            the topCcmtsSysUpTime to set
     */
    public void setTopCcmtsSysUpTime(Long topCcmtsSysUpTime) {
        if (topCcmtsSysRunTime != null) {
            this.topCcmtsSysUpTime = topCcmtsSysRunTime;
        } else {
            this.topCcmtsSysUpTime = topCcmtsSysUpTime;
        }
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

    /**
     * @return the topCcmtsSysContact
     */
    public String getTopCcmtsSysContact() {
        return topCcmtsSysContact;
    }

    /**
     * @param topCcmtsSysContact
     *            the topCcmtsSysContact to set
     */
    public void setTopCcmtsSysContact(String topCcmtsSysContact) {
        this.topCcmtsSysContact = topCcmtsSysContact;
    }

    /**
     * @return the topCcmtsSysName
     */
    public String getTopCcmtsSysName() {
        return topCcmtsSysName;
    }

    /**
     * @param topCcmtsSysName
     *            the topCcmtsSysName to set
     */
    public void setTopCcmtsSysName(String topCcmtsSysName) {
        this.topCcmtsSysName = topCcmtsSysName;
    }

    /**
     * @return the topCcmtsSysLocation
     */
    public String getTopCcmtsSysLocation() {
        return topCcmtsSysLocation;
    }

    /**
     * @param topCcmtsSysLocation
     *            the topCcmtsSysLocation to set
     */
    public void setTopCcmtsSysLocation(String topCcmtsSysLocation) {
        this.topCcmtsSysLocation = topCcmtsSysLocation;
    }

    /**
     * @return the topCcmtsSysService
     */
    public Integer getTopCcmtsSysService() {
        return topCcmtsSysService;
    }

    /**
     * @param topCcmtsSysService
     *            the topCcmtsSysService to set
     */
    public void setTopCcmtsSysService(Integer topCcmtsSysService) {
        this.topCcmtsSysService = topCcmtsSysService;
    }

    /**
     * @return the topCcmtsSysORLastChange
     */
    public Long getTopCcmtsSysORLastChange() {
        return topCcmtsSysORLastChange;
    }

    /**
     * @param topCcmtsSysORLastChange
     *            the topCcmtsSysORLastChange to set
     */
    public void setTopCcmtsSysORLastChange(Long topCcmtsSysORLastChange) {
        this.topCcmtsSysORLastChange = topCcmtsSysORLastChange;
    }

    /**
     * @return the topCcmtsDocsisBaseCapability
     */
    public Integer getTopCcmtsDocsisBaseCapability() {
        return topCcmtsDocsisBaseCapability;
    }

    /**
     * @param topCcmtsDocsisBaseCapability
     *            the topCcmtsDocsisBaseCapability to set
     */
    public void setTopCcmtsDocsisBaseCapability(Integer topCcmtsDocsisBaseCapability) {
        this.topCcmtsDocsisBaseCapability = topCcmtsDocsisBaseCapability;
    }

    /**
     * @return the topCcmtsSysRAMRatio
     */
    public Integer getTopCcmtsSysRAMRatio() {
        return topCcmtsSysRAMRatio;
    }

    /**
     * @param topCcmtsSysRAMRatio
     *            the topCcmtsSysRAMRatio to set
     */
    public void setTopCcmtsSysRAMRatio(Integer topCcmtsSysRAMRatio) {
        this.topCcmtsSysRAMRatio = topCcmtsSysRAMRatio;
    }

    /**
     * @return the topCcmtsSysCPURatio
     */
    public Integer getTopCcmtsSysCPURatio() {
        return topCcmtsSysCPURatio;
    }

    /**
     * @param topCcmtsSysCPURatio
     *            the topCcmtsSysCPURatio to set
     */
    public void setTopCcmtsSysCPURatio(Integer topCcmtsSysCPURatio) {
        this.topCcmtsSysCPURatio = topCcmtsSysCPURatio;
    }

    /**
     * @return the topCcmtsSysMacAddr
     */
    public String getTopCcmtsSysMacAddr() {
        return topCcmtsSysMacAddr;
    }

    /**
     * @param topCcmtsSysMacAddr
     *            the topCcmtsSysMacAddr to set
     */
    public void setTopCcmtsSysMacAddr(String topCcmtsSysMacAddr) {
        this.topCcmtsSysMacAddr = topCcmtsSysMacAddr;
    }

    /**
     * @return the topCcmtsSysMacAddrLong
     */
    public Long getTopCcmtsSysMacAddrLong() {
        return topCcmtsSysMacAddrLong;
    }

    /**
     * @param topCcmtsSysMacAddrLong
     *            the topCcmtsSysMacAddrLong to set
     */
    public void setTopCcmtsSysMacAddrLong(Long topCcmtsSysMacAddrLong) {
        this.topCcmtsSysMacAddrLong = topCcmtsSysMacAddrLong;
    }

    /**
     * @return the topCcmtsSysFlashRatio
     */
    public Integer getTopCcmtsSysFlashRatio() {
        return topCcmtsSysFlashRatio;
    }

    /**
     * @param topCcmtsSysFlashRatio
     *            the topCcmtsSysFlashRatio to set
     */
    public void setTopCcmtsSysFlashRatio(Integer topCcmtsSysFlashRatio) {
        this.topCcmtsSysFlashRatio = topCcmtsSysFlashRatio;
    }

    /**
     * @return the topCcmtsCmNumTotal
     */
    public Long getTopCcmtsCmNumTotal() {
        return topCcmtsCmNumTotal;
    }

    /**
     * @param topCcmtsCmNumTotal
     *            the topCcmtsCmNumTotal to set
     */
    public void setTopCcmtsCmNumTotal(Long topCcmtsCmNumTotal) {
        this.topCcmtsCmNumTotal = topCcmtsCmNumTotal;
    }

    /**
     * @return the topCcmtsCmNumOutline
     */
    public Long getTopCcmtsCmNumOutline() {
        return topCcmtsCmNumOutline;
    }

    /**
     * @param topCcmtsCmNumOutline
     *            the topCcmtsCmNumOutline to set
     */
    public void setTopCcmtsCmNumOutline(Long topCcmtsCmNumOutline) {
        this.topCcmtsCmNumOutline = topCcmtsCmNumOutline;
    }

    /**
     * @return the topCcmtsCmNumOnline
     */
    public Long getTopCcmtsCmNumOnline() {
        return topCcmtsCmNumOnline;
    }

    /**
     * @param topCcmtsCmNumOnline
     *            the topCcmtsCmNumOnline to set
     */
    public void setTopCcmtsCmNumOnline(Long topCcmtsCmNumOnline) {
        this.topCcmtsCmNumOnline = topCcmtsCmNumOnline;
    }

    /**
     * @return the topCcmtsCmNumReg
     */
    public Long getTopCcmtsCmNumReg() {
        return topCcmtsCmNumReg;
    }

    /**
     * @param topCcmtsCmNumReg
     *            the topCcmtsCmNumReg to set
     */
    public void setTopCcmtsCmNumReg(Long topCcmtsCmNumReg) {
        this.topCcmtsCmNumReg = topCcmtsCmNumReg;
    }

    /**
     * @return the topCcmtsCmNumRanged
     */
    public Long getTopCcmtsCmNumRanged() {
        return topCcmtsCmNumRanged;
    }

    /**
     * @param topCcmtsCmNumRanged
     *            the topCcmtsCmNumRanged to set
     */
    public void setTopCcmtsCmNumRanged(Long topCcmtsCmNumRanged) {
        this.topCcmtsCmNumRanged = topCcmtsCmNumRanged;
    }

    /**
     * @return the topCcmtsCmNumRanging
     */
    public Long getTopCcmtsCmNumRanging() {
        return topCcmtsCmNumRanging;
    }

    /**
     * @param topCcmtsCmNumRanging
     *            the topCcmtsCmNumRanging to set
     */
    public void setTopCcmtsCmNumRanging(Long topCcmtsCmNumRanging) {
        this.topCcmtsCmNumRanging = topCcmtsCmNumRanging;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcSystemBasicInfo [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", ifIndex=");
        builder.append(ifIndex);
        builder.append(", cmcDeviceStyle=");
        builder.append(cmcDeviceStyle);
        builder.append(", topCcmtsSysDescr=");
        builder.append(topCcmtsSysDescr);
        builder.append(", topCcmtsSysObjectId=");
        builder.append(topCcmtsSysObjectId);
        builder.append(", topCcmtsSysUpTime=");
        builder.append(topCcmtsSysUpTime);
        builder.append(", topCcmtsSysContact=");
        builder.append(topCcmtsSysContact);
        builder.append(", topCcmtsSysName=");
        builder.append(topCcmtsSysName);
        builder.append(", topCcmtsSysLocation=");
        builder.append(topCcmtsSysLocation);
        builder.append(", topCcmtsSysService=");
        builder.append(topCcmtsSysService);
        builder.append(", topCcmtsSysORLastChange=");
        builder.append(topCcmtsSysORLastChange);
        builder.append(", topCcmtsDocsisBaseCapability=");
        builder.append(topCcmtsDocsisBaseCapability);
        builder.append(", topCcmtsSysRAMRatio=");
        builder.append(topCcmtsSysRAMRatio);
        builder.append(", topCcmtsSysCPURatio=");
        builder.append(topCcmtsSysCPURatio);
        builder.append(", topCcmtsSysMacAddr=");
        builder.append(topCcmtsSysMacAddr);
        builder.append(", topCcmtsSysMacAddrLong=");
        builder.append(topCcmtsSysMacAddrLong);
        builder.append(", topCcmtsSysFlashRatio=");
        builder.append(topCcmtsSysFlashRatio);
        builder.append(", topCcmtsCmNumTotal=");
        builder.append(topCcmtsCmNumTotal);
        builder.append(", topCcmtsCmNumOutline=");
        builder.append(topCcmtsCmNumOutline);
        builder.append(", topCcmtsCmNumOnline=");
        builder.append(topCcmtsCmNumOnline);
        builder.append(", topCcmtsCmNumReg=");
        builder.append(topCcmtsCmNumReg);
        builder.append(", topCcmtsCmNumRanged=");
        builder.append(topCcmtsCmNumRanged);
        builder.append(", topCcmtsCmNumRanging=");
        builder.append(topCcmtsCmNumRanging);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the cmcDeviceStyle
     */
    public Integer getCmcDeviceStyle() {
        return cmcDeviceStyle;
    }

    /**
     * @param cmcDeviceStyle
     *            the cmcDeviceStyle to set
     */
    public void setCmcDeviceStyle(Integer cmcDeviceStyle) {
        this.cmcDeviceStyle = cmcDeviceStyle;
    }

}
