/***********************************************************************
 * $Id: OnuHealthyThreshold.java,v1.0 2017年7月20日 上午9:21:25 $
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
 * @created @2017年7月20日-上午9:21:25
 *
 */
public class OnuHealthyThreshold implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 6799443558074427348L;

    // 各指标阈值
    private String targetId;
    private String thresholds;

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getThresholds() {
        return thresholds;
    }

    public void setThresholds(String thresholds) {
        this.thresholds = thresholds;
    }

}
