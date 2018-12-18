package com.topvision.ems.network.domain;

import java.sql.Timestamp;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Level;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;

public class EntitySnap extends Entity implements AliasesSuperType {
    private static final long serialVersionUID = 3852357551724154646L;
    private String typeName;
    private Boolean state;
    private String sysUpTime;
    private Double cpu = -1.0;
    private Double mem = -1.0;
    private Double vmem = -1.0;
    private Double disk = -1.0;
    private Double usedDisk = -1.0;
    private Double usedMem = -1.0;
    private Double diskio = -1.0;
    private Integer delay = -1;
    private Integer processCount = -1;
    private Byte alertLevel = Level.CLEAR_LEVEL;
    private String alertDesc = "";
    private Timestamp alertTime;
    private Timestamp snapTime = new Timestamp(System.currentTimeMillis());
    private Timestamp delayTime;
    private String delayUpdateTime;
    private String delayOutTime;
    private Long outTime;
    private String cpuStr;
    private String memStr;
    private String recentUpdateTime; // 距离上次更新时间
    // alert Id
    private Long alertId = EponConstants.NO_ALERT;
    private String uplinkDevice;

    /**
     * @return the alertDesc
     */
    public String getAlertDesc() {
        return alertDesc;
    }

    /**
     * @return the alertLevel
     */
    public Byte getAlertLevel() {
        return alertLevel;
    }

    /**
     * @return the cpu
     */
    public Double getCpu() {
        return cpu;
    }

    /**
     * @return the delay
     */
    public Integer getDelay() {
        return delay;
    }

    /**
     * @return the delayTime
     */
    public Timestamp getDelayTime() {
        return delayTime;
    }

    /**
     * @return the disk
     */
    public Double getDisk() {
        return disk;
    }

    /**
     * @return the diskio
     */
    public Double getDiskio() {
        return diskio;
    }

    /**
     * @return the mem
     */
    public Double getMem() {
        return mem;
    }

    /**
     * @return the processCount
     */
    public Integer getProcessCount() {
        return processCount;
    }

    /**
     * @return the snapTime
     */
    public Timestamp getSnapTime() {
        return snapTime;
    }

    public Long getSnapTimeMillis() {
        return snapTime.getTime();
    }

    /**
     * @return the state
     */
    /*
     * public Boolean getState() { return state; }
     */

    /**
     * @return the sysUpTime
     */
    public String getSysUpTime() {
        return sysUpTime;
    }

    /**
     * @return the typeName
     */
    @Override
    public String getTypeName() {
        return typeName;
    }

    /**
     * @return the usedDisk
     */
    public Double getUsedDisk() {
        return usedDisk;
    }

    /**
     * @return the usedMem
     */
    public Double getUsedMem() {
        return usedMem;
    }

    /**
     * @return the vmem
     */
    public Double getVmem() {
        return vmem;
    }

    /**
     * @return the state
     */

    public Boolean isState() {
        return state;
    }

    /**
     * @param alertDesc
     *            the alertDesc to set
     */
    public void setAlertDesc(String alertDesc) {
        this.alertDesc = alertDesc;
    }

    /**
     * @param alertLevel
     *            the alertLevel to set
     */
    public void setAlertLevel(Byte alertLevel) {
        this.alertLevel = alertLevel;
    }

