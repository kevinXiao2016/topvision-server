/***********************************************************************
 * $Id: OltSubDeviceInfo.java,v1.0 2014-7-12 下午5:05:08 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2014-7-12-下午5:05:08
 *
 */
@TableProperty(tables = { "default", "productInfo" })
public class OltSubDeviceInfo implements AliasesSuperType {
    private static final long serialVersionUID = 2863504899465113078L;

    private Long entityId;
    private Long onuDeviceIndex;
    private String onuName;
    private String macAddress;
    private Integer operationStatus;
    private Integer testDistance;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.1", type = "Integer32", index = true)
    private Long cardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.2", type = "Integer32", index = true)
    private Integer portIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.3", type = "Integer32", index = true)
    private Integer onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.4", type = "Integer32")
    private Integer recvOpticalPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.5", type = "Integer32")
    private Integer transOpticalPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.9", type = "Integer32")
    private Integer oltRecvPower;

    private Integer onuType;
    private Long onuId;

    private String displayType;
    private Long typeId;

    private int cmNum;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuDeviceIndex() {
        return EponIndex.getOnuIndexByMibIndex(cardIndex);
    }

    public void setOnuDeviceIndex(Long onuDeviceIndex) {
    	this.onuDeviceIndex = onuDeviceIndex;
    }

    public String getOnuName() {
        return onuName;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public void setOnuName(String onuName) {
        if (onuName == null || "".equals(onuName) || "NO_DESCRIPTION".equals(onuName)) {
            this.onuName = "ONU_"
                    + EponIndex.getOnuStringByIndex(EponIndex.getOnuIndexByMibIndex(this.cardIndex)).toString();
        } else {
            this.onuName = onuName;
        }
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }

    public Integer getTestDistance() {
        return testDistance;
    }

    public void setTestDistance(Integer testDistance) {
        this.testDistance = testDistance;
    }

    public Long getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(Long cardIndex) {
        this.cardIndex = cardIndex;
    }

    public Integer getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Integer portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Integer onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Integer getRecvOpticalPower() {
        return recvOpticalPower;
    }

    public void setRecvOpticalPower(Integer recvOpticalPower) {
        this.recvOpticalPower = recvOpticalPower;
    }

    public Integer getTransOpticalPower() {
        return transOpticalPower;
    }

    public void setTransOpticalPower(Integer transOpticalPower) {
        this.transOpticalPower = transOpticalPower;
    }

    public Integer getOltRecvPower() {
        return oltRecvPower;
    }

    public void setOltRecvPower(Integer oltRecvPower) {
        this.oltRecvPower = oltRecvPower;
    }

    public Integer getOnuType() {
        return onuType;
    }

    public void setOnuType(Integer onuType) {
        this.onuType = onuType;
        this.displayType = EponConstants.EPON_ONU_TYPE.get(onuType);
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public void addCmNum(Integer onlineNum) {
        this.cmNum += onlineNum;
    }

    public int getCmNum() {
        return cmNum;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public void setCmNum(int cmNum) {
        this.cmNum = cmNum;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSubDeviceInfo [entityId=");
        builder.append(entityId);
        builder.append(", onuDeviceIndex=");
        builder.append(onuDeviceIndex);
        builder.append(", onuName=");
        builder.append(onuName);
        builder.append(", macAddress=");
        builder.append(macAddress);
        builder.append(", operationStatus=");
        builder.append(operationStatus);
        builder.append(", testDistance=");
        builder.append(testDistance);
        builder.append(", cardIndex=");
        builder.append(cardIndex);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", recvOpticalPower=");
        builder.append(recvOpticalPower);
        builder.append(", transOpticalPower=");
        builder.append(transOpticalPower);
        builder.append(", oltRecvPower=");
        builder.append(oltRecvPower);
        builder.append(", onuType=");
        builder.append(onuType);
        builder.append(", displayType=");
        builder.append(displayType);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", cmNum=");
        builder.append(cmNum);
        builder.append("]");
        return builder.toString();
    }

}
