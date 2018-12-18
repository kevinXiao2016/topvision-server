/***********************************************************************
 * $Id: CmcOpticalReceiverPerfResult.java,v1.0 2013-12-16 上午11:51:32 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.List;

import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverDcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverAcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverInputInfo;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfPort;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author dosion
 * @created @2013-12-16-上午11:51:32
 * 
 */
public class CmcOpticalReceiverPerfResult extends PerformanceResult<OperClass> {
    private static final long serialVersionUID = 6287319433826555340L;
    private List<CmcOpReceiverInputInfo> inputInfo;
    private CmcOpReceiverAcPower acPower;
    private List<CmcOpReceiverDcPower> dcPowerList;
    private List<CmcOpReceiverRfPort> rfPortList;

    public CmcOpticalReceiverPerfResult(OperClass domain) {
        super(domain);
    }

    public List<CmcOpReceiverInputInfo> getInputInfo() {
        return inputInfo;
    }

    public void setInputInfo(List<CmcOpReceiverInputInfo> inputInfo) {
        this.inputInfo = inputInfo;
    }

    public CmcOpReceiverAcPower getAcPower() {
        return acPower;
    }

    public void setAcPower(CmcOpReceiverAcPower acPower) {
        this.acPower = acPower;
    }

    public List<CmcOpReceiverDcPower> getDcPowerList() {
        return dcPowerList;
    }

    public void setDcPowerList(List<CmcOpReceiverDcPower> dcPowerList) {
        this.dcPowerList = dcPowerList;
    }

    public List<CmcOpReceiverRfPort> getRfPortList() {
        return rfPortList;
    }

    public void setRfPortList(List<CmcOpReceiverRfPort> rfPortList) {
        this.rfPortList = rfPortList;
    }

}