    /**
     * @param cpu
     *            the cpu to set
     */
    public void setCpu(Double cpu) {
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
     * @param delayTime
     *            the delayTime to set
     */
    public void setDelayTime(Timestamp delayTime) {
        this.delayTime = delayTime;
    }

    /**
     * @param disk
     *            the disk to set
     */
    public void setDisk(Double disk) {
        this.disk = disk;
    }

    /**
     * @param diskio
     *            the diskio to set
     */
    public void setDiskio(Double diskio) {
        this.diskio = diskio;
    }

    /**
     * @param mem
     *            the mem to set
     */
    public void setMem(Double mem) {
        this.mem = mem;
    }

    /**
     * @param processCount
     *            the processCount to set
     */
    public void setProcessCount(Integer processCount) {
        this.processCount = processCount;
    }

    /**
     * @param snapTime
     *            the snapTime to set
     */
    public void setSnapTime(Timestamp snapTime) {
        this.snapTime = snapTime;
    }

    public void setSnapTimeMillis(Long snapTimeMillis) {
        snapTime.setTime(snapTimeMillis);
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(Boolean state) {
        if (state != null) {
            if (!state) {
                setCpu(-1.0);
                setMem(-1.0);
                setVmem(-1.0);
                setDisk(-1.0);
                setUsedDisk(-1.0);
                setUsedMem(-1.0);
                setProcessCount(-1);
                setDiskio(-1.0);
                setSysUpTime(null);
            }
        }
        this.state = state;
    }

    /**
     * @param sysUpTime
     *            the sysUpTime to set
     */
    public void setSysUpTime(String sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    /**
     * @param typeName
     *            the typeName to set
     */
    @Override
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * @param usedDisk
     *            the usedDisk to set
     */
    public void setUsedDisk(Double usedDisk) {
        this.usedDisk = usedDisk;
    }

    /**
     * @param usedMem
     *            the usedMem to set
     */
    public void setUsedMem(Double usedMem) {
        this.usedMem = usedMem;
    }

    /**
     * @param vmem
     *            the vmem to set
     */
    public void setVmem(Double vmem) {
        this.vmem = vmem;
    }

    public String getDelayUpdateTime() {
        return delayUpdateTime;
    }

    public void setDelayUpdateTime(String delayUpdateTime) {
        this.delayUpdateTime = delayUpdateTime;
    }

    public String getDelayOutTime() {
        return delayOutTime;
    }

    public void setDelayOutTime(String delayOutTime) {
        this.delayOutTime = delayOutTime;
    }

    public String getDtStr() {
        return DateUtils.format(this.snapTime);
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public Timestamp getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(Timestamp alertTime) {
        this.alertTime = alertTime;
    }

    public Long getOutTime() {
        return outTime;
    }

    public void setOutTime(Long outTime) {
        this.outTime = outTime;
    }

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }

    public String getCpuStr() {
        return cpuStr;
    }

    public void setCpuStr(String cpuStr) {
        this.cpuStr = cpuStr;
    }

    public String getMemStr() {
        return memStr;
    }

    public void setMemStr(String memStr) {
        this.memStr = memStr;
    }

    public String getRecentUpdateTime() {
        return recentUpdateTime;
    }

    public void setRecentUpdateTime(String recentUpdateTime) {
        this.recentUpdateTime = recentUpdateTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EntitySnap [typeName=");
        builder.append(typeName);
        builder.append(", state=");
        builder.append(state);
        builder.append(", sysUpTime=");
        builder.append(sysUpTime);
        builder.append(", cpu=");
        builder.append(cpu);
        builder.append(", mem=");
        builder.append(mem);
        builder.append(", vmem=");
        builder.append(vmem);
        builder.append(", disk=");
        builder.append(disk);
        builder.append(", usedDisk=");
        builder.append(usedDisk);
        builder.append(", usedMem=");
        builder.append(usedMem);
        builder.append(", diskio=");
        builder.append(diskio);
        builder.append(", delay=");
        builder.append(delay);
        builder.append(", processCount=");
        builder.append(processCount);
        builder.append(", alertLevel=");
        builder.append(alertLevel);
        builder.append(", alertDesc=");
        builder.append(alertDesc);
        builder.append(", alertTime=");
        builder.append(alertTime);
        builder.append(", snapTime=");
        builder.append(snapTime);
        builder.append(", delayTime=");
        builder.append(delayTime);
        builder.append(", delayUpdateTime=");
        builder.append(delayUpdateTime);
        builder.append(", delayOutTime=");
        builder.append(delayOutTime);
        builder.append(", outTime=");
        builder.append(outTime);
        builder.append(", cpuStr=");
        builder.append(cpuStr);
        builder.append(", memStr=");
        builder.append(memStr);
        builder.append(", recentUpdateTime=");
        builder.append(recentUpdateTime);
        builder.append(", alertId=");
        builder.append(alertId);
        builder.append(", uplinkDevice=");
        builder.append(uplinkDevice);
        builder.append("]");
        return builder.toString();
    }

}
