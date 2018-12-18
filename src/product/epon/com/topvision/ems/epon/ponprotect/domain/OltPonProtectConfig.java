/***********************************************************************
 * $Id: oltPonProtectConfig.java,v1.0 2012-11-1 上午11:13:37 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.ponprotect.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2012-11-1-上午11:13:37
 * 
 */
public class OltPonProtectConfig implements AliasesSuperType {
    private static final long serialVersionUID = 2494953387949953351L;

    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final int offset = Calendar.getInstance().getTimeZone().getRawOffset();
    /* 别名 */
    private String alias;
    /* 设备在DB中的编号 */
    private Long entityId;
    /* 设备IP ADDRESS */
    private String entityIp;
    /* 保护组ID */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.1.1.1.1", index = true)
    private Integer topPonPSGrpIndex;
    /* 保护组状态 */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.1.1.1.2", writable = true, type = "Integer32")
    private Integer topPonPsGrpAdmin;
    /* 主端口 INDEX */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.1.1.1.3", writable = true, type = "OctetString")
    private String topPonPSWorkPortItem;
    private long topPonPSWorkPortIndex;
    /* 主端口当前状态 */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.1.1.1.4")
    private Integer topPonPsWorkPortStatus;
    /* 备端口INDEX */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.1.1.1.5", writable = true, type = "OctetString")
    private String topPonPSStandbyPortItem;
    private long topPonPSStandbyPortIndex;
    /* 备端口当前状态 */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.1.1.1.6")
    private Integer topPonPsStandbyPortStatus;
    /* 手动倒换 */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.1.1.1.7", writable = true, type = "Integer32")
    private Integer topPonPsManualSwitch;
    /* 倒换次数 */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.1.1.1.8")
    private Integer topPonPsTimes;
    /* 最后一次倒换时间 */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.1.1.1.9")
    private Long topPonPsLastSwitchTime;
    private String lastSwitchTime;
    /* 倒换原因 */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.1.1.1.10")
    private String topPonPsReason;
    /* 自动回复为主端口激活 */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.1.1.1.11", writable = true, type = "Integer32")
    private Integer topPonPsResume;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.1.1.1.12", writable = true, type = "Integer32")
    private Integer topPonPsRowstatus;

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
     * @return the entityIp
     */
    public String getEntityIp() {
        return entityIp;
    }

    /**
     * @param entityIp
     *            the entityIp to set
     */
    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    /**
     * @return the topPonPSGrpIndex
     */
    public Integer getTopPonPSGrpIndex() {
        return topPonPSGrpIndex;
    }

    /**
     * @param topPonPSGrpIndex
     *            the topPonPSGrpIndex to set
     */
    public void setTopPonPSGrpIndex(Integer topPonPSGrpIndex) {
        this.topPonPSGrpIndex = topPonPSGrpIndex;
    }

    /**
     * @return the topPonPsGrpAdmin
     */
    public Integer getTopPonPsGrpAdmin() {
        return topPonPsGrpAdmin;
    }

    /**
     * @param topPonPsGrpAdmin
     *            the topPonPsGrpAdmin to set
     */
    public void setTopPonPsGrpAdmin(Integer topPonPsGrpAdmin) {
        this.topPonPsGrpAdmin = topPonPsGrpAdmin;
    }

    /**
     * @return the topPonPSWorkPortItem
     */
    public String getTopPonPSWorkPortItem() {
        return topPonPSWorkPortItem;
    }

    /**
     * @param topPonPSWorkPortItem
     *            the topPonPSWorkPortItem to set
     */
    public void setTopPonPSWorkPortItem(String topPonPSWorkPortItem) {
        this.topPonPSWorkPortItem = topPonPSWorkPortItem;
        this.topPonPSWorkPortIndex = EponIndex.getPortIndex(topPonPSWorkPortItem);
    }

    /**
     * @return the topPonPsWorkPortStatus
     */
    public Integer getTopPonPsWorkPortStatus() {
        return topPonPsWorkPortStatus;
    }

    /**
     * @param topPonPsWorkPortStatus
     *            the topPonPsWorkPortStatus to set
     */
    public void setTopPonPsWorkPortStatus(Integer topPonPsWorkPortStatus) {
        this.topPonPsWorkPortStatus = topPonPsWorkPortStatus;
    }

    /**
     * @return the topPonPSStandbyPortItem
     */
    public String getTopPonPSStandbyPortItem() {
        return topPonPSStandbyPortItem;
    }

    /**
     * @param topPonPSStandbyPortItem
     *            the topPonPSStandbyPortItem to set
     */
    public void setTopPonPSStandbyPortItem(String topPonPSStandbyPortItem) {
        this.topPonPSStandbyPortItem = topPonPSStandbyPortItem;
        this.topPonPSStandbyPortIndex = EponIndex.getPortIndex(topPonPSStandbyPortItem);
    }

