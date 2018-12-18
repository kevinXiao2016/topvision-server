/***********************************************************************
 * $Id: OltPonAttribute.java,v1.0 2011-9-26 上午09:18:59 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * PON口属性
 * 
 * @author zhanglongyang
 * 
 */
public class OltPonAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -7493185179518076372L;
    private Long entityId;
    private String entityIp;
    private String entityName;
    private Long ponId;
    private String ponPort;// 报表用字段
    private String ponPortName;// 报表用字段
    private Long slotId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.1", index = true)
    private Long deviceNo = 1L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.2", index = true)
    private Long slotNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.3", index = true)
    private Long ponNo;
    private Long ponIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.4")
    private Integer ponPortType;
    private String ponPortTypeString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.5")
    private Integer ponOperationStatus;
    private String ponOperationStatusString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.6", writable = true, type = "Integer32")
    private Integer ponPortAdminStatus;
    private String ponPortAdminStatusString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.7")
    private Integer ponPortMaxOnuNumSupport;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.8")
    private Integer ponPortUpOnuNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.9", writable = true, type = "Integer32")
    private Integer ponPortEncryptMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.10", writable = true, type = "Integer32")
    private Integer ponPortEncryptKeyExchangeTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.11", writable = true, type = "Integer32")
    private Integer ponPortIsolationEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.12")
    private Integer maxDsBandwidth;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.13")
    private Integer actualDsBandwidthInUse;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.14")
    private Integer remainDsBandwidth;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.15", writable = true, type = "Integer32")
    private Integer perfStats15minuteEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.16", writable = true, type = "Integer32")
    private Integer perfStats24hourEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.17", writable = true, type = "Integer32")
    private Long ponPortMacAddrLearnMaxNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.18")
    private Integer maxUsBandwidth;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.19")
    private Integer actualUsBandwidthInUse;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.20")
    private Integer remainUsBandwidth;
    private String sMaxDsBandwidth;
    private String sMaxUsBandwidth;
    // 标记它是否是PON保护组备用端口，如果是备用端口，在前端将无法进行任何操作
    private boolean isStandbyPort;
    private Integer ponSpeedMode;//PON端口工作模式 用于2.5G EPON
    private Integer ponRogueSwitch;//PON端口长发光排查开关

    // PON口最大带宽限制
    private Integer ponBandMax;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Long getPonIndex() {
        if (ponIndex == null) {
            ponIndex = new EponIndex(slotNo.intValue(), ponNo.intValue()).getPonIndex();
        }
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
        slotNo = EponIndex.getSlotNo(ponIndex);
        ponNo = EponIndex.getPonNo(ponIndex);
    }

    public Integer getPonPortType() {
        return ponPortType;
    }

    public void setPonPortType(Integer ponPortType) {
        this.ponPortType = ponPortType;
    }

    public Integer getPonOperationStatus() {
        return ponOperationStatus;
    }

    public void setPonOperationStatus(Integer ponOperationStatus) {
        this.ponOperationStatus = ponOperationStatus;
    }

    public Integer getPonPortAdminStatus() {
        return ponPortAdminStatus;
    }

    public void setPonPortAdminStatus(Integer ponPortAdminStatus) {
        this.ponPortAdminStatus = ponPortAdminStatus;
    }

    public Integer getPonPortMaxOnuNumSupport() {
        return ponPortMaxOnuNumSupport;
    }

    public void setPonPortMaxOnuNumSupport(Integer ponPortMaxOnuNumSupport) {
        this.ponPortMaxOnuNumSupport = ponPortMaxOnuNumSupport;
    }

    public Integer getPonPortUpOnuNum() {
        return ponPortUpOnuNum;
    }

    public void setPonPortUpOnuNum(Integer ponPortUpOnuNum) {
        this.ponPortUpOnuNum = ponPortUpOnuNum;
    }

    public Integer getPonPortEncryptMode() {
        return ponPortEncryptMode;
    }

    public void setPonPortEncryptMode(Integer ponPortEncryptMode) {
        this.ponPortEncryptMode = ponPortEncryptMode;
    }

    public Integer getPonPortEncryptKeyExchangeTime() {
        return ponPortEncryptKeyExchangeTime;
    }

    public void setPonPortEncryptKeyExchangeTime(Integer ponPortEncryptKeyExchangeTime) {
        this.ponPortEncryptKeyExchangeTime = ponPortEncryptKeyExchangeTime;
    }

    public Integer getPonPortIsolationEnable() {
        return ponPortIsolationEnable;
    }

    public void setPonPortIsolationEnable(Integer ponPortIsolationEnable) {
        this.ponPortIsolationEnable = ponPortIsolationEnable;
    }

    public Integer getMaxDsBandwidth() {
        return maxDsBandwidth;
    }

    public void setMaxDsBandwidth(Integer maxDsBandwidth) {
        this.maxDsBandwidth = maxDsBandwidth;
    }

    public Integer getActualDsBandwidthInUse() {
        return actualDsBandwidthInUse;
    }

    public void setActualDsBandwidthInUse(Integer actualDsBandwidthInUse) {
        this.actualDsBandwidthInUse = actualDsBandwidthInUse;
    }

    public Integer getRemainDsBandwidth() {
        return remainDsBandwidth;
    }

    public void setRemainDsBandwidth(Integer remainDsBandwidth) {
        this.remainDsBandwidth = remainDsBandwidth;
    }

    public Integer getPerfStats15minuteEnable() {
        return perfStats15minuteEnable;
    }

    public void setPerfStats15minuteEnable(Integer perfStats15minuteEnable) {
        this.perfStats15minuteEnable = perfStats15minuteEnable;
    }

    public Integer getPerfStats24hourEnable() {
        return perfStats24hourEnable;
    }

    public void setPerfStats24hourEnable(Integer perfStats24hourEnable) {
        this.perfStats24hourEnable = perfStats24hourEnable;
    }

    public Long getPonPortMacAddrLearnMaxNum() {
        return ponPortMacAddrLearnMaxNum;
    }

    public void setPonPortMacAddrLearnMaxNum(Long ponPortMacAddrLearnMaxNum) {
        this.ponPortMacAddrLearnMaxNum = ponPortMacAddrLearnMaxNum;
    }

    public Integer getMaxUsBandwidth() {
        return maxUsBandwidth;
    }

    public void setMaxUsBandwidth(Integer maxUsBandwidth) {
        this.maxUsBandwidth = maxUsBandwidth;
    }

    public Integer getActualUsBandwidthInUse() {
        return actualUsBandwidthInUse;
    }

    public void setActualUsBandwidthInUse(Integer actualUsBandwidthInUse) {
        this.actualUsBandwidthInUse = actualUsBandwidthInUse;
    }

    public Integer getRemainUsBandwidth() {
        return remainUsBandwidth;
    }

    public void setRemainUsBandwidth(Integer remainUsBandwidth) {
        this.remainUsBandwidth = remainUsBandwidth;
    }

    public Long getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(Long deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Long getPonNo() {
        return ponNo;
    }

    public void setPonNo(Long ponNo) {
        this.ponNo = ponNo;
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
    }

    public String getSMaxDsBandwidth() {
        if (getMaxDsBandwidth() == 0) {
            sMaxDsBandwidth = "No limit";
        } else if (this.getMaxDsBandwidth() >= 1000) {
            sMaxDsBandwidth = this.getMaxDsBandwidth() / 1000 + "Mbps";
        } else {
            sMaxDsBandwidth = this.getMaxDsBandwidth() + "Kbps";
        }
        return sMaxDsBandwidth;
    }

    public String getSMaxUsBandwidth() {
        if (getMaxUsBandwidth() == 0) {
            sMaxUsBandwidth = "No limit";
        } else if (this.getMaxUsBandwidth() >= 1000) {
            sMaxUsBandwidth = this.getMaxUsBandwidth() / 1000 + "Mbps";
        } else {
            sMaxUsBandwidth = this.getMaxUsBandwidth() + "Kbps";
        }
        return sMaxUsBandwidth;
    }

    public void setSMaxUsBandwidth(String sMaxUsBandwidth) {
        this.sMaxUsBandwidth = sMaxUsBandwidth;
    }

    public void setSMaxDsBandwidth(String sMaxDsBandwidth) {
        this.sMaxDsBandwidth = sMaxDsBandwidth;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public String getPonOperationStatusString() {
        return ponOperationStatusString;
    }

    public void setPonOperationStatusString(String ponOperationStatusString) {
        this.ponOperationStatusString = ponOperationStatusString;
    }

    public String getPonPortAdminStatusString() {
        return ponPortAdminStatusString;
    }

    public void setPonPortAdminStatusString(String ponPortAdminStatusString) {
        this.ponPortAdminStatusString = ponPortAdminStatusString;
    }

    public String getPonPortTypeString() {
        return ponPortTypeString;
    }

    public void setPonPortTypeString(String ponPortTypeString) {
        this.ponPortTypeString = ponPortTypeString;
    }

    public String getPonPortName() {
        return ponPortName;
    }

    public void setPonPortName(String ponPortName) {
        this.ponPortName = ponPortName;
    }

    public String getsMaxDsBandwidth() {
        return sMaxDsBandwidth;
    }

    public void setsMaxDsBandwidth(String sMaxDsBandwidth) {
        this.sMaxDsBandwidth = sMaxDsBandwidth;
    }

    public String getsMaxUsBandwidth() {
        return sMaxUsBandwidth;
    }

    public void setsMaxUsBandwidth(String sMaxUsBandwidth) {
        this.sMaxUsBandwidth = sMaxUsBandwidth;
    }

    public String getPonPort() {
        return ponPort;
    }

    public void setPonPort(String ponPort) {
        this.ponPort = ponPort;
    }

    public Integer getPonBandMax() {
        return ponBandMax;
    }

    public void setPonBandMax(Integer ponBandMax) {
        this.ponBandMax = ponBandMax;
    }

    /**
     * @return the isStandbyPort
     */
    public boolean isStandbyPort() {
        return isStandbyPort;
    }

    /**
     * @param isStandbyPort
     *            the isStandbyPort to set
     */
    public void setStandbyPort(Long isStandbyPort) {
        // 该属性不为空则表示是PON保护备用端口，否则就是PON保护备用端口
        if (isStandbyPort == null || isStandbyPort.longValue() == 0L) {
            this.isStandbyPort = Boolean.FALSE;
        } else {
            this.isStandbyPort = Boolean.TRUE;
        }

    }

    public Integer getPonSpeedMode() {
        return ponSpeedMode;
    }

    public void setPonSpeedMode(Integer ponSpeedMode) {
        this.ponSpeedMode = ponSpeedMode;
    }

    public Integer getPonRogueSwitch() {
        return ponRogueSwitch;
    }

    public void setPonRogueSwitch(Integer ponRogueSwitch) {
        this.ponRogueSwitch = ponRogueSwitch;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltPonAttribute");
        sb.append("{actualDsBandwidthInUse=").append(actualDsBandwidthInUse);
        sb.append(", entityId=").append(entityId);
        sb.append(", ponId=").append(ponId);
        sb.append(", slotId=").append(slotId);
        sb.append(", deviceNo=").append(deviceNo);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", ponNo=").append(ponNo);
        sb.append(", ponIndex=").append(ponIndex);
        sb.append(", ponPortType=").append(ponPortType);
        sb.append(", ponOperationStatus=").append(ponOperationStatus);
        sb.append(", ponPortAdminStatus=").append(ponPortAdminStatus);
        sb.append(", ponPortMaxOnuNumSupport=").append(ponPortMaxOnuNumSupport);
        sb.append(", ponPortUpOnuNum=").append(ponPortUpOnuNum);
        sb.append(", ponPortEncryptMode=").append(ponPortEncryptMode);
        sb.append(", ponPortEncryptKeyExchangeTime=").append(ponPortEncryptKeyExchangeTime);
        sb.append(", ponPortIsolationEnable=").append(ponPortIsolationEnable);
        sb.append(", maxDsBandwidth=").append(maxDsBandwidth);
        sb.append(", sMaxDsBandwidth=").append(sMaxDsBandwidth);
        sb.append(", remainDsBandwidth=").append(remainDsBandwidth);
        sb.append(", perfStats15minuteEnable=").append(perfStats15minuteEnable);
        sb.append(", perfStats24hourEnable=").append(perfStats24hourEnable);
        sb.append(", ponPortMacAddrLearnMaxNum=").append(ponPortMacAddrLearnMaxNum);
        sb.append(", maxUsBandwidth=").append(maxUsBandwidth);
        sb.append(", sMaxUsBandwidth=").append(sMaxUsBandwidth);
        sb.append(", actualUsBandwidthInUse=").append(actualUsBandwidthInUse);
        sb.append(", remainUsBandwidth=").append(remainUsBandwidth);
        sb.append('}');
        return sb.toString();
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

}
