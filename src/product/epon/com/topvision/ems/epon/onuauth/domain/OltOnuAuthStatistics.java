/***********************************************************************
 * $Id: OltOnuAuthStatistics.java,v1.0 2015年4月17日 下午1:58:15 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2015年4月17日-下午1:58:15
 * 
 */
public class OltOnuAuthStatistics implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6016875272315050936L;

    private Long entityId;
    private String ip;
    private String name;
    private Integer macAuthCount;
    private Integer snAuthCount;
    private Integer authFailCount;
    private Integer gponAuthCount;
    private Integer gponAutoFindCount;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMacAuthCount() {
        return macAuthCount;
    }

    public void setMacAuthCount(Integer macAuthCount) {
        this.macAuthCount = macAuthCount;
    }

    public Integer getSnAuthCount() {
        return snAuthCount;
    }

    public void setSnAuthCount(Integer snAuthCount) {
        this.snAuthCount = snAuthCount;
    }

    public Integer getAuthFailCount() {
        return authFailCount;
    }

    public void setAuthFailCount(Integer authFailCount) {
        this.authFailCount = authFailCount;
    }

    public Integer getGponAuthCount() {
        return gponAuthCount;
    }

    public void setGponAuthCount(Integer gponAuthCount) {
        this.gponAuthCount = gponAuthCount;
    }

    public Integer getGponAutoFindCount() {
        return gponAutoFindCount;
    }

    public void setGponAutoFindCount(Integer gponAutoFindCount) {
        this.gponAutoFindCount = gponAutoFindCount;
    }

}
