/***********************************************************************
 * $Id: Olt.java,v1.0 2016年7月16日 下午1:44:44 $ * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.domain;

import java.sql.Timestamp;
import java.text.DecimalFormat;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author YangYi
 * @created @2016年7月16日-下午1:44:44
 *
 */
public class MobileOlt implements AliasesSuperType {
    private static final long serialVersionUID = -2734283245464116541L;
    private DecimalFormat formatter = new DecimalFormat("0");

    private Long entityId;
    // 别名
    private String name;
    // 设备类型ID
    private Long typeId;
    // 类型名称
    private String oltType;
    // 管理IP
    private String ip;
    // MAC地址
    private String mac;
    // 在线状态
    private Integer state;
    // 在线时长
    private Long sysUpTime;
    private String sysUpTimeString;
    // OLT 软件版本
    private String softVersion;
    // CPU利用率
    private Double cpuUsed;
    private String cpu;
    // 内存利用率
    private Double memUsed;
    private String mem;
    private Timestamp snapTime;
    private String oltLocation;
    // 下级CC
    private Integer cmcTotal;
    // 下级CC在线数
    private Integer cmcOnline;
    // 下级onu
    private Integer onuTotal;
    // 下级onu在线数
    private Integer onuOnline;

    public String getSysUpTimeString() {
        return sysUpTimeString;
    }

    public void setSysUpTimeString(String sysUpTimeString) {
        this.sysUpTimeString = sysUpTimeString;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getOltType() {
        return oltType;
    }

    public void setOltType(String oltType) {
        this.oltType = oltType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(Long sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    public String getSoftVersion() {
        return softVersion;
    }

    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    public Double getCpuUsed() {
        return cpuUsed;
    }

    public void setCpuUsed(Double cpuUsed) {
        this.cpuUsed = cpuUsed;
    }

    public String getCpu() {
        if (cpuUsed == null) {
            return "--";
        } else if (cpuUsed == -1) {
            return "--";
        } else {
            String c = formatter.format(cpuUsed * 100);
            return c + "%";
        }
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public Double getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(Double memUsed) {
        this.memUsed = memUsed;
    }

    public String getMem() {
        if (memUsed == null) {
            return "--";
        } else if (memUsed == -1) {
            return "--";
        } else {
            String c = formatter.format(memUsed * 100);
            return c + "%";
        }
    }

    public void setMem(String mem) {
        this.mem = mem;
    }

    public Integer getCmcTotal() {
        return cmcTotal;
    }

    public void setCmcTotal(Integer cmcTotal) {
        this.cmcTotal = cmcTotal;
    }

    public Integer getCmcOnline() {
        return cmcOnline;
    }

    public void setCmcOnline(Integer cmcOnline) {
        this.cmcOnline = cmcOnline;
    }

    public Integer getOnuTotal() {
        return onuTotal;
    }

    public void setOnuTotal(Integer onuTotal) {
        this.onuTotal = onuTotal;
    }

    public Integer getOnuOnline() {
        return onuOnline;
    }

    public void setOnuOnline(Integer onuOnline) {
        this.onuOnline = onuOnline;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MobileOlt [formatter=");
        builder.append(formatter);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", name=");
        builder.append(name);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", oltType=");
        builder.append(oltType);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", state=");
        builder.append(state);
        builder.append(", sysUpTime=");
        builder.append(sysUpTime);
        builder.append(", sysUpTimeString=");
        builder.append(sysUpTimeString);
        builder.append(", softVersion=");
        builder.append(softVersion);
        builder.append(", cpuUsed=");
        builder.append(cpuUsed);
        builder.append(", cpu=");
        builder.append(cpu);
        builder.append(", memUsed=");
        builder.append(memUsed);
        builder.append(", mem=");
        builder.append(mem);
        builder.append(", snapTime=");
        builder.append(snapTime);
        builder.append(", cmcTotal=");
        builder.append(cmcTotal);
        builder.append(", cmcOnline=");
        builder.append(cmcOnline);
        builder.append(", onuTotal=");
        builder.append(onuTotal);
        builder.append(", onuOnline=");
        builder.append(onuOnline);
        builder.append("]");
        return builder.toString();
    }

    public Timestamp getSnapTime() {
        return snapTime;
    }

    public void setSnapTime(Timestamp snapTime) {
        this.snapTime = snapTime;
    }

    public String getOltLocation() {
        return oltLocation;
    }

    public void setOltLocation(String oltLocation) {
        this.oltLocation = oltLocation;
    }

}
