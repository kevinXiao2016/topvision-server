/***********************************************************************
 * $Id: TopSIPAgentProfInfo.java,v1.0 2017年5月3日 下午4:13:12 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2017年5月3日-下午4:13:12
 *
 */
public class TopSIPAgentProfInfo implements AliasesSuperType {
    private static final long serialVersionUID = 1700910271420959010L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.1", index = true, type = "Integer32")
    private Integer topSIPAgtProfIdx;// 1-64
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.2", writable = true, type = "OctetString")
    private String topSIPAgtProfName;// 1-31
    // 代理服务器配置
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.3", writable = true, type = "OctetString")
    private String topSIPAgtProxyAddr;// 0-63 代理服务器IP地址
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.4", writable = true, type = "Integer32")
    private Integer topSIPAgtProxyPort;// 0-65535 代理服務器端口
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.5", writable = true, type = "OctetString")
    private String topSIPAgtSecProxyAddr;// 0-63 备用代理服务器IP地址
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.6", writable = true, type = "Integer32")
    private Integer topSIPAgtSecProxyPort;// 0-65535 备用代理服務器端口
    // outbound服务器配置
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.7", writable = true, type = "OctetString")
    private String topSIPAgtOutboundAddr;// 0-63 outbound服务器IP地址
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.8", writable = true, type = "Integer32")
    private Integer topSIPAgtOutboundPort;// 0-65535 outbound服务器端口
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.9", writable = true, type = "OctetString")
    private String topSIPAgtSecOutboundAddr;// 0-63 备用outbound服务器IP地址
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.10", writable = true, type = "Integer32")
    private Integer topSIPAgtSecOutboundPort;// 0-65535 备用outbound服务器端口
    // 注册服务器配置
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.14", writable = true, type = "OctetString")
    private String topSIPAgtRegAddr;// 0-63 注册服务器IP地址
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.15", writable = true, type = "Integer32")
    private Integer topSIPAgtRegPort;// 0-65535 注册服务器端口
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.16", writable = true, type = "OctetString")
    private String topSIPAgtSecRegAddr;// 0-63 备用注册服务器IP地址
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.17", writable = true, type = "Integer32")
    private Integer topSIPAgtSecRegPort;// 0-65535 备用注册服务器端口
    
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.18", writable = true, type = "Integer32")
    private Integer topSIPAgtReqExpTime;//SIP注册服务器注册有效时长 1-65534 s
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.24", type = "Integer32")
    private Integer topSIPAgtBindCnt;// 被绑定次数
    
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.1.1.25", writable = true, type = "Integer32")
    private Integer topSIPAgtRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopSIPAgtProfIdx() {
        return topSIPAgtProfIdx;
    }

    public void setTopSIPAgtProfIdx(Integer topSIPAgtProfIdx) {
        this.topSIPAgtProfIdx = topSIPAgtProfIdx;
    }

    public String getTopSIPAgtProfName() {
        return topSIPAgtProfName;
    }

    public void setTopSIPAgtProfName(String topSIPAgtProfName) {
        this.topSIPAgtProfName = topSIPAgtProfName;
    }

    public String getTopSIPAgtProxyAddr() {
        return topSIPAgtProxyAddr;
    }

    public void setTopSIPAgtProxyAddr(String topSIPAgtProxyAddr) {
        this.topSIPAgtProxyAddr = topSIPAgtProxyAddr;
    }

    public Integer getTopSIPAgtProxyPort() {
        return topSIPAgtProxyPort;
    }

    public void setTopSIPAgtProxyPort(Integer topSIPAgtProxyPort) {
        this.topSIPAgtProxyPort = topSIPAgtProxyPort;
    }

    public String getTopSIPAgtSecProxyAddr() {
        return topSIPAgtSecProxyAddr;
    }

    public void setTopSIPAgtSecProxyAddr(String topSIPAgtSecProxyAddr) {
        this.topSIPAgtSecProxyAddr = topSIPAgtSecProxyAddr;
    }

    public Integer getTopSIPAgtSecProxyPort() {
        return topSIPAgtSecProxyPort;
    }

    public void setTopSIPAgtSecProxyPort(Integer topSIPAgtSecProxyPort) {
        this.topSIPAgtSecProxyPort = topSIPAgtSecProxyPort;
    }

    public String getTopSIPAgtOutboundAddr() {
        return topSIPAgtOutboundAddr;
    }

    public void setTopSIPAgtOutboundAddr(String topSIPAgtOutboundAddr) {
        this.topSIPAgtOutboundAddr = topSIPAgtOutboundAddr;
    }

    public Integer getTopSIPAgtOutboundPort() {
        return topSIPAgtOutboundPort;
    }

