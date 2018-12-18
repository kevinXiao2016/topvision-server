/***********************************************************************
 * $Id: CmRemoteQuery.java,v1.0 2014-1-27 上午9:50:39 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.remotequerycm.facade.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author YangYi
 * @created @2014-1-27-上午9:50:39
 * 
 */
public class Cm2RemoteQuery implements Serializable {
    private static final long serialVersionUID = -3139735259903684499L;
    private final static DecimalFormat df = new DecimalFormat("0.0");
    private Long cmcId; // 所属Cmc的Id
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.4.1.1", index = true)
    private Long cmIndex; // CM的Index
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.4.1.2")
    private Long status; // false表示获取失败，一般是cm下线了，或者关闭了remotequery
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.4.1.3")
    private Long cmRxPower; // 下行接收电平
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.4.1.4")
    private Long cmTxPower; // 上行发射电平
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.4.1.5")
    private Long cmSignalNoise; // 下行信噪比

    private String cmRxPowerString;// 下行接收电平,转换后
    private String cmTxPowerString;// 上行发射电平,转换后
    private String cmSignalNoiseString; // 下行信噪比,转换后

    public String getCmSignalNoiseString() {
        if (this.getCmSignalNoise() != null) {
            cmSignalNoiseString = df.format((double) this.getCmSignalNoise() / 10);
        } else {
            cmSignalNoiseString = "";
        }
        return cmSignalNoiseString;
    }

    public void setCmSignalNoiseString(String cmSignalNoiseString) {
        this.cmSignalNoiseString = cmSignalNoiseString;
    }

    public String getCmTxPowerString() {
        if (this.getCmTxPower() != null) {
            cmTxPowerString = df.format((double) this.getCmTxPower() / 10);
        } else {
            cmTxPowerString = "";
        }
        return cmTxPowerString;
    }

    public void setCmTxPowerString(String cmTxPowerString) {
        this.cmTxPowerString = cmTxPowerString;
    }

    public String getCmRxPowerString() {
        if (this.getCmRxPower() != null) {
            cmRxPowerString = df.format((double) this.getCmRxPower() / 10);
        } else {
            cmRxPowerString = "";
        }
        return cmRxPowerString;
    }

    public void setCmRxPowerString(String cmRxPowerString) {
        this.cmRxPowerString = cmRxPowerString;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    public Long getCmRxPower() {
        return cmRxPower;
    }

    public void setCmRxPower(Long cmRxPower) {
        this.cmRxPower = cmRxPower;
    }

    public Long getCmTxPower() {
        return cmTxPower;
    }

    public void setCmTxPower(Long cmTxPower) {
        this.cmTxPower = cmTxPower;
    }

    public Long getCmSignalNoise() {
        return cmSignalNoise;
    }

    public void setCmSignalNoise(Long cmSignalNoise) {
        this.cmSignalNoise = cmSignalNoise;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Cm2RemoteQuery{" +
                "cmcId=" + cmcId +
                ", cmIndex=" + cmIndex +
                ", status=" + status +
                ", cmRxPower=" + cmRxPower +
                ", cmTxPower=" + cmTxPower +
                ", cmSignalNoise=" + cmSignalNoise +
                ", cmRxPowerString='" + cmRxPowerString + '\'' +
                ", cmTxPowerString='" + cmTxPowerString + '\'' +
                ", cmSignalNoiseString='" + cmSignalNoiseString + '\'' +
                '}';
    }
}
