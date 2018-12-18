/***********************************************************************
 * $Id: OnuHealthyPerfRule.java,v1.0 2017年7月20日 上午9:28:18 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author ls
 * @created @2017年7月20日-上午9:28:18
 *
 */
public class OnuHealthyPerfRule implements Serializable, AliasesSuperType, Comparable<OnuHealthyPerfRule> {

    private static final long serialVersionUID = -5241050969594672685L;

    private Integer compareWay;
    private Integer level;
    private Float value;
    private Integer priority;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCompareWay() {
        return compareWay;
    }

    public void setCompareWay(Integer compareWay) {
        this.compareWay = compareWay;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(OnuHealthyPerfRule o) {
        return o.level-this.level;
    }

}
