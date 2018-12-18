/***********************************************************************
 * $Id: OltSlotInfo.java,v1.0 2014-7-12 下午2:05:42 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author flack
 * @created @2014-7-12-下午2:05:42
 *
 */
@TableProperty(tables = { "default", "nscrtv" })
public class OltSlotInfo implements AliasesSuperType {
    private static final long serialVersionUID = -9168564271278662769L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.1", type = "Integer32", index = true)
    private Integer slotIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.2", type = "Integer32")
    private Integer preConfigType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.3", type = "Integer32")
    private Integer actualType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.4", type = "Integer32")
    private Long cpuUseRatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.5", type = "Integer32")
    private Long totalMemSize;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.6", type = "Integer32")
    private Long freeMemSize;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.7", type = "Integer32")
    private Long totalFlashSize;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.8", type = "Integer32")
    private Long freeFlashSize;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.9", type = "Integer32")
    private Integer currentTemp;

    @SnmpProperty(table = "nscrtv", oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.1,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.1", index = true)
    private Integer deviceIndex = 1;
    @SnmpProperty(table = "nscrtv", oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.2,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.2", index = true)
    private Integer cardIndex;
    @SnmpProperty(table = "nscrtv", oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.5,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.5", type = "Integer32")
    private Integer operationStatus;
    @SnmpProperty(table = "nscrtv", oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.15,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.15", type = "Integer32")
    private Integer presenceStatus;
    @SnmpProperty(table = "nscrtv", oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.10,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.10")
    private Long slotUptime;
    @SnmpProperty(table = "nscrtv", oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.9,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.9")
    private String slotVersion;

    private Integer memUseRatio;
    private Integer flashUseRatio;
    private int cmNum;//default 0
    //转换后的温度值
    private Integer tempValue;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Integer deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public Integer getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(Integer slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }

    public Integer getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(Integer cardIndex) {
        this.cardIndex = cardIndex;
    }

    public Integer getPreConfigType() {
        return preConfigType;
    }

    public void setPreConfigType(Integer preConfigType) {
        this.preConfigType = preConfigType;
    }

    public Integer getActualType() {
        return actualType;
    }

    public void setActualType(Integer actualType) {
        this.actualType = actualType;
    }

    public Long getCpuUseRatio() {
        return cpuUseRatio;
    }

    public void setCpuUseRatio(Long cpuUseRatio) {
        this.cpuUseRatio = cpuUseRatio;
    }

    public Long getTotalMemSize() {
        return totalMemSize;
    }

    public void setTotalMemSize(Long totalMemSize) {
        this.totalMemSize = totalMemSize;
    }

    public Long getFreeMemSize() {
        return freeMemSize;
    }

    public void setFreeMemSize(Long freeMemSize) {
        this.freeMemSize = freeMemSize;
    }

    public Long getTotalFlashSize() {
        return totalFlashSize;
    }

    public void setTotalFlashSize(Long totalFlashSize) {
        this.totalFlashSize = totalFlashSize;
    }

    public Long getFreeFlashSize() {
        return freeFlashSize;
    }

    public void setFreeFlashSize(Long freeFlashSize) {
        this.freeFlashSize = freeFlashSize;
    }

    public Integer getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(Integer currentTemp) {
        this.currentTemp = currentTemp;
        if (currentTemp != null) {
            this.tempValue = UnitConfigConstant.translateTemperature(currentTemp);
        }
    }

    public Integer getPresenceStatus() {
        return presenceStatus;
    }

    public void setPresenceStatus(Integer presenceStatus) {
        this.presenceStatus = presenceStatus;
    }

    public Long getSlotUptime() {
        return slotUptime;
    }

    public void setSlotUptime(Long slotUptime) {
        this.slotUptime = slotUptime;
    }

    public String getSlotVersion() {
        return slotVersion;
    }

    public void setSlotVersion(String slotVersion) {
        this.slotVersion = slotVersion;
    }

    public Integer getMemUseRatio() {
        return memUseRatio;
    }

    public void setMemUseRatio(Integer memUseRatio) {
        this.memUseRatio = memUseRatio;
    }

    public Integer getFlashUseRatio() {
        return flashUseRatio;
    }

    public void setFlashUseRatio(Integer flashUseRatio) {
        this.flashUseRatio = flashUseRatio;
    }

    public void addCmNum(Integer cmNum) {
        this.cmNum += cmNum;
    }

    public int getCmNum() {
        return cmNum;
    }

    public Integer getTempValue() {
        return tempValue;
    }

    public void setTempValue(Integer tempValue) {
        this.tempValue = tempValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSlotInfo [entityId=");
        builder.append(entityId);
        builder.append(", slotIndex=");
        builder.append(slotIndex);
        builder.append(", preConfigType=");
        builder.append(preConfigType);
        builder.append(", actualType=");
        builder.append(actualType);
        builder.append(", cpuUseRatio=");
        builder.append(cpuUseRatio);
        builder.append(", totalMemSize=");
        builder.append(totalMemSize);
        builder.append(", freeMemSize=");
        builder.append(freeMemSize);
        builder.append(", totalFlashSize=");
        builder.append(totalFlashSize);
        builder.append(", freeFlashSize=");
        builder.append(freeFlashSize);
        builder.append(", currentTemp=");
        builder.append(currentTemp);
        builder.append(", deviceIndex=");
        builder.append(deviceIndex);
        builder.append(", cardIndex=");
        builder.append(cardIndex);
        builder.append(", operationStatus=");
        builder.append(operationStatus);
        builder.append(", presenceStatus=");
        builder.append(presenceStatus);
        builder.append(", slotUptime=");
        builder.append(slotUptime);
        builder.append(", slotVersion=");
        builder.append(slotVersion);
        builder.append(", memUseRatio=");
        builder.append(memUseRatio);
        builder.append(", flashUseRatio=");
        builder.append(flashUseRatio);
        builder.append("]");
        return builder.toString();
    }
}
