/***********************************************************************
 * $Id: OltPortOpticalInfo.java,v1.0 2016-4-12 下午1:49:50 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.portinfo.domain;

import java.util.Date;

import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.optical.domain.OltSniOptical;
import com.topvision.ems.epon.realtime.domain.OltPortSpeedInfo;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author flack
 * @created @2016-4-12-下午1:49:50
 *
 */
public class OltPortOpticalInfo implements AliasesSuperType {
    private static final long serialVersionUID = 8581225916556371096L;

    private Long entityId;
    private Long slotId;
    // 端口Id
    private Long portId;
    // 端口索引
    private Long portIndex;
    // 端口名称
    private String portName;
    // 操作状态
    private Integer operationStatus;
    // 管理状态
    private Integer adminStatus;
    // 入方向实时速率
    private Long inCurrentRate;
    // 出方向实时速率
    private Long outCurrentRate;
    // 自协调模式
    private Integer autoNegoMode;
    // 端口流控使能
    private Integer flowCtrlEnable;
    // 上行方向限速
    private Integer upLimitRate;
    // 下行方向限速
    private Integer downLimitRate;
    // 光模块类型
    private Integer identifier;
    // 厂家名称
    private String vendorName;
    // 波长
    private Integer waveLength;
    // PN码
    private String vendorPN;
    // SN码
    private String vendorSN;
    // 温度
    private Long workingTemp;
    // 电压
    private Long workingVoltage;
    // 偏置电流
    private Long biasCurrent;
    // 发送功率
    private Long txPower;
    // 接收功率
    private Long rxPower;
    // 信息更新时间
    private Date modifyTime;
    // 端口号,如2/1
    private String portLocation;
    // 端口类型
    private Integer portType;
    // 板卡预配置类型
    private Integer slotType;
    // 性能统计使能
    private Integer perfStats;

    // 时间展示格式化后字符串
    private String modifyTimeStr;
    // 温度转换展示
    private Integer workTempDisplay;

    public OltPortOpticalInfo() {

    }

    public OltPortOpticalInfo(OltSniOptical sniOptical, OltPortSpeedInfo speedInfo) {
        this.entityId = sniOptical.getEntityId();
        this.portId = sniOptical.getSniId();
        this.portIndex = sniOptical.getPortIndex();
        this.identifier = sniOptical.getIdentifier();
        this.vendorName = sniOptical.getVendorName();
        this.waveLength = sniOptical.getWaveLength();
        this.vendorPN = sniOptical.getVendorPN();
        this.vendorSN = sniOptical.getVendorSN();
        // 对无效值进行处理,包括温度、电流、电压、发送功率、接收功率
        if (sniOptical.getWorkingTemp() != null && sniOptical.getWorkingTemp().equals(EponConstants.OPT_TEMP)) {
            this.workingTemp = null;
        } else {
            this.workingTemp = sniOptical.getWorkingTemp();
        }
        if (sniOptical.getWorkingVoltage() != null && sniOptical.getWorkingVoltage().equals(EponConstants.OPT_VOLTAGE)) {
            this.workingVoltage = null;
        } else {
            this.workingVoltage = sniOptical.getWorkingVoltage();
        }
        if (sniOptical.getBiasCurrent() != null && sniOptical.getBiasCurrent().equals(EponConstants.OPT_CURRENT)) {
            this.biasCurrent = null;
        } else {
            this.biasCurrent = sniOptical.getBiasCurrent();
        }
        if (sniOptical.getTxPower() != null && sniOptical.getTxPower().equals(EponConstants.TX_POWER)) {
            this.txPower = null;
        } else {
            this.txPower = sniOptical.getTxPower();
        }
        if (sniOptical.getRxPower() != null && sniOptical.getRxPower().equals(EponConstants.RE_POWER)) {
            this.rxPower = null;
        } else {
            this.rxPower = sniOptical.getRxPower();
        }
        if (speedInfo.getInBindWidth() == null || speedInfo.getOutBindWidth() == null) {
            this.inCurrentRate = -1L;
            this.outCurrentRate = -1L;
        } else {
            this.inCurrentRate = speedInfo.getInBindWidth();
            this.outCurrentRate = speedInfo.getOutBindWidth();
        }
    }

