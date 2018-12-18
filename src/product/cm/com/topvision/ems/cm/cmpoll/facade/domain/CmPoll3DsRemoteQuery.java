/***********************************************************************
 * $Id: CmPoll3DsRemoteQuery.java,v1.0 2015年3月21日 上午10:05:39 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author loyal
 * @created @2015年3月21日-上午10:05:39
 * 
 */
public class CmPoll3DsRemoteQuery implements Serializable {
    private static final long serialVersionUID = -3560646985491329439L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.4.1.1", index = true)
    private Long cmIndex; // CM的Index
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.6.1.1", index = true)
    private Long cmDsIndex; // CM下行信道的Index
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.6.1.2")
    private Integer cmDsId; // CM下行信道的Id
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.6.1.3")
    private Long cmDsRxPower; // 3.0CM 下行接收电平
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.6.1.4")
    private Long cmDsSignalNoise; // 3.0CM 下行信道信噪比

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    public Long getCmDsIndex() {
        return cmDsIndex;
    }

    public void setCmDsIndex(Long cmDsIndex) {
        this.cmDsIndex = cmDsIndex;
    }

    public Long getCmDsRxPower() {
        return cmDsRxPower;
    }

    public void setCmDsRxPower(Long cmDsRxPower) {
        this.cmDsRxPower = cmDsRxPower;
    }

    public Long getCmDsSignalNoise() {
        return cmDsSignalNoise;
    }

    public void setCmDsSignalNoise(Long cmDsSignalNoise) {
        this.cmDsSignalNoise = cmDsSignalNoise;
    }

    public Integer getCmDsId() {
        return cmDsId;
    }

    public void setCmDsId(Integer cmDsId) {
        this.cmDsId = cmDsId;
    }

    @Override
    public String toString() {
        return "CmPoll3DsRemoteQuery{" +
                "cmIndex=" + cmIndex +
                ", cmDsIndex=" + cmDsIndex +
                ", cmDsId=" + cmDsId +
                ", cmDsRxPower=" + cmDsRxPower +
                ", cmDsSignalNoise=" + cmDsSignalNoise +
                '}';
    }
}
