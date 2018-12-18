/***********************************************************************
 * $Id: OltFanStatus.java,v1.0 2011-9-26 上午09:21:30 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.ems.epon.olt.constants.FanAlarmLevelEnum;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * 风扇实时状态
 * 
 * @author zhanglongyang
 * 
 */
@TableProperty(tables = { "default", "fan" })
public class OltFanStatus implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 785026203560961521L;
    private Long entityId;
    private Long fanCardId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.5.1.1.1", index = true)
    private Long deviceNo = 1L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.5.1.1.2", index = true)
    private Long fanNo;
    @SnmpProperty(table = "fan", oid = "1.3.6.1.4.1.32285.11.2.3.1.5.1.1.1", index = true)
    private Long topSysFanCardNo;
    @SnmpProperty(table = "fan", oid = "1.3.6.1.4.1.32285.11.2.3.1.5.1.1.2", index = true)
    private Long topSysFanNo = 1L;
    private Long fanCardIndex;
    @SnmpProperty(table = "fan", oid = "1.3.6.1.4.1.32285.11.2.3.1.5.1.1.4")
    private Integer topSysFanSpeed;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.5.1.1.4")
    private String alarmStatus;
    private Integer fanCardAlarmStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.5.1.1.6")
    private Integer fanCardPresenceStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.5.1.1.3")
    private Integer fanCardOperationStatus;
    private Timestamp changeTime;

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
     * @return the fanCardId
     */
    public Long getFanCardId() {
        return fanCardId;
    }

    /**
     * @param fanCardId
     *            the fanCardId to set
     */
    public void setFanCardId(Long fanCardId) {
        this.fanCardId = fanCardId;
    }

    /**
     * @return the fanCardIndex
     */
    public Long getFanCardIndex() {
        fanCardIndex = new EponIndex(fanNo.intValue()).getSlotIndex();
        return fanCardIndex;
    }

    /**
     * @param fanCardIndex
     *            the fanCardIndex to set
     */
    public void setFanCardIndex(Long fanCardIndex) {
        this.fanCardIndex = fanCardIndex;
        fanNo = topSysFanCardNo = EponIndex.getSlotNo(fanCardIndex);
    }

    /**
     * @return the topSysFanSpeed
     */
    public Integer getTopSysFanSpeed() {
        return topSysFanSpeed;
    }

    /**
     * @param topSysFanSpeed
     *            the topSysFanSpeed to set
     */
    public void setTopSysFanSpeed(Integer topSysFanSpeed) {
        this.topSysFanSpeed = topSysFanSpeed;
    }

    /**
     * @return the fanCardAlarmStatus
     */
    public Integer getFanCardAlarmStatus() {
        return fanCardAlarmStatus;
    }

    /**
     * @param fanCardAlarmStatus
     *            the fanCardAlarmStatus to set
     */
    public void setFanCardAlarmStatus(Integer fanCardAlarmStatus) {
        this.fanCardAlarmStatus = fanCardAlarmStatus;
    }

    /**
     * @return the fanCardPresenceStatus
     */
    public Integer getFanCardPresenceStatus() {
        return fanCardPresenceStatus;
    }

    /**
     * @param fanCardPresenceStatus
     *            the fanCardPresenceStatus to set
     */
    public void setFanCardPresenceStatus(Integer fanCardPresenceStatus) {
        this.fanCardPresenceStatus = fanCardPresenceStatus;
    }

    /**
     * @return the fanCardOperationStatus
     */
    public Integer getFanCardOperationStatus() {
        return fanCardOperationStatus;
    }

    /**
     * @param fanCardOperationStatus
     *            the fanCardOperationStatus to set
     */
    public void setFanCardOperationStatus(Integer fanCardOperationStatus) {
        this.fanCardOperationStatus = fanCardOperationStatus;
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

    public Long getFanNo() {
        return fanNo;
    }

    public void setFanNo(Long fanNo) {
        this.fanNo = fanNo;
        fanCardIndex = EponIndex.getSlotIndex(fanNo.intValue());
    }

    public Long getTopSysFanCardNo() {
        return topSysFanCardNo;
    }

    public void setTopSysFanCardNo(Long topSysFanCardNo) {
        this.topSysFanCardNo = topSysFanCardNo;
    }

    public Long getTopSysFanNo() {
        return topSysFanNo;
    }

    public void setTopSysFanNo(Long topSysFanNo) {
        this.topSysFanNo = topSysFanNo;
    }

    /**
     * @return the alarmStatus
     */
    public String getAlarmStatus() {
        return alarmStatus;
    }

    /**
     * @param alarmStatus
     *            the alarmStatus to set
     */
    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
        if (alarmStatus == null || alarmStatus.length() == 0) {
            fanCardAlarmStatus = 0;
            return;
        }
        byte[] bytes = alarmStatus.getBytes();
        Integer alarmStatusInteger = Integer.valueOf(bytes[0]);
        if (alarmStatusInteger.equals(128)) {
            fanCardAlarmStatus = FanAlarmLevelEnum.CRITICAL.getLevel();
        } else if (alarmStatusInteger.equals(64)) {
            fanCardAlarmStatus = FanAlarmLevelEnum.MAJOR.getLevel();
        } else if (alarmStatusInteger.equals(32)) {
            fanCardAlarmStatus = FanAlarmLevelEnum.MINOR.getLevel();
        } else if (alarmStatusInteger.equals(16)) {
            fanCardAlarmStatus = FanAlarmLevelEnum.WARNING.getLevel();
        } else {
            fanCardAlarmStatus = FanAlarmLevelEnum.NONE.getLevel();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltFanStatus");
        sb.append("{changeTime=").append(changeTime);
        sb.append(", entityId=").append(entityId);
        sb.append(", fanCardId=").append(fanCardId);
        sb.append(", deviceNo=").append(deviceNo);
        sb.append(", fanNo=").append(fanNo);
        sb.append(", topSysFanCardNo=").append(topSysFanCardNo);
        sb.append(", topSysFanNo=").append(topSysFanNo);
        sb.append(", fanCardIndex=").append(fanCardIndex);
        sb.append(", topSysFanSpeed=").append(topSysFanSpeed);
        sb.append(", fanCardAlarmStatus=").append(fanCardAlarmStatus);
        sb.append(", fanCardPresenceStatus=").append(fanCardPresenceStatus);
        sb.append(", fanCardOperationStatus=").append(fanCardOperationStatus);
        sb.append('}');
        return sb.toString();
    }
}