    public OltPortOpticalInfo(OltPonOptical ponOptical, OltPortSpeedInfo speedInfo) {
        this.entityId = ponOptical.getEntityId();
        this.portId = ponOptical.getPonId();
        this.portIndex = ponOptical.getPortIndex();
        this.identifier = ponOptical.getIdentifier();
        this.vendorName = ponOptical.getVendorName();
        this.waveLength = ponOptical.getWaveLength();
        this.vendorPN = ponOptical.getVendorPN();
        this.vendorSN = ponOptical.getVendorSN();
        // 对无效值进行处理,包括温度、电流、电压、发送功率、接收功率
        if (ponOptical.getWorkingTemp() != null && ponOptical.getWorkingTemp().equals(EponConstants.OPT_TEMP)) {
            this.workingTemp = null;
        } else {
            this.workingTemp = ponOptical.getWorkingTemp();
        }
        if (ponOptical.getWorkingVoltage() != null && ponOptical.getWorkingVoltage().equals(EponConstants.OPT_VOLTAGE)) {
            this.workingVoltage = null;
        } else {
            this.workingVoltage = ponOptical.getWorkingVoltage();
        }
        if (ponOptical.getBiasCurrent() != null && ponOptical.getBiasCurrent().equals(EponConstants.OPT_CURRENT)) {
            this.biasCurrent = null;
        } else {
            this.biasCurrent = ponOptical.getBiasCurrent();
        }
        if (ponOptical.getTxPower() != null && ponOptical.getTxPower().equals(EponConstants.TX_POWER)) {
            this.txPower = null;
        } else {
            this.txPower = ponOptical.getTxPower();
        }
        if (ponOptical.getRxPower() != null && ponOptical.getRxPower().equals(EponConstants.RE_POWER)) {
            this.rxPower = null;
        } else {
            this.rxPower = ponOptical.getRxPower();
        }
        if (speedInfo.getInBindWidth() == null || speedInfo.getOutBindWidth() == null) {
            this.inCurrentRate = -1L;
            this.outCurrentRate = -1L;
        } else {
            this.inCurrentRate = speedInfo.getInBindWidth();
            this.outCurrentRate = speedInfo.getOutBindWidth();
        }
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }

    public Integer getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(Integer adminStatus) {
        this.adminStatus = adminStatus;
    }

    public Long getInCurrentRate() {
        return inCurrentRate;
    }

    public void setInCurrentRate(Long inCurrentRate) {
        this.inCurrentRate = inCurrentRate;
    }

    public Long getOutCurrentRate() {
        return outCurrentRate;
    }

    public void setOutCurrentRate(Long outCurrentRate) {
        this.outCurrentRate = outCurrentRate;
    }

    public Integer getAutoNegoMode() {
        return autoNegoMode;
    }

    public void setAutoNegoMode(Integer autoNegoMode) {
        this.autoNegoMode = autoNegoMode;
    }

    public Integer getFlowCtrlEnable() {
        return flowCtrlEnable;
    }

    public void setFlowCtrlEnable(Integer flowCtrlEnable) {
        this.flowCtrlEnable = flowCtrlEnable;
    }

    public Integer getUpLimitRate() {
        return upLimitRate;
    }

    public void setUpLimitRate(Integer upLimitRate) {
        this.upLimitRate = upLimitRate;
    }

    public Integer getDownLimitRate() {
        return downLimitRate;
    }

