/***********************************************************************
 * $Id: CmStatus.java,v1.0 2012-2-1 下午01:40:09 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Administrator
 * @created @2012-2-1-下午01:40:09
 * 
 */
public class CmStatusForContactCmc implements Serializable {
    private static final long serialVersionUID = -722529209955894122L;
    private String cmIp;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.5.0")
    private Integer docsIfDocsisBaseCapability;// 注册模式
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.21.2.0")
    private String docsIf3CmCapabilitiesRsp;// CM能力响应消息

    // 以下四个为兼容3.0CM而做
    // 下行射频信息列表，从cm上取
    private List<DocsIfDownstreamChannelForContactCmc> docsIfDownstreamChannelList;
    // 上行射频信息列表，从cm上取
    private List<DocsIfUpstreamChannelForContactCmc> docsIfUpstreamChannelList;
    // 下行信号质量，从cm上取
    private List<DocsIfSignalQualityForContactCmc> docsIfSignalQualityList;

    public String getCmIp() {
        return cmIp;
    }

    public void setCmIp(String cmIp) {
        this.cmIp = cmIp;
    }

    public Integer getDocsIfDocsisBaseCapability() {
        return docsIfDocsisBaseCapability;
    }

    public void setDocsIfDocsisBaseCapability(Integer docsIfDocsisBaseCapability) {
        this.docsIfDocsisBaseCapability = docsIfDocsisBaseCapability;
    }

    public List<DocsIfDownstreamChannelForContactCmc> getDocsIfDownstreamChannelList() {
        return docsIfDownstreamChannelList;
    }

    public void setDocsIfDownstreamChannelList(List<DocsIfDownstreamChannelForContactCmc> docsIfDownstreamChannelList) {
        this.docsIfDownstreamChannelList = docsIfDownstreamChannelList;
    }

    public List<DocsIfUpstreamChannelForContactCmc> getDocsIfUpstreamChannelList() {
        return docsIfUpstreamChannelList;
    }

    public void setDocsIfUpstreamChannelList(List<DocsIfUpstreamChannelForContactCmc> docsIfUpstreamChannelList) {
        this.docsIfUpstreamChannelList = docsIfUpstreamChannelList;
    }

    public List<DocsIfSignalQualityForContactCmc> getDocsIfSignalQualityList() {
        return docsIfSignalQualityList;
    }

    public void setDocsIfSignalQualityList(List<DocsIfSignalQualityForContactCmc> docsIfSignalQualityList) {
        this.docsIfSignalQualityList = docsIfSignalQualityList;
    }

    public String getDocsIf3CmCapabilitiesRsp() {
        return docsIf3CmCapabilitiesRsp;
    }

    public void setDocsIf3CmCapabilitiesRsp(String docsIf3CmCapabilitiesRsp) {
        this.docsIf3CmCapabilitiesRsp = docsIf3CmCapabilitiesRsp;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmStatusForContactCmc [cmIp=");
        builder.append(cmIp);
        builder.append(", docsIfDocsisBaseCapability=");
        builder.append(docsIfDocsisBaseCapability);
        builder.append(", docsIf3CmCapabilitiesRsp=");
        builder.append(docsIf3CmCapabilitiesRsp);
        builder.append(", docsIfDownstreamChannelList=");
        builder.append(docsIfDownstreamChannelList);
        builder.append(", docsIfUpstreamChannelList=");
        builder.append(docsIfUpstreamChannelList);
        builder.append(", docsIfSignalQualityList=");
        builder.append(docsIfSignalQualityList);
        builder.append("]");
        return builder.toString();
    }

}