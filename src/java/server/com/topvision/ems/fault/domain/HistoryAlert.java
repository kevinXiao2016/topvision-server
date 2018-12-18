package com.topvision.ems.fault.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.topvision.framework.common.DateUtils;

public class HistoryAlert extends Alert {
    private static final long serialVersionUID = 4239200628285906648L;
    private String clearUser;
    private Timestamp clearTime;
    private String clearTimeStr;
    private String clearMessage;

    /**
     * 
     * @return clearMessage
     */
    public String getClearMessage() {
        return clearMessage;
    }

    /**
     * 
     * @return clearTime
     */
    public Timestamp getClearTime() {
        return clearTime;
    }

    /**
     * 
     * @return clearUser
     */
    public String getClearUser() {
        return clearUser;
    }

    /**
     * 
     * @param alert
     */
    public void setAlert(Alert alert) {
        setAlertId(alert.getAlertId());
        if (alert.getConfirmUser() == null) {
            // 告警清除时处于无确认状态
            setConfirmMessage(alert.getClearMessage());
            setConfirmTime(alert.getClearTime());
            setConfirmUser(alert.getClearUser());
        } else {
            // 告警清除时处于已确认状态
            setConfirmMessage(alert.getConfirmMessage());
            setConfirmTime(alert.getConfirmTime());
            setConfirmUser(alert.getConfirmUser());
        }
        setClearUser(alert.getClearUser());
        setClearTime(alert.getClearTime());
        setClearMessage(alert.getClearMessage());
        setFirstTime(alert.getFirstTime());
        setHappenTimes(alert.getHappenTimes());
        setHost(alert.getHost());
        setLastTime(alert.getLastTime());
        setLevelId(alert.getLevelId());
        setMessage(alert.getMessage());
        setName(alert.getName());
        setSource(alert.getSource());
        setMonitorId(alert.getMonitorId());
        setEntityId(alert.getEntityId());
        setTypeId(alert.getTypeId());
        setUserObject(alert.getUserObject());
    }

    /**
     * 
     * @param clearMessage
     */
    public void setClearMessage(String clearMessage) {
        this.clearMessage = clearMessage;
    }

    /**
     * 
     * @param clearTime
     */
    public void setClearTime(Timestamp clearTime) {
        this.clearTime = clearTime;
    }

    /**
     * 
     * @param clearUser
     */
    public void setClearUser(String clearUser) {
        this.clearUser = clearUser;
    }

    /**
     * @return the clearTimeStr
     */
    public String getClearTimeStr() {
        if (clearTimeStr == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            clearTimeStr = sdf.format(clearTime);
        }
        return clearTimeStr;
    }

    /**
     * @param clearTimeStr
     *            the clearTimeStr to set
     */
    public void setClearTimeStr(String clearTimeStr) {
        this.clearTimeStr = DateUtils.format(this.getClearTime());
    }
}