    public void setDownLimitRate(Integer downLimitRate) {
        this.downLimitRate = downLimitRate;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Integer getWaveLength() {
        return waveLength;
    }

    public void setWaveLength(Integer waveLength) {
        this.waveLength = waveLength;
    }

    public String getVendorPN() {
        return vendorPN;
    }

    public void setVendorPN(String vendorPN) {
        this.vendorPN = vendorPN;
    }

    public String getVendorSN() {
        return vendorSN;
    }

    public void setVendorSN(String vendorSN) {
        this.vendorSN = vendorSN;
    }

    public Long getWorkingTemp() {
        return workingTemp;
    }

    public void setWorkingTemp(Long workingTemp) {
        this.workingTemp = workingTemp;
    }

    public Long getWorkingVoltage() {
        return workingVoltage;
    }

    public void setWorkingVoltage(Long workingVoltage) {
        this.workingVoltage = workingVoltage;
    }

    public Long getBiasCurrent() {
        return biasCurrent;
    }

    public void setBiasCurrent(Long biasCurrent) {
        this.biasCurrent = biasCurrent;
    }

    public Long getTxPower() {
        return txPower;
    }

    public void setTxPower(Long txPower) {
        this.txPower = txPower;
    }

    public Long getRxPower() {
        return rxPower;
    }

    public void setRxPower(Long rxPower) {
        this.rxPower = rxPower;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getPortLocation() {
        if (portLocation == null && portIndex != null) {
            portLocation = EponIndex.getPortStringByIndex(portIndex).toString();
        }
        return portLocation;
    }

    public void setPortLocation(String portLocation) {
        this.portLocation = portLocation;
    }

    public Integer getPortType() {
        return portType;
    }

    public void setPortType(Integer portType) {
        this.portType = portType;
    }

    public Integer getSlotType() {
        return slotType;
    }

    public void setSlotType(Integer slotType) {
        this.slotType = slotType;
    }

    public String getModifyTimeStr() {
        this.modifyTimeStr = DateUtils.format(modifyTime);
        return modifyTimeStr;
    }

    public void setModifyTimeStr(String modifyTimeStr) {
        this.modifyTimeStr = modifyTimeStr;
    }

    public Integer getWorkTempDisplay() {
        if (workingTemp != null && EponConstants.OPT_TEMP != workingTemp) {
            int tempValue = Math.round(workingTemp / 256);
            this.workTempDisplay = UnitConfigConstant.translateTemperature(tempValue);
        }
        return workTempDisplay;
    }

    public void setWorkTempDisplay(Integer workTempDisplay) {
        this.workTempDisplay = workTempDisplay;
    }

    public Integer getPerfStats() {
        return perfStats;
    }

    public void setPerfStats(Integer perfStats) {
        this.perfStats = perfStats;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltPortOpticalInfo [entityId=");
        builder.append(entityId);
        builder.append(", slotId=");
        builder.append(slotId);
        builder.append(", portId=");
        builder.append(portId);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", portName=");
        builder.append(portName);
        builder.append(", operationStatus=");
        builder.append(operationStatus);
        builder.append(", adminStatus=");
        builder.append(adminStatus);
        builder.append(", inCurrentRate=");
        builder.append(inCurrentRate);
        builder.append(", outCurrentRate=");
        builder.append(outCurrentRate);
        builder.append(", autoNegoMode=");
        builder.append(autoNegoMode);
        builder.append(", flowCtrlEnable=");
        builder.append(flowCtrlEnable);
        builder.append(", upLimitRate=");
        builder.append(upLimitRate);
        builder.append(", downLimitRate=");
        builder.append(downLimitRate);
        builder.append(", identifier=");
        builder.append(identifier);
        builder.append(", vendorName=");
        builder.append(vendorName);
        builder.append(", waveLength=");
        builder.append(waveLength);
        builder.append(", vendorPN=");
        builder.append(vendorPN);
        builder.append(", vendorSN=");
        builder.append(vendorSN);
        builder.append(", workingTemp=");
        builder.append(workingTemp);
        builder.append(", workingVoltage=");
        builder.append(workingVoltage);
        builder.append(", biasCurrent=");
        builder.append(biasCurrent);
        builder.append(", txPower=");
        builder.append(txPower);
        builder.append(", rxPower=");
        builder.append(rxPower);
        builder.append(", modifyTime=");
        builder.append(modifyTime);
        builder.append(", portLocation=");
        builder.append(portLocation);
        builder.append(", portType=");
        builder.append(portType);
        builder.append(", slotType=");
        builder.append(slotType);
        builder.append(", perfStats=");
        builder.append(perfStats);
        builder.append(", modifyTimeStr=");
        builder.append(modifyTimeStr);
        builder.append(", workTempDisplay=");
        builder.append(workTempDisplay);
        builder.append("]");
        return builder.toString();
    }
}
