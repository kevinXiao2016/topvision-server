/***********************************************************************
 * $Id: StartStopTime.java,v1.0 2013-6-21 上午9:46:58 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.domain;

import java.util.Date;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author fanzidong
 * @created @2013-6-21-上午9:46:58
 * 
 */
public class StartStopTime implements AliasesSuperType {
    private static final long serialVersionUID = -4203156152027758146L;

    private Date stTime;
    private Date etTime;

    public Date getStTime() {
        return stTime;
    }

    public void setStTime(Date stTime) {
        this.stTime = stTime;
    }

    public Date getEtTime() {
        return etTime;
    }

    public void setEtTime(Date etTime) {
        this.etTime = etTime;
    }

    public StartStopTime() {
        super();
    }

    public StartStopTime(Date stTime, Date etTime) {
        super();
        this.stTime = stTime;
        this.etTime = etTime;
    }

    @Override
    public String toString() {
        return "StartStopTime [stTime=" + stTime + ", etTime=" + etTime + "]";
    }

}
