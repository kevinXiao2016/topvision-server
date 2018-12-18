/***********************************************************************
 * $Id: OnuHealthyInfo.java,v1.0 2017年7月20日 上午9:25:03 $
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
 * @created @2017年7月20日-上午9:25:03
 *
 */
public class OnuHealthyInfo implements Serializable, AliasesSuperType, Comparable<OnuHealthyInfo> {

    private static final long serialVersionUID = -8913230565213066736L;

    private Integer flag;
    private String content;
    private Integer healthyTarget;//超标指标

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(OnuHealthyInfo o) {
        return o.flag-this.flag;
    }

    public Integer getHealthyTarget() {
        return healthyTarget;
    }

    public void setHealthyTarget(Integer healthyTarget) {
        this.healthyTarget = healthyTarget;
    }

}