    public void setTopSIPAgtOutboundPort(Integer topSIPAgtOutboundPort) {
        this.topSIPAgtOutboundPort = topSIPAgtOutboundPort;
    }

    public String getTopSIPAgtSecOutboundAddr() {
        return topSIPAgtSecOutboundAddr;
    }

    public void setTopSIPAgtSecOutboundAddr(String topSIPAgtSecOutboundAddr) {
        this.topSIPAgtSecOutboundAddr = topSIPAgtSecOutboundAddr;
    }

    public Integer getTopSIPAgtSecOutboundPort() {
        return topSIPAgtSecOutboundPort;
    }

    public void setTopSIPAgtSecOutboundPort(Integer topSIPAgtSecOutboundPort) {
        this.topSIPAgtSecOutboundPort = topSIPAgtSecOutboundPort;
    }

    public String getTopSIPAgtRegAddr() {
        return topSIPAgtRegAddr;
    }

    public void setTopSIPAgtRegAddr(String topSIPAgtRegAddr) {
        this.topSIPAgtRegAddr = topSIPAgtRegAddr;
    }

    public Integer getTopSIPAgtRegPort() {
        return topSIPAgtRegPort;
    }

    public void setTopSIPAgtRegPort(Integer topSIPAgtRegPort) {
        this.topSIPAgtRegPort = topSIPAgtRegPort;
    }

    public String getTopSIPAgtSecRegAddr() {
        return topSIPAgtSecRegAddr;
    }

    public void setTopSIPAgtSecRegAddr(String topSIPAgtSecRegAddr) {
        this.topSIPAgtSecRegAddr = topSIPAgtSecRegAddr;
    }

    public Integer getTopSIPAgtSecRegPort() {
        return topSIPAgtSecRegPort;
    }

    public void setTopSIPAgtSecRegPort(Integer topSIPAgtSecRegPort) {
        this.topSIPAgtSecRegPort = topSIPAgtSecRegPort;
    }

    public Integer getTopSIPAgtRowStatus() {
        return topSIPAgtRowStatus;
    }

    public void setTopSIPAgtRowStatus(Integer topSIPAgtRowStatus) {
        this.topSIPAgtRowStatus = topSIPAgtRowStatus;
    }

    public Integer getTopSIPAgtReqExpTime() {
        return topSIPAgtReqExpTime;
    }

    public void setTopSIPAgtReqExpTime(Integer topSIPAgtReqExpTime) {
        this.topSIPAgtReqExpTime = topSIPAgtReqExpTime;
    }

    public Integer getTopSIPAgtBindCnt() {
        return topSIPAgtBindCnt;
    }

    public void setTopSIPAgtBindCnt(Integer topSIPAgtBindCnt) {
        this.topSIPAgtBindCnt = topSIPAgtBindCnt;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopSIPAgentProfInfo [entityId=");
        builder.append(entityId);
        builder.append(", topSIPAgtProfIdx=");
        builder.append(topSIPAgtProfIdx);
        builder.append(", topSIPAgtProfName=");
        builder.append(topSIPAgtProfName);
        builder.append(", topSIPAgtProxyAddr=");
        builder.append(topSIPAgtProxyAddr);
        builder.append(", topSIPAgtProxyPort=");
        builder.append(topSIPAgtProxyPort);
        builder.append(", topSIPAgtSecProxyAddr=");
        builder.append(topSIPAgtSecProxyAddr);
        builder.append(", topSIPAgtSecProxyPort=");
        builder.append(topSIPAgtSecProxyPort);
        builder.append(", topSIPAgtOutboundAddr=");
        builder.append(topSIPAgtOutboundAddr);
        builder.append(", topSIPAgtOutboundPort=");
        builder.append(topSIPAgtOutboundPort);
        builder.append(", topSIPAgtSecOutboundAddr=");
        builder.append(topSIPAgtSecOutboundAddr);
        builder.append(", topSIPAgtSecOutboundPort=");
        builder.append(topSIPAgtSecOutboundPort);
        builder.append(", topSIPAgtRegAddr=");
        builder.append(topSIPAgtRegAddr);
        builder.append(", topSIPAgtRegPort=");
        builder.append(topSIPAgtRegPort);
        builder.append(", topSIPAgtSecRegAddr=");
        builder.append(topSIPAgtSecRegAddr);
        builder.append(", topSIPAgtSecRegPort=");
        builder.append(topSIPAgtSecRegPort);
        builder.append(", topSIPAgtReqExpTime=");
        builder.append(topSIPAgtReqExpTime);
        builder.append(", topSIPAgtBindCnt=");
        builder.append(topSIPAgtBindCnt);
        builder.append(", topSIPAgtRowStatus=");
        builder.append(topSIPAgtRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
