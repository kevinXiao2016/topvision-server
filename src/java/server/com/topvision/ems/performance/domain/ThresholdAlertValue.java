/***********************************************************************
 * $Id: ThreshholdAlertValue.java,v1.0 2016年8月13日 下午2:14:43 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2016年8月13日-下午2:14:43
 *
 */
public class ThresholdAlertValue implements AliasesSuperType {
    private static final long serialVersionUID = -9150489364663035835L;
    private Long entityId;
    private Integer alertEventId;
    private String source;
    private Integer levelId;
    private Float perfValue;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getAlertEventId() {
        return alertEventId;
    }

    public void setAlertEventId(Integer alertEventId) {
        this.alertEventId = alertEventId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Float getPerfValue() {
        return perfValue;
    }

    public void setPerfValue(Float perfValue) {
        this.perfValue = perfValue;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

}
