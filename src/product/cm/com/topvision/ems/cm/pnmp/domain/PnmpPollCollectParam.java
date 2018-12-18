/***********************************************************************
 * $Id: CmPollCollectParam.java,v1.0 2015年3月9日 下午2:37:14 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.domain;

import com.topvision.framework.snmp.SnmpParam;

import java.io.Serializable;

/**
 * @author loyal
 * @created @2015年3月9日-下午2:37:14
 * 
 */
public class PnmpPollCollectParam implements Serializable {
    private static final long serialVersionUID = -7303542138520015912L;
    private Integer maxPoolSize;
    private Integer sendCmCount;
    private SnmpParam snmpParam;
    private String callbackUrl;

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getSendCmCount() {
        return sendCmCount;
    }

    public void setSendCmCount(Integer sendCmCount) {
        this.sendCmCount = sendCmCount;
    }

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
