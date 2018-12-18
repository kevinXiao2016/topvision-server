/***********************************************************************
 * $Id: CmcOpReceiverRfCfg.java,v1.0 2013-12-2 下午2:11:49 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.optical.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverDcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverInputInfo;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfCfg;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfPort;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverSwitchCfg;

/**
 * @author dosion
 * @created @2013-12-2-下午2:11:49
 * 
 */
public class CmcOpReceiverStatus implements Serializable {
    private static final long serialVersionUID = 3784533831663236148L;
    private Long cmcId;
    private String dorType;
    private Integer outputLevel;
    private Byte switchState;
    private Integer acPowerVoltage;
    private Integer channelNum;
    private List<CmcOpReceiverInputInfo> inputInfoList;
    private List<CmcOpReceiverDcPower> dcPower;
    private List<CmcOpReceiverRfPort> rfPort;
    private CmcOpReceiverRfCfg rfCfg;
    private CmcOpReceiverSwitchCfg switchCfg;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getOutputLevel() {
        return outputLevel;
    }

    public void setOutputLevel(Integer outputLevel) {
        this.outputLevel = outputLevel;
    }

    public Byte getSwitchState() {
        return switchState;
    }

    public void setSwitchState(Byte switchState) {
        this.switchState = switchState;
    }

    public Integer getAcPowerVoltage() {
        return acPowerVoltage;
    }

    public void setAcPowerVoltage(Integer acPowerVoltage) {
        this.acPowerVoltage = acPowerVoltage;
    }

    public List<CmcOpReceiverDcPower> getDcPower() {
        return dcPower;
    }

    public void setDcPower(List<CmcOpReceiverDcPower> dcPower) {
        this.dcPower = dcPower;
    }

    public Integer getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(Integer channelNum) {
        this.channelNum = channelNum;
    }

    public List<CmcOpReceiverRfPort> getRfPort() {
        return rfPort;
    }

    public void setRfPort(List<CmcOpReceiverRfPort> rfPort) {
        this.rfPort = rfPort;
    }

    public List<CmcOpReceiverInputInfo> getInputInfoList() {
        return inputInfoList;
    }

    public void setInputInfoList(List<CmcOpReceiverInputInfo> inputInfoList) {
        this.inputInfoList = inputInfoList;
    }

    public CmcOpReceiverRfCfg getRfCfg() {
        return rfCfg;
    }

    public void setRfCfg(CmcOpReceiverRfCfg rfCfg) {
        this.rfCfg = rfCfg;
    }

    public CmcOpReceiverSwitchCfg getSwitchCfg() {
        return switchCfg;
    }

    public void setSwitchCfg(CmcOpReceiverSwitchCfg switchCfg) {
        this.switchCfg = switchCfg;
    }

    public String getDorType() {
        return dorType;
    }

    public void setDorType(String dorType) {
        this.dorType = dorType;
    }

}
