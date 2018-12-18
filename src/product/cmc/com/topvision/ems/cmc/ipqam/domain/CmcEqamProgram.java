/***********************************************************************
 * $Id: CmcEqamProgram.java,v1.0 2016年5月3日 上午11:01:59 $
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
 * @created @2016年5月3日-上午11:01:59
 * 
 */
@Alias("cmcEqamProgram")
public class CmcEqamProgram implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6065746593926220018L;
    private Long entityId;
    private Long cmcId;
    private Long portId;

    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.4.1.1", index = true)
    private Long mpegVideoSessionIndex;// 编码类似于ifIndex，低8位表示会话index，从1开始
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.4.1.9")
    private Long mpegVideoSessionBitRate;// 输出码率
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.4.1.1.4.1.10")
    private String mpegVideoSessionID;// 会话ID

    public Long getMpegVideoSessionIndex() {
        return mpegVideoSessionIndex;
    }

    public void setMpegVideoSessionIndex(Long mpegVideoSessionIndex) {
        this.mpegVideoSessionIndex = mpegVideoSessionIndex;
    }

    public String getMpegVideoSessionID() {
        return mpegVideoSessionID;
    }

    public void setMpegVideoSessionID(String mpegVideoSessionID) {
        this.mpegVideoSessionID = mpegVideoSessionID;
    }

    public Long getMpegVideoSessionBitRate() {
        return mpegVideoSessionBitRate;
    }

    public void setMpegVideoSessionBitRate(Long mpegVideoSessionBitRate) {
        this.mpegVideoSessionBitRate = mpegVideoSessionBitRate;
    }

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

}
