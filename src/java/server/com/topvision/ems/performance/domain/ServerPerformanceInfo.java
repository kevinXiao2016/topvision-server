package com.topvision.ems.performance.domain;

import java.text.DecimalFormat;

import com.topvision.framework.domain.BaseEntity;

public class ServerPerformanceInfo extends BaseEntity {

    private static final long serialVersionUID = -7966140024149018299L;

    /**
     * 磁盘总容量
     */
    private Long totalDiskSize;
    /**
     * 可使用容量
     */
    private Long freeDiskSize;
    /**
     * 磁盘已使用容量
     */
    private Long usedDiskSize;
    /**
     * 磁盘使用率
     */
    private float diskRatio;
    private String serverIp;

    public Long getTotalDiskSize() {
        return totalDiskSize;
    }

    public void setTotalDiskSize(Long totalDiskSize) {
        this.totalDiskSize = totalDiskSize;
    }

    public Long getFreeDiskSize() {
        return freeDiskSize;
    }

    public void setFreeDiskSize(Long freeDiskSize) {
        this.freeDiskSize = freeDiskSize;
    }

    public Long getUsedDiskSize() {
        return totalDiskSize - freeDiskSize;
    }

    public float getDiskRatio() {
        DecimalFormat df = new DecimalFormat("######0.00");
        String ratio = df.format((float) getUsedDiskSize() / (float) totalDiskSize * 100);
        return Float.valueOf(ratio);
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ServerPerformanceInfo [totalDiskSize=");
        builder.append(totalDiskSize);
        builder.append(", freeDiskSize=");
        builder.append(freeDiskSize);
        builder.append(", usedDiskSize=");
        builder.append(getUsedDiskSize());
        builder.append(", diskRatio=");
        builder.append(getDiskRatio());
        builder.append(", serverIp=");
        builder.append(serverIp);
        builder.append("]");
        return builder.toString();
    }

}
