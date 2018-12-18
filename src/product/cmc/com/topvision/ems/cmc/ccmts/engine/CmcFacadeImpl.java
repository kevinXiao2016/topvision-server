/***********************************************************************
 * $Id: CmcFacadeImpl.java,v1.0 2011-7-1 下午02:56:35 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.engine;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.ccmts.facade.CmcFacade;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcDevSoftware;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcReplaceEntry;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcSysControl;
import com.topvision.ems.cmc.facade.domain.CmcSystemIpInfo;
import com.topvision.ems.cmc.performance.dbsaver.FlapDBSaver;
import com.topvision.ems.cmc.performance.domain.CMFlapHis;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-7-1-下午02:56:35
 * 
 */
@Facade("ccmtsCmcFacade")
public class CmcFacadeImpl extends EmsFacade implements CmcFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public CmcSysControl cmcSysControlSetWithotAgent(SnmpParam dolSnmpParam, CmcSysControl cmcSysControl) {
        return snmpExecutorService.setData(dolSnmpParam, cmcSysControl);
    }

    @Override
    public CmcDevSoftware updateCmc(SnmpParam snmpParam, CmcDevSoftware cmcDevSoftware) {
        CmcDevSoftware afterModified = snmpExecutorService.setData(snmpParam, cmcDevSoftware);
        logger.debug("\n\nSet CmcDevSoftware:{}", cmcDevSoftware);
        return afterModified;
    }

    @Override
    public Integer getCmcUpdateProgress(SnmpParam snmpParam) {
        return Integer.parseInt(snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.5.1.9.3.0"));
    }

    @Override
    public String getChannelUtilizationInteInterval(SnmpParam dolSnmpParam) {
        return snmpExecutorService.get(dolSnmpParam, "1.3.6.1.2.1.10.127.1.3.8.0");
    }

    @Override
    public void setChannelUtilizationInteInterval(SnmpParam dolSnmpParam, Long period) {
        snmpExecutorService.set(dolSnmpParam, "1.3.6.1.2.1.10.127.1.3.8.0", period.toString());
    }

    @Override
    public CmcSystemIpInfo getCmcSystemIpInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, CmcSystemIpInfo.class);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmc.ccmts.facade.CmcFacade#replaceCmcEntry(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.cmc.ccmts.domain.CmcReplaceEntry)
     */
    @Override
    public void replaceCmcEntry(SnmpParam snmpParam, CmcReplaceEntry cmcReplaceEntry) {
        snmpExecutorService.setData(snmpParam, cmcReplaceEntry);
    }

    public void clearCmcFlap(SnmpParam snmpParam, Long cmcIndex) {
        snmpExecutorService.set(snmpParam, CmcConstants.TOPCCMTSCMFLAPCLEAR + "." + cmcIndex, "1");
    }

    @Override
    public void clearCmcAllCmFlap(Long cmcId) {
        Map<String, CMFlapHis> cmflapMap = FlapDBSaver.lastStaticMap.get(cmcId);
        if (cmflapMap != null) {
            for (CMFlapHis flapHis : cmflapMap.values()) {
                flapHis.setInsFailNum(0);
                flapHis.setHitNum(0);
                flapHis.setMissNum(0);
                flapHis.setCrcErrorNum(0);
                flapHis.setPowerAdjLowerNum(0);
                flapHis.setPowerAdjHigherNum(0);
                flapHis.setIncreaseInsNum(0);
                flapHis.setIncreaseHitPercent(0.0f);
                flapHis.setIncreasePowerAdjNum(0);
            }
        }
    }
}
