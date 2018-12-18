/***********************************************************************
 * $Id: ProgramOut.java,v1.0 2016年5月3日 上午11:02:23 $
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
 * @created @2016年5月3日-上午11:02:23
 * 
 */
@Alias("programOut")
public class ProgramOut implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 2429489758496763548L;
    private Long cmcId;
    private Long entityId;
    private Long portId;
    private Long sessionId;
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.2.3.1.1", index = true)
    private Long mpegOutputTSIndex;// 同ifIndex
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.2.4.1.1", index = true)
    private Long mpegOutputProgIndex;// 同mpegVideoSession位，会话index
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.2.4.1.2")
    private Long mpegOutputProgNo; // 节目号
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.2.4.1.3")
    private Long mpegOutputProgPmtVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.2.4.1.4")
    private Long mpegOutputProgPmtPid;
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.2.4.1.5")
    private Long mpegOutputProgPcrPid;
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.2.4.1.7")
    private Long mpegOutputProgNumElems;// 输出TS流成员数量

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getMpegOutputProgNo() {
        return mpegOutputProgNo;
    }

    public void setMpegOutputProgNo(Long mpegOutputProgNo) {
        this.mpegOutputProgNo = mpegOutputProgNo;
    }

    public Long getMpegOutputProgPmtVersion() {
        return mpegOutputProgPmtVersion;
    }

    public void setMpegOutputProgPmtVersion(Long mpegOutputProgPmtVersion) {
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

    public Long getMpegOutputProgNumElems() {
        return mpegOutputProgNumElems;
    }

    public void setMpegOutputProgNumElems(Long mpegOutputProgNumElems) {
        this.mpegOutputProgNumElems = mpegOutputProgNumElems;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getMpegOutputTSIndex() {
        return mpegOutputTSIndex;
    }

    public void setMpegOutputTSIndex(Long mpegOutputTSIndex) {
        this.mpegOutputTSIndex = mpegOutputTSIndex;
    }

    public Long getMpegOutputProgIndex() {
        return mpegOutputProgIndex;
    }

    public void setMpegOutputProgIndex(Long mpegOutputProgIndex) {
        this.mpegOutputProgIndex = mpegOutputProgIndex;
    }

}
