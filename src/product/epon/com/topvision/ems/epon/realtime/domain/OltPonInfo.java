/***********************************************************************
 * $Id: OltPonInfo.java,v1.0 2014-7-12 下午4:31:03 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-7-12-下午4:31:03
 *
 */
@TableProperty(tables = { "default", "ponInfo" })
public class OltPonInfo implements AliasesSuperType {
    private static final long serialVersionUID = 394906969511429338L;

    private Long entityId;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.5.1.1", type = "Integer32", index = true)
    private Integer slotIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.5.1.2", type = "Integer32", index = true)
    private Integer ponIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.5.1.12", type = "Integer32")
    private Long txPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.5.1.13", type = "Integer32")
    private Long rvPower;
    private String ponLocation;

    @SnmpProperty(table = "ponInfo", oid = "1.3.6.1.4.1.17409.2.3.3.1.1.1", type = "Integer32", index = true)
    private Integer deviceIndex;
    @SnmpProperty(table = "ponInfo", oid = "1.3.6.1.4.1.17409.2.3.3.1.1.2", type = "Integer32", index = true)
    private Integer cardIndex;
    @SnmpProperty(table = "ponInfo", oid = "1.3.6.1.4.1.17409.2.3.3.1.1.3", type = "Integer32", index = true)
    private Integer portIndex;
    @SnmpProperty(table = "ponInfo", oid = "1.3.6.1.4.1.17409.2.3.3.1.1.5", type = "Integer32")
    private Integer operationStatus;
    @SnmpProperty(table = "ponInfo", oid = "1.3.6.1.4.1.17409.2.3.3.1.1.8", type = "Integer32")
    private Integer downLinkCount;
    @SnmpProperty(table = "ponInfo", oid = "1.3.6.1.4.1.17409.2.3.3.1.1.15", type = "Integer32")
    private Integer perfStatsEnable;

    private Double portInSpeed;
    private Double portOutSpeed;
    private Double transPower;
    private Double recvPower;
    private int cmNum;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(Integer slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Integer getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Integer ponIndex) {
        this.ponIndex = ponIndex;
        this.ponLocation = this.slotIndex + "/" + ponIndex;
    }

    public Long getTxPower() {
        return txPower;
    }

    public void setTxPower(Long txPower) {
        this.txPower = txPower;
    }

    public Double getTransPower() {
        return transPower;
    }

    public void setTransPower(Double transPower) {
        this.transPower = transPower;
    }

    public Integer getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Integer deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public Integer getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(Integer cardIndex) {
        this.cardIndex = cardIndex;
    }

    public Integer getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Integer portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }

    public Integer getDownLinkCount() {
        return downLinkCount;
    }

    public void setDownLinkCount(Integer downLinkCount) {
        this.downLinkCount = downLinkCount;
    }

    public Double getPortInSpeed() {
        return portInSpeed;
    }

    public void setPortInSpeed(Double portInSpeed) {
        this.portInSpeed = portInSpeed;
    }

    public Double getPortOutSpeed() {
        return portOutSpeed;
    }

    public void setPortOutSpeed(Double portOutSpeed) {
        this.portOutSpeed = portOutSpeed;
    }

    public String getPonLocation() {
        return ponLocation;
    }

    public void setPonLocation(String ponLocation) {
        this.ponLocation = ponLocation;
    }

    public int getCmNum() {
        return cmNum;
    }

    public void addCmNum(int cmNum) {
        this.cmNum += cmNum;
    }

    public Integer getPerfStatsEnable() {
        return perfStatsEnable;
    }

    public void setPerfStatsEnable(Integer perfStatsEnable) {
        this.perfStatsEnable = perfStatsEnable;
    }

    public Long getRvPower() {
        return rvPower;
    }

    public void setRvPower(Long rvPower) {
        this.rvPower = rvPower;
    }

    public Double getRecvPower() {
        return recvPower;
    }

    public void setRecvPower(Double recvPower) {
        this.recvPower = recvPower;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltPonInfo [entityId=");
        builder.append(entityId);
        builder.append(", slotIndex=");
        builder.append(slotIndex);
        builder.append(", ponIndex=");
        builder.append(ponIndex);
        builder.append(", txPower=");
        builder.append(txPower);
        builder.append(", rvPower=");
        builder.append(rvPower);
        builder.append(", ponLocation=");
        builder.append(ponLocation);
        builder.append(", deviceIndex=");
        builder.append(deviceIndex);
        builder.append(", cardIndex=");
        builder.append(cardIndex);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", operationStatus=");
        builder.append(operationStatus);
        builder.append(", downLinkCount=");
        builder.append(downLinkCount);
        builder.append(", perfStatsEnable=");
        builder.append(perfStatsEnable);
        builder.append(", portInSpeed=");
        builder.append(portInSpeed);
        builder.append(", portOutSpeed=");
        builder.append(portOutSpeed);
        builder.append(", transPower=");
        builder.append(transPower);
        builder.append(", recvPower=");
        builder.append(recvPower);
        builder.append(", cmNum=");
        builder.append(cmNum);
        builder.append("]");
        return builder.toString();
    }

}
