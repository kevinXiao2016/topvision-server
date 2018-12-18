/***********************************************************************
 * $Id: PonOnuAuthStatistics.java,v1.0 2015年4月17日 下午3:33:47 $
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
 * @created @2015年4月17日-下午3:33:47
 * 
 */
public class PonOnuAuthStatistics implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 864553923504473817L;

    private Long entityId;
    private Long ponId;
    private Long ponIndex;
    private Integer macAuthCount;
    private Integer snAuthCount;
    private Integer authFailCount;
    private Integer gponAuthCount;
    private Integer gponAutoFindCount;
    private String ponAuthMode;
    private Integer ponPortType;
    private Integer gponAuthMode;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
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

    public String getPonAuthMode() {
        return ponAuthMode;
    }

    public void setPonAuthMode(String ponAuthMode) {
        this.ponAuthMode = ponAuthMode;
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

    public Integer getPonPortType() {
        return ponPortType;
    }

    public void setPonPortType(Integer ponPortType) {
        this.ponPortType = ponPortType;
    }

    public Integer getGponAuthMode() {
        return gponAuthMode;
    }

    public void setGponAuthMode(Integer gponAuthMode) {
        this.gponAuthMode = gponAuthMode;
    }

}
