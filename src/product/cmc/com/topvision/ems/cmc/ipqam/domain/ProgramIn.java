/***********************************************************************
 * $Id: ProgramIn.java,v1.0 2016年5月3日 上午11:02:14 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ipqam.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2016年5月3日-上午11:02:14
 * 
 */
@Alias("programIn")
public class ProgramIn implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -1040516458532353323L;

    private Long entityId;
    private Long cmcId;
    private Long portId;
    private Long sessionId;
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.1.6.1.1", index = true)
    private Long mpegInputUdpOriginationIndex;// 同mpegVideoSessionIndex.
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.1.6.1.2", index = true)
    private Long mpegInputUdpOriginationId;// 固定为1
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.1.6.1.5", type = "IpAddress")
    private String mpegInputUdpOriginationSrcInetAddr;// 输入流源地址
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.1.6.1.6", type = "IpAddress")
    private String mpegInputUdpOriginationDestInetAddr;// 输入流目的地址
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.1.6.1.7")
    private Long mpegInputUdpOriginationDestPort;// 输入流目的端口
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.1.6.1.8")
    private Long mpegInputUdpOriginationActive;// 同步
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.1.6.1.9")
    private Long mpegInputUdpOriginationPacketsDetected;// 检测

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public Long getMpegInputUdpOriginationIndex() {
        return mpegInputUdpOriginationIndex;
    }

    public void setMpegInputUdpOriginationIndex(Long mpegInputUdpOriginationIndex) {
        this.mpegInputUdpOriginationIndex = mpegInputUdpOriginationIndex;
    }

    public Long getMpegInputUdpOriginationId() {
        return mpegInputUdpOriginationId;
    }

    public void setMpegInputUdpOriginationId(Long mpegInputUdpOriginationId) {
        this.mpegInputUdpOriginationId = mpegInputUdpOriginationId;
    }

    public Long getMpegInputUdpOriginationDestPort() {
        return mpegInputUdpOriginationDestPort;
    }

    public void setMpegInputUdpOriginationDestPort(Long mpegInputUdpOriginationDestPort) {
        this.mpegInputUdpOriginationDestPort = mpegInputUdpOriginationDestPort;
    }

    public String getMpegInputUdpOriginationDestInetAddr() {
        return mpegInputUdpOriginationDestInetAddr;
    }

    public void setMpegInputUdpOriginationDestInetAddr(String mpegInputUdpOriginationDestInetAddr) {
        this.mpegInputUdpOriginationDestInetAddr = mpegInputUdpOriginationDestInetAddr;
    }

    public String getMpegInputUdpOriginationSrcInetAddr() {
        return mpegInputUdpOriginationSrcInetAddr;
    }

    public void setMpegInputUdpOriginationSrcInetAddr(String mpegInputUdpOriginationSrcInetAddr) {
        this.mpegInputUdpOriginationSrcInetAddr = mpegInputUdpOriginationSrcInetAddr;
    }

    public Long getMpegInputUdpOriginationActive() {
        return mpegInputUdpOriginationActive;
    }

    public void setMpegInputUdpOriginationActive(Long mpegInputUdpOriginationActive) {
        this.mpegInputUdpOriginationActive = mpegInputUdpOriginationActive;
    }

    public Long getMpegInputUdpOriginationPacketsDetected() {
        return mpegInputUdpOriginationPacketsDetected;
    }

    public void setMpegInputUdpOriginationPacketsDetected(Long mpegInputUdpOriginationPacketsDetected) {
        this.mpegInputUdpOriginationPacketsDetected = mpegInputUdpOriginationPacketsDetected;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

}
