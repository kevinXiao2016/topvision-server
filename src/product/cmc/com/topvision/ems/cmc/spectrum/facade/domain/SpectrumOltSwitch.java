/***********************************************************************
 * $Id: SpectrumOltSwitch.java,v1.0 2014-3-4 下午3:18:19 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author haojie
 * @created @2014-3-4-下午3:18:19
 *
 */
public class SpectrumOltSwitch implements Serializable {
    private static final long serialVersionUID = -5442184281677865143L;
    private Long entityId;
    private String entityIp;
    private String oltName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.1.1.0", writable = true, type = "Integer32")
    private Integer collectSwitch;
    private Integer state;//olt的在线状态

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public String getOltName() {
        return oltName;
    }

    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    public Integer getCollectSwitch() {
        return collectSwitch;
    }

    public void setCollectSwitch(Integer collectSwitch) {
        this.collectSwitch = collectSwitch;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SpectrumOltSwitch [entityId=");
        builder.append(entityId);
        builder.append(", entityIp=");
        builder.append(entityIp);
        builder.append(", oltName=");
        builder.append(oltName);
        builder.append(", collectSwitch=");
        builder.append(collectSwitch);
        builder.append(", state=");
        builder.append(state);
        builder.append("]");
        return builder.toString();
    }

}
