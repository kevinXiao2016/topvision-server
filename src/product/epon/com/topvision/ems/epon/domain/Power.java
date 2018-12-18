/***********************************************************************
 * $Id: Power.java,v1.0 2011-10-13 上午08:19:25 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.ems.epon.olt.domain.OltPowerAttribute;
import com.topvision.ems.epon.olt.domain.OltPowerStatus;



/**
 * 电源属性
 * 
 * @author zhanglongyang
 * @created @2011-10-13-上午08:19:25
 * 
 */
@SuppressWarnings("rawtypes")
public class Power implements Serializable, Comparable {
    private static final long serialVersionUID = 4869020535800598435L;

    private Long powerCardId;
    private Long powerCardNum;
    private Long powerCardIndex;
    private Long powerCardRealIndex;
    private String powerCardName;
    private String topSysPowerSupplyType;
    private Integer topSysPowerSupplyACVoltage;
    private Integer topSysPowerSupplyACTemperature;
    private Integer powerCardAlarmStatus;
    private Timestamp changeTime;
    private Integer powerCardPresenceStatus;
    public static final String[] POWERSUPPLYTYPE = { "", "powerDC", "powerAC" };

    public void setAttribute(OltPowerAttribute oltPowerAttribute, OltPowerStatus oltPowerStatus) {
        this.powerCardName = oltPowerAttribute.getPowerCardName();
        // TODO 由于8601的电源类型数据存在问题，故此处进行特殊处理
        this.topSysPowerSupplyType = POWERSUPPLYTYPE[oltPowerAttribute.getTopSysPowerSupplyType() > 2 ? 2
                : oltPowerAttribute.getTopSysPowerSupplyType()];
        this.topSysPowerSupplyACVoltage = oltPowerAttribute.getTopSysPowerSupplyACVoltage();
        this.topSysPowerSupplyACTemperature = oltPowerStatus.getTopSysPowerSupplyACTemperature();
        this.powerCardAlarmStatus = oltPowerStatus.getPowerCardAlarmStatus();
        this.changeTime = oltPowerStatus.getChangeTime();
        this.powerCardPresenceStatus = oltPowerStatus.getPowerCardPresenceStatus();
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
     * @return the powerCardNum
     */
    public Long getPowerCardNum() {
        return powerCardNum;
    }

    /**
     * @param powerCardNum
     *            the powerCardNum to set
     */
    public void setPowerCardNum(Long powerCardNum) {
        this.powerCardNum = powerCardNum;
    }

    /**
     * @return the powerCardIndex
     */
    public Long getPowerCardIndex() {
        return powerCardIndex;
    }

    /**
     * @param powerCardIndex
     *            the powerCardIndex to set
     */
    public void setPowerCardIndex(Long powerCardIndex) {
        this.powerCardIndex = powerCardIndex;
    }

    /**
     * @return the powerCardRealIndex
     */
    public Long getPowerCardRealIndex() {
        return powerCardRealIndex;
    }

    /**
     * @param powerCardRealIndex
     *            the powerCardRealIndex to set
     */
    public void setPowerCardRealIndex(Long powerCardRealIndex) {
        this.powerCardRealIndex = powerCardRealIndex;
    }

    /**
     * @return the powerCardName
     */
    public String getPowerCardName() {
        return powerCardName;
    }

    /**
     * @param powerCardName
     *            the powerCardName to set
     */
    public void setPowerCardName(String powerCardName) {
        this.powerCardName = powerCardName;
    }

    /**
     * @return the topSysPowerSupplyType
     */
    public String getTopSysPowerSupplyType() {
        return topSysPowerSupplyType;
    }

    /**
     * @param topSysPowerSupplyType
     *            the topSysPowerSupplyType to set
     */
    public void setTopSysPowerSupplyType(String topSysPowerSupplyType) {
        this.topSysPowerSupplyType = topSysPowerSupplyType;
    }

    /**
     * @return the topSysPowerSupplyACVoltage
     */
    public Integer getTopSysPowerSupplyACVoltage() {
        return topSysPowerSupplyACVoltage;
    }

    /**
     * @param topSysPowerSupplyACVoltage
     *            the topSysPowerSupplyACVoltage to set
     */
    public void setTopSysPowerSupplyACVoltage(Integer topSysPowerSupplyACVoltage) {
        this.topSysPowerSupplyACVoltage = topSysPowerSupplyACVoltage;
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
     * Compares this object with the specified object for order. Returns a negative integer, zero,
     * or a positive integer as this object is less than, equal to, or greater than the specified
     * object.
     * <p/>
     * <p>
     * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>. (This implies that
     * <tt>x.compareTo(y)</tt> must throw an exception iff <tt>y.compareTo(x)</tt> throws an
     * exception.)
     * <p/>
     * <p>
     * The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p/>
     * <p>
     * Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt> implies that
     * <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for all <tt>z</tt>.
     * <p/>
     * <p>
     * It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>. Generally speaking, any class that implements
     * the <tt>Comparable</tt> interface and violates this condition should clearly indicate this
     * fact. The recommended language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p/>
     * <p>
     * In the foregoing description, the notation <tt>sgn(</tt><i>expression</i><tt>)</tt>
     * designates the mathematical <i>signum</i> function, which is defined to return one of
     * <tt>-1</tt>, <tt>0</tt>, or <tt>1</tt> according to whether the value of <i>expression</i> is
     * negative, zero or positive.
     * 
     * @param o
     *            the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal
     *         to, or greater than the specified object.
     * @throws ClassCastException
     *             if the specified object's type prevents it from being compared to this object.
     */
    public int compareTo(Object o) {
        Power power = (Power) o;
        if (this.getPowerCardIndex() > power.getPowerCardIndex()) {
            return 1;
        } else if (this.getPowerCardIndex().equals(power.getPowerCardIndex())) {
            return 0;
        } else {
            return -1;
        }
    }
}
