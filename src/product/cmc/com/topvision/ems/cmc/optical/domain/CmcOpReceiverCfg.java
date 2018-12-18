/***********************************************************************
 * $Id: CmcOpReceiverRfCfg.java,v1.0 2013-12-2 下午2:11:49 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.optical.domain;

import java.io.Serializable;

/**
 * @author dosion
 * @created @2013-12-2-下午2:11:49
 * 
 */
public class CmcOpReceiverCfg implements Serializable {
    private static final long serialVersionUID = 3784533831663236148L;
    private Integer dorType;
    private Long cmcId;
    private Integer outputIndex; // topCcmtsOpRxOutputTable表索引
    private Byte outputControl; // 下行链路开关（OFF:1, ON:2）
    private Byte outputGainType; // 增益类型(1.constantLevel;2.constantGain;-1:不支持增益类型)
    private Integer outputLevel;
    private Integer outputAGCOrigin; // AGC起始功率(topCcmtsDorConfigurationAGCOrigin)
    private Integer outputRFlevelatt; // 射频衰减量
    private Integer switchIndex;
    private Byte switchControl; // 开关控制(指的是优先A/优先B...)
    private Integer switchThres; // 转换门限
    private Byte switchState;

    private Float outputLevelUnit;
    private Float outputRFlevelattUnit; // 输出电平衰减量(topCcmtsDorConfigurationOutputRFlevelatt)

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getOutputIndex() {
        return outputIndex;
    }

    public void setOutputIndex(Integer outputIndex) {
        this.outputIndex = outputIndex;
    }

    public Byte getOutputControl() {
        return outputControl;
    }

    public void setOutputControl(Byte outputControl) {
        this.outputControl = outputControl;
    }

    public Byte getOutputGainType() {
        return outputGainType;
    }

    public void setOutputGainType(Byte outputGainType) {
        this.outputGainType = outputGainType;
    }

    public Integer getOutputLevel() {
        return outputLevel;
    }

    public void setOutputLevel(Integer outputLevel) {
        this.outputLevel = outputLevel;
        if (outputLevel != null) {
            outputLevelUnit = (float) outputLevel / 10;
        } else {
            outputLevelUnit = null;
        }
    }

    public Integer getOutputAGCOrigin() {
        return outputAGCOrigin;
    }

    public void setOutputAGCOrigin(Integer outputAGCOrigin) {
        this.outputAGCOrigin = outputAGCOrigin;
    }

    public Integer getOutputRFlevelatt() {
        return outputRFlevelatt;
    }

    public void setOutputRFlevelatt(Integer outputRFlevelatt) {
        this.outputRFlevelatt = outputRFlevelatt;
        if (outputRFlevelatt != null) {
            outputRFlevelattUnit = (float) outputRFlevelatt / 10;
        } else {
            outputRFlevelattUnit = null;
        }
    }

    public Integer getSwitchIndex() {
        return switchIndex;
    }

    public void setSwitchIndex(Integer switchIndex) {
        this.switchIndex = switchIndex;
    }

    public Byte getSwitchControl() {
        return switchControl;
    }

    public void setSwitchControl(Byte switchControl) {
        this.switchControl = switchControl;
    }

    public Integer getSwitchThres() {
        return switchThres;
    }

    public void setSwitchThres(Integer switchThres) {
        this.switchThres = switchThres;
    }

    public Byte getSwitchState() {
        return switchState;
    }

    public void setSwitchState(Byte switchState) {
        this.switchState = switchState;
    }

    public Float getOutputLevelUnit() {
        return outputLevelUnit;
    }

    public void setOutputLevelUnit(Float outputLevelUnit) {
        this.outputLevelUnit = outputLevelUnit;
        if (outputLevelUnit != null) {
            outputLevel = (int) (outputLevelUnit * 10);
        } else {
            outputLevel = null;
        }

    }

    public Float getOutputRFlevelattUnit() {
        return outputRFlevelattUnit;
    }

    public void setOutputRFlevelattUnit(Float outputRFlevelattUnit) {
        this.outputRFlevelattUnit = outputRFlevelattUnit;
        if (outputRFlevelattUnit != null) {
            outputRFlevelatt = (int) (outputRFlevelattUnit * 10);
        } else {
            outputRFlevelatt = null;
        }
    }

    public Integer getDorType() {
        return dorType;
    }

    public void setDorType(Integer dorType) {
        this.dorType = dorType;
    }

}
