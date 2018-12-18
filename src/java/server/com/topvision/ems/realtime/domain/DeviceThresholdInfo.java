/**
 * 
 */
package com.topvision.ems.realtime.domain;

import com.topvision.ems.performance.domain.Threshold;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author niejun
 * 
 */
public class DeviceThresholdInfo extends BaseEntity {

    private static final long serialVersionUID = -6287599680082187131L;
    private long entityId;
    private int cpuCount;
    private Threshold[] cpuThresholds;
    private Threshold[] memThresholds;

    public int getCpuCount() {
        return cpuCount;
    }

    public Threshold[] getCpuThresholds() {
        return cpuThresholds;
    }

    public long getEntityId() {
        return entityId;
    }

    public Threshold[] getMemThresholds() {
        return memThresholds;
    }

    public void setCpuCount(int cpuCount) {
        this.cpuCount = cpuCount;
    }

    public void setCpuThresholds(Threshold[] cpuThresholds) {
        this.cpuThresholds = cpuThresholds;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setMemThresholds(Threshold[] memThresholds) {
        this.memThresholds = memThresholds;
    }

}
