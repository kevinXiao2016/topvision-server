/***********************************************************************
 * $Id: CmPollCollectParam.java,v1.0 2015年3月9日 下午2:37:14 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.config.domain;

import java.io.Serializable;

import com.topvision.framework.snmp.SnmpParam;

/**
 * @author loyal
 * @created @2015年3月9日-下午2:37:14
 * 
 */
public class CmPollCollectParam implements Serializable {
    private static final long serialVersionUID = -7303542138520015912L;
    private Integer maxPoolSize;
    private Integer sendCmCount;
    private SnmpParam cmCollectSnmpParam;
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

    public SnmpParam getCmCollectSnmpParam() {
        return cmCollectSnmpParam;
    }

    public void setCmCollectSnmpParam(SnmpParam cmCollectSnmpParam) {
        this.cmCollectSnmpParam = cmCollectSnmpParam;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
