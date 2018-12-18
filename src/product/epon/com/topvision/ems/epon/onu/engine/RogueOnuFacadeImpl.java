/***********************************************************************
 * $Id: RogueOnuFacadeImpl.java,v1.0 2017年6月17日 下午4:03:12 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.onu.domain.TopOnuLaserEntry;
import com.topvision.ems.epon.onu.domain.TopPonPortRogueEntry;
import com.topvision.ems.epon.onu.domain.TopSystemRogueCheck;
import com.topvision.ems.epon.onu.facade.RogueOnuFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2017年6月17日-下午4:03:12
 *
 */
@Facade("rogueOnuFacade")
public class RogueOnuFacadeImpl extends EmsFacade implements RogueOnuFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public Integer getOltRogueSwitch(SnmpParam snmpParam) {
        return Integer.parseInt(snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.2.3.1.13.1.0"));
    }

    @Override
    public void setOltRogueSwitch(SnmpParam snmpParam, TopSystemRogueCheck systemRogueCheck) {
        snmpExecutorService.setData(snmpParam, systemRogueCheck);
    }

    @Override
    public List<TopPonPortRogueEntry> getPonPortRogueOnuList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopPonPortRogueEntry.class);
    }

    @Override
    public TopPonPortRogueEntry getPonPortRogueOnuInfo(SnmpParam snmpParam, TopPonPortRogueEntry topPonPortRogueEntry) {
        return snmpExecutorService.getTableLine(snmpParam, topPonPortRogueEntry);
    }

    @Override
    public void setPonPortRogueSwitch(SnmpParam snmpParam, TopPonPortRogueEntry topPonPortRogueEntry) {
        snmpExecutorService.setData(snmpParam, topPonPortRogueEntry);
    }

    @Override
    public void setPonPortRogueOnuCheck(SnmpParam snmpParam, TopPonPortRogueEntry topPonPortRogueEntry) {
        snmpExecutorService.setData(snmpParam, topPonPortRogueEntry);
    }

    @Override
    public List<TopOnuLaserEntry> getOnuLaserEntry(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopOnuLaserEntry.class);
    }

    @Override
    public void setOnuLaserSwitch(SnmpParam snmpParam, TopOnuLaserEntry topOnuLaserEntry) {
        snmpExecutorService.setData(snmpParam, topOnuLaserEntry);
    }

    @Override
    public TopOnuLaserEntry getOnuLaserSwitch(SnmpParam snmpParam, TopOnuLaserEntry topOnuLaserEntry) {
        return snmpExecutorService.getTableLine(snmpParam, topOnuLaserEntry);
    }

}
