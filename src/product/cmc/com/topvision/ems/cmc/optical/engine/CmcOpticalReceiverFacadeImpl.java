/***********************************************************************
 * $Id: CmcOpticalReceiverFacadeImpl.java,v1.0 2013-12-2 下午4:52:09 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.optical.engine;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.optical.facade.CmcOpticalReceiverFacade;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverAcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverChannelNum;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverDcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverInputInfo;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfCfg;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfPort;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverSwitchCfg;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverType;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author dosion
 * @created @2013-12-2-下午4:52:09
 * 
 */
@Facade("cmcOpticalReceiverFacade")
public class CmcOpticalReceiverFacadeImpl extends EmsFacade implements CmcOpticalReceiverFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public CmcOpReceiverRfCfg getOpReceiverRfCfg(SnmpParam snmpParam, Integer index) {
        CmcOpReceiverRfCfg cmcOpReceiverRfCfg = new CmcOpReceiverRfCfg();
        cmcOpReceiverRfCfg.setOutputIndex(index);
        return snmpExecutorService.getTableLine(snmpParam, cmcOpReceiverRfCfg);
    }

    @Override
    public CmcOpReceiverSwitchCfg getOpReceiverSwitchCfg(SnmpParam snmpParam, Integer index) {
        CmcOpReceiverSwitchCfg cmcOpReceiverSwitchCfg = new CmcOpReceiverSwitchCfg();
        cmcOpReceiverSwitchCfg.setSwitchIndex(index);
        return snmpExecutorService.getTableLine(snmpParam, cmcOpReceiverSwitchCfg);
    }

    @Override
    public CmcOpReceiverInputInfo getOpReceiverInputInfo(SnmpParam snmpParam, Integer index) {
        CmcOpReceiverInputInfo cmcOpReceiverInputInfo = new CmcOpReceiverInputInfo();
        cmcOpReceiverInputInfo.setInputIndex(index);
        return snmpExecutorService.getTableLine(snmpParam, cmcOpReceiverInputInfo);
    }

    @Override
    public CmcOpReceiverAcPower getOpReceiverAcPower(SnmpParam snmpParam, Integer index) {
        CmcOpReceiverAcPower cmcOpReceiverAcPower = new CmcOpReceiverAcPower();
        cmcOpReceiverAcPower.setPowerIndex(index);
        return snmpExecutorService.getTableLine(snmpParam, cmcOpReceiverAcPower);
    }

    @Override
    public CmcOpReceiverDcPower getOpReceiverDcPower(SnmpParam snmpParam, Integer index) {
        CmcOpReceiverDcPower cmcOpReceiverDcPower = new CmcOpReceiverDcPower();
        cmcOpReceiverDcPower.setPowerIndex(index);
        return snmpExecutorService.getTableLine(snmpParam, cmcOpReceiverDcPower);
    }

    @Override
    public CmcOpReceiverRfCfg setOpReceiverRfCfg(SnmpParam snmpParam, CmcOpReceiverRfCfg cmcOpReceiverRfCfg) {
        return snmpExecutorService.setData(snmpParam, cmcOpReceiverRfCfg);
    }

    @Override
    public CmcOpReceiverSwitchCfg setOpReceiverSwitchCfg(SnmpParam snmpParam,
            CmcOpReceiverSwitchCfg cmcOpReceiverSwitchCfg) {
        return snmpExecutorService.setData(snmpParam, cmcOpReceiverSwitchCfg);
    }

    @Override
    public CmcOpReceiverRfPort getOpReceiverRfPort(SnmpParam snmpParam, Integer index) {
        CmcOpReceiverRfPort rfPort = new CmcOpReceiverRfPort();
        rfPort.setRfPortIndex(index);
        return snmpExecutorService.getTableLine(snmpParam, rfPort);
    }

    @Override
    public CmcOpReceiverChannelNum getOpReceiverChannelNum(SnmpParam snmpParam, Integer index) {
        CmcOpReceiverChannelNum channelNum = new CmcOpReceiverChannelNum();
        channelNum.setChannelNumIndex(index);
        return snmpExecutorService.getTableLine(snmpParam, channelNum);
    }

    @Override
    public CmcOpReceiverType getOpReceiverType(SnmpParam snmpParam, Integer index) {
        CmcOpReceiverType cmcOpReceiverType = new CmcOpReceiverType();
        cmcOpReceiverType.setDorDevTypeIndex(index);
        return snmpExecutorService.getTableLine(snmpParam, cmcOpReceiverType);
    }

}
