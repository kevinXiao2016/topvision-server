/***********************************************************************
 * $Id: SendConfigResult.java,v1.0 2014-7-21 下午2:01:11 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import java.sql.Timestamp;

/**
 * @author jay
 * @created @2014-7-21-下午2:01:11
 *
 */
public class SendConfigResult {
    public static Integer NOSTART = 1;
    public static Integer TELNETERROR = 2;
    public static Integer CONFIGERROR = 3;
    public static Integer SUCCESS = 4;
    public static Integer WRITEFILEERROR = 5;
    public static Integer WAITWRITECONFIG = 6;
    private String result;
    private Long entityId;
    private Integer state = NOSTART;
    private Long resultId;
    private Timestamp dt;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Timestamp getDt() {
        return dt;
    }

    public void setDt(Timestamp dt) {
        this.dt = dt;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "SendConfigResult [entityId=" + entityId + ", state=" + state + ", resultId=" + resultId + ", dt=" + dt
                + "]";
    }

}
