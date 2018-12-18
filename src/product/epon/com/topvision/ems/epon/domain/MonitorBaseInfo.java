/***********************************************************************
 * $Id: MonitorTable.java,v1.0 2012-5-27 下午05:39:12 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2012-5-27-下午05:39:12
 * 
 */
public class MonitorBaseInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6243694864870115344L;
    private Long monitorId;
    private String monitorName;
    private String monitorDesc;
    private Integer monitorType;
    private Integer monitorTick;
    private List<MonitorPort> monitorPorts;
    private List<MonitorIndex> monitorIndexes;
    private String name;
    private String ip;

    public Long getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public String getMonitorDesc() {
        return monitorDesc;
    }

    public void setMonitorDesc(String monitorDesc) {
        this.monitorDesc = monitorDesc;
    }

    public Integer getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(Integer monitorType) {
        this.monitorType = monitorType;
    }

    public List<MonitorPort> getMonitorPorts() {
        return monitorPorts;
    }

    public void setMonitorPorts(List<MonitorPort> monitorPorts) {
        this.monitorPorts = monitorPorts;
    }

    public List<MonitorIndex> getMonitorIndexes() {
        return monitorIndexes;
    }

    public void setMonitorIndexes(List<MonitorIndex> monitorIndexes) {
        this.monitorIndexes = monitorIndexes;
    }

    public Integer getMonitorTick() {
        return monitorTick;
    }

    public void setMonitorTick(Integer monitorTick) {
        this.monitorTick = monitorTick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
