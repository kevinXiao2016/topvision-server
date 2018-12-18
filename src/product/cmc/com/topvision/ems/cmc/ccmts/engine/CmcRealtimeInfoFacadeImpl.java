/***********************************************************************
 * $ CmcRealtimeInfoFacadeImpl.java,v1.0 14-5-11 下午2:57 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.ccmts.facade.CmcRealtimeInfoFacade;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcCmNum;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcCpeTypeNum;
import com.topvision.ems.cmc.ccmts.facade.domain.CmtsCmQuality;
import com.topvision.ems.cmc.ccmts.facade.domain.DownChannelTxPower;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm2RemoteQuery;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author jay
 * @created @14-5-11-下午2:57
 */
@Facade("cmcRealtimeInfoFacadeImpl")
public class CmcRealtimeInfoFacadeImpl extends EmsFacade implements CmcRealtimeInfoFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public List<CmtsCmQuality> getCmtsCmQualitys(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmtsCmQuality.class);
    }

    @Override
    public List<Cm2RemoteQuery> getCm2RemoteQuerys(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, Cm2RemoteQuery.class);
    }

    @Override
    public List<DownChannelTxPower> getDownChannelTxPowers(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, DownChannelTxPower.class);
    }

    @Override
    public CmcCpeTypeNum getCmcCpeTypeNum(SnmpParam snmpParam, Long cmcIndex) {
        CmcCpeTypeNum cmcCpeTypeNum = new CmcCpeTypeNum();
        cmcCpeTypeNum.setIfIndex(cmcIndex);
        return snmpExecutorService.getTableLine(snmpParam, cmcCpeTypeNum);
    }

    @Override
    public CmcCmNum getCmcCmNum(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, CmcCmNum.class);
    }

    @Override
    public String[] getCmcSnr(SnmpParam snmpParam, String[] oids) {
        return snmpExecutorService.get(snmpParam, oids);
    }

    @Override
    public void setRealTimeSnmpDataStatus(SnmpParam snmpParam, String state) {
        try {
            snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.18.2.1.0", state);
        } catch (Exception e) {
            logger.debug("set topUpChannelSignalQualityRealTimeSnmpDataStatus error", e);
        }
    }

}
