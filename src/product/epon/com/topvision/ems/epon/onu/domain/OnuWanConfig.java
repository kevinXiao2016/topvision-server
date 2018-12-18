/***********************************************************************
 * $Id: OnuWanConfig.java,v1.0 2016年5月30日 下午5:12:41 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author loyal
 * @created @2016年5月30日-下午5:12:41
 * 
 */
public class OnuWanConfig implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -4125438659836858190L;
    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.1.1.1,V1.10:1.3.6.1.4.1.17409.2.9.1.1.1.1.1", index = true)
    private Long onuMibIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.1.1.2,V1.10:1.3.6.1.4.1.17409.2.9.1.1.1.1.2", writable = true, type = "Integer32")
    private Integer clearWan;// 清除WAN连接（WAN连接信息中）
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.1.1.3,V1.10:1.3.6.1.4.1.17409.2.9.1.1.1.1.3", writable = true, type = "Integer32")
    private Integer resetWan;// 恢复出厂设置（快照工具栏中）
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.1.1.4,V1.10:1.3.6.1.4.1.17409.2.9.1.1.1.1.4", writable = true, type = "Integer32")
    private Integer wanEnnable;// WAN使能（无线配置中） 1 enable,2 disable
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.1.1.5,V1.10:1.3.6.1.4.1.17409.2.9.1.1.1.1.5")
    private String hardVersion;// WIFI 硬件版本
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.1.1.6,V1.10:1.3.6.1.4.1.17409.2.9.1.1.1.1.6")
    private String softVersion;// WIFI 软件版本
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.1.1.7,V1.10:1.3.6.1.4.1.17409.2.9.1.1.1.1.7", writable = true, type = "Integer32")
    private Integer channelId;// 信道ID（自动，1-13）
    /**
     * STANDARD_802_11_B, STANDARD_802_11_G, STANDARD_802_11_N, STANDARD_802_11_BG,
     * STANDARD_802_11_BNG
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.1.1.8,V1.10:1.3.6.1.4.1.17409.2.9.1.1.1.1.8", writable = true)
    private String workMode;// 工作模式（使用bit位图描述，从MSB开始bit0-802.11b，bit1-802.11g，bit2-802.11n，该bit=0则不支持该模式，1-支持该模式）
    private Integer workModeForShow;// 工作模式1,2,3,4,5
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.1.1.9,V1.10:1.3.6.1.4.1.17409.2.9.1.1.1.1.9", writable = true, type = "Integer32")
    private Integer channelWidth;// 信道宽度
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.1.1.10,V1.10:1.3.6.1.4.1.17409.2.9.1.1.1.1.10", writable = true, type = "Integer32")
    private Integer sendPower;// 发射功率模式

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Integer getClearWan() {
        return clearWan;
    }

    public void setClearWan(Integer clearWan) {
        this.clearWan = clearWan;
    }

    public Integer getResetWan() {
        return resetWan;
    }

    public void setResetWan(Integer resetWan) {
        this.resetWan = resetWan;
    }

    public Integer getWanEnnable() {
        return wanEnnable;
    }

    public void setWanEnnable(Integer wanEnnable) {
        this.wanEnnable = wanEnnable;
    }

    public String getSoftVersion() {
        return softVersion;
    }

    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    public String getHardVersion() {
        return hardVersion;
    }

    public void setHardVersion(String hardVersion) {
        this.hardVersion = hardVersion;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getChannelWidth() {
        return channelWidth;
    }

    public void setChannelWidth(Integer channelWidth) {
        this.channelWidth = channelWidth;
    }

    public Integer getSendPower() {
        return sendPower;
    }

    public void setSendPower(Integer sendPower) {
        this.sendPower = sendPower;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        if (onuIndex != null) {
            onuMibIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
        }
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
     * @return the onuMibIndex
     */
    public Long getOnuMibIndex() {
        return onuMibIndex;
    }

    /**
     * @param onuMibIndex
     *            the onuMibIndex to set
     */
    public void setOnuMibIndex(Long onuMibIndex) {
        this.onuMibIndex = onuMibIndex;
        if (onuMibIndex != null) {
            onuIndex = EponIndex.getOnuIndexByMibIndex(onuMibIndex);
        }
    }

    /**
     * @return the workModeMib
     */
    public String getWorkMode() {
        return workMode;
    }

    /**
     * @param workModeMib
     *            the workModeMib to set
     */
    public void setWorkMode(String workMode) {
        if (workMode.length() == 1) {
            workMode = Integer.toHexString(workMode.getBytes()[0]);
        }
        this.workMode = workMode;
        // 工作模式（使用bit位图描述，从MSB开始bit0-802.11b，bit1-802.11g，bit2-802.11n，该bit=0则不支持该模式，1-支持该模式）
        switch (workMode) {
        case "80":
        case "01":
            this.workModeForShow = 1;// b
            break;
        case "40":
        case "02":
            this.workModeForShow = 2;// g
            break;
        case "20":
        case "04":
            this.workModeForShow = 3;// n
            break;
        case "c0":
        case "03":
            this.workModeForShow = 4;// bg
            break;
        case "e0":
        case "07":
            this.workModeForShow = 5;// bgn
            break;
        }
    }

    public Integer getWorkModeForShow() {
        return workModeForShow;
    }

    public void setWorkModeForShow(Integer workModeForShow) {
        this.workModeForShow = workModeForShow;
    }

}
