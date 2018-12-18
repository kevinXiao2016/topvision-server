/***********************************************************************
 * $Id: Fan.java,v1.0 2011-10-13 上午08:18:22 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltFanStatus;



/**
 * 风扇属性
 * 
 * @author zhanglongyang
 * @created @2011-10-13-上午08:18:22
 * 
 */
@SuppressWarnings("rawtypes")
public class Fan implements Serializable, Comparable {
    private static final long serialVersionUID = -8034569263662531471L;

    private Long fanCardId;
    private Long fanCardNum;
    private Long fanCardIndex;
    private Long fanCardRealIndex;
    private String fanCardName;
    private Integer topSysFanSpeed;
    private Integer topSysFanSpeedControl;
    private Integer fanCardAlarmStatus;
    private Integer fanCardPresenceStatus;
    private Integer fanCardOperationStatus;
    private Timestamp changeTime;

    public void setAttribute(OltFanAttribute oltFanAttribute, OltFanStatus oltFanStatus) {
        this.fanCardName = oltFanAttribute.getFanCardName();
        this.topSysFanSpeed = oltFanStatus.getTopSysFanSpeed();
        this.topSysFanSpeedControl = oltFanAttribute.getTopSysFanSpeedControl();
        this.fanCardAlarmStatus = oltFanStatus.getFanCardAlarmStatus();
        this.fanCardPresenceStatus = oltFanStatus.getFanCardPresenceStatus();
        this.fanCardOperationStatus = oltFanStatus.getFanCardOperationStatus();
        this.changeTime = oltFanStatus.getChangeTime();
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
     * @return the fanCardNum
     */
    public Long getFanCardNum() {
        return fanCardNum;
    }

    /**
     * @param fanCardNum
     *            the fanCardNum to set
     */
    public void setFanCardNum(Long fanCardNum) {
        this.fanCardNum = fanCardNum;
    }

    /**
     * @return the fanCardIndex
     */
    public Long getFanCardIndex() {
        return fanCardIndex;
    }

    /**
     * @param fanCardIndex
     *            the fanCardIndex to set
     */
    public void setFanCardIndex(Long fanCardIndex) {
        this.fanCardIndex = fanCardIndex;
    }

    /**
     * @return the fanCardRealIndex
     */
    public Long getFanCardRealIndex() {
        return fanCardRealIndex;
    }

    /**
     * @param fanCardRealIndex
     *            the fanCardRealIndex to set
     */
    public void setFanCardRealIndex(Long fanCardRealIndex) {
        this.fanCardRealIndex = fanCardRealIndex;
    }

    /**
     * @return the fanCardName
     */
    public String getFanCardName() {
        return fanCardName;
    }

    /**
     * @param fanCardName
     *            the fanCardName to set
     */
    public void setFanCardName(String fanCardName) {
        this.fanCardName = fanCardName;
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
     * @return the topSysFanSpeedControl
     */
    public Integer getTopSysFanSpeedControl() {
        return topSysFanSpeedControl;
    }

    /**
     * @param topSysFanSpeedControl
     *            the topSysFanSpeedControl to set
     */
    public void setTopSysFanSpeedControl(Integer topSysFanSpeedControl) {
        this.topSysFanSpeedControl = topSysFanSpeedControl;
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
        Fan fan = (Fan) o;
        if (this.getFanCardIndex() > fan.getFanCardIndex()) {
            return 1;
        } else if (this.getFanCardIndex().equals(fan.getFanCardIndex())) {
            return 0;
        } else {
            return -1;
        }
    }
}
