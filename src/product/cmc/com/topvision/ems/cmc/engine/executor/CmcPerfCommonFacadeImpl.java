/***********************************************************************
 * $Id: CmcPerfCommonFacadeImpl.java,v1.0 2013-12-2 上午10:03:10 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.engine.executor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.topvision.ems.cmc.facade.CmcPerfCommonFacade;
import com.topvision.ems.cmc.performance.facade.CmcServiceQuality;
import com.topvision.ems.cmc.performance.facade.CmcSignalQuality;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Rod John
 * @created @2013-12-2-上午10:03:10
 * 
 */
@Facade("cmcPerfCommonFacade")
public class CmcPerfCommonFacadeImpl extends EmsFacade implements CmcPerfCommonFacade {
    @Resource(name = "snmpExecutorService")
    private SnmpExecutorService snmpExecutorService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.facade.CmcPerfCommonFacade#fetchCmcServiceQuality(com.topvision.framework
     * .snmp.SnmpParam, java.lang.Long)
     */
    @Override
    public CmcServiceQuality fetchCmcServiceQuality(SnmpParam snmpParam, Long cmcIndex) {
        CmcServiceQuality quality = new CmcServiceQuality();
        quality.setCmcIndex(cmcIndex);
        return snmpExecutorService.getTableLine(snmpParam, quality);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.facade.CmcPerfCommonFacade#fetchCmcSignalQuality(com.topvision.framework
     * .snmp.SnmpParam, java.lang.Long, java.lang.Long)
     */
    @Override
    public CmcSignalQuality fetchCmcSignalQuality(SnmpParam snmpParam, Long cmcIndex, Long channelIndex) {
        CmcSignalQuality quality = new CmcSignalQuality();
        quality.setCmcIndex(cmcIndex);
        quality.setCmcChannelIndex(channelIndex);
        return snmpExecutorService.getTableLine(snmpParam, quality);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.facade.CmcPerfCommonFacade#fetchCmcSignalQuality(com.topvision.framework
     * .snmp.SnmpParam, java.lang.Long, java.util.List)
     */
    @Override
    public List<CmcSignalQuality> fetchCmcSignalQuality(SnmpParam snmpParam, Long cmcIndex, List<Long> channelIndexs) {
        List<CmcSignalQuality> signalQualities = new ArrayList<CmcSignalQuality>();
        for (Long channelIndex : channelIndexs) {
            CmcSignalQuality quality = new CmcSignalQuality();
            quality.setCmcIndex(cmcIndex);
            quality.setCmcChannelIndex(channelIndex);
            quality = snmpExecutorService.getTableLine(snmpParam, quality);
            signalQualities.add(quality);
        }
        return signalQualities;
    }

}
