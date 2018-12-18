package com.topvision.ems.epon.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class PerforamanceRank implements AliasesSuperType {
    private static final long serialVersionUID = -3468209049272929812L;
    private final DateFormat formator = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String ip;
    private Long entityId;
    // from table entity snap @query for cpu/mem
    private Float cpu = -1.0f;
    private Float mem = -1.0f;
    private Long vmem = -1l;
    private Long usedMem = -1l;
    private Long disk = -1l;
    private Long usedDisk = -1l;
    private Long diskio = -1l;
    private Integer processCount;
    private Date snapTime;
    private Integer state;
    private String delay;
    private Date delayTime;
    private String CPU_p;
    private String MEM_p;
    // from oltSniAttribute @QUERY for sni/pon speed
    private Long portIndex;
    private String portName;
    private Long inValue;
    private Long outValue;
    private Long ioValue;
    private String endTime;
    private String inSpeed;
    private String out;
    private String io;
    private Date sysUpTime;
    private String name;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the cpu
     */
    public Float getCpu() {
        return cpu;
    }

    /**
     * @param cpu
     *            the cpu to set
     */
    public void setCpu(Float cpu) {
        this.cpu = cpu;
    }

    /**
     * @return the mem
     */
    public Float getMem() {
        return mem;
    }

    /**
     * @param mem
     *            the mem to set
     */
    public void setMem(Float mem) {
        this.mem = mem;
    }

    /**
     * @return the vmem
     */
    public Long getVmem() {
        return vmem;
    }

    /**
     * @param vmem
     *            the vmem to set
     */
    public void setVmem(Long vmem) {
        this.vmem = vmem;
    }

    /**
     * @return the usedMem
     */
    public Long getUsedMem() {
        return usedMem;
    }

    /**
     * @param usedMem
     *            the usedMem to set
     */
    public void setUsedMem(Long usedMem) {
        this.usedMem = usedMem;
    }

    /**
     * @return the disk
     */
    public Long getDisk() {
        return disk;
    }

    /**
     * @param disk
     *            the disk to set
     */
    public void setDisk(Long disk) {
        this.disk = disk;
    }

    /**
     * @return the usedDisk
     */
    public Long getUsedDisk() {
        return usedDisk;
    }

    /**
     * @param usedDisk
     *            the usedDisk to set
     */
    public void setUsedDisk(Long usedDisk) {
        this.usedDisk = usedDisk;
    }

    /**
     * @return the diskio
     */
    public Long getDiskio() {
        return diskio;
    }

    /**
     * @param diskio
     *            the diskio to set
     */
    public void setDiskio(Long diskio) {
        this.diskio = diskio;
    }

    /**
     * @return the processCount
     */
    public Integer getProcessCount() {
        return processCount;
    }

    /**
     * @param processCount
     *            the processCount to set
     */
    public void setProcessCount(Integer processCount) {
        this.processCount = processCount;
    }

    /**
     * @return the formator
     */
    public DateFormat getFormator() {
        return formator;
    }

    /**
     * @param delay
     *            the delay to set
     */
    public void setDelay(String delay) {
        this.delay = delay;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Date getSnapTime() {
        return snapTime;
    }

    public void setSnapTime(Date snapTime) {
        this.snapTime = snapTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        if (delay == -1) {
            this.delay = "Equipment can not be connected";
            return;
        }
        this.delay = delay + "";
    }

    public Date getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Date delayTime) {
        this.delayTime = delayTime;
    }

    public Date getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(Date sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMEM_p() {
        return MEM_p;
    }

    public void setMEM_p(String mEM_p) {
        MEM_p = mEM_p;
    }

    public String getCPU_p() {
        return CPU_p;
    }

    public void setCPU_p(String cPU_p) {
        CPU_p = cPU_p;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
        this.setPortName(EponIndex.getSlotNo(portIndex) + "/" + EponIndex.getPonNo(portIndex));
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public Long getInValue() {
        return inValue;
    }

    public void setInValue(Long inValue) {
        this.inValue = inValue;
    }

    public Long getOutValue() {
        return outValue;
    }

    public void setOutValue(Long outValue) {
        this.outValue = outValue;
    }

    public Long getIoValue() {
        return ioValue;
    }

    public void setIoValue(Long ioValue) {
        this.ioValue = ioValue;
        if (ioValue < 1024) {
            this.setIo(ioValue + "Byte");
        } else if (ioValue < 1024 * 1024) {
            this.setIo(ioValue / 1024 + "KB");
        } else {
            this.setIo(ioValue / 1024 / 1024 + "MB");
        }
    }

    public String getEndTime() {
        int length = endTime.length();
        return endTime.substring(0, length - 2);
    }

    public void setEndTime(Date endTime) {
        this.endTime = formator.format(endTime);
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getIo() {
        return io;
    }

    public void setIo(String io) {
        this.io = io;
    }

    public String getInSpeed() {
        return inSpeed;
    }

    public void setInSpeed(String inSpeed) {
        this.inSpeed = inSpeed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
