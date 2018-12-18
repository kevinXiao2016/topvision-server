/***********************************************************************
 * $Id: OnlineResult.java,v1.0 2014-3-14 上午9:56:47 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;
import com.topvision.ems.facade.nbi.NbiSnmpProperty;

/**
 * @author Rod John
 * @created @2014-3-14-上午9:56:47
 * 
 */
public class OnlineResult extends PerformanceResult<OperClass> implements Serializable {
    private static final long serialVersionUID = 4103315820443974838L;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.10.1.1")
    private Integer delay;
    private String sysUpTime;
    private Timestamp collectTime;
    private List<String> strategys;
    private String mac;

    /**
     * @param domain
     */
    public OnlineResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the delay
     */
    public Integer getDelay() {
        return delay;
    }

    /**
     * @param delay
     *            the delay to set
     */
    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    /**
     * @return the sysUpTime
     */
    public String getSysUpTime() {
        return sysUpTime;
    }

    /**
     * @param sysUpTime
     *            the sysUpTime to set
     */
    public void setSysUpTime(String sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    /**
     * @return the collectTime
     */
    public Timestamp getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public List<String> getStrategys() {
        return strategys;
    }

    public void setStrategys(List<String> strategys) {
        this.strategys = strategys;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

}
