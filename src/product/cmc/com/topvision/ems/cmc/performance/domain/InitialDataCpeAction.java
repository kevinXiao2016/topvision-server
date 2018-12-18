/***********************************************************************
 * $ CmStatusDB.java,v1.0 2013-6-21 15:45:13 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2013-6-21-15:45:13
 */
@Alias("initialDataCpeAction")
public class InitialDataCpeAction implements Serializable,  AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    /*
    1: host(1)
 	2: mta(2)
 	3: stb(3)
     */
    public static Integer BROADBAND = 1;
    public static Integer MTA = 2;
    public static Integer INTERACTIVE = 3;
    private Long entityId;
    private Long cmmac;
    private Long cpemac;
    private Long cpeip;
    private Integer cpetype;
    private Long realtimeLong;

    public Long getCmmac() {
        return cmmac;
    }

    public void setCmmac(Long cmmac) {
        this.cmmac = cmmac;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Timestamp getRealtime() {
        return new Timestamp(realtimeLong);
    }

    public void setRealtime(Timestamp realtime) {
        this.realtimeLong = realtime.getTime();
    }

    public Long getRealtimeLong() {
        return realtimeLong;
    }

    public void setRealtimeLong(Long realtimeLong) {
        this.realtimeLong = realtimeLong;
    }

    public Long getCpeip() {
        return cpeip;
    }

    public void setCpeip(Long cpeip) {
        this.cpeip = cpeip;
    }

    public Long getCpemac() {
        return cpemac;
    }

    public void setCpemac(Long cpemac) {
        this.cpemac = cpemac;
    }

    public Integer getCpetype() {
        return cpetype;
    }

    public void setCpetype(Integer cpetype) {
        this.cpetype = cpetype;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("InitialDataCpeAction");
        sb.append("{cmmac=").append(cmmac);
        sb.append(", entityId=").append(entityId);
        sb.append(", cpemac=").append(cpemac);
        sb.append(", cpeip=").append(cpeip);
        sb.append(", cpetype=").append(cpetype);
        sb.append(", realtimeLong=").append(realtimeLong);
        sb.append('}');
        return sb.toString();
    }
}