/***********************************************************************
 * $Id: SpectrumFacadeImpl.java,v1.0 2014-1-13 上午9:22:22 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.engine;

import java.util.List;

import com.topvision.ems.cmc.spectrum.facade.SpectrumFacade;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumCfg;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumCmtsSwitch;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumOltSwitch;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.domain.PhysAddress;
import com.topvision.framework.common.MacUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author haojie
 * @created @2014-1-13-上午9:22:22
 *
 */
@Facade("spectrumFacade")
public class SpectrumFacadeImpl extends EmsFacade implements SpectrumFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    public void spectrumCfg(SnmpParam snmpParam, SpectrumCfg spectrumCfg) {
        snmpExecutorService.setData(snmpParam, spectrumCfg);
    }

    public boolean getOltSwitchStatus(SnmpParam snmpParam) {
        String status = snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.16.1.1.0");
        return status != null && status.equalsIgnoreCase("1");
    }

    public boolean getCmcSwitchStatus(SnmpParam snmpParam, String cmcMac) {
        SpectrumCmtsSwitch spectrumCmtsSwitch = new SpectrumCmtsSwitch();
        spectrumCmtsSwitch.setFftMacIndex(new PhysAddress(MacUtils.convertToMaohaoFormat(cmcMac)));
        SpectrumCmtsSwitch scs = snmpExecutorService.getTableLine(snmpParam, spectrumCmtsSwitch);
        return scs != null && scs.getFftMonitorPollingStatus() != null && scs.getFftMonitorPollingStatus() == 1;
    }

    @Override
    public List<SpectrumCfg> getSpectrumCfg(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, SpectrumCfg.class);
    }

    @Override
    public void startSpectrumSwitchOlt(SnmpParam snmpParam) {
        snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.16.1.1.0", "1");
    }

    @Override
    public void stopSpectrumSwitchOlt(SnmpParam snmpParam) {
        snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.16.1.1.0", "0");
    }

    @Override
    public SpectrumOltSwitch getSpectrumOltSwitch(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, SpectrumOltSwitch.class);
    }
}
