/**
 * 
 */
package com.topvision.ems.epon.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author kelers
 * 
 */
public class DeviceAlertRate extends BaseEntity {

    private static final long serialVersionUID = -8048685850074397929L;

    private long entityId;
    private String name;
    private String ip;

    private double alertRate;
    private long alertTimeLength;
    private long totalTimeLength;
    private List<TimeSegment> timeSegments = new ArrayList<TimeSegment>();

    public void addTimeSegment(TimeSegment segment) {
        timeSegments.add(segment);
    }

    public double getAlertRate() {
        return alertRate;
    }

    public long getAlertTimeLength() {
        return alertTimeLength;
    }

    public long getEntityId() {
        return entityId;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public List<TimeSegment> getTimeSegments() {
        return timeSegments;
    }

    public long getTotalTimeLength() {
        return totalTimeLength;
    }

    public void setAlertRate(double alertRate) {
        this.alertRate = alertRate;
    }

    public void setAlertTimeLength(long alertTimeLength) {
        this.alertTimeLength = alertTimeLength;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeSegments(List<TimeSegment> timeSegments) {
        this.timeSegments = timeSegments;
    }

    public void setTotalTimeLength(long totalTimeLength) {
        this.totalTimeLength = totalTimeLength;
    }

}
