/***********************************************************************
 * $Id: Program.java,v1.0 2016年5月6日 下午5:13:08 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ipqam.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2016年5月6日-下午5:13:08
 * 
 */
@Alias("program")
public class Program implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6304266331894276070L;
    private Long entityId;
    private Long cmcId;
    private String cmtsName;
    private Long portId;
    private Long channelId;
    private Long ifIndex;
    private Long sessionId;
    private Long mpegVideoSessionIndex;
    private Long mpegVideoSessionID; // 会话ID
    private String mpegInputUdpOriginationSrcInetAddr;// 输入流源地址
    private String mpegInputUdpOriginationDestInetAddr;// 输入流目的地址
    private Integer mpegInputUdpOriginationDestPort;// 输入流目的端口
    private Integer mpegInputUdpOriginationPacketsDetected;// 检测
    private Integer mpegInputUdpOriginationActive;// 同步
    private Long mpegOutputProgNo;// 输出节目号
    private String mpegOutputProgPmtVersion;// 输出节目号
    private Long mpegOutputProgPmtPid;// 输出PMT PID
    private Long mpegOutputProgPcrPid;// 输出PCR PID
    private Integer mpegOutputProgNumElems;// 输出TS流成员数量
    private Long mpegVideoSessionBitRate;// 输出码率

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

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getMpegVideoSessionIndex() {
        return mpegVideoSessionIndex;
    }

    public void setMpegVideoSessionIndex(Long mpegVideoSessionIndex) {
        this.mpegVideoSessionIndex = mpegVideoSessionIndex;
    }

    public Long getMpegVideoSessionID() {
        return mpegVideoSessionID;
    }

    public void setMpegVideoSessionID(Long mpegVideoSessionID) {
        this.mpegVideoSessionID = mpegVideoSessionID;
    }

    public String getMpegInputUdpOriginationSrcInetAddr() {
        return mpegInputUdpOriginationSrcInetAddr;
    }

    public void setMpegInputUdpOriginationSrcInetAddr(String mpegInputUdpOriginationSrcInetAddr) {
        this.mpegInputUdpOriginationSrcInetAddr = mpegInputUdpOriginationSrcInetAddr;
    }

    public String getMpegInputUdpOriginationDestInetAddr() {
        return mpegInputUdpOriginationDestInetAddr;
    }

    public void setMpegInputUdpOriginationDestInetAddr(String mpegInputUdpOriginationDestInetAddr) {
        this.mpegInputUdpOriginationDestInetAddr = mpegInputUdpOriginationDestInetAddr;
    }

    public Integer getMpegInputUdpOriginationDestPort() {
        return mpegInputUdpOriginationDestPort;
    }

    public void setMpegInputUdpOriginationDestPort(Integer mpegInputUdpOriginationDestPort) {
        this.mpegInputUdpOriginationDestPort = mpegInputUdpOriginationDestPort;
    }

    public Integer getMpegInputUdpOriginationPacketsDetected() {
        return mpegInputUdpOriginationPacketsDetected;
    }

    public void setMpegInputUdpOriginationPacketsDetected(Integer mpegInputUdpOriginationPacketsDetected) {
        this.mpegInputUdpOriginationPacketsDetected = mpegInputUdpOriginationPacketsDetected;
    }

    public Integer getMpegInputUdpOriginationActive() {
        return mpegInputUdpOriginationActive;
    }

    public void setMpegInputUdpOriginationActive(Integer mpegInputUdpOriginationActive) {
        this.mpegInputUdpOriginationActive = mpegInputUdpOriginationActive;
    }

    public Long getMpegOutputProgNo() {
        return mpegOutputProgNo;
    }

    public void setMpegOutputProgNo(Long mpegOutputProgNo) {
        this.mpegOutputProgNo = mpegOutputProgNo;
    }

    public String getMpegOutputProgPmtVersion() {
        return mpegOutputProgPmtVersion;
    }

    public void setMpegOutputProgPmtVersion(String mpegOutputProgPmtVersion) {
        this.mpegOutputProgPmtVersion = mpegOutputProgPmtVersion;
    }

    public Long getMpegOutputProgPmtPid() {
        return mpegOutputProgPmtPid;
    }

    public void setMpegOutputProgPmtPid(Long mpegOutputProgPmtPid) {
        this.mpegOutputProgPmtPid = mpegOutputProgPmtPid;
    }

    public Long getMpegOutputProgPcrPid() {
        return mpegOutputProgPcrPid;
    }

    public void setMpegOutputProgPcrPid(Long mpegOutputProgPcrPid) {
        this.mpegOutputProgPcrPid = mpegOutputProgPcrPid;
    }

    public Integer getMpegOutputProgNumElems() {
        return mpegOutputProgNumElems;
    }

    public void setMpegOutputProgNumElems(Integer mpegOutputProgNumElems) {
        this.mpegOutputProgNumElems = mpegOutputProgNumElems;
    }

    public Long getMpegVideoSessionBitRate() {
        return mpegVideoSessionBitRate;
    }

    public void setMpegVideoSessionBitRate(Long mpegVideoSessionBitRate) {
        this.mpegVideoSessionBitRate = mpegVideoSessionBitRate;
    }

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getCmtsName() {
        return cmtsName;
    }

    public void setCmtsName(String cmtsName) {
        this.cmtsName = cmtsName;
    }

}
