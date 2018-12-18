package com.topvision.ems.epon.domain;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 历史性能数据Domian,兼容15分钟，24小时
 * 
 * @author Bravin
 * 
 */
@Alias(value = "historyPerformance")
public class HistoryPerforamnceIndex implements AliasesSuperType {
    private static final long serialVersionUID = -4574248499943326329L;
    // private String category;
    private Long entityId;
    private Long portIndex;
    private String monitorIndex;
    private Long value;
    private Long time;
    private Timestamp collectTime;
    private String ip;

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
     * @return the portIndex
     */
    public Long getPortIndex() {
        return portIndex;
    }

    /**
     * @param portIndex
     *            the portIndex to set
     */
    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    /**
     * @return the monitorIndex
     */
    public String getMonitorIndex() {
        return monitorIndex;
    }

    /**
     * @param monitorIndex
     *            the monitorIndex to set
     */
    public void setMonitorIndex(String monitorIndex) {
        this.monitorIndex = monitorIndex;
    }

    /**
     * @return the value
     */
    public Long getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(Long value) {
        this.value = value;
    }

    /**
     * @return the time
     */
    public Long getTime() {
        return time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setTime(Long time) {
        this.time = time;
        this.collectTime = new Timestamp(time);
    }

    /**
     * @return the collectTime
     */
    public Timestamp getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
        this.time = collectTime.getTime();
    }

}
