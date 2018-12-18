/***********************************************************************
 * $Id: OltAlertTop.java,v1.0 2012-3-12 上午11:40:33 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2012-3-12-上午11:40:33
 * 
 */
public class OltAlertTop implements AliasesSuperType {
    private static final long serialVersionUID = 4800392379025020153L;
    private Long entityId;
    private String oltName;
    private String ip;
    private Long happenTimes;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getOltName() {
        return oltName;
    }

    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getHappenTimes() {
        return happenTimes;
    }

    public void setHappenTimes(Long happenTimes) {
        this.happenTimes = happenTimes;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltAlertTop [entityId=");
        builder.append(entityId);
        builder.append(", oltName=");
        builder.append(oltName);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", happenTimes=");
        builder.append(happenTimes);
        builder.append("]");
        return builder.toString();
    }
}
