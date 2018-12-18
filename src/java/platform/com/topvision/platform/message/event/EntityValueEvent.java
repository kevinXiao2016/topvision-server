/***********************************************************************
 * $Id: EntityValueEvent.java,v 1.1 Sep 7, 2009 11:31:43 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

import java.sql.Timestamp;

/**
 * @Create Date Sep 7, 2009 11:31:43 AM
 * 
 * @author kelers
 * 
 */
public class EntityValueEvent extends EmsEventObject<EntityValueListener> {
    private static final long serialVersionUID = -3186751269446925196L;
    private long entityId;
    private Double cpu;
    private Double mem;
    private Double vmem;
    private Double disk;
    private String sysUpTime;
    private Boolean state;
    private Integer delay;
    private Integer maxAlertLevel;
    private String maxAlertDesc;
    private Timestamp maxAlertTime;
    //alert id
    private Long alertId;

    /**
     * @param source
     */
    public EntityValueEvent(Object source) {
        super(source);
    }

    /**
     * @return the cpu
     */
    public final Double getCpu() {
        return cpu;
    }

    /**
     * @return the delay
     */
    public Integer getDelay() {
        return delay;
    }

    /**
     * @return the disk
     */
    public Double getDisk() {
        return disk;
    }

    /**
     * @return the entityId
     */
    public final long getEntityId() {
        return entityId;
    }

    /**
     * @return the maxAlertLevel
     */
    public final Integer getMaxAlertLevel() {
        return maxAlertLevel;
    }

    /**
     * @return the maxAlertDesc
     */
    public final String getMaxAlertDesc() {
        return maxAlertDesc;
    }

    /**
     * @return the mem
     */
    public final Double getMem() {
        return mem;
    }

    /**
     * @return the sysUpTime
     */
    public final String getSysUpTime() {
        return sysUpTime;
    }

    /**
     * @return the vmem
     */
    public final Double getVmem() {
        return vmem;
    }

    /**
     * @return the state
     */
    public final Boolean isState() {
        return state;
    }

    /**
     * @param cpu
     *            the cpu to set
     */
    public final void setCpu(Double cpu) {
        this.cpu = cpu;
    }

    /**
     * @param delay
     *            the delay to set
     */
    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    /**
     * @param disk
     *            the disk to set
     */
    public void setDisk(Double disk) {
        this.disk = disk;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public final void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    /**
     * @param maxAlertLevel
     *            the maxAlertLevel to set
     */
    public final void setMaxAlertLevel(Integer maxAlertLevel) {
        this.maxAlertLevel = maxAlertLevel;
    }

    /**
     * @param maxAlertDesc
     *            the maxAlertDesc to set
     */
    public final void setMaxAlertDesc(String maxAlertDesc) {
        this.maxAlertDesc = maxAlertDesc;
    }

    /**
     * @param mem
     *            the mem to set
     */
    public final void setMem(Double mem) {
        this.mem = mem;
    }

    /**
     * @param state
     *            the state to set
     */
    public final void setState(Boolean state) {
        this.state = state;
    }

    /**
     * @param sysUpTime
     *            the sysUpTime to set
     */
    public final void setSysUpTime(String sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    /**
     * @param vmem
     *            the vmem to set
     */
    public final void setVmem(Double vmem) {
        this.vmem = vmem;
    }

	public Long getAlertId() {
		return alertId;
	}

	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}

    public Boolean getState() {
        return state;
    }

    public Timestamp getMaxAlertTime() {
        return maxAlertTime;
    }

    public void setMaxAlertTime(Timestamp maxAlertTime) {
        this.maxAlertTime = maxAlertTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EntityValueEvent [entityId=");
        builder.append(entityId);
        builder.append(", cpu=");
        builder.append(cpu);
        builder.append(", mem=");
        builder.append(mem);
        builder.append(", vmem=");
        builder.append(vmem);
        builder.append(", disk=");
        builder.append(disk);
        builder.append(", sysUpTime=");
        builder.append(sysUpTime);
        builder.append(", state=");
        builder.append(state);
        builder.append(", delay=");
        builder.append(delay);
        builder.append(", maxAlertLevel=");
        builder.append(maxAlertLevel);
        builder.append(", maxAlertDesc=");
        builder.append(maxAlertDesc);
        builder.append(", maxAlertTime=");
        builder.append(maxAlertTime);
        builder.append(", alertId=");
        builder.append(alertId);
        builder.append("]");
        return builder.toString();
    }
}
