/***********************************************************************
 * $Id: PingPerf.java,v1.0 2014-1-7 上午9:53:33 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author Rod John
 * @created @2014-1-7-上午9:53:33
 * 
 */
@Scope("prototype")
@Service("pingPerf")
public class PingPerf extends OperClass implements Serializable {
    private static final long serialVersionUID = 9171993402531693975L;
    private String ipAddress;

    public PingPerf() {
        super("pingSaver", "pingScheduler", "pingPerf");
    }

    @Override
    public boolean isTaskCancle() {
        return false;
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
    }

    @Override
    public void startUpTarget(String targetName, Object data) {
    }

    @Override
    public long getIdentifyKey() {
        return entityId;
    }

    @Override
    public void setIdentifyKey(Long identifyKey) {
        this.entityId = identifyKey;
    }

    @Override
    public String[] makeOids() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PerformanceDomain[] makeObjects() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress
     *            the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
