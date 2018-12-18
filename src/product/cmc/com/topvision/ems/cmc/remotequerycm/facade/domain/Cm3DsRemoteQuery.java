/***********************************************************************
 * $Id: Cm3RemoteQuery.java,v1.0 2014-2-8 下午7:11:46 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.remotequerycm.facade.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * CM 3.0 下行信道 RemoteQuery方式查询 Domain
 * 
 * @author YangYi
 * @created @2014-2-8-下午7:11:46
 * 
 */
public class Cm3DsRemoteQuery implements Serializable, Comparable<Cm3DsRemoteQuery> {
    private static final long serialVersionUID = -6555344799539128417L;                     
    private final static DecimalFormat df = new DecimalFormat("0.00");
    private Long cmcId; // 所属Cmc的Id
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.4.1.1", index = true)
    private Long cmIndex; // CM的Index
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.6.1.1", index = true)
    private Long cmDsIndex; // CM下行信道的Index
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.6.1.2")
    private Long cmDsChanId; // 信道ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.6.1.3")
    private Long cmDsRxPower; // 3.0CM 下行接收电平
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.6.1.4")
    private Long cmDsSignalNoise; // 3.0CM 下行信道信噪比

    private String cmDsRxPowerString; // 下行接收电平,转换后
    private String cmDsRxPowerStringFordBmV; // 下行接收电平,转换后
    private String cmDsSignalNoiseString; // 下行信道信噪比,转换后

    @Override
    public int compareTo(Cm3DsRemoteQuery o) {
        if (o != null) {
            if (this.getCmDsChanId().longValue() > o.getCmDsChanId().longValue()) {
                return 1;
            } else if (this.getCmDsChanId().longValue() == o.getCmDsChanId().longValue()) {
                return 0;
            }
        }
        return -1;
    }

    public String getCmDsRxPowerString() {
        if (this.getCmDsRxPower() != null) {
            // cmDsRxPowerString = df.format((double) this.getCmDsRxPower() / 10);
            double powerValue = UnitConfigConstant.parsePowerValue((double) this.getCmDsRxPower() / 10);
            cmDsRxPowerString = df.format(powerValue);
        } else {
            cmDsRxPowerString = "";
        }
        return cmDsRxPowerString;
    }

    public void setCmDsRxPowerString(String cmDsRxPowerString) {
        this.cmDsRxPowerString = cmDsRxPowerString;
    }

    public String getCmDsRxPowerStringFordBmV() {
        if (this.getCmDsRxPower() != null) {
            cmDsRxPowerStringFordBmV = df.format((double) this.getCmDsRxPower() / 10);
        } else {
            cmDsRxPowerStringFordBmV = "";
        }
        return cmDsRxPowerStringFordBmV;
    }

    public void setCmDsRxPowerStringFordBmV(String cmDsRxPowerStringFordBmV) {
        this.cmDsRxPowerStringFordBmV = cmDsRxPowerStringFordBmV;
    }

    public String getCmDsSignalNoiseString() {
        if (this.getCmDsSignalNoise() != null) {
            cmDsSignalNoiseString = df.format((double) this.getCmDsSignalNoise() / 10);
        } else {
            cmDsSignalNoiseString = "";
        }
        return cmDsSignalNoiseString;
    }

    public void setCmDsSignalNoiseString(String cmDsSignalNoiseString) {
        this.cmDsSignalNoiseString = cmDsSignalNoiseString;
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

    public Long getCmDsIndex() {
        return cmDsIndex;
    }

    public void setCmDsIndex(Long cmDsIndex) {
        this.cmDsIndex = cmDsIndex;
    }

    public Long getCmDsChanId() {
        return cmDsChanId;
    }

    public void setCmDsChanId(Long cmDsChanId) {
        this.cmDsChanId = cmDsChanId;
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

}
