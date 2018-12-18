/***********************************************************************
 * $Id: OnuCpe.java,v1.0 2013-8-6 下午04:45:40 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2013-8-6-下午04:45:40
 * 
 */
public class OnuUniCpeCount implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -7135580713841469808L;
    private Long entityId;
    private Long uniIndex;
    private Long uniNo;
    private Integer cpecount;
    private Timestamp realtime;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getUniNo() {
        return uniNo;
    }

    public void setUniNo(Long uniNo) {
        this.uniNo = uniNo;
    }

    public Integer getCpecount() {
        return cpecount;
    }

    public void setCpecount(Integer cpecount) {
        this.cpecount = cpecount;
    }

    public Timestamp getRealtime() {
        return realtime;
    }

    public void setRealtime(Timestamp realtime) {
        this.realtime = realtime;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

}
