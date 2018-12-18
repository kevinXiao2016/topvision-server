/***********************************************************************
 * $Id: OltPowerStatus.java,v1.0 2011-9-26 上午09:19:56 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * 电源实时状态
 * 
 * @author zhanglongyang
 * 
 */
@TableProperty(tables = { "default", "power" })
public class OltPowerStatus implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 2923518446131208721L;
    private Long entityId;
    private Long powerCardId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.4.1.1.1", index = true)
    private Long deviceNo = 1L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.4.1.1.2", index = true)
    private Long powerNo;
    @SnmpProperty(table = "power", oid = "1.3.6.1.4.1.32285.11.2.3.1.4.1.1.1", index = true)
    private Long topSysPowerCardNo;
    private Long powerCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.4.1.1.4")
    private String alarmStatus;
    private Integer powerCardAlarmStatus;
    @SnmpProperty(table = "power", oid = "1.3.6.1.4.1.32285.11.2.3.1.4.1.1.4")
    private Integer topSysPowerSupplyACTemperature;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.4.1.1.7")
    private Integer powerCardPresenceStatus;
    private Timestamp changeTime;

    //电源温度展示
    private Integer powerDisplayTemp;

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
     * @return the powerCardId
     */
    public Long getPowerCardId() {
        return powerCardId;
    }

    /**
     * @param powerCardId
     *            the powerCardId to set
     */
    public void setPowerCardId(Long powerCardId) {
        this.powerCardId = powerCardId;
    }

    /**
     * @return the powerCardIndex
     */
    public Long getPowerCardIndex() {
        powerCardIndex = new EponIndex(powerNo.intValue()).getSlotIndex();
        return powerCardIndex;
    }

    /**
     * @param powerCardIndex
     *            the powerCardIndex to set
     */
    public void setPowerCardIndex(Long powerCardIndex) {
        powerNo = topSysPowerCardNo = EponIndex.getSlotNo(powerCardIndex);
        this.powerCardIndex = powerCardIndex;
    }

    /**
     * @return the powerCardAlarmStatus
     */
    public Integer getPowerCardAlarmStatus() {
        return powerCardAlarmStatus;
    }

    /**
     * @param powerCardAlarmStatus
     *            the powerCardAlarmStatus to set
     */
    public void setPowerCardAlarmStatus(Integer powerCardAlarmStatus) {
        this.powerCardAlarmStatus = powerCardAlarmStatus;
    }

    /**
     * @return the topSysPowerSupplyACTemperature
     */
    public Integer getTopSysPowerSupplyACTemperature() {
        return topSysPowerSupplyACTemperature;
    }

    /**
     * @param topSysPowerSupplyACTemperature
     *            the topSysPowerSupplyACTemperature to set
     */
    public void setTopSysPowerSupplyACTemperature(Integer topSysPowerSupplyACTemperature) {
        this.topSysPowerSupplyACTemperature = topSysPowerSupplyACTemperature;
        if (topSysPowerSupplyACTemperature != null) {
            this.powerDisplayTemp = UnitConfigConstant.translateTemperature(topSysPowerSupplyACTemperature);
        }
    }

    /**
     * @return the powerCardPresenceStatus
     */
    public Integer getPowerCardPresenceStatus() {
        return powerCardPresenceStatus;
    }

    /**
     * @param powerCardPresenceStatus
     *            the powerCardPresenceStatus to set
     */
    public void setPowerCardPresenceStatus(Integer powerCardPresenceStatus) {
        this.powerCardPresenceStatus = powerCardPresenceStatus;
    }

    /**
     * @return the changeTime
     */
    public Timestamp getChangeTime() {
        return changeTime;
    }

    /**
     * @param changeTime
     *            the changeTime to set
     */
    public void setChangeTime(Timestamp changeTime) {
        this.changeTime = changeTime;
    }

    public Long getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(Long deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Long getPowerNo() {
        return powerNo;
    }

    public void setPowerNo(Long powerNo) {
        this.powerNo = powerNo;
        powerCardIndex = EponIndex.getSlotIndex(powerNo.intValue());
    }

    public Long getTopSysPowerCardNo() {
        return topSysPowerCardNo;
    }

    public void setTopSysPowerCardNo(Long topSysPowerCardNo) {
        this.topSysPowerCardNo = topSysPowerCardNo;
    }

    /**
     * @return the alarmStatus
     */
    public String getAlarmStatus() {
        return alarmStatus;
    }

    public Integer getPowerDisplayTemp() {
        return powerDisplayTemp;
    }

    public void setPowerDisplayTemp(Integer powerDisplayTemp) {
        this.powerDisplayTemp = powerDisplayTemp;
    }

    /**
     * @param alarmStatus
     *            the alarmStatus to set
     */
    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
        if (alarmStatus == null || alarmStatus.length() == 0) {
            powerCardAlarmStatus = 0;
        } else if (alarmStatus.equals("80")) {
            powerCardAlarmStatus = 1;
        } else if (alarmStatus.equals("40")) {
            powerCardAlarmStatus = 2;
        } else if (alarmStatus.equals("20")) {
            powerCardAlarmStatus = 3;
        } else if (alarmStatus.equals("10")) {
            powerCardAlarmStatus = 4;
        } else {
            powerCardAlarmStatus = 0;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltPowerStatus");
        sb.append("{changeTime=").append(changeTime);
        sb.append(", entityId=").append(entityId);
        sb.append(", powerCardId=").append(powerCardId);
        sb.append(", deviceNo=").append(deviceNo);
        sb.append(", powerNo=").append(powerNo);
        sb.append(", topSysPowerCardNo=").append(topSysPowerCardNo);
        sb.append(", powerCardIndex=").append(powerCardIndex);
        sb.append(", powerCardAlarmStatus=").append(powerCardAlarmStatus);
        sb.append(", topSysPowerSupplyACTemperature=").append(topSysPowerSupplyACTemperature);
        sb.append(", powerCardPresenceStatus=").append(powerCardPresenceStatus);
        sb.append('}');
        return sb.toString();
    }
}
