/***********************************************************************
 * $Id: OltSniAttribute.java,v1.0 2011-9-26 上午09:05:59 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * SNI口属性
 * 
 * @author zhanglongyang
 * 
 */
@TableProperty(tables = { "default", "sni" })
public class OltSniAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -2682676149572010001L;
    private Long entityId;
    private String entityIp;
    private String entityName;
    private Long sniId;
    private Long slotId;
    private Long sniIndex;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.1", index = true)
    private Long deviceNo = 1L;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.2", index = true)
    private Long slotNo;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.3", index = true)
    private Long sniNo;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.4", writable = true, type = "OctetString")
    private String sniPortName;
    private String sniPort;// 报表用字段
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.6")
    private Integer sniOperationStatus;
    private String sniOperationStatusString;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.5", writable = true, type = "Integer32")
    private Integer sniAdminStatus;
    private String sniAdminStatusString;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.7")
    private Integer sniMediaType;
    private String sniMediaTypeString;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.8")
    private Integer sniAutoNegotiationStatus;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.9", writable = true, type = "Integer32")
    private Integer sniAutoNegotiationMode;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.10", writable = true, type = "Integer32")
    private Integer sniPerfStats15minuteEnable;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.11", writable = true, type = "Integer32")
    private Integer sniPerfStats24hourEnable;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.12")
    private Long sniLastStatusChangeTime;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.13", writable = true, type = "Integer32")
    private Long sniMacAddrLearnMaxNum;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.14", writable = true, type = "Integer32")
    private Integer sniIsolationEnable;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.15")
    private Integer sniPortType; //端口下发时的实际类型 ge-Port(1) ten-Ge-Port(2)
    @SnmpProperty(table = "sni", oid = "1.3.6.1.4.1.32285.11.2.3.2.1.1.1", index = true)
    private Long topSniAttrCardNo;
    @SnmpProperty(table = "sni", oid = "1.3.6.1.4.1.32285.11.2.3.2.1.1.2", index = true)
    private Long topSniAttrPortNo;
    @SnmpProperty(table = "sni", oid = "1.3.6.1.4.1.32285.11.2.3.2.1.1.3", writable = true, type = "Integer32")
    private Integer topSniAttrFlowCtrlEnable;
    @SnmpProperty(table = "sni", oid = "1.3.6.1.4.1.32285.11.2.3.2.1.1.4", writable = true, type = "Integer32")
    private Integer topSniAttrIngressRate;
    @SnmpProperty(table = "sni", oid = "1.3.6.1.4.1.32285.11.2.3.2.1.1.5", writable = true, type = "Integer32")
    private Integer topSniAttrEgressRate;
    @SnmpProperty(table = "sni", oid = "1.3.6.1.4.1.32285.11.2.3.2.1.1.6")
    private Long topSniAttrActualSpeed;
    @SnmpProperty(table = "sni", oid = "1.3.6.1.4.1.32285.11.2.3.2.1.1.7")
    private Integer topSniAttrPortType; // 端口面板图中的类型  geCopper(1) geFiber(2) xeFiber(3)

    private Integer stpPortEnabled;
    private Integer stpPortRstpProtocolMigration;
    // SNI端口展示名称
    private String sniDisplayName;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getSniId() {
        return sniId;
    }

    public void setSniId(Long sniId) {
        this.sniId = sniId;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Long getSniIndex() {
        if (sniIndex == null) {
            sniIndex = new EponIndex(slotNo.intValue(), sniNo.intValue()).getSniIndex();
        }
        return sniIndex;
    }

    public void setSniIndex(Long sniIndex) {
        this.sniIndex = sniIndex;
        slotNo = topSniAttrCardNo = EponIndex.getSlotNo(sniIndex);
        sniNo = topSniAttrPortNo = EponIndex.getSniNo(sniIndex);
    }

    public String getSniPortName() {
        return sniPortName;
    }

    public void setSniPortName(String sniPortName) {
        this.sniPortName = sniPortName;
    }

    public Integer getSniOperationStatus() {
        return sniOperationStatus;
    }

    public void setSniOperationStatus(Integer sniOperationStatus) {
        this.sniOperationStatus = sniOperationStatus;
    }

    public Integer getSniAdminStatus() {
        return sniAdminStatus;
    }

    public void setSniAdminStatus(Integer sniAdminStatus) {
        this.sniAdminStatus = sniAdminStatus;
    }

    public Integer getSniMediaType() {
        return sniMediaType;
    }

    public void setSniMediaType(Integer sniMediaType) {
        this.sniMediaType = sniMediaType;
    }

    public Integer getSniAutoNegotiationStatus() {
        return sniAutoNegotiationStatus;
    }

    public void setSniAutoNegotiationStatus(Integer sniAutoNegotiationStatus) {
        this.sniAutoNegotiationStatus = sniAutoNegotiationStatus;
    }

    public Integer getSniAutoNegotiationMode() {
        return sniAutoNegotiationMode;
    }

    public void setSniAutoNegotiationMode(Integer sniAutoNegotiationMode) {
        this.sniAutoNegotiationMode = sniAutoNegotiationMode;
    }

    public Integer getSniPerfStats15minuteEnable() {
        return sniPerfStats15minuteEnable;
    }

    public void setSniPerfStats15minuteEnable(Integer sniPerfStats15minuteEnable) {
        this.sniPerfStats15minuteEnable = sniPerfStats15minuteEnable;
    }

    public Integer getSniPerfStats24hourEnable() {
        return sniPerfStats24hourEnable;
    }

    public void setSniPerfStats24hourEnable(Integer sniPerfStats24hourEnable) {
        this.sniPerfStats24hourEnable = sniPerfStats24hourEnable;
    }

    public Long getSniLastStatusChangeTime() {
        return sniLastStatusChangeTime;
    }

    public void setSniLastStatusChangeTime(Long sniLastStatusChangeTime) {
        this.sniLastStatusChangeTime = sniLastStatusChangeTime;
    }

    public Long getSniMacAddrLearnMaxNum() {
        return sniMacAddrLearnMaxNum;
    }

    public void setSniMacAddrLearnMaxNum(Long sniMacAddrLearnMaxNum) {
        this.sniMacAddrLearnMaxNum = sniMacAddrLearnMaxNum;
    }

    public Integer getSniIsolationEnable() {
        return sniIsolationEnable;
    }

    public void setSniIsolationEnable(Integer sniIsolationEnable) {
        this.sniIsolationEnable = sniIsolationEnable;
    }

    public Integer getTopSniAttrFlowCtrlEnable() {
        return topSniAttrFlowCtrlEnable;
    }

    public void setTopSniAttrFlowCtrlEnable(Integer topSniAttrFlowCtrlEnable) {
        this.topSniAttrFlowCtrlEnable = topSniAttrFlowCtrlEnable;
    }

    public Integer getTopSniAttrIngressRate() {
        return topSniAttrIngressRate;
    }

    public void setTopSniAttrIngressRate(Integer topSniAttrIngressRate) {
        this.topSniAttrIngressRate = topSniAttrIngressRate;
    }

    public Integer getTopSniAttrEgressRate() {
        return topSniAttrEgressRate;
    }

    public void setTopSniAttrEgressRate(Integer topSniAttrEgressRate) {
        this.topSniAttrEgressRate = topSniAttrEgressRate;
    }

    public Long getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(Long deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
    }

    public Long getSniNo() {
        return sniNo;
    }

    public void setSniNo(Long sniNo) {
        this.sniNo = sniNo;
    }

    public Long getTopSniAttrCardNo() {
        return topSniAttrCardNo;
    }

    public void setTopSniAttrCardNo(Long topSniAttrCardNo) {
        this.topSniAttrCardNo = topSniAttrCardNo;
    }

    public Long getTopSniAttrPortNo() {
        return topSniAttrPortNo;
    }

    public void setTopSniAttrPortNo(Long topSniAttrPortNo) {
        this.topSniAttrPortNo = topSniAttrPortNo;
    }

    public Long getTopSniAttrActualSpeed() {
        return topSniAttrActualSpeed;
    }

    public void setTopSniAttrActualSpeed(Long topSniAttrActualSpeed) {
        this.topSniAttrActualSpeed = topSniAttrActualSpeed;
    }

    public Integer getTopSniAttrPortType() {
        return topSniAttrPortType;
    }

    public void setTopSniAttrPortType(Integer topSniAttrPortType) {
        this.topSniAttrPortType = topSniAttrPortType;
    }

    public Integer getStpPortEnabled() {
        return stpPortEnabled;
    }

    public void setStpPortEnabled(Integer stpPortEnabled) {
        this.stpPortEnabled = stpPortEnabled;
    }

    public Integer getStpPortRstpProtocolMigration() {
        return stpPortRstpProtocolMigration;
    }

    public void setStpPortRstpProtocolMigration(Integer stpPortRstpProtocolMigration) {
        this.stpPortRstpProtocolMigration = stpPortRstpProtocolMigration;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public String getSniOperationStatusString() {
        return sniOperationStatusString;
    }

    public void setSniOperationStatusString(String sniOperationStatusString) {
        this.sniOperationStatusString = sniOperationStatusString;
    }

    public String getSniAdminStatusString() {
        return sniAdminStatusString;
    }

    public void setSniAdminStatusString(String sniAdminStatusString) {
        this.sniAdminStatusString = sniAdminStatusString;
    }

    public String getSniMediaTypeString() {
        return sniMediaTypeString;
    }

    public void setSniMediaTypeString(String sniMediaTypeString) {
        this.sniMediaTypeString = sniMediaTypeString;
    }

    public String getSniPort() {
        return sniPort;
    }

    public void setSniPort(String sniPort) {
        this.sniPort = sniPort;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getSniDisplayName() {
        if (sniDisplayName == null && sniIndex != null) {
            sniDisplayName = EponIndex.getPortStringByIndex(sniIndex).toString();
        }
        return sniDisplayName;
    }

    public void setSniDisplayName(String sniDisplayName) {
        this.sniDisplayName = sniDisplayName;
    }

    public Integer getSniPortType() {
        return sniPortType;
    }

    public void setSniPortType(Integer sniPortType) {
        this.sniPortType = sniPortType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSniAttribute [entityId=");
        builder.append(entityId);
        builder.append(", sniId=");
        builder.append(sniId);
        builder.append(", slotId=");
        builder.append(slotId);
        builder.append(", sniIndex=");
        builder.append(sniIndex);
        builder.append(", deviceNo=");
        builder.append(deviceNo);
        builder.append(", slotNo=");
        builder.append(slotNo);
        builder.append(", sniNo=");
        builder.append(sniNo);
        builder.append(", sniPortName=");
        builder.append(sniPortName);
        builder.append(", sniOperationStatus=");
        builder.append(sniOperationStatus);
        builder.append(", sniAdminStatus=");
        builder.append(sniAdminStatus);
        builder.append(", sniMediaType=");
        builder.append(sniMediaType);
        builder.append(", sniAutoNegotiationStatus=");
        builder.append(sniAutoNegotiationStatus);
        builder.append(", sniAutoNegotiationMode=");
        builder.append(sniAutoNegotiationMode);
        builder.append(", sniPerfStats15minuteEnable=");
        builder.append(sniPerfStats15minuteEnable);
        builder.append(", sniPerfStats24hourEnable=");
        builder.append(sniPerfStats24hourEnable);
        builder.append(", sniLastStatusChangeTime=");
        builder.append(sniLastStatusChangeTime);
        builder.append(", sniMacAddrLearnMaxNum=");
        builder.append(sniMacAddrLearnMaxNum);
        builder.append(", sniIsolationEnable=");
        builder.append(sniIsolationEnable);
        builder.append(", topSniAttrCardNo=");
        builder.append(topSniAttrCardNo);
        builder.append(", topSniAttrPortNo=");
        builder.append(topSniAttrPortNo);
        builder.append(", topSniAttrFlowCtrlEnable=");
        builder.append(topSniAttrFlowCtrlEnable);
        builder.append(", topSniAttrIngressRate=");
        builder.append(topSniAttrIngressRate);
        builder.append(", topSniAttrEgressRate=");
        builder.append(topSniAttrEgressRate);
        builder.append(", topSniAttrActualSpeed=");
        builder.append(topSniAttrActualSpeed);
        builder.append(", topSniAttrPortType=");
        builder.append(topSniAttrPortType);
        builder.append("]");
        return builder.toString();
    }
}