    /**
     * @return the topPonPsStandbyPortStatus
     */
    public Integer getTopPonPsStandbyPortStatus() {
        return topPonPsStandbyPortStatus;
    }

    /**
     * @param topPonPsStandbyPortStatus
     *            the topPonPsStandbyPortStatus to set
     */
    public void setTopPonPsStandbyPortStatus(Integer topPonPsStandbyPortStatus) {
        this.topPonPsStandbyPortStatus = topPonPsStandbyPortStatus;
    }

    /**
     * @return the topPonPsManualSwitch
     */
    public Integer getTopPonPsManualSwitch() {
        return topPonPsManualSwitch;
    }

    /**
     * @param topPonPsManualSwitch
     *            the topPonPsManualSwitch to set
     */
    public void setTopPonPsManualSwitch(Integer topPonPsManualSwitch) {
        this.topPonPsManualSwitch = topPonPsManualSwitch;
    }

    /**
     * @return the topPonPsTimes
     */
    public Integer getTopPonPsTimes() {
        return topPonPsTimes;
    }

    /**
     * @param topPonPsTimes
     *            the topPonPsTimes to set
     */
    public void setTopPonPsTimes(Integer topPonPsTimes) {
        this.topPonPsTimes = topPonPsTimes;
    }

    /**
     * @return the topPonPsLastSwitchTime
     */
    public Long getTopPonPsLastSwitchTime() {
        return topPonPsLastSwitchTime;
    }

    /**
     * @param topPonPsLastSwitchTime
     *            the topPonPsLastSwitchTime to set
     */
    public void setTopPonPsLastSwitchTime(Long topPonPsLastSwitchTime) {
        this.topPonPsLastSwitchTime = topPonPsLastSwitchTime;
    }

    /**
     * @return the topPonPsReason
     */
    public String getTopPonPsReason() {
        return topPonPsReason;
    }

    /**
     * @param topPonPsReason
     *            the topPonPsReason to set
     */
    public void setTopPonPsReason(String topPonPsReason) {
        this.topPonPsReason = topPonPsReason;
    }

    /**
     * @return the topPonPsResume
     */
    public Integer getTopPonPsResume() {
        return topPonPsResume;
    }

    /**
     * @param topPonPsResume
     *            the topPonPsResume to set
     */
    public void setTopPonPsResume(Integer topPonPsResume) {
        this.topPonPsResume = topPonPsResume;
    }

    /**
     * @return the topPonPsRowstatus
     */
    public Integer getTopPonPsRowstatus() {
        return topPonPsRowstatus;
    }

    /**
     * @param topPonPsRowstatus
     *            the topPonPsRowstatus to set
     */
    public void setTopPonPsRowstatus(Integer topPonPsRowstatus) {
        this.topPonPsRowstatus = topPonPsRowstatus;
    }

    /**
     * @return the topPonPSWorkPortIndex
     */
    public long getTopPonPSWorkPortIndex() {
        return topPonPSWorkPortIndex;
    }

    /**
     * @param topPonPSWorkPortIndex
     *            the topPonPSWorkPortIndex to set
     */
    public void setTopPonPSWorkPortIndex(long topPonPSWorkPortIndex) {
        this.topPonPSWorkPortIndex = topPonPSWorkPortIndex;
        this.topPonPSWorkPortItem = EponIndex.getPortIndex(topPonPSWorkPortIndex);
    }

    /**
     * @return the topPonPSStandbyPortIndex
     */
    public long getTopPonPSStandbyPortIndex() {
        return topPonPSStandbyPortIndex;
    }

    /**
     * @param topPonPSStandbyPortIndex
     *            the topPonPSStandbyPortIndex to set
     */
    public void setTopPonPSStandbyPortIndex(long topPonPSStandbyPortIndex) {
        this.topPonPSStandbyPortIndex = topPonPSStandbyPortIndex;
        this.topPonPSStandbyPortItem = EponIndex.getPortIndex(topPonPSStandbyPortIndex);
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias
     *            the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @return the lastSwitchTime
     */
    public String getLastSwitchTime() {
        // 如果 > 0 则表示倒换过，不设置时间，否则会显示为1970年的时间
        if (this.topPonPsLastSwitchTime > 0) {
            this.lastSwitchTime = formatter.format(this.topPonPsLastSwitchTime * 1000 - offset);
        }
        return lastSwitchTime;
    }

    /**
     * @param lastSwitchTime
     *            the lastSwitchTime to set
     */
    public void setLastSwitchTime(String lastSwitchTime) {
        this.lastSwitchTime = lastSwitchTime;
    }

}
