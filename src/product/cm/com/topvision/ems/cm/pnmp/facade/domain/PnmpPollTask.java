package com.topvision.ems.cm.pnmp.facade.domain;

import com.topvision.framework.snmp.SnmpParam;

import java.io.Serializable;

/**
 * @author jay
 * @created 15-3-3.
 */
public class PnmpPollTask implements Serializable {
    private static final long serialVersionUID = 3286404264964208289L;
    private Integer engineId;
    private Long taskId;
    private Long entityId;
    private Long cmcId;
    private Long cmcIndex;
    private String cmMac;
    private String cmIp;
    private Long statusIndex;
    private Integer statusValue;
    private SnmpParam cmtsSnmpParam;

    public SnmpParam getCmtsSnmpParam() {
        return cmtsSnmpParam;
    }

    public void setCmtsSnmpParam(SnmpParam cmtsSnmpParam) {
        this.cmtsSnmpParam = cmtsSnmpParam;
    }

    public Integer getEngineId() {
        return engineId;
    }

    public void setEngineId(Integer engineId) {
        this.engineId = engineId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getStatusIndex() {
        return statusIndex;
    }

    public void setStatusIndex(Long statusIndex) {
        this.statusIndex = statusIndex;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public String getCmIp() {
        return cmIp;
    }

    public void setCmIp(String cmIp) {
        this.cmIp = cmIp;
    }

    public Integer getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(Integer statusValue) {
		this.statusValue = statusValue;
	}

	public void setBaseTaskInfo(CmtsCm cmtsCm) {
        String ip = cmtsCm.getStatusIpAddress();
        if (ip == null || ip.equalsIgnoreCase("") || ip.equalsIgnoreCase("noSuchObject")) {
            ip = cmtsCm.getStatusInetAddress();
        }
        setEntityId(cmtsCm.getEntityId());
        setCmcId(cmtsCm.getCmcId());
        setCmcIndex(cmtsCm.getCmcIndex());
        setStatusIndex(cmtsCm.getStatusIndex());
        setStatusValue(cmtsCm.getStatusValue());
        setCmMac(cmtsCm.getStatusMacAddress());
        setCmIp(ip);
    }
}
