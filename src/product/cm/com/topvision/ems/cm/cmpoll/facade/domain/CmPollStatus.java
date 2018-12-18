/***********************************************************************
 * $Id: CmStatus.java,v1.0 2012-2-1 下午01:40:09 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Administrator
 * @created @2012-2-1-下午01:40:09
 * 
 */
public class CmPollStatus implements Serializable {
    private static final long serialVersionUID = -722529209955894122L;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.5.0")
    private Integer docsIfDocsisBaseCapability;// 注册模式
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.21.2.0")
    private String docsIf3CmCapabilitiesRsp;// CM能力响应消息

    public Integer getDocsIfDocsisBaseCapability() {
        return docsIfDocsisBaseCapability;
    }

    public void setDocsIfDocsisBaseCapability(Integer docsIfDocsisBaseCapability) {
        this.docsIfDocsisBaseCapability = docsIfDocsisBaseCapability;
    }

    public String getDocsIf3CmCapabilitiesRsp() {
        return docsIf3CmCapabilitiesRsp;
    }

    public void setDocsIf3CmCapabilitiesRsp(String docsIf3CmCapabilitiesRsp) {
        this.docsIf3CmCapabilitiesRsp = docsIf3CmCapabilitiesRsp;
    }
}