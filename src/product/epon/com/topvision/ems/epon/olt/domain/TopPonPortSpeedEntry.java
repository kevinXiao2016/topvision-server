/***********************************************************************
 * $Id: TopPonPortSpeedEntry.java,v1.0 2013-06-13 下午13:47:59 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lzt
 * @created @2013-06-13-下午13:47:59
 * 
 */
public class TopPonPortSpeedEntry implements AliasesSuperType {
    private static final long serialVersionUID = -6932253060043103459L;
    private Long entityId;
    private Long ponIndex;
    private Long ponId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.8.1.1", index = true)
    private Integer ponPortSpeedCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.8.1.2", index = true)
    private Integer ponPortSpeedPortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.8.1.3", writable = true, type = "Integer32")
    private Integer ponPortSpeedMod;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Integer getPonPortSpeedCardIndex() {
        return ponPortSpeedCardIndex;
    }

    public void setPonPortSpeedCardIndex(Integer ponPortSpeedCardIndex) {
        this.ponPortSpeedCardIndex = ponPortSpeedCardIndex;
    }

    public Integer getPonPortSpeedPortIndex() {
        return ponPortSpeedPortIndex;
    }

    public void setPonPortSpeedPortIndex(Integer ponPortSpeedPortIndex) {
        this.ponPortSpeedPortIndex = ponPortSpeedPortIndex;
    }

    public Integer getPonPortSpeedMod() {
        return ponPortSpeedMod;
    }

    public void setPonPortSpeedMod(Integer ponPortSpeedMod) {
        this.ponPortSpeedMod = ponPortSpeedMod;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopPonPortSpeedEntry [entityId=");
        builder.append(entityId);
        builder.append(", ponIndex=");
        builder.append(ponIndex);
        builder.append(", ponId=");
        builder.append(ponId);
        builder.append(", ponPortSpeedCardIndex=");
        builder.append(ponPortSpeedCardIndex);
        builder.append(", ponPortSpeedPortIndex=");
        builder.append(ponPortSpeedPortIndex);
        builder.append(", ponPortSpeedMod=");
        builder.append(ponPortSpeedMod);
        builder.append("]");
        return builder.toString();
    }

}
