/***********************************************************************
 * $Id: OnuCpeConfig.java,v1.0 2016年7月5日 下午3:27:13 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onucpe.domain;

import java.io.Serializable;

/**
 * @author Bravin
 * @created @2016年7月5日-下午3:27:13
 *
 */
public class OnuCpeConfig implements Serializable {
    private static final long serialVersionUID = 6173475305450016182L;
    //是否采集CPE原始数据 0 开启 1关闭
    private Integer onuCpeStatus;
    //采集周期
    private Integer onuCpeInterval;

    public Integer getOnuCpeStatus() {
        return onuCpeStatus;
    }

    public void setOnuCpeStatus(Integer onuCpeStatus) {
        this.onuCpeStatus = onuCpeStatus;
    }

    public Integer getOnuCpeInterval() {
        return onuCpeInterval;
    }

    public void setOnuCpeInterval(Integer onuCpeInterval) {
        this.onuCpeInterval = onuCpeInterval;
    }

}
