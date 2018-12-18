/***********************************************************************
 * $Id: SystemMonitor.java,v1.0 2014-6-24 上午9:07:15 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin.domain;

import java.util.Date;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Victor
 * @created @2014-6-24-上午9:07:15
 *
 */
public class SystemMonitor implements AliasesSuperType {
    private static final long serialVersionUID = -7304213104574261545L;
    private Date collectTime;
    private double cpu;
    private long heapMemory;
    private long nonHeapMemory;
    private long disk;
    private long threadCount;
    private long diskReads;
    private long diskWrites;

    /**
     * @return the collectTime
     */
    public Date getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    /**
     * @return the cpu
     */
    public double getCpu() {
        return cpu;
    }

    /**
     * @param cpu
     *            the cpu to set
     */
    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    /**
     * @return the heapMemory
     */
    public long getHeapMemory() {
        return heapMemory;
    }

    /**
     * @param heapMemory
     *            the heapMemory to set
     */
    public void setHeapMemory(long heapMemory) {
        this.heapMemory = heapMemory;
    }

    /**
     * @return the nonHeapMemory
     */
    public long getNonHeapMemory() {
        return nonHeapMemory;
    }

    /**
     * @param nonHeapMemory
     *            the nonHeapMemory to set
     */
    public void setNonHeapMemory(long nonHeapMemory) {
        this.nonHeapMemory = nonHeapMemory;
    }

    /**
     * @return the disk
     */
    public long getDisk() {
        return disk;
    }

    /**
     * @param disk
     *            the disk to set
     */
    public void setDisk(long disk) {
        this.disk = disk;
    }

    /**
     * @return the threadCount
     */
    public long getThreadCount() {
        return threadCount;
    }

    /**
     * @param threadCount
     *            the threadCount to set
     */
    public void setThreadCount(long threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SystemMonitor [collectTime=");
        builder.append(collectTime);
        builder.append(", cpu=");
        builder.append(cpu);
        builder.append(", heapMemory=");
        builder.append(heapMemory);
        builder.append(", nonHeapMemory=");
        builder.append(nonHeapMemory);
        builder.append(", disk=");
        builder.append(disk);
        builder.append(", threadCount=");
        builder.append(threadCount);
        builder.append("]");
        return builder.toString();
    }

    /**
     * @return the diskReads
     */
    public long getDiskReads() {
        return diskReads;
    }

    /**
     * @param diskReads
     *            the diskReads to set
     */
    public void setDiskReads(long diskReads) {
        this.diskReads = diskReads;
    }

    /**
     * @return the diskWrites
     */
    public long getDiskWrites() {
        return diskWrites;
    }

    /**
     * @param diskWrites
     *            the diskWrites to set
     */
    public void setDiskWrites(long diskWrites) {
        this.diskWrites = diskWrites;
    }
}
